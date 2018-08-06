package com.andy.yy.base.db;

import com.andy.yy.base.log.LoggerUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 可配置在接口、实现类以及方法中，覆盖规则:方法>实现类>接口
 * @author richard
 * @since 2018/1/31 16:01
 */
@Aspect
@Order(1)
public class DynamicDataSourceAspect {

	private static final LoggerUtils logger = LoggerUtils.newLogger(DynamicDataSourceAspect.class);

	@Pointcut("execution(* com.andy.yy.*.service.impl.*.*(..))")
	public void pointCut(){}

	@Before(value = "pointCut()")
	public void before(JoinPoint point) {
		Class<?> target = point.getTarget().getClass();
		MethodSignature signature = (MethodSignature) point.getSignature();
		for (Class<?> cls : target.getInterfaces()) {
			setDataSource(cls, signature.getMethod());
		}
		setDataSource(target, signature.getMethod());
	}

	private void setDataSource(Class<?> cls, Method method) {
		try {
			Class<?>[] types = method.getParameterTypes();
			if (cls.isAnnotationPresent(DataSourceKey.class)) {
				DataSourceKey source = cls.getAnnotation(DataSourceKey.class);
				DataSourceContext.setDataSourceKey(source.value());
			}
			Method m = cls.getMethod(method.getName(), types);
			if (m != null && m.isAnnotationPresent(DataSourceKey.class)) {
				DataSourceKey source = m.getAnnotation(DataSourceKey.class);
				DataSourceContext.setDataSourceKey(source.value());
			}
		} catch (Exception e) {
			logger.info("exception in dataSource aspect set dataSource: {}", e.getMessage());
		}
	}
}
