package com.biit.abcd.webpages.elements.form.designer.validators;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.entity.TreeObject;
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
			//If we check from subanswer, check from question level. 
			if(parent instanceof Answer){
				parent = parent.getParent();
			}
			
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(treeObject)) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(
							ServerTranslate.translate(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME));
				}
				if(child instanceof Answer){
					//Answers and subanswer share restriction. 
					for (TreeObject subanswer : child.getChildren()) {
						if (subanswer.equals(treeObject)) {
							// If this child is treeObject, ignore.
							continue;
						}
						if (subanswer.getName().equals(newName)) {
							throw new InvalidValueException(
									ServerTranslate.translate(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME));
						}
					}
				}
			}

		}

	}

}
