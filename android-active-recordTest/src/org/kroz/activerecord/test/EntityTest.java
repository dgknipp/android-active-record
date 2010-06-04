/**
 * 
 */
package org.kroz.activerecord.test;

import org.kroz.activerecord.ActiveRecordBase;

import android.test.AndroidTestCase;

/**
 * @author VKROZ
 *
 */
public class EntityTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.kroz.activerecord.ActiveRecordBase#test()}.
	 */
	public void testTest() {
		ActiveRecordBase e = new ActiveRecordBase();
		assertEquals("Hello", e.test());
	}

}
