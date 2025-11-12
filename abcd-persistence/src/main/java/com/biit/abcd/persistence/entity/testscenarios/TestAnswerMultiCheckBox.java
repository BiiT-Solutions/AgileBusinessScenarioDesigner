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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines radio button values.
 *
 */
@Entity
@Table(name = "test_answer_multi_checkbox")
public class TestAnswerMultiCheckBox extends TestAnswer {
	private static final long serialVersionUID = -3255342701991988332L;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "test_answer_multi_checkbox_values", joinColumns=@JoinColumn(name = "test_answer_multi_checkbox", referencedColumnName = "id"))
	@Column(name="multi_check_box_value")
	private Set<String> multiCheckBoxValue;

	public TestAnswerMultiCheckBox() {
		super();
		multiCheckBoxValue = new HashSet<String>();
	}

	@Override
	public Set<String> getValue() {
		return multiCheckBoxValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof Set<?>)) {
				throw new NotValidAnswerValue("Expected Set<String> object in '" + value + "'");
			}
			setValue((Set<String>) value);
		} else {
			multiCheckBoxValue = null;
		}
	}

	public void setValue(Set<String> value) {
		this.multiCheckBoxValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerMultiCheckBox) {
			super.copyBasicInfo(object);
			TestAnswerMultiCheckBox testAnswerMultiCheckBox = (TestAnswerMultiCheckBox) object;
			multiCheckBoxValue = testAnswerMultiCheckBox.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of TestAnswerMultiCheckBox.");
		}
	}
}
