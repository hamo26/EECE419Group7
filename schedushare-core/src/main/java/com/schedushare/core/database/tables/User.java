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
public class User extends org.jooq.impl.UpdatableTableImpl<com.schedushare.core.database.tables.records.UserRecord> {

	private static final long serialVersionUID = -1076924689;

	/**
	 * The singleton instance of schedushare.user
	 */
	public static final com.schedushare.core.database.tables.User USER = new com.schedushare.core.database.tables.User();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.schedushare.core.database.tables.records.UserRecord> getRecordType() {
		return com.schedushare.core.database.tables.records.UserRecord.class;
	}

	/**
	 * The table column <code>schedushare.user.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.UserRecord, java.lang.String> ID = createField("ID", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>schedushare.user.AUTHTOKEN</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.UserRecord, java.lang.String> AUTH_TOKEN = createField("AUTH_TOKEN", org.jooq.impl.SQLDataType.VARCHAR, this);
	
	/**
	 * The table column <code>schedushare.user.NAME</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.UserRecord, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR, this);
	

	/**
	 * The table column <code>schedushare.user.EMAIL</code>
	 */
	public final org.jooq.TableField<com.schedushare.core.database.tables.records.UserRecord, java.lang.String> EMAIL = createField("EMAIL", org.jooq.impl.SQLDataType.VARCHAR, this);
	

	public User() {
		super("USER", com.schedushare.core.database.Schedushare.SCHEDUSHARE);
	}

	public User(java.lang.String alias) {
		super(alias, com.schedushare.core.database.Schedushare.SCHEDUSHARE, com.schedushare.core.database.tables.User.USER);
	}

	@Override
	public org.jooq.UniqueKey<com.schedushare.core.database.tables.records.UserRecord> getMainKey() {
		return com.schedushare.core.database.Keys.KEY_USER_PRIMARY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<com.schedushare.core.database.tables.records.UserRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.schedushare.core.database.tables.records.UserRecord>>asList(com.schedushare.core.database.Keys.KEY_USER_PRIMARY);
	}

	@Override
	public com.schedushare.core.database.tables.User as(java.lang.String alias) {
		return new com.schedushare.core.database.tables.User(alias);
	}
}
