package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IQuestionDao;
import com.biit.abcd.persistence.entity.Question;

@Repository
public class QuestionDao extends GenericDao<Question> implements IQuestionDao {

	public QuestionDao() {
		super(Question.class);
	}

	@Override
	protected void initializeSets(List<Question> questions) {
		for (Question question : questions) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(question.getChildren());
		}
	}
}
