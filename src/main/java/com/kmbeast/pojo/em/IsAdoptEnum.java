package com.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

//宠物领养状态枚举
@Getter
@AllArgsConstructor
public enum IsAdoptEnum {

    NO_ADOPT(0 , "未领养"),
    ADOPT(1, "已领养"),
    APPLIED(2, "已被申请");



    private final Integer status; // 领养状态
    private final String name; // 描述



}
