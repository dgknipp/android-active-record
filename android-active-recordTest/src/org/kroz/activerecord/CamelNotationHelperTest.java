package org.kroz.activerecord;

import org.kroz.activerecord.CamelNotationHelper;

import android.test.AndroidTestCase;

public class CamelNotationHelperTest extends AndroidTestCase {

	final String sqlStr1 = "THIS_IS_A_NAME";
	final String javaStr1 = "thisIsAName";
	
	final String sqlStr2 = "A_CLASS_NAME";
	final String javaStr2 = "AClassName";

	public void testJavaToSqlNotation() {
		assertEquals("Got: "+CamelNotationHelper.toSQLName(javaStr1), sqlStr1, CamelNotationHelper.toSQLName(javaStr1));
		assertEquals("Got: "+CamelNotationHelper.toSQLName(javaStr2), sqlStr2, CamelNotationHelper.toSQLName(javaStr2));
	}

	public void testSqlToJavaNotation() {
		assertEquals(javaStr1, CamelNotationHelper.toJavaMethodName(sqlStr1));
		assertEquals(javaStr2, CamelNotationHelper.toJavaClassName(sqlStr2));
	}

}
