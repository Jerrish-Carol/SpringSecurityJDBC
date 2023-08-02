package com.isteer.springsecurity.sqlqueries;

public class SqlQueries {

	public static final String FIND_BY_EMAIL = "SELECT * FROM tbl_user WHERE email=?";
	public static final String FIND_BY_NAME = "SELECT * FROM tbl_user WHERE name=?";
	public static final String FIND_BY_USER_ID = "SELECT * FROM tbl_user WHERE userId=?";
	public static final String FIND_NECESSSARY_BY_EMAIL = "SELECT email , password FROM tbl_user WHERE email=?";
	public static final String FIND_ALL_USERS = "SELECT * FROM tbl_user";
	public static final String INSERT_ROLE_QUERY = "INSERT INTO tbl_role(roleName) VALUES (?)";
	public static final String INSERT_USER_QUERY = "INSERT INTO tbl_user(userId, name, email, password, accountNonLocked, accountNonExpired, credentialsNonExpired, enabled) VALUES (? ,? ,?, ?, ? ,?, ?, ?)";
	public static final String CHECK_EMAIL_IS_PRESENT_QUERY = "SELECT EXISTS(SELECT * from tbl_user WHERE email=?)";
	public static final String CHECK_ID_IS_PRESENT_QUERY = "SELECT EXISTS(SELECT * from tbl_user WHERE userId=?)";
	public static final String DELETE_USER_BY_ID_QUERY="DELETE FROM tbl_user WHERE userId=?";
	public static final String UPDATE_USERS_BY_ID_QUERY = "UPDATE tbl_user SET name =?, email =?, password =?, accountNonLocked =?, accountNonExpired =?, credentialsNonExpired =?, enabled =? WHERE userId = ?";
	public static final String GET_ROLES_BY_ID_QUERY="SELECT * FROM tbl_role WHERE roleId=?";
	public static final String GET_ROLEID_BY_NAME_QUERY="SELECT roleId FROM tbl_role WHERE roleName = ? ORDER BY roleId DESC LIMIT 1";
	
	public static final String INSERT_USER_ROLES_QUERY="INSERT INTO users_roles(userIdForeign, roleIdForeign) VALUES (? ,?)";
	
	public static final String INSERT_EMPLOYEE_QUERY="INSERT INTO tbl_employee(name, dob, gender, email, department, roleId) VALUES (? ,? ,?, ?, ?, ?)";
	public static final String INSERT_ADDRESS_QUERY="INSERT INTO tbl_address(addressId, employeeId,street,city,state,country) VALUES (? ,? ,?, ?, ?, ?)";
	public static final String INSERT_ROLES_QUERY="INSERT INTO tbl_roles(roles, project, billable, hierarchicalLevel, buName, buHead, hrManager) VALUES (? ,? ,?, ?, ?, ?, ?)";
	
	public static final String UPDATE_EMPLOYEES_BY_ID_QUERY="UPDATE tbl_employee SET name = ?, dob = ?, gender = ?, isActive = ?,isAccountLocked = ?, email = ?, department = ?, roleId = ? WHERE employeeId = ?";
	public static final String UPDATE_ADDRESS_BY_ID_QUERY="UPDATE tbl_address SET employeeId=?, street=?, city=?, state=?, country=? WHERE addressId=?";
	public static final String UPDATE_ROLES_BY_ID_QUERY="UPDATE tbl_roles SET roles=?, project=?, billable=?, hierarchicalLevel=?, buName=?, buHead=?, hrManager=? WHERE roleId=?";
	
	public static final String GET_EMPLOYEES_BY_ID_QUERY="SELECT * FROM tbl_employee WHERE employeeId=?";
	public static final String GET_ADDRESS_BY_ID_QUERY="SELECT * FROM tbl_address WHERE employeeId=?";

	
	public static final String DELETE_EMPLOYEES_BY_ID_QUERY="DELETE FROM tbl_employee WHERE employeeId=?";
	public static final String DELETE_ROLES_BY_ID_QUERY="DELETE FROM tbl_roles WHERE roleId=?";
	public static final String DELETE_ADDRESS_BY_ID_QUERY="DELETE FROM tbl_address WHERE employeeId=?";
	
	public static final String GET_EMPLOYEES_QUERY="SELECT * FROM tbl_employee";
	public static final String GET_ADDRESS_QUERY="SELECT * FROM tbl_address";
	public static final String GET_ROLES_QUERY="SELECT * FROM tbl_roles";
	
	public static final String CHECK_ROLE_ID_IS_PRESENT_QUERY="SELECT EXISTS(SELECT * from tbl_roles WHERE roleId=?)";

	public static final String GET_EMPLOYEES_AND_ROLE_QUERY="SELECT * FROM tbl_employee FULL OUTER JOIN tbl_roles ON tbl_employees.employeeId =  tbl_roles.roleId";
	public static final String GET_EMPID_AND_ROLEID="SELECT * FROM tbl_employee FULL OUTER JOIN tbl_roles ON tbl_employees.employeeId =  tbl_roles.roleId";
		
	public static final String GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_QUERY="SELECT e.employeeId, e.name, e.dob, e.gender, e.isActive, e.isAccountLocked, e.email, e.department, e.roleId, a.addressId, a.employeeId, a.street, a.city, a.state, a.country, r.roleId, r.role, r.project, r.billable, r.hierarchicalLevel, r.buName, r.buHead, r.hrManager " +
            "FROM tbl_employee e " +
            "LEFT JOIN tbl_address a ON e.employeeId = a.employeeId " +
            "LEFT JOIN tbl_roles r ON e.roleId = r.roleId " +
            "GROUP BY e.employeeId, e.name, e.dob, e.gender, e.isActive, e.isAccountLocked, e.email, e.department, e.roleId, a.addressId, a.employeeId, a.street, a.city, a.state, a.country, r.roleId, r.role, "+
            "r.project, r.billable, r.hierarchicalLevel, r.buName, r.buHead, r.hrManager";
	
	public static final String GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_USING_ID_QUERY="SELECT e.employeeId, e.name, e.dob, e.gender, e.isActive, e.isAccountLocked, e.email, e.department, e.roleId, a.addressId, a.employeeId, a.street, a.city, a.state, a.country, r.roleId, r.role, r.project, r.billable, r.hierarchicalLevel, r.buName, r.buHead, r.hrManager " +
			"FROM tbl_employee e " +
			"LEFT JOIN tbl_address a ON e.employeeId = a.employeeId " +
			"LEFT JOIN tbl_roles r ON e.roleId = r.roleId  WHERE e.id = ? " +
			"GROUP BY e.employeeId, e.name, e.dob, e.gender, e.isActive, e.isAccountLocked, e.email, e.department, e.roleId, a.addressId, a.employeeId, a.street, a.city, a.state, a.country, r.roleId, r.role, "+
			"r.project, r.billable, r.hierarchicalLevel, r.buName, r.buHead, r.hrManager ";
	
	private SqlQueries() {

	}
}


