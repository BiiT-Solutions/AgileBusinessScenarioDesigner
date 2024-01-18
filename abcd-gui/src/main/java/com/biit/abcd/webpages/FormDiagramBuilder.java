package com.biit.abcd.webpages;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.components.SelectDiagramTable;
import com.biit.abcd.webpages.elements.diagram.builder.AbcdDiagramBuilder;
import com.biit.abcd.webpages.elements.diagram.builder.AbcdDiagramBuilder.DiagramObjectPickedListener;
import com.biit.abcd.webpages.elements.diagram.builder.DiagramObjectUpdatedListener;
import com.biit.abcd.webpages.elements.diagram.builder.DiagramPropertiesComponent;
import com.biit.abcd.webpages.elements.diagram.builder.FormDiagramBuilderUpperMenu;
import com.biit.abcd.webpages.elements.diagram.builder.JumpToListener;
import com.biit.abcd.webpages.elements.diagram.builder.WindowNewDiagram;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FormDiagramBuilder extends FormWebPageComponent {
    private static final long serialVersionUID = 3237410805898133935L;
    private static final List<AbcdActivity> activityPermissions = new ArrayList<>(
            Arrays.asList(AbcdActivity.READ));
    private SelectDiagramTable diagramBuilderTable;
    private AbcdDiagramBuilder diagramBuilder;
    private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;
    private DiagramPropertiesComponent propertiesContainer;
    private DiagramTableValueChange diagramTableValueChange;

    public FormDiagramBuilder() {
        super();
    }

    @Override
    protected void initContent() {
        // If there is no form, then go back to form manager.
        if (UserSessionHandler.getFormController().getForm() == null) {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
            return;
        }

        updateButtons(true);

        HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel(false);
        rootLayout.setSizeFull();

        HorizontalLayout rootDiagramBuilder = new HorizontalLayout();
        rootDiagramBuilder.setSpacing(true);

        propertiesContainer = new DiagramPropertiesComponent();
        propertiesContainer.setSizeFull();

        diagramBuilderTable = new SelectDiagramTable();
        diagramBuilderTable.setSizeFull();
        diagramBuilderTable.setImmediate(true);
        diagramBuilderTable.setSelectable(true);
        diagramBuilderTable.setNullSelectionAllowed(false);
        diagramTableValueChange = new DiagramTableValueChange();

        diagramBuilder = new AbcdDiagramBuilder();
        diagramBuilder.setSizeFull();

        diagramBuilder.addDiagramObjectPickedListener(new DiagramObjectPickedListener() {

            @Override
            public void diagramObjectPicked(DiagramObject object) {
                //Object can be null (no-selection)
                propertiesContainer.focus();
                propertiesContainer.updatePropertiesComponent(object);

                if (object != null) {
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' Element " + object
                            + " picked in Diagram: '" + diagramBuilder.getDiagram().getName() + "'.");
                } else {
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' Cleared it's selection.");
                }
            }
        });
        diagramBuilder.addJumpToListener(new JumpToListener() {

            @Override
            public void jumpTo(Object element) {
                if (element == null) {
                    return;
                }
                if (element instanceof ExpressionValueTreeObjectReference) {
                    ApplicationFrame.navigateTo(WebMap.TREE_DESIGNER);
                    FormDesigner formDesigner = (FormDesigner) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
                    formDesigner.selectComponent(((ExpressionValueTreeObjectReference) element).getReference());
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' jumped to " + WebMap.TREE_DESIGNER
                            + " from Diagram: '" + diagramBuilder.getDiagram().getName() + "'.");
                }
                if (element instanceof TableRule) {
                    ApplicationFrame.navigateTo(WebMap.DECISSION_TABLE_EDITOR);
                    TableRuleEditor decisionTable = (TableRuleEditor) ((ApplicationFrame) UI.getCurrent())
                            .getCurrentView();
                    decisionTable.selectComponent((TableRule) element);
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' jumped to "
                            + WebMap.DECISSION_TABLE_EDITOR + " from Diagram: '"
                            + diagramBuilder.getDiagram().getName() + "'.");
                }
                if (element instanceof ExpressionChain) {
                    ApplicationFrame.navigateTo(WebMap.EXPRESSION_EDITOR);
                    ExpressionEditor expressionEditor = (ExpressionEditor) ((ApplicationFrame) UI.getCurrent())
                            .getCurrentView();
                    expressionEditor.selectComponent((ExpressionChain) element);
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' jumped to "
                            + WebMap.EXPRESSION_EDITOR + " from Diagram: '" + diagramBuilder.getDiagram().getName()
                            + "'.");
                }
                if (element instanceof Rule) {
                    ApplicationFrame.navigateTo(WebMap.DROOLS_RULE_EDITOR);
                    DroolsRuleEditor ruleEditor = (DroolsRuleEditor) ((ApplicationFrame) UI.getCurrent())
                            .getCurrentView();
                    ruleEditor.selectComponent((Rule) element);
                    AbcdLogger.info(this.getClass().getName(), "User '"
                            + UserSessionHandler.getUser().getEmailAddress() + "' jumped to "
                            + WebMap.DROOLS_RULE_EDITOR + " from Diagram: '" + diagramBuilder.getDiagram().getName()
                            + "'.");
                }
                if (element instanceof Diagram) {
                    selectComponent((Diagram) element);
                }
            }
        });

        diagramBuilder.addDiagramObjectAddedListener(diagramObject -> {
            // A link must obtain the diagram source for the expressions.
            if (diagramObject instanceof DiagramLink) {
                DiagramLink diagramLink = (DiagramLink) diagramObject;
                if (diagramLink.getSourceElement() instanceof DiagramFork) {
                    updateForkChanges(((DiagramFork) diagramLink.getSourceElement()));
                    if (diagramLink.getSourceElement() != null
                            && ((DiagramFork) diagramLink.getSourceElement()).getReference() != null) {
                        Expression expression = ((DiagramFork) diagramLink.getSourceElement()).getReference()
                                .generateCopy();
                        expression.resetIds();
                        expression.setEditable(false);
                        diagramLink.replaceExpressions(expression);
                    }
                }
            }
        });

        diagramBuilder.addDiagramObjectUpdatedListener(new DiagramObjectUpdatedListener() {
            @Override
            public void elementUpdated(DiagramObject diagramObject) {
                // A link must obtain the diagram source for the expressions.
                if (diagramObject instanceof DiagramLink) {
                    DiagramLink diagramLink = (DiagramLink) diagramObject;
                    if (diagramLink.getParent() != null) {
                        if (diagramLink.getTargetElement().getIncomingLinks().size() > 1) {
                            diagramBuilder.undo();
                            AbcdLogger.warning(this.getClass().getName(),
                                    "The selected node can't have two incoming links");
                        } else if (!(diagramLink.getSourceElement() instanceof DiagramFork)) {
                            if (diagramLink.getSourceElement() != null
                                    && diagramLink.getSourceElement().getOutgoingLinks().size() > 1) {
                                diagramBuilder.undo();
                                AbcdLogger.warning(this.getClass().getName(),
                                        "The selected node can't have two outgoing links");
                            }
                        }
                    }
                }
            }
        });
        // Diagram builder starts as disabled until a diagram is selected.
        diagramBuilder.setEnabled(false);

        propertiesContainer.addPropertyUpdateListener(new PropertieUpdateListener() {
            @Override
            public void propertyUpdate(Object element) {
                // Switch limitation with instanceof.
                if (element instanceof DiagramLink) {
                    // If we are updating a link, then we must update the source
                    DiagramLink currentLink = (DiagramLink) element;
                    DiagramObject source = currentLink.getSourceElement();
                    if (source != null) {
                        propertyUpdate(source);
                    }
                    return;
                }
                if (element instanceof DiagramFork) {
                    updateForkChanges((DiagramFork) element);
                    return;
                }
                if (element instanceof DiagramChild) {
                    DiagramChild diagramChild = (DiagramChild) element;
                    diagramBuilder.updateChangesToDiagram(diagramChild);
                    initializeDiagramsTable();
                    // BUG FIX to extrange null pointer exception
                    // diagramBuilderTable.setValue(diagramChild.getParent());
                    return;
                }
                // Anyone else
                diagramBuilder.updateChangesToDiagram((DiagramObject) element);
            }
        });

        rootDiagramBuilder.addComponent(diagramBuilder);
        rootDiagramBuilder.setExpandRatio(diagramBuilder, 0.80f);
        rootDiagramBuilder.addComponent(propertiesContainer);
        rootDiagramBuilder.setExpandRatio(propertiesContainer, 0.20f);

        rootLayout.createMenu(diagramBuilderTable);
        rootLayout.setContent(rootDiagramBuilder);

        getWorkingAreaLayout().addComponent(rootLayout);

        initializeDiagramsTableAndSelectFirst();
        initUpperMenu();
    }

    private void updateForkChanges(DiagramFork fork) {
        List<DiagramLink> links = fork.getOutgoingLinks();
        // TODO add a checking to know if it is valid and change
        // color or something
        for (DiagramLink link : links) {
            diagramBuilder.updateChangesToDiagram(link);
        }
        diagramBuilder.updateChangesToDiagram(fork);
    }

    protected void selectComponent(Diagram element) {
        diagramBuilderTable.setValue(element);
        AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                + "' Selected diagram: " + diagramBuilder.getDiagram().getName() + "'.");
    }

    private void initializeDiagramsTable() {
        if (UserSessionHandler.getFormController().getForm() == null) {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
        } else {
            Set<Diagram> diagrams = UserSessionHandler.getFormController().getForm().getDiagrams();
            diagramBuilderTable.removeValueChangeListener(diagramTableValueChange);
            diagramBuilderTable.setValue(null);
            diagramBuilderTable.removeAllItems();
            diagramBuilderTable.addRows(diagrams);
            diagramBuilderTable.addValueChangeListener(diagramTableValueChange);
        }
    }

    private void initializeDiagramsTableAndSelectFirst() {
        initializeDiagramsTable();
        if (UserSessionHandler.getFormController().getLastAccessDiagram() != null) {
            diagramBuilderTable.setValue(UserSessionHandler.getFormController().getLastAccessDiagram());
        } else {
            diagramBuilderTable.selectFirstRow();
        }
    }

    private void resetDiagramsTable() {
        Diagram selectedDiagram = (Diagram) diagramBuilderTable.getValue();
        diagramBuilderTable.setValue(null);
        initializeDiagramsTable();

        //We need to get the new instance that has been merged to the current jpa context.
        for (Diagram diagram : UserSessionHandler.getFormController().getForm().getDiagrams()) {
            if (selectedDiagram != null && diagram.getComparationId().equals(selectedDiagram.getComparationId())) {
                diagramBuilderTable.setValue(diagram);
                break;
            }
        }
    }

    private void initUpperMenu() {
        final FormDiagramBuilder thisPage = this;
        diagramBuilderUpperMenu = new FormDiagramBuilderUpperMenu();
        diagramBuilderUpperMenu.addNewDiagramButtonClickListener((ClickListener) event -> UI.getCurrent().addWindow(
                new WindowNewDiagram(thisPage, LanguageCodes.FORM_DIAGRAM_BUILDER_NEW_DIAGRAM_CAPTION,
                        LanguageCodes.FORM_DIAGRAM_BUILDER_NEW_DIAGRAM_TEXTFIELD)));
        diagramBuilderUpperMenu.addDeleteNewDiagramButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (diagramBuilderTable.getValue() != null) {
                    final AlertMessageWindow windowAccept = new AlertMessageWindow(
                            LanguageCodes.WARNING_DIAGRAM_DELETION);
                    windowAccept.addAcceptActionListener(new AcceptActionListener() {
                        @Override
                        public void acceptAction(AcceptCancelWindow window) {
                            Diagram diagram = (Diagram) diagramBuilderTable.getValue();
                            deleteDiagram();
                            AbcdLogger.info(this.getClass().getName(),
                                    "User '" + UserSessionHandler.getUser().getEmailAddress() + "' has deleted a "
                                            + diagram.getClass() + " with 'Name: " + diagram.getName() + "'.");
                            windowAccept.close();
                        }
                    });
                    windowAccept.showCentered();
                }
            }
        });
        diagramBuilderUpperMenu.addClearButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.clear();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' has cleared Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });

        diagramBuilderUpperMenu.addSaveButtonClickListener((ClickListener) event -> save());

        diagramBuilderUpperMenu.addUndoButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.undo();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'Undo' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });
        diagramBuilderUpperMenu.addRedoButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.redo();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'Redo' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });
        diagramBuilderUpperMenu.addToFrontButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.toFront();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'To Front' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });
        diagramBuilderUpperMenu.addToBackButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.toBack();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'To Back' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });
        diagramBuilderUpperMenu.addToSvgButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.openAsSvg();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'Svg' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });
        diagramBuilderUpperMenu.addToPngButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                diagramBuilder.openAsPng();
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' 'Png' action in Diagram '" + diagramBuilder.getDiagram().getName() + "'.");
            }
        });

        setUpperMenu(diagramBuilderUpperMenu);
    }

    private void deleteDiagram() {
        if (diagramBuilderTable.getValue() != null) {
            UserSessionHandler.getFormController().getForm().removeDiagram((Diagram) diagramBuilderTable.getValue());
            diagramBuilderTable.removeItem(diagramBuilderTable.getValue());
            diagramBuilderTable.setValue(null);
        }
    }

    @Override
    public List<AbcdActivity> accessAuthorizationsRequired() {
        return activityPermissions;
    }

    private void save() {
        try {
            diagramBuilder.setDiagram(null);
            UserSessionHandler.getFormController().save();
            resetDiagramsTable();
            MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
        } catch (Exception e) {
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
            AbcdLogger.errorMessage(FormDiagramBuilder.class.getName(), e);
        }
    }

    public void addDiagram(Diagram newDiagram) {
        diagramBuilderTable.addDiagram(newDiagram);
        diagramBuilderTable.setValue(newDiagram);
    }

    public void sortTableMenu() {
        diagramBuilderTable.sort();
    }

    private class DiagramTableValueChange implements ValueChangeListener {
        private static final long serialVersionUID = 8142953635201344140L;

        @Override
        public void valueChange(ValueChangeEvent event) {
            final Diagram currentDiagram = (Diagram) event.getProperty().getValue();
            propertiesContainer.setFireListeners(false);
            propertiesContainer.updatePropertiesComponent(null);
            diagramBuilder.setDiagram(currentDiagram);
            propertiesContainer.setFireListeners(true);
            UserSessionHandler.getFormController().setLastAccessDiagram(currentDiagram);
        }
    }
}