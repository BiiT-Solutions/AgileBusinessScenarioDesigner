package com.biit.abcd.webpages;

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
