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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.BaseCategory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_category")
@Cacheable(true)
public class TestScenarioCategory extends BaseCategory {
	private static final long serialVersionUID = 1339828980031764729L;
	private static final String DEFAULT_CATEGORY_NAME = "TestScenarioCategory";

	public TestScenarioCategory() {
		super();
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_CATEGORY_NAME;
	}
	
	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// TODO Auto-generated method stub
	}
}
