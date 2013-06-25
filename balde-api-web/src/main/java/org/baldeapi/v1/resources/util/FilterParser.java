package org.baldeapi.v1.resources.util;

import java.util.List;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class FilterParser {

	public static FilterParser getInstance(String resource) {
		return new FilterParser();
	}
	
	public DBObject parse(List<String> filters) {
		if (filters == null || filters.size() == 0) {
			return null;
		}
		
		return (DBObject) JSON.parse(filters.get(0));
	}
	
	
}
