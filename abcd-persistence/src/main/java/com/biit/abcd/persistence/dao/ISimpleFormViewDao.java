package com.biit.abcd.persistence.dao;

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

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

import java.util.List;

public interface ISimpleFormViewDao {

    int getRowCount() throws UnexpectedDatabaseException;

    List<SimpleFormView> getAll();

    List<SimpleFormView> getSimpleFormViewByLabelAndOrganization(String label, Long organizationId);

    SimpleFormViewWithContent getSimpleFormViewByLabelAndVersionAndOrganization(String label, Integer version, Long organizationId);

    List<SimpleFormView> getSimpleFormViewByOrganization(Long organizationId);

    SimpleFormViewWithContent get(Long id);

}
