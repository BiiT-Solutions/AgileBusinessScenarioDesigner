package com.biit.abcd.persistence.entity;

public enum FormWorkStatus {

	DESIGN(1),

	FINAL_DESIGN(2),

	;

	private int level;

	FormWorkStatus(int level) {
		this.level = level;
	}

	public boolean isMovingForward(FormWorkStatus newStatus) {
		return level < newStatus.level;
	}

	public static FormWorkStatus getFromString(String name) {
		for (FormWorkStatus value : values()) {
			if (value.toString().equals(name)) {
				return value;
			}
		}
		return null;
	}
}
