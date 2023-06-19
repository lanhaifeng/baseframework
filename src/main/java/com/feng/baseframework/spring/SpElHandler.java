package com.feng.baseframework.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nullable;

/**
 * 类名:SpElHandler <br/>
 * 描述:spring el表达式 <br/>
 * 时间:2022/3/14 16:15 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Component
public class SpElHandler implements InitializingBean {

    private Environment environment;
    private ApplicationContext applicationContext;

    private ExpressionParser expressionParser;
    private StandardEvaluationContext context;
    private ParserContext parserContext;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 描述:解析spring EL表达式 <br/>
     * 如：#{'${spring.security.salt}' eq 'feng'}，其中读取配置文件中spring.security.salt值，再与字符串比较
     * 时间:16:21 2022/3/14 <br/>
     *
     * @param template
     * @param returnType
     * @author lanhaifeng
     * @return T
     */
    public <T> T getValue(String template, @Nullable Class<T> returnType) {
        template = environment.resolvePlaceholders(template);
        Expression expression = expressionParser.parseExpression(template, parserContext);
        return expression.getValue(context, returnType);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        expressionParser = new SpelExpressionParser();
        parserContext = new TemplateParserContext();
    }
}
