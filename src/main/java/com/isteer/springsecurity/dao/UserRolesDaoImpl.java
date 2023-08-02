package com.isteer.springsecurity.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.entities.UserRole;
import com.isteer.springsecurity.exception.SqlSyntaxException;
import com.isteer.springsecurity.sqlqueries.SqlQueries;
import com.isteer.springsecurity.statuscode.StatusCodes;

@Repository
public class UserRolesDaoImpl implements UserRolesDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageService messageservice;
	
	private static final Logger auditlogger = LogManager.getLogger(UserRolesDaoImpl.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: User DAO Layer";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: User DAO Layer";


	@Override
	public void save( List<Long> roleIds, long userId) {
		try {
		        for (Long roleId : roleIds) {
		        	auditlogger.info(roleId);
		            jdbcTemplate.update(SqlQueries.INSERT_USER_ROLES_QUERY, userId, roleId);
		        }
			
		
			} catch (DataAccessException exception) {
		        List<String> exceptions = new ArrayList<>(); // Initialize the exceptions list
		        exceptions.add(exception.getMessage());
		        auditlogger.warn(wLog, "Role provided", StatusCodes.BAD_REQUEST.getStatusCode(),
		                messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		        throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
		                messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		    }
	}
}
