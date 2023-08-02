package com.isteer.springsecurity.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import com.isteer.springsecurity.exception.DetailsNotFoundException;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.sqlqueries.SqlQueries;
import com.isteer.springsecurity.statuscode.StatusCodes;
import com.isteer.springsecurity.MessageService;
import com.isteer.springsecurity.dao.UserDaoImpl;
import com.isteer.springsecurity.dao.UserRolesDaoImpl;
import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserResult;
import com.isteer.springsecurity.entities.UserRole;
import com.isteer.springsecurity.response.CustomPostResponse;
import com.isteer.springsecurity.service.UserService;

@RestController
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final Logger auditlogger = LoggerFactory.getLogger(HomeController.class);
	
	private static final String ILOG = "Succes: {} Status Code : {} Message : {}";
	
	private static final String WLOG = "Failure: {} Status Code : {} Message : {}";
	
	@Autowired
	private MessageService messageservice;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDaoImpl uDAO;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//http://localhost:8081/Users

	@GetMapping("/Admin")
	public String admin() {
		return "admin";
	}

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }


	@PostMapping("/create-user") //save
	public ResponseEntity<CustomPostResponse> createUser(@RequestBody User user) {
		return new ResponseEntity<>( userService.createUser(user), HttpStatus.CREATED);

	}

	@GetMapping("/Current")
	public String getLoggedInUser(Principal principal) {
		return principal.getName();
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserResult>> getUser() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK) ;
	}
	
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResult> getUserById(@PathVariable long userId) {
		return new ResponseEntity<>(uDAO.getUserWithRoleById(userId), HttpStatus.OK) ;
	}
	
	
	@PutMapping("/users/{userId}")
	public ResponseEntity<CustomPostResponse> updateUser(@RequestBody User user ,@PathVariable long userId) throws SQLException {
		return new ResponseEntity<>(userService.updateUsers(user, userId), HttpStatus.OK) ;
	}
	
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable long userId) throws SQLException {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, userId) == 0) {

			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			auditlogger.warn(WLOG , userId , StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageservice.getNoContentToDeleteMessage());
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNoContentToDeleteMessage(), exception);

		} else {
			auditlogger.info("Data is deleted for ID : " + userId + " Status Code :"+StatusCodes.SUCCESS.getStatusCode()+" Mesage :"+ messageservice.getDetailsUpdatedMessage());
			return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
		}

	}

}
