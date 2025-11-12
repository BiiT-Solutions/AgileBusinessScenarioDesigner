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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalButtonGroup extends CustomComponent {

	private static final long serialVersionUID = 4862986305501412362L;
	private static String CLASSNAME = "v-horizontal-button-group";
	protected HorizontalLayout rootLayout;
	private String size;
	private boolean contractIcons;
	private Set<Button> buttons;

	private IAbcdFormAuthorizationService securityService;

	public HorizontalButtonGroup() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
		buttons = new HashSet<>();
		initHorizontalButtonGroup();
		setIconSizeWithAttachListener();
	}

	protected void initHorizontalButtonGroup() {
		setStyleName(CLASSNAME);

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(false);
		setCompositionRoot(rootLayout);
		setSizeFull();

		contractIcons = false;
	}

	protected void setIconSizeWithAttachListener() {
		addAttachListener(new AttachListener() {
			private static final long serialVersionUID = -2513076537414804598L;

			@Override
			public void attach(AttachEvent event) {
				setIconSize();
			}
		});
	}

	public void addIconButton(IconButton button) {
		buttons.add(button);
		rootLayout.addComponent(button);
		button.setSizeFull();
	}

	public void setContractIcons(boolean contractIcons) {
		this.contractIcons = contractIcons;
		this.size = null;
		rootLayout.setWidth(null);
	}

	public void setContractIcons(boolean contractIcons, String size) {
		this.contractIcons = contractIcons;
		this.size = size;
		rootLayout.setWidth(null);
	}

	private void setIconSize() {
		Iterator<Component> itr = rootLayout.iterator();
		if (contractIcons) {
			rootLayout.setWidth(null);
		}

		while (itr.hasNext()) {
			Component component = itr.next();
			rootLayout.setExpandRatio(component, 0.0f);
			if (contractIcons) {
				component.setWidth(size);
			} else {
				component.setWidth("100%");
			}
		}

		markAsDirtyRecursive();
	}

	public Set<Button> getButtons() {
		return buttons;
	}

	protected IAbcdFormAuthorizationService getSecurityService() {
		return securityService;
	}

}
