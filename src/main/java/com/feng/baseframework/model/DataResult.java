package com.feng.baseframework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feng.baseframework.constant.ResultEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

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
@Setter
@Getter
public class DataResult<T> implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 6682137523867425905L;
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String message;

    /** 具体的内容. */
    private T data;
    public DataResult() {
        super();
    }

    public DataResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public DataResult(ResultEnum resultEnum, T data) {
        this(resultEnum.getCode(), resultEnum.getMessage(), data);
    }

    public static <T> DataResult<T> ok(T data) {
        return new DataResult<>(ResultEnum.SUCCESS, data);
    }

    public static <T> DataResult<T> fail(ResultEnum ResultEnum, T data) {
        return new DataResult<>(ResultEnum, data);
    }

    public static <T> DataResult<T> fail(ResultEnum ResultEnum) {
        return new DataResult<>(ResultEnum, null);
    }

    public static <T> DataResult<T> fail(int ResultEnum, String message) {
        return new DataResult<>(ResultEnum, message, null);
    }

    public static <T> DataResult<T> empty() {
        return new DataResult<>(ResultEnum.SUCCESS, null);
    }
}
