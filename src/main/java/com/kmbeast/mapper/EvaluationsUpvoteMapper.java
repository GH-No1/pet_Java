package com.kmbeast.mapper;

import com.kmbeast.pojo.entity.EvaluationsUpvote;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论点赞持久化接口
 */
@Mapper
public interface EvaluationsUpvoteMapper {

    void save(EvaluationsUpvote evaluationsUpvote);

    void delete(EvaluationsUpvote evaluationsUpvote);

    int queryCount(EvaluationsUpvote evaluationsUpvote);

}
