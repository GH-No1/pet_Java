package com.kmbeast.service.impl;

import com.kmbeast.mapper.PetTypeMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.PetTypeQueryDto;
import com.kmbeast.pojo.entity.PetType;
import com.kmbeast.pojo.vo.PetVO;
import com.kmbeast.service.PetTypeService;
import com.kmbeast.utils.AssertUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Service
public class PetTypeServiceImpl implements PetTypeService {


    @Resource
    private PetTypeMapper petTypeMapper;

    @Override
    public Result<String> save(PetType petType) {
        //确保传进来的宠物类别名不为空
        AssertUtils.hasText(petType.getName(), "宠物类别名不能为空");

        //如果传进来的宠物类别名已经存在，则不能新增
        PetType petTypeEntity = petTypeMapper.queryByName(petType.getName());
        AssertUtils.isTrue(petTypeEntity == null, "该宠物类别已经存在");

        petTypeMapper.save(petType);//save()有返回值，返回的是数据库影响的行数
        return ApiResult.success("宠物类别新增成功");
    }

    @Override
    public Result<String> update(PetType petType) {
        //确保传进来的宠物类别名不为空
        AssertUtils.hasText(petType.getName(), "宠物类别名不能为空");

        //如果传进来的宠物类别名已经存在，则不能修改
        PetType petTypeEntity = petTypeMapper.queryByName(petType.getName());
        AssertUtils.isTrue(petTypeEntity == null, "该宠物类别已经存在");
        petTypeMapper.update(petType);
        return ApiResult.success("宠物类别修改成功");
    }

    @Override
    public Result<String> deleteById(Integer id) {
        petTypeMapper.deleteById(id);
        return ApiResult.success("宠物类别删除成功");
    }


    //petTypeQueryDto 查询条件类
    @Override
    public Result<List<PetType>> query(PetTypeQueryDto petTypeQueryDto) {
        // 查符合条件的总条数 - 前端分页用的
        Integer count = petTypeMapper.queryCount(petTypeQueryDto);
        // 查符合条件的数据项
        List<PetType> petTypeList = petTypeMapper.query(petTypeQueryDto);
        return ApiResult.success(petTypeList, count);
    }
}
