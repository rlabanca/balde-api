package org.baldeapi.v1.resources.util;

import org.baldeapi.v1.representation.ErrorRepresentationV1;
import org.baldeapi.v1.representation.ErrorType;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;

import com.mongodb.DBObject;

public class FilterValidator {

	public static FilterValidator getInstance(String resource) {
		return new FilterValidator();
	}
	
	public FilterValidation validate(DBObject filter) {
		return new FilterValidation();
	}
	
	public static class FilterValidation {
		
		private ErrorsRepresentationV1 errors;
		
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
		
	}
	
}
