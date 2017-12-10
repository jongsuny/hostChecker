package com.jongsuny.monitor.hostChecker.repository;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by jongsuny on 17/12/10.
 */
@Mapper
public interface ServiceConfigRepository {
    @Select("SELECT service_id AS serviceId,service_name AS serviceName,domain,env,description FROM services")
    List<ServiceConfig> list();

    @Insert("<script>" +
            "INSERT INTO services (service_name,domain,env,description) values(#{service.serviceName},#{service.domain},#{service.env},#{service.description})" +
            "</script>")
    int insert(@Param("service") ServiceConfig serviceConfig);

    @Insert("<script>" +
            "INSERT INTO services (service_name,domain,env,description) values" +
            "<foreach collection =\"serviceList\" item=\"service\" index= \"index\" separator =\",\"> (" +
            "(#{service.serviceName},#{service.domain},#{service.env},#{service.description})" +
            "</foreach >" +
            "</script>")
    int insertList(@Param("serviceList") List<ServiceConfig> serviceConfigList);

    @Update("<script>" +
            "UPDATE services set " +
            "service_name = #{service.serviceName},domain = #{service.domain},env = #{service.env},description = #{service.description}" +
            " WHERE service_id = ${service.serviceId} " +
            "</script>")
    int update(@Param("service") ServiceConfig serviceConfig);
    @Update("<script>" +
            "DELETE FROM services WHERE service_id = ${serviceId} " +
            "</script>")
    int delete(@Param("serviceId") Long id);
}
