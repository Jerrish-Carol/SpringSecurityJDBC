package com.isteer.springsecurity.entities;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.isteer.springsecurity.entities.UserResult.Role;
import com.isteer.springsecurity.controller.HomeController;
import com.isteer.springsecurity.dao.UserDaoImpl;

import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;



@NoArgsConstructor
public class UserPrincipal implements UserDetails , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserDaoImpl uDAO;
	
	private static final Logger auditlogger = LoggerFactory.getLogger(UserPrincipal.class);
	
	public User user;
	
	public UserResult userResult;
	
	public UserPrincipal(User user) {
		this.user=user;
	}

	public UserPrincipal(User user, UserDaoImpl uDAO) {
        this.user = user;
        this.uDAO = uDAO;
        userResult = uDAO.getUserWithRoleByEmail(user.getEmail());
    }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : userResult.getRole()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        }
        return authorities;
    }

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return userResult.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserPrincipal(UserResult user2, UserDaoImpl uDAO2) {
		this.userResult = user2;
        this.uDAO = uDAO2;
        auditlogger.info("user email" + uDAO2.getUserWithRoleByEmail(userResult.getEmail()));
        userResult = uDAO2.getUserWithRoleByEmail(userResult.getEmail());
	}
}

