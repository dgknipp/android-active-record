package org.kroz.activerecord;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

class ARDictionary {
	private Map<String, WeakReference<AREntity>> map = new HashMap<String, WeakReference<AREntity>>();

	@SuppressWarnings("unchecked")
	<T extends AREntity> T get(Class<T> c, long id) {
		WeakReference<AREntity> i = map.get(String.format("%s%d", c.getName(), id));
		if (i == null)
			return null;
		return (T) i.get();
	}

	void set(AREntity e) {
		map.put(String.format("%s%d", e.getClass().getName(), e.getID()), new WeakReference<AREntity>(e));
	}
}
