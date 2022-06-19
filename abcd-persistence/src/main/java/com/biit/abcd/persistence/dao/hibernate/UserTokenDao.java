package com.biit.abcd.persistence.dao.hibernate;

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
