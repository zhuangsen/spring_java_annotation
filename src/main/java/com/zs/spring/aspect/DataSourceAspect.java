package com.zs.spring.aspect;

import com.zs.spring.annoation.DataSource;
import com.zs.spring.config.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-28 17:48
 * @description:
 */
//@Component
@Slf4j
//@Order(1)
//@Aspect
//@EnableAspectJAutoProxy
public class DataSourceAspect {

    //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    @Pointcut("execution(* com.zs.spring.service..*Impl.*(..))")
    public void aspect() { }

    @Before("aspect()")
    public void before(JoinPoint joinPoint) {
        log.info("-------before------");
        Class<?> target = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        resolveDataSource(target, signature.getMethod());
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     *
     * @param clazz
     * @param method
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                DynamicDataSource.key.set(source.value());
            }
            // 方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                DynamicDataSource.key.set(source.value());
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
