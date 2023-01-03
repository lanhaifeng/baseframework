#### spring bean定义创建与实例化

第一步：将解析到的BeanDefinition注册到容器

org.springframework.beans.factory.support.DefaultListableBeanFactory#registerBeanDefinition



第二步：由BeanDefinition创建实例

org.springframework.context.event.ApplicationListenerMethodAdapter#onApplicationEvent->processEvent->doInvoke->getTargetBean->getBean

而

org.springframework.beans.factory.support.AbstractBeanFactory #getBean

中触发创建bean实例

https://blog.csdn.net/wkwk12345/article/details/108171620

https://www.cnblogs.com/loongk/p/12262101.html

#### 注册自定义BeanDefinition

**目的：**

扫描某个自定义注解标注的类， 或者自定义xml

为这些类生成spring Bean

 

基本原理：org.springframework.beans.factory.support.DefaultListableBeanFactory#registerBeanDefinition

 

BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)applicationContext.getAutowireCapableBeanFactory();



BeanDefinition的定义使用org.springframework.beans.factory.support.BeanDefinitionBuilder

```java
BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Person.class);
```

http://fetosoft.cn/archives/2020/06/04/208



#### 动态向容器中添加或移除bean

添加bean

org.springframework.beans.factory.support#registerBeanDefinition

移除bean

org.springframework.beans.factory.support#removeBeanDefinition

https://www.cnblogs.com/yjmyzz/p/how-to-dynamic-destroy-or-register-bean-to-spring-container.html



#### 利用BeanDefinitionRegistryPostProcessor动态替换bean

解决某些场景下不好mock的情况，例如将异步调用改为同步调用

https://blog.csdn.net/teaandnoodle/article/details/103105061