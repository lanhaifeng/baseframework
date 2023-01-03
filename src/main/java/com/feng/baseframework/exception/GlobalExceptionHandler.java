package com.feng.baseframework.exception;


import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.model.DataResult;
import com.feng.baseframework.util.DataResultUtil;
import com.feng.baseframework.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ProjectName: svc-search-biz
 * @description: 全局异常处理类
 * @author: lanhaifeng
 * @create: 2018-05-02 15:42
 * @UpdateUser:
 * @UpdateDate: 2018/5/2 15:42
 * @UpdateRemark:
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public DataResult handler(Exception e){
        if( e instanceof BusinessException){
            BusinessException businessException = (BusinessException) e;
            logger.info("[" + businessException.getMessage() + "] {}" , ExceptionUtil.getStackTrace(e));
            return DataResultUtil.error( businessException.getCode(), businessException.getMessage());
        }else if( e instanceof AccessDeniedException){
            logger.info("[没有权限] {}", ExceptionUtil.getStackTrace(e));
            return DataResultUtil.error(ResultEnum.ACCESS_DENY_ERROR.getCode(), ResultEnum.ACCESS_DENY_ERROR.getMessage());
        }
        else {
            logger.info("[系统异常] {}", ExceptionUtil.getStackTrace(e));
            return DataResultUtil.error( -1, "未知错误");
        }
    }

}
