package com.biit.abcd.webpages.elements.form.designer.validators;

import java.util.HashSet;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Validator;

/**
 * Two subanswers children of different answers in the same question cannot have
 * the same name.<br>
 * Also a subanswer cannot have the same name as an answer
 * 
 */
public class ValidatorDuplicateSubAnswerNameInSameQuestion implements Validator {
	private static final long serialVersionUID = -6253757334392693487L;

	private TreeObject treeObject;
	private HashSet<String> answerSubAnswerSet = null;

	public ValidatorDuplicateSubAnswerNameInSameQuestion(TreeObject treeObject) {
		this.treeObject = treeObject;
		answerSubAnswerSet = new HashSet<String>();
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = treeObject.getParent();
		if (parent != null) {
			if (parent instanceof Answer) {
				// We are in a subAnswer
				TreeObject grandParent = parent.getParent();
				if (grandParent != null) {
					answerSubAnswerSet.clear();
					// Look for the uncles of the subanswer
					for (TreeObject uncle : grandParent.getChildren()) {
						if (uncle.equals(parent)) {
							// If this uncle is the subanswer parent, ignore
							// brothers (Brothers checked in other validator)
							answerSubAnswerSet.add(uncle.getName());
							continue;
						} else {
							answerSubAnswerSet.add(uncle.getName());
							for (TreeObject cousin : uncle.getChildren()) {
								answerSubAnswerSet.add(cousin.getName());
							}
						}
					}
					if (answerSubAnswerSet.contains(newName)) {
						throw new InvalidValueException(
								ServerTranslate.translate(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME));
					}
				}
			}
		}
	}

}
