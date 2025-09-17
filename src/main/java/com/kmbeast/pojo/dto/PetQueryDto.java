package com.kmbeast.pojo.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

//宠物信息查询条件类
@EqualsAndHashCode(callSuper = true)
@Data  //Lombok的自动创建类的getter和setter方法
public class PetQueryDto extends  QueryDto{


    //类别名
    private String name;


    //是否推荐（0：不推荐 1：推荐）
    private Boolean isRecommend;

    //宠物类别ID
    private Integer petTypeId;
}
