package gov.va.ascent.framework.log;

import static gov.va.ascent.framework.log.LogHttpRequestInterceptor.CLIENT_REQUEST_MESSAGE_TEXT;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;

import gov.va.ascent.framework.log.HttpLoggingUtils.ByteArrayTransportOutputStream;

@RunWith(SpringRunner.class)
public class LogHttpRequestInterceptorTest {

	private static final String TEST_SAMPLE_SOAP_MESSAGE = "test sample soap message";

	/** The output capture. */
	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Mock
	private WebServiceMessage request;

	@Mock
	private MessageContext messageContext;

	@Test
	public void handleRequestTest() {

		try {
			doAnswer((Answer) invocation -> {
				ByteArrayTransportOutputStream arg0 =
						invocation.getArgumentAt(0, HttpLoggingUtils.ByteArrayTransportOutputStream.class);
				arg0.write(TEST_SAMPLE_SOAP_MESSAGE.getBytes("UTF-8"));
				return null;
			}).when(request).writeTo(any(HttpLoggingUtils.ByteArrayTransportOutputStream.class));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Should not throw execption");
		}
		when(messageContext.getRequest()).thenReturn(request);

		LogHttpRequestInterceptor interceptor = new LogHttpRequestInterceptor();
		interceptor.handleRequest(messageContext);

		final String outString = outputCapture.toString();

		assertTrue(outString.contains(CLIENT_REQUEST_MESSAGE_TEXT));
		assertTrue(outString.contains(TEST_SAMPLE_SOAP_MESSAGE));
	}

	@Test
	public void handleResponseTest() {
		LogHttpRequestInterceptor interceptor = new LogHttpRequestInterceptor();
		assertTrue(interceptor.handleResponse(messageContext));
	}

	@Test
	public void handleFaultTest() {
		LogHttpRequestInterceptor interceptor = new LogHttpRequestInterceptor();
		assertTrue(interceptor.handleFault(messageContext));
	}

}
