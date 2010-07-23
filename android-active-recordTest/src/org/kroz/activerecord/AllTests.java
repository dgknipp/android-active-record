/**
 * 
 */
package org.kroz.activerecord;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * @author vkroz
 * @see http://marakana.com/tutorials/android/junit-test-example.html
 */
public class AllTests extends TestSuite {

	/**
	 * aa
	 */
	public AllTests() {
	}

	/**
	 * @param theClass
	 */
	@SuppressWarnings("unchecked")
	public AllTests(Class theClass) {
		super(theClass);
	}

	/**
	 * @param name
	 */
	public AllTests(String name) {
		super(name);
	}

	/**
	 * @param theClass
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public AllTests(Class theClass, String name) {
		super(theClass, name);
	}

	public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
