package gov.va.ascent.framework.audit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by vgadda on 8/17/17.
 */
public class BaseAuditAspect {

    /**
	 * This aspect defines the pointcut of methods that...
	 * 
	 * (1) are annotated with gov.va.ascent.framework.audit.Auditable
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
    @Pointcut("@annotation(gov.va.ascent.framework.audit.Auditable) && execution(* *(..))")
    protected final static void auditableExecution(){}
    
    /**
	 * This aspect defines the pointcut of standard REST controller.  Those are controllers that...
	 * 
	 * (1) are annotated with org.springframework.web.bind.annotation.RestController
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	protected final static void auditRestController() {}
	
	/**
	 * This aspect defines the pointcut of standard REST endpoints.  Those are endpoints that...
	 * 
	 * (1) are rest controllers (see that pointcut)
	 * (2) the method is public
	 * (3) the method returns org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+>
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("execution(public org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+> *(..))")
	protected final static void auditPublicServiceResponseRestMethod() {}
	
    /**
     * Gets the method and arguments as string.
     *
     * @param joinPoint the join point
     * @return the method and arguments as string
     */
    protected String getMethodAndArgumentsAsString(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).map(arg -> arg.toString())
                .collect(Collectors.joining(", ", getMethodName(joinPoint) + "(", ")"));
    }

    /**
     * Gets the method name.
     *
     * @param joinPoint the join point
     * @return the method name
     */
    protected String getMethodName(ProceedingJoinPoint joinPoint) {
        return MethodSignature.class.cast(joinPoint.getSignature()).getMethod().getName();
    }

    /**
     * Gets the result as string.
     *
     * @param result the result
     * @return the result as string
     */
    protected String getResultAsString(Object result) {
        return new StringBuilder(" returned ").append(result).toString();
    }

    /**
     * Gets the exception as string.
     *
     * @param ex the ex
     * @param duration the duration
     * @return the exception as string
     */
    protected String getExceptionAsString(Throwable ex, long duration) {
        return new StringBuilder(" threw ").append(ex.getClass().getSimpleName()).append(" after ").append(duration)
                .append(" msecs with message ").append(ex.getMessage()).toString();
    }
    
    /**
     * Gets the default auditable instance.
     *
     * @param method the method
     * @return the auditable instance
     */
    public static Auditable getDefaultAuditableInstance(final Method method) {
        Auditable auditableAnnotation = new Auditable()
        {
			@Override
			public AuditEvents event() {
				return AuditEvents.REQUEST_RESPONSE;
			}

			@Override
			public String activity() {
				if (method==null) {
					return "";
				} else {
					return method.getName();
				}
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return Auditable.class;
			}

			@Override
			public String auditClass() {
				if (method==null) {
					return "";
				} else {
					return method.getDeclaringClass().getName();
				}
			}
        };
        
        return auditableAnnotation;
    }
    
    /**
     * Gets the modified auditable instance from Method argument.
     *
     * @param auditable the auditable
     * @param method the method
     * @return the auditable instance
     */
    public static Auditable getAuditableInstance(final Auditable auditable, final Method method) {
    	
    	if (method==null) {
    		return auditable;
    	} 
    	else if (auditable!=null) {
	        Auditable auditableAnnotation = new Auditable()
	        {
				@Override
				public AuditEvents event() {
					if (auditable.event()==null) {
						return AuditEvents.REQUEST_RESPONSE;
					} else {
						return auditable.event();
					}
				}
	
				@Override
				public String activity() {
					if (auditable.activity()==null) {
						return method.getName();
					} else {
						return auditable.activity();
					}
				}
	
				@Override
				public String auditClass() {
					if (auditable.auditClass()==null) {
						return method.getDeclaringClass().getName();
					} else {
						return auditable.auditClass();
					}
				}
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return Auditable.class;
				}
	        };
	        return auditableAnnotation;
    	} 
    	else {
    		return null;
    	}	
    }
}
