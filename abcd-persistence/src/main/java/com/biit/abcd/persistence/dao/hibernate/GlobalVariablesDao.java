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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

@Repository
public class GlobalVariablesDao extends AnnotatedGenericDao<GlobalVariable, Long> implements IGlobalVariablesDao {

	public GlobalVariablesDao() {
		super(GlobalVariable.class);
	}

	@Override
	public int getFormNumberUsing(GlobalVariable globalVariable) throws UnexpectedDatabaseException {
		if (globalVariable == null || globalVariable.getId() == null) {
			return 0;
		}

		try {
			Query query = getEntityManager().createQuery("SELECT count(*) FROM ExpressionValueGlobalVariable WHERE globalVariable=:globalVariableId");
			query.setParameter("globalVariableId", globalVariable);
			Long count = (Long) query.getSingleResult();
			return count.intValue();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	public int getFormNumberUsing(Set<GlobalVariable> globalVariables) throws UnexpectedDatabaseException {
		if (globalVariables == null || globalVariables.isEmpty()) {
			return 0;
		}

		List<Long> ids = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariables) {
			if (globalVariable!=null && globalVariable.getId() != null) {
				ids.add(globalVariable.getId());
			}
		}

		try {
			Query query = getEntityManager().createQuery("SELECT count(*) FROM ExpressionValueGlobalVariable WHERE globalVariable IN (:globalVariablesId)");
			query.setParameter("globalVariablesId", globalVariables);
			Long count = (Long) query.getSingleResult();
			return count.intValue();
		} catch (RuntimeException e) {
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}
}
