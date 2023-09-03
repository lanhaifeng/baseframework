package com.feng.baseframework.controller;

import com.feng.baseframework.mapper.MenuMapper;
import com.feng.baseframework.mapper.OperLogMapper;
import com.feng.baseframework.model.DataResult;
import com.feng.baseframework.model.OperLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * baseframework
 * 2023/9/3 19:49
 * 操作日志
 *
 * @author lanhaifeng
 * @since 3.0
 **/
@RestController
@Validated
public class OperLogController implements InitializingBean {

    @Autowired
    private OperLogMapper operLogMapper;

    @GetMapping(value = "/operlog/selectAll")
    public DataResult<List<OperLog>> selectAll(){
        return DataResult.ok(operLogMapper.selectAll());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("chushihua");
        System.out.println(operLogMapper);
    }
}
