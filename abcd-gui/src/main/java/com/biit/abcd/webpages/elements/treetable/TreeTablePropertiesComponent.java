package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class TreeTablePropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = -6946016320623141040L;

	public TreeTablePropertiesComponent() {
		super();
		registerPropertiesComponent(new FormProperties());
		registerPropertiesComponent(new CategoryProperties());
		registerPropertiesComponent(new GroupProperties());
		registerPropertiesComponent(new QuestionProperties());
		registerPropertiesComponent(new AnswerProperties());
	}

}
