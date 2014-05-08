package com.biit.abcd.webpages.basic;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public abstract class DWebPage extends VerticalLayout implements View {
	private static final long serialVersionUID = 5010733144484212054L;
	protected final static Integer HEADER_HEIGHT = 40;
	protected final static Integer BORDER_MARGIN_SIZE = 20;
	protected final static String HEADER_WIDTH = "98%";

	public abstract void init(ViewChangeEvent event);

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
