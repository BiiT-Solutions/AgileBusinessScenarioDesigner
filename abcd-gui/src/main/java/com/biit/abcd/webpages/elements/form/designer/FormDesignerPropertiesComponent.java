package com.biit.abcd.webpages.elements.form.designer;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class FormDesignerPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = -6946016320623141040L;

	public FormDesignerPropertiesComponent() {
		super();
		registerPropertiesComponent(new FormProperties());
		registerPropertiesComponent(new CategoryProperties());
		registerPropertiesComponent(new GroupProperties());
		registerPropertiesComponent(new QuestionProperties());
		registerPropertiesComponent(new AnswerProperties());
	}

}
