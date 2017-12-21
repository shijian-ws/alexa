package org.sj.alexa.util;

import org.sj.alexa.model.v3.AlexaEndpoint;
import org.sj.alexa.model.v3.Capability;
import org.sj.alexa.model.v3.Support;

import java.util.ArrayList;
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
public class AlexaUtil {
    /**
     * 添加类型
     */
    public static void addCategory(AlexaEndpoint endpoint, String... categories) {
        if (endpoint == null) {
            throw new IllegalArgumentException("AlexaEndpoint对象不能未空!");
        }
        if (categories == null || categories.length == 0) {
            throw new IllegalArgumentException("被添加Category集不能未空!");
        }
        Set<String> categorySet = endpoint.getDisplayCategories();
        if (categorySet == null) {
            endpoint.setDisplayCategories(Stream.of(categories).collect(Collectors.toSet()));
            return;
        }
        for (String category : categories) {
            categorySet.add(category);
        }
    }

    /**
     * 添加功能
     */
    public static void addCapability(AlexaEndpoint endpoint, Capability... capabilities) {
        if (endpoint == null) {
            throw new IllegalArgumentException("AlexaEndpoint对象不能未空!");
        }
        if (capabilities == null || capabilities.length == 0) {
            throw new IllegalArgumentException("被添加Capability集不能未空!");
        }
        List<Capability> capabilityList = endpoint.getCapabilities();
        if (capabilityList == null) {
            endpoint.setCapabilities(Stream.of(capabilities).collect(Collectors.toList()));
            return;
        }
        for (Capability capability : capabilities) {
            if (capability != null) {
                capabilityList.add(capability);
            }
        }
    }

    /**
     * 生成默认功能对象
     */
    public static Capability createDefaultCapability() {
        Capability capability = new Capability();
        capability.setType("AlexaInterface");
        capability.setInterface("Alexa");
        capability.setVersion("3");
        return capability;
    }

    /**
     * 生成功能对象
     *
     * @param type                功能类型
     * @param namespace           功能接口
     * @param version             功能版本
     * @param proactivelyReported
     * @param retrievable
     * @param names
     * @return
     */
    public static Capability createCapability(String type, String namespace, String version, boolean proactivelyReported, boolean retrievable, String... names) {
        Capability capability = new Capability();
        capability.setType(type);
        capability.setInterface(namespace);
        capability.setVersion(version);
        org.sj.alexa.model.v3.Properties props = new org.sj.alexa.model.v3.Properties();
        props.setProactivelyReported(proactivelyReported);
        props.setRetrievable(retrievable);
        if (names != null && names.length > 0) {
            List<Support> supportList = new ArrayList<>();
            for (String name : names) {
                supportList.add(new Support(name));
            }
            props.setSupported(supportList);
        }
        capability.setProperties(props);
        return capability;
    }
}
