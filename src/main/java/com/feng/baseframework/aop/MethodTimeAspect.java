package com.feng.baseframework.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * baseframework
 * 2018/9/13 17:13
 * 方法执行完毕提取执行时间
 * 切面只能切入spring管理的bean
 *
 * @author lanhaifeng
 * @since
 **/

@Aspect
@Component
public class MethodTimeAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 开始时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    /**
     * map1存放方法被调用的次数O
     */
    ThreadLocal<Map<String, Long >> invokeMap = new ThreadLocal<>();

    /**
     * map2存放方法总耗时
     */
    ThreadLocal<Map<String, Long >> timeMap = new ThreadLocal<>();

    @Pointcut("execution(* com.feng.baseframework..*.*(..))")
    public void webLog(){}
    @Pointcut("@annotation(com.feng.baseframework.annotation.MethodTimeAop)")
    public void webLogAnnotation(){}

    @Before("webLog() && webLogAnnotation()")
    public void beforMethod(JoinPoint joinPoint) {
        logger.info("开始切入方法：" + joinPoint.getSignature().getName());
        startTime.set(System.currentTimeMillis());
    }

    @Around("webLog() && webLogAnnotation()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //初始化 一次
        if(invokeMap.get() ==null ){
            invokeMap.set(new HashMap<>());
        }

        if(timeMap.get() == null){
            timeMap.set(new HashMap<>());
        }

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            /*if(result==null){
                //如果切到了 没有返回类型的void方法，这里直接返回
                return null;
            }*/
            long end = System.currentTimeMillis();

            logger.info("===================");
            String tragetClassName = joinPoint.getSignature().getDeclaringTypeName();
            String MethodName = joinPoint.getSignature().getName();

            Object[] args = joinPoint.getArgs();// 参数
            int argsSize = args.length;
            String argsTypes = "";
            String typeStr = joinPoint.getSignature().getDeclaringType().toString().split(" ")[0];
            String returnType = joinPoint.getSignature().toString().split(" ")[0];
            logger.info("类/接口:" + tragetClassName + "(" + typeStr + ")");
            logger.info("方法:" + MethodName);
            logger.info("参数个数:" + argsSize);
            logger.info("返回类型:" + returnType);
            if (argsSize > 0) {
                // 拿到参数的类型
                for (Object object : args) {
                    argsTypes += object.getClass().getTypeName().toString() + " ";
                }
                logger.info("参数类型：" + argsTypes);
            }

            Long total = end - start;
            logger.info("耗时: " + total + " ms!");

            if(invokeMap.get().containsKey(MethodName)){
                Long count = invokeMap.get().get(MethodName);
                invokeMap.get().remove(MethodName);//先移除，在增加
                invokeMap.get().put(MethodName, count+1);

                count = timeMap.get().get(MethodName);
                timeMap.get().remove(MethodName);
                timeMap.get().put(MethodName, count+total);
            }else{
                invokeMap.get().put(MethodName, 1L);
                timeMap.get().put(MethodName, total);
            }

            return result;

        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            logger.info("====around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : "
                    + e.getMessage());
            throw e;
        }
    }

    @AfterReturning("webLog() && webLogAnnotation()")
    public void afterMethod(JoinPoint joinPoint){
        long end = System.currentTimeMillis();
        long total =  end - startTime.get();
        String methodName = joinPoint.getSignature().getName();
        logger.info("连接点方法为：" + methodName + ",执行总耗时为：" +total+"ms");

        //重新new一个map
        Map<String, Long> map = new HashMap<>();

        //从timeMap中将最后的 连接点方法给移除了，替换成最终的，避免连接点方法多次进行叠加计算
        //由于timeMap受ThreadLocal的保护，这里不支持remove，因此，需要单开一个map进行数据交接
        for(Map.Entry<String, Long> entry:timeMap.get().entrySet()){
            if(entry.getKey().equals(methodName)){
                map.put(methodName, total);

            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<String, Long> entry :invokeMap.get().entrySet()) {
            for(Map.Entry<String, Long> entry2 :map.entrySet()){
                if(entry.getKey().equals(entry2.getKey())){
                    logger.info(entry.getKey()+",被调用次数："+entry.getValue()+",综合耗时："+entry2.getValue()+"ms");
                }
            }

        }


    }
}
