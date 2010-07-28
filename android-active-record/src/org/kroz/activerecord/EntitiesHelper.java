package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class EntitiesHelper {
	private static final String AR_BASE_CLASS_NAME = ActiveRecordBase.class
			.getSimpleName();
	private static final String AR_PK_FIELD_JAVA_NAME = "id";

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
		
		// Build list of fields in source object
		Hashtable<String, Field> srcFields = new Hashtable<String, Field>();
		for (Field field : src.getClass().getFields()) {
			srcFields.put(field.getName(), field);
		}
		
		copyPkFields(dst, src, dstFields, srcFields);
		copyDataFields(dst, src, dstFields, srcFields);

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
	static public <T1, T2> T1 copyFieldsWithoutID(T1 dst, T2 src) {

		// Build list of fields in target object
		Hashtable<String, Field> dstFields = new Hashtable<String, Field>();
		for (Field field : dst.getClass().getFields()) {
			dstFields.put(field.getName(), field);
		}

		// Build list of fields in source object
		Hashtable<String, Field> srcFields = new Hashtable<String, Field>();
		for (Field field : src.getClass().getFields()) {
			srcFields.put(field.getName(), field);
		}
		
		copyDataFields(dst, src, dstFields, srcFields);

		return dst;
	}

	/**
	 * Copies ID field. If dst or src are subclasses of ActiveRecordBase, then
	 * copies id fields of parent class
	 * 
	 * @param <T2>
	 * @param <T1>
	 * @param dst
	 * @param src
	 * @param dstFields
	 */
	private static <T2, T1> void copyPkFields(T1 dst, T2 src,
			Hashtable<String, Field> dstFields, Hashtable<String, Field> srcFields) {
		boolean srcIsAR = (src.getClass().getSuperclass().getSimpleName().equals(AR_BASE_CLASS_NAME))? true: false;
		boolean dstIsAR = (dst.getClass().getSuperclass().getSimpleName().equals(AR_BASE_CLASS_NAME))? true: false;
		boolean dstHasId = (dstFields.containsKey(AR_PK_FIELD_JAVA_NAME)) ? true : false;
		boolean srcHasId = (srcFields.containsKey(AR_PK_FIELD_JAVA_NAME)) ? true : false;
		
		if (srcIsAR && dstIsAR){
			((ActiveRecordBase)dst)._id = ((ActiveRecordBase)src)._id; 
		}
		else if( srcIsAR && dstHasId) {
			try {
				Field dstId = dst.getClass().getField("id");
				dstId.setLong(dst, ((ActiveRecordBase)src)._id);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		else if( srcHasId && dstIsAR) {
			try {
				Field id = src.getClass().getField("id");
				((ActiveRecordBase)dst)._id = id.getLong(src);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		else if( srcHasId && dstHasId) {
			try {
				Field srcId = src.getClass().getField("id");
				Field dstId = dst.getClass().getField("id");
				dstId.setLong(dst, srcId.getLong(src));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			
		}
	}

	private static <T2, T1> void copyDataFields(T1 dst, T2 src,
			Hashtable<String, Field> dstFields, Hashtable<String, Field> srcFields) {
		// Iterate through fields of source object
		for (Field srcField : srcFields.values()) {

			try {
				String srcFieldName = srcField.getName();
				//Skip ID field - it's copied by special method
				if(srcFieldName.equalsIgnoreCase("id"))
					continue;

				// If destination object has field corresponding with the field
				// in source object
				// Then copy value from source field to destination
				if (dstFields.containsKey(srcFieldName)) {

					// Get destination field from list
					Field dstField = dstFields.get(srcFieldName);

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
