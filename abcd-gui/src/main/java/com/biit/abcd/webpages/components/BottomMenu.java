package com.biit.abcd.webpages.components;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BottomMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 6149788828670200504L;
	private IconButton treeDesignerButton, formVariables, diagramBuilderButton, expressionsEditorButton,
			ruleEditorButton, decisionTableButton;

	private List<IFormSelectedListener> formSelectedListeners;

	protected BottomMenu() {
		super();
		formSelectedListeners = new ArrayList<>();
		defineButtomMenu();
	}

	private void defineButtomMenu() {
		setWidth("100%");
		setHeight("80px");

		// Add Tree Designer button.
		treeDesignerButton = new IconButton(LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, ThemeIcon.TREE_DESIGNER_PAGE,
				LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 6344185231722436896L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.TREE_DESIGNER);
					}
				});
		treeDesignerButton.setEnabled(false);
		addIconButton(treeDesignerButton);

		// Add Form Variables button.
		formVariables = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_VARIABLES, ThemeIcon.FORM_VARIABLES,
				LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -6778800510554818966L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.FORM_VARIABLES);
					}
				});
		formVariables.setEnabled(false);
		addIconButton(formVariables);

		// Add Diagram Builder button.
		diagramBuilderButton = new IconButton(LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER,
				ThemeIcon.DIAGRAM_BUILDER_PAGE, LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER, IconSize.BIG,
				new ClickListener() {
					private static final long serialVersionUID = -6778800510554818966L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.DIAGRAM_BUILDER);
					}
				});
		diagramBuilderButton.setEnabled(false);
		addIconButton(diagramBuilderButton);

		// Add calculus expresion editor.
		expressionsEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR,
				ThemeIcon.EXPRESSION_EDITOR_PAGE, LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR, IconSize.BIG,
				new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.EXPRESSION_EDITOR);
					}
				});
		expressionsEditorButton.setEnabled(false);
		addIconButton(expressionsEditorButton);

		// Add Drools Editor button.
		ruleEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, ThemeIcon.DROOLS_RULE_EDITOR_PAGE,
				LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.DROOLS_RULE_EDITOR);
					}
				});
		ruleEditorButton.setEnabled(false);
		addIconButton(ruleEditorButton);

		decisionTableButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_TABLE_EDITOR, ThemeIcon.TABLE,
				LanguageCodes.BOTTOM_MENU_DROOLS_TABLE_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -2494460723270342409L;

					@Override
					public void buttonClick(ClickEvent event) {
						launchListeners();
						changeView(WebMap.DECISSION_TABLE_EDITOR);
					}
				});
		decisionTableButton.setEnabled(false);
		addIconButton(decisionTableButton);
	}

	private void changeView(WebMap newView) {
		try {
			ApplicationFrame.navigateTo(newView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateButtons(boolean enableFormButtons) {
		if (treeDesignerButton != null) {
			treeDesignerButton.setEnabled(enableFormButtons);
		}
		if (formVariables != null) {
			formVariables.setEnabled(enableFormButtons);
		}
		if (diagramBuilderButton != null) {
			diagramBuilderButton.setEnabled(enableFormButtons);
		}
		if (expressionsEditorButton != null) {
			expressionsEditorButton.setEnabled(enableFormButtons);
		}
		if (ruleEditorButton != null) {
			ruleEditorButton.setEnabled(enableFormButtons);
		}
		if (decisionTableButton != null) {
			decisionTableButton.setEnabled(enableFormButtons);
		}
	}

	private void launchListeners() {
		for (IFormSelectedListener listener : formSelectedListeners) {
			listener.formSelected();
		}
	}

	public void addFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.add(listener);
	}

	public void removeFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.remove(listener);
	}
}
