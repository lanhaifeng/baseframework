1. Editor->Live Templates

   -   add Template group myGroup
   -   add Live Template m
   -   Template info
        Abbreviation
        ```
        m
        ```
        Template text
        ```
        /**
        $context$
        */
        ```
        Edit variables
        ```
        groovyScript("F:\\IDEALiveTemplate.groovy",
        methodName(), methodParameters(), methodReturnType(),
         date("yyyy-MM-dd"), time("HH:mm:ss"))
        ```
        or
        ```
        groovyScript("F:\\IDEALiveTemplate.groovy",
                methodName(), methodParameters(), methodReturnType(),
                 date("yyyy-MM-dd"), time("HH:mm:ss"), "projectName")
        ```

2. IDEALiveTemplate.groovy
```
见src\main\java\com\feng\baseframework\groovy\IDEALiveTemplate.groovy
```
将该文件放在F:盘下