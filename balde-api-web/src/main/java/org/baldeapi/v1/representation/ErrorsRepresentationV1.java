package org.baldeapi.v1.representation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="errors")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="errors")
public class ErrorsRepresentationV1 {

	private List<ErrorRepresentationV1> error;
	
	public static ErrorsRepresentationV1 notFound() {
		ErrorRepresentationV1 error = new ErrorRepresentationV1(ErrorType.NOT_FOUND, "Check your filters");
		ErrorsRepresentationV1 errors = new ErrorsRepresentationV1();
		errors.add(error);
		return errors;
	}

	public ErrorsRepresentationV1() {
	}
	
	public ErrorsRepresentationV1(List<ErrorRepresentationV1> error) {
		super();
		this.error = error;
	}

	public List<ErrorRepresentationV1> getError() {
		return error;
	}

	public void setError(List<ErrorRepresentationV1> error) {
		this.error = error;
	}
		
	public int size() {
		if (error == null) {
			return 0;
		}
		return error.size();
	}
	
	public void add(ErrorRepresentationV1 error) {
		if (this.error == null) {
			this.error = new ArrayList<ErrorRepresentationV1>();
		}
		this.error.add(error);
	}
	
}
