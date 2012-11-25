package com.schedushare.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.db.UserData;

public class ScheduleTimeBlockWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserData user;
	public ScheduleData schedule;
	public ArrayList<TimeBlockData> timeBlocks;
	
	public byte[] serialize() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(this);
        return b.toByteArray();
    }

    public static ScheduleTimeBlockWrapper deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (ScheduleTimeBlockWrapper) o.readObject();
    }
}
