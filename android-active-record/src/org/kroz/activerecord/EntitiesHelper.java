package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class EntitiesHelper {

	/**
	 * Copies all fields from src to dst which have the same name and type
	 * 
	 * @param <T1>
	 * @param <T2>
	 * @param dst
	 * @param src
	 * @return
	 */
	static public <T1, T2> T1 copyFields(T1 dst, T2 src) {

		// Build list of fields in target object
		Hashtable<String, Field> dstFields = new Hashtable<String, Field>();
		for (Field field : dst.getClass().getFields()) {
			dstFields.put(field.getName(), field);
		}

		copyFieldsData(dst, src, dstFields);

		return dst;
	}

	/**
	 * Copies all fields from src to dst which have the same name and type,
	 * except id or _id field
	 * 
	 * @param <T1>
	 * @param <T2>
	 * @param dst
	 * @param src
	 * @return
	 */
	static public <T1, T2> T1 copyFieldsWithoutId(T1 dst, T2 src) {

		// Build list of fields in target object
		Hashtable<String, Field> dstFields = new Hashtable<String, Field>();
		for (Field field : dst.getClass().getFields()) {
			String name = field.getName();
			
			// Skip 'id' and '_id' fields
			if(name.equalsIgnoreCase("id")||name.equalsIgnoreCase("_id"))
				continue;
			
			dstFields.put(field.getName(), field);
		}

		copyFieldsData(dst, src, dstFields);

		return dst;
	}

	private static <T2, T1> void copyFieldsData(T1 dst, T2 src,
			Hashtable<String, Field> dstFields) {
		// Iterate through fields of source object
		for (Field srcField : src.getClass().getFields()) {

			try {

				// If destination object has field corresponding with the field
				// in source object
				// Then copy value from source field to destination
				if (dstFields.contains(srcField.getName())) {

					// Get destination field from list
					Field dstField = dstFields.get(srcField.getName());

					// Additional check - fields should have similar type
					String srcFldTypeName = srcField.getType().getSimpleName();
					String dstFldTypeName = dstField.getType().getSimpleName();
					if (!srcFldTypeName.equals(dstFldTypeName))
						continue;

					// Assign values
					// Need this long constructto handle various types via
					// reflection mechanism
					if (dstFldTypeName.equals("long")) {
						long srcValue;
						srcValue = srcField.getLong(src);
						dstField.setLong(dst, srcValue);
					}
					if (dstFldTypeName.equals("int")) {
						int srcValue = srcField.getInt(src);
						dstField.setInt(dst, srcValue);
					} else if (dstFldTypeName.equals("short")) {
						short srcValue = srcField.getShort(src);
						dstField.setShort(dst, srcValue);
					} else if (dstFldTypeName.equals("float")) {
						float srcValue = srcField.getFloat(src);
						dstField.setFloat(dst, srcValue);
					} else if (dstFldTypeName.equals("double")) {
						double srcValue = srcField.getDouble(src);
						dstField.setDouble(dst, srcValue);
					} else if (dstFldTypeName.equals("boolean")) {
						boolean srcValue = srcField.getBoolean(src);
						dstField.setBoolean(dst, srcValue);
					} else {
						Object srcValue = srcField.get(src);
						dstField.set(dst, srcValue);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}


}
