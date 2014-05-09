package com.biit.abcd.webpages.components;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

/**
 * Parent class for all webPages. it extends a CustomComponent. Any child class
 * needs to call {@link CustomComponent#setCompositionRoot(Component)} in its
 * constructor.
 * 
 */
public abstract class WebPageComponent extends CustomComponent implements View {

	private static final long serialVersionUID = -386946981801328161L;

}
