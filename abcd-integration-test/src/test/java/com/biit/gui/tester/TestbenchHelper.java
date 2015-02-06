package com.biit.gui.tester;

import org.testng.Assert;

import com.vaadin.testbench.elements.NotificationElement;

public class TestbenchHelper {
	
	private static final String NOTIFICATION_TYPE_HUMANIZED = "humanized";
	private static final String NOTIFICATION_TYPE_WARNING = "warning";
	private static final String NOTIFICATION_TYPE_ERROR = "error";

	public static void checkNotificationIsError(NotificationElement notification) {
		Assert.assertEquals(NOTIFICATION_TYPE_ERROR, notification.getType());
	}

	public static void checkNotificationIsWarning(NotificationElement notification) {
		Assert.assertEquals(NOTIFICATION_TYPE_WARNING, notification.getType());
	}
	
	public static void checkNotificationIsHumanized(NotificationElement notification) {
		Assert.assertEquals(NOTIFICATION_TYPE_HUMANIZED, notification.getType());
	}
	
}
