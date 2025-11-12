package com.biit.abcd.webpages;

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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.WebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Page to show an error. 
 */
public class ErrorPage extends WebPageComponent {
	private static final long serialVersionUID = -587681959593342489L;
	private static final String TEXT_WIDTH = "600px";
	private static final String FULL = "100%";
	private static final String IMAGE_LAYOUT_HEIGHT = "300px";
	private static final String IMAGE_WIDTH = "80px";
	private static final String IMAGE_HEIGHT = "80px";
	private VerticalLayout rootLayout;

	private VerticalLayout messageLayout;
	private Label label;
	private Image image;

	public ErrorPage() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();

		messageLayout = new VerticalLayout();
		messageLayout.setWidth(TEXT_WIDTH);
		messageLayout.setHeight(null);
		messageLayout.setSpacing(true);

		image = new Image();
		image.setWidth(IMAGE_WIDTH);
		image.setHeight(IMAGE_HEIGHT);

		VerticalLayout imageLayout = new VerticalLayout();
		imageLayout.setWidth(FULL);
		imageLayout.setHeight(IMAGE_LAYOUT_HEIGHT);
		imageLayout.addComponent(image);
		imageLayout.setComponentAlignment(image, Alignment.BOTTOM_LEFT);
		imageLayout.setMargin(new MarginInfo(false, false, true, false));

		label = new Label();

		messageLayout.addComponent(imageLayout);
		messageLayout.addComponent(label);

		rootLayout.addComponent(messageLayout);
		rootLayout.setComponentAlignment(messageLayout, Alignment.TOP_CENTER);

		setLabelContent(ServerTranslate.translate(LanguageCodes.PAGE_ERROR));
		setImageSource(ThemeIcon.PAGE_ERROR.getThemeResource());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

	public void setLabelContent(String content) {
		label.setValue(content);
	}

	public void setImageSource(ThemeResource resource) {
		image.setSource(resource);
	}

}
