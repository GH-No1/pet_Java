package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


//宠物信息表，与数据库pet表关联

@Data//创建getter和setter方法  通过Lombok
@AllArgsConstructor//创建有参构造
@NoArgsConstructor//创建无参构造
@TableName(value = "pet")
public class Pet {
    //主键ID
    @TableId(type= IdType.AUTO)
    private Integer id;

    //宠物名称
    private String name;


    //宠物封面图
    private String cover;

    //宠物描述
    private String detail;

    //宠物所在地点
    private String address;

    //宠物年龄
    private Integer age;


    //宠物性别（0：公，1：母）
    private Integer gender;

    //宠物类别ID，外键
    private Integer petTypeId;

    //是否接种疫苗（0：未接种 1：已接种）
    private Boolean isVaccine;

    //是否推荐（0：不推荐 1：推荐）
    private Boolean isRecommend;

    //是否领养（0：未领养 1：已领养 2:已被申请）
    @TableField("is_adopt")
    private Integer isAdopt;


    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
