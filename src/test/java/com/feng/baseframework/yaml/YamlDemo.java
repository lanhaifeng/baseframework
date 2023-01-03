package com.feng.baseframework.yaml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: yaml测试实体
 * @Author: lanhaifeng
 * @CreateDate: 2018/8/17 21:34
 * @UpdateUser:
 * @UpdateDate: 2018/8/17 21:34
 * @UpdateRemark:
 * @Version: 1.0
 */
public class YamlDemo implements Serializable{

    private static final long serialVersionUID = -7419693173040869880L;
    private String name;
    private String desc;
    private List<YamlDemo> items;
    private String[] hobbys;

    public String[] getHobbys() {
        return hobbys;
    }

    public void setHobbys(String[] hobbys) {
        this.hobbys = hobbys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<YamlDemo> getItems() {
        return items;
    }

    public void setItems(List<YamlDemo> items) {
        this.items = items;
    }

    public void addItem(YamlDemo yamlDemo){
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        if(yamlDemo != null){
            this.items.add(yamlDemo);
        }
    }

    @Override
    public String toString() {
        String items = "";
        if(this.items != null && this.items.size() > 0){
            items = ", items=" + this.items.toString();
        }
        String hobby = "";
        if(this.hobbys != null && this.hobbys.length > 0){
            hobby = ", hobby=" + Arrays.toString(this.hobbys);
        }
        return "YamlDemo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                items + '\'' +
                hobby+
                '}';
    }
}
