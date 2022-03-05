package com.feng.baseframework.model;

import java.io.Serializable;

/**
 * 类名:JsonpProxy <br/>
 * 描述:jsonp返回实体包装类 <br/>
 * 时间:2022/1/11 19:04 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class JsonpProxy implements Serializable {

    public JsonpProxy(Object value) {
        this.value = value;
    }

    private Object value;
    private String jsonpFunction;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getJsonpFunction() {
        return jsonpFunction;
    }

    public void setJsonpFunction(String jsonpFunction) {
        this.jsonpFunction = jsonpFunction;
    }
}
