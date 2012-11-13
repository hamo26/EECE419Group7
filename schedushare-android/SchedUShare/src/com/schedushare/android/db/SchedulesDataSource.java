package com.schedushare.android.db;

import java.util.ArrayList;
import java.util.HashMap;

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
			SchedulesSQLiteHelper.COLUMN_LAST_MODIFIED
	};
	
	// All column names of time blocks.
	public static String[] allTimeBlockColumns = {
			SchedulesSQLiteHelper.COLUMN_ID,
			SchedulesSQLiteHelper.COLUMN_SID,
			SchedulesSQLiteHelper.COLUMN_NAME,
			SchedulesSQLiteHelper.COLUMN_START_TIME,
			SchedulesSQLiteHelper.COLUMN_END_TIME,
			SchedulesSQLiteHelper.COLUMN_DAY,
			SchedulesSQLiteHelper.COLUMN_BLOCK_TYPE_ID,
			SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID,
			SchedulesSQLiteHelper.COLUMN_LONGITUDE,
			SchedulesSQLiteHelper.COLUMN_LATITUDE
	};
	
	// All column names of block types.
	public static String[] allBlockTypeColumns = {
			SchedulesSQLiteHelper.COLUMN_ID,
			SchedulesSQLiteHelper.COLUMN_SID,
			SchedulesSQLiteHelper.COLUMN_NAME
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
		
		long insertId = this.database.insert(SchedulesSQLiteHelper.TABLE_USER, null, values);
		
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_USER,
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
		
		long insertId = this.database.insert(SchedulesSQLiteHelper.TABLE_SCHEDULE, null, values);
		
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		
		return scheduleFromCursor(cursor);
	}
	
	// Deletes given schedule from table.
	public void deleteSchedule(ScheduleData schedule) {
		this.database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_SCHEDULE + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ schedule.sid);
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
		
		this.database.execSQL(sql);
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getAllSchedulesCursor() {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		return cursor;
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getMenuSchedulesCursor() {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.menuScheduleColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		return cursor;
	}
	
	// Returns one schedule as per id.
	public ScheduleData getScheduleFromId(long id) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, SchedulesSQLiteHelper.COLUMN_ID +
				" = " + id, null, null, null, null);
		cursor.moveToFirst();
		
		return scheduleFromCursor(cursor);
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
	
	//
	// Methods for time block table.
	//
	// Creates a new time block entry in table.
	public TimeBlockData createTimeBlock(long sid, String name, String startTime, String endTime,
			int day, long blockTypeId, long scheduleId, double longitude, double latitude) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
		values.put(SchedulesSQLiteHelper.COLUMN_START_TIME, startTime);
		values.put(SchedulesSQLiteHelper.COLUMN_END_TIME, endTime);
		values.put(SchedulesSQLiteHelper.COLUMN_DAY, day);
		values.put(SchedulesSQLiteHelper.COLUMN_BLOCK_TYPE_ID, blockTypeId);
		values.put(SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID, scheduleId);
		values.put(SchedulesSQLiteHelper.COLUMN_LONGITUDE, longitude);
		values.put(SchedulesSQLiteHelper.COLUMN_LATITUDE, latitude);
		
		long insertId = this.database.insert(SchedulesSQLiteHelper.TABLE_TIME_BLOCK, null, values);
		
