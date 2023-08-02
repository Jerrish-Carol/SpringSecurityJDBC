package com.isteer.springsecurity.response;

import com.isteer.springsecurity.entities.User;

import lombok.Data;

@Data
public class CustomGetResponse {

	private User user;

	public CustomGetResponse(User employee) {
		this.user = employee;
	}
}
