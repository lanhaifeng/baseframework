package com.feng.baseframework.util;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.model.RuleTp;
import com.feng.baseframework.model.User;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DroolsUtilTest extends MockitoBaseTest {

	@Test
	public void dynamicLoadRule() throws CloneNotSupportedException {
		Long start = new Date().getTime();
		KieSession kieSession = DroolsUtil.getInstance().dynamicLoadRule(loadRuleTps());
		List<User> users = new ArrayList<>();
		User user = new User.Builder().withUserName("【SQL注入】afda").build();
		for(int i=0;i<100;i++){
			User domain = user.clone();
			kieSession.insert(domain);
			users.add(domain);
		}
		int ruleFiredCount = kieSession.fireAllRules();

		//释放资源
		for(User temp : users){
			kieSession.delete(kieSession.getFactHandle(temp));
		}
		kieSession.dispose();
		users.clear();

		Long end = new Date().getTime();
		Long time = end - start;
		String response = "总共触发了" + ruleFiredCount + "次动态加载规则,"+"耗时：" + time + "ms";
		System.out.println(response);
	}

	private List<RuleTp> loadRuleTps(){
		List<RuleTp> ruleTps = new ArrayList<>();
		ruleTps.add(new RuleTp("matd : User(userName != null, userName == \"admin\");","System.out.println(\"动态加载rule，admin用户!\");",0));
		ruleTps.add(new RuleTp("matd : User(userName != null, ( ( userName matches 'SELECT count\\\\(\\\\*\\\\) FROM INFORMATION_SCHEMA.*' ) ),true );","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, ( ( userName matches '(?i).*information_schema.*' ) ),true );","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName.indexOf('#&~')==-1,true );","System.out.println(\"动态加载rule，非admin用户!\");",1));

		ruleTps.add(new RuleTp("matd : User(userName != null, userName matches '#|`|(%00)|(--\\\\+)|(---)|(\\\\/\\\\*)|(\\\\*\\\\/)|(%09)|(%0A)|(%0B)|(%0C)|(%0D)|(%A0)|(%20)|!');","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName contains '【SQL注入】');","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName matches 'SELECT(\\\\s*)COUNT\\\\(\\\\*\\\\) FROM INFORMATION_SCHEMA.*');","System.out.println(\"动态加载rule，非admin用户!\");",1));

		ruleTps.add(new RuleTp("matd : User(userName != null);","System.out.println(\"动态加载rule，非admin用户!\");regexSql('SELECT COUNT\\\\(\\\\*\\\\) FROM INFORMATION_SCHEMA.*',matd.getUserName());",1));
		ruleTps.add(new RuleTp("matd : User(userName != null) and eval(regexSql('SELECT COUNT\\\\(\\\\*\\\\) FROM INFORMATION_SCHEMA.*',matd.getUserName()));","System.out.println(\"动态加载rule，非admin用户!\");",1));
		return  ruleTps;
	}
}