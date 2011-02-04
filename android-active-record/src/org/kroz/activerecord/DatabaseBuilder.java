package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.kroz.activerecord.annotations.ActiveRecordIgnoreAttribute;
import org.kroz.activerecord.annotations.ManyToManyRelation;

/**
 * Defines DB schema definition statements from provided Java classes. <br/>
 * Use this class to specify structure of your DB. Call method addClass() for
 * each table and provide corresponding Java class. <br/>
 * Normally this class instantiated only once at the very beginning of the
 * application lifecycle. Once instantiated it is used by underlying
 * SQLDatabaseHelper and provides SQL statements for create or upgrade of DB
 * schema.
 * 
 * @author JEREMYOT
 * @author Vladimir Kroz
 * <p>This project based on and inspired by 'androidactiverecord' project written by JEREMYOT</p>
 */
public class DatabaseBuilder {
	
	@SuppressWarnings("unchecked")
	Map<String, Class> classes = new HashMap<String, Class>();
	String _dbName;

	/**
	 * Create a new DatabaseBuilder for a database.
	 */
	public DatabaseBuilder(String dbName) {
		this._dbName = dbName;
	}

	/**
	 * Add or update a table for an AREntity that is stored in the current
	 * database.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase type.
	 * @param c
	 *            The class to reference when updating or adding a table.
	 */
	public <T extends ActiveRecordBase> void addClass(Class<T> c) {
		String simpleName = c.getSimpleName();
		classes.put(simpleName, c);
	}
	
	public <T extends ActiveRecordBase> Class<T> getClassForName(String simpleName){
		return (Class<T>) classes.get(simpleName);
	}

	/**
	 * Returns list of DB tables according to classes added to a schema map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String[] getTables() {
		String[] ret = new String[classes.size()];
		Class[] arr = new Class[classes.size()];
		arr = classes.values().toArray(arr);
		for (int i = 0; i < arr.length; i++) {
			Class c = arr[i];
			ret[i] = CamelNotationHelper.toSQLName(c.getSimpleName());
		}
		return ret;
	}

	/**
	 * Returns SQL create statement for specified table
	 * 
	 * @param table
	 *            name in SQL notation
	 * @throws ActiveRecordException
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecordBase> String getSQLCreate(String table)
			throws ActiveRecordException {
		StringBuilder sb = null;
		Class<T> c = getClassBySqlName(table);
		T e = null;
		try {
			e = c.newInstance();
		} catch (IllegalAccessException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		} catch (InstantiationException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		}
		if (null != c) {
			sb = new StringBuilder("CREATE TABLE ").append(table).append(
					" (_id integer primary key");
			for (Field column : e.getColumnFieldsWithoutID()) {
				if(column.getAnnotation(ManyToManyRelation.class) != null) {
					ManyToManyRelation many2many = (ManyToManyRelation) column.getAnnotation(ManyToManyRelation.class);
					String joinTableName = many2many.joinTableName();
					if(joinTableName.length() == 0) {
						joinTableName = buildJoinTableName(this.getClass(), many2many.target());
					}
					//createIntersectionTableIfNotExists()
				}
				if(column.getAnnotation(ActiveRecordIgnoreAttribute.class) != null) { continue; }
				String jname = column.getName();
				String qname = CamelNotationHelper.toSQLName(jname);
				Class<?> jtype = column.getType();
				String qtype = Database.getSQLiteTypeString(jtype);
				sb.append(", ").append(qname).append(" ").append(qtype);
			}
			sb.append(")");

		}
		return sb.toString();
	}

	/**
	 * Returns SQL drop table statement for specified table
	 * 
	 * @param table
	 *            name in SQL notation
	 */
	public String getSQLDrop(String table) {
		return "DROP TABLE IF EXISTS " + table;
	}

	public String getDatabaseName() {
		return _dbName;
	}
	
	public String buildJoinTableName(Class source, Class target){
		String srcStr = CamelNotationHelper.toSQLName(source.getSimpleName());
		String trgtStr = CamelNotationHelper.toSQLName(target.getSimpleName());
		
		if(srcStr.compareTo(trgtStr) < 0) {
			return srcStr + "_" + trgtStr;
		}
		return trgtStr + "_" + srcStr;
	}

	@SuppressWarnings("unchecked")
	private Class getClassBySqlName(String table) {
		String jName = CamelNotationHelper.toJavaClassName(table);
		return classes.get(jName);
	}
}
