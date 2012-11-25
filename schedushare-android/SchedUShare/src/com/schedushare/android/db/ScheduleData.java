package com.schedushare.android.db;

import java.io.Serializable;

public class ScheduleData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public long sid;
	public String name;
	public boolean active;
	public long ownerId;
	public String lastModified;
}