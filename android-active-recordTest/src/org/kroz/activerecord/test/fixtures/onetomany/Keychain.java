package org.kroz.activerecord.test.fixtures.onetomany;

import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.annotations.ActiveRecordIgnoreAttribute;


public class Keychain extends ActiveRecordBase {
	
	@ActiveRecordIgnoreAttribute
	public List<Key> keys;
	
	public String name;
	
	public List<Key> getKeys() throws Exception{
		if(keys == null) {
			keys = getHasMany(Key.class);
		}
		return keys;
	}

	public void setKeys(List<Key> keys) throws Exception{
		for(Key key : keys) {
			key.keychain = this;
		}
		this.keys = keys;
	}
	
	public void addKey(Key key) throws Exception {
		//if(getID() < 0) { save(); }
		if(keys == null) { keys = new ArrayList<Key>(); }
		key.keychain = this;
		this.keys.add(key);
	}

}
