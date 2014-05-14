package com.biit.abcd.webpages.components;

public class UpperMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "120px";

	protected UpperMenu() {
		super();
		defineUpperMenu();
		setContractIcons(true, BUTTON_WIDTH);
	}

	private void defineUpperMenu() {
		setWidth("100%");
		setHeight("70px");
		setStyleName("upper-menu v-horizontal-button-group");
	}
}
