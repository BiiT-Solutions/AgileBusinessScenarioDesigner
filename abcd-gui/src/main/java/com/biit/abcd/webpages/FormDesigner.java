package com.biit.abcd.webpages;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.components.SelectTreeObjectWindow;
import com.biit.abcd.webpages.elements.formdesigner.FormDesignerPropertiesComponent;
import com.biit.abcd.webpages.elements.formdesigner.FormDesignerUpperMenu;
import com.biit.abcd.webpages.elements.formdesigner.FormTreeTable;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class FormDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;
	private FormTreeTable formTreeTable;
	private FormDesignerPropertiesComponent propertiesComponent;
	private FormDesignerUpperMenu upperMenu;
	private TreeTableValueChangeListener treeTableValueChangeListener;
	private boolean tableIsGoingToDetach;

	public FormDesigner() {
		updateButtons(true);
	}

	@Override
	protected void initContent() {
		// If there is no form, then go back to form manager.
		if (UserSessionHandler.getFormController().getForm() == null) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
			return;
		}

		tableIsGoingToDetach = false;
		upperMenu = initUpperMenu();
		setUpperMenu(upperMenu);

		treeTableValueChangeListener = new TreeTableValueChangeListener();

		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
		formTreeTable.addValueChangeListener(treeTableValueChangeListener);

		propertiesComponent = new FormDesignerPropertiesComponent();
		propertiesComponent.setSizeFull();
		propertiesComponent.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				if (tableIsGoingToDetach) {
					return;
				}
				formTreeTable.updateItem((TreeObject) element);
				updateUpperMenu(formTreeTable.getTreeObjectSelected());
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

		// Remove ValueChange listener and re add it after load the entire form.
		// This will remove the unnecessary overhead of calls when loading a
		// form.
		formTreeTable.setValue(null);
		formTreeTable.removeValueChangeListener(treeTableValueChangeListener);
		formTreeTable.setRootElement(UserSessionHandler.getFormController().getForm());
		formTreeTable.addValueChangeListener(treeTableValueChangeListener);

		formTreeTable.addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -9057209239644161482L;

			@Override
			public void detach(DetachEvent event) {
				tableIsGoingToDetach = true;
				formTreeTable.removeValueChangeListener(treeTableValueChangeListener);
			}
		});

		if (UserSessionHandler.getFormController().getLastAccessTreeObject() != null) {
			selectComponent(UserSessionHandler.getFormController().getLastAccessTreeObject());
		} else {
			formTreeTable.setValue(UserSessionHandler.getFormController().getForm());
		}
		// Collapse the table at question level
		formTreeTable.collapseFrom(Question.class);
	}

	protected void updatePropertiesComponent(TreeObject value) {
		propertiesComponent.updatePropertiesComponent(value);
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void updateUpperMenu(TreeObject selectedObject) {
		upperMenu.setEnabledButtons(selectedObject);
	}

	private FormDesignerUpperMenu initUpperMenu() {
		FormDesignerUpperMenu upperMenu = new FormDesignerUpperMenu();

		upperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7788465178005102302L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		upperMenu.addNewCategoryButtonButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -2120693196054701885L;

			@Override
			public void buttonClick(ClickEvent event) {
				addCategory();
			}
		});

		upperMenu.addNewGroupButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 564498223106608362L;

			@Override
			public void buttonClick(ClickEvent event) {
				addGroup();
			}
		});

		upperMenu.addNewQuestionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -6280274747616018339L;

			@Override
			public void buttonClick(ClickEvent event) {
				addQuestion();
			}
		});

		upperMenu.addNewAnswerButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -1019979581523797207L;

			@Override
			public void buttonClick(ClickEvent event) {
				addAnswer();
			}
		});

		upperMenu.addMoveUpButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -9204448853469545121L;

			@Override
			public void buttonClick(ClickEvent event) {
				moveUp();
			}
		});

		upperMenu.addMoveDownButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 4575916787722812776L;

			@Override
			public void buttonClick(ClickEvent event) {
				moveDown();
			}
		});

		upperMenu.addRemoveButtonButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4058521934359941054L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelected();
			}
		});

		upperMenu.addMoveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 808060310562321887L;

			@Override
			public void buttonClick(ClickEvent event) {
				openMoveWindow();
			}
		});

		return upperMenu;
	}

	/**
	 * Opens move element window.
	 */
	protected void openMoveWindow() {
		final SelectTreeObjectWindow moveWindow = new SelectTreeObjectWindow(UserSessionHandler.getFormController()
				.getForm(), false);
		moveWindow.showCentered();
		moveWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(AcceptCancelWindow window) {
				if (formTreeTable.getTreeObjectSelected() != null && moveWindow.getSelectedTreeObject() != null) {
					try {
						TreeObject whatToMove = formTreeTable.getTreeObjectSelected();
						TreeObject whereToMove = moveWindow.getSelectedTreeObject();
						TreeObject.move(whatToMove, whereToMove);
						window.close();
						clearAndUpdateFormTable();
						formTreeTable.setValue(whatToMove);
						formTreeTable.collapseFrom(Question.class);
					} catch (ChildrenNotFoundException | NotValidChildException e) {
						MessageManager.showWarning(LanguageCodes.WARNING_MOVEMENT_NOT_VALID,
								LanguageCodes.WARNING_MOVEMENT_DESCRIPTION_NOT_VALID);
					}
				}
			}
		});
	}

	private void clearAndUpdateFormTable() {
		// Clear and update form
		TreeObject currentSelection = formTreeTable.getTreeObjectSelected();
		formTreeTable.setRootElement(UserSessionHandler.getFormController().getForm());
		formTreeTable.select(currentSelection);
	}

	private Form getForm() {
		return UserSessionHandler.getFormController().getForm();
	}

	/**
	 * Adds a new category into the UI and the Form object.
	 */
	public void addCategory() {
		if (getForm() != null) {
			Category newCategory = new Category();
			setCreator(newCategory);
			try {
				if (formTreeTable.getTreeObjectSelected() != null) {
					Category selectedCategory = (Category) formTreeTable.getTreeObjectSelected().getAncestor(
							Category.class);
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
				try {
					newCategory.setName(newCategory.getDefaultName(getForm(), getForm().getChildren().size()));
				} catch (FieldTooLongException | CharacterNotAllowedException e) {
					// Default name is never so long.
				}
				addCategoryToUI(newCategory);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has created a " + newCategory.getClass() + " with 'Name: " + newCategory.getName() + "'.");
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
		if (formTreeTable.getTreeObjectSelected() != null) {
			Category selectedCategory = (Category) formTreeTable.getTreeObjectSelected().getAncestor(Category.class);
			if (selectedCategory != null) {
				TreeObject getLastElementOfCategory = selectedCategory.getLastElement();
				formTreeTable.addItemAfter(getLastElementOfCategory, category, getForm());
			} else {
				formTreeTable.addItem(category, getForm());
			}
		} else {
			formTreeTable.addItem(category, getForm());
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
				if (formTreeTable.getTreeObjectSelected() != null) {
					TreeObject container = formTreeTable.getTreeObjectSelected().getAncestor(Group.class);
					if (container == null) {
						container = formTreeTable.getTreeObjectSelected().getAncestor(Category.class);
					}
					if (container != null) {
						try {
							newGroup.setName(newGroup.getDefaultName(container, 1));
						} catch (FieldTooLongException | CharacterNotAllowedException e) {
							// Default name is never so long.
						}
						addElementToUI(newGroup, container);
						container.addChild(newGroup);
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has created a "
										+ newGroup.getClass() + " with 'Name: " + newGroup.getName() + "'.");
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
			newQuestion.setAnswerType(AnswerType.RADIO);
			setCreator(newQuestion);
			try {
				if (formTreeTable.getTreeObjectSelected() != null) {
					TreeObject parent = null;
					if ((formTreeTable.getTreeObjectSelected() instanceof Category)
							|| (formTreeTable.getTreeObjectSelected() instanceof Group)) {
						parent = formTreeTable.getTreeObjectSelected();
						// If selected a question, we consider the same that
						// selecting the question's parent.
					} else if (formTreeTable.getTreeObjectSelected() instanceof Question) {
						parent = formTreeTable.getTreeObjectSelected().getParent();
					} else if (formTreeTable.getTreeObjectSelected() instanceof Answer) {
						parent = formTreeTable.getTreeObjectSelected().getParent().getParent();
					}
					if (parent != null) {
						try {
							newQuestion.setName(newQuestion.getDefaultName(parent, 1));
						} catch (FieldTooLongException | CharacterNotAllowedException e) {
							// Default name is never so long.
						}
						addElementToUI(newQuestion, parent);
						parent.addChild(newQuestion);
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has created a "
										+ newQuestion.getClass() + " with 'Name: " + newQuestion.getName()
										+ " - Type: " + newQuestion.getAnswerType() + "'.");
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
				if (formTreeTable.getTreeObjectSelected() != null) {
					TreeObject parent = null;
					if (formTreeTable.getTreeObjectSelected() instanceof Question) {
						parent = formTreeTable.getTreeObjectSelected();
						// If selected an answer, we consider the same that
						// selecting the question.
					} else if (formTreeTable.getTreeObjectSelected() instanceof Answer) {
						parent = formTreeTable.getTreeObjectSelected().getParent();
					}
					if (parent != null) {
						try {
							newAnswer.setName(newAnswer.getDefaultName(parent, 1));
						} catch (FieldTooLongException | CharacterNotAllowedException e) {
							// Default name is never so long.
						}
						// First add to UI and then add parent.
						addElementToUI(newAnswer, parent);
						parent.addChild(newAnswer);
						AbcdLogger.info(this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has created a "
										+ newAnswer.getClass() + " with 'Name: " + newAnswer.getName() + "'.");
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
		if (formTreeTable.getTreeObjectSelected() != null) {
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
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (DuplicatedVariableException e) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE_CAPTION);

			} catch (ConstraintViolationException cve) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE_CAPTION);
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
			TreeObject selected = formTreeTable.getTreeObjectSelected();
			if ((selected != null) && (selected.getParent() != null)
					&& (selected.getParent().getChildren().indexOf(selected) > 0)) {
				try {
					selected.getParent().switchChildren(selected.getParent().getChildren().indexOf(selected),
							selected.getParent().getChildren().indexOf(selected) - 1, UserSessionHandler.getUser());
					// Refresh the GUI.
					formTreeTable.setRootElement(getForm());
					// Select the moved element
					formTreeTable.setValue(selected);

					AbcdLogger.info(
							this.getClass().getName(),
							"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has moved up a "
									+ selected.getClass() + "in the Form, with 'Name: " + selected.getName() + "'.");

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
			TreeObject selected = formTreeTable.getTreeObjectSelected();
			if ((selected != null)
					&& (selected.getParent() != null)
					&& (selected.getParent().getChildren().indexOf(selected) < (selected.getParent().getChildren()
							.size() - 1))) {
				try {
					selected.getParent().switchChildren(selected.getParent().getChildren().indexOf(selected),
							selected.getParent().getChildren().indexOf(selected) + 1, UserSessionHandler.getUser());
					// Refresh the GUI.
					formTreeTable.setRootElement(getForm());
					// Select the moved element
					formTreeTable.setValue(selected);

					AbcdLogger.info(this.getClass().getName(),
							"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has moved down a "
									+ selected.getClass() + "in the Form, with 'Name: " + selected.getName() + "'.");

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
			TreeObject selected = formTreeTable.getTreeObjectSelected();
			if ((selected != null) && (selected.getParent() != null)) {
				try {
					selected.remove();
					removeElementFromUI(selected);
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has removed a " + selected.getClass()
							+ " from the Form, with 'Name: " + selected.getName() + "'.");
				} catch (DependencyExistException e) {
					// Forbid the remove action if exist dependency.
					MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
							LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);

				}
			}
		}
	}

	private void removeElementFromUI(TreeObject element) {
		for (TreeObject child : element.getChildren()) {
			removeElementFromUI(child);
		}
		if ((element.getParent() != null) && element.getParent().getChildren().isEmpty()) {
			// formTreeTable.setChildrenAllowed(element.getParent(), false);
		}
		formTreeTable.removeItem(element);
	}

	private class TreeTableValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = 5598877051361847210L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (formTreeTable.getTreeObjectSelected() != null) {
				UserSessionHandler.getFormController().setLastAccessTreeObject(formTreeTable.getTreeObjectSelected());
			}
			updateUpperMenu(formTreeTable.getTreeObjectSelected());
			updatePropertiesComponent(formTreeTable.getTreeObjectSelected());
		}
	}

	public void selectComponent(TreeObject element) {
		if (formTreeTable.getItem(element) != null) {
			formTreeTable.setValue(element);
		} else {
			MessageManager.showWarning(LanguageCodes.WARNING_ELEMENT_NOT_FOUND,
					LanguageCodes.WARNING_ELEMENT_NOT_FOUND_DESCRIPTION);
		}
	}

}
