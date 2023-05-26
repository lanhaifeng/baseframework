package com.feng.baseframework.constant;

/**     
  *
  * @ProjectName:    svc-search-biz
  * @Description:    定义响应结果
  * @Author:         lanhaifeng
  * @CreateDate:     2018/5/2 16:20
  * @UpdateUser:     
  * @UpdateDate:     2018/5/2 16:20
  * @UpdateRemark:   
 */
public enum ResultEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    REDIS_SET_ERROR(1,"redis缓存数据失败"),
    REDIS_GET_ERROR(2,"redis获取缓存数据失败"),
    REDIS_REMOVE_ERROR(3,"redis清理键数据失败"),
    REDIS_KEY_NULL_ERROR(4,"redis键为空"),
    PARAM_NULL_ERROR(5, "参数为空"),
    RESTFULT_CALL_ERROR(6,"restful接口调用失败"),
    DATA_PARSER_ERROR(7, "数据解析失败"),
    PARAM_CACHE_ERROR(8,"参数缓存失效"),
    PARAM_SEARCHID_NULL(9,"参数searchId为空"),
    JACKSON_PARSE_ERROR(10,"jackson转化失败"),
    ACCESS_DENY_ERROR(11,"不允许访问"),
    REFERER_ERROR(12,"防盗链"),
    PARAM_ILLEGAL_ERROR(13,"参数非法"),
    /**
     * 非法job
     */
    ILLEGAL_JOB_ERROR(14, "非法job，无法正常执行"),
    ATTACH_JVM_ERROR(15, "无法attach目标jvm"),
    RESPONSE_RESULT_ERROR(503, "服务当前无法处理请求")

    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

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
}
