package com.biit.abcd.webpages.elements.decisiontable;

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

import java.util.List;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.ExpressionEditorTabComponent;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.form.entity.TreeObject;

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
