package com.feng.baseframework.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @ProjectName: svc-search-biz
 * @description: 数据结果实体对象
 * @author: lanhaifeng
 * @create: 2018-05-02 16:55
 * @UpdateUser:
 * @UpdateDate: 2018/5/2 16:55
 * @UpdateRemark:
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResult<T> implements java.io.Serializable{
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String message;

    /** 具体的内容. */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
