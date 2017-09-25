package gov.va.ascent.framework.rest.provider;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceResponse;

/**
 * The Class RestHttpResponseCodeAspect is an aspect to alter HTTP response codes from our REST endpoints.
 * It defers to the MessagesToHttpStatusRulesEngine to determine codes.  
 * 
 * This aspect pointcuts on standard REST endpoints.  
 * Ensure you follow that pattern to make use of this standard aspect.
 *
 * @author jshrader
 * @see gov.va.ascent.framework.rest.provider.BaseRestProviderAspect 
 */
@Aspect
@Order(-9998)
public class RestProviderHttpResponseCodeAspect extends BaseRestProviderAspect {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProviderHttpResponseCodeAspect.class);
	
	/** The rules engine. */
	private MessagesToHttpStatusRulesEngine rulesEngine;

	/**
     * Instantiates a new RestHttpResponseCodeAspect using a default MessagesToHttpStatusRulesEngine.
     * 
     * Use a custom bean and the other constructor to customize the rules.
     *
     * @param rulesEngine the rules engine
     */
	public RestProviderHttpResponseCodeAspect() {
		MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR)); 
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.ERROR, 
						HttpStatus.BAD_REQUEST));
		this.rulesEngine = ruleEngine;
    }
    
    /**
     * Instantiates a new RestHttpResponseCodeAspect using the specified MessagesToHttpStatusRulesEngine
     *
     * @param rulesEngine the rules engine
     */
    public RestProviderHttpResponseCodeAspect(MessagesToHttpStatusRulesEngine rulesEngine) {
    	this.rulesEngine = rulesEngine;
    }
	
	/**
	 * Around advice executes around the pointcut.
	 *
	 * @param joinPoint the join point
	 * @return the response entity
	 * @throws Throwable the throwable
	 */
	@Around("restController() && publicServiceResponseRestMethod()")
	public ResponseEntity<ServiceResponse> aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("RestHttpResponseCodeAspect executing around method:" + joinPoint.toLongString());			
		}
		
		ResponseEntity<ServiceResponse> returnObject = null;
        try {
            returnObject = (ResponseEntity<ServiceResponse>) joinPoint.proceed();
            ServiceResponse serviceResponse = returnObject.getBody();
            if (serviceResponse == null) {
            	serviceResponse = new ServiceResponse();
            }
			final HttpStatus ruleStatus = rulesEngine.messagesToHttpStatus(serviceResponse.getMessages());
			if (ruleStatus != null) {
				returnObject = new ResponseEntity<>(serviceResponse, ruleStatus);
			}
        } catch (Throwable throwable) {
        	LOGGER.error("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint Throwable Cause.", throwable.getCause());
        	LOGGER.error("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint Throwable Message.", throwable.getMessage());
        	AscentRuntimeException ascentRuntimeException;
        	if(throwable instanceof AscentRuntimeException){
        		ascentRuntimeException = (AscentRuntimeException) throwable;
        	} else {
        		ascentRuntimeException = new AscentRuntimeException(throwable);
        	}
        	LOGGER.error("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint.", ascentRuntimeException);
            ServiceResponse serviceResponse = new ServiceResponse();
            serviceResponse.addMessage(MessageSeverity.FATAL, "UNEXPECTED_ERROR", ascentRuntimeException.getMessage());
            returnObject = new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
        	LOGGER.debug("RestHttpResponseCodeAspect after method was called.");
        }
        return returnObject;
    }
	
}
