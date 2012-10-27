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
public class ScheduleRecord extends org.jooq.impl.UpdatableRecordImpl<com.schedushare.core.database.tables.records.ScheduleRecord> {

	private static final long serialVersionUID = 1640478464;

	/**
	 * The table column <code>schedushare.schedule.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value) {
		setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ID, value);
	}

	/**
	 * The table column <code>schedushare.schedule.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId() {
		return getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ID);
	}

	/**
	 * The table column <code>schedushare.schedule.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.util.List<com.schedushare.core.database.tables.records.TimeblockRecord> fetchTimeblockList() {
		return create()
			.selectFrom(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK)
			.where(com.schedushare.core.database.tables.Timeblock.TIMEBLOCK.SCHEDULE_ID.equal(getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ID)))
			.fetch();
	}

	/**
	 * The table column <code>schedushare.schedule.NAME</code>
	 */
	public void setName(java.lang.String value) {
		setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.NAME, value);
	}

	/**
	 * The table column <code>schedushare.schedule.NAME</code>
	 */
	public java.lang.String getName() {
		return getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.NAME);
	}

	/**
	 * The table column <code>schedushare.schedule.ACTIVE</code>
	 */
	public void setActive(java.lang.Byte value) {
		setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ACTIVE, value);
	}

	/**
	 * The table column <code>schedushare.schedule.ACTIVE</code>
	 */
	public java.lang.Byte getActive() {
		return getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.ACTIVE);
	}

	/**
	 * The table column <code>schedushare.schedule.OWNER_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT ScheduleOwner
	 * FOREIGN KEY (OWNER_ID)
	 * REFERENCES schedushare.user (ID)
	 * </pre></code>
	 */
	public void setOwnerId(java.lang.Integer value) {
		setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.OWNER_ID, value);
	}

	/**
	 * The table column <code>schedushare.schedule.OWNER_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT ScheduleOwner
	 * FOREIGN KEY (OWNER_ID)
	 * REFERENCES schedushare.user (ID)
	 * </pre></code>
	 */
	public java.lang.Integer getOwnerId() {
		return getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.OWNER_ID);
	}

	/**
	 * Link this record to a given {@link com.schedushare.core.database.tables.records.UserRecord 
	 * UserRecord}
	 */
	public void setOwnerId(com.schedushare.core.database.tables.records.UserRecord value) {
		if (value == null) {
			setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.OWNER_ID, null);
		}
		else {
			setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.OWNER_ID, value.getValue(com.schedushare.core.database.tables.User.USER.ID));
		}
	}

	/**
	 * The table column <code>schedushare.schedule.OWNER_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT ScheduleOwner
	 * FOREIGN KEY (OWNER_ID)
	 * REFERENCES schedushare.user (ID)
	 * </pre></code>
	 */
	public com.schedushare.core.database.tables.records.UserRecord fetchUser() {
		return create()
			.selectFrom(com.schedushare.core.database.tables.User.USER)
			.where(com.schedushare.core.database.tables.User.USER.ID.equal(getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.OWNER_ID)))
			.fetchOne();
	}

	/**
	 * The table column <code>schedushare.schedule.LAST_MODIFIED</code>
	 */
	public void setLastModified(java.sql.Timestamp value) {
		setValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.LAST_MODIFIED, value);
	}

	/**
	 * The table column <code>schedushare.schedule.LAST_MODIFIED</code>
	 */
	public java.sql.Timestamp getLastModified() {
		return getValue(com.schedushare.core.database.tables.Schedule.SCHEDULE.LAST_MODIFIED);
	}

	/**
	 * Create a detached ScheduleRecord
	 */
	public ScheduleRecord() {
		super(com.schedushare.core.database.tables.Schedule.SCHEDULE);
	}
}
