package com.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum IsAuditEnum {

    NO_AUDIT(0, "未审核"),

    PASSED(1, "已通过"),   // “已通过”状态
    REJECTED(2, "未通过"); // “未通过”状态

    private final Integer  status; // 状态
    private final String name; // 描述

}