package com.biit.abcd.webpages.components;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class DroolsSubmittedFormResultWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -9123887739972604746L;

	public DroolsSubmittedFormResultWindow(ISubmittedForm submittedForm) {
		super();
		setCaption("Submitted form scores");
		setWidth("40%");
		setHeight("40%");
		setClosable(false);
		setModal(true);
		setResizable(false);
		setContent(generateContent(submittedForm));
	}

	private Component generateContent(ISubmittedForm submittedForm) {

		VerticalLayout layout = new VerticalLayout();
		// Create content
		TreeTable resultTreeTable = new TreeTable();
		resultTreeTable.setSizeFull();
		resultTreeTable.setSelectable(true);

		resultTreeTable.addContainerProperty("Name", String.class, "");
		resultTreeTable.addContainerProperty("Score", Double.class, 0.);
		if (submittedForm != null) {
			for (ICategory category : submittedForm.getCategories()) {
				final Object categoryItem = resultTreeTable.addItem(new Object[] { category.getText(),
						((Category) category).getVariableValue("cScore") }, null);
				for (IQuestion question : category.getQuestions()) {
					final Object questionItem = resultTreeTable.addItem(new Object[] { question.getTag(),
							((Question) question).getVariableValue("qScore") }, null);
					resultTreeTable.setParent(questionItem, categoryItem);
					resultTreeTable.setChildrenAllowed(questionItem, false);
				}
			}
		}
		layout.addComponent(resultTreeTable);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}
}
