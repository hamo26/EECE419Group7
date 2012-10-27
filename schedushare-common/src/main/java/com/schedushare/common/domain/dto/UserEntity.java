package com.schedushare.common.domain.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * User {@link RestEntity} passed between client and server as a JSON representation.
 * 
 * Simple user pojo that is structured to be serialized by JOOQ and automatically mapped.
 * Did not like the DTO generated by JOOQ so we chose to make our own.
 */
public class UserEntity extends RestEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5832677761522878554L;

	public static final String AUTH_TOKEN = "auth-token";
	public static final String USER_ID = "user-id";

	@SerializedName(USER_ID)
	private String userId;
	
	@SerializedName(AUTH_TOKEN)
	private String authToken;
	
	/**
	 * Default constructor.
	 * 
	 * Used by jooq to read from/into database.
	 *
	 * @param USER_ID the user id
	 * @param AUTH_TOKEN the auth token
	 */
	public UserEntity(final String USER_ID, final String AUTH_TOKEN) {
		this.userId = USER_ID;
		this.authToken = AUTH_TOKEN;
	}

	public String getUserId() {
		return this.userId;
	}
	
	public String getAuthToken() {
		return this.authToken;
	}
}