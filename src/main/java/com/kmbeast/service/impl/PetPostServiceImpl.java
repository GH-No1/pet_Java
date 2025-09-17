package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.ActiveNetMapper;
import com.kmbeast.mapper.PetPostMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.ActiveNetQueryDto;
import com.kmbeast.pojo.dto.PetPostQueryDto;
import com.kmbeast.pojo.em.ActiveNetType;
import com.kmbeast.pojo.em.IsAuditEnum;
import com.kmbeast.pojo.entity.ActiveNet;
import com.kmbeast.pojo.entity.PetPost;
import com.kmbeast.pojo.vo.PetPostListItemVO;
import com.kmbeast.pojo.vo.PetPostVO;
import com.kmbeast.service.PetPostService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物经验帖子业务逻辑接口实现类
 */
@Service
public class PetPostServiceImpl extends ServiceImpl<PetPostMapper, PetPost> implements PetPostService {
    @Resource
    ActiveNetMapper activeNetMapper;

    @Autowired
    private PetPostMapper petPostMapper;

    /**
     * 经验帖子列表查询
     *
     * @param petPostQueryDto 查询条件
     * @return Result<List < PetPostListItemVO>> 后台通用响应
     */
    @Override
    public Result<List<PetPostListItemVO>> list(PetPostQueryDto petPostQueryDto) {
        List<PetPostListItemVO> petPostListItemVOS = this.baseMapper.list(petPostQueryDto);
        Integer count = this.baseMapper.listCount(petPostQueryDto);
        return ApiResult.success(petPostListItemVOS, count);
    }

    /**
     * 通过ID查询宠物帖子信息
     *
     * @param id 宠物帖子主键ID
     * @return Result<PetPostVO>
     */
    @Override
    public Result<PetPostVO> getById(Integer id) {
        // 查询帖子详情
        PetPostVO petPostVO = this.baseMapper.getById(id);
        if (petPostVO == null) {
            return ApiResult.success(null); // 或返回资源不存在的提示
        }

        // 获取当前用户ID
        Integer userId = LocalThreadHolder.getUserId();
        if (userId == null) {
            // 未登录用户不记录浏览行为，直接返回结果
            return ApiResult.success(petPostVO);
        }

        // 登录用户记录浏览行为
        try {
            // 检查是否已记录过浏览
            ActiveNetQueryDto queryDto = new ActiveNetQueryDto();
            queryDto.setUserId(userId);
            queryDto.setContentId(id);
            queryDto.setContentType("PET_POST"); // 标识为帖子类型
            queryDto.setType(ActiveNetType.VIEW.getStatus());


            Integer count = activeNetMapper.queryCount(queryDto);
            if (count == 0) {
                // 新增浏览记录
                ActiveNet activeNet = new ActiveNet();
                activeNet.setUserId(userId);
                activeNet.setContentId(id);
                activeNet.setContentType("PET_POST");
                activeNet.setType(ActiveNetType.VIEW.getStatus());
                activeNet.setCreateTime(LocalDateTime.now());
                activeNetMapper.insert(activeNet);
            }
        } catch (Exception e) {
            // 浏览记录异常不影响主功能，仅记录日志
            log.error("记录帖子浏览行为失败，帖子ID: {}");
        }

        return ApiResult.success(petPostVO);
    }
    /**
     * 经验帖子新增
     *
     * @param petPost 实体数据
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(PetPost petPost) {
        judgeParam(petPost); // 先做参数校验
        petPost.setUserId(LocalThreadHolder.getUserId()); // 设置发布者用户ID
        petPost.setCreateTime(LocalDateTime.now()); // 设置发布时间
        petPost.setIsAudit(IsAuditEnum.NO_AUDIT.getStatus()); // 发布时，初始是未审核的
        save(petPost);
        return ApiResult.success("宠物经验帖子新增成功");
    }

    private void judgeParam(PetPost petPost) {
        AssertUtils.notNull(petPost.getPetTypeId(), "宠物类型不为空哦");
        AssertUtils.hasText(petPost.getTitle(), "标题不为空哦");
        AssertUtils.hasText(petPost.getCover(), "封面要上传哦");
        AssertUtils.hasText(petPost.getSummary(), "请补充摘要");
        AssertUtils.isTrue(petPost.getTitle().length() < 30, "标题长度最多30个字哦");
        AssertUtils.isTrue(petPost.getSummary().length() < 200, "摘要长度要控制在200个字以内哦");
    }

    /**
     * 宠物经验帖子修改
     *
     * @param petPost 实体信息
     * @return Result<String>
     */
    @Override
    public Result<String> updateEntity(PetPost petPost) {
        judgeParam(petPost); // 先做参数校验
        updateById(petPost);
        return ApiResult.success("宠物经验帖子修改成功");
    }

    /**
     * 宠物经验帖子审核
     *
     * @param id 主键ID
     * @return Result<String> 通用返回封装类
     */
    @Override
    public Result<String> audit(Integer id) {
        PetPost petPost = new PetPost();
        petPost.setId(id);
        petPost.setIsAudit(IsAuditEnum.PASSED.getStatus()); // 已通过
        updateById(petPost);
        return ApiResult.success("审核成功");
    }

    /**
     * 审核未通过（带理由）
     */
    @Override
    public Result<String> auditReject(Integer id, String auditReason) {

        AssertUtils.hasText(auditReason, "审核未通过理由不能为空");
        PetPost petPost = new PetPost();
        petPost.setId(id);
        petPost.setIsAudit(IsAuditEnum.REJECTED.getStatus()); // 未通过
        petPost.setAuditReason(auditReason); // 存储未通过理由
        updateById(petPost);
        return ApiResult.success("已标记为未通过");
    }


    /**
     * 查询用户收藏的宠物经验帖子信息
     *
     * @return Result<List < PetListItemVO>> 通用返回封装类
     */
    @Override
    public Result<List<PetPostListItemVO>> saveList() {
        // 先去查收藏了哪些？
        ActiveNetQueryDto activeNetQueryDto = new ActiveNetQueryDto();
        activeNetQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户的ID
        activeNetQueryDto.setContentType("PET-POST"); // 设置查询的模块 - PET - 宠物模块
        activeNetQueryDto.setType(ActiveNetType.SAVE.getStatus()); // 查询宠物模块下面的是收藏的类型
        List<ActiveNet> activeNetList = activeNetMapper.query(activeNetQueryDto);
        if (activeNetList.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<Integer> petIds = activeNetList.stream()
                .map(ActiveNet::getContentId)
                .collect(Collectors.toList());
        List<PetPostListItemVO> petPostListItemVOS = petPostMapper.queryListItemByIds(petIds);
        return ApiResult.success(petPostListItemVOS);
    }
}