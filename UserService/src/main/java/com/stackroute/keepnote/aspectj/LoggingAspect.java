package com.stackroute.keepnote.aspectj;

/* Annotate this class with @Aspect and @Component */

import com.stackroute.keepnote.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of Category controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	@Before(value = "execution(* com.stackroute.keepnote.controller.UserController .*(..))")
	public void beforeAdvicemethod(JoinPoint joinPoint ){

		logger.info("inside the before advice");
		logger.info("Target method object" + joinPoint.getSignature().getName());

	}

	@After(value = "execution(* com.stackroute.keepnote.controller.UserController .*(..)) && args (user)")
	public void afterAdvicemethod(JoinPoint joinPoint , User user){

		logger.info("inside the after  advice");
		logger.info("Target method object --->" + joinPoint.getSignature().getName());
		logger.info("Object with following data will be persisted " + user);
	}

	@AfterReturning(value = "execution(* com.stackroute.keepnote.controller.UserController .*(..))" , returning = "retval")
	public void afterReturningAdvice(JoinPoint joinPoint , Object retval) {

		logger.info("inside the afterReturn  advice");
		logger.info("Target method object --->" + joinPoint.getSignature().getName());
		logger.info("Object with following data will be persisted " + retval);
	}
}
