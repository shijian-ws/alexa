package org.sj.alexa.model.v3;

/**
 * Alexa远程控制请求参数接收
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
public class AlexaResponseVO {
    private String endpointId;
    private String namespace;
    private String name;

    /**
     * 接收颜色hvs参数
     */
    private Color color;
    private int colorTemperatureInKelvin;

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
}
