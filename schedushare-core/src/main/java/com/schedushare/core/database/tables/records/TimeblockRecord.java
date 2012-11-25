/**
 * This class is generated by jOOQ
 */
package com.schedushare.core.database.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class TimeblockRecord extends org.jooq.impl.UpdatableRecordImpl<com.schedushare.core.database.tables.records.TimeblockRecord> {

	private static final long serialVersionUID = -1250011613;

	/**
	 * The table column <code>schedushare.timeblock.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.ID, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.ID);
	}

	/**
	 * The table column <code>schedushare.timeblock.START_TIME</code>
	 */
	public void setStartTime(java.sql.Time value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.START_TIME, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.START_TIME</code>
	 */
	public java.sql.Time getStartTime() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.START_TIME);
	}

	/**
	 * The table column <code>schedushare.timeblock.END_TIME</code>
	 */
	public void setEndTime(java.sql.Time value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.END_TIME, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.END_TIME</code>
	 */
	public java.sql.Time getEndTime() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.END_TIME);
	}

	/**
	 * The table column <code>schedushare.timeblock.DAY</code>
	 */
	public void setDay(com.schedushare.core.database.enums.TimeblockDay value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.DAY, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.DAY</code>
	 */
	public com.schedushare.core.database.enums.TimeblockDay getDay() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.DAY);
	}

	/**
	 * The table column <code>schedushare.timeblock.TYPE</code>
	 */
	public void setType(java.lang.String value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.TYPE, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.TYPE</code>
	 */
	public java.lang.String getType() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.TYPE);
	}

	/**
	 * The table column <code>schedushare.timeblock.NAME</code>
	 */
	public void setName(java.lang.String value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.NAME, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.NAME</code>
	 */
	public java.lang.String getName() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.NAME);
	}
	
	/**
	 * The table column <code>schedushare.timeblock.SCHEDULE_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT TimeBlockOwner
	 * FOREIGN KEY (SCHEDULE_ID)
	 * REFERENCES schedushare.schedule (ID)
	 * </pre></code>
	 */
	public void setScheduleId(java.lang.Integer value) {
		setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID, value);
	}

	/**
	 * The table column <code>schedushare.timeblock.SCHEDULE_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT TimeBlockOwner
	 * FOREIGN KEY (SCHEDULE_ID)
	 * REFERENCES schedushare.schedule (ID)
	 * </pre></code>
	 */
	public java.lang.Integer getScheduleId() {
		return getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID);
	}

	/**
	 * Link this record to a given {@link com.schedushare.core.database.tables.records.ScheduleRecord 
	 * ScheduleRecord}
	 */
	public void setScheduleId(com.schedushare.core.database.tables.records.ScheduleRecord value) {
		if (value == null) {
			setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID, null);
		}
		else {
			setValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID, value.getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ID));
		}
	}

	/**
	 * The table column <code>schedushare.timeblock.SCHEDULE_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT TimeBlockOwner
	 * FOREIGN KEY (SCHEDULE_ID)
	 * REFERENCES schedushare.schedule (ID)
	 * </pre></code>
	 */
	public com.schedushare.core.database.tables.records.ScheduleRecord fetchSchedule() {
		return create()
			.selectFrom(com.schedushare.core.database.tables.Schedule.SCHEDULE)
			.where(com.schedushare.core.database.tables.Schedule.SCHEDULE.ID.equal(getValue(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID)))
			.fetchOne();
	}

	/**
	 * Create a detached TimeblockRecord
	 */
	public TimeblockRecord() {
		super(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK);
	}
}
