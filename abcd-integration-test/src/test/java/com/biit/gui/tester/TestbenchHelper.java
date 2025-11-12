package com.biit.gui.tester;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import org.testng.Assert;

import com.vaadin.testbench.elements.NotificationElement;

public class TestbenchHelper {
	
	private static final String NOTIFICATION_TYPE_HUMANIZED = "humanized";
	private static final String NOTIFICATION_TYPE_WARNING = "warning";
	private static final String NOTIFICATION_TYPE_ERROR = "error";

	public static void checkNotificationIsError(NotificationElement notification) {
		Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_ERROR);
		notification.close();
	}

	public static void checkNotificationIsWarning(NotificationElement notification) {
		Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_WARNING);
		notification.close();
	}
	
	public static void checkNotificationIsHumanized(NotificationElement notification) {
		Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_HUMANIZED);
		notification.close();
	}
	
}
