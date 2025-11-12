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

import com.biit.abcd.persistence.dao.IUserTokenDao;
import com.biit.abcd.persistence.entity.UserToken;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserTokenDao extends AnnotatedGenericDao<UserToken, Long> implements IUserTokenDao {

    public UserTokenDao() {
        super(UserToken.class);
    }

    @Override
    public void makeTransient(UserToken userToken) throws ElementCannotBeRemovedException {
        super.makeTransient(userToken);
    }

    @Override
    public UserToken makePersistent(UserToken userToken) {
        return super.makePersistent(userToken);
    }

    @Override
    public UserToken merge(UserToken userToken) {
        return super.merge(userToken);
    }

    @Override
    public UserToken get(Long id) {
        return super.get(id);
    }

    @Override
    public int getRowCount() {
        return super.getRowCount();
    }

    @Override
    public List<UserToken> getAll() {
        return super.getAll();
    }

    @Override
    public void evictAllCache() {
        super.evictAllCache();
    }

    @Override
    public EntityManager getEntityManager() {
        return super.getEntityManager();
    }

    @Override
    public void evictCache() {
        super.evictCache();
    }
}
