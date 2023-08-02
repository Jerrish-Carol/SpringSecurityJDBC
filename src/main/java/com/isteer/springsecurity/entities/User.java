package com.isteer.springsecurity.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class User {

	private long userId;

	private String name;

	private String email;

	private String password;

	private boolean isAccountNonExpired;

	private boolean isAccountNonLocked;

	private boolean isCredentialsNonExpired;

	private boolean isEnabled;
	
	private List<Role> role = new ArrayList<>();
	
	public List<Role> getRoles() {
		return role;
	}
	
	public List<Role> setRoles(){
		return role;
		
	}


	
}
