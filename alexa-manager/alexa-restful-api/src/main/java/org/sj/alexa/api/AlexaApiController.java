package org.sj.alexa.api;

import org.sj.alexa.model.v3.AlexaControlVO;
import org.sj.alexa.model.v3.AlexaEndpoint;
import org.sj.alexa.model.v3.Capability;
import org.sj.alexa.model.v3.Properties;
import org.sj.alexa.service.tx.IAlexaV3Service;
import org.sj.alexa.util.AlexaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alexa控制访问API
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
@RestController
@RequestMapping("/api/alexa")
public class AlexaApiController {
    @Autowired
    private IAlexaV3Service alexaV3Service;

    /**
     * 初始化数据
     */
    @PostMapping("/v3/init")
    public Object saveAlexaEndpoint(AlexaEndpoint args, HttpServletRequest request) {
        String userId = "admin";
        String gateway = "123456789012";
        {
            AlexaEndpoint endpoint = new AlexaEndpoint("Scene", "Sample Manufacturer", "This is Scene", userId, gateway, AlexaEndpoint.NAME_SCENE_TRIGGER, "1");
            AlexaUtil.addCapability(endpoint, AlexaUtil.createDefaultCapability());
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_POWER, "3", true, false, Properties.NAME_POWERSTATE));
            alexaV3Service.saveAlexaEndpoint(endpoint);
        }
        {
            AlexaEndpoint endpoint = new AlexaEndpoint("Light", "Sample Manufacturer", "This is Light", userId, gateway, AlexaEndpoint.NAME_LIGHT, "1");
            AlexaUtil.addCapability(endpoint, AlexaUtil.createDefaultCapability());
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_POWER, "3", true, false, Properties.NAME_POWERSTATE));
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_POWERLEVEL, "3", true, false, Properties.NAME_POWERLEVEL));
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_COLOR, "3", true, false, Properties.NAME_COLOR));
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_COLORTEMPERATURE, "3", true, false, Properties.NAME_COLOR_TEMPERATURE));
            AlexaUtil.addCapability(endpoint, AlexaUtil.createCapability(Capability.TYPE_ALEXA_INTERFACE, Capability.INTERFACE_PERCENTAGE, "3", true, false, Properties.NAME_PERCENTAGE));
            alexaV3Service.saveAlexaEndpoint(endpoint);
        }
        return "OK";
    }

    @GetMapping("/v3/discovery")
    public Object listAlexaEndpoint(AlexaEndpoint args, HttpServletRequest request) {
        String userId = args.getUserId();
        if (userId == null) {
            userId = "admin";
        }
        String gateway = args.getGateway();
        if (gateway == null) {
            gateway = "123456789012";
        }
        Map<String, Object> result = new HashMap<>();
        List<AlexaEndpoint> alexaEndpoints = null;
        if (gateway == null) {
            alexaEndpoints = alexaV3Service.listAlexaEndpoint(userId);
        } else {
            alexaEndpoints = alexaV3Service.listAlexaEndpoint(userId, gateway);
        }
        result.put("rows", alexaEndpoints);
        return result;
    }

    /**
     * 远程控制
     */
    @PostMapping("/v3/remote_control")
    public Object remoteControl(@RequestBody AlexaControlVO vo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        AlexaControlVO resp = alexaV3Service.remoteControl(vo, null);
        List<AlexaControlVO> rows = new ArrayList<>();
        if (resp != null) {
            rows.add(resp);
        }
        result.put("rows", rows);
        return result;
    }
}
