package com.feng.baseframework.util;

import com.feng.baseframework.model.DataResult;

/**
 * @ProjectName: svc-search-biz
 * @description: 数据结果工具类
 * @author: lanhaifeng
 * @create: 2018-05-02 16:57
 * @UpdateUser:
 * @UpdateDate: 2018/5/2 16:57
 * @UpdateRemark:
 **/
public class DataResultUtil {
    public static <T> DataResult<T> success(T t) {
        DataResult<T> result = new DataResult<>();
        result.setCode(0);
        result.setMessage("成功");
        result.setData(t);
        return result;
    }

    public static <T> DataResult<T> success() {
        return success(null);
    }

    public static <T> DataResult<T> error(Integer code, String mssage) {
        DataResult<T> result = new DataResult<T>();
        result.setCode(code);
        result.setMessage(mssage);
        return result;
    }
}
