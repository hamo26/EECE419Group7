package com.schedushare.core.user.service.impl;

import java.sql.Connection;
import java.util.List;

import com.google.inject.Inject;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.core.database.SchedushareFactory;
import com.schedushare.core.database.Tables;
import com.schedushare.core.user.service.UserService;

/**
 * Implements {@link UserService}.
 */
public class UserServiceImpl implements UserService {

	private final SchedushareExceptionFactory schedushareExceptionFactory;
	
	@Inject
	public UserServiceImpl(final SchedushareExceptionFactory schedushareExceptionFactory) {
		this.schedushareExceptionFactory = schedushareExceptionFactory;
	}
	
	@Override
	public UserEntity getUser(Connection connection, String userId) throws SchedushareException {
		SchedushareFactory getUserQuery = new SchedushareFactory(connection);
		
		List<UserEntity> userResult = getUserQuery.select()
												  .from(Tables.USER)
												  .where(Tables.USER.ID.equal(userId))
												  .fetchInto(UserEntity.class);
		if(userResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("User id does not exist");
		}
		
		return userResult.get(0);
	}

	@Override
	public UserEntity getUser(Connection connection, String userId, String authToken)
			throws SchedushareException {
		
		SchedushareFactory getUserQuery = new SchedushareFactory(connection);
		
		List<UserEntity> userResult = getUserQuery.select()
												  .from(Tables.SCHEDULE)
												  .where(Tables.USER.ID.equal(userId))
												  .and(Tables.USER.AUTH_TOKEN.equal(authToken))
												  .fetchInto(UserEntity.class);
		if(userResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("User id and password does not exist");
		}
		
		return userResult.get(0);
	}

	@Override
	public UserEntity createUser(Connection connection, UserEntity userEntity)
			throws SchedushareException {
		SchedushareFactory createUserQuery = new SchedushareFactory(connection);
		
		List<UserEntity> userResult = createUserQuery.select()
													 .from(Tables.USER)
													 .where(Tables.USER.ID.equal(userEntity.getUserId()))
													 .fetchInto(UserEntity.class);
		if (!userResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("User id was not unique.");
		} else {
			try {
				createUserQuery.insertInto(Tables.USER, Tables.USER.ID, Tables.USER.EMAIL, Tables.USER.NAME, 
														Tables.USER.AUTH_TOKEN)
													   .values(userEntity.getUserId(), userEntity.getEmail(), userEntity.getName(), userEntity.getAuthToken())
													   .execute();
				List<UserEntity> createdUserResult = createUserQuery.select()
														  .from(Tables.USER)
														  .orderBy(Tables.USER.ID)
														  .limit(1)
														  .fetchInto(UserEntity.class);
																		  
				return createdUserResult.get(0);
			} catch (Exception e) {
				throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
			}
													   
		}
	}

	@Override
	public UserEntity updateUser(Connection connection, UserEntity userEntity)
			throws SchedushareException {
		SchedushareFactory updateUserQuery = new SchedushareFactory(connection);
		List<UserEntity> result = updateUserQuery.select()
					   .from(Tables.USER)
					   .where(Tables.USER.ID.equal(userEntity.getUserId()))
					   .fetchInto(UserEntity.class);
		if(result.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("Attempting to update track that does not exist.");
		} else {
			try {
				updateUserQuery.update(Tables.USER)
							   .set(Tables.USER.NAME, userEntity.getName())
							   .set(Tables.USER.AUTH_TOKEN, userEntity.getAuthToken())
							   .set(Tables.USER.EMAIL, userEntity.getEmail())
							   .where(Tables.USER.ID.equal(userEntity.getUserId()))
							   .execute();
			} catch (Exception e) {
				throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
			}
		}
		return userEntity;
	}


}
