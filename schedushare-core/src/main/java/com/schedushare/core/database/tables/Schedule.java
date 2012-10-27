/**
 * This class is generated by jOOQ
 */
package com.schedushare.core.database.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Schedule extends org.jooq.impl.UpdatableTableImpl<com.schedushare.core.database.tables.records.ScheduleRecord> {

	private static final long serialVersionUID = 1573411060;

	/**
	 * The singleton instance of schedushare.schedule
	 */
	public static final com.schedushare.core.database.tables.Schedule SCHEDULE = new com.schedushare.core.database.tables.Schedule();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.schedushare.core.database.tables.records.ScheduleRecord> getRecordType() {
		return com.schedushare.core.database.tables.records.ScheduleRecord.class;
	}

	/**
	 * The table column <code>schedushare.schedule.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.ScheduleRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>schedushare.schedule.NAME</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.ScheduleRecord, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>schedushare.schedule.ACTIVE</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.ScheduleRecord, java.lang.Byte> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.TINYINT, this);

	/**
	 * The table column <code>schedushare.schedule.OWNER_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT ScheduleOwner
	 * FOREIGN KEY (OWNER_ID)
	 * REFERENCES schedushare.user (ID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.ScheduleRecord, java.lang.Integer> OWNER_ID = createField("OWNER_ID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>schedushare.schedule.LAST_MODIFIED</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.ScheduleRecord, java.sql.Timestamp> LAST_MODIFIED = createField("LAST_MODIFIED", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	public Schedule() {
		super("schedule", com.schedushare.core.database.Schedushare.SCHEDUSHARE);
	}

	public Schedule(java.lang.String alias) {
		super(alias, com.schedushare.core.database.Schedushare.SCHEDUSHARE, com.schedushare.core.database.tables.Schedule.SCHEDULE);
	}

	@Override
	public org.jooq.UniqueKey<com.schedushare.core.database.tables.records.ScheduleRecord> getMainKey() {
		return com.schedushare.core.database.Keys.KEY_SCHEDULE_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<com.schedushare.core.database.tables.records.ScheduleRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.schedushare.core.database.tables.records.ScheduleRecord>>asList(com.schedushare.core.database.Keys.KEY_SCHEDULE_PRIMARY);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<com.schedushare.core.database.tables.records.ScheduleRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.schedushare.core.database.tables.records.ScheduleRecord, ?>>asList(com.schedushare.core.database.Keys.SCHEDULEOWNER);
	}

	@Override
	public com.schedushare.core.database.tables.Schedule as(java.lang.String alias) {
		return new com.schedushare.core.database.tables.Schedule(alias);
	}
}
