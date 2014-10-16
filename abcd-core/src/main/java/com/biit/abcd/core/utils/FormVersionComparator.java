package com.biit.abcd.core.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.core.utils.exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.core.utils.exceptions.FormNotEqualsException;
import com.biit.abcd.core.utils.exceptions.GroupNotEqualsException;
import com.biit.abcd.core.utils.exceptions.QuestionNotEqualsException;
import com.biit.abcd.core.utils.exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.core.utils.exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;

/**
 * Compares two forms. Must be equals (but with different IDs and ComparationIds).
 */
public class FormVersionComparator {
	private static Set<StorableObject> alreadyComparedForm1Element = new HashSet<>();

	private static void compare(StorableObject object1, StorableObject object2) throws StorableObjectNotEqualsException {
		if ((object1.getId() != null && object2.getId() == null)
				|| (object1.getId() == null && object2.getId() != null)) {
			throw new StorableObjectNotEqualsException("Storable objects has different ids state: " + object1.getId()
					+ " <-> " + object2.getId());
		}
		if (object1.getId() != null && object2.getId() != null && object1.getId().equals(object2.getId())) {
			throw new StorableObjectNotEqualsException("Storable objects has same id!: '" + object1.getId() + "'");
		}
		if (object1.getComparationId() == object2.getComparationId()) {
			throw new StorableObjectNotEqualsException("Storable objects is the same objects!");
		}
	}

	private static void compare(Group object1, Group object2) throws GroupNotEqualsException {
		if (object1.isRepeatable() != object2.isRepeatable()) {
			throw new GroupNotEqualsException("Check repeatable options of groups '" + object1 + "' and '" + object2
					+ "'.");
		}
	}

	private static void compare(Question object1, Question object2) throws QuestionNotEqualsException {
		if (object1.getAnswerFormat() != object2.getAnswerFormat()) {
			throw new QuestionNotEqualsException("Answer formats are different between questions '" + object1
					+ "' and '" + object2 + "'.");
		}
		if (object1.getAnswerType() != object2.getAnswerType()) {
			throw new QuestionNotEqualsException("Answer types are different between questions '" + object1 + "' and '"
					+ object2 + "'.");
		}
	}

	private static void compare(CustomVariable object1, CustomVariable object2) throws CustomVariableNotEqualsException {
		if (object1.getForm().getName() != object2.getForm().getName()) {
			throw new CustomVariableNotEqualsException("Parents are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");
		}
		if (object1.getName() != object2.getName()) {
			throw new CustomVariableNotEqualsException("Names are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");

		}
		if (object1.getScope() != object2.getScope()) {
			throw new CustomVariableNotEqualsException("Scopes are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");

		}
		if (object1.getType() != object2.getType()) {
			throw new CustomVariableNotEqualsException("Types are different between custom variables '" + object1
					+ "' and '" + object2 + "'.");
		}
	}

	private static void compare(TreeObject object1, TreeObject object2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException {
		// Already compared because it is a child of a compared element.
		if (alreadyComparedForm1Element.contains(object1)) {
			return;
		}

		if (object1.getName() != object2.getName()) {
			throw new TreeObjectNotEqualsException("Names are different between tree objects '" + object1 + "' and '"
					+ object2 + "'.");
		}
		if (object1.getLabel() != object2.getLabel()) {
			throw new TreeObjectNotEqualsException("Labels are different between tree objects '" + object1 + "' and '"
					+ object2 + "'.");
		}
		compare((StorableObject) object1, (StorableObject) object2);

		// Compare parent.
		if ((object1.getParent() != object2.getParent())
				|| ((object1.getParent() != null && object2.getParent() != null) && (object1.getParent().getName()
						.equals(object2.getParent().getName())))) {
			throw new TreeObjectNotEqualsException("TreeObject '" + object1 + "' compared with '" + object2
					+ "' has different parents.");
		}

		// Compare Children.
		for (int i = 0; i < object1.getChildren().size(); i++) {
			compare(object1.getChildren().get(i), object2.getChildren().get(i));
		}

		// Compare specific data
		if (object1 instanceof Group) {
			compare((Group) object1, (Group) object2);
		} else if (object1 instanceof Question) {
			compare((Question) object1, (Question) object2);
		}
		alreadyComparedForm1Element.add(object1);
	}

	/**
	 * Form1 is the previous version of Form2.
	 * 
	 * @param form1
	 * @param form2
	 * @throws TreeObjectNotEqualsException
	 * @throws StorableObjectNotEqualsException
	 * @throws FormNotEqualsException
	 * @throws QuestionNotEqualsException
	 * @throws GroupNotEqualsException
	 * @throws CustomVariableNotEqualsException 
	 */
	public static void compare(Form form1, Form form2) throws TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, FormNotEqualsException, GroupNotEqualsException,
			QuestionNotEqualsException, CustomVariableNotEqualsException {
		if ((form1 == null || form2 == null) && (form1 != null || form2 != null)) {
			throw new FormNotEqualsException("Obtained form is null");
		}
		if (form1.getChildren().size() != form2.getChildren().size()) {
			throw new FormNotEqualsException("Form has different children size!");
		}
		if (form1.getName() != form2.getName()) {
			throw new FormNotEqualsException("Form has different name!");
		}
		if (form1.getVersion() + 1 != (int) form2.getVersion()) {
			throw new FormNotEqualsException("Form's versions are incorrect!");
		}
		if (form1.getOrganizationId() != form2.getOrganizationId()) {
			throw new FormNotEqualsException("Form's organizations are different!");
		}
		// Previous version ends when the other starts.
		if (form1.getAvailableTo() != form2.getAvailableFrom()) {
			throw new FormNotEqualsException("Form's validTo and ValidFrom are different!");
		}
		// Compare treeObject hierarchy.
		for (int i = 0; i < form2.getChildren().size(); i++) {
			compare(form1.getChildren().get(i), form2.getChildren().get(i));
		}

		// Compare custom variables
		Iterator<CustomVariable> variableIterator1 = form1.getCustomVariables().iterator();
		Iterator<CustomVariable> variableIterator2 = form2.getCustomVariables().iterator();
		do {
			if (variableIterator1.hasNext() != variableIterator2.hasNext()) {
				throw new CustomVariableNotEqualsException("CustomVariables list length differs!");
			}
			compare(variableIterator1.next(), variableIterator2.next());
		} while (variableIterator1.hasNext() || variableIterator2.hasNext());

	}
}
