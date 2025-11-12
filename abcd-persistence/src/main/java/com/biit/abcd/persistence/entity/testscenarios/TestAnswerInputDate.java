package com.biit.abcd.persistence.entity.testscenarios;

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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines radio button values.
 *
 */
@Entity
@Table(name = "test_answer_input_date")
public class TestAnswerInputDate extends TestAnswer {
	private static final long serialVersionUID = 4067658245634637534L;

	@Column(name = "date_value")
	private Timestamp dateValue = null;

	public TestAnswerInputDate() {
		super();
	}

	@Override
	public Timestamp getValue() {
		return dateValue;
	}

	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof Timestamp)) {
				if (value instanceof Date) {
					setValue(new Timestamp(((Date) value).getTime()));
				} else {
					throw new NotValidAnswerValue("Expected Timestamp object in '" + value + "'");
				}
			} else {
				setValue((Timestamp) value);
			}
		} else {
			dateValue = null;
		}

	}

	public void setValue(Timestamp value) {
		this.dateValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerInputDate) {
			super.copyBasicInfo(object);
			TestAnswerInputDate testAnswerInputDate = (TestAnswerInputDate) object;
			dateValue = testAnswerInputDate.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TestAnswerInputDate.");
		}
	}
}
