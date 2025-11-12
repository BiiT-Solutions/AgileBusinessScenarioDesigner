package com.biit.abcd.persistence.entity;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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
