spring boot练习项目,集成一些常用的框架

##### 手动回滚事务
```
    @Transactional(rollbackFor = Exception.class)
    @Test
    public void testTransactional() {
        try {
           
        } catch (Exception e) {
            log.error("新增失败错误：" + ExceptionUtils.getFullStackTrace(e));
            //通常情况下，主动回滚事务，可以手动抛异常即可，不抛异常可以如下方式回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
```

#### java17模块不可见
java compiler
```txt
-parameters -bootclasspath D:\jdk_64_17.0.2/lib/jrt-fs.jar
 --add-exports java.base/sun.security.x509=ALL-UNNAMED
 --add-exports java.base/sun.security.tools.keytool=ALL-UNNAMED
```

#### 初始化
```
mysql -uroot -proot soc < initTable.sql
mysql -uroot -proot soc < quartz-ddl.sql
```

#### 特征
1. springboot框架搭建
2. 新增md5、json处理工具类
3. 统一异常、数据返回封装
4. 集成spring security
5. 集成redis简单使用
6. 自定义spring security认证
7. 集成测试框架mockito和powerMockito
8. 配置restTemplate支持https
9. 集成ehcache本地缓存
10. 集成solr进行度写操作
11. 防盗链实现
12. 常用设计模式实现：责任链
13. 集成kafka，基本的消费、生产练习
14. 补充记录学习文档，见docs目录
15. 进程通信学习：信号量、父子进程间通信、管道、共享内存
16. 集成工具japidoc、smart-doc、easycode插件
17. 常用设计模式实现：阻塞队列实现生产者消费者模式
18. 学习java8：时间处理、lambda、方法引用
19. 集成quartz分布式调度任务
20. 升级springboot到2.7
21. 利用actuator实现热重启
22. 集成文件预览功能
23. 升级jdk17和springboot为3.1.0
24. 测试相似算法
25. 实现本地配置自动刷新
26. 测试redis、as的lua脚本
