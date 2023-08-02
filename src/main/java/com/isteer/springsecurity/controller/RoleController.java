package com.isteer.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.dao.RoleDao;
import com.isteer.springsecurity.dao.RoleDaoImpl;
import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.response.CustomRolePostResponse;
import com.isteer.springsecurity.sqlqueries.SqlQueries;
import com.isteer.springsecurity.statuscode.StatusCodes;
import com.isteer.springsecurity.exception.ConstraintException;
import com.isteer.springsecurity.exception.DetailsNotFoundException;
import com.isteer.springsecurity.exception.DetailsNotProvidedException;
import com.isteer.springsecurity.exception.SqlSyntaxException;
import com.isteer.springsecurity.exception.BadSqlSyntaxException;

	@RestController
	public class RoleController {
		
		private static final Logger auditlogger = LogManager.getLogger(RoleController.class);
		
		private static String wLog = "Id: {0} Status Code: {1} Message: {2} Exception: {3} Layer: {4}";
		
		private static String iLog = "Id: {0} Status Code: {1} Message: {2} Layer:{3}";
		
		@Autowired
		private MessageService messageservice;
		
		@Autowired
		private RoleDaoImpl rDAO;

		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		@GetMapping("/role")
		public ResponseEntity<List<Role>> getAllRoles(){
			auditlogger.info(iLog,"All details",StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsDisplayedMessage(),"Role Controller Layer");
			return new ResponseEntity<>(rDAO.getAll(),HttpStatus.OK);
			
		}
		
		@GetMapping("/role/{role_id}")
		public ResponseEntity<List<Role>> getRoleById(@PathVariable long roleId) {
			if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
				List<String> exception = new ArrayList<>();
				exception.add("The details are not present for the role id " + roleId);
				auditlogger.warn(wLog, roleId,StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), exception,"Role Controller Layer ");

				throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), exception);
			} else {
				auditlogger.info(iLog,roleId ,StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsDisplayedMessage(),"Role Controller Layer ");
				return new ResponseEntity<>(rDAO.getById(roleId), HttpStatus.OK);
			}

		}
		
		@PostMapping("/role")
		public ResponseEntity<List<Role>> saveRole(@RequestBody List<Role> role){
			List<String> exceptions = rDAO.validateRole(role);
			if (exceptions == null) {
				auditlogger.info(iLog,"New Entity",StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsSavedMessage(), "Role Controller Layer");
				return new ResponseEntity<>(rDAO.save(role),HttpStatus.CREATED);
			}
			else {
				auditlogger.warn(wLog,"New Entity",StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(),exceptions ,"Role Controller Layer");
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getConstraintsInvalidMessage(), exceptions);
			}
			
		}
		
		@PutMapping("/role/{role_id}")
		public ResponseEntity<CustomRolePostResponse> update(@RequestBody List<Role> role, @PathVariable long roleId) {
			
			if (rDAO.getById(roleId) != null) {

				List<String> exceptions = rDAO.validateRole(role);

				if (exceptions.isEmpty()) {
					auditlogger.info(iLog,roleId,StatusCodes.SUCCESS.getStatusCode(),messageservice.getFieldValidatedMessage(), "Role Controller Layer");
					return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.OK);
				} else {
					auditlogger.warn(wLog,roleId,StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(), exceptions ,"Role Controller Layer");
					throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(), exceptions);
				}
			} else {
				auditlogger.warn(wLog,roleId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNoContentToUpdateMessage(),"Id is not present" ,"Role Controller Layer");
				return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.NOT_FOUND);
			}

		}
		
		@DeleteMapping("/role/{role_id}")
		public ResponseEntity<CustomDeleteResponse> deleteById(@PathVariable long roleId) {
			
			if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
				
				List<String> exception = new ArrayList<>();
				exception.add("Not data present to delete");
				auditlogger.warn(wLog,roleId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNoContentToDeleteMessage(), exception ,"Role Controller Layer");
				throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNoContentToDeleteMessage(), exception);
			
			}
			else {
				auditlogger.warn(wLog,roleId ,StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsDeletedMessage(),"Role Controller Layer");
				return new ResponseEntity<>(rDAO.delete(roleId),HttpStatus.OK);
			}
			
			
			
		}
}
