package com.kmbeast.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmbeast.pojo.dto.PetQueryDto;
import com.kmbeast.pojo.entity.Pet;
import com.kmbeast.pojo.entity.PetAdoptOrder;
import com.kmbeast.pojo.vo.PetListItemVO;
import com.kmbeast.pojo.vo.PetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//宠物信息Mapper接口
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
    //新增宠物信息
    void save(Pet pet);

    //更新宠物信息
    void update(Pet pet);

    //删除宠物信息
    void deleteById(@Param(value = "id") Integer id);



    //通过宠物ID查询宠物信息
    PetVO getById(@Param(value = "id") Integer id);

    //查询宠物列表
    List<PetListItemVO> queryListItem(PetQueryDto petQueryDto);


    //查询符合条件总记录数
    Integer queryCount(PetQueryDto petQueryDto);



    List<Integer> queryAllIds();


    List<PetListItemVO> queryListItemByIds(@Param(value = "ids") List<Integer> recommendItems);


}