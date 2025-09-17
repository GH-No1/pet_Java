package com.kmbeast.service.impl;

import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.ActiveNetMapper;
import com.kmbeast.mapper.PetMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.ActiveNetQueryDto;
import com.kmbeast.pojo.dto.PetQueryDto;
import com.kmbeast.pojo.em.ActiveNetType;
import com.kmbeast.pojo.em.IsAdoptEnum;
import com.kmbeast.pojo.em.IsRecommendEnum;
import com.kmbeast.pojo.entity.ActiveNet;
import com.kmbeast.pojo.entity.Pet;
import com.kmbeast.pojo.vo.PetListItemVO;
import com.kmbeast.pojo.vo.PetVO;
import com.kmbeast.pojo.vo.ScoreVO;
import com.kmbeast.service.PetService;
import com.kmbeast.utils.AssertUtils;
import com.kmbeast.utils.UserBasedCFUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PetServiceImpl implements PetService {


    @Resource
    private PetMapper petMapper;

    @Resource
    private ActiveNetMapper activeNetMapper;


    @Override
    public Result<String> save(Pet pet) {
        petParamCheck(pet);
        pet.setIsAdopt(IsAdoptEnum.NO_ADOPT.getStatus()); // 初始状态未领养
        pet.setCreateTime(LocalDateTime.now());
        petMapper.save(pet);
        return ApiResult.success("宠物信息新增成功");
    }

    //宠物信息校验
    private void petParamCheck(Pet pet) {
        AssertUtils.hasText(pet.getName(), "宠物名不能为空");
        AssertUtils.hasText(pet.getCover(), "请上传一张封面");
        AssertUtils.hasText(pet.getAddress(), "请补充宠物所在地");
        AssertUtils.notNull(pet.getPetTypeId(), "请选择宠物类型");

        AssertUtils.notNull(pet.getAge(), "请填写宠物年龄");
        AssertUtils.notNull(pet.getDetail(), "请填写宠物描述");
        AssertUtils.notNull(pet.getGender(), "请填写宠物性别");
        AssertUtils.notNull(pet.getIsVaccine(), "请选择宠物是否已经接种疫苗");
        AssertUtils.notNull(pet.getIsRecommend(), "请选择是否推荐");
    }

    @Override
    public Result<String> update(Pet pet) {
        petParamCheck(pet);
        petMapper.update(pet);
        return ApiResult.success("宠物信息修改成功");
    }

    @Override
    public Result<String> deleteById(Integer id) {
        petMapper.deleteById(id);
        return ApiResult.success("宠物信息删除成功");
    }


    //通过宠物ID查询宠物信息
    @Override
    public Result<PetVO> getById(Integer id) {
        PetVO petVO = petMapper.getById(id);
        // 浏览逻辑实现
        ActiveNetQueryDto activeNetQueryDto = new ActiveNetQueryDto();
        System.out.println("userId : " + LocalThreadHolder.getUserId()   );
        activeNetQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID
        activeNetQueryDto.setContentId(id); // 设置内容ID
        activeNetQueryDto.setContentType("PET"); // 标识查的是宠物模块类型
        activeNetQueryDto.setType(ActiveNetType.VIEW.getStatus()); // 声明为浏览类型
        Integer count = activeNetMapper.queryCount(activeNetQueryDto);
        if (count == 0) { // 证明用户没有针对宠物模块下面的宠物信息浏览过
            ActiveNet activeNet = new ActiveNet();
            activeNet.setUserId(LocalThreadHolder.getUserId());
            activeNet.setContentId(id);
            activeNet.setContentType("PET");
            activeNet.setType(ActiveNetType.VIEW.getStatus());
            activeNet.setCreateTime(LocalDateTime.now());
            activeNetMapper.insert(activeNet); // 浏览记录新增
        }
        return ApiResult.success(petVO);
    }

    //通过宠物ID查询宠物信息  未登录
    @Override
    public Result<PetVO> getByIdUn(Integer id) {
        PetVO petVO = petMapper.getById(id);

        return ApiResult.success(petVO);
    }

    //查询宠物列表
    @Override
    public Result<List<PetListItemVO>> list(PetQueryDto petQueryDto) {
        List<PetListItemVO> petListItemVOS = petMapper.queryListItem(petQueryDto);
        Integer count = petMapper.queryCount(petQueryDto);
        return ApiResult.success(petListItemVOS, count);
    }

    @Override
    public Result<List<PetListItemVO>> recommend() {
        PetQueryDto petQueryDto = new PetQueryDto();
        petQueryDto.setIsRecommend(IsRecommendEnum.RECOMMEND.getStatus());
        List<PetListItemVO> petListItemVOS = petMapper.queryListItem(petQueryDto);
        if (!petListItemVOS.isEmpty()) {
            return ApiResult.success(petListItemVOS);
        }

        //如果系统没有推荐的
        PetQueryDto defaultPetQueryDto = new PetQueryDto();
        defaultPetQueryDto.setCurrent(0);
        defaultPetQueryDto.setSize(3);
        List<PetListItemVO> defaultPetListItemVOS = petMapper.queryListItem(defaultPetQueryDto);
        return ApiResult.success(defaultPetListItemVOS);
    }

    /**
     * 智能推荐宠物信息
     *
     * @param count 期望拿到的条数
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @Override
    public Result<List<PetListItemVO>> autoRecommend(Integer count) {
        List<Integer> petIds = petMapper.queryAllIds(); // 获取全部的宠物ID列表
        // 兴趣评分 = 浏览 * 1 + 收藏 * 3 + 喜欢 * 1
        List<ScoreVO> scoreVOS = activeNetMapper.queryScore("PET");
        // 期望用到的评分数据集
        List<UserBasedCFUtil.Score> scoreList = scoreVOS.stream().map(scoreVO -> new UserBasedCFUtil.Score(
                scoreVO.getUserId(),
                scoreVO.getContentId(),
                scoreVO.getScore()
        )).collect(Collectors.toList());
        // 构建用户对于物品评分的矩阵

        Map<Integer, Map<Integer, Double>> userItemMatrix = UserBasedCFUtil.buildUserItemMatrix(petIds, scoreList);
        UserBasedCFUtil userBasedCFUtil = new UserBasedCFUtil(userItemMatrix);

        List<Integer> recommendItems = userBasedCFUtil.recommendItems(LocalThreadHolder.getUserId(), count);

        // “冷启动”

        if (recommendItems.isEmpty()) {
            List<ScoreVO> scoreVOList = activeNetMapper.queryAllIds(
                    "PET",
                    ActiveNetType.VIEW.getStatus(),
                    count
            );
            if (scoreVOList.isEmpty()) {
                return ApiResult.success(new ArrayList<>());
            }
            List<Integer> petNetIds = scoreVOList.stream()
                    .map(ScoreVO::getContentId)
                    .collect(Collectors.toList());
            List<PetListItemVO> petListItemVOS = petMapper.queryListItemByIds(petNetIds);
            return ApiResult.success(petListItemVOS);
        }
        List<PetListItemVO> petListItemVOS = petMapper.queryListItemByIds(recommendItems);
        return ApiResult.success(petListItemVOS);
    }

    /**
     * 查询用户收藏的宠物信息
     *
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @Override
    public Result<List<PetListItemVO>> saveList() {
        // 先去查收藏了哪些？
        ActiveNetQueryDto activeNetQueryDto = new ActiveNetQueryDto();
        activeNetQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户的ID
        activeNetQueryDto.setContentType("PET"); // 设置查询的模块 - PET - 宠物模块
        activeNetQueryDto.setType(ActiveNetType.SAVE.getStatus()); // 查询宠物模块下面的是收藏的类型
        List<ActiveNet> activeNetList = activeNetMapper.query(activeNetQueryDto);
        if (activeNetList.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<Integer> petIds = activeNetList.stream()
                .map(ActiveNet::getContentId)
                .collect(Collectors.toList());
        List<PetListItemVO> petListItemVOS = petMapper.queryListItemByIds(petIds);
        return ApiResult.success(petListItemVOS);
    }

}
