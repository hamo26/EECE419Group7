package com.schedushare.core.user.service;

import java.sql.Connection;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.user.service.impl.UserServiceImpl;


/**
 * User DAO service.
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {


	/**
	 * Gets a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @return the user
	 * @throws SchedushareException the schedushare exception
	 */
	UserEntity getUser(Connection connection, String userId) throws SchedushareException;
	
	/**
	 * Gets a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @param authToken the auth token
	 * @return the user
	 * @throws SchedushareException the schedushare exception
	 */
	UserEntity getUser(Connection connection, String userId, String authToken) throws SchedushareException;
	
	/**
	 * Creates a user.
	 *
	 * @param userEntity the user entity
	 * @return the user entity
	 */
	UserEntity createUser(Connection connection, UserEntity userEntity) throws SchedushareException;
	
	/**
	 * Updates a user.
	 *
	 * @param connection the connection
	 * @param userEntity the user entity
	 * @return the user entity
	 * @throws SchedushareException the schedushare exception
	 */
	UserEntity updateUser(Connection connection, UserEntity userEntity) throws SchedushareException;
}
