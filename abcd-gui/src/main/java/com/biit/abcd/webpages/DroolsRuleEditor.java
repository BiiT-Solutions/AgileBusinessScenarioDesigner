package com.biit.abcd.webpages;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectDroolsRuleEditable;
import com.biit.abcd.webpages.elements.drools.rule.ConditionActionEditorComponent;
import com.biit.abcd.webpages.elements.drools.rule.DroolsRuleEditorUpperMenu;
import com.biit.abcd.webpages.elements.drools.rule.SecuredConditionActionEditorComponent;
import com.biit.abcd.webpages.elements.drools.rule.WindowNewRule;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionEditorComponent;
import com.biit.form.exceptions.DependencyExistException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DroolsRuleEditor extends FormWebPageComponent {
    private static final long serialVersionUID = -1017932957756165996L;
    private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
            Arrays.asList(AbcdActivity.READ));
    private DroolsRuleEditorUpperMenu droolsRuleEditorUpperMenu;
    private SelectDroolsRuleEditable tableSelectRule;
    private ExpressionEditorComponent ruleExpressionEditorComponent;

    public DroolsRuleEditor() {
        super();
    }

    @Override
    protected void initContent() {
        updateButtons(true);

        // Create container
        HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
        collapsibleLayout.setSizeFull();

        // Create menu
        tableSelectRule = new SelectDroolsRuleEditable();
        tableSelectRule.setWidth("100%");
        tableSelectRule.setHeight("100%");
        tableSelectRule.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -7103550436798085895L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                UserSessionHandler.getFormController().setLastAccessRule(getSelectedRule());
                refreshRuleEditor();
            }

        });
        collapsibleLayout.createMenu(tableSelectRule);

        // Create content
        ruleExpressionEditorComponent = new SecuredConditionActionEditorComponent();
        ruleExpressionEditorComponent.setSizeFull();
        collapsibleLayout.setContent(ruleExpressionEditorComponent);

        getWorkingAreaLayout().addComponent(collapsibleLayout);

        initUpperMenu();

        if (UserSessionHandler.getFormController().getForm() != null) {
            // Add expressions
            for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
                addRuleToTableMenu(rule);
            }

            sortTableMenu();

            // Select the last access object or the first one
            if (UserSessionHandler.getFormController().getLastAccessExpression() != null) {
                tableSelectRule.setSelectedExpression(UserSessionHandler.getFormController().getLastAccessRule());
            } else {
                // Select the first one if available.
                if (UserSessionHandler.getFormController().getForm().getRules().size() > 0) {

                    Iterator<Rule> iterator = (UserSessionHandler.getFormController().getForm().getRules().iterator());
                    tableSelectRule.setSelectedExpression(iterator.next());
                }
            }
            refreshRuleEditor();
        } else {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
        }
    }

    private void initUpperMenu() {
        final DroolsRuleEditor thisPage = this;
        droolsRuleEditorUpperMenu = new DroolsRuleEditorUpperMenu();

        droolsRuleEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
            private static final long serialVersionUID = 6036676119057486519L;

            @Override
            public void buttonClick(ClickEvent event) {
                save();
            }
        });

        droolsRuleEditorUpperMenu.addNewRuleButtonClickListener(new ClickListener() {
            private static final long serialVersionUID = 377976184801401863L;

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(
                        new WindowNewRule(thisPage, LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR,
                                LanguageCodes.DROOLS_RULES_EDITOR_NEW_RULE_TEXTFIELD));
            }
        });

        droolsRuleEditorUpperMenu.addRemoveRuleButtonClickListener(new ClickListener() {
            private static final long serialVersionUID = -3561685413299735048L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (getSelectedRule() != null) {
                    final AlertMessageWindow windowAccept = new AlertMessageWindow(LanguageCodes.WARNING_RULE_DELETION);
                    windowAccept.addAcceptActionListener(new AcceptActionListener() {
                        @Override
                        public void acceptAction(AcceptCancelWindow window) {
                            Rule rule = tableSelectRule.getSelectedRule();
                            try {
                                CheckDependencies.checkRulesDependencies(UserSessionHandler.getFormController()
                                        .getForm(), rule);
                                removeSelectedRule();
                                AbcdLogger.info(this.getClass().getName(),
                                        "User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
                                                + rule.getClass() + " with name '" + rule.getName() + "'.");
                                windowAccept.close();
                            } catch (DependencyExistException e) {
                                // Forbid the remove action if exist dependency.
                                MessageManager.showError(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
                                        LanguageCodes.RULE_DESIGNER_WARNING_CANNOT_REMOVE_RULE);
                            }
                        }
                    });
                    windowAccept.showCentered();
                }
            }
        });

        droolsRuleEditorUpperMenu.addCopyRuleButtonClickListener(new ClickListener() {
            private static final long serialVersionUID = 377976184801431863L;

            @Override
            public void buttonClick(ClickEvent event) {
                Rule rule = tableSelectRule.getSelectedRule();
                if (rule != null) {
                    rule = rule.generateCopy();
                    rule.setName(rule.getName() + " - Copy");
                    rule.resetIds();
                    UserSessionHandler.getFormController().getForm().getRules().add(rule);
                    addRulefromWindow(rule);
                    sortTableMenu();
                }
            }
        });

        setUpperMenu(droolsRuleEditorUpperMenu);
    }

    private Rule getSelectedRule() {
        return tableSelectRule.getSelectedRule();
    }

    private void refreshRuleEditor() {
        ((ConditionActionEditorComponent) ruleExpressionEditorComponent).refreshRuleEditor(getSelectedRule());
        ruleExpressionEditorComponent.updateSelectionStyles();
    }

    private void save() {
        try {
            UserSessionHandler.getFormController().save();
            refreshLeftTable();
            MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
        } catch (Exception e) {
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
            AbcdLogger.errorMessage(TableRuleEditor.class.getName(), e);
        }
    }

    private void refreshLeftTable() {
        Rule selectedRule = getSelectedRule();
        tableSelectRule.setSelectedExpression(null);
        tableSelectRule.update(UserSessionHandler.getFormController().getForm());
        //We need to get the new instance that has been merged to the current jpa context.
        for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
            if (selectedRule != null && rule.getComparationId().equals(selectedRule.getComparationId())) {
                tableSelectRule.setSelectedExpression(rule);
            }
        }
    }

    @Override
    public List<AbcdActivity> accessAuthorizationsRequired() {
        return activityPermissions;
    }

    private void removeSelectedRule() {
        UserSessionHandler.getFormController().getForm().getRules().remove(tableSelectRule.getSelectedRule());
        tableSelectRule.removeSelectedRow();
        refreshRuleEditor();
    }

    public void addRulefromWindow(Rule rule) {
        tableSelectRule.addRow(rule);
        tableSelectRule.setSelectedExpression(rule);
    }

    public void addRuleToTableMenu(Rule rule) {
        tableSelectRule.addRow(rule);
    }

    public void sortTableMenu() {
        tableSelectRule.sort();
    }

    public void selectComponent(Rule rule) {
        tableSelectRule.setValue(rule);
    }
}