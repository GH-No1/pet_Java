package com.kmbeast.pojo.vo;

import com.kmbeast.pojo.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//宠物信息VO类
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetVO extends Pet {


    //宠物类别
    private  String petTypeName;
    //点赞量
    private Integer upvoteNumber;
    //浏览量
    private Integer viewNumber;
    //收藏量
    private Integer saveNumber;

}
