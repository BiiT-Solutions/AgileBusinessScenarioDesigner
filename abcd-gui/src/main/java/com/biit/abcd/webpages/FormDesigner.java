package com.biit.abcd.webpages;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.authentication.exceptions.NotEnoughRightsToChangeStatusException;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.components.SelectTreeObjectWindow;
import com.biit.abcd.webpages.elements.form.designer.FormDesignerPropertiesComponent;
import com.biit.abcd.webpages.elements.form.designer.FormDesignerUpperMenu;
import com.biit.abcd.webpages.elements.form.designer.FormTreeTable;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.InvalidNameException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormDesigner extends FormWebPageComponent {
    private static final long serialVersionUID = 3237410805898133935L;
    private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
            Arrays.asList(AbcdActivity.READ));
    private FormTreeTable formTreeTable;
    private FormDesignerPropertiesComponent propertiesComponent;
    private FormDesignerUpperMenu upperMenu;
    private TreeTableValueChangeListener treeTableValueChangeListener;
    private boolean tableIsGoingToDetach;

    private IAbcdFormAuthorizationService securityService;

    private IFormDao formDao;

    private CollapseListener collapseListener;
    private ExpandListener expandListener;

    public FormDesigner() {
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        formDao = (IFormDao) helper.getBean("formDao");
        securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
        updateButtons(true);

        collapseListener = new CollapseListener() {
            private static final long serialVersionUID = -4969316575917593209L;

            @Override
            public void nodeCollapse(CollapseEvent event) {
                if (UserSessionHandler.getFormController().getCollapsedStatus() == null) {
                    UserSessionHandler.getFormController().setCollapsedStatus(new HashSet<>());
                }
                UserSessionHandler.getFormController().getCollapsedStatus().add(event.getItemId());
            }
        };
        expandListener = new ExpandListener() {
            private static final long serialVersionUID = -7235454850117978231L;

            @Override
            public void nodeExpand(ExpandEvent event) {
                if (UserSessionHandler.getFormController().getCollapsedStatus() == null) {
                    UserSessionHandler.getFormController().setCollapsedStatus(new HashSet<>());
                }
                UserSessionHandler.getFormController().getCollapsedStatus().remove(event.getItemId());
            }
        };
    }

    @Override
    protected void initContent() {
        // If there is no form, then go back to form manager.
        if (UserSessionHandler.getFormController().getForm() == null) {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
            return;
        }

        tableIsGoingToDetach = false;
        upperMenu = initUpperMenu();
        setUpperMenu(upperMenu);

        treeTableValueChangeListener = new TreeTableValueChangeListener();

        formTreeTable = new FormTreeTable();
        // Remove lazy loading
        formTreeTable.setPageLength(0);
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

        // Collapse the table at category level
        formTreeTable.collapseFrom(Category.class);
        // Retrieve old collapsed state if existed.
        retrieveCollapsedTableState();
        if (UserSessionHandler.getFormController().getLastAccessTreeObject() != null) {
            selectComponent(UserSessionHandler.getFormController().getLastAccessTreeObject());
        } else {
            formTreeTable.setValue(UserSessionHandler.getFormController().getForm());
        }
        saveCollapsedTableState();

        // Set current selected element properties.
        updatePropertiesComponent(formTreeTable.getTreeObjectSelected());
        updateUpperMenu(formTreeTable.getTreeObjectSelected());
    }

    protected void updatePropertiesComponent(TreeObject value) {
        propertiesComponent.updatePropertiesComponent(value);
    }

    @Override
    public List<AbcdActivity> accessAuthorizationsRequired() {
        return activityPermissions;
    }

    private void updateUpperMenu(TreeObject selectedObject) {
        upperMenu.setEnabledButtons(selectedObject);
    }

    private FormDesignerUpperMenu initUpperMenu() {
        FormDesignerUpperMenu upperMenu = new FormDesignerUpperMenu();

        upperMenu.addSaveButtonClickListener((ClickListener) event -> save());

        upperMenu.addNewCategoryButtonButtonClickListener((ClickListener) event -> addCategory());

        upperMenu.addNewGroupButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                addGroup();
            }
        });

        upperMenu.addNewQuestionButtonClickListener((ClickListener) event -> addQuestion());

        upperMenu.addNewAnswerButtonClickListener((ClickListener) event -> addAnswer());
        upperMenu.addNewSubanswerButtonClickListener((ClickListener) event -> addSubanswer());

        upperMenu.addMoveUpButtonClickListener((ClickListener) event -> moveUp());

        upperMenu.addMoveDownButtonClickListener((ClickListener) event -> moveDown());

        upperMenu.addRemoveButtonButtonClickListener((ClickListener) event -> {
            if (existTestScenariosLinked()) {
                final AlertMessageWindow windowAccept = new AlertMessageWindow(
                        LanguageCodes.WARNING_TEST_SCENARIOS_LINKED);
                windowAccept.addAcceptActionListener(window -> {
                    removeSelected();
                    // testScenariosModified = true;
                    windowAccept.close();
                });
                windowAccept.showCentered();
            } else {
                // No remove the form.
                TreeObject selected = formTreeTable.getTreeObjectSelected();
                if ((selected != null) && (selected.getParent() != null)) {
                    final AlertMessageWindow windowAccept = new AlertMessageWindow(
                            LanguageCodes.WARNING_REMOVE_ELEMENT);
                    windowAccept.addAcceptActionListener(new AcceptActionListener() {
                        @Override
                        public void acceptAction(AcceptCancelWindow window) {
                            removeSelected();
                            windowAccept.close();
                        }
                    });
                    windowAccept.showCentered();
                }
            }
        });

        upperMenu.addMoveButtonListener((ClickListener) event -> openMoveWindow());

        upperMenu.addFinishListener((ClickListener) event -> finishForm());

        return upperMenu;
    }

    protected void finishForm() {
        AlertMessageWindow window = new AlertMessageWindow(LanguageCodes.TEXT_PROCEED_FORM_CLOSE);
        window.addAcceptActionListener(new AcceptActionListener() {
            @Override
            public void acceptAction(AcceptCancelWindow window) {
                try {
                    changeStatus(UserSessionHandler.getFormController().getForm(), FormWorkStatus.FINAL_DESIGN);
                    UiAccesser.releaseForm(UserSessionHandler.getUser());
                    ApplicationFrame.navigateTo(WebMap.getMainPage());
                    window.close();
                } catch (Exception e) {
                    MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
                            LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
                    AbcdLogger.errorMessage(this.getClass().getName(), e);
                }
            }
        });
        window.showCentered();
    }

    private void changeStatus(Form form, FormWorkStatus value) {
        try {
            if (!securityService.isAuthorizedActivity(UserSessionHandler.getUser(), form.getOrganizationId(),
                    AbcdActivity.FORM_STATUS_UPGRADE)) {
                throw new NotEnoughRightsToChangeStatusException("User '"
                        + UserSessionHandler.getUser().getEmailAddress()
                        + "' has not enought rights to change the status of form '" + form.getLabel() + "'!");
            }

            form.setStatus(value);
            try {
                formDao.updateFormStatus(form.getId(), value);
            } catch (UnexpectedDatabaseException e) {
                MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
                        LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
            }

        } catch (NotEnoughRightsToChangeStatusException e) {
            MessageManager.showWarning(LanguageCodes.ERROR_OPERATION_NOT_ALLOWED);
        }
    }

    /**
     * Opens move element window.
     */
    protected void openMoveWindow() {
        Set<Class<? extends TreeObject>> classesAllowed = formTreeTable.getTreeObjectSelected().getAncestorsClasses();
        Class<?>[] classesShown = new Class[classesAllowed.size()];
        int i = 0;
        for (Class<? extends TreeObject> parentClass : classesAllowed) {
            classesShown[i] = parentClass;
            i++;
        }

        final SelectTreeObjectWindow moveWindow = new SelectTreeObjectWindow(UserSessionHandler.getFormController()
                .getForm(), false, classesShown);
        moveWindow.showCentered();
        moveWindow.select(formTreeTable.getTreeObjectSelected());
        moveWindow.addAcceptActionListener(new AcceptActionListener() {

            @Override
            public void acceptAction(AcceptCancelWindow window) {
                if (formTreeTable.getTreeObjectSelected() != null && moveWindow.getSelectedTreeObject() != null) {
                    if (existTestScenariosLinked()) {
                        final AlertMessageWindow windowAccept = new AlertMessageWindow(
                                LanguageCodes.WARNING_TEST_SCENARIOS_LINKED);
                        windowAccept.addAcceptActionListener(new AcceptActionListener() {
                            @Override
                            public void acceptAction(AcceptCancelWindow window) {
                                try {
                                    TreeObject whatToMove = formTreeTable.getTreeObjectSelected();
                                    TreeObject whereToMove = moveWindow.getSelectedTreeObject();
                                    TreeObject whatToMoveNewInstance = Form.move(whatToMove, whereToMove);

                                    window.close();
                                    clearAndUpdateFormTable();
                                    formTreeTable.uncollapse(whatToMove);
                                    formTreeTable.setValue(whatToMoveNewInstance);
                                } catch (ChildrenNotFoundException | NotValidChildException | ElementIsReadOnly e) {
                                    MessageManager.showError(LanguageCodes.WARNING_MOVEMENT_NOT_VALID,
                                            LanguageCodes.WARNING_MOVEMENT_DESCRIPTION_NOT_VALID);
                                }
                                // testScenariosModified = true;
                                windowAccept.close();
                                moveWindow.close();
                            }
                        });
                        windowAccept.showCentered();
                    } else {
                        try {
                            TreeObject whatToMove = formTreeTable.getTreeObjectSelected();
                            TreeObject whereToMove = moveWindow.getSelectedTreeObject();
                            TreeObject whatToMoveNewInstance = Form.move(whatToMove, whereToMove);

                            window.close();
                            clearAndUpdateFormTable();
                            formTreeTable.uncollapse(whatToMove);
                            formTreeTable.setValue(whatToMoveNewInstance);
                        } catch (ChildrenNotFoundException | NotValidChildException | ElementIsReadOnly e) {
                            MessageManager.showError(LanguageCodes.WARNING_MOVEMENT_NOT_VALID,
                                    LanguageCodes.WARNING_MOVEMENT_DESCRIPTION_NOT_VALID);
                        }
                    }
                }
            }
        });
    }

    private void clearAndUpdateFormTable() {
        // Clear and update form
        removeCollapseStateListeners();
        TreeObject currentSelection = formTreeTable.getTreeObjectSelected();
        // Required do not remove. If a table detects that you are setting it's
        // value to anything that complies with equals doesn't update the value.
        formTreeTable.setValue(null);
        formTreeTable.setRootElement(UserSessionHandler.getFormController().getForm());
        retrieveCollapsedTableState();
        if (currentSelection != null) {
            if (currentSelection instanceof Form) {
                formTreeTable.setValue(UserSessionHandler.getFormController().getForm());
            } else {
                formTreeTable.setValue(UserSessionHandler.getFormController().getForm()
                        .getChild(currentSelection.getPath()));
            }
        }
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
                        + "' has created a " + newCategory.getClass() + " with name: '" + newCategory.getName() + "'.");
            } catch (NotValidChildException e) {
                // Not possible.
            } catch (ElementIsReadOnly e) {
                MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
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
                formTreeTable.addItem(category, getForm(), true);
            }
        } else {
            formTreeTable.addItem(category, getForm(), true);
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
                                        + newGroup.getClass() + " with name '" + newGroup.getName() + "'.");
                    }
                }
            } catch (NotValidChildException e) {
                // Not possible.
            } catch (ElementIsReadOnly e) {
                MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }
        }
    }

    /**
     * Adds a new question into the UI and the Form object.
     */
    public void addQuestion() {
        if (getForm() != null) {
            Question newQuestion = new Question();
            newQuestion.setAnswerType(AnswerType.INPUT);
            try {
                newQuestion.setAnswerFormat(AnswerFormat.TEXT);
            } catch (InvalidAnswerFormatException e1) {
                // Nothing.
            }
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
                        // If answer or subanswer selected, must be added in the
                        // parent of the question.
                        parent = formTreeTable.getTreeObjectSelected().getAncestor(Question.class).getParent();
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
                                        + newQuestion.getClass() + " with name '" + newQuestion.getName() + " - Type: "
                                        + newQuestion.getAnswerType() + "'.");
                    }
                }
            } catch (NotValidChildException e) {
                // Not possible.
            } catch (ElementIsReadOnly e) {
                MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }
        }
    }

    /**
     * Adds a new answer into the UI and the Form object.
     */
    private void addAnswer() {
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
                        if (((Answer) formTreeTable.getTreeObjectSelected()).isSubanswer()) {
                            parent = formTreeTable.getTreeObjectSelected().getParent().getParent();
                        } else {
                            parent = formTreeTable.getTreeObjectSelected().getParent();
                        }
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
                                        + newAnswer.getClass() + " with name '" + newAnswer.getName() + "'.");
                    }
                }
            } catch (NotValidChildException e) {
                // Not possible.
            } catch (ElementIsReadOnly e) {
                MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }
        }
    }

    private void addSubanswer() {
        TreeObject selectedRow = formTreeTable.getTreeObjectSelected();
        if (selectedRow != null && selectedRow instanceof BaseAnswer) {
            try {
                Answer newAnswer = new Answer();
                setCreator(newAnswer);
                if (formTreeTable.getTreeObjectSelected() != null) {
                    TreeObject parent = formTreeTable.getTreeObjectSelected();
                    if (parent != null) {
                        if (formTreeTable.getTreeObjectSelected() instanceof Answer) {
                            if (((Answer) formTreeTable.getTreeObjectSelected()).isSubanswer()) {
                                parent = formTreeTable.getTreeObjectSelected().getParent();
                            } else {
                                parent = formTreeTable.getTreeObjectSelected();
                            }
                        }
                        try {
                            // Default name must be unique in questions
                            newAnswer.setName(newAnswer.getDefaultName(parent.getParent(), 1));
                        } catch (FieldTooLongException | CharacterNotAllowedException e) {
                            // Default name is never so long.
                        }
                        // First add to UI and then add parent.
                        addElementToUI(newAnswer, parent);
                        parent.addChild(newAnswer);
                        AbcdLogger.info(this.getClass().getName(), "User '"
                                + UserSessionHandler.getUser().getEmailAddress() + "' has created a subanswer "
                                + newAnswer.getClass() + " with name '" + newAnswer.getName() + "'.");
                    }
                }
            } catch (NotValidChildException | ElementIsReadOnly e) {
                // Not possible.
                AbcdLogger.errorMessage(this.getClass().getName(), e);
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
                clearAndUpdateFormTable();
                MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
            } catch (DuplicatedVariableException e) {
                MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_FORM_VARIABLE,
                        LanguageCodes.ERROR_DATABASE_DUPLICATED_FORM_VARIABLE_CAPTION);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            } catch (ConstraintViolationException cve) {
                MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_FORM_VARIABLE,
                        LanguageCodes.ERROR_DATABASE_DUPLICATED_FORM_VARIABLE_CAPTION);
                AbcdLogger.errorMessage(this.getClass().getName(), cve);
            } catch (UnexpectedDatabaseException | ElementCannotBePersistedException e) {
                MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
                        LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            } catch (InvalidNameException e) {
                MessageManager.showError(LanguageCodes.ERROR_INVALID_NAME);
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
                    removeCollapseStateListeners();
                    formTreeTable.setRootElement(getForm());
                    retrieveCollapsedTableState();

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
                    removeCollapseStateListeners();
                    // Refresh the GUI.
                    formTreeTable.setRootElement(getForm());
                    retrieveCollapsedTableState();
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
                    MessageManager.showError(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
                            LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);

                } catch (ElementIsReadOnly e) {
                    MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
                    AbcdLogger.errorMessage(this.getClass().getName(), e);
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
            MessageManager.showError(LanguageCodes.WARNING_ELEMENT_NOT_FOUND,
                    LanguageCodes.WARNING_ELEMENT_NOT_FOUND_DESCRIPTION);
        }
    }

    private boolean existTestScenariosLinked() {
        List<TestScenario> testScenarios = UserSessionHandler.getTestScenariosController().getTestScenarios(
                UserSessionHandler.getFormController().getForm());
        return !testScenarios.isEmpty();
    }

    private void removeCollapseStateListeners() {
        formTreeTable.removeCollapseListener(collapseListener);
        formTreeTable.removeExpandListener(expandListener);
    }

    private void addCollapseStateListeners() {
        formTreeTable.addCollapseListener(collapseListener);
        formTreeTable.addExpandListener(expandListener);
    }

    protected void saveCollapsedTableState() {
        UserSessionHandler.getFormController().setCollapsedStatus(
                formTreeTable.getCollapsedStatus(UserSessionHandler.getFormController().getForm()));
    }

    private void retrieveCollapsedTableState() {
        removeCollapseStateListeners();
        if (UserSessionHandler.getFormController().getCollapsedStatus() != null) {
            formTreeTable.setCollapsedStatus(UserSessionHandler.getFormController().getForm(), UserSessionHandler
                    .getFormController().getCollapsedStatus());
        }
        addCollapseStateListeners();
    }
}
