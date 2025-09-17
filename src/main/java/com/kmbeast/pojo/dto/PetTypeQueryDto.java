package com.kmbeast.pojo.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

//宠物类别查询条件类
@EqualsAndHashCode(callSuper = true)
@Data  //Lombok的自动创建类的getter和setter方法
public class PetTypeQueryDto extends  QueryDto{


    //类别名
    private String name;
}
