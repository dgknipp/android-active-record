package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/**
 * 
 * @author Vladimir Kroz (AKA vkroz)
 * @author jeremyot
 * 
 *         This project based on and inspired by 'androidactiverecord' project
 *         written by JEREMYOT
 */
public class ActiveRecordBase {

	Database _database;

	static public ActiveRecordBase connect(Database db) {
		return new ActiveRecordBase(db);
	}

	static public ActiveRecordBase connect(Context ctx, String dbName) {
		Database db = Database.create(ctx, dbName);
		db.open();
		return ActiveRecordBase.connect(db);
	}

	public void close() {
		_database.close();
	}

	protected ActiveRecordBase(Database db) {
		_database = db;
	}

	/**
	 * Creates new entity instance connected with opened database
	 * 
	 * @param <T>
	 * @param type
	 *            The type of the required entity
	 * @return New entity instance
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T extends ActiveRecordBase> T newEntity(Class<T> type)
			throws IllegalAccessException, InstantiationException {
		T entity = type.newInstance();
		entity.setDatabase(_database);
		return entity;
	}

	static Database s_Database;
	static EntitiesMap s_EntitiesMap = new EntitiesMap();
	protected boolean m_NeedsInsert = true;

	/**
	 * Sets value of the field
	 * 
	 * @param string
	 * @param string2
	 * @throws IllegalAccessException
	 */
	public <V> void set(String fieldName, V value)
			throws IllegalArgumentException, IllegalAccessException {
		for (Field field : getColumnFields()) {
			if (field.getName().equalsIgnoreCase("fieldName")) {
				field.set(this, value);
			}
		}
		// m_status = EntityStatus.Changed;
	}

