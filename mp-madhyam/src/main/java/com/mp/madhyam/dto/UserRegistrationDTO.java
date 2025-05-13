package com.mp.madhyam.dto;

import java.util.Set;

public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private Set<Long> roleIds;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}
    
    
}
