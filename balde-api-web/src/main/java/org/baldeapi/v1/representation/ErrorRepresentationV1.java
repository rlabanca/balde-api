package org.baldeapi.v1.representation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="error")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="error")
public class ErrorRepresentationV1 {

	private ErrorType name;
	
	private String message;
	
	public ErrorRepresentationV1() {
	}
	
	public ErrorRepresentationV1(ErrorType name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public ErrorType getName() {
		return name;
	}

	public void setName(ErrorType name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
}
