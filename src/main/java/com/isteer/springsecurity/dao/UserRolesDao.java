package com.isteer.springsecurity.dao;

import java.util.List;


public interface UserRolesDao {
	
	void save(List<Long> roleIds, long userId);

}
