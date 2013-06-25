
package org.baldeapi.v1.security;

import java.util.Set;
import java.util.TreeSet;


public class User implements java.security.Principal {
	
	public enum Role {
		GRANTED(401, "Invalid or null api key. Check the X-API-Key header."),
		BPSSOAUTH(401, "Invalid or null Single Sign On Token. Check the X-BP-SSOToken header.");
		
		private String message;
		private int statusCode;
		
		private Role(int statusCode, String message) {
			this.message = message;
			this.statusCode = statusCode;
		}
		
		public String getMessage() {
			return message;
		}
		
		public int getStatusCode() {
			return this.statusCode;
		}
		
	}
	
	private Long id;
	
	private String authProvider;
	
	private String name;
	
	private String apiKey;
	
	private String email;
	
	private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public boolean addRole(Role role) {
		if (roles == null) {
			roles = new TreeSet<User.Role>();
		}
		return roles.add(role);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
