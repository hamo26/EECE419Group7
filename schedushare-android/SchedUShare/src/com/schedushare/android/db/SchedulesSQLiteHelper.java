package com.schedushare.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchedulesSQLiteHelper extends SQLiteOpenHelper {
	// Shared columns.
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SID = "sid";
	public static final String COLUMN_NAME = "name";
	
	// Column names of user table.
	public static final String TABLE_USER = "user";
	
	// Column names of schedule table.
	public static final String TABLE_SCHEDULE = "schedule";
	public static final String COLUMN_OWNER_ID = "owner_id";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_LAST_MODIFIED = "last_modified";
	
	// Column names of time block table.
	public static final String TABLE_TIME_BLOCK = "time_block";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_BLOCK_TYPE_ID = "block_type_id";
	public static final String COLUMN_SCHEDULE_ID = "schedule_id";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_LATITUDE = "latitude";
	
	// Column names of block type table.
	public static final String TABLE_BLOCK_TYPE = "block_type";
	
	// Database attributes.
	private static final String DATABASE_NAME = "schedules.db";
	private static final int DATABASE_VERSION = 1;
	
	// Table creation SQL statements.
	private static final String DATABASE_CREATE_USER = "CREATE TABLE "
			+ TABLE_USER + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " VARCHAR(255) NOT NULL "
			+ ");";
	
	private static final String DATABASE_CREATE_SCHEDULE = "CREATE TABLE "
			+ TABLE_SCHEDULE + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " VARCHAR(255) NOT NULL, "
			+ COLUMN_ACTIVE + " BOOLEAN NOT NULL, "
			+ COLUMN_OWNER_ID + " INTEGER NOT NULL, "
			+ COLUMN_LAST_MODIFIED + " TIMESTAMP NOT NULL, "
			+ " FOREIGN KEY (" + COLUMN_OWNER_ID + ") REFERENCES " + TABLE_USER
			+ " ON DELETE CASCADE "
			+ ");";
	
	private static final String DATABASE_CREATE_TIME_BLOCK = "CREATE TABLE "
			+ TABLE_TIME_BLOCK + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SID + " INTEGER NOT NULL, "
			+ COLUMN_START_TIME + " TIME NOT NULL, "
			+ COLUMN_END_TIME + " TIME NOT NULL, "
			+ COLUMN_DAY + " INTEGER NOT NULL, "
			+ COLUMN_BLOCK_TYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_SCHEDULE_ID + " INTEGER NOT NULL, "
			+ COLUMN_LONGITUDE + "DOUBLE NOT NULL, "
			+ COLUMN_LATITUDE + "DOUBLE NOT NULL, "
			+ " FOREIGN KEY (" + COLUMN_BLOCK_TYPE_ID + ") REFERENCES " + TABLE_BLOCK_TYPE
			+ " ON DELETE CASCADE, "
			+ " FOREIGN KEY (" + COLUMN_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE
			+ " ON DELETE CASCADE "
			+ ");";
	
	private static final String DATABASE_CREATE_BLOCK_TYPE = "CREATE TABLE "
			+ TABLE_BLOCK_TYPE + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " VARCHAR(100) NOT NULL "
			+ ");";
			
	
	// Constructor.
	public SchedulesSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creates database.
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_USER);
		database.execSQL(DATABASE_CREATE_SCHEDULE);
		database.execSQL(DATABASE_CREATE_TIME_BLOCK);
		database.execSQL(DATABASE_CREATE_BLOCK_TYPE);
	}
	
	// Drops and recreates database.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_BLOCK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK_TYPE);
		this.onCreate(db);
	}
	
	// Enables foreign key constraints.
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		
		if (!db.isReadOnly()) {
			// Enable foreign key constraints.
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
}