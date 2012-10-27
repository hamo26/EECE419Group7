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
	
	// All column names of schedule table.
	public static String[] allScheduleColumns = {
			SchedulesSQLiteHelper.COLUMN_ID,
			SchedulesSQLiteHelper.COLUMN_OWNER,
			SchedulesSQLiteHelper.COLUMN_NAME};
	
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
	
	// Creates a new schedule entry in table.
	public ScheduleData createSchedule(long owner, String name) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_OWNER, owner);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
		
		long insertId = database.insert(SchedulesSQLiteHelper.TABLE_SCHEDULES, null, values);
		
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_SCHEDULES,
				allScheduleColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		return scheduleFromCursor(cursor);
	}
	
	// Deletes given schedule from table.
	public void deleteSchedule(ScheduleData schedule) {
		database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_SCHEDULES + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ schedule.id);
	}
	
	// Updates given schedule in table.
	public void updateSchedule(ScheduleData schedule) {
		String sql = "UPDATE " + SchedulesSQLiteHelper.TABLE_SCHEDULES + " "
				+ "SET " + SchedulesSQLiteHelper.COLUMN_OWNER + " = " + schedule.owner + ", "
				+ SchedulesSQLiteHelper.COLUMN_NAME + " = '" + schedule.name + "' "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = " + schedule.id;
		
		database.execSQL(sql);
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getAllSchedulesCursor() {
		Cursor cursor = database.query(SchedulesSQLiteHelper.TABLE_SCHEDULES,
				allScheduleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		return cursor;
	}
	
	// Returns schedule pointed to by cursor.
	private ScheduleData scheduleFromCursor(Cursor cursor) {
		ScheduleData schedule = new ScheduleData();
		schedule.id = cursor.getLong(0);
		schedule.owner = cursor.getLong(1);
		schedule.name = cursor.getString(2);
		return schedule;
	}
}