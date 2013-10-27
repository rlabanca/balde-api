package org.baldeapi.v1.resources.util;

public enum Status {
	ACTIVE(1),
	INACTIVE(0);
	
	private final int st;
	private Status(int i) {
		this.st = i;
	}
	
	public int getId() {
		return this.st;
	}
}