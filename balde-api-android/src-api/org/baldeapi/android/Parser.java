package org.baldeapi.android;

public interface Parser<T> {

	public T parse(String json);
	
}
