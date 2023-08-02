package com.isteer.springsecurity.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.isteer.springsecurity.exception.DetailsNotProvidedException;
import com.isteer.springsecurity.statuscode.StatusCodes;
import com.isteer.springsecurity.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.springbootsecurity.entities.VariableDeclaration;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserResult;
import com.isteer.springsecurity.exception.SqlSyntaxException;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.response.CustomPostResponse;
import com.isteer.springsecurity.sqlqueries.SqlQueries;

@Repository
public class UserDaoImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageService messageservice;

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger auditlogger = LogManager.getLogger(UserDaoImpl.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: User DAO Layer";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: User DAO Layer";

	public User findByEmail(String email) {

		return jdbcTemplate.query(SqlQueries.FIND_BY_EMAIL, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = new User();
				try {

					while (rs.next()) {

						user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
						user.setName(rs.getString(VariableDeclaration.NAME));
						user.setEmail(rs.getString(VariableDeclaration.EMAIL));
						user.setPassword(rs.getString(VariableDeclaration.PASSWORD));
						user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
						user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
						user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
						user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));

					}
					return user;
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(0, "cannot find by name", exceptions);
				}
			}
		}, email);
	}

	public User findByName(String name) {

		return jdbcTemplate.query(SqlQueries.FIND_BY_NAME, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = new User();
				try {

					while (rs.next()) {

						user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
						user.setName(rs.getString(VariableDeclaration.NAME));
						user.setEmail(rs.getString(VariableDeclaration.EMAIL));
						user.setPassword(rs.getString(VariableDeclaration.PASSWORD));
						user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
						user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
						user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
						user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));

					}
					return user;
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(0, "cannot find by name", exceptions);
				}
			}
		}, name);
	}

	public List<User> findAll() {

		return jdbcTemplate.query(SqlQueries.FIND_ALL_USERS, new ResultSetExtractor<List<User>>() {

			@Override
			public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<User> users = new ArrayList<>();

				try {

					while (rs.next()) {
						User user = new User();
						user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
						user.setName(rs.getString(VariableDeclaration.NAME));
						user.setEmail(rs.getString(VariableDeclaration.EMAIL));
						user.setPassword(rs.getString(VariableDeclaration.PASSWORD));
						user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
						user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
						user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
						user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));

						users.add(user);
					}
					return users;
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(0, "cannot find by name", exceptions);
				}
			}
		});
	}

	public void save(User user) {
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		auditlogger.info("inside userdao");

		try {
			int count=jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
				auditlogger.info("inside ps of userdao");
				ps.setLong(1, user.getUserId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPassword());
				ps.setBoolean(5, user.isAccountNonLocked());
				ps.setBoolean(6, user.isAccountNonExpired());
				ps.setBoolean(7, user.isCredentialsNonExpired());
				ps.setBoolean(8, user.isEnabled());
				auditlogger.info(user);
				return ps;
			}, keyHolder) ;
			if(count == 1){
				user.setUserId(keyHolder.getKey().longValue());
			}
		} catch (NullPointerException nullexceptions) {

			List<String> exceptions = new ArrayList<>();
			exceptions.add(nullexceptions.getMessage());
			throw new SqlSyntaxException(0, "", exceptions);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(0, "", exceptions);
		}
		

	}

	public CustomPostResponse update(User user, long userId) {
		try {

			if (jdbcTemplate.update(SqlQueries.UPDATE_USERS_BY_ID_QUERY, user.getName(),
					user.getEmail(), user.getPassword(), user.isAccountNonLocked(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), userId) >= 1) {

				jdbcTemplate.query(SqlQueries.FIND_BY_USER_ID, new ResultSetExtractor<User>() {

					public User extractData(ResultSet rs) {

						User user = new User();
						try {
							while (rs.next()) {
								user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
								user.setName(rs.getString(VariableDeclaration.NAME));
								user.setEmail(rs.getString(VariableDeclaration.EMAIL));
								user.setPassword(rs.getString(VariableDeclaration.PASSWORD));
								user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
								user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
								user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
								user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));

							}
						} catch (SQLException exception) {
							List<String> exceptions = new ArrayList<>();
							exceptions.add(exception.getMessage());
							auditlogger.warn(wLog, userId, StatusCodes.BAD_REQUEST.getStatusCode(),
									messageservice.getBadSqlSyntaxErrorMessage(),exceptions);
							throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
									messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
						}
						return user;
					}
				}, userId);

				auditlogger.info(iLog, userId, StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsUpdatedMessage());
				return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsUpdatedMessage(), getUserWithRoleById(userId));

			} else {
				List<String> exception = new ArrayList<>();
				exception.add("Provide all details required");
				auditlogger.warn(wLog, userId, StatusCodes.BAD_REQUEST.getStatusCode(),
						messageservice.getDetailsNotProvidedMessage(), exception);
				throw new DetailsNotProvidedException(StatusCodes.BAD_REQUEST.getStatusCode(),
						messageservice.getDetailsNotProvidedMessage(), exception);
			}

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, userId, StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);

		}

	}

	public UserResult getUserWithRoleByEmail(String email) {
	    String sql = "CALL GetUserRolesByEmail(?)";
	  
	    return jdbcTemplate.query(sql, ps -> ps.setString(1, email), this::mapUserResult);
	}
	
	public UserResult getUserWithRoleByName(String name) {
	    String sql = "CALL GetUserRolesByName(?)";
	  
	    return jdbcTemplate.query(sql, ps -> ps.setString(2, name), this::mapUserResult);
	}


	public CustomDeleteResponse delete(long userId) {
		List<String> statement = new ArrayList<>();
		try {
			jdbcTemplate.update(SqlQueries.DELETE_USER_BY_ID_QUERY, userId);
			statement.add("Data in id " + userId + " is deleted");
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, userId, StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}
		auditlogger.info(iLog, userId , StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsDeletedMessage());
		return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsDeletedMessage(), statement);
	}
	
	public UserResult getUserWithRoleById(long userId) {
	    String sql = "CALL GetUserRolesById(?)";
	    auditlogger.info("inside getUserWithRoleById(long userId)");
	    return jdbcTemplate.query(sql, ps -> ps.setLong(1, userId), this::mapUserResult);
	}

	private UserResult mapUserResult(ResultSet rs) throws SQLException {
	    UserResult user = new UserResult();
	    auditlogger.info("inside mapUserResult");
	    
	  while(rs.next()) {
	    	 auditlogger.info("inside resultset while");
	    	 
	        user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
			user.setName(rs.getString(VariableDeclaration.NAME));
			user.setEmail(rs.getString(VariableDeclaration.EMAIL));
			auditlogger.info("middle of set");
			user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
			user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
			user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
			user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));

			auditlogger.info("inside while"+user);
	        String rolesJson = rs.getString("roles");
	        auditlogger.info(rolesJson);
	        List<UserResult.Role> roles = convertJsonToRole(rolesJson);
	        user.setRole(roles);
	    }
	    auditlogger.info(iLog,"User Id" ,StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsDisplayedMessage());
	    return user;
	}

	
	private List<UserResult.Role> convertJsonToRole(String rolesJson) {
	    if (rolesJson != null) {
	        try {
	        	 auditlogger.info(iLog, "Role" ,StatusCodes.SUCCESS.getStatusCode(),
		     				messageservice.getDetailsDisplayedMessage());
	            return objectMapper.readValue(rolesJson, new TypeReference<List<UserResult.Role>>() {});
	        } catch (JsonProcessingException exceptions) {
	            List<String> list = new ArrayList<>();
	            list.add(exceptions.getMessage());
	            auditlogger.warn(wLog, "Role" ,StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	            throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	        }
	    }
	    return null;
	}

	
	public List<UserResult> getAllUserWithRoles() {

		try {

			String sql = "Call GetAllUserRoles()";
			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				UserResult user = new UserResult();
				user.setUserId(rs.getLong(VariableDeclaration.USER_ID));
				user.setName(rs.getString(VariableDeclaration.NAME));
				user.setEmail(rs.getString(VariableDeclaration.EMAIL));
				user.setAccountNonExpired(rs.getBoolean(VariableDeclaration.ACCOUNTNONEXPIRED));
				user.setAccountNonLocked(rs.getBoolean(VariableDeclaration.ACCOUNTNONLOCKED));
				user.setCredentialsNonExpired(rs.getBoolean(VariableDeclaration.CREDENTIALSNONEXPIRED));
				user.setEnabled(rs.getBoolean(VariableDeclaration.ENABLED));
				
				String roleJson = rs.getString("roles");
				List<UserResult.Role> roles = null;
				if (roleJson != null) {
					try {
						roles = objectMapper.readValue(roleJson,
								new TypeReference<List<UserResult.Role>>() {
								});
					}

					catch (JsonProcessingException exceptions) {
						List<String> list = new ArrayList<>();
						list.add(exceptions.getMessage());
						auditlogger.warn(wLog, user.getUserId() ,StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
						throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
					}

				}
				user.setRole(roles);

				auditlogger.info(iLog, user.getUserId() ,StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsDisplayedMessage(),"Employee DAO Layer");
				return user;
			});
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, "All employees" ,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getJsonParseExceptionMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}

}