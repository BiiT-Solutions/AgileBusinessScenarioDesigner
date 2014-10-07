package com.biit.abcd.webpages.elements.decisiontable;

import java.util.List;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.ExpressionEditorTabComponent;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.form.TreeObject;

public class QuestionEditorComponent extends ExpressionEditorTabComponent {

	private static final long serialVersionUID = -3303171393725653154L;
	private QuestionTabFormVariablesLayout questionFormTab;

	// private GenericElementTabFormVariablesLayout genericFormTab;

	public QuestionEditorComponent(ExpressionValueCustomVariable expression) {
		super();
		initTabs();
		getRootLayout().addComponent(getTabSheet());
		getRootLayout().setExpandRatio(getTabSheet(), 1.f);
		setCompositionRoot(getRootLayout());
		setSelected(expression.getReference());
	}

	public void setSelected(TreeObject expression) {
		if (questionFormTab != null) {
			questionFormTab.setValue(expression);
		}
	}

	public void setSelected(ExpressionValueCustomVariable customVariable) {
		if (questionFormTab != null) {
			questionFormTab.setValue(customVariable);
		}
	}

	public void initTabs() {
		questionFormTab = new QuestionTabFormVariablesLayout();
		setTab(questionFormTab, "", ThemeIcon.EXPRESSION_EDITOR_TAB_FORM_VARIABLES.getThemeResource());
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

	public void clearSelection() {
		questionFormTab.clearSelection();
	}

}