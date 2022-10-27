package service;

import entity.Role;

public class RoleService {

	public static boolean hasRole(String role) {
		for(Role s : Contains.account.getRoles()) {
			if(s.getRoleName().equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}
}
