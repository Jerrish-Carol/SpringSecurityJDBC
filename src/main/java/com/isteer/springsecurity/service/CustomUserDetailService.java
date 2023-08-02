package com.isteer.springsecurity.service;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.isteer.springsecurity.dao.UserDaoImpl;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserPrincipal;
import com.isteer.springsecurity.exception.DetailsNotFoundException;


public class CustomUserDetailService extends JdbcUserDetailsManager implements UserDetailsService {

	@Autowired
	private UserDaoImpl uDAO;

	public CustomUserDetailService(DataSource dataSource) {
	        super(dataSource);
	        setUsersByUsernameQuery("SELECT name, email, password FROM tbl_user WHERE email=?");
	        
	    }

	 @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try{
			 User user = uDAO.findByEmail(username);
			 System.out.println(user);
			 if (user.getName() == null) {
		            throw new UsernameNotFoundException("User not found!");
		        }
		        return new UserPrincipal(user, uDAO);
		}
		catch(RuntimeException exception) {
			List<String> list = new ArrayList<>();
			list.add(exception.getMessage());
			System.out.println(exception.getMessage());
			throw new DetailsNotFoundException(0,"details not found",list);
		}
		
	}

}
