package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.ActiveNetMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.ActiveNetQueryDto;
import com.kmbeast.pojo.em.ActiveNetType;
import com.kmbeast.pojo.entity.ActiveNet;
import com.kmbeast.service.ActiveNetService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//行为互动业务逻辑接口实现类
@Service
public class ActiveNetServiceImpl extends ServiceImpl<ActiveNetMapper, ActiveNet> implements ActiveNetService {

    //查询行为互动信息
    @Override
    public Result<List<ActiveNet>> query(ActiveNetQueryDto activeNetQueryDto) {
        List<ActiveNet> activeNetList = this.getBaseMapper().query(activeNetQueryDto);
        Integer count = this.getBaseMapper().queryCount(activeNetQueryDto);
        return ApiResult.success(activeNetList, count);
    }

    // 行为互动新增

    @Override
    public Result<String> saveEntity(ActiveNet activeNet) {
        System.out.println("-----校验开始-----");
        AssertUtils.notNull(activeNet.getContentId(), "内容ID不为空");
        AssertUtils.notNull(activeNet.getType(), "互动类型不为空");
        AssertUtils.notNull(activeNet.getContentType(), "内容类型不为空");
        activeNet.setUserId(LocalThreadHolder.getUserId()); // 设置当前操作者用户ID
        activeNet.setCreateTime(LocalDateTime.now()); // 设置行为互动时间
        System.out.println("-----校验结束-----");
        save(activeNet);// 调用Mybatis-Plus提供的新增方法
        return ApiResult.success();
    }

    private ActiveNetQueryDto createActiveNetQueryDto(String contentType, Integer type) {
        // 先去查收藏了哪些？
        ActiveNetQueryDto activeNetQueryDto = new ActiveNetQueryDto();
        activeNetQueryDto.setUserId(LocalThreadHolder.getUserId()); // 设置用户的ID
        activeNetQueryDto.setContentType(contentType); // 设置查询的模块 - PET - 宠物模块
        activeNetQueryDto.setType(type); // 查询宠物模块下面的是收藏的类型
        return activeNetQueryDto;
    }

    /**
     * 删除用户收藏的宠物信息
     *
     * @return Result<String> 通用返回封装类
     */
    @Override
    public Result<String> deleteUserPetList() {
        ActiveNetQueryDto activeNetQueryDto = createActiveNetQueryDto("PET", ActiveNetType.SAVE.getStatus());
        List<ActiveNet> activeNetList = this.baseMapper.query(activeNetQueryDto);
        if (activeNetList.isEmpty()) {
            return ApiResult.success();
        }
        List<Integer> ids = activeNetList.stream()
                .map(ActiveNet::getId)
                .collect(Collectors.toList());
        this.baseMapper.deleteBatchIds(ids);
        return ApiResult.success();
    }


    /**
     * 删除用户收藏的宠物经验帖子信息
     *
     * @return Result<String> 通用返回封装类
     */
    @Override
    public Result<String> deleteUserPetPostList() {
        ActiveNetQueryDto activeNetQueryDto = createActiveNetQueryDto("PET-POST", ActiveNetType.SAVE.getStatus());
        List<ActiveNet> activeNetList = this.baseMapper.query(activeNetQueryDto);
        if (activeNetList.isEmpty()) {
            return ApiResult.success();
        }
        List<Integer> ids = activeNetList.stream()
                .map(ActiveNet::getId)
                .collect(Collectors.toList());
        this.baseMapper.deleteBatchIds(ids);
        return ApiResult.success();
    }

}