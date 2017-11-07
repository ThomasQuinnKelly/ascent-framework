package gov.va.ascent.framework.audit;

import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.service.ServiceResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class RequestResponseAspectTest {

	private AnnotationConfigWebApplicationContext context;
	private RequestResponseAspect requestResponseAspect;
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    private TestServiceResponse mockReturnObject = new TestServiceResponse();
    @Mock
    private MethodSignature mockSignature;
    private TestServiceRequest mockRequestObject = new TestServiceRequest();
    private Object[] mockArray = {mockRequestObject};
	@Before
	public void setUp() throws Exception {
		try{
	        context = new AnnotationConfigWebApplicationContext();
	        context.register(TestObjectMapperConfig.class);
	        context.refresh();
	        requestResponseAspect = context.getBean(RequestResponseAspect.class);	        
			when(proceedingJoinPoint.proceed()).thenReturn(mockReturnObject);
			when(proceedingJoinPoint.getArgs()).thenReturn(mockArray);
			when(proceedingJoinPoint.getSignature()).thenReturn(mockSignature);
			when(mockSignature.getMethod()).thenReturn(myMethod());
		}catch(Throwable e) {
			
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAnnotatedMethodRequestResponse() {
		Object obj = requestResponseAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		assertNotNull(obj);
	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeException() {

		try {
			Object[] array = {null, new Object()};
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			Object obj = requestResponseAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch(Throwable throwable){
			assertTrue(throwable instanceof RuntimeException);
		}

	}

	@Test
	public void testAnnotatedMethodRequestResponseRunTimeExceptionArrayZero() {

		try {
			Object[] array = new Object[0];
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			Object obj = requestResponseAspect.logAnnotatedMethodRequestResponse(proceedingJoinPoint);
		} catch(Throwable throwable){
			assertTrue(throwable instanceof RuntimeException);
		}

	}

	@Test
	public void testlogRestPublicMethodRequestResponseRunTimeException() {

		try {
			Object[] array = {null, new Object()};
			when(proceedingJoinPoint.getArgs()).thenReturn(array);
			when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Unit Test Exception"));
			Object obj = requestResponseAspect.logRestPublicMethodRequestResponse(proceedingJoinPoint);
		} catch(Throwable throwable){
			assertTrue(throwable instanceof RuntimeException);
		}


	}

	@Test
	public void testLogRestPublicMethodRequestResponse() {
		try{
			Object obj = requestResponseAspect.logRestPublicMethodRequestResponse(proceedingJoinPoint);
			assertNotNull(obj);
		}catch(Throwable e) {
			
		}
	}

    public Method myMethod() throws NoSuchMethodException{
        return getClass().getDeclaredMethod("someMethod");
    }

    @Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "unittestactivity")
    public void someMethod() {
        // do nothing
    }	
}
class TestServiceRequest extends ServiceRequest {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }	
}
class TestServiceResponse extends ServiceResponse {
	
}
