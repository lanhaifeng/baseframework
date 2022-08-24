package com.feng.baseframework.autoconfig;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * baseframework
 * 2018/9/30 11:25
 * drools配置类
 * 通过配置文件进行drools规则匹配
 *
 * @author lanhaifeng
 * @since
 **/
@Configuration
public class DroolsConfig {
	private static final String[] RULES_PATHS = new String[]{"rules/", "rules2/"};

	@Bean
	@ConditionalOnMissingBean(KieFileSystem.class)
	public KieFileSystem kieFileSystem() throws IOException {
		KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
		for (String RULES_PATH : RULES_PATHS) {
			for (Resource file : getRuleFiles(RULES_PATH)) {
				kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
			}
		}
		return kieFileSystem;
	}

	private Resource[] getRuleFiles(String RULES_PATH) throws IOException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
	}

	@Bean
	@ConditionalOnMissingBean(KieContainer.class)
	public KieContainer kieContainer() throws IOException {
		final KieRepository kieRepository = getKieServices().getRepository();
		KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
		kieBuilder.buildAll();

		return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
	}

	private KieServices getKieServices() {
		return KieServices.Factory.get();
	}

	@Bean
	@ConditionalOnMissingBean(KieBase.class)
	public KieBase kieBase() throws IOException {
		return kieContainer().getKieBase();
	}

	@Bean
	@ConditionalOnMissingBean(KieSession.class)
	public KieSession kieSession() throws IOException {
		return kieContainer().newKieSession();
	}

	@Bean
	@ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
	public KModuleBeanFactoryPostProcessor kiePostProcessor() {
		return new KModuleBeanFactoryPostProcessor();
	}
}
