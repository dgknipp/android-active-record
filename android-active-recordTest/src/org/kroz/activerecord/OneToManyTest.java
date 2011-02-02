package org.kroz.activerecord;

import java.util.List;

import org.kroz.activerecord.test.fixtures.onetomany.Key;
import org.kroz.activerecord.test.fixtures.onetomany.Keychain;


public class OneToManyTest extends ActiveRecordTestCase {
	
	private static Class[] classes = {Key.class, Keychain.class};
	
	public void setUp() throws Exception {
		super.setUp(classes);
	}
	
	public Keychain setupKeysAndKeychain() throws Exception {
		Keychain chain = connection.newEntity(Keychain.class);
		chain.name = "Keyring1";
		
		Key houseKey = connection.newEntity(Key.class);
		houseKey.keyName = "House Key";
		
		Key carKey = connection.newEntity(Key.class);
		carKey.keyName = "Car Key";
		
		chain.addKey(carKey);
		chain.addKey(houseKey);
		return chain;
	}
	
	public void testOneToMany() throws Exception {
		Keychain chain = setupKeysAndKeychain();
		
		for(Key key : chain.keys) {
			assertEquals(0, key.keychain.getID());
		}
		
		chain.save();
		
		List<Keychain> chains = connection.findByColumn(Keychain.class, "NAME", "Keyring1");
		assertEquals(1, chains.size());
		
		Keychain dbCopy = chains.get(0);
		
		List<Key> keys = dbCopy.getKeys(); 
		assertEquals(2, keys.size());
		
		Key key1 = keys.get(0);
		Key key2 = keys.get(1);
		
		for(Key key : keys) {
			assertTrue( key.keyName.equals("Car Key") || key.keyName.equals("House Key"));
			assertEquals(chain.getID(), key.keychain.getID());
		}
		
	}
	
	public void testVerifyOneToManyRelationship() throws Exception {
		Keychain chain = setupKeysAndKeychain();
		
		chain.save();
		
		Key houseKey = connection.findByColumn(Key.class, "KEY_NAME", "House Key").get(0);
		Key carKey = connection.findByColumn(Key.class, "KEY_NAME", "Car Key").get(0);
		
		assertEquals("House Key", houseKey.keyName);
		assertEquals("Car Key", carKey.keyName);
		
		assertEquals(chain.getID(), carKey.keychain.getID());
		assertEquals(chain.getID(), houseKey.keychain.getID());
	}

}