//		System.out.println("inserted time block with id: " + insertId);
		
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
				SchedulesDataSource.allTimeBlockColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		
		return timeBlockFromCursor(cursor);
	}
	
	// Deletes given time block from table.
	public void deleteTimeBlock(TimeBlockData timeBlock) {
		this.database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_TIME_BLOCK + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ timeBlock.sid);
	}
	
	// Updates given time block in table.
	public void updateTimeBlock(TimeBlockData timeBlock) {
		String sql = "UPDATE " + SchedulesSQLiteHelper.TABLE_TIME_BLOCK + " "
				+ "SET " + SchedulesSQLiteHelper.COLUMN_SID + " = " + timeBlock.sid + ", "
				+ SchedulesSQLiteHelper.COLUMN_NAME + " = '" + timeBlock.name + "', "
				+ SchedulesSQLiteHelper.COLUMN_START_TIME + " = '" + timeBlock.startTime + "', "
				+ SchedulesSQLiteHelper.COLUMN_END_TIME + " = '" + timeBlock.endTime + "', "
				+ SchedulesSQLiteHelper.COLUMN_DAY + " = " + Integer.toString(timeBlock.day) + ", "
				+ SchedulesSQLiteHelper.COLUMN_BLOCK_TYPE_ID + " = " + Long.toString(timeBlock.blockTypeId) + ", "
				+ SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID + " = " + Long.toString(timeBlock.scheduleId) + ", "
				+ SchedulesSQLiteHelper.COLUMN_LONGITUDE + " = " + Double.toString(timeBlock.longitude) + ", "
				+ SchedulesSQLiteHelper.COLUMN_LATITUDE + " = " + Double.toString(timeBlock.latitude) + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = " + timeBlock.id + ";";
		
		this.database.execSQL(sql);
	}
	
	// Returns a cursor that points to all time blocks of a given schedule.
	public ArrayList<TimeBlockData> getSchdeduleDayTimeBlocks(long scheduleId, int day) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
				SchedulesDataSource.allTimeBlockColumns,
				SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID + " = " + Long.toString(scheduleId) +
				" AND " + SchedulesSQLiteHelper.COLUMN_DAY + " = " + Integer.toString(day),
				null, null, null, SchedulesSQLiteHelper.COLUMN_START_TIME);
		cursor.moveToFirst();
		
		ArrayList<TimeBlockData> timeBlocks = new ArrayList<TimeBlockData>();
		while(!cursor.isAfterLast()) {
//			System.out.println("DataSource: start: " + cursor.getString(2) + " end: " + cursor.getString(3));
			timeBlocks.add(timeBlockFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		return timeBlocks;
	}
	
	public boolean isTimeBlockExists(long id) throws SQLException {
	    Cursor mCursor = this.database.query(true, SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
	    		SchedulesDataSource.allTimeBlockColumns, SchedulesSQLiteHelper.COLUMN_ID + 
	    		" = " + id, null, null, null, null, null);
	    
	    if (mCursor != null) {
	        return true;
	    }
	    return false;
	}
	
	// Returns time block pointed to by cursor.
	private TimeBlockData timeBlockFromCursor(Cursor cursor) {
		TimeBlockData timeBlock = new TimeBlockData();
		timeBlock.id = cursor.getLong(0);
		timeBlock.sid = cursor.getLong(1);
		timeBlock.name = cursor.getString(2);
		timeBlock.startTime = cursor.getString(3);
		timeBlock.endTime = cursor.getString(4);
		timeBlock.day = cursor.getInt(5);
		timeBlock.blockTypeId = cursor.getLong(6);
		timeBlock.scheduleId = cursor.getLong(7);
		timeBlock.longitude = cursor.getDouble(8);
		timeBlock.latitude = cursor.getDouble(9);
		
		return timeBlock;
	}
	
	//
	// Methods for block type table.
	//
	public BlockTypeData createBlockType(long sid, String name) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
		
		long insertId = this.database.insert(SchedulesSQLiteHelper.TABLE_BLOCK_TYPE, null, values);
		
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_BLOCK_TYPE,
				SchedulesDataSource.allBlockTypeColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		
		return blockTypeFromCursor(cursor);
	}
	
	// Returns a cursor that points to all block types.
	public HashMap<Long, BlockTypeData> getAllBlockTypes() {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_BLOCK_TYPE,
				SchedulesDataSource.allBlockTypeColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		HashMap<Long, BlockTypeData> hashMap = new HashMap<Long, BlockTypeData>();
		while(!cursor.isAfterLast()) {
			hashMap.put(cursor.getLong(1), blockTypeFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		return hashMap;
	}
	
	// Get id of block type.
	public long getBlockTypeId(String name) throws SQLException {
	    Cursor cursor = this.database.query(true, SchedulesSQLiteHelper.TABLE_BLOCK_TYPE,
	    		SchedulesDataSource.allBlockTypeColumns, SchedulesSQLiteHelper.COLUMN_NAME + 
	    		" = '" + name + "'", null, null, null, null, null);
	    cursor.moveToFirst();
	    
	    return cursor.getLong(0);
	}
	
	// Returns block type pointed to by cursor.
	private BlockTypeData blockTypeFromCursor(Cursor cursor) {
		BlockTypeData blockType = new BlockTypeData();
		blockType.id = cursor.getLong(0);
		blockType.sid = cursor.getLong(1);
		blockType.name = cursor.getString(2);
		
		return blockType;
	}
}