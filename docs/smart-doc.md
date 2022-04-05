# 集成
## 代码方式集成
1. 导入依赖
```xml
<dependency>
    <groupId>com.github.shalousun</groupId>
    <artifactId>smart-doc</artifactId>
    <version>2.4.1</version>
</dependency>
```
2. 通过代码生成
```text
public static void main(String[] args) {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://localhost:8080");
        //设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
        config.setMd5EncryptedHtmlName(true);
        config.setStrict(false);//true会严格要求注释，推荐设置true
//        config.setOutPath(DocGlobalConstants.HTML_DOC_OUT_PATH);//输出到static/doc下
        config.setOutPath("D:\\data\\smart-api");
        //不指定SourcePaths默认加载代码为项目src/main/java下的,若是项目的某一些实体来自外部代码能够一块儿加载
        config.setSourceCodePaths(
                SourceCodePath.builder().setDesc("本项目代码").setPath("src/main/java")
        );
        config.setPackageFilters("com.feng.baseframework.controller");
        long start = System.currentTimeMillis();
        HtmlApiDocBuilder.buildApiDoc(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }
```
## 插件方式集成
```xml
			<plugin>
				<groupId>com.github.shalousun</groupId>
				<artifactId>smart-doc-maven-plugin</artifactId>
				<version>2.4.1</version>
				<configuration>
					<!--指定生成文档的使用的配置文件,配置文件放在自己的项目中-->
					<configFile>./src/main/resources/smart-doc.json</configFile>
					<!--指定项目名称-->
					<projectName>测试DEMO</projectName>
					<excludes>
						<exclude>
							bouncycastle:bcprov-jdk14
						</exclude>
						<exclude>
							org.jyaml:jyaml:jar:sources
						</exclude>
						<exclude>
							org.apache.poi:poi-ooxml-schemas
						</exclude>
						<exclude>
							org.apache.xmlbeans:xmlbeans
						</exclude>
						<exclude>
							com.feng:memory-calculation
						</exclude>
						<exclude>
							commons-beanutils:commons-beanutils
						</exclude>
						<exclude>
							com.google.code.findbugs:annotations
						</exclude>
						<exclude>
							com.sun.jdmk:jmxtools
						</exclude>
						<exclude>net.sf.ehcache:ehcache</exclude>
						<exclude>org.gnu:gnu-crypto</exclude>
					</excludes>
					<!--<includes>
						<include></include>
					</includes>-->
				</configuration>
				<executions>
					<execution>
						<!--如果不需要在执行编译时启动smart-doc，则将phase注释掉-->
						<phase>compile</phase>
						<goals>
							<goal>markdown</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```
2. smart配置文件
```json
{
  "projectName": "smart-doc",
  "serverUrl": "http://127.0.0.1:9090",
  "outPath": "D:\\data\\smart-api",
  "packageFilters": "com.feng.baseframework.controller",
  "allInOneDocFileName":"api.html",
  "allInOne": true,
  "showAuthor": true,
  "showJavaType": true,
  "ignoreRequestParams":[
    "org.springframework.ui.ModelMap"
  ],
  "dataDictionaries": [
    {
      "title": "API状态码字典",
      "enumClassName": "com.feng.baseframework.constant.ResultEnum",
      "codeField": "code",
      "descField": "message"
    }
  ],
  "errorCodeDictionaries": [],
  "customResponseFields": [
    {
      "name": "resultCode",
      "desc": "Response code",
      "value": "200"
    },
    {
      "name": "resultMsg",
      "desc": "错误信息",
      "value": null
    }
  ],
  "requestHeaders": [
    {
      "name": "token",
      "type": "string",
      "desc": "存放于cookie的校验信息",
      "required": true,
      "since": "-"
    }
  ],
  "revisionLogs": [{
    "version": "1.0",
    "revisionTime": "2020-12-31 10:30",
    "status": "update",
    "author": "author",
    "remarks": "desc"
  }]
}
```

# 官方文档
+ [FAQ](https://smart-doc-group.github.io/#/zh-cn/QA?id=syntax-error%ef%bc%9f)
+ [start](https://smart-doc-group.github.io/#/zh-cn/?id=smart-doc)
+ [文档注解](http://yui.github.io/yuidoc/syntax/index.html)
+ [javadoc](https://docs.oracle.com/javase/1.5.0/docs/tooldocs/solaris/javadoc.html#documentationcomments)