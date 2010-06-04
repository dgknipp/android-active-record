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
	 * @return
	 */
	public static String javaToSqlNotation(String javaNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = javaNotation.toCharArray();
		for (char c : buf) {
			if (Character.isLowerCase(c)) {
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
	 * skip letter and add next letter to output string whithout converting to
	 * lower case
	 * 
	 * @param javaNotation
	 * @return
	 */
	public static String sqlToJavaNotation(String sqlNotation) {
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
	 * Convert name of format thisIsAName to THIS_IS_A_NAME For each letter: if
	 * lower case then convert to upper case if upper case then add '_' to
	 * output before ading letter
	 * 
	 * @param javaNotation
	 * @return
	 */
	public String javaToSqlNotation2(String javaNotation) {
		StringBuilder sb = new StringBuilder();
		char[] buf = javaNotation.toCharArray();
		for (char c : buf) {
			if (Character.isLowerCase(c)) {
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
	 * skip letter and add next letter to output string whithout converting to
	 * lower case
	 * 
	 * @param javaNotation
	 * @return
	 */
	public String sqlToJavaNotation2(String sqlNotation) {
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

}
