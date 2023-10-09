package com.feng.baseframework.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * baseframework
 * 2023/9/3 10:23
 * solr配置类
 *
 * @author lanhaifeng
 * @since 3.0
 **/
@ConfigurationProperties(prefix = "spring.data.solr")
@Setter
@Getter
@RefreshScope
public class SolrProperties {
    private String host = "http://127.0.0.1:8983/solr";
    private String zkHost;
}
