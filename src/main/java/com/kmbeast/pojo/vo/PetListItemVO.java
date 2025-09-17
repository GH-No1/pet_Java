package com.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kmbeast.pojo.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//宠物列表VO类

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetListItemVO{
    //主键ID
    private Integer id;

    //宠物名称
    private String name;


    //宠物封面图
    private String cover;


    //宠物所在地点
    private String address;

    //宠物年龄
    private Integer age;

    //宠物性别
    private Integer gender;

    //宠物类别ID，外键
    private Integer petTypeId;


    //宠物类别
    private  String petTypeName;

    //是否接种疫苗（0：未接种 1：已接种）
    private Boolean isVaccine;

    //是否推荐（0：不推荐 1：推荐）
    private Boolean isRecommend;

    //是否领养（0：未领养 2：已领养 1已被申请）
    private Integer isAdopt;




    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
