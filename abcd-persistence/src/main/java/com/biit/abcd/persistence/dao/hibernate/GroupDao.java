package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGroupDao;
import com.biit.abcd.persistence.entity.Group;

@Repository
public class GroupDao extends GenericDao<Group> implements IGroupDao {

	public GroupDao() {
		super(Group.class);
	}

	@Override
	protected void initializeSets(List<Group> groups) {
		for (Group group : groups) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(group.getChildren());
		}
	}
}
