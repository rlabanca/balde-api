package org.baldeapi.v1.security;

import javax.ws.rs.ext.Provider;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.security.User.Role;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

@Provider
public class BasicAuthValidationFilter implements ContainerRequestFilter, ResourceFilter {
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		String authToken = request.getHeaderValue("Authorization");
		
		if (authToken != null) {
			
			String auth64 = authToken.split(" ")[1];
			
			auth64 = Base64.base64Decode(auth64);
			
			String[] auth = auth64.split(":");
			
			String email = auth[0];
			String password = MD5.make(auth[1]);
			
			GenericDAO dao = new GenericDAO("user");
			
			DBObject filter = BasicDBObjectBuilder.start("email", email)
												  .append("password", password)
												  .get();
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
