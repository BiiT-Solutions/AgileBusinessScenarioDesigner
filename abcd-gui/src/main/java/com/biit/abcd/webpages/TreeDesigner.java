package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.treetable.FormTreeTable;
import com.biit.abcd.webpages.elements.treetable.TreeTableUpperMenu;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class TreeDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;
	private FormTreeTable formTreeTable;
	private Form form;
	private TreeTableUpperMenu upperMenu;

	public TreeDesigner() {

	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		this.upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		getWorkingAreaLayout().addComponent(formTreeTable);
		formTreeTable.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5598877051361847210L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu(formTreeTable.getValue());
			}
		});
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		formTreeTable.setForm(form);
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void updateUpperMenu(TreeObject selectedObject) {
		upperMenu.setEnabledButtons(selectedObject);
	}

	private TreeTableUpperMenu createUpperMenu() {
		TreeTableUpperMenu upperMenu = new TreeTableUpperMenu(this);
		return upperMenu;
	}

	public void addCategory() {
		Category newCategory = new Category();
		try {
			if (formTreeTable.getValue() != null) {
				Category selectedCategory = formTreeTable.getValue().getCategory();
				if (selectedCategory == null) {
					getForm().addChild(newCategory);
				} else {
					int index = getForm().getChildren().indexOf(selectedCategory);
					if (index >= 0) {
						getForm().addChild(index, newCategory);
					} else {
						getForm().addChild(newCategory);
					}
				}
			} else {
				getForm().addChild(newCategory);
			}
		} catch (NotValidChildException e) {
			// Not possible.
		}
		addCategoryToUI(newCategory);
	}

	public void addCategoryToUI(Category category) {
		if (formTreeTable.getValue() != null) {
			Category selectedCategory = formTreeTable.getValue().getCategory();
			TreeObject getLastElementOfCategory = selectedCategory.getLastElement();
			formTreeTable.addItemAfter(FormTreeTable.getItemId(getLastElementOfCategory), category);
		} else {
			formTreeTable.addItem(category);
		}
	}

	public void addGroup() {
		Group newGroup = new Group();
		try {
			if (formTreeTable.getValue() != null) {
				Category selectedCategory = formTreeTable.getValue().getCategory();
				if (selectedCategory != null) {
					selectedCategory.addChild(newGroup);
					addGroupToUI(newGroup);
				}
			}
		} catch (NotValidChildException e) {
			// Not possible.
		}
	}

	public void addGroupToUI(Group group) {
		if (formTreeTable.getValue() != null) {
			Category selectedCategory = formTreeTable.getValue().getCategory();
			TreeObject getLastElementOfCategory = selectedCategory.getLastElement();
			formTreeTable.addItemAfter(FormTreeTable.getItemId(getLastElementOfCategory), group);
		}
	}

	public void addQuestion() {

	}

	public void addQuestionToUI(Question question) {

	}

	public void addAnswer() {

	}

	public void addAnswerToUI(Answer answer) {

	}

}
