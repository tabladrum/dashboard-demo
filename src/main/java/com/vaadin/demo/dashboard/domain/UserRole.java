package com.vaadin.demo.dashboard.domain;

public class UserRole {
	public RoleType getRole() {
		return role;
	}
	public void setRoleCode(RoleType role) {
		this.role = role;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	private RoleType role;
	private String roleDescription;

}
