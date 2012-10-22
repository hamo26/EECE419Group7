package com.schedushare.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchedulesSQLiteHelper extends SQLiteOpenHelper {
	// Shared columns.
	public static final String COLUMN_ID = "_id";
	
	// Columns names of schedules table.
	public static final String TABLE_SCHEDULES = "schedules";
	public static final String COLUMN_OWNER = "owner";
	public static final String COLUMN_NAME = "name";
	
	// Database attributes.
	private static final String DATABASE_NAME = "passports.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation SQL statement.
	private static final String DATABASE_CREATE_SCHEDULES = "CREATE TABLE "
			+ TABLE_SCHEDULES + "("
			+ COLUMN_OWNER + "INTEGER NOT NULL, "
			+ COLUMN_NAME + "VARCHAR(250) NOT NULL, "
			+ ");";
	
	// Constructor.
	public SchedulesSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creates database.
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_SCHEDULES);
	}
	
	// Drops and recreates database.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
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