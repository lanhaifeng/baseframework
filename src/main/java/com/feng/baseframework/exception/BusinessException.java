package com.feng.baseframework.exception;

import com.feng.baseframework.constant.ResultEnum;

/**
 * @ProjectName: svc-search-biz
 * @description: 业务异常类
 * @author: lanhaifeng
 * @create: 2018-05-02 18:45
 * @UpdateUser:
 * @UpdateDate: 2018/5/2 18:45
 * @UpdateRemark:
 **/
public class BusinessException extends RuntimeException {
    /**  错误码 **/
    private Integer code;
    /**  原始异常 **/
    private Exception sourceException;

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public BusinessException(ResultEnum resultEnum, Exception sourceException) {
        super(resultEnum.getMessage(), sourceException);
        this.code = resultEnum.getCode();
        this.sourceException = sourceException;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Exception getSourceException() {
        return sourceException;
    }

    public void setSourceException(Exception sourceException) {
        this.sourceException = sourceException;
    }
}
