package org.baldeapi.v1.security;

import javax.ws.rs.ext.Provider;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.persistence.MongoManager;
import org.baldeapi.v1.security.User.Role;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

@Provider
public class ApiKeyValidationFilter implements ContainerRequestFilter, ResourceFilter {
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		String apiKey = request.getHeaderValue("X-API-Key");
		
		apiKey = MD5.make(apiKey);
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), "apikeys");
		DBObject apiKeyObject = dao.findOne(BasicDBObjectBuilder.start("_id", apiKey).get());
		
		User user = new User();
		
		if (apiKeyObject != null) {
			user.addRole(Role.GRANTED);
		}
		
		request.setSecurityContext(new ApiSecurityContext(user));
		
		return request;
		
	}
	
	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}
	
}
