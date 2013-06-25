package org.baldeapi.v1.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
		
		apiKey = md5(apiKey);
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), "apikeys");
		DBObject apiKeyObject = dao.findOne(BasicDBObjectBuilder.start("_id", apiKey).get());
		
		User user = new User();
		
		if (apiKeyObject != null) {
			user.addRole(Role.GRANTED);
		}
		
		request.setSecurityContext(new ApiSecurityContext(user));
		
		return request;
		
	}
	
	 public String md5(String senha){
		if (senha == null) {
			return null;
		}
        String sen = "";  
        MessageDigest md = null;  
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {
        }  
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
        sen = hash.toString(16);              
        return sen;  
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
