package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_answers")
public class Answer extends BaseAnswer {
	private static final long serialVersionUID = -7358559199240262641L;
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(Answer.class));

	public Answer() {
	}

	public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super();
		setName(name);
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		copyBasicInfo(object);
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		CheckDependencies.checkTreeObjectDependencies(this);
	}

	@Override
	public String getSimpleAsciiName() {
		return getName().replaceAll("[^a-zA-Z0-9.]", "");
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDREN;
	}

	/**
	 * Checks if this answer is a subanswer by looking if it has a parent and if it has, if it is an answer.
	 * 
	 * @return
	 */
	public boolean isSubanswer() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return false;
		}
		return true;
	}

	/**
	 * Calculates a unique name of an answer or subanswer in the a question.
	 */
	@Override
	public String getDefaultName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = getDefaultTechnicalName() + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if ((child.getClass() == this.getClass()) && (child.getName() != null) && child.getName().equals(name)) {
					return getDefaultName(parent, startingIndex + 1);
				}
				for (TreeObject subanswer : child.getChildren()) {
					if ((subanswer.getClass() == this.getClass()) && (subanswer.getName() != null)
							&& subanswer.getName().equals(name)) {
						return getDefaultName(parent, startingIndex + 1);
					}
				}
			}
		} else {
			name = getDefaultTechnicalName();
		}
		return name;
	}
}
