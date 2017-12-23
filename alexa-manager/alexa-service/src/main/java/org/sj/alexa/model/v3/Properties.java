package org.sj.alexa.model.v3;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 功能属性, https://developer.amazon.com/docs/smarthome/state-reporting-for-a-smart-home-skill.html
 */
public class Properties {
    private List<Support> supported; // 支持的属性
    /**
     * 组合使用
     * proactivelyReported=true,retrievable=false 可能为一个低功率设备通常处于休眠状态, 不能查询到响应, 能接收指令被唤醒
     * proactivelyReported=false,retrievable=false 可能为一个红外控制器, 命令单向发送到控制设备, 但被控制设备不能将其状态响应
     */
    private Boolean proactivelyReported; // 可操作设备是否有响应
    private Boolean retrievable; // 可操作设备是否可以主动查询到响应

    private String id; // 主键

    private String capabilityId; // 所属功能

    public List<Support> getSupported() {
        return supported;
    }

    public void setSupported(List<Support> supported) {
        this.supported = supported;
    }

    public Boolean getProactivelyReported() {
        return proactivelyReported;
    }

    public void setProactivelyReported(Boolean proactivelyReported) {
        this.proactivelyReported = proactivelyReported;
    }

    public Boolean getRetrievable() {
        return retrievable;
    }

    public void setRetrievable(Boolean retrievable) {
        this.retrievable = retrievable;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    /**
     * 开关属性名
     */
    public static final String NAME_POWERSTATE = "powerState";
    /**
     * 功率属性名
     */
    public static final String NAME_POWERLEVEL = "powerLevel";
    /**
     * 亮度属性名
     */
    public static final String NAME_BRIGHTNESS = "brightness";
    /**
     * 颜色属性名
     */
    public static final String NAME_COLOR = "color";
    /**
     * 色温属性名
     */
    public static final String NAME_COLOR_TEMPERATURE = "colorTemperatureInKelvin";
    /**
     * 百分比属性名
     */
    public static final String NAME_PERCENTAGE = "percentage";
}