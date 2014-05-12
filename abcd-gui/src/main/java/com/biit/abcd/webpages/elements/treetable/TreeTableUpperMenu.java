package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.TreeDesigner;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class TreeTableUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -4712688788270327039L;
	private static String iconWidth = "150px";
	private IconButton newCategoryButton, newQuestionButton, newGroupButton, newAnswerButton;
	private TreeDesigner parent;	

	public TreeTableUpperMenu(TreeDesigner parent) {
		super();
		this.parent = parent;
		setContractIcons(true,iconWidth);
		defineMenu();
	}

	private void defineMenu() {
		// Add new Category
		newCategoryButton = new IconButton(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER),ThemeIcons.TREE_DESIGNER_ADD_CATEGORY.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 4094066808071081684L;

					@Override
					public void buttonClick(ClickEvent event) {
						parent.addCategory();
					}
				});
		addIconButton(newCategoryButton);

		// Add new Group
		newGroupButton = new IconButton(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER),ThemeIcons.TREE_DESIGNER_ADD_GROUP.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -3422118691290819294L;

					@Override
					public void buttonClick(ClickEvent event) {
						parent.addGroup();
					}
				});
		addIconButton(newGroupButton);

		// Add new Question
		newQuestionButton = new IconButton(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER),ThemeIcons.TREE_DESIGNER_ADD_QUESTION.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -3581383072543137712L;

					@Override
					public void buttonClick(ClickEvent event) {
						parent.addQuestion();
					}
				});
		addIconButton(newQuestionButton);

		// Add new Answer
		newAnswerButton = new IconButton(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER),ThemeIcons.TREE_DESIGNER_ADD_ANSWER.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 5128294955249902659L;

					@Override
					public void buttonClick(ClickEvent event) {
						parent.addAnswer();
					}
				});
		addIconButton(newAnswerButton);
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
			newGroupButton.setEnabled(false);
			newQuestionButton.setEnabled(true);
			newAnswerButton.setEnabled(true);
		}
		if (selectedObject instanceof Answer) {
			newCategoryButton.setEnabled(true);
			newGroupButton.setEnabled(false);
			newQuestionButton.setEnabled(false);
			newAnswerButton.setEnabled(true);
		}
	}

}
