package com.andy.yy.base.log;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
@Aspect
public class MethodAroundLoggerAspect {

	private LoggerUtils loggerUtils = LoggerUtils.newLogger("method-around-log");

	private static TraceStack<Integer> traceStack = new TraceStack();
	private static Integer EXCITE_IDENTIFY = 1;

	@Around("execution(public * com.andy.yy..*.*Service.*(..)) || " +
			"execution(public * com.andy.yy..*.*ServiceImpl.*(..)) || " +
			"execution(public * com.andy.yy..*.*Controller.*(..))")
	public Object methodAroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
		traceStack.push(EXCITE_IDENTIFY);
		try {
			return processMethodLog(joinPoint);
		} finally {
			LoggerUtils.decrMethodSequence();
			traceStack.pop();
			if(traceStack.isEmpty()) {
				LoggerUtils.clearContext();
			}
		}
	}

	private Object processMethodLog(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Class<?> returnType = methodSignature.getReturnType();
		boolean noReturnValue = Void.class.equals(returnType) || Void.TYPE.equals(returnType);
		String argInfo = getArgsInfo(joinPoint, methodSignature);
		Object retVal = null;
		try {
			if (noReturnValue) {
				joinPoint.proceed(joinPoint.getArgs());
			} else {
				retVal = joinPoint.proceed(joinPoint.getArgs());
			}
			loggerUtils.infoMethod(System.currentTimeMillis() - startTime, argInfo, JSON.toJSONString(retVal));
		} catch (Throwable ex) {
			loggerUtils.errorMethod(System.currentTimeMillis() - startTime, argInfo, ex);
			throw ex;
		}
		return retVal;
	}

	private static String[] getMethodParamNames(Method method) {
		String[] names =  new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
		if(names == null) {
			int len = method.getParameterCount();
			names = new String[len];
			for(int i = 0; i < len; i++) {
				names[i] = String.valueOf(i);
			}
		}
		return names;
	}

	private String getArgsInfo(ProceedingJoinPoint joinPoint, MethodSignature methodSignature) {
		String[] paramNames = getMethodParamNames(methodSignature.getMethod());
		Object[] paramValues = joinPoint.getArgs();
		StringBuilder sb = new StringBuilder();
		if (paramValues != null && paramValues.length > 0) {
			for (int i = 0; i < paramValues.length; ++i) {
				if(paramValues[i] != null) {
					if (paramValues[i] instanceof HttpServletRequest || paramValues[i] instanceof HttpServletResponse) {
						sb.append(paramNames[i]).append("[@").append(paramValues[i].getClass().getSimpleName()).append("], ");
					} else {
						sb.append(paramNames[i]).append("[").append(JSON.toJSONString(paramValues[i])).append("], ");
					}
				} else {
					sb.append(paramNames[i]).append("[null], ");
				}
			}
		}
		return sb.toString();
	}
}
