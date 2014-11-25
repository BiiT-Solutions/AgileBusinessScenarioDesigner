package com.biit.abcd.core.drools;

import com.biit.abcd.persistence.entity.Form;

public class DroolsHelper {

	private Form form;

	public DroolsHelper(Form form){
		setForm(form);
	}
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
