/**
 * 
 */
package org.kroz.activerecord.test;

import org.kroz.activerecord.Entity;

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
	 * Test method for {@link org.kroz.activerecord.Entity#test()}.
	 */
	public void testTest() {
		Entity e = new Entity();
		assertEquals("Hello!", e.test());
	}

}
