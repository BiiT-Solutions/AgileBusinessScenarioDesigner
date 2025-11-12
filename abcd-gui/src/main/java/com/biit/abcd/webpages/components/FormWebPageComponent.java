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

import com.biit.abcd.ApplicationFrame;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Pages that has a form definition and can access to other pages that also have
 * a form.
 */
public abstract class FormWebPageComponent extends SecuredWebPageComponent {
	private static final long serialVersionUID = 5215781045989004097L;
	private AbstractOrderedLayout workingAreaLayout;
	private BottomMenu bottomMenu;
	private UpperMenu upperMenu;

	public FormWebPageComponent() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		setSizeFull();
		setAsOneWindowWithBottomMenu();
	}

	public void updateButtons(boolean enableFormButtons) {
		bottomMenu.updateButtons(enableFormButtons);
	}

	public void setAsOneWindowWithBottomMenu() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		setCompositionRoot(getRootLayout());
		setSizeFull();

		Panel mainPanel = new Panel();
		getRootLayout().addComponent(mainPanel);
		getRootLayout().setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);
		getRootLayout().setExpandRatio(mainPanel, 1);
		mainPanel.setSizeFull();

		workingAreaLayout = new VerticalLayout();
		workingAreaLayout.setMargin(false);
		workingAreaLayout.setSpacing(false);
		workingAreaLayout.setSizeFull();

		mainPanel.setContent(workingAreaLayout);
		mainPanel.setSizeFull();

		setButtonMenu();
	}

	public void setUpperMenu(UpperMenu upperMenu) {
		if (this.upperMenu != null) {
			this.getRootLayout().removeComponent(this.upperMenu);
		}
		this.upperMenu = upperMenu;

		// Hide logout button.
		if (upperMenu instanceof UpperMenu) {
			if (((ApplicationFrame) UI.getCurrent()).getUserEmail() != null
					&& ((ApplicationFrame) UI.getCurrent()).getPassword() != null) {
				((UpperMenu) upperMenu).hideLogoutButton(true);
			}
		}

		this.getRootLayout().addComponent(upperMenu, 0);
		getRootLayout().setComponentAlignment(upperMenu, Alignment.BOTTOM_CENTER);
	}

	private void setButtonMenu() {
		bottomMenu = new BottomMenu();
		getRootLayout().addComponent(bottomMenu);
		getRootLayout().setComponentAlignment(bottomMenu, Alignment.BOTTOM_CENTER);
	}

	/**
	 * The Working Area Layout is the layout where the page includes its own
	 * components.
	 *
	 * @return The layout
	 */
	public AbstractOrderedLayout getWorkingAreaLayout() {
		return workingAreaLayout;
	}

	public BottomMenu getBottomMenu() {
		return bottomMenu;
	}
}
