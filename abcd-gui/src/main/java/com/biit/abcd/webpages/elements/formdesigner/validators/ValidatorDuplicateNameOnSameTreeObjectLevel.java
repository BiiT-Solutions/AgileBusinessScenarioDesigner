package com.biit.abcd.webpages.elements.formdesigner.validators;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.TreeObject;
import com.vaadin.data.Validator;

public class ValidatorDuplicateNameOnSameTreeObjectLevel implements Validator {
	private static final long serialVersionUID = -6253757334392693487L;

	private TreeObject treeObject;

	public ValidatorDuplicateNameOnSameTreeObjectLevel(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = treeObject.getParent();

		if (parent != null) {
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(treeObject)) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(
							ServerTranslate.translate(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME));
				}
			}

		}

	}

}
