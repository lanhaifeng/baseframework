package com.feng.baseframework.autoconfig;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

/**
 * baseframework
 * 2023/9/3 10:26
 * solr配置类
 *
 * @author lanhaifeng
 * @since 3.0
 **/
@EnableConfigurationProperties({SolrProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HttpSolrClient.class, CloudSolrClient.class})
public class SolrConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public SolrClient solrClient(SolrProperties properties) {
        if (StringUtils.isNotBlank(properties.getZkHost())) {
            return new CloudSolrClient.Builder(Arrays.asList(properties.getZkHost()), Optional.empty()).build();
        }
        return new HttpSolrClient.Builder(properties.getHost()).build();
    }

}
