package com.feng.baseframework.util;

import com.feng.baseframework.model.RuleTp;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * baseframework
 * 2018/9/30 16:41
 * drools工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class DroolsUtil {

	private static final Logger logger = LoggerFactory.getLogger(DroolsUtil.class);

	private DroolsUtil() {
	}

	private static class SingletonHolder {
		static DroolsUtil instance = new DroolsUtil();
	}

	public static DroolsUtil getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 2018/10/5 17:02
	 * 动态加载drools规则，并返回kieSession
	 *
	 * @param ruleTps
	 * @author lanhaifeng
	 * @return org.kie.api.runtime.KieSession
	 */
	public KieSession dynamicLoadRule(List<RuleTp> ruleTps){
		try {
			KieServices kieServices = KieServices.Factory.get();
			KieFileSystem kfs = kieServices.newKieFileSystem();
			kfs.write("src/main/resources/dynamic/rules/" + StringUtil.generateUUID() + ".drl", getRulesByTemplate(ruleTps));

			KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
			if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
				throw new RuntimeException(kieBuilder.getResults().getMessages().toString());
			}
			KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
			KieBase kBase = kieContainer.getKieBase();

			KieSession kieSession = kBase.newKieSession();
			kieSession.addEventListener(new DebugRuleRuntimeEventListener());
			return kieSession;
		} catch (Exception e) {
			logger.error("规则引擎初始化失败，请查看错误信息:" + e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 2018/10/5 16:50
	 * 根据模板和规则信息生成规则
	 *
	 * @param ruleTps
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	private String getRulesByTemplate(List<RuleTp> ruleTps) throws Exception {
		if(ruleTps == null || ruleTps.size() < 1){
			return null;
		}
		ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();

		String rule = objectDataCompiler.compile(ruleTps, getDroolsTemplate());
		logger.info(rule);

		return rule;
	}

	/**
	 * drools 获取规则模板文件
	 * @return string
	 * @throws Exception
	 */
	private InputStream getDroolsTemplate() {
		return FileUtils.getResource("/ruleTemplate/Tp.drl");
	}
}
