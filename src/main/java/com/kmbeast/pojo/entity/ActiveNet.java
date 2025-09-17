package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


//行为信息表，与数据库active_net表关联

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "active_net") // 标识对应的数据库表
public class ActiveNet {
    //主键ID
    @TableId(type = IdType.AUTO)
    private Integer id;

    //用户ID
    private Integer userId;


    //内容ID
    private Integer contentId;


    //行为类型（1：浏览 2：点赞 3：收藏）
    private Integer type;


    //内容模块
    private String contentType;

    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
