#### Quartz
1.Quartz依赖
```
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>4.2.6.RELEASE</version>
  </dependency>
  <dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.2.1</version>
  </dependency>
```

2.xml配置Quartz
```
<!-- 使用MethodInvokingJobDetailFactoryBean，任务类可以不实现Job接口，通过targetMethod指定调用方法-->
<bean id="taskJob" class="com.tyyd.dw.task.DataConversionTask"/>
<bean id="jobDetail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
    <property name="group" value="job_work"/>
    <property name="name" value="job_work_name"/>
    <!--false表示等上一个任务执行完后再开启新的任务-->
    <property name="concurrent" value="false"/>
    <property name="targetObject">
        <ref bean="taskJob"/>
    </property>
    <property name="targetMethod">
        <value>run</value>
    </property>
</bean>
 
<!--  调度触发器 -->
<bean id="myTrigger"
      class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
    <property name="name" value="work_default_name"/>
    <property name="group" value="work_default"/>
    <property name="jobDetail">
        <ref bean="jobDetail" />
    </property>
    <property name="cronExpression">
        <value>0/5 * * * * ?</value>
    </property>
</bean>
 
<!-- 调度工厂 -->
<bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="triggers">
        <list>
            <ref bean="myTrigger"/>
        </list>
    </property>
</bean>
```
DataConversionTask
```
//DataConversionTask类是一个普通的Java类，没有继承任何类和实现任何接口(当然可以用注解方式来声明bean)
public class DataConversionTask{
 
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(DataConversionTask.class);
 
    public void run() {
 
        if (LOG.isInfoEnabled()) {
            LOG.info("数据转换任务线程开始执行");
        }
    }
}
```

3.动态加载Quartz



#### spring自带task
1.xml配置Scheduler
```
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:beans="http://www.springframework.org/schema/beans"  
    xmlns:context="http://www.springframework.org/schema/context"  
    xmlns:aop="http://www.springframework.org/schema/aop"  
    xmlns:mvc="http://www.springframework.org/schema/mvc"  
    xmlns:p="http://www.springframework.org/schema/p"  
    xmlns:task="http://www.springframework.org/schema/task" 
    xsi:schemaLocation="http://www.springframework.org/schema/beans   
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd  
    http://www.springframework.org/schema/context  
    http://www.springframework.org/schema/context/spring-context-4.0.xsd  
    http://www.springframework.org/schema/aop  
    http://www.springframework.org/schema/aop/spring-aop-4.0.xsd  
    http://www.springframework.org/schema/mvc  
    http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
    http://www.springframework.org/schema/task
    http://www.springframework.org/schema/task/spring-task-4.0.xsd">  
    
    <beans:bean id="springSchedulerTask" class="cn.sunpy.task.SpringSchedulerTask"></beans:bean>
    
    <task:scheduled-tasks>
        <task:scheduled ref="springSchedulerTask" method="executeTask" cron="0/3 * * * * ?"/>
    </task:scheduled-tasks>
    
</beans>
```
任务
```
public class SpringSchedulerTask {
    
    public void executeTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        System.out.println("executeTask" + sdf.format(new Date()));
    }
}
```

2.注解方式
xml方式开启Scheduler的注解
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" 
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:tx="http://www.springframework.org/schema/tx" 
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xmlns:task="http://www.springframework.org/schema/task" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans  
                           http://www.springframework.org/schema/beans/spring-beans-4.3.xsd  
                           http://www.springframework.org/schema/context  
                           http://www.springframework.org/schema/context/spring-context-4.3.xsd
                           http://www.springframework.org/schema/tx
                           http://www.springframework.org/schema/tx/spring-tx-4.3.xsd
                           http://www.springframework.org/schema/aop 
                           http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
                           http://code.alibabatech.com/schema/dubbo 
                           http://code.alibabatech.com/schema/dubbo/dubbo.xsd
                           http://www.springframework.org/schema/task  
                           http://www.springframework.org/schema/task/spring-task.xsd">

    <context:component-scan base-package="com.hlw.grhc" />
    <context:component-scan base-package="com.hlw.sch" />

    <!—开启这个配置，spring才能识别@Scheduled注解   -->  
    <task:annotation-driven mode="proxy"/>  
</beans>
```

注解方式开启Scheduler的注解
```
@EnableScheduling
```


任务
```
@Component
public class OrgDataSch {
        
    /**
     * 每日00:00:00执行 ,如2018/7/31 0:00:00
     */
    @Scheduled(cron="0 30 0 * * ?")
    public void countPhsTask() throws ServiceException{
        ......
    }
}
```

3.多线程定时任务(即异步)
xml开启异步
```
    <!-- 开启这个配置，spring才能识别@Scheduled注解   -->  
    <task:annotation-driven scheduler="qbScheduler" mode="proxy"/>  
    <!-- 配置异步线程池   -->  
    <task:scheduler id="qbScheduler" pool-size="10"/>
```


开启异步支持，并配置定时任务线程池
```
@Configuration
//开启对异步的支持
@EnableAsync
public class ExecutorConfig1 {
 
 @Bean
 //配置线程池executor1
 public Executor executor1() {
  ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  executor.setThreadNamePrefix("test-schedule1-");
  executor.setMaxPoolSize(20);
  executor.setCorePoolSize(15);
  executor.setQueueCapacity(0);
  executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
  return executor;
 }
 
  
 @Bean
 //配置线程池executor2
 public Executor executor2() {
  ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  executor.setThreadNamePrefix("test-schedule2-");
  executor.setMaxPoolSize(20);
  executor.setCorePoolSize(15);
  executor.setQueueCapacity(0);
  executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
  return executor;
  }
}
```

使用线程池
```
// 间隔1秒执行一次
@Async("executor1")
@Scheduled(cron = "0/1 * * * * ?")
public void method1() {
 logger.info("——————————method1 start——————————");
 logger.info("——————————method1 end——————————");
}
 
// 间隔1秒执行一次
@Scheduled(cron = "0/1 * * * * ?")
@Async("executor2")
public void method2() {
 logger.info("——————————method2 start——————————");
 logger.info("——————————method2 end——————————");
}
```
注意：  
  没有配置自己的线程池时，会默认使用SimpleAsyncTaskExecutor。  
  如果项目中只配置了一个线程池，那么不需要显示指定使用这个线程池，spring也会自动使用用户配置的线程池，但是如果配置了多个就必须要显示指定，否则还是会使用默认的。  
  如果想要指定使用哪个线程池，可以使用@Async("executor2")显示指定  
  
4.动态加载定时任务(SchedulingConfigurer)


**优点**  
1.使用简单。   
2.支持cron表达式，支持固定频率执行。   
3.代码侵入性低。  

**缺点**  
1.无法解决分布式调度问题。   
2.无法监控任务状态。   
3.无其他骚功能。如失败提醒，重试等等。   

#### java自带的API java.util.Timer类和java.util.TimerTask类



#### 原理
ScheduledAnnotationBeanPostProcessor  
