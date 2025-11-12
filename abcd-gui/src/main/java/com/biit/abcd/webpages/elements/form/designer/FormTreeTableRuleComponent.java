package com.biit.abcd.webpages.elements.form.designer;

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

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.DroolsRuleEditor;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class FormTreeTableRuleComponent extends CustomComponent implements LayoutClickNotifier {
	private static final long serialVersionUID = -1481676401269063469L;
	private static final String classname = "v-form-tree-table-rule-component";

	private CssLayout rootLayout;

	public FormTreeTableRuleComponent() {
		setStyleName(classname);

		rootLayout = new CssLayout();
		rootLayout.setWidth(null);
		rootLayout.setImmediate(true);
		setWidth(null);
		setImmediate(true);

		setCompositionRoot(rootLayout);
	}

	public void addRuleReference(final Rule rule) {
		IconButton button = new IconButton(ThemeIcon.DROOLS_RULE_EDITOR_PAGE, IconSize.SMALL, (String) null);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1201742020857735883L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.DROOLS_RULE_EDITOR);
				DroolsRuleEditor ruleEditor = (DroolsRuleEditor) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
				ruleEditor.selectComponent(rule);
			}
		});
		rootLayout.addComponent(button);
		Label label = new Label(rule.getName(), ContentMode.HTML);
		label.setWidth(null);
		rootLayout.addComponent(label);
	}

	@Override
	public void addLayoutClickListener(LayoutClickListener listener) {
		rootLayout.addLayoutClickListener(listener);
	}

	@Override
	@Deprecated
	public void addListener(LayoutClickListener listener) {
		addLayoutClickListener(listener);
	}

	@Override
	public void removeLayoutClickListener(LayoutClickListener listener) {
		rootLayout.removeLayoutClickListener(listener);
	}

	@Override
	@Deprecated
	public void removeListener(LayoutClickListener listener) {
		removeLayoutClickListener(listener);
	}
}
