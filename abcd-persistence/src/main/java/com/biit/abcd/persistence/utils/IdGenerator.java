package com.biit.abcd.persistence.utils;

import java.util.UUID;

public class IdGenerator {
	public static String createId() {
		UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString();
	}
}
