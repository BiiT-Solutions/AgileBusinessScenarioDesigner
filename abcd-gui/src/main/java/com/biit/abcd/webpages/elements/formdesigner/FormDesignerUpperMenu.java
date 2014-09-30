package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.form.TreeObject;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

public class FormDesignerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -4712688788270327039L;
	private IconButton saveButton, newCategoryButton, newQuestionButton, newGroupButton, newAnswerButton, moveUpButton,
			moveDownButton, removeButton, moveButton;

	public FormDesignerUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		// Save
		saveButton = new IconButton(LanguageCodes.MENU_SAVE, ThemeIcon.FORM_SAVE, LanguageCodes.MENU_SAVE);
		addIconButton(saveButton);

		// Add new Category
		newCategoryButton = new IconButton(LanguageCodes.TREE_DESIGNER_CATEGORY_ADD,
				ThemeIcon.TREE_DESIGNER_ADD_CATEGORY, LanguageCodes.BOTTOM_MENU_FORM_MANAGER);
		addIconButton(newCategoryButton);

		// Add new Group
		newGroupButton = new IconButton(LanguageCodes.TREE_DESIGNER_GROUP_ADD, ThemeIcon.TREE_DESIGNER_ADD_GROUP,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER);
		addIconButton(newGroupButton);

		// Add new Question
		newQuestionButton = new IconButton(LanguageCodes.TREE_DESIGNER_QUESTION_ADD,
				ThemeIcon.TREE_DESIGNER_ADD_QUESTION, LanguageCodes.BOTTOM_MENU_FORM_MANAGER);
		addIconButton(newQuestionButton);

		// Add new Answer
		newAnswerButton = new IconButton(LanguageCodes.TREE_DESIGNER_ANSWER_ADD, ThemeIcon.TREE_DESIGNER_ADD_ANSWER,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER);
		addIconButton(newAnswerButton);

		// Move up.
		moveUpButton = new IconButton(LanguageCodes.MENU_MOVE_UP, ThemeIcon.MOVE_UP, LanguageCodes.MENU_MOVE_UP);
		addIconButton(moveUpButton);

		// Move down.
		moveDownButton = new IconButton(LanguageCodes.MENU_MOVE_DOWN, ThemeIcon.MOVE_DOWN,
				LanguageCodes.MENU_MOVE_DOWN);
		addIconButton(moveDownButton);
		
		//Move to
		moveButton = new IconButton(LanguageCodes.MENU_MOVE_TO, ThemeIcon.MOVE_TO,
				LanguageCodes.MENU_MOVE_TO);
		addIconButton(moveButton);

		// Remove
		removeButton = new IconButton(LanguageCodes.TREE_DESIGNER_ELEMENT_REMOVE, ThemeIcon.DELETE,
				LanguageCodes.TREE_DESIGNER_ELEMENT_REMOVE);
		addIconButton(removeButton);
	}

	public void setEnabledButtons(TreeObject selectedObject) {
		if (selectedObject instanceof Form) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(false);
			newQuestionButton.setEnabled(false);
			newAnswerButton.setEnabled(false);
		}
		if (selectedObject instanceof Category) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			newAnswerButton.setEnabled(false);
		}
		if (selectedObject instanceof Group) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			newAnswerButton.setEnabled(false);
		}
		if (selectedObject instanceof Question) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			if (((Question) selectedObject).getAnswerType().equals(AnswerType.INPUT)) {
				newAnswerButton.setEnabled(false);
			} else {
				newAnswerButton.setEnabled(true);
			}
		}
		if (selectedObject instanceof Answer) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
		}
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addNewCategoryButtonButtonClickListener(Button.ClickListener listener) {
		newCategoryButton.addClickListener(listener);
	}

	public void addNewQuestionButtonClickListener(Button.ClickListener listener) {
		newQuestionButton.addClickListener(listener);
	}

	public void addNewGroupButtonClickListener(Button.ClickListener listener) {
		newGroupButton.addClickListener(listener);
	}

	public void addNewAnswerButtonClickListener(Button.ClickListener listener) {
		newAnswerButton.addClickListener(listener);
	}

	public void addMoveUpButtonClickListener(Button.ClickListener listener) {
		moveUpButton.addClickListener(listener);
	}

	public void addMoveDownButtonClickListener(Button.ClickListener listener) {
		moveDownButton.addClickListener(listener);
	}

	public void addRemoveButtonButtonClickListener(Button.ClickListener listener) {
		removeButton.addClickListener(listener);
	}
	
	public void addMoveButtonListener(ClickListener listener) {
		moveButton.addClickListener(listener);
	}

}
