package com.isteer.springsecurity.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.springsecurity.exception.SqlSyntaxException;
import com.isteer.springsecurity.sqlqueries.SqlQueries;
import com.isteer.springsecurity.statuscode.StatusCodes;
import com.isteer.springbootsecurity.entities.VariableDeclaration;
import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.response.CustomRolePostResponse;

@Repository
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageService messageservice;
	
	private static final Logger auditlogger = LogManager.getLogger(RoleDaoImpl.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: User DAO Layer";
	
	private static String iLog = "Id: {0} Status Code: {} Message: {} Layer: User DAO Layer";

	
	public List<String> validateRole(User user) {
		List<String> exception = new ArrayList<>();

		List<Role> roles = user.getRoles();

		for (int i = 0; i < roles.size(); i++) {
			Role role = roles.get(i);

			if (role.getRoleId()!= 0) {
				exception.add("	Role Id is required for role : " + (i + 1));
			}

			if (role.getRoleName().equals("")) {
				exception.add("Role Details is required for role :" + (i + 1));
			}
		}

		return exception;
	}

	@Override
	public List<Role> getAll() {
		return jdbcTemplate.query(SqlQueries.GET_ROLES_QUERY, new ResultSetExtractor<List<Role>>() {

			public List<Role> extractData(ResultSet rs) throws SQLException {
				List<Role> roles = new ArrayList<>();

				try {

					while (rs.next()) {
						Role role = new Role();
						role.setRoleId(rs.getLong(VariableDeclaration.ROLE_ID));
						role.setRoleName(rs.getString(VariableDeclaration.ROLE_NAME));
						roles.add(role);

					}
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					auditlogger.warn(wLog, "All Addresses" ,StatusCodes.BAD_REQUEST.getStatusCode(),
							messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
							messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
				}
				auditlogger.info(iLog, "All Addresses",StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsDisplayedMessage());
				return roles;
			}

		});

	}

	@Override
	public 	List<Role> getById(long roleId) {
		try {

			return jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new ResultSetExtractor<List<Role>>() {

				public List<Role> extractData(ResultSet rs) throws SQLException {
					List<Role> roles = new ArrayList<>();

					while (rs.next()) {
						Role role = new Role();
						role.setRoleId(rs.getLong(VariableDeclaration.ROLE_ID));
						role.setRoleName(rs.getString(VariableDeclaration.ROLE_NAME));
						roles.add(role);

					}

					auditlogger.info(iLog, roleId ,StatusCodes.SUCCESS.getStatusCode(),
							messageservice.getDetailsDisplayedMessage());
					return roles;
				}

			}, roleId);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog,roleId,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}
	

	@Override
	public List<Role> save(List<Role> roles) {
		try {

			jdbcTemplate.batchUpdate(SqlQueries.INSERT_ROLE_QUERY, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Role role = roles.get(i);
					ps.setString(1, role.getRoleName());
					role.setRoleId(jdbcTemplate.queryForObject(SqlQueries.GET_ROLEID_BY_NAME_QUERY,Long.class,new Object[]{role.getRoleName()}));
					auditlogger.info(role.getRoleId());
					
				}

				@Override
				public int getBatchSize() {
					return roles.size();
				}

			});
			auditlogger.info(iLog, "Role provided" ,StatusCodes.SUCCESS.getStatusCode(),
					messageservice.getDetailsSavedMessage());
			return roles;
	
		} catch (DataAccessException exception) {
	        List<String> exceptions = new ArrayList<>(); // Initialize the exceptions list
	        exceptions.add(exception.getMessage());
	        auditlogger.warn(wLog, "Role provided", StatusCodes.BAD_REQUEST.getStatusCode(),
	                messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
	        throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
	                messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
	    }
	}

	@Override
	public CustomRolePostResponse update(List<Role> roles, long roleId) {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.UPDATE_ADDRESS_BY_ID_QUERY, new BatchPreparedStatementSetter() {

			
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Role role = roles.get(i);
					System.out.println(role);
					ps.setLong(1, role.getRoleId());
					ps.setString(2, role.getRoleName());

				}

				@Override
				public int getBatchSize() {

					return roles.size();
				}

			});
			auditlogger.info(iLog, roleId ,StatusCodes.SUCCESS.getStatusCode(),
					messageservice.getDetailsUpdatedMessage());
			return null;
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, roleId ,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}

	@Override
	public CustomDeleteResponse delete(long roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validateRole(List<Role> role) {
		// TODO Auto-generated method stub
		return null;
	}

	

}



