package com.isteer.springsecurity.dao;

import java.util.List;

import com.isteer.springsecurity.entities.Role;
import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.response.CustomDeleteResponse;
import com.isteer.springsecurity.response.CustomRolePostResponse;

public interface RoleDao {

	List<Role> getAll();

	CustomDeleteResponse delete(long roleId);

	List<String> validateRole(List<Role> role);

	List<Role> getById(long roleId);

	List<Role> save(List<Role> role);

	CustomRolePostResponse update(List<Role> role, long roleId);

	//List<Role> save(List<Role> roles, long userId);


}
