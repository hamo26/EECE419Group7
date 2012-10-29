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
public class UserRecord extends org.jooq.impl.UpdatableRecordImpl<com.schedushare.core.database.tables.records.UserRecord> {

	private static final long serialVersionUID = -1497057835;

	/**
	 * The table column <code>schedushare.user.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value) {
		setValue(com.schedushare.core.database.tables.User.USER.ID, value);
	}

	/**
	 * The table column <code>schedushare.user.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId() {
		return getValue(com.schedushare.core.database.tables.User.USER.ID);
	}

	/**
	 * The table column <code>schedushare.user.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.util.List<com.schedushare.core.database.tables.records.ScheduleRecord> fetchScheduleList() {
		return create()
			.selectFrom(com.schedushare.core.database.tables.Schedule.SCHEDULE)
			.where(com.schedushare.core.database.tables.Schedule.SCHEDULE.USER_ID.equal(getValue(com.schedushare.core.database.tables.User.USER.ID)))
			.fetch();
	}

	/**
	 * The table column <code>schedushare.user.PASSWORD</code>
	 */
	public void setPassword(java.lang.String value) {
		setValue(com.schedushare.core.database.tables.User.USER.AUTH_TOKEN, value);
	}

	/**
	 * The table column <code>schedushare.user.PASSWORD</code>
	 */
	public java.lang.String getPassword() {
		return getValue(com.schedushare.core.database.tables.User.USER.AUTH_TOKEN);
	}

	/**
	 * The table column <code>schedushare.user.EMAIL</code>
	 */
	public java.lang.String getEmail() {
		return getValue(com.schedushare.core.database.tables.User.USER.EMAIL);
	}
	
	/**
	 * The table column <code>schedushare.user.EMAIL</code>
	 */
	public void setEmail(java.lang.String value) {
		setValue(com.schedushare.core.database.tables.User.USER.EMAIL, value);
	}


	/**
	 * Create a detached UserRecord
	 */
	public UserRecord() {
		super(com.schedushare.core.database.tables.User.USER);
	}
}
