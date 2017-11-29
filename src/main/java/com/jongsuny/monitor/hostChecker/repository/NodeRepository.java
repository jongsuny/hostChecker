package com.jongsuny.monitor.hostChecker.repository;

import com.jongsuny.monitor.hostChecker.domain.Node;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by jongsuny on 17/12/11.
 */
@Mapper
public interface NodeRepository {
    @Select("SELECT node_id as nodeId,group_id AS groupId,ip,hostname,description FROM nodes")
    List<Node> list();

    @Insert("<script>" +
            "INSERT INTO nodes (group_id,ip,hostname,description) " +
            "values(#{node.groupId},#{node.ip},#{node.hostname},#{node.description})" +
            "</script>")
    int insert(@Param("node") Node node);

    @Insert("<script>" +
            "INSERT INTO nodes (group_id,ip,hostname,description) values" +
            "<foreach collection =\"nodeList\" item=\"node\" index= \"index\" separator =\",\"> (" +
            "(#{node.groupId},#{node.ip},#{node.hostname},#{node.description}))" +
            "</foreach >" +
            "</script>")
    int insertList(@Param("nodeList") List<Node> nodeList);

    @Update("<script>" +
            "UPDATE nodes set " +
            "group_id = #{node.groupId},ip = #{node.ip},hostname = #{node.hostname},description = #{node.description}" +
            " WHERE node_id = ${node.nodeId} " +
            "</script>")
    int update(@Param("node") Node node);

    @Update("<script>" +
            "DELETE FROM nodes WHERE node_id = ${nodeId} " +
            "</script>")
    int delete(@Param("nodeId") Long id);
}
