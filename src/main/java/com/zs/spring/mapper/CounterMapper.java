package com.zs.spring.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-28 10:25
 * @description:
 */
public interface CounterMapper {

    @Select("select group_name from credit_limit_group where group_id = #{groupId}")
    List<String> findGroupNameById(@Param("groupId") String groupId);

    @Insert("INSERT into credit_limit_redis_counter_log(create_time,group_id,group_name,counter) VALUE (now(), #{groupId},#{groupName},#{counterValue})")
    void insertCounter(@Param("groupId") String groupId, @Param("groupName") String groupName, @Param("counterValue") String counterValue);
}
