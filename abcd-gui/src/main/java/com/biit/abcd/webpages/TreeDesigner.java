package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.persistence.dao.IFormDao;
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
import com.vaadin.server.VaadinServlet;

public class TreeDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;
	private static final String DEFAULT_ANSWER_TECHNICAL_NAME = "Answer";
	private static final String DEFAULT_QUESTION_TECHNICAL_NAME = "Question";
	private static final String DEFAULT_GROUP_TECHNICAL_NAME = "Group";
	private static final String DEFAULT_CATEGORY_NAME = "Category";
	private FormTreeTable formTreeTable;
	private Form form;
	private TreeTableUpperMenu upperMenu;

	private IFormDao formDao;

	public TreeDesigner() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		this.upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
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

	/**
	 * Adds a new category into the UI and the Form object.
	 */
	public void addCategory() {
		if (getForm() != null) {
			Category newCategory = new Category();
			newCategory.setLabel(DEFAULT_CATEGORY_NAME + (form.getChildren().size() + 1));
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
	}

	/**
	 * Adds a Category to the UI. The parent always will be the form.
	 * 
	 * @param category
	 */
	private void addCategoryToUI(Category category) {
		if (formTreeTable.getValue() != null) {
			Category selectedCategory = formTreeTable.getValue().getCategory();
			if (selectedCategory != null) {
				TreeObject getLastElementOfCategory = selectedCategory.getLastElement();
				formTreeTable.addItemAfter(getLastElementOfCategory, category, form);
			} else {
				formTreeTable.addItem(category, form);
			}
		} else {
			formTreeTable.addItem(category, form);
		}
	}

	/**
	 * Adds a new group into the UI and the Form object.
	 */
	public void addGroup() {
		if (getForm() != null) {
			Group newGroup = new Group();
			try {
				if (formTreeTable.getValue() != null) {
					Category selectedCategory = formTreeTable.getValue().getCategory();
					if (selectedCategory != null) {
						newGroup.setTechnicalName(DEFAULT_GROUP_TECHNICAL_NAME
								+ (selectedCategory.getChildren().size() + 1));
						addElementToUI(newGroup, selectedCategory);
						selectedCategory.addChild(newGroup);
					}
				}
			} catch (NotValidChildException e) {
				// Not possible.
			}
		}
	}

	/**
	 * Adds a new question into the UI and the Form object.
	 */
	public void addQuestion() {
		if (getForm() != null) {
			Question newQuestion = new Question();
			try {
				if (formTreeTable.getValue() != null) {
					TreeObject parent = null;
					if (formTreeTable.getValue() instanceof Category || formTreeTable.getValue() instanceof Group) {
						parent = formTreeTable.getValue();
						// If selected a question, we consider the same that selecting the question's parent.
					} else if (formTreeTable.getValue() instanceof Question) {
						parent = formTreeTable.getValue().getParent();
					} else if (formTreeTable.getValue() instanceof Answer) {
						parent = formTreeTable.getValue().getParent().getParent();
					}
					if (parent != null) {
						newQuestion.setTechnicalName(DEFAULT_QUESTION_TECHNICAL_NAME
								+ (parent.getChildren().size() + 1));
						addElementToUI(newQuestion, parent);
						parent.addChild(newQuestion);
					}
				}
			} catch (NotValidChildException e) {
				// Not possible.
			}
		}
	}

	/**
	 * Adds a new answer into the UI and the Form object.
	 */
	public void addAnswer() {
		if (getForm() != null) {
			Answer newAnswer = new Answer();
			try {
				if (formTreeTable.getValue() != null) {
					TreeObject parent = null;
					if (formTreeTable.getValue() instanceof Question) {
						parent = formTreeTable.getValue();
						// If selected an answer, we consider the same that selecting the question.
					} else if (formTreeTable.getValue() instanceof Answer) {
						parent = formTreeTable.getValue().getParent();
					}
					if (parent != null) {
						newAnswer.setTechnicalName(DEFAULT_ANSWER_TECHNICAL_NAME + (parent.getChildren().size() + 1));
						addElementToUI(newAnswer, parent);
						parent.addChild(newAnswer);
					}
				}
			} catch (NotValidChildException e) {
				// Not possible.
			}
		}
	}

	/**
	 * Adds an element in the tree using the parent as root.
	 * 
	 * @param child
	 * @param parent
	 */
	private void addElementToUI(TreeObject child, TreeObject parent) {
		if (formTreeTable.getValue() != null) {
			TreeObject lastElement = parent.getLastElement();
			formTreeTable.addItemAfter(lastElement, child, parent);
		}
	}

	public void save() {
		if (getForm() != null) {
			formDao.makePersistent(getForm());
		}
	}

}
