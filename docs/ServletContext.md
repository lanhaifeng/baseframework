#### 设置context-param
1. EmbeddedServletContainerCustomizer
```
    @Bean
    public EmbeddedServletContainerCustomizer webServerFactoryCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                configurableEmbeddedServletContainer.addInitializers(servletContextInitializer());
            }
        };
    }

    public ServletContextInitializer servletContextInitializer(){
            Map<String, String> contextParams = new HashMap<>();
            contextParams.put("logbackConfigLocation", "classpath:log/logback.xml");
            return new InitParameterConfiguringServletContextInitializer(contextParams);
    }
```

2. WebApplicationContext
```
    @Autowired
    private WebApplicationContext webApplicationConnect;

    @Bean
    public WebAppRootListener webAppRootListener(){
        //设置环境上下文
        //项目根路径
        ServletContext servletContext = webApplicationConnect.getServletContext();
        servletContext.setInitParameter("webAppRootKey","projectRootPath");
        return new WebAppRootListener();
    }
```

3. ApplicationListener
```
@Component
public class SettingDataInitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 将 ApplicationContext 转化为 WebApplicationContext 
        WebApplicationContext webApplicationContext = 
            (WebApplicationContext)contextRefreshedEvent.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext 
        ServletContext servletContext = webApplicationContext.getServletContext();
        // servletContext设置值
        servletContext.setAttribute("key", "value");
    }
}
```

#### 获取ServletContext
1.HttpServletRequest
```
ServletContext sc = request.getServletContext();
```

2.自动注入
```
@Autowired
private ServletContext servletContext;
```

3.WebApplicationContext
```
@Autowired
WebApplicationContext webApplicationConnect;

ServletContext servletContext = webApplicationConnect.getServletContext();
```