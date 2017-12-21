package org.sj.alexa.model.v3;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Alexa远程控制参数
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
public class AlexaControlVO {
    private String endpointId;
    private String namespace;
    private String name;
    /**
     * 打开
     */
    public static final String TURN_OFF = "TurnOff";
    /**
     * 关闭
     */
    public static final String TURN_ON = "TurnOn";
    /**
     * 设置亮度绝对值
     */
    public static final String SET_POWER_LEVEL = "SetPowerLevel";
    /**
     * 调整亮度
     */
    public static final String ADJUST_POWER_LEVEL = "AdjustPowerLevel";
    /**
     * 设置颜色
     */
    public static final String SET_COLOR = "SetColor";
    /**
     * 减少色温
     */
    public static final String DECREASE_COLOR_TEMPERATURE = "DecreaseColorTemperature";
    /**
     * 增加色温
     */
    public static final String INCREASE_COLOR_TEMPERATURE = "IncreaseColorTemperature";
    /**
     * 设置色温绝对值
     */
    public static final String SET_COLOR_TEMPERATURE = "SetColorTemperature";
    /**
     * 调整百分比值
     */
    public static final String SET_PERCENTAGE = "SetPercentage";
    /**
     * 调整百分比比例
     */
    public static final String ADJUST_PERCENTAGE = "AdjustPercentage";

    public AlexaControlVO() {
    }

    public AlexaControlVO(String namespace, String name, Object value) {
        this(namespace, name, value, 6000);
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public AlexaControlVO(String namespace, String name, Object value, int uncertaintyInMilliseconds) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
        this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
        this.timeOfSample = FORMATTER.format(Instant.now().atZone(ZoneOffset.UTC));
    }

    /**
     * 接收亮度值参数
     */
    private Integer powerLevel;
    /**
     * 接收亮度百分比参数, -100~100
     */
    private Integer powerLevelDelta;
    /**
     * 接收颜色hvs参数
     */
    private Color color;
    /**
     * 接收色温值参数
     */
    private Integer colorTemperatureInKelvin;
    /**
     * 接收百分比参数, -100~100
     */
    private Integer percentage;
    /**
     * 接收百分比比例参数, -100~100
     */
    private Integer percentageDelta;

    /**
     * 兼容值, 基本类型, String, Value
     */
    private Object value;
    /**
     * ISO 8601 格式的UTC时间, YYYY-MM-DDThh:mm:ss.sD
     */
    private String timeOfSample;

    /**
     * 当前接口不确定延迟时间, 毫秒
     */
    private Integer uncertaintyInMilliseconds;

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }

    public Integer getPowerLevelDelta() {
        return powerLevelDelta;
    }

    public void setPowerLevelDelta(Integer powerLevelDelta) {
        this.powerLevelDelta = powerLevelDelta;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getColorTemperatureInKelvin() {
        return colorTemperatureInKelvin;
    }

    public void setColorTemperatureInKelvin(Integer colorTemperatureInKelvin) {
        this.colorTemperatureInKelvin = colorTemperatureInKelvin;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Integer getPercentageDelta() {
        return percentageDelta;
    }

    public void setPercentageDelta(Integer percentageDelta) {
        this.percentageDelta = percentageDelta;
    }

    public Object getValue() {
        return value;
    }

    /*public void setValue(Object value) {
        this.value = value;
    }*/

    public String getTimeOfSample() {
        return timeOfSample;
    }

    /*public void setTimeOfSample(String timeOfSample) {
        this.timeOfSample = timeOfSample;
    }*/

    public Integer getUncertaintyInMilliseconds() {
        return uncertaintyInMilliseconds;
    }

    /*public void setUncertaintyInMilliseconds(Integer uncertaintyInMilliseconds) {
        this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
    }*/

    public static class Color {
        private double hue; // 色调, 0-360
        private double saturation; // 饱和度, 0-1
        private double brightness; // 亮度, 0-1

        public double getHue() {
            return hue;
        }

        public void setHue(double hue) {
            this.hue = hue;
        }

        public double getSaturation() {
            return saturation;
        }

        public void setSaturation(double saturation) {
            this.saturation = saturation;
        }

        public double getBrightness() {
            return brightness;
        }

        public void setBrightness(double brightness) {
            this.brightness = brightness;
        }
    }

    /**
     * 值对象
     */
    public static class Value {
        private Object value;

        public Value() {
        }

        public Value(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
