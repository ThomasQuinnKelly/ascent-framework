package gov.va.ascent.framework.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.service.ServiceResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by vgadda on 8/17/17.
 */
@Aspect
@Component
public class RequestResponseAspect extends BaseAuditAspect{

    @Autowired
    ObjectMapper mapper;

    @Around("auditableExecution()")
    public void logRequestResponse(final ProceedingJoinPoint joinPoint){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Auditable auditableAnnotation = method.getAnnotation(Auditable.class);
        if(auditableAnnotation.event().equals(AuditEvents.REQUEST_RESPONSE)){
            ServiceResponse returnObject = null;
            ServiceRequest request = (ServiceRequest)joinPoint.getArgs()[0];
            try {
                returnObject = (ServiceResponse)joinPoint.proceed();
                AuditLogger.info("", AuditEvents.REQUEST_RESPONSE.name(), mapper.writeValueAsString(new RequestResponse(request, returnObject)));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }

        }
    }


}

class RequestResponse implements Serializable{
    private ServiceRequest request;

    private ServiceResponse response;

    public RequestResponse(ServiceRequest request, ServiceResponse response) {
        this.request = request;
        this.response = response;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(ServiceRequest request) {
        this.request = request;
    }

    public ServiceResponse getResponse() {
        return response;
    }

    public void setResponse(ServiceResponse response) {
        this.response = response;
    }
}
