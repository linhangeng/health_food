package com.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName ManageAop
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
@Aspect // 切面注解
@Component // springBean
@Order(10)  // 定义切面执行顺序，数值越小越先执行
@Slf4j
public class ManageAop {



    // 切入点
    @Pointcut("execution(* com.example.controller..*(..))")
    public void manageAop(){}

    // 环绕通知 @Around 方法执行前后执行 注意：环绕通知必须调用 pjp.proceed() 来执行目标方法
    // 前置通知 @before 方法执行前执行
    // 后置通知 @after 方法执行后执行
    // @AfterReturning 返回通知，在目标方法正常返回后执行
    // @AfterThrowing  异常通知，在目标方法抛出异常后执行
    @Around("manageAop()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        // 具体业务逻辑 ... 忽略


        // 设置
        result = pjp.proceed();
        UserContext.setLocalUserId(1);


        // 环绕通知中若不调用 pjp.proceed()，目标方法将永远不会执行。
        return result;
    }
}
