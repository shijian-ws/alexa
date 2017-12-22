package org.sj.alexa.service.tx;

import org.sj.alexa.model.v3.AlexaControlVO;
import org.sj.alexa.model.v3.AlexaEndpoint;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Alexa可控设备业务接口, v3
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
public interface IAlexaV3Service {
    /**
     * 查询指定用户授权Alexa可控设备信息
     *
     * @param userId 用户ID
     * @return
     */
    List<AlexaEndpoint> listAlexaEndpoint(String userId);

    /**
     * 查询指定用户的网关设备授权Alexa可控设备信息
     *
     * @param userId  用户ID
     * @param gateway 网关MAC
     * @return
     */
    List<AlexaEndpoint> listAlexaEndpoint(String userId, String gateway);

    /**
     * 保存Alexa可控设备信息
     */
    void saveAlexaEndpoint(AlexaEndpoint alexaEndpoint);

    /**
     * 移除Alexa可控设备信息
     */
    void removeAlexaEndpoint(String userId);

    /**
     * 移除Alexa可控设备信息
     */
    void removeAlexaEndpoint(String userId, String gateway);

    /**
     * 远程控制
     *
     * @param vo   参数: https://github.com/alexa/alexa-smarthome/tree/master/sample_messages
     * @param func 远程控制函数
     * @return
     */
    AlexaControlVO remoteControl(AlexaControlVO vo, BiConsumer<AlexaEndpoint, AlexaControlVO> func);
}
