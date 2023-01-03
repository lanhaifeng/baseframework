package com.feng.baseframework.expression;

import com.feng.baseframework.model.User;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @ProjectName: baseframework
 * @Description: spring表达式语言
 * @Author: lanhaifeng
 * @CreateDate: 2019/6/15 10:32
 * @UpdateUser:
 * @UpdateDate: 2019/6/15 10:32
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SpringExpressionTest {

    @Test
    public void testBeanPropertyReplace(){
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
                this.getClass().getClassLoader());

        //创建SpEL表达式的解析器
        ExpressionParser parser=new SpelExpressionParser(config);

        String source = "'修改用户' + #user.name + '失败'";

        User user = new User();
        user.setName("test");

        //解析表达式需要的上下文，解析时有一个默认的上下文
        EvaluationContext ctx = new StandardEvaluationContext();
        //在上下文中设置变量，变量名为user，内容为user对象
        ctx.setVariable("user", user);

        String target = (String)parser.parseExpression(source).getValue(ctx);
        Assert.state("修改用户test失败".equals(target), "bean属性替换失败");

        target = (String)parser.parseExpression("'修改用户' + name + '失败'").getValue(user);
        Assert.state("修改用户test失败".equals(target), "bean属性替换失败");

        ctx.setVariable("user", null);
        User  testUser = (User)ctx.lookupVariable("user");
        Assert.isNull(testUser, "bean获取属性失败");
    }
}
