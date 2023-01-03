#### groovy与java混合编译
1. 由于的groovy中使用的jdk tools.jar包
    ```
        <dependency>
            <groupId>com.sun</groupId>
            <artifactId>tools</artifactId>
            <version>1.8</version>
            <scope>system</scope>
            <systemPath>${java.home}/../lib/tools.jar</systemPath>
        </dependency>
    ```
    在项目的全局依赖中加入对于tools.jar的依赖，否则编译会报错

2. 添加编译插件
    gmaven-plugin
    ```
        <plugin>
            <groupId>org.codehaus.groovy.maven</groupId>
            <artifactId>gmaven-plugin</artifactId>
            <version>1.0-rc-4</version>
            <executions>
                <execution>
                    <phase>compile</phase>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <sources>
                    <source>
                        <directory>src/main</directory>
                        <includes>
                            <include>**/*.groovy</include>
                        </includes>
                    </source>
                </sources>
                <stacktrace>true</stacktrace>
            </configuration>
        </plugin>
    ```
    gmavenplus-plugin
    ```
        <plugin>
            <groupId>org.codehaus.gmavenplus</groupId>
            <artifactId>gmavenplus-plugin</artifactId>
            <version>1.12.1</version>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <sources>
                    <source>
                        <directory>src/main</directory>
                        <includes>
                            <include>**/*.groovy</include>
                        </includes>
                    </source>
                </sources>
            </configuration>
        </plugin>
    ```

3. 配置运行
    gmaven-plugin
    ```
    groovy:compile compile
    ```
    gmavenplus-plugin
    ```
        gplus:compile compile
    ```
    或者为gmavenplus-plugin绑定生命周期到generate-sources这样，执行compile时，
    能保证它的compile在maven-compiler-plugin的前边运行，这样可以直接使用compile即可
    ```
    compile
    ```
    
4. groovy-maven-plugin   
    source内置groovy脚本
    ```
        <plugin>
            <groupId>org.codehaus.gmaven</groupId>
            <artifactId>groovy-maven-plugin</artifactId>
            <version>2.0</version>
           <configuration>
               <source>
                   println 'i can run groovy within maven!'
               </source>
           </configuration>
        </plugin>
    ```
    指定groovy脚本
    ```
        <plugin>
            <groupId>org.codehaus.gmaven</groupId>
            <artifactId>groovy-maven-plugin</artifactId>
            <version>2.0</version>
           <configuration>
               <source>${project.basedir}/src/main/groovy/hi.groovy</source>
           </configuration>
        </plugin>
    ```

5. groovy-eclipse-compiler
    为maven-compiler-plugin添加编译支持
    ```
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <encoding>utf-8</encoding>
                <compilerId>groovy-eclipse-compiler</compilerId>
                <verbose>true</verbose>
                <fork>true</fork>
                <compilerArguments>
                    <verbose/>
                    <bootclasspath>${JAVA_HOME}/jre/lib/rt.jar${path.separator}${JAVA_HOME}/jre/lib/jce.jar${path.separator}${JAVA_HOME}/lib/tools.jar</bootclasspath>
                    <javaAgentClass>lombok.launch.Agent</javaAgentClass>
                </compilerArguments>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>org.codehaus.groovy</groupId>
                    <artifactId>groovy-eclipse-compiler</artifactId>
                    <version>2.9.1-01</version>
                </dependency>
                <!-- for 2.8.0-01 and later you must have an explicit dependency on groovy-eclipse-batch -->
                <dependency>
                    <groupId>org.codehaus.groovy</groupId>
                    <artifactId>groovy-eclipse-batch</artifactId>
                    <version>2.3.7-01</version>
                </dependency>
                <dependency>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>1.16.4</version>
                </dependency>
            </dependencies>
        </plugin>
    ```