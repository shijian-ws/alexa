package org.sj.alexa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sj.alexa.model.v3.AlexaEndpoint;
import org.sj.alexa.model.v3.Capability;
import org.sj.alexa.model.v3.Properties;

import java.util.List;

/**
 * Alexa数据操作接口
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
@Mapper
public interface IAlexaV3DAO {
    /**
     * 查询指定用户授权Alexa可控设备信息
     *
     * @param userId 用户ID
     * @return
     */
    List<AlexaEndpoint> listAlexaEndpointByUserId(@Param("userId") String userId);

    /**
     * 查询指定用户授权Alexa可控设备信息
     *
     * @param userId  用户ID
     * @param gateway 网关设备
     * @return
     */
    List<AlexaEndpoint> listAlexaEndpointByUserIdAndGateway(@Param("userId") String userId, @Param("gateway") String gateway);

    /**
     * 根据主键获取Alexa可控设备
     */
    AlexaEndpoint getAlexaEndpointById(@Param("endpointId") String endpointId);

    /**
     * 获取Alexa可操作设备信息
     */
    AlexaEndpoint getAlexaEndpointByCategory(@Param("userId") String userId, @Param("gateway") String gateway, @Param("category") String category, @Param("operId") String operId);

    /**
     * 保存授权Alexa可控设备信息
     */
    boolean checkSupportName(@Param("interface") String _interface, @Param("name") String name);

    /**
     * 查询属性保存
     */
    long saveAlexaProperties(Properties properties);

    /**
     * 保存功能
     */
    long saveAlexaCapability(Capability capability);

    /**
     * 保存授权Alexa可控设备信息
     */
    long saveAlexaEndpoint(AlexaEndpoint endpoint);

    /**
     * 移除功能属性
     */
    long removeAlexaPropertiesByCapabilityId(@Param("capabilityId") String capabilityId);

    /**
     * 移除功能
     */
    long removeAlexaCapabilityByEndpointId(@Param("endpointId") String endpointId);

    /**
     * 移除可控设备
     */
    long removeAlexaEndpointById(@Param("endpointId") String endpointId);
}
