/**
 * 
 */
package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;

import org.kroz.activerecord.test.fixtures.User;

import android.test.AndroidTestCase;

/**
 * @author VKROZ
 *
 */
public class EntitiesHelperTest extends AndroidTestCase {


	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.kroz.activerecord.EntitiesHelper.copyFields}.
	 */
	public void testCopyFields() {
		EntityPOJO pojo = new EntityPOJO();
		pojo.id=123;
		pojo.aaa=new Timestamp(112233);
		pojo.bbb="qwerty";
		pojo.ccc=456;
		pojo.ddd=7890;
		EntityAR ar = new EntityAR();
		EntitiesHelper.copyFields(ar, pojo);
		assertEquals(pojo.aaa, ar.aaa);
		assertEquals(pojo.bbb, ar.bbb);
		assertEquals(pojo.ddd, ar.ddd);
		assertNotSame(pojo.ccc, ar.ccc);
		assertEquals(pojo.id, ar._id);
	}

	public void testGetFieldsMap() throws Exception {
		Map<String, Field> entityFields = EntitiesHelper.getFieldsMap(EntityAR.class);

		assertEquals(5, entityFields.size());
		assertTrue(entityFields.containsKey("aaa"));
		assertTrue(entityFields.containsKey("aaa1"));
		assertTrue(entityFields.containsKey("bbb"));
		assertTrue(entityFields.containsKey("ccc"));
		assertTrue(entityFields.containsKey("ddd"));

		assertEquals("Timestamp", entityFields.get("aaa").getType().getSimpleName());
		assertEquals("Timestamp", entityFields.get("aaa1").getType().getSimpleName());
		assertEquals("String", entityFields.get("bbb").getType().getSimpleName());
		assertEquals("String", entityFields.get("ccc").getType().getSimpleName());
		assertEquals("int", entityFields.get("ddd").getType().getSimpleName());
	}


}

class EntityPOJO {
	public EntityPOJO() {
		
	}
	
	public int id;
	public Timestamp aaa;
	public String bbb;
	public int ccc;
	public int ddd;
}

class EntityAR extends ActiveRecordBase {
	public EntityAR() {
		
	}
	public Timestamp aaa;
	public Timestamp aaa1;
	public String bbb;
	public String ccc;
	public int ddd;
}
