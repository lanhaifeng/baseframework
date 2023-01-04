package com.feng.baseframework.model;

import com.feng.baseframework.annotation.Setter;

import java.io.Serializable;

/**
 * 配置模型
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/1/4 18:38创建:ConfigModel
 * @since v2.0.0
 */
@Setter
public class ConfigModel implements Serializable {

    private String name;

    public String getName() {
        return name;
    }
}
