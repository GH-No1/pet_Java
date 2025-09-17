package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

@TableName(value = "pet_adopt_order")
public class PetAdoptOrder {
    //主键ID
    @TableId(type= IdType.AUTO)
    private Integer id;


    //用户ID，外键，关联用户表
    private Integer userId;

    //宠物ID，外键，关联宠物表
    private Integer petId;

    //收货地址ID
    private Integer addressId;


    //领养描述
    private String detail;

    //单据状态（1：申请中；2：已审核；3：审核未通过；）
    private Integer status;

    //审核不通过的原因
    private String auditErrorDetail;

    //是否再次提交
    private Boolean isAgainPost;

    //提交次数
    private Integer postNumber;


    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
