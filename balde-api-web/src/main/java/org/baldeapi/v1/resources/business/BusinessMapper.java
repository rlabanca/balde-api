package org.baldeapi.v1.resources.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.baldeapi.v1.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mongodb.DBObject;

public class BusinessMapper {

	private static final Properties properties;
	private static final Logger log = LoggerFactory.getLogger(BusinessMapper.class);
	
	private User user;
	
	static {
		properties = new Properties();
		ClassLoader loader = BusinessMapper.class.getClassLoader();           
		InputStream stream = loader.getResourceAsStream("resource_business.properties");
		if (stream != null) {
			try {
				properties.load(stream);
			} catch (IOException e) {
				log.error("Error loading resource_business.properties", e);
			}
		}
	}
	
	public static BusinessMapper getInstance(String resource, User user) {
		
		BusinessMapper mapper = new BusinessMapper();
		
		if (properties.containsKey(resource)) {
			
			String className = (String) properties.get(resource);
			
			try {
				Class<?> klass = Class.forName(className);
				mapper = (BusinessMapper) klass.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Can't instantiate", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Can't access", e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Class not found", e);
			}
			
		}
		
		mapper.setUser(user);
		
		return mapper;
		
	}
	
	private void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void applyRules(DBObject object) {
		
	}
	
}
