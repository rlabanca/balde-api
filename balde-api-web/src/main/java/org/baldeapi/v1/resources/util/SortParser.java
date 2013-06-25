package org.baldeapi.v1.resources.util;

import java.util.List;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class SortParser {

	public static SortParser getInstance(String resource) {
		return new SortParser();
	}
	
	public DBObject parse(List<String> sortFields) {
		
		if (sortFields == null || sortFields.size() == 0) {
			return null;
		}
		
		return (DBObject) JSON.parse(sortFields.get(0));
		
	}
	
}
