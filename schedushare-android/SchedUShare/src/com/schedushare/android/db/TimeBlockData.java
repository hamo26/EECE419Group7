package com.schedushare.android.db;

public class TimeBlockData {
	public long id;
	public long sid;
	public String name;
	public String startTime;
	public String endTime;
	public int day;
	public long blockTypeId;
	public long scheduleId;
	public double longitude;
	public double latitude;
	
	public String getDayString() {
		switch (this.day) {
			case 1:
				return "Monday";
			case 2:
				return "Tuesday";
			case 3:
				return "Wednesday";
			case 4:
				return "Thursday";
			case 5:
				return "Friday";
			case 6:
				return "Saturday";
			case 7:
				return "Sunday";
			default:
				return "Monday";
		}
	}
}