package org.baldeapi.v1.security;

import javax.ws.rs.ext.Provider;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.security.User.Role;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

@Provider
public class UserValidationFilter implements ContainerRequestFilter, ResourceFilter {
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		String authToken = request.getHeaderValue("X-AuthToken");
		
		if (authToken != null) {
			
			GenericDAO dao = new GenericDAO("user");
			
			authToken = MD5.make(authToken);
			
			DBObject filter = BasicDBObjectBuilder.start("tokens.token", authToken).get();
			DBObject dbUser = dao.findOne(filter);
			
			if (dbUser != null) {
			
				User user = (User) request.getUserPrincipal();
				user.addRole(Role.AUTH);
				
				user.setEmail((String)dbUser.get("email"));
				
				ObjectId oid = (ObjectId) dbUser.get("_id");
				user.setId(oid.toString());
				
				user.setName((String)dbUser.get("name"));
				user.setAuthProvider("LASTMINUTE");
				
				user.setGender((String)dbUser.get("gender"));
				
				user.setCpf((String)dbUser.get("cpf"));
				
				user.setPhone((String)dbUser.get("phone"));
				
				request.setSecurityContext(new ApiSecurityContext(user));
			
			}
			
		}
		
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
