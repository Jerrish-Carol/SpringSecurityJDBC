package com.isteer.springsecurity.dao;

import java.util.List;

import com.isteer.springsecurity.entities.User;

public interface UserDao {

	List<User> findAll();

	User findByEmail(String username);

}
