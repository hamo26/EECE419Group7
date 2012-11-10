package com.schedushare.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SchedulesDataSource {
	// Database fields.
	private SQLiteDatabase database;
	private SchedulesSQLiteHelper dbHelper;
	
	// All column names of user table.
	public static String[] allUserColumns = {
			SchedulesSQLiteHelper.COLUMN_ID,
			SchedulesSQLiteHelper.COLUMN_SID,
			SchedulesSQLiteHelper.COLUMN_NAME
	};
	
	// All column names of schedule table.
	public static String[] allScheduleColumns = {
			SchedulesSQLiteHelper.COLUMN_ID,
			SchedulesSQLiteHelper.COLUMN_SID,
			SchedulesSQLiteHelper.COLUMN_NAME,
			SchedulesSQLiteHelper.COLUMN_ACTIVE,
			SchedulesSQLiteHelper.COLUMN_OWNER_ID,
			SchedulesSQLiteHelper.COLUMN_LAST_MODIFIED
	};
	
	// Column names of used to display schedule menu.
	public static String[] menuScheduleColumns = {
			SchedulesSQLiteHelper.COLUMN_NAME,
			SchedulesSQLiteHelper.COLUMN_OWNER_ID
	};
	
	// Constructor.
	public SchedulesDataSource(Context context) {
		dbHelper = new SchedulesSQLiteHelper(context);
	}
	
	// Opens interface to database.
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	// Close interface to database.
	public void close() {
		dbHelper.close();
	}
	
	// Drops all database tables.
	public void dropAllTables() {
		dbHelper.onUpgrade(database, 1, 1);
	}
	
	//
	// Methods for user table.
	//
	
	// Creates a new user entry in table.
	public UserData createUser(long sid, String name) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
		
		long insertId = database.insert(SchedulesSQLiteHelper.TABLE_USER, null, values);
		
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_USER,
				SchedulesDataSource.allUserColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		
		return userFromCursor(cursor);
	}
	
	// Returns user pointed to by cursor.
	private UserData userFromCursor(Cursor cursor) {
		UserData user = new UserData();
		user.id = cursor.getLong(0);
		user.sid = cursor.getLong(1);
		user.name = cursor.getString(2);
		return user;
	}
	
	//
	// Methods for schedule table.
	//
	
	// Creates a new schedule entry in table.
	public ScheduleData createSchedule(long sid, String name, boolean active, long ownerId, String lastModified) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
		values.put(SchedulesSQLiteHelper.COLUMN_ACTIVE, active);
		values.put(SchedulesSQLiteHelper.COLUMN_OWNER_ID, ownerId);
		values.put(SchedulesSQLiteHelper.COLUMN_LAST_MODIFIED, lastModified);
		
		long insertId = database.insert(SchedulesSQLiteHelper.TABLE_SCHEDULE, null, values);
		
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		return scheduleFromCursor(cursor);
	}
	
	// Deletes given schedule from table.
	public void deleteSchedule(ScheduleData schedule) {
		database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_SCHEDULE + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ schedule.id);
	}
	
	// Updates given schedule in table.
	public void updateSchedule(ScheduleData schedule) {
		String sql = "UPDATE " + SchedulesSQLiteHelper.TABLE_SCHEDULE + " "
				+ "SET " + SchedulesSQLiteHelper.COLUMN_SID + " = " + schedule.sid + ", "
				+ SchedulesSQLiteHelper.COLUMN_NAME + " = '" + schedule.name + "', "
				+ SchedulesSQLiteHelper.COLUMN_ACTIVE + " = " + schedule.active + ", "
				+ SchedulesSQLiteHelper.COLUMN_OWNER_ID + " = " + schedule.ownerId + ", "
				+ SchedulesSQLiteHelper.COLUMN_LAST_MODIFIED + " = '" + schedule.lastModified + "' "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = " + schedule.id;
		
		database.execSQL(sql);
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getAllSchedulesCursor() {
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		return cursor;
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getMenuSchedulesCursor() {
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.menuScheduleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		return cursor;
	}
	
	// Returns schedule pointed to by cursor.
	private ScheduleData scheduleFromCursor(Cursor cursor) {
		ScheduleData schedule = new ScheduleData();
		schedule.id = cursor.getLong(0);
		schedule.sid = cursor.getLong(1);
		schedule.name = cursor.getString(2);
		schedule.active = cursor.getInt(3) > 0;
		schedule.ownerId = cursor.getInt(4);
		schedule.lastModified = cursor.getString(5);
		return schedule;
	}
}