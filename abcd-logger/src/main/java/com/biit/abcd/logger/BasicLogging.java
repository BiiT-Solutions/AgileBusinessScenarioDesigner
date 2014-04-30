package com.biit.abcd.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class BasicLogging extends AbstractLogging {

	//@Before("execution(* com.biit.abcd.persistence.HibernateBasicExample.testCrud())")
	//@Before(value = "@annotation(loggable)")
	//@Before("execution(* com.biit.abcd..*.*(..))")
	public void logBefore(JoinPoint joinPoint) throws Throwable {
		log(joinPoint);
	}

	// within(com.biit.abcd..*)
	@Around("execution(* com.biit.abcd..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		Object returnValue = null;

		stopWatch.start();
		returnValue = joinPoint.proceed();
		stopWatch.stop();

		log(stopWatch.getTotalTimeMillis(), joinPoint);

		return returnValue;
	}
}
