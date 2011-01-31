package org.kroz.activerecord.annotations;

import java.lang.reflect.Field;

import android.test.AndroidTestCase;

public class IgnoreAttributeAnnotationTest extends AndroidTestCase{
	
	private static class IgnoreValueClass {
		@ActiveRecordIgnoreAttribute
		public String ignoreMe;
		public String heyListen;
	}
	
	public void testValidateAnnotation() throws Exception {
		IgnoreValueClass ivc = new IgnoreValueClass();
		Field ignoreMe = ivc.getClass().getField("ignoreMe");
		Field heyListen = ivc.getClass().getField("heyListen");
		
		assertNotNull(ignoreMe.getAnnotation(ActiveRecordIgnoreAttribute.class));
		assertNull(heyListen.getAnnotation(ActiveRecordIgnoreAttribute.class));
	}

}
