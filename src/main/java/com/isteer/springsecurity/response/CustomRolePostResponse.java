package com.isteer.springsecurity.response;

import java.util.List;

import com.isteer.springsecurity.entities.Role;

public class CustomRolePostResponse {
	
	private List<Role> roles;

	private long statusCode;

	private String message;


	public CustomRolePostResponse(long statusCode, String message, List<Role> roles) {
		
			super();
			this.statusCode = statusCode;
			this.message = message;
			this.roles = roles;
		}
	}


