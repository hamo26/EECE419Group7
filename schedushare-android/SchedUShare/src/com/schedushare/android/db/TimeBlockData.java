package com.schedushare.android.db;

import java.io.Serializable;

public class TimeBlockData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	public static int getDayIntFromString(String s) {
		if (s.equals("Monday")) {
			return 0;
		} else if (s.equals("Tuesday")) {
			return 1;
		} else if (s.equals("Wednesday")) {
			return 2;
		} else if (s.equals("Thursday")) {
			return 3;
		} else if (s.equals("Friday")) {
			return 4;
		} else if (s.equals("Saturday")) {
			return 5;
		} else if (s.equals("Sunday")) {
			return 6;
		} else {
			return 0;
		}
	}
	
	public String getBlockTypeString() {
			switch ((int)this.blockTypeId) {
			case 1:
				return "School";
			case 2:
				return "Work";
			case 3:
				return "Social";
			case 4:
				return "Extra Curricular";
			case 5:
				return "On Bus";
			case 6:
				return "On Vacation";
			default:
				return "Work";
		}
	}
}