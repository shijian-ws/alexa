package org.sj.alexa.service.tx.impl;

import org.sj.alexa.dao.IAlexaV3DAO;
import org.sj.alexa.model.v3.*;
import org.sj.alexa.service.tx.IAlexaV3Service;
import org.sj.alexa.util.AlexaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-19
 */
@Service
public class AlexaV3ServiceImpl implements IAlexaV3Service {
    @Autowired
    private IAlexaV3DAO alexaDAO;

    @Override
    public List<AlexaEndpoint> listAlexaEndpoint(String userId) {
        return alexaDAO.listAlexaEndpointByUserId(userId);
    }

    @Override
    public List<AlexaEndpoint> listAlexaEndpoint(String userId, String gateway) {
        return alexaDAO.listAlexaEndpointByUserId(userId);
    }

    private static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void saveAlexaEndpoint(AlexaEndpoint alexaEndpoint) {
        List<Capability> capabilities = alexaEndpoint.getCapabilities();
        if (capabilities == null || capabilities.isEmpty()) {
            throw new IllegalArgumentException("未找到Alexa可控设备的功能描述!");
        }
        String endpointId = alexaEndpoint.getEndpointId();
        if (endpointId == null) {
            // 未找到功能id, 生成
            endpointId = createUUID();
            alexaEndpoint.setEndpointId(endpointId);
        }
        String friendlyName = alexaEndpoint.getFriendlyName();
        if (friendlyName == null || friendlyName.isEmpty()) {
            throw new IllegalArgumentException("未找到Alexa可控设备显示名称!");
        }
        String manufacturerName = alexaEndpoint.getManufacturerName();
        if (manufacturerName == null) {
            throw new IllegalArgumentException("未找到Alexa可控设备的设备制造商描述!");
        }
        String description = alexaEndpoint.getDescription();
        if (description == null) {
            throw new IllegalArgumentException("未找到Alexa可控设备的描述!");
        }
        Set<String> displayCategories = alexaEndpoint.getDisplayCategories();
        if (displayCategories == null) {
            throw new IllegalArgumentException("未找到Alexa可控设备类型!");
        }
        if (displayCategories.size() != 1) {
            throw new IllegalArgumentException("当前Alexa可控设备类型不允许多种同时存在!");
        }
        String userId = alexaEndpoint.getUserId();
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("未找到Alexa可控设备所属用户!");
        }
        String gateway = alexaEndpoint.getGateway();
        if (gateway == null || gateway.isEmpty()) {
            throw new IllegalArgumentException("未找到Alexa可控设备所属网关!");
        }
        Set<String> operIds = alexaEndpoint.getOperIds();
        if (operIds == null || operIds.isEmpty()) {
            throw new IllegalArgumentException("Alexa可控设备的设备id不能为空!");
        }
        for (Capability capability : capabilities) {
            // 保存功能
            String capabilityId = capability.getId();
            if (capabilityId == null) {
                // 未找到功能id, 生成
                capabilityId = createUUID();
                capability.setId(capabilityId);
            }
            String type = capability.getType();
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("未找到Alexa可控设备的功能类型!");
            }
            String _interface = capability.getInterface(); // 获取功能接口名
            if (_interface == null || _interface.isEmpty()) {
                throw new IllegalArgumentException("未找到Alexa可控设备的功能接口!");
            }
            if (!"Alexa".equals(_interface)) {
                Properties properties = capability.getProperties();
                if (properties == null) {
                    throw new IllegalArgumentException("未找到Alexa可控设备的功能属性!");
                }
                String propertiesId = properties.getId();
                if (propertiesId == null) {
                    // 未找到功能属性id, 生成
                    propertiesId = createUUID();
                    properties.setId(propertiesId);
                }
                List<Support> supported = properties.getSupported();
                if (supported == null || supported.isEmpty()) {
                    throw new IllegalArgumentException("未找到Alexa可控设备的功能属性支持的控制名!");
                }
                for (Support support : supported) {
                    String name = support.getName();
                    if (name == null || name.isEmpty()) {
                        throw new IllegalArgumentException("Alexa可控设备的功能属性支持的控制名不能为空!");
                    }
                    if (!alexaDAO.checkSupportName(_interface, name)) {
                        throw new IllegalArgumentException(String.format("Alexa可控设备的功能属性不支持的控制名%s!", name));
                    }
                }
                properties.setCapabilityId(capabilityId); // 关联功能ID
                alexaDAO.saveAlexaProperties(properties); // 保存属性
            }
            capability.setVersion("3"); // 固定版本3
            capability.setEndpointId(endpointId);
            alexaDAO.saveAlexaCapability(capability); // 保存功能
        }
        alexaEndpoint.setCreationTime(System.currentTimeMillis()); // 创建时间戳
        alexaDAO.saveAlexaEndpoint(alexaEndpoint);
    }

    @Override
    public void updateAlexaEndpoint(AlexaEndpoint alexaEndpoint) {
        alexaDAO.updateAlexaEndpoint(alexaEndpoint);
    }

    private void removeAlexaEndpoint(List<AlexaEndpoint> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (AlexaEndpoint endpoint : list) {
            String endpointId = endpoint.getEndpointId();
            List<Capability> capabilities = endpoint.getCapabilities();
            if (capabilities != null && !capabilities.isEmpty()) {
                for (Capability capability : capabilities) {
                    alexaDAO.removeAlexaPropertiesByCapabilityId(capability.getId());
                }
            }
            alexaDAO.removeAlexaCapabilityByEndpointId(endpointId);
            alexaDAO.removeAlexaEndpointById(endpointId);
        }
    }

    @Override
    public void removeAlexaEndpoint(String userId) {
        removeAlexaEndpoint(alexaDAO.listAlexaEndpointByUserId(userId));
    }

    @Override
    public void removeAlexaEndpoint(String userId, String gateway) {
        removeAlexaEndpoint(alexaDAO.listAlexaEndpointByUserIdAndGateway(userId, gateway));
    }

    private static AlexaControlVO checkAndProcess(AlexaEndpoint endpoint, AlexaControlVO vo, BiConsumer<AlexaEndpoint, AlexaControlVO> rcFunc, String respName, Supplier<?> valFunc, boolean checkVal, String error) {
        if (valFunc == null) {
            throw new IllegalArgumentException("响应值函数不能为空!");
        }
        if (checkVal && valFunc.get() == null) {
            // 值函数的返回值为空
            if (error == null) {
                throw new NullPointerException();
            }
            throw new IllegalArgumentException(error);
        }
        String namespace = vo.getNamespace();
        if (!AlexaUtil.checkOper(endpoint, namespace)) {
            throw new IllegalArgumentException(String.format("Alexa可控设备不支持 %s 操作!", namespace));
        }
        // TODO 发送远程控制指令
        if (rcFunc != null) {
            rcFunc.accept(endpoint, vo);
        }
        return new AlexaControlVO(namespace, respName, valFunc.get()); // 生成响应
    }

    @Override
    public AlexaControlVO remoteControl(AlexaControlVO vo, BiConsumer<AlexaEndpoint, AlexaControlVO> func) {
        if (vo == null) {
            throw new IllegalArgumentException("远程控制参数对象不能为空!");
        }
        String endpointId = vo.getEndpointId();
        if (endpointId == null) {
            throw new IllegalArgumentException("远程控制参数中可控设备ID不能为空!");
        }
        AlexaEndpoint endpoint = alexaDAO.getAlexaEndpointById(endpointId);
        if (endpoint == null) {
            throw new IllegalArgumentException("未找到Alexa可控设备空!");
        }
        if (!endpoint.getUserId().equals(vo.getUserId())) {
            throw new IllegalArgumentException("Alexa可控设备所属用户与登录用户不匹配!");
        }
        String name = vo.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("远程控制类型不能为空!");
        }
        Supplier<?> valFunc = null;
        boolean checkVal = true;
        String respName = null;
        String error = "远程控制%s不能为空!";
        String arg = null;
        // 开关: https://developer.amazon.com/docs/device-apis/alexa-powercontroller.html
        if (AlexaControlVO.TURN_ON.equals(name)) {
            // 控制打开
            valFunc = "ON"::intern;
            respName = Properties.NAME_POWERSTATE;
            checkVal = false;
        } else if (AlexaControlVO.TURN_OFF.equals(name)) {
            valFunc = "OFF"::intern;
            respName = Properties.NAME_POWERSTATE;
            checkVal = false;
        }
        // 功率: https://developer.amazon.com/docs/device-apis/alexa-powercontroller.html
        else if (AlexaControlVO.SET_POWER_LEVEL.equals(name)) {
            // 设置功率绝对值
            valFunc = vo::getPowerLevel; // 功率, 0~100
            respName = Properties.NAME_POWERLEVEL;
            arg = "调整功率值";
        } else if (AlexaControlVO.ADJUST_POWER_LEVEL.equals(name)) {
            // 调整功率百分比
            valFunc = vo::getPowerLevelDelta; // 功率百分比, -100~100
            respName = Properties.NAME_POWERLEVEL;
            arg = "调整功率百分比值";
        }
        // 亮度: https://developer.amazon.com/docs/device-apis/alexa-brightnesscontroller.html
        else if (AlexaControlVO.SET_BRIGHTNESS.equals(name)) {
            // 设置亮度值
            valFunc = vo::getBrightness; // 亮度值, 0~100
            respName = Properties.NAME_BRIGHTNESS;
            arg = "调整亮度值";
        } else if (AlexaControlVO.ADJUST_BRIGHTNESS.equals(name)) {
            // 设置亮度百分比
            valFunc = vo::getBrightnessDelta; // 亮度百分比, -100~100
            respName = Properties.NAME_BRIGHTNESS;
            arg = "调整亮度百分比值";
        }
        // 颜色: https://developer.amazon.com/docs/device-apis/alexa-colorcontroller.html
        else if (AlexaControlVO.SET_COLOR.equals(name)) {
            // 设置颜色
            valFunc = vo::getColor; // 颜色参数对象
            respName = Properties.NAME_COLOR;
            arg = "设置颜色参数对象";
        }
        // 色温: https://developer.amazon.com/docs/device-apis/alexa-colortemperaturecontroller.html
        else if (AlexaControlVO.DECREASE_COLOR_TEMPERATURE.equals(name) || AlexaControlVO.INCREASE_COLOR_TEMPERATURE.equals(name)) {
            // 增加或减少色温
            valFunc = vo::getColorTemperatureInKelvin; // 色温函数, 用于设置之后的回调响应
            respName = Properties.NAME_COLOR_TEMPERATURE;
            checkVal = false;
        } else if (AlexaControlVO.SET_COLOR_TEMPERATURE.equals(name)) {
            // 设置色温
            valFunc = vo::getColorTemperatureInKelvin; // 色温, 0-100
            respName = Properties.NAME_COLOR_TEMPERATURE;
            arg = "设置色温值";
        }
        // 百分比: // https://developer.amazon.com/docs/device-apis/alexa-percentagecontroller.html
        else if (AlexaControlVO.SET_PERCENTAGE.equals(name)) {
            // 设置百分比, 需要业务定义
            valFunc = vo::getPercentage; // 百分比值, 0~100
            respName = Properties.NAME_PERCENTAGE;
            arg = "设置百分比值";
        } else if (AlexaControlVO.ADJUST_PERCENTAGE.equals(name)) {
            // 设置百分比比例, 需要业务定义
            valFunc = vo::getPercentageDelta; // 百分比比例, -100~100
            respName = Properties.NAME_PERCENTAGE;
            arg = "设置百分比比例值";
        }
        if (respName == null) {
            throw new IllegalStateException("不支持的操作!");
        }
        error = String.format(error, arg);
        return checkAndProcess(endpoint, vo, func, respName, valFunc, checkVal, error);
    }
}
