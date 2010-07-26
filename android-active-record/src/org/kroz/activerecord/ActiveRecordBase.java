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

	static EntitiesMap s_EntitiesMap = new EntitiesMap();

	boolean m_NeedsInsert = true;
	Database _database;

	/**
	 * Creates new ActiveRecord instance. Returned instances is not initially
	 * opened. Calling application must explicitly open it by calling open()
	 * method
	 * 
	 * @param db
	 * @return
	 */
	static public ActiveRecordBase createInstance(Database db) {
		return new ActiveRecordBase(db);
	}

	/**
	 * Creates and opens new ActiveRecord object instance and underlying
	 * database. Returned ActiveRecord object is fully ready for use.
	 * 
	 * @param ctx
	 * @param dbName
	 * @return
	 * @throws ActiveRecordException
	 */
	static public ActiveRecordBase open(Context ctx, String dbName, int dbVersion)
			throws ActiveRecordException {
		Database db = Database.createInstance(ctx, dbName, dbVersion);
		db.open();
		return ActiveRecordBase.createInstance(db);
	}

	/**
	 * Opens ActiveRecord object and associated underlying database
	 * 
	 * @throws ActiveRecordException
	 */
	public void open() throws ActiveRecordException {
		_database.open();
	}

	/**
	 * Closes ActiveRecord object and associated underlying database
	 */
	public void close() {
		_database.close();
	}

	/**
	 * 
	 * @param db
	 */
	protected ActiveRecordBase(Database db) {
		_database = db;
	}

	protected ActiveRecordBase() {
	}

	/**
	 * Creates new entity instance connected with opened database
	 * 
	 * @param <T>
	 * @param type
	 *            The type of the required entity
	 * @return New entity instance
	 */
	public <T extends ActiveRecordBase> T newEntity(Class<T> type)
			throws ActiveRecordException {
		T entity = null;
		try {
			entity = type.newInstance();
			entity.setDatabase(_database);
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException("Can't instantiate "
					+ type.getClass());
		} catch (InstantiationException e) {
			throw new ActiveRecordException("Can't instantiate "
					+ type.getClass());
		}
		return entity;
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
		_database = database;
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
		return CamelNotationHelper.toSQLName(getClass().getSimpleName());
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
	 * @throws ActiveRecordException
	 */
	private void insert() throws ActiveRecordException {
		List<Field> columns = _id > 0 ? getColumnFields()
				: getColumnFieldsWithoutID();
		ContentValues values = new ContentValues(columns.size());
		for (Field column : columns) {
			try {
				if (column.getType().getSuperclass() == ActiveRecordBase.class)
					values.put(CamelNotationHelper.toSQLName(column.getName()),
							column.get(this) != null ? String
									.valueOf(((ActiveRecordBase) column
											.get(this))._id) : "0");
				else
					values.put(CamelNotationHelper.toSQLName(column.getName()), String.valueOf(column
							.get(this)));
			} catch (IllegalAccessException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			}
		}
		_id = _database.insert(getTableName(), values);
		if(-1 != _id)
			m_NeedsInsert = false;
		else
			throw new ActiveRecordException(ErrMsg.ERR_INSERT_FAILED);
	}

	/**
	 * Update this entity in the database.
	 * 
	 * @throws NoSuchFieldException
	 */
	private void update() throws ActiveRecordException {
		List<Field> columns = getColumnFieldsWithoutID();
		ContentValues values = new ContentValues(columns.size());
		for (Field column : columns) {
			try {
				if (column.getType().getSuperclass() == ActiveRecordBase.class)
					values.put(CamelNotationHelper.toSQLName(column.getName()),
							column.get(this) != null ? String
									.valueOf(((ActiveRecordBase) column
											.get(this))._id) : "0");
				else
					values.put(CamelNotationHelper.toSQLName(column.getName()), String.valueOf(column
							.get(this)));
			} catch (IllegalArgumentException e) {
				throw new ActiveRecordException("No column " + column.getName());
			} catch (IllegalAccessException e) {
				throw new ActiveRecordException("No column " + column.getName());
			}
		}
		_database.update(getTableName(), values, "_id = ?",
				new String[] { String.valueOf(_id) });
	}

	/**
	 * Remove this entity from the database.
	 * 
	 * @return Whether or the entity was successfully deleted.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean delete() throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
		boolean toRet = _database.delete(getTableName(), "_id = ?",
				new String[] { String.valueOf(_id) }) != 0;
		_id = 0;
		m_NeedsInsert = true;
		return toRet;
	}

	/**
	 * Saves this entity to the database, inserts or updates as needed.
	 * 
	 * @throws ActiveRecordException
	 */
	public void save() throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
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
	void inflate(Cursor cursor) throws ActiveRecordException {
		HashMap<Field, Long> entities = new HashMap<Field, Long>();
		for (Field field : getColumnFields()) {
			try {
				String typeString = field.getType().getName();
				if (typeString.equals("long"))
					field.setLong(this, cursor.getLong(cursor
							.getColumnIndex(field.getName())));
				else if (typeString.equals("java.lang.String")) {
					String val = cursor.getString(cursor.getColumnIndex(field
							.getName()));
					field.set(this, val.equals("null") ? null : val);
				} else if (typeString.equals("double"))
					field.setDouble(this, cursor.getDouble(cursor
							.getColumnIndex(field.getName())));
				else if (typeString.equals("boolean"))
					field.setBoolean(this, cursor.getString(
							cursor.getColumnIndex(field.getName())).equals(
							"true"));
				else if (typeString.equals("[B"))
					field.set(this, cursor.getBlob(cursor.getColumnIndex(field
							.getName())));
				else if (typeString.equals("int"))
					field.setInt(this, cursor.getInt(cursor
							.getColumnIndex(field.getName())));
				else if (typeString.equals("float"))
					field.setFloat(this, cursor.getFloat(cursor
							.getColumnIndex(field.getName())));
				else if (typeString.equals("short"))
					field.setShort(this, cursor.getShort(cursor
							.getColumnIndex(field.getName())));
				else if (field.getType().getSuperclass() == ActiveRecordBase.class) {
					long id = cursor.getLong(cursor.getColumnIndex(field
							.getName()));
					if (id > 0)
						entities.put(field, id);
					else
						field.set(this, null);
				} else
					throw new ActiveRecordException(
							"Class cannot be read from Sqlite3 database.");
			} catch (IllegalArgumentException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			}

		}

		s_EntitiesMap.set(this);
		for (Field f : entities.keySet()) {
			try {
				f.set(this, this.findByID((Class<? extends ActiveRecordBase>) f
						.getType(), entities.get(f)));
			} catch (SQLiteException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			} catch (IllegalArgumentException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				throw new ActiveRecordException(e.getLocalizedMessage());
			}
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
	public <T extends ActiveRecordBase> int delete(Class<T> type,
			String whereClause, String[] whereArgs)
			throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
		T entity;
		try {
			entity = type.newInstance();
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		}
		return _database.delete(entity.getTableName(), whereClause, whereArgs);
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
	 * @throws ActiveRecordException
	 */
	public <T extends ActiveRecordBase> int deleteByColumn(Class<T> type,
			String column, String value) throws ActiveRecordException {
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
	public <T extends ActiveRecordBase> List<T> find(Class<T> type,
			String whereClause, String[] whereArgs)
			throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
		T entity = null;
		try {
			entity = type.newInstance();
		} catch (IllegalAccessException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		} catch (InstantiationException e1) {
			throw new ActiveRecordException(e1.getLocalizedMessage());
		}
		List<T> toRet = new ArrayList<T>();
		Cursor c = _database.query(entity.getTableName(), null, whereClause,
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
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
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
	public <T extends ActiveRecordBase> List<T> find(Class<T> type,
			boolean distinct, String whereClause, String[] whereArgs,
			String orderBy, String limit) throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
		T entity;
		try {
			entity = type.newInstance();
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		}
		List<T> toRet = new ArrayList<T>();
		Cursor c = _database.query(distinct, entity.getTableName(), null,
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
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
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
	 * @throws ActiveRecordException
	 */
	public <T extends ActiveRecordBase> List<T> findByColumn(Class<T> type,
			String column, String value) throws ActiveRecordException {
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
	 * @return The matching entity if reocrd found in DB, null otherwise
	 * @throws ActiveRecordException
	 */
	public <T extends ActiveRecordBase> T findByID(Class<T> type, long id)
			throws ActiveRecordException {
		if (_database == null)
			throw new ActiveRecordException("Set database first");
		T entity = s_EntitiesMap.get(type, id);
		if (entity != null)
			return entity;
		try {
			entity = type.newInstance();
		} catch (IllegalAccessException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new ActiveRecordException(e.getLocalizedMessage());
		}
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
	 * @throws ActiveRecordException
	 */
	public <T extends ActiveRecordBase> List<T> findAll(Class<T> type)
			throws ActiveRecordException {
		return find(type, null, null);
	}

}

// enum EntityStatus {
// Detached,
// Changed,
// NotChanged
// }
