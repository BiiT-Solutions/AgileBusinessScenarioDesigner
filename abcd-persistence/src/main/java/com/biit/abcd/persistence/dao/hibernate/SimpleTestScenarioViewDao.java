package com.biit.abcd.persistence.dao.hibernate;

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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ISimpleTestScenarioViewDao;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;

@Repository
public class SimpleTestScenarioViewDao implements ISimpleTestScenarioViewDao {

	private Class<SimpleTestScenarioView> type;

	@PersistenceContext(unitName = "abcdPersistenceUnit")
	@Qualifier(value = "abcdManagerFactory")
	private EntityManager entityManager;

	public SimpleTestScenarioViewDao() {
		this.type = SimpleTestScenarioView.class;

	}

	public Class<SimpleTestScenarioView> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleTestScenarioView> getSimpleTestScenariosByFormId(Long formId) {
		Query query = entityManager.createNativeQuery("SELECT tests.id, tests.name, tests.form_id, formsOfTest.form_version FROM test_scenario AS tests JOIN (SELECT t2.id form_id, t2.version form_version FROM tree_forms AS t2 JOIN tree_forms AS t1 ON t1.organization_id=t2.organization_id AND t1.label=t2.label WHERE t1.id= ? ) AS formsOfTest WHERE tests.form_id=formsOfTest.form_id;");
		query.setParameter(1, formId);
		
		List<Object[]> queries = query.getResultList();
		List<SimpleTestScenarioView> testScenarioViews = new ArrayList<>();
		for(Object[] result: queries){
			SimpleTestScenarioView testScenarioView = new SimpleTestScenarioView();
			testScenarioView.setId( ((BigInteger) result[0]).longValue());
			testScenarioView.setName((String) result[1]);
			testScenarioView.setFormId(((BigInteger) result[2]).longValue());
			testScenarioView.setFormVersion((Integer) result[3]);
			testScenarioViews.add(testScenarioView);
		}
		
		return testScenarioViews;
	}
}
