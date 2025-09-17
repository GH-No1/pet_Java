package com.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 宠物领养订单状态枚举
 * 1：申请中；2：审核通过；3：审核未通过；
 */
@Getter
@AllArgsConstructor
public enum PetAdoptOrderStatus {

    REPLYING(1, "申请中"),
    AUDIT_OK(2, "审核通过"),
    AUDIT_ERROR(3, "审核不通过");


    private final Integer status; // 状态码
    private final String name; // 状态描述
}