/**
 * 
 */
package org.kroz.activerecord;

import java.sql.Timestamp;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.test.fixtures.TestConst;
import org.kroz.activerecord.test.fixtures.User;
import org.kroz.activerecord.test.fixtures.UserData;

import android.content.Context;
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
