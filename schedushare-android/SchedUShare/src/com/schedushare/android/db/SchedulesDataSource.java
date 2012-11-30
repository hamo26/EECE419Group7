package com.schedushare.android.db;

import java.util.ArrayList;
import java.util.Calendar;
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
		UserData user = getUserFromSid(sid);
		
		// Only create user if it doesn't yet exist.
		if (user == null) {
			ContentValues values = new ContentValues();
			values.put(SchedulesSQLiteHelper.COLUMN_SID, sid);
			values.put(SchedulesSQLiteHelper.COLUMN_NAME, name);
			
			long insertId = this.database.insert(SchedulesSQLiteHelper.TABLE_USER, null, values);
			
			Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_USER,
					SchedulesDataSource.allUserColumns, SchedulesSQLiteHelper.COLUMN_ID + " = " + insertId,
					null, null, null, null);
			cursor.moveToFirst();
			
			return userFromCursor(cursor);
		} else {			
			return user;
		}
	}
	
	// Updates given schedule in table.
	public void updateUser(UserData user) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, user.sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, user.name);
		this.database.update(SchedulesSQLiteHelper.TABLE_USER, values,
				SchedulesSQLiteHelper.COLUMN_ID + " = " + user.id, null);
	}
	
	// Get user from from id.
	public UserData getUserFromId(long id) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_USER,
				SchedulesDataSource.allUserColumns, SchedulesSQLiteHelper.COLUMN_ID +
				" = " + id, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return userFromCursor(cursor);
		}
	}
	
	
	// Get user from from server id.
	public UserData getUserFromSid(long sid) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_USER,
				SchedulesDataSource.allUserColumns, SchedulesSQLiteHelper.COLUMN_SID +
				" = " + sid, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return userFromCursor(cursor);
		}
	}
	
	// Checks whether owner has been created.
	public UserData checkOwnerCreated() {
		UserData owner = getUserFromId(1);
		
		// If owner has not been created, create and return owner entry.
		if (owner == null) {
			owner = createUser(0, "owner");
			Calendar currentTime = Calendar.getInstance();
			createSchedule(0, "First Schedule", true, owner.id, currentTime.getTime().toString());
			createBlockType(1, "School");
	        createBlockType(2, "Work");
	        createBlockType(3, "Social");
	        createBlockType(4, "Extra Curricular");
	        createBlockType(5, "On Bus");
	        createBlockType(6, "On Vacation");
		}
		
		return owner;
	}
	
	// Check whether user has been created.
	public boolean isUserCreated(long sid) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_USER,
				SchedulesDataSource.allUserColumns, SchedulesSQLiteHelper.COLUMN_SID +
				" = " + sid, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		return true;
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
		ScheduleData schedule = getScheduleFromSid(sid);
		
		// Only create if the schedule does not already exist.
		if (schedule == null) {
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
		} else {
			schedule.name = name;
			schedule.active = active;
			schedule.ownerId = ownerId;
			schedule.lastModified = lastModified;
			updateSchedule(schedule);
			
			return schedule;
		}
	}
	
	// Deletes given schedule from table.
	public void deleteSchedule(ScheduleData schedule) {
		this.database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_SCHEDULE + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ schedule.id);
	}
	
	// Deletes given schedule with given id from table.
	public void deleteSchedule(long scheduleId) {
		this.database.execSQL("DELETE FROM "
				+ SchedulesSQLiteHelper.TABLE_SCHEDULE + " "
				+ "WHERE " + SchedulesSQLiteHelper.COLUMN_ID + " = "
				+ scheduleId);
	}
	
	// Updates given schedule in table.
	public void updateSchedule(ScheduleData schedule) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, schedule.sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, schedule.name);
		values.put(SchedulesSQLiteHelper.COLUMN_ACTIVE, schedule.active);
		values.put(SchedulesSQLiteHelper.COLUMN_OWNER_ID, schedule.ownerId);
		values.put(SchedulesSQLiteHelper.COLUMN_LAST_MODIFIED, schedule.lastModified);
		this.database.update(SchedulesSQLiteHelper.TABLE_SCHEDULE, values,
				SchedulesSQLiteHelper.COLUMN_ID + " = " + schedule.id, null);
	}
	
	// Returns a cursor that points to all schedules currently in table.
	public Cursor getAllOwnerSchedulesCursor(long ownerId) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns,
				SchedulesSQLiteHelper.COLUMN_OWNER_ID + " = " + ownerId,
				null, null, null, null);
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
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return scheduleFromCursor(cursor);
		}
	}
	
	// Returns one schedule as per sid.
	public ScheduleData getScheduleFromSid(long sid) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, SchedulesSQLiteHelper.COLUMN_SID +
				" = " + sid, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return scheduleFromCursor(cursor);
		}
	}
	
	public boolean isScheduleExists(long sid) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns, SchedulesSQLiteHelper.COLUMN_SID +
				" = " + sid, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0)
			return false;
		
		return true;
	}
	
	// Returns active schedule as per owner id.
	public ScheduleData getActiveScheduleFromOwnerId(long ownerId) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns,
				SchedulesSQLiteHelper.COLUMN_OWNER_ID + " = " + ownerId + " AND " + 
				SchedulesSQLiteHelper.COLUMN_ACTIVE + " = " + 1,
				null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return scheduleFromCursor(cursor);
		}
	}
	
	// Searches for a schedule of the same name.
	public ScheduleData getScheduleFromName(String name) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_SCHEDULE,
				SchedulesDataSource.allScheduleColumns,
				SchedulesSQLiteHelper.COLUMN_NAME + " = ?",
				new String[]{name}, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return scheduleFromCursor(cursor);
		}
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
				+ timeBlock.id);
	}
	
	// Updates given time block in table.
	public void updateTimeBlock(TimeBlockData timeBlock) {
		ContentValues values = new ContentValues();
		values.put(SchedulesSQLiteHelper.COLUMN_SID, timeBlock.sid);
		values.put(SchedulesSQLiteHelper.COLUMN_NAME, timeBlock.name);
		values.put(SchedulesSQLiteHelper.COLUMN_START_TIME, timeBlock.startTime);
		values.put(SchedulesSQLiteHelper.COLUMN_END_TIME, timeBlock.endTime);
		values.put(SchedulesSQLiteHelper.COLUMN_DAY, timeBlock.day);
		values.put(SchedulesSQLiteHelper.COLUMN_BLOCK_TYPE_ID, timeBlock.blockTypeId);
		values.put(SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID, timeBlock.scheduleId);
		values.put(SchedulesSQLiteHelper.COLUMN_LONGITUDE, timeBlock.longitude);
		values.put(SchedulesSQLiteHelper.COLUMN_LATITUDE, timeBlock.latitude);
		this.database.update(SchedulesSQLiteHelper.TABLE_TIME_BLOCK, values,
				SchedulesSQLiteHelper.COLUMN_ID + " = " + timeBlock.id, null);
	}
	
	// Returns a cursor that points to all time blocks of a given schedule and day.
	public ArrayList<TimeBlockData> getScheduleDayTimeBlocks(long scheduleId, int day) {
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
	
	// Checks whether the day is free for the given schedule.
	public boolean isDayFree(long scheduleId, int day) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
				SchedulesDataSource.allTimeBlockColumns,
				SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID + " = " + Long.toString(scheduleId) +
				" AND " + SchedulesSQLiteHelper.COLUMN_DAY + " = " + Integer.toString(day),
				null, null, null, null);
	    
	    if (cursor == null || cursor.getCount() == 0) {
	        return true;
	    }
	    return false;
	}
	
	// Returns a cursor that points to all time blocks of a given schedule.
	public ArrayList<TimeBlockData> getSchdeduleTimeBlocks(long scheduleId) {
		Cursor cursor = this.database.query(SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
				SchedulesDataSource.allTimeBlockColumns,
				SchedulesSQLiteHelper.COLUMN_SCHEDULE_ID + " = " + Long.toString(scheduleId),
				null, null, null, SchedulesSQLiteHelper.COLUMN_START_TIME);
		cursor.moveToFirst();
		
		ArrayList<TimeBlockData> timeBlocks = new ArrayList<TimeBlockData>();
		while(!cursor.isAfterLast()) {
//				System.out.println("DataSource: start: " + cursor.getString(2) + " end: " + cursor.getString(3));
			timeBlocks.add(timeBlockFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		return timeBlocks;
	}
	
	public boolean isTimeBlockExists(long id) {
	    Cursor cursor = this.database.query(true, SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
	    		SchedulesDataSource.allTimeBlockColumns, SchedulesSQLiteHelper.COLUMN_ID + 
	    		" = " + id, null, null, null, null, null);
	    
	    if (cursor == null || cursor.getCount() == 0) {
	        return false;
	    }
	    return true;
	}
	
	public TimeBlockData getTimeBlockFromSid(long sid) {
	    Cursor cursor = this.database.query(true, SchedulesSQLiteHelper.TABLE_TIME_BLOCK,
	    		SchedulesDataSource.allTimeBlockColumns, SchedulesSQLiteHelper.COLUMN_SID + 
	    		" = " + sid, null, null, null, null, null);
	    
	    if (cursor == null || cursor.getCount() == 0) {
	        return null;
	    } else {
	    	cursor.moveToFirst();
	    	return timeBlockFromCursor(cursor);
	    }
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
	public long getBlockTypeId(String name) {
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