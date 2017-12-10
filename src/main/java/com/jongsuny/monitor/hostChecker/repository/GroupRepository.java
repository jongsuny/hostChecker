package com.jongsuny.monitor.hostChecker.repository;

import com.jongsuny.monitor.hostChecker.domain.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by jongsuny on 17/12/11.
 */
@Mapper
public interface GroupRepository {
    @Select("SELECT group_id as groupId,service_id AS serviceId,group_name AS groupName,is_default as defaultGroup,description FROM groups")
    List<Group> list();

    @Insert("<script>" +
            "INSERT INTO groups (service_id,group_name,is_default,description) " +
            "values(#{group.serviceId},#{group.groupName},#{group.defaultGroup},#{group.description})" +
            "</script>")
    int insert(@Param("group") Group group);

    @Insert("<script>" +
            "INSERT INTO groups (service_id,group_name,is_default,description) values" +
            "<foreach collection =\"groupList\" item=\"group\" index= \"index\" separator =\",\"> (" +
            "(#{group.serviceId},#{group.groupName},#{group.defaultGroup},#{group.description}))" +
            "</foreach >" +
            "</script>")
    int insertList(@Param("groupList") List<Group> groupList);

    @Update("<script>" +
            "UPDATE groups set " +
            "service_id = #{group.serviceId},group_name = #{group.groupName},description = #{group.description}" +
            " WHERE group_id = ${group.group_id} " +
            "</script>")
    int update(@Param("group") Group group);

    @Update("<script>" +
            "DELETE FROM groups WHERE group_id = ${groupId} " +
            "</script>")
    int delete(@Param("groupId") Long id);
}
