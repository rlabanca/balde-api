package org.baldeapi.v1.security;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.baldeapi.v1.representation.ErrorRepresentationV1;
import org.baldeapi.v1.representation.ErrorType;
import org.baldeapi.v1.security.User.Role;

public class ApiSecurityContext implements javax.ws.rs.core.SecurityContext {
 
    private final User user;
 
    public ApiSecurityContext(User user) {
        this.user = user;
    }
 
    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
 
    @Override
    public Principal getUserPrincipal() {
        return user;
    }
 
    @Override
    public boolean isSecure() {
        return true;
    }
 
    @Override
    public boolean isUserInRole(String role) {
 
    	boolean allowed;
    	
    	String message = "OK";
    	int statusCode = 200;
    	
        try {
            
        	Role userRole = Role.valueOf(Role.class, role);
        	
        	message = userRole.getMessage();
        	
        	statusCode = userRole.getStatusCode();
        	
            allowed = user.getRoles().contains(userRole);
            
        } catch (Exception e) {
        	
        	allowed = false;
        	
        }
        
        if (!allowed) {
        	
        	ErrorRepresentationV1 error = new ErrorRepresentationV1(ErrorType.PERMISSION_DENIED, message);
    		
            Response denied = Response.status(statusCode).entity(error).build();
            
            throw new WebApplicationException(denied);
        	
        }
        
        return true;
    }
    
}

