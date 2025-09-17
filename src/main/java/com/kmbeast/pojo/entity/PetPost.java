package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


//宠物经验帖子表，与数据库pet_post表关联

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "pet_post")
public class PetPost {
    //主键ID
    @TableId(type = IdType.AUTO)
    private Integer id;


    //用户ID，外键，关联用户表
    private Integer userId;

    //宠物类型ID，外键，关联宠物类型表
    private Integer petTypeId;


    //标题
    private String title;


    //帖子封面
    private String cover;

    //帖子内容
    private String content;

    //摘要
    private String summary;

    //是否审核（0：未审核  1：已通过  2：未通过）
    private Integer IsAudit;

    //未通过理由
    private String auditReason;


    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
