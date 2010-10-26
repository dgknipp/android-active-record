package org.kroz.activerecord;

import org.kroz.activerecord.CamelNotationHelper;

import android.test.AndroidTestCase;

/**
 * Checks CamelNotationHelper methods
 * @author vkroz
 * @see CamelNotationHelperTest
 *
 */
public class CamelNotationHelperTest extends AndroidTestCase {

	final String[] sqlStr = { "AB_CD", "THIS_IS_A_NAME", "DAO", "DB_CHANGES",
			"SHOWPLACE", "SHOWPLACE_DETAILS", "SHOWPLACE_DETAILS_VO", "_SOME" };
	final String[] javaClassStr = { "AbCd", "ThisIsAName", "Dao", "DbChanges",
			"Showplace", "ShowplaceDetails", "ShowplaceDetailsVo", "_some" };
	final String[] javaMethodStr = { "abCd", "thisIsAName", "dao", "dbChanges",
			"showplace", "showplaceDetails", "showplaceDetailsVo", "_some" };

	public void testJavaToSqlNotation() {
		for (int i = 0; i < javaClassStr.length; i++) {
			String str = CamelNotationHelper.toSQLName(javaClassStr[i]);
			assertEquals("i="+i+": Expected '"+sqlStr[i]+"', got '"+str+"'", sqlStr[i], str);
		}
	}

	public void testSqlToJavaClassName() {
		for (int i = 0; i < sqlStr.length; i++) {
			String str = CamelNotationHelper.toJavaClassName(sqlStr[i]);
			assertEquals("i="+i+": Expected '"+javaClassStr[i]+"', got '"+str+"'", javaClassStr[i], str);
		}
	}

	public void testSqlToJavaMethodName() {
		for (int i = 0; i < sqlStr.length; i++) {
			String str = CamelNotationHelper.toJavaMethodName(sqlStr[i]);
			assertEquals("i="+i+": Expected '"+javaMethodStr[i]+"', got '"+str+"'", javaMethodStr[i], str);
		}
	}

}
