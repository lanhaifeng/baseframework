package com.feng.baseframework.mapper;

import com.feng.annotation.TableInfo;
import com.feng.baseframework.model.OperLog;
import org.springframework.stereotype.Component;

@Component
@TableInfo("oper_Log")
public interface OperLogMapper extends CommonMapper<OperLog> {

    /**
     * @param operLog   操作日志
     * @return {@link OperLog}
     */
    OperLog insertSelective(OperLog operLog);

}
