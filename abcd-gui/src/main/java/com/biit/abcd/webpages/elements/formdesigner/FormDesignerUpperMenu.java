package com.biit.abcd.webpages.elements.formdesigner;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.form.entity.TreeObject;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

public class FormDesignerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -4712688788270327039L;
	private IconButton saveButton, newCategoryButton, newQuestionButton, newGroupButton, newAnswerButton,
			newSubanswerButton, moveUpButton, moveDownButton, removeButton, moveButton, finish;

	public FormDesignerUpperMenu() {
		super();
		defineMenu();
		disableNotAllowedButtons();
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

		// Add new Subanswer
		newSubanswerButton = new IconButton(LanguageCodes.TREE_DESIGNER_SUBANSWER_ADD,
				ThemeIcon.TREE_DESIGNER_ADD_SUBANSWER, LanguageCodes.BOTTOM_MENU_FORM_MANAGER);
		addIconButton(newSubanswerButton);

		// Move to
		moveButton = new IconButton(LanguageCodes.MENU_MOVE_TO, ThemeIcon.MOVE_TO, LanguageCodes.MENU_MOVE_TO);
		addIconButton(moveButton);

		// Move up.
		moveUpButton = new IconButton(LanguageCodes.MENU_MOVE_UP, ThemeIcon.MOVE_UP, LanguageCodes.MENU_MOVE_UP);
		addIconButton(moveUpButton);

		// Move down.
		moveDownButton = new IconButton(LanguageCodes.MENU_MOVE_DOWN, ThemeIcon.MOVE_DOWN, LanguageCodes.MENU_MOVE_DOWN);
		addIconButton(moveDownButton);

		// Remove
		removeButton = new IconButton(LanguageCodes.TREE_DESIGNER_ELEMENT_REMOVE, ThemeIcon.DELETE,
				LanguageCodes.TREE_DESIGNER_ELEMENT_REMOVE);

		addIconButton(removeButton);

		finish = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcon.FORM_FINISH,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);
		finish.setEnabled(UserSessionHandler.getFormController().getForm().getStatus().equals(FormWorkStatus.DESIGN)
				&& AbcdAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
						UserSessionHandler.getFormController().getForm().getOrganizationId(),
						AbcdActivity.FORM_STATUS_UPGRADE));

		addIconButton(finish);
	}

	public void setEnabledButtons(TreeObject selectedObject) {
		if (selectedObject == null) {
			newCategoryButton.setEnabled(false);
			newGroupButton.setEnabled(false);
			newQuestionButton.setEnabled(false);
			newAnswerButton.setEnabled(false);
			removeButton.setEnabled(false);
			newSubanswerButton.setEnabled(false);
		} else if (selectedObject instanceof Form) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(false);
			newQuestionButton.setEnabled(false);
			newAnswerButton.setEnabled(false);
			removeButton.setEnabled(false);
			newSubanswerButton.setEnabled(false);
		} else if (selectedObject instanceof Category) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			removeButton.setEnabled(true);
			newAnswerButton.setEnabled(false);
			newSubanswerButton.setEnabled(false);
		} else if (selectedObject instanceof Group) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			newAnswerButton.setEnabled(false);
			removeButton.setEnabled(true);
			newSubanswerButton.setEnabled(false);
		} else if (selectedObject instanceof Question) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			if (((Question) selectedObject).getAnswerType().equals(AnswerType.INPUT)) {
				newAnswerButton.setEnabled(false);
			} else {
				newAnswerButton.setEnabled(true);
			}
			newSubanswerButton.setEnabled(false);
			removeButton.setEnabled(true);
		} else if (selectedObject instanceof Answer) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(true);
			newQuestionButton.setEnabled(true);
			removeButton.setEnabled(true);
			newAnswerButton.setEnabled(true);
			newSubanswerButton.setEnabled(true);
		} 

		// Disable buttons that user has no permissions to use.
		disableNotAllowedButtons();
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(saveButton)) {
			saveButton.addClickListener(listener);
		}
	}

	public void addNewCategoryButtonButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newCategoryButton)) {
			newCategoryButton.addClickListener(listener);
		}
	}

	public void addNewQuestionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newQuestionButton)) {
			newQuestionButton.addClickListener(listener);
		}
	}

	public void addNewGroupButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newGroupButton)) {
			newGroupButton.addClickListener(listener);
		}
	}

	public void addNewAnswerButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newAnswerButton)) {
			newAnswerButton.addClickListener(listener);
		}
	}

	public void addNewSubanswerButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newSubanswerButton)) {
			newSubanswerButton.addClickListener(listener);
		}
	}

	public void addMoveUpButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(moveUpButton)) {
			moveUpButton.addClickListener(listener);
		}
	}

	public void addMoveDownButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(moveDownButton)) {
			moveDownButton.addClickListener(listener);
		}
	}

	public void addRemoveButtonButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeButton)) {
			removeButton.addClickListener(listener);
		}
	}

	public void addMoveButtonListener(ClickListener listener) {
		if (!getDisabledButtons().contains(moveButton)) {
			moveButton.addClickListener(listener);
		}
	}

	private void disableNotAllowedButtons() {
		// Disable buttons that user has no permissions to use.
		for (Button button : getDisabledButtons()) {
			button.setEnabled(false);
		}
	}

	public void addFinishListener(ClickListener clickListener) {
		finish.addClickListener(clickListener);
	}

	@Override
	public Set<Button> getSecuredButtons() {
		// All except save form.
		Set<Button> securedButtons = new HashSet<Button>(getButtons());
		return securedButtons;
	}
}