	/**
	 * Copies values of fields from src to current object. Scans src object for
	 * the fields with the same names as in current object and copies it's
	 * valies. All fields are copied except special fields: 'id', 'created',
	 * 'modified', and also fields prefixed as 'm_*' and 's_' If src has fields
	 * not defiend in current object such fields are ignored
	 * 
	 * @param src
	 */
	public void copyFrom(Object src) {
		for (Field dstField : this.getColumnFieldsWithoutID()) {
			try {
				Field srcField = src.getClass().getField(dstField.getName());
				dstField.set(this, srcField.get(src));

			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}

	}
	


	/**
	 * Call this once at application launch, sets the database to use for
	 * AREntities.
	 * 
	 * @param database
	 *            The database to use.
	 */
	public void setDatabase(Database database) {
		s_Database = database;
	}

	protected long _id = 0;

	/**
	 * This entities row id.
	 * 
	 * @return The SQLite row id.
	 */
	public long getID() {
		return _id;
	}

	/**
	 * Get the table name for this class.
	 * 
	 * @return The table name for this class.
	 */
	protected String getTableName() {
		return getClass().getSimpleName();
	}

	/**
	 * Get this class's columns without the id column.
	 * 
	 * @return An array of the columns in this class's table.
	 */
	protected String[] getColumnsWithoutID() {
		List<String> columns = new ArrayList<String>();
		for (Field field : getColumnFieldsWithoutID()) {
			columns.add(field.getName());
		}
		return columns.toArray(new String[0]);
	}

	/**
	 * Get this class's columns.
	 * 
	 * @return An array of the columns in this class's table.
	 */
	protected String[] getColumns() {
		List<String> columns = new ArrayList<String>();
		for (Field field : getColumnFields()) {
			columns.add(field.getName());
		}
		return columns.toArray(new String[0]);
	}

	/**
	 * Get this class's fields without _id.
	 * 
	 * @return An array of fields for this class.
	 */
	protected List<Field> getColumnFieldsWithoutID() {
		Field[] fields = getClass().getDeclaredFields();
		List<Field> columns = new ArrayList<Field>();
		for (Field field : fields) {
			if (!field.getName().startsWith("m_")
					&& !field.getName().startsWith("s_"))
				columns.add(field);
		}
		return columns;
	}

	/**
	 * Get this class's fields.
	 * 
	 * @return An array of fields for this class.
	 */
	protected List<Field> getColumnFields() {
		Field[] fields = getClass().getDeclaredFields();
		List<Field> columns = new ArrayList<Field>();
		for (Field field : fields) {
			if (!field.getName().startsWith("m_")
					&& !field.getName().startsWith("s_")) {
				columns.add(field);
			}
		}
		if (!getClass().equals(ActiveRecordBase.class)) {
			fields = ActiveRecordBase.class.getDeclaredFields();
			for (Field field : fields) {
				if (!field.getName().startsWith("m_")
						&& !field.getName().startsWith("s_")) {
					columns.add(field);
				}
			}
		}
		return columns;
	}

	/**
	 * Insert this entity into the database.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insert() throws IllegalArgumentException,
			IllegalAccessException {
		List<Field> columns = _id > 0 ? getColumnFields()
				: getColumnFieldsWithoutID();
		ContentValues values = new ContentValues(columns.size());
		for (Field column : columns) {
			if (column.getType().getSuperclass() == ActiveRecordBase.class)
				values.put(column.getName(), column.get(this) != null ? String
						.valueOf(((ActiveRecordBase) column.get(this))._id)
						: "0");
			else
				values.put(column.getName(), String.valueOf(column.get(this)));
		}
		_id = s_Database.insert(getTableName(), values);
		m_NeedsInsert = false;
	}

	/**
	 * Update this entity in the database.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void update() throws IllegalArgumentException,
			IllegalAccessException {
		List<Field> columns = getColumnFieldsWithoutID();
		ContentValues values = new ContentValues(columns.size());
		for (Field column : columns) {
			if (column.getType().getSuperclass() == ActiveRecordBase.class)
				values.put(column.getName(), column.get(this) != null ? String
						.valueOf(((ActiveRecordBase) column.get(this))._id)
						: "0");
			else
				values.put(column.getName(), String.valueOf(column.get(this)));
		}
		s_Database.update(getTableName(), values, "_id = ?",
				new String[] { String.valueOf(_id) });
	}

	/**
	 * Remove this entity from the database.
	 * 
	 * @return Whether or the entity was successfully deleted.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean delete() throws IllegalArgumentException,
			IllegalAccessException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		boolean toRet = s_Database.delete(getTableName(), "_id = ?",
				new String[] { String.valueOf(_id) }) != 0;
		_id = 0;
		m_NeedsInsert = true;
		return toRet;
	}

	/**
	 * Saves this entity to the database, inserts or updates as needed.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void save() throws IllegalArgumentException, IllegalAccessException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		if (m_NeedsInsert)
			insert();
		else
			update();
		s_EntitiesMap.set(this);
	}

	/**
	 * Inflate this entity using the current row from the given cursor.
	 * 
	 * @param cursor
	 *            The cursor to get object data from.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	void inflate(Cursor cursor) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		HashMap<Field, Long> entities = new HashMap<Field, Long>();
		for (Field field : getColumnFields()) {
			String typeString = field.getType().getName();
			if (typeString.equals("long"))
				field.setLong(this, cursor.getLong(cursor.getColumnIndex(field
						.getName())));
			else if (typeString.equals("java.lang.String")) {
				String val = cursor.getString(cursor.getColumnIndex(field
						.getName()));
				field.set(this, val.equals("null") ? null : val);
			} else if (typeString.equals("double"))
				field.setDouble(this, cursor.getDouble(cursor
						.getColumnIndex(field.getName())));
			else if (typeString.equals("boolean"))
				field.setBoolean(this, cursor.getString(
						cursor.getColumnIndex(field.getName())).equals("true"));
			else if (typeString.equals("[B"))
				field.set(this, cursor.getBlob(cursor.getColumnIndex(field
						.getName())));
			else if (typeString.equals("int"))
				field.setInt(this, cursor.getInt(cursor.getColumnIndex(field
						.getName())));
			else if (typeString.equals("float"))
				field.setFloat(this, cursor.getFloat(cursor
						.getColumnIndex(field.getName())));
			else if (typeString.equals("short"))
				field.setShort(this, cursor.getShort(cursor
						.getColumnIndex(field.getName())));
			else if (field.getType().getSuperclass() == ActiveRecordBase.class) {
				long id = cursor
						.getLong(cursor.getColumnIndex(field.getName()));
				if (id > 0)
					entities.put(field, id);
				else
					field.set(this, null);
			} else
				throw new IllegalArgumentException(
						"Class cannot be read from Sqlite3 database.");
		}
		s_EntitiesMap.set(this);
		for (Field f : entities.keySet()) {
			f.set(this, this.findByID((Class<? extends ActiveRecordBase>) f
					.getType(), entities.get(f)));
		}
	}

	/**
	 * Delete selected entities from the database.
	 * 
	 * @param <T>
	 *            Any AREntity class.
	 * @param type
	 *            The class of the entities to delete.
	 * @param whereClause
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @return The number of rows affected.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> int delete(Class<T> type,
			String whereClause, String[] whereArgs)
			throws IllegalAccessException, InstantiationException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		T entity = type.newInstance();
		return s_Database.delete(entity.getTableName(), whereClause, whereArgs);
	}

	/**
	 * Delete all instances of an entity from the database where a column has a
	 * specified value.
	 * 
	 * @param <T>
	 *            Any AREntity class.
	 * @param type
	 *            The class of the entities to delete.
	 * @param column
	 *            The column to match.
	 * @param value
	 *            The value required for deletion.
	 * @return The number of rows affected.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> int deleteByColumn(
			Class<T> type, String column, String value)
			throws IllegalAccessException, InstantiationException {
		return delete(type, String.format("%s = ?", column),
				new String[] { value });
	}

	/**
	 * Return all instances of an entity that match the given criteria.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase class.
	 * @param type
	 *            The class of the entities to return.
	 * @param whereClause
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @return A generic list of all matching entities.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> List<T> find(Class<T> type,
			String whereClause, String[] whereArgs)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		T entity = type.newInstance();
		List<T> toRet = new ArrayList<T>();
		Cursor c = s_Database.query(entity.getTableName(), null, whereClause,
				whereArgs);
		try {
			while (c.moveToNext()) {
				entity = s_EntitiesMap.get(type, c.getLong(c
						.getColumnIndex("_id")));
				if (entity == null) {
					entity = type.newInstance();
					entity.m_NeedsInsert = false;
					entity.inflate(c);
				}
				toRet.add(entity);
			}
		} finally {
			c.close();
		}
		return toRet;
	}

	/**
	 * Return all instances of an entity that match the given criteria.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase class.
	 * @param type
	 *            The class of the entities to return.
	 * @param distinct
	 * @param whereClause
	 *            The condition to match (Don't include "where").
	 * @param whereArgs
	 *            The arguments to replace "?" with.
	 * @param orderBy
	 * @param limit
	 * @return A generic list of all matching entities.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> List<T> find(Class<T> type,
			boolean distinct, String whereClause, String[] whereArgs,
			String orderBy, String limit) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		T entity = type.newInstance();
		List<T> toRet = new ArrayList<T>();
		Cursor c = s_Database.query(distinct, entity.getTableName(), null,
				whereClause, whereArgs, null, null, orderBy, limit);
		try {
			while (c.moveToNext()) {
				entity = s_EntitiesMap.get(type, c.getLong(c
						.getColumnIndex("_id")));
				if (entity == null) {
					entity = type.newInstance();
					entity.m_NeedsInsert = false;
					entity.inflate(c);
				}
				toRet.add(entity);
			}
		} finally {
			c.close();
		}
		return toRet;
	}

	/**
	 * Return all instances of an entity from the database where a column has a
	 * specified value.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase class.
	 * @param type
	 *            The class of the entities to return.
	 * @param column
	 *            The column to match.
	 * @param value
	 *            The desired value.
	 * @return A generic list of all matching entities.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> List<T> findByColumn(
			Class<T> type, String column, String value)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		return find(type, String.format("%s = ?", column),
				new String[] { value });
	}

	/**
	 * Return the instance of an entity with a matching id.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase class.
	 * @param type
	 *            The class of the entity to return.
	 * @param id
	 *            The desired ID.
	 * @return The matching entity.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T extends ActiveRecordBase> T findByID(Class<T> type, long id)
			throws IllegalAccessException, InstantiationException,
			SQLiteException {
		if (s_Database == null)
			throw new RuntimeException("Set database before using AREntities.");
		T entity = s_EntitiesMap.get(type, id);
		if (entity != null)
			return entity;
		entity = type.newInstance();
		entity.m_NeedsInsert = false;
		Cursor c = _database.query(entity.getTableName(), null, "_id = ?",
				new String[] { String.valueOf(id) });
		try {
			if (!c.moveToNext())
				return null;
			entity.inflate(c);
		} finally {
			c.close();
		}
		return entity;
	}

	/**
	 * Return all instances of an entity from the database.
	 * 
	 * @param <T>
	 *            Any ActiveRecordBase class.
	 * @param type
	 *            The class of the entities to return.
	 * @return A generic list of all matching entities.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends ActiveRecordBase> List<T> findAll(Class<T> type)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		return find(type, null, null);
	}

}

// enum EntityStatus {
// Detached,
// Changed,
// NotChanged
// }
