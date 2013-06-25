package org.baldeapi.v1.resources.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProjectionMapper {

	private static final Properties properties;
	private static final Logger log = LoggerFactory.getLogger(ProjectionMapper.class);
	
	static {
		properties = new Properties();
		ClassLoader loader = ProjectionMapper.class.getClassLoader();           
		InputStream stream = loader.getResourceAsStream("resource_projection.properties");
		if (stream != null) {
			try {
				properties.load(stream);
			} catch (IOException e) {
				log.error("Error loading resource_projection.properties", e);
			}
		}
	}
	
	public static ProjectionMapper getInstance(String resource) {
		
		ProjectionMapper mapper = new ProjectionMapper();
		
		if (properties.containsKey(resource)) {
			
			String className = (String) properties.get(resource);
			
			try {
				Class<?> klass = Class.forName(className);
				mapper = (ProjectionMapper) klass.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Can't instantiate", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Can't access", e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Class not found", e);
			}
			
		}
		
		return mapper;
		
	}
	
	public DBObject project() {
		
		BasicDBObject projection = new BasicDBObject();
		
		return projection;
		
	}
	
}
