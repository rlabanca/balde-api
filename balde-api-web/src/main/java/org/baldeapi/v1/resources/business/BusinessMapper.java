package org.baldeapi.v1.resources.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.baldeapi.v1.representation.ErrorRepresentationV1;
import org.baldeapi.v1.representation.ErrorType;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;
import org.baldeapi.v1.resources.util.Utils;
import org.baldeapi.v1.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBObject;

public class BusinessMapper {

	private static final Properties properties;
	private static final Logger log = LoggerFactory.getLogger(BusinessMapper.class);
	
	private User user;
	private ErrorsRepresentationV1 errors;
	private boolean saveParent;
	private boolean saved;
	private boolean searched;
	private DBObject object;
	private boolean list;
	
	private String buildVersion;
	
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
		return getInstance(resource, user, null);
	}
	
	public static BusinessMapper getInstance(String resource, User user, String buildVersion) {
		
		BusinessMapper mapper = new BusinessMapper();
		
		if (properties.containsKey(resource)) {
			
			String resourceKey = resource;
			if(!Utils.isEmpty(buildVersion)) resourceKey = resource + "_" + buildVersion;
			
			String className = properties.containsKey(resourceKey) 
					? (String) properties.get(resourceKey)
					: (String) properties.get(resource);
			
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
		mapper.setBuildVersion(buildVersion);
		
		return mapper;
		
	}
	
	public String getBuildVersion() {
		return buildVersion;
	}

	private void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	private void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void applyPostRules(DBObject object) {
		
	}
	
	public void applyGetRules(DBObject filter) {
		
	}
	
	public void applyPutRules(DBObject filter, DBObject object) {
		
	}
	
	public void applyDelRules(DBObject filter) {
		
	}
	
	public void applySort(DBObject sort) {
		
	}
	
	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}
	
	public ErrorsRepresentationV1 getErrors() {
		return errors;
	}
	
	public void addError(ErrorType type, String message) {
		if (errors == null) {
			errors = new ErrorsRepresentationV1();
		}
		ErrorRepresentationV1 error = new ErrorRepresentationV1(type, message);
		errors.add(error);
	}
	
	public void addError(ErrorType type) {
		if (errors == null) {
			errors = new ErrorsRepresentationV1();
		}
		ErrorRepresentationV1 error = new ErrorRepresentationV1(type, type.getDefaultMessage());
		errors.add(error);
	}

	public boolean isSaveParent() {
		return saveParent;
	}

	public void setSaveParent(boolean saveParent) {
		this.saveParent = saveParent;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public DBObject getObject() {
		return object;
	}

	public void setObject(DBObject object) {
		this.object = object;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}
	
}
