package com.feng.baseframework.controller;

import com.feng.baseframework.model.RuleTp;
import com.feng.baseframework.model.User;
import com.feng.baseframework.util.DroolsUtil;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * baseframework
 * 2019/7/5 10:34
 * drools控制器
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@RestController
public class DroolsController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private KieSession kieSession;

	@RequestMapping(value = "/drools/student",method={RequestMethod.GET, RequestMethod.POST})
	public String droolsTest(@RequestBody User user){
		User user1 = new User();
		user1.setUserName(user.getUserName());
		User user2 = new User();
		user2.setUserName("test");
		kieSession.insert(user);
		kieSession.insert(user1);
		kieSession.insert(user2);
		int ruleFiredCount = kieSession.fireAllRules();

		String response = user == null || user.getUserName() == null ? "" : user.getUserName() + "触发了" + ruleFiredCount + "条规则";
		return  response;
	}

	@RequestMapping(value = "/drools/dynamicStudent",method={RequestMethod.GET, RequestMethod.POST})
	public String dynamicLoadRule(@RequestBody User user) throws CloneNotSupportedException {
		Long start = new Date().getTime();
		KieSession kieSession = DroolsUtil.getInstance().dynamicLoadRule(loadRuleTps());
		List<User> users = new ArrayList<>();
		for(int i=0;i<1000;i++){
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
		return  response;
	}

	private List<RuleTp> loadRuleTps(){
		List<RuleTp> ruleTps = new ArrayList<>();
		ruleTps.add(new RuleTp("matd : User(userName != null, userName == \"admin\");","System.out.println(\"动态加载rule，admin用户!\");",0));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		ruleTps.add(new RuleTp("matd : User(userName != null, userName != \"admin\");","System.out.println(\"动态加载rule，非admin用户!\");",1));
		return  ruleTps;
	}
}
