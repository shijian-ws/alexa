package org.sj.alexa.model.v3;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Alexa可控设备, https://developer.amazon.com/docs/smarthome/smart-home-skill-api-message-reference.html
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
public class AlexaEndpoint {
    /**
     * Alexa需要属性
     */
    private String endpointId;
    private String friendlyName; // 显示名称
    private String description; // 描述
    private String manufacturerName; // 设备制造商描述
    private Set<String> displayCategories; // 可控设备类型
    private List<Capability> capabilities; // 可控设备应有功能

    /**
     * 业务操作需要属性
     */
    private String userId; // 可操作设备所属用户
    private String gateway; // 可操作设备所属网关
    private Set<String> operIds; // 可操作设备绑定的设备标识, 情景的主键, 灯设备的MAC
    private Long creationTime; // 创建时间

    public AlexaEndpoint() {
    }

    public AlexaEndpoint(String friendlyName, String manufacturerName, String description, String userId, String gateway, String category, String openId) {
        this.friendlyName = friendlyName;
        this.manufacturerName = manufacturerName;
        this.description = description;
        this.userId = userId;
        this.gateway = gateway;
        this.setCategory(category);
        this.setOperId(openId);
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    /**
     * 兼容mybatis, 将集合属性转换单一字段值
     */
    @JsonIgnore
    public String getCategory() {
        if (displayCategories != null) {
            return displayCategories.parallelStream().collect(Collectors.joining(","));
        }
        return null;
    }

    /**
     * 兼容mybatis, 将单一字段值转换集合属性
     */
    public void setCategory(String category) {
        if (category != null) {
            this.displayCategories = Stream.of(category.split("\\,")).collect(Collectors.toSet());
        }
    }

    public Set<String> getDisplayCategories() {
        return displayCategories;
    }

    public void setDisplayCategories(Set<String> displayCategories) {
        this.displayCategories = displayCategories;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    /**
     * 兼容mybatis, 将集合属性转换单一字段值
     */
    @JsonIgnore
    public String getOperId() {
        if (operIds != null) {
            return operIds.parallelStream().collect(Collectors.joining(","));
        }
        return null;
    }

    /**
     * 兼容mybatis, 将单一字段值转换集合属性
     */
    public void setOperId(String operId) {
        if (operId != null) {
            this.operIds = Stream.of(operId.split("\\,")).collect(Collectors.toSet());
        }
    }

    @JsonIgnore
    public Set<String> getOperIds() {
        return operIds;
    }

    public void setOperIds(Set<String> operIds) {
        this.operIds = operIds;
    }

    @JsonIgnore
    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * 可控设备类型, https://developer.amazon.com/docs/device-apis/alexa-discovery.html#display-categories
     */
    public static final String NAME_SCENE_TRIGGER = "SCENE_TRIGGER"; // 情景
    public static final String NAME_SWITCH = "SWITCH"; // 开关
    public static final String NAME_LIGHT = "LIGHT"; // 灯
}
