package org.kroz.activerecord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.kroz.activerecord.annotations.ActiveRecordIgnoreAttribute;

/**
 * Converts names to/from Java to SQL according to naming convention
 * 
 * @author Vladimir Kroz
 * 
 * <p>This project based on and inspired by 'androidactiverecord' project written by JEREMYOT</p>
 */
public class CamelNotationHelper {

	private static final String[] BLACKLIST = {
		"ABORT","ACTION","ADD","AFTER","ALL","ALTER","ANALYZE","AND","AS","ASC",
		"ATTACH","AUTOINCREMENT","BEFORE","BEGIN","BETWEEN","BY","CASCADE","CASE",
		"CAST","CHECK","COLLATE","COLUMN","COMMIT","CONFLICT","CONSTRAINT","CREATE"
		,"CROSS","CURRENT_DATE","CURRENT_TIME","CURRENT_TIMESTAMP","DATABASE","DEFAULT",
		"DEFERRABLE","DEFERRED","DELETE","DESC","DETACH","DISTINCT","DROP","EACH","ELSE",
		"END","ESCAPE","EXCEPT","EXCLUSIVE","EXISTS","EXPLAIN","FAIL","FOR",
		"FOREIGN","FROM","FULL","GLOB","GROUP","HAVING","IF","IGNORE","IMMEDIATE",
		"IN","INDEX","INDEXED","INITIALLY","INNER","INSERT","INSTEAD","INTERSECT",
		"INTO","IS","ISNULL","JOIN","KEY","LEFT","LIKE","LIMIT","MATCH","NATURAL",
		"NO","NOT","NOTNULL","NULL","OF","OFFSET","ON","OR","ORDER","OUTER","PLAN",
		"PRAGMA","PRIMARY","QUERY","RAISE","REFERENCES","REGEXP","REINDEX","RELEASE",
		"RENAME","REPLACE","RESTRICT","RIGHT","ROLLBACK","ROW","SAVEPOINT","SELECT",
		"SET","TABLE","TEMP","TEMPORARY","THEN","TO","TRANSACTION","TRIGGER","UNION",
		"UNIQUE","UPDATE","USING","VACUUM","VALUES","VIEW","VIRTUAL","WHEN","WHERE"
	};
	private static final String SAFEWORD = "XXX";
	private static final Set<String> BL_SET = new HashSet<String>(Arrays.asList(BLACKLIST));

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
		if(javaNotation.equalsIgnoreCase("_id"))
			return "_id";
		
		StringBuilder sb = new StringBuilder();
		char[] buf = javaNotation.toCharArray();

		for (int i = 0; i < buf.length; i++) {
			char prevChar = (i > 0) ? buf[i - 1] : 0;
			char c = buf[i];
			char nextChar = (i < buf.length - 1) ? buf[i + 1] : 0;
			boolean isFirstChar = (i == 0) ? true : false;

			// "AbCd"->"AB_CD"
			// "ABCd"->"AB_CD"
			// "AbCD"->"AB_CD"
			// "ShowplaceDetailsVO"->"SHOWPLACE_DETAILS_VO"
			if (isFirstChar || Character.isLowerCase(c)) {
				sb.append(Character.toUpperCase(c));
			} else if (Character.isUpperCase(c)) {
				if (Character.isLetterOrDigit(prevChar)) {
					if (Character.isLowerCase(prevChar)) {
						sb.append('_').append(Character.toUpperCase(c));
					} else if (nextChar > 0 && Character.isLowerCase(nextChar)) {
						sb.append('_').append(Character.toUpperCase(c));
					} else {
						sb.append(c);
					}
				}
				else {
					sb.append(c);
				}				
			} else if(Character.isDigit(c)) {
				sb.append(c);
			}
		}
		String result = sb.toString();
		if(BL_SET.contains(result.toUpperCase())) {
			result = SAFEWORD + result;
		}
		return result;
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
		StringBuilder dest = new StringBuilder();
		char[] src = sqlNotation.toCharArray();
		
		for (int i = 0; i < src.length; i++) {
			char c = src[i];
			boolean isFirstChar = (i == 0) ? true : false;
			
			if (isFirstChar || c != '_') {
				dest.append(Character.toLowerCase(c));
			} else {
				i++;
				if (i < src.length) {
					dest.append(src[i]);
				}
			}
		}
		return dest.toString();
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
		if(sqlNotation.startsWith(SAFEWORD)) {
			sqlNotation = sqlNotation.replace(SAFEWORD, "");
		}
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
