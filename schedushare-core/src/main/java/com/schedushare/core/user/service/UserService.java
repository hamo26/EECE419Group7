package com.schedushare.core.user.service;

import java.sql.Connection;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.user.service.impl.UserServiceImpl;

/**
 * The Interface UserService.
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

	/**
	 * Gets a user.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @return the user
	 */
	UserEntity getUser(Connection connection, int userId) throws SchedushareException;
	
	/**
	 * Gets a user.
	 *
	 * @param userId the user id
	 * @param authToken the password
	 * @return the user
	 */
	UserEntity getUser(Connection connection, int userId, String authToken) throws SchedushareException;
	
	/**
	 * Creates a user.
	 *
	 * @param userEntity the user entity
	 * @return the user entity
	 */
	UserEntity createUser(Connection connection, UserEntity userEntity) throws SchedushareException;
}
