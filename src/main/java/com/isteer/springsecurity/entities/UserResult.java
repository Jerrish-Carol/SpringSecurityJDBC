package com.isteer.springsecurity.entities;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor  
@Getter
@Setter
public class UserResult {
	
	private long userId;

	private String name;

	private String email;

	private boolean isAccountNonExpired;

	private boolean isAccountNonLocked;

	private boolean isCredentialsNonExpired;

	private boolean isEnabled;
	
	private List<Role> role;
	
		@Data
	    @AllArgsConstructor
	    @NoArgsConstructor  
	    @Getter
	    @Setter
	    public static class Role {
			
			private long roleId;
			
			private String roleName;
			
		

	    }

		@Override
		public String toString() {
			return "UserResult [userId=" + userId + ", name=" + name + ", email=" + email + ", isAccountNonExpired="
					+ isAccountNonExpired + ", isAccountNonLocked=" + isAccountNonLocked + ", isCredentialsNonExpired="
					+ isCredentialsNonExpired + ", isEnabled=" + isEnabled + ", role=" + role + "]";
		}
		

}
