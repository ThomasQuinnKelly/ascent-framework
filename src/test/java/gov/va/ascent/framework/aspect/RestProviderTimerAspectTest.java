package gov.va.ascent.framework.aspect;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.AbstractBaseLogTester;
import gov.va.ascent.framework.rest.provider.RestProviderTimerAspect;

@RunWith(MockitoJUnitRunner.class)
public class RestProviderTimerAspectTest extends AbstractBaseLogTester {

	/** Underlying implementation of AscentLogger */
	private Logger AspectLoggingLOG = super.getLogger(RestProviderTimerAspectTest.class).getLoggerBoundImpl();
	/** Underlying implementation of AscentLogger */
	private Logger AspectLoggingTestLOG = super.getLogger(RestProviderTimerAspectTest.class).getLoggerBoundImpl();

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private MethodSignature signature;

	@Mock
	private JoinPoint.StaticPart staticPart;

	@Override
	@Before
	public void setup() throws Throwable {
		when(proceedingJoinPoint.toLongString()).thenReturn("ProceedingJoinPointLongString");
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
		when(staticPart.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(myMethod());
	}

	@Override
	@After
	public void tearDown() {
		AspectLoggingLOG.setLevel(Level.DEBUG);
		AspectLoggingTestLOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testAroundAdviceDebugOn() throws Throwable {
		super.getAppender().clear();

		RestProviderTimerAspect restProviderTimerAspect = new RestProviderTimerAspect();
		restProviderTimerAspect.aroundAdvice(proceedingJoinPoint);

		assertEquals("PerformanceLoggingAspect executing around method:ProceedingJoinPointLongString",
				super.getAppender().get(0).getMessage());
		assertEquals("enter [RestProviderTimerAspectTest.someMethod]", super.getAppender().get(1).getMessage());
		assertEquals("PerformanceLoggingAspect after method was called.", super.getAppender().get(2).getMessage());
		assertEquals(Level.INFO, super.getAppender().get(3).getLevel());
	}

	public Method myMethod() throws NoSuchMethodException {
		return getClass().getDeclaredMethod("someMethod");
	}

	public void someMethod() {
		// do nothing
	}
}
