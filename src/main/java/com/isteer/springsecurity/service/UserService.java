package com.isteer.springsecurity.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.springbootsecurity.entities.VariableDeclaration;
import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.dao.RoleDaoImpl;
import com.isteer.springsecurity.dao.UserDaoImpl;
import com.isteer.springsecurity.dao.UserRolesDaoImpl;
import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserRole;
import com.isteer.springsecurity.exception.SqlSyntaxException;
import com.isteer.springsecurity.entities.UserResult;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.response.CustomPostResponse;
import com.isteer.springsecurity.sqlqueries.SqlQueries;

@Service
public class UserService {

	@Autowired
	private UserDaoImpl uDAO;
	
	@Autowired
	private RoleDaoImpl rDAO;
	
	@Autowired
	private UserRolesDaoImpl urDAO;

	@Autowired
	private PasswordEncoder passwordencoder;

	@Autowired
	private MessageService messageservice;
	
	private static final Logger auditlogger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public CustomPostResponse createUser(User user) {
	    try {
	        user.setPassword(passwordencoder.encode(user.getPassword()));
	        auditlogger.info(user);

	        // Save the user in the database
	        uDAO.save(user);
	        auditlogger.info(user);

	        // Save the roles associated with the user
	        rDAO.save(user.getRoles());
	        auditlogger.info(user.getRoles());

	        // Execute the INSERT_USER_ROLES_QUERY to insert user roles
	        // Replace the query placeholder with the actual SQL query for inserting user roles.
	       
	        List<Long> roleIds = user.getRoles().stream().map(Role::getRoleId).collect(Collectors.toList());

	        urDAO.save(roleIds, user.getUserId());
	   
	       // jdbcTemplate.update(SqlQueries.INSERT_USER_ROLES_QUERY, user.getUserId(), user.getRoles());
	        
	        return new CustomPostResponse(1, "", uDAO.getUserWithRoleById(user.getUserId()));
	    } catch (DataAccessException exception) {
	        List<String> exceptions = new ArrayList<>();
	        exceptions.add(exception.getMessage());
	        throw new SqlSyntaxException(0, "cannot find by name", exceptions);
	    }
	}

	public UserResult getByEmail(String email) {
		return uDAO.getUserWithRoleByEmail(email);
	}

	public List<UserResult> getUsers(){
		return uDAO.getAllUserWithRoles();
	}

	public CustomPostResponse updateUsers(User user, long userId) throws SQLException {
	   return uDAO.update(user, userId);
	}
	
	public CustomDeleteResponse deleteUser(long userId) throws SQLException {
		   return uDAO.delete(userId);
		}

}