package com.biit.abcd.webpages.elements.decisiontable;

import java.util.List;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.TabEditorComponent;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.form.TreeObject;

public class QuestionEditorComponent extends TabEditorComponent {

	private static final long serialVersionUID = -3303171393725653154L;
	private QuestionTabFormVariablesLayout questionFormTab;
//	private GenericElementTabFormVariablesLayout genericFormTab;

	public QuestionEditorComponent(TreeObject treeObject) {
		super();
		initTabs();
		getRootLayout().addComponent(getTabSheet());
		getRootLayout().setExpandRatio(getTabSheet(), 1.f);
		setCompositionRoot(getRootLayout());
	}

	public void initTabs() {
		// First Tab
		questionFormTab = new QuestionTabFormVariablesLayout();
		setTab(questionFormTab, "", ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_VARIABLES.getThemeResource());
		// Second Tab
//		genericFormTab = new GenericElementTabFormVariablesLayout();
//		setTab(new TabFormGenericTreeObjectLayout(), "",
//				ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_GENERIC_VARIABLES.getThemeResource());
		// Third tab
//		setTab(new TabGlobalConstantsLayout(), "", ThemeIcon.EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS.getThemeResource());

	}

	public Object getSelectedObject() {
		List<ExpressionValueCustomVariable> expValCustomVarList = questionFormTab.getValues();
		if ((expValCustomVarList != null) && (expValCustomVarList.size() != 0)) {
			return expValCustomVarList.get(0);
		} else {
			List<TreeObject> treeObjectsList = questionFormTab.getSelectedFormElements();
			if ((treeObjectsList != null) && (treeObjectsList.size() != 0)) {
				return treeObjectsList.get(0);
			}
		}
		return null;
	}

	public Object getSelectedCustomVariable() {
		List<ExpressionValueCustomVariable> expValCustomVarList = questionFormTab.getValues();
		if ((expValCustomVarList != null) && (expValCustomVarList.size() != 0)) {
			return expValCustomVarList.get(0);
		}
		return null;
	}

	public Object getSelectedFormElement() {
		List<TreeObject> treeObjectsList = questionFormTab.getSelectedFormElements();
		if ((treeObjectsList != null) && (treeObjectsList.size() != 0)) {
			return treeObjectsList.get(0);
		}
		return null;
	}

}