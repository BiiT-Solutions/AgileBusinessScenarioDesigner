package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IAnswerDao;
import com.biit.abcd.persistence.entity.Answer;

@Repository
public class AnswerDao extends GenericDao<Answer> implements IAnswerDao {

	public AnswerDao() {
		super(Answer.class);
	}

	@Override
	protected void initializeSets(List<Answer> answers) {
		for (Answer answer : answers) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(answer.getChildren());
		}
	}

}
