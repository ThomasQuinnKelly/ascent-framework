package gov.va.ascent.framework.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.validation.ModelValidator.Modes;

public class ModelValidatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateModelForEmptyMessages() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		assertTrue(modelTest.validateModel(tr, messages, (Class[]) null));
	}
	
	@Test
	public void testValidateModelForNonEmptyMessages() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		Class<?>[] classes = new Class[1];
		classes[0] = Default.class;			
		
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		
		List<ViolationMessageParts> msgList = new ArrayList<>();
		msgList.add(violationMessageParts);
		
		Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		messages.put("TestKey", msgList);
		assertTrue(modelTest.validateModel(tr, messages, classes));
	}	

	@Test
	public void testValidateModelPropertiesForEmptyMessages() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		assertTrue(modelTest.validateModelProperties(tr, messages, (Class[]) null));
	}
	
	@Test
	public void testValidateModelPropertiesForNonEmptyMessages() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		
		ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		List<ViolationMessageParts> msgList = new ArrayList<>();
		msgList.add(violationMessageParts);
		messages.put("TestKey", msgList);
		Class<?>[] classes = new Class[1];
		classes[0] = Default.class;	
		
		assertTrue(modelTest.validateModelProperties(tr, messages, classes));
	}

	@Test
	public void testConvertKeyToNodepathStyle() {
		String retVal = (ModelValidator.convertKeyToNodepathStyle("TestKey", "message value1.message value2"));
		assertTrue(retVal.contains("."));
		assertTrue("TestKey.message value1".equals(retVal));
	}

	@Test
	public void testDoValidate() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		assertNotNull(modelTest.doValidate(tr, (Class[]) null));
	}

	@Test
	public void testDoValidateProperty() {
		ModelValidator modelTest = new ModelValidator();
		TestRequest tr = new TestRequest();
		Class<?>[] classes = new Class[1];
		classes[0] = Default.class;		
		assertNotNull(modelTest.doValidateProperty(tr, "TestKey", classes));
	}

	@Test
	public void testModes() {
		assertNotNull(Modes.MODEL);
		assertNotNull(Modes.PROPERTIES);
	}
}
class TestRequest extends ServiceRequest {
	
}