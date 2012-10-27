/**
 * This class is generated by jOOQ
 */
package com.schedushare.core.database;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;

import com.schedushare.core.database.tables.Schedule;
import com.schedushare.core.database.tables.Timeblock;
import com.schedushare.core.database.tables.User;
import com.schedushare.core.database.tables.records.ScheduleRecord;
import com.schedushare.core.database.tables.records.TimeblockRecord;
import com.schedushare.core.database.tables.records.UserRecord;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the <code>schedushare</code> 
 * schema
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Keys {

	// IDENTITY definitions

	// UNIQUE and PRIMARY KEY definitions
	public static final UniqueKey<ScheduleRecord> KEY_SCHEDULE_PRIMARY = UniqueKeys0.KEY_SCHEDULE_PRIMARY;
	public static final UniqueKey<TimeblockRecord> KEY_TIMEBLOCK_PRIMARY = UniqueKeys0.KEY_TIMEBLOCK_PRIMARY;
	public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = UniqueKeys0.KEY_USER_PRIMARY;
	public static final UniqueKey<UserRecord> KEY_USER_PASSWORD_UNIQUE = UniqueKeys0.KEY_USER_PASSWORD_UNIQUE;

	// FOREIGN KEY definitions
	public static final ForeignKey<ScheduleRecord, UserRecord> SCHEDULEOWNER = ForeignKeys0.SCHEDULEOWNER;
	public static final ForeignKey<TimeblockRecord, ScheduleRecord> TIMEBLOCKOWNER = ForeignKeys0.TIMEBLOCKOWNER;

	/**
	 * No instances
	 */
	private Keys() {}

	@SuppressWarnings({"hiding", "unchecked"})
	private static class UniqueKeys0 extends org.jooq.impl.AbstractKeys {
		public static final UniqueKey<ScheduleRecord> KEY_SCHEDULE_PRIMARY = createUniqueKey(Schedule.SCHEDULE, Schedule.SCHEDULE.ID);
		public static final UniqueKey<TimeblockRecord> KEY_TIMEBLOCK_PRIMARY = createUniqueKey(Timeblock.TIMEBLOCK, Timeblock.TIMEBLOCK.ID);
		public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = createUniqueKey(User.USER, User.USER.ID);
		public static final UniqueKey<UserRecord> KEY_USER_PASSWORD_UNIQUE = createUniqueKey(User.USER, User.USER.PASSWORD);
	}

	@SuppressWarnings({"hiding", "unchecked"})
	private static class ForeignKeys0 extends org.jooq.impl.AbstractKeys {
		public static final ForeignKey<ScheduleRecord, UserRecord> SCHEDULEOWNER = createForeignKey(Keys.KEY_USER_PRIMARY, Schedule.SCHEDULE, Schedule.SCHEDULE.OWNER_ID);
		public static final ForeignKey<TimeblockRecord, ScheduleRecord> TIMEBLOCKOWNER = createForeignKey(Keys.KEY_SCHEDULE_PRIMARY, Timeblock.TIMEBLOCK, Timeblock.TIMEBLOCK.SCHEDULE_ID);
	}
}
