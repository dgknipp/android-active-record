package org.kroz.activerecord;

/**
 * Converts names to/from Java to SQL according to naming convention
 * 
 * @author Vladimir Kroz (AKA vkroz)
 * @author jeremyot
 * 
 *         This project based on and inspired by 'androidactiverecord' project
 *         written by JEREMYOT
 * 
 */
public class CamelNotationHelper {

	public CamelNotationHelper() {
	}

	/**
	 * Convert name of format thisIsAName to THIS_IS_A_NAME For each letter: if
	 * lower case then convert to upper case if upper case then add '_' to
	 * output before ading letter
	 * 
	 * @param javaNotation
	 * @return SQL name translated from Java name
	 */
	public static String toSQLName(String javaNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = javaNotation.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (Character.isLowerCase(c) || i==0 ) {
				sb.append(Character.toUpperCase(c));
			} else if (Character.isUpperCase(c)) {
				sb.append('_').append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * Convert name of format THIS_IS_A_NAME to thisIsAName For each letter: if
	 * not '_' then convert to lower case and add to output string if '_' then
	 * skip letter and add next letter to output string without converting to
	 * lower case
	 * 
	 * @param sqlNotation
	 * @return A name complaint with naming convention for Java methods and
	 *         fields, converted from SQL name
	 */
	public static String toJavaMethodName(String sqlNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = sqlNotation.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (c != '_') {
				sb.append(Character.toLowerCase(c));
			} else {
				i++;
				if (i < buf.length) {
					sb.append(buf[i]);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert name of format THIS_IS_A_NAME to ThisIsAName For each letter: if
	 * not '_' then convert to lower case and add to output string if '_' then
	 * skip letter and add next letter to output string without converting to
	 * lower case
	 * 
	 * @param sqlNotation
	 * @return A name complaint with naming convention for Java classes,
	 *         converted from SQL name
	 */
	public static String toJavaClassName(String sqlNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = sqlNotation.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (i == 0) {
				sb.append(buf[i]);
			} else if (c != '_') {
				sb.append(Character.toLowerCase(c));
			} else {
				i++;
				if (i < buf.length) {
					sb.append(buf[i]);
				}
			}
		}
		return sb.toString();
	}

}
