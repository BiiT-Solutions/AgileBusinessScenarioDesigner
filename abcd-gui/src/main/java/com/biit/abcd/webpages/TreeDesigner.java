package com.biit.abcd.webpages;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.MessageManager;
import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormTreeTable;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.elements.treetable.TreeTablePropertiesComponent;
import com.biit.abcd.webpages.elements.treetable.TreeTableUpperMenu;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;

public class TreeDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;
	private FormTreeTable formTreeTable;
	private TreeTablePropertiesComponent propertiesComponent;
	private Form form;
	private TreeTableUpperMenu upperMenu;
	private TreeTableValueChangeListener treeTableValueChangeListener;

	private IFormDao formDao;

	public TreeDesigner() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		updateButtons(true);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		this.upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		treeTableValueChangeListener = new TreeTableValueChangeListener();

		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
		formTreeTable.addValueChangeListener(treeTableValueChangeListener);

		propertiesComponent = new TreeTablePropertiesComponent();
		propertiesComponent.setSizeFull();
		propertiesComponent.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				formTreeTable.updateItem((TreeObject) element);
			}
		});

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);

		rootLayout.addComponent(formTreeTable);
		rootLayout.addComponent(propertiesComponent);
		rootLayout.setExpandRatio(formTreeTable, 0.75f);
		rootLayout.setExpandRatio(propertiesComponent, 0.25f);

		getWorkingAreaLayout().addComponent(rootLayout);
	}

	protected void updatePropertiesComponent(TreeObject value) {
		propertiesComponent.updatePropertiesComponent(value);
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		// Remove ValueChange listener and re add it after load the entire form.
		// This will remove the unnecesary overhead of calls when loading a
		// form.
		formTreeTable.setValue(null);
		formTreeTable.removeValueChangeListener(treeTableValueChangeListener);
		formTreeTable.setRootElement(form);
		formTreeTable.addValueChangeListener(treeTableValueChangeListener);
		formTreeTable.setValue(form);
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
			setCreator(newCategory);
			try {
				if (formTreeTable.getValue() != null) {
					Category selectedCategory = formTreeTable.getValue().getCategory();
					if (selectedCategory == null) {
						getForm().addChild(newCategory);
					} else {
						int index = getForm().getChildren().indexOf(selectedCategory);
						if (index >= 0) {
							getForm().addChild(index + 1, newCategory);
						} else {
							getForm().addChild(newCategory);
						}
					}
				} else {
					getForm().addChild(newCategory);
				}
				newCategory.setName(newCategory.getDefaultName(form, form.getChildren().size()));
				addCategoryToUI(newCategory);
			} catch (NotValidChildException e) {
				// Not possible.
			}
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
			setCreator(newGroup);
			try {
				if (formTreeTable.getValue() != null) {
					TreeObject container = formTreeTable.getValue().getGroup();
					if (container == null) {
						container = formTreeTable.getValue().getCategory();
					}
					if (container != null) {
						newGroup.setName(newGroup.getDefaultName(container, 1));
						addElementToUI(newGroup, container);
						container.addChild(newGroup);
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
			setCreator(newQuestion);
			try {
				if (formTreeTable.getValue() != null) {
					TreeObject parent = null;
					if (formTreeTable.getValue() instanceof Category || formTreeTable.getValue() instanceof Group) {
						parent = formTreeTable.getValue();
						// If selected a question, we consider the same that
						// selecting the question's parent.
					} else if (formTreeTable.getValue() instanceof Question) {
						parent = formTreeTable.getValue().getParent();
					} else if (formTreeTable.getValue() instanceof Answer) {
						parent = formTreeTable.getValue().getParent().getParent();
					}
					if (parent != null) {
						newQuestion.setName(newQuestion.getDefaultName(parent, 1));
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
			setCreator(newAnswer);
			try {
				if (formTreeTable.getValue() != null) {
					TreeObject parent = null;
					if (formTreeTable.getValue() instanceof Question) {
						parent = formTreeTable.getValue();
						// If selected an answer, we consider the same that
						// selecting the question.
					} else if (formTreeTable.getValue() instanceof Answer) {
						parent = formTreeTable.getValue().getParent();
					}
					if (parent != null) {
						newAnswer.setName(newAnswer.getDefaultName(parent, 1));
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

	/**
	 * Saves the form into the database.
	 */
	public void save() {
		if (getForm() != null) {
			try {
				formDao.makePersistent(getForm());
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (ConstraintViolationException cve) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY_CAPTION);
			}
		}
	}

	/**
	 * Updates the creator of the object and its parents.
	 * 
	 * @param treeObject
	 */
	private void setCreator(TreeObject treeObject) {
		if (treeObject != null) {
			treeObject.setCreatedBy(UserSessionHandler.getUser());
			treeObject.setCreationTime(new Timestamp(new Date().getTime()));
			setUpdater(treeObject);
		}
	}

	/**
	 * Updates the updater of the object and its parents.
	 * 
	 * @param treeObject
	 */
	private void setUpdater(TreeObject treeObject) {
		if (treeObject != null) {
			treeObject.setUpdatedBy(UserSessionHandler.getUser());
			treeObject.setUpdateTime(new Timestamp(new Date().getTime()));
		}
	}

	/**
	 * Moves the selected element up if possible.
	 * 
	 * @return true if the element has been moved.
	 */
	public boolean moveUp() {
		if (formTreeTable != null) {
			TreeObject selected = formTreeTable.getValue();
			if (selected.getParent() != null && selected.getParent().getChildren().indexOf(selected) > 0) {
				try {
					selected.getParent().switchChildren(selected.getParent().getChildren().indexOf(selected),
							selected.getParent().getChildren().indexOf(selected) - 1, UserSessionHandler.getUser());
					// Refresh the GUI.
					formTreeTable.setRootElement(form);
					// Select the moved element
					formTreeTable.setValue(selected);
					return true;
				} catch (ChildrenNotFoundException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		}
		return false;
	}

	/**
	 * Moves the selected element down if possible.
	 * 
	 * @return true if the element has been moved.
	 */
	public boolean moveDown() {
		if (formTreeTable != null) {
			TreeObject selected = formTreeTable.getValue();
			if (selected.getParent() != null
					&& selected.getParent().getChildren().indexOf(selected) < selected.getParent().getChildren().size() - 1) {
				try {
					selected.getParent().switchChildren(selected.getParent().getChildren().indexOf(selected),
							selected.getParent().getChildren().indexOf(selected) + 1, UserSessionHandler.getUser());
					// Refresh the GUI.
					formTreeTable.setRootElement(form);
					// Select the moved element
					formTreeTable.setValue(selected);
					return true;
				} catch (ChildrenNotFoundException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		}
		return false;
	}

	public void removeSelected() {
		if (formTreeTable != null) {
			TreeObject selected = formTreeTable.getValue();
			if (selected != null && selected.getParent() != null) {
				selected.remove();
				removeElementFromUI(selected);
			}
		}
	}

	private void removeElementFromUI(TreeObject element) {
		for (TreeObject child : element.getChildren()) {
			removeElementFromUI(child);
		}
		if (element.getParent() != null && element.getParent().getChildren().isEmpty()) {
			// formTreeTable.setChildrenAllowed(element.getParent(), false);
		}
		formTreeTable.removeItem(element);
	}

	private class TreeTableValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = 5598877051361847210L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			updateUpperMenu(formTreeTable.getValue());
			updatePropertiesComponent(formTreeTable.getValue());
		}
	}
}
