package org.sj.alexa.model.v3;

/**
 * 接口支持的操作
 *
 * @author shijian
 * @email shijianws@163.com
 * @date 2017-12-18
 */
public class Support {
    private String id; // Capability.interface
    private String name;

    public Support() {
    }

    public Support(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
