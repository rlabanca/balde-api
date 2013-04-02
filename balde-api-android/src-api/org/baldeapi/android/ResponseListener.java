package org.baldeapi.android;


public interface ResponseListener {

	public void onEvent(boolean array, Object object, int statusCode, boolean success);
	
}
