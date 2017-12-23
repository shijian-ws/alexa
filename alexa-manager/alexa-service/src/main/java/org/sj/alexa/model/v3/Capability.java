package org.sj.alexa.model.v3;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Alexa可控设备功能, https://developer.amazon.com/docs/device-apis/alexa-discovery.html#capability-object
 */
public class Capability {
    private String type; // 功能类型,Alexa, AlexaInterface
    private String _interface; // 功能接口名称
    private String version; // 功能版本
    private Properties properties;

    private String id; // 主键

    private String endpointId; // 所属可控设备

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterface() {
        return _interface;
    }

    public void setInterface(String _interface) {
        this._interface = _interface;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    /**
     * 功能类型
     */
    public static final String TYPE_ALEXA = "Alexa";
    /**
     * 接口类型
     */
    public static final String TYPE_ALEXA_INTERFACE = "AlexaInterface";

    /**
     * 控制打开关闭
     */
    public static final String INTERFACE_POWER = "Alexa.PowerController";
    /**
     * 控制功率
     */
    public static final String INTERFACE_POWERLEVEL = "Alexa.PowerLevelController";
    /**
     * 控制亮度
     */
    public static final String INTERFACE_BRIGHTNESS = "Alexa.BrightnessController";
    /**
     * 控制颜色
     */
    public static final String INTERFACE_COLOR = "Alexa.ColorController";
    /**
     * 控制色温
     */
    public static final String INTERFACE_COLORTEMPERATURE = "Alexa.ColorTemperatureController";
    /**
     * 控制百分比
     */
    public static final String INTERFACE_PERCENTAGE = "Alexa.PercentageController";
}