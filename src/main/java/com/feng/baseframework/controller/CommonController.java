package com.feng.baseframework.controller;

import com.feng.baseframework.BaseframeworkApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 通用控制器
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/5/17 17:56创建:CommonController
 * @since v2.0
 */
@RestController
@Validated
public class CommonController {

    @Resource
    private ConfigurableApplicationContext context;

    @PutMapping(value = "/baseManage/liveReload")
    public void liveReload(){
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            context = SpringApplication.run(BaseframeworkApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
