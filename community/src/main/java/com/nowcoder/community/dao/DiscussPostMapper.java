package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Msq
 * @date 2020/11/18 - 11:15
 */

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit);

    /*
    @Param注解用于给参数取别名
    如果只有一个参数，并且在<if>里使用，则必须加别名
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    /*
        根据帖子id查询出帖子详情
     */
    DiscussPost selectDiscussPostById(int id);
}
