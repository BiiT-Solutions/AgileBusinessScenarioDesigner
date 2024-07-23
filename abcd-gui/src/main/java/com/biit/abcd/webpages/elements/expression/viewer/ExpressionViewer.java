package com.biit.abcd.webpages.elements.expression.viewer;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.rules.validators.ExpressionValidator;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValue;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.SelectGlobalConstantsWindow;
import com.biit.abcd.webpages.components.SelectTreeObjectWindow;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.biit.abcd.webpages.components.WindowSelectDateUnit;
import com.biit.form.entity.TreeObject;
import com.biit.utils.date.DateManager;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExpressionViewer extends CssLayout {
    private static final long serialVersionUID = -3032370197806581430L;
    private static final String CLASSNAME = "v-expression-viewer";
    private static final String LINE_HEIGHT = "32px";
    private ExpressionChain expressions;
    private Expression selectedExpression = null;
    private VerticalLayout rootLayout;
    // Used for storing the relationship.
    protected HashMap<ExpressionElement, Expression> expressionOfElement;
    private Label evaluatorOutput;
    // If this editor has the focus.
    private boolean focused;
    private final transient List<LayoutClickedListener> clickedListeners;
    private final List<HorizontalLayout> lines;
    private HorizontalLayout selectedLine = null;

    public interface LayoutClickedListener {
        public void clickedAction(ExpressionViewer viewer);
    }

    public ExpressionViewer() {
        setImmediate(true);
        lines = new ArrayList<>();
        expressionOfElement = new HashMap<>();
        setStyleName(CLASSNAME);
        clickedListeners = new ArrayList<LayoutClickedListener>();
    }

    public void updateExpression() {
        updateExpression(expressions);
    }

    public void updateExpression(ExpressionChain expressions) {
        removeAllComponents();
        expressionOfElement = new HashMap<>();

        rootLayout = new VerticalLayout();
        rootLayout.setMargin(true);
        rootLayout.setSpacing(false);
        rootLayout.setImmediate(true);
        rootLayout.setWidth("100%");
        rootLayout.setHeight(null);
        addClickController();

        this.expressions = expressions;

        // Evaluator
        HorizontalLayout evaluatorLayout = createEvaluatorLayout();
        rootLayout.addComponent(evaluatorLayout);

        addNewLine();

        if (expressions != null) {
            addExpressions(expressions);
        } else {
            selectedExpression = null;
        }

        // If expand ratio is 0, component is not shown.
        rootLayout.setExpandRatio(evaluatorLayout, 0.00001f);
        rootLayout.setComponentAlignment(evaluatorLayout, Alignment.BOTTOM_RIGHT);

        updateExpressionSelectionStyles();

        addComponent(rootLayout);

        updateEvaluator();
    }

    private void addNewLine() {
        if (rootLayout != null) {
            // One line for the expressions.
            HorizontalLayout lineLayout = new HorizontalLayout();
            lineLayout.setMargin(false);
            lineLayout.setSpacing(false);
            lineLayout.setImmediate(true);
            lineLayout.setWidth(null);
            lineLayout.setHeight(LINE_HEIGHT);
            rootLayout.addComponent(lineLayout);
            rootLayout.setExpandRatio(lineLayout, 0.99999f);

            selectedLine = lineLayout;
            lines.add(lineLayout);
        }
    }

    private void addExpressions(ExpressionChain expressions) {
        if (selectedLine == null) {
            addNewLine();
        }
        for (Expression expression : expressions.getExpressions()) {
            addExpression(selectedLine, expression);
            if (expression instanceof ExpressionSymbol) {
                if (((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.PILCROW)) {
                    addNewLine();
                }
            }
        }
    }

    protected boolean isExpressionEditable(Expression expression) {
        return expression.isEditable();
    }

    private void addExpression(HorizontalLayout lineLayout, final Expression expression) {
        final ExpressionElement expressionElement = new ExpressionElement(expression, new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutClickEvent event) {
              setSelectedExpression(expression);
                // Double click open operator popup.

                if (event.isDoubleClick()) {
                    // For Operators.
                    if (isExpressionEditable(expression)) {
                        if (expression instanceof ExpressionOperator) {

                            ChangeExpressionOperatorWindow operatorWindow = new ChangeExpressionOperatorWindow(
                                    expression);
                            operatorWindow.showCentered();
                            operatorWindow.addAcceptActionListener(window -> {
                                try {
                                    ((ExpressionOperator) expression)
                                            .setValue(((ChangeExpressionOperatorWindow) window).getOperator());
                                    window.close();
                                    updateExpression();
                                    setSelectedExpression(expression);
                                } catch (NotValidOperatorInExpression e) {
                                    AbcdLogger.errorMessage(this.getClass().getName(), e);
                                    // Not possible
                                }

                            });
                            // SystemDates cannot be edited.
                        } else if (expression instanceof ExpressionValueSystemDate) {
                            // For Input fields.
                        } else if (expression instanceof ExpressionValueString
                                || expression instanceof ExpressionValueNumber
                                || expression instanceof ExpressionValuePostalCode
                                || expression instanceof ExpressionValueTimestamp) {
                            StringInputWindow stringInputWindow = new StringInputWindow();
                            // stringInputWindow.enableExpressionType(false);
                            stringInputWindow.setCaption(ServerTranslate
                                    .translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_CAPTION));

                            if (expression instanceof ExpressionValuePostalCode) {
                                stringInputWindow.setFormat(AnswerFormat.POSTAL_CODE);

                            } else if (expression instanceof ExpressionValueString) {
                                stringInputWindow.setFormat(AnswerFormat.TEXT);

                            } else if (expression instanceof ExpressionValueNumber) {
                                stringInputWindow.setFormat(AnswerFormat.NUMBER);

                            } else if (expression instanceof ExpressionValueTimestamp) {
                                stringInputWindow.setFormat(AnswerFormat.DATE);
                            }

                            if (expression instanceof ExpressionValueTimestamp) {
                                stringInputWindow.setValue(DateManager.convertDateToString(
                                        (Timestamp) expression.getValue(), ExpressionValueTimestamp.DATE_FORMAT));
                            } else {
                                stringInputWindow.setValue(((ExpressionValue<?>) expression).getValue() + "");
                            }

                            stringInputWindow.addAcceptActionListener(new AcceptActionListener() {
                                @Override
                                public void acceptAction(AcceptCancelWindow window) {
                                    String value = ((StringInputWindow) window).getValue();
                                    if ((value == null) || value.isEmpty()) {
                                        MessageManager.showError(ServerTranslate
                                                .translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
                                    } else {
                                        try {
                                            // It is a number.
                                            int currentModified = expressions.getExpressions().indexOf(expression);
                                            Expression selectExpression = expression;
                                            switch (((StringInputWindow) window).getFormat()) {
                                                case NUMBER:
                                                    try {
                                                        Double valueAsDouble = Double.parseDouble(value);
                                                        ExpressionValueNumber exprValueNumber = new ExpressionValueNumber(
                                                                valueAsDouble);
                                                        exprValueNumber.copyBasicExpressionInfo(expression);
                                                        expressions.getExpressions().set(currentModified, exprValueNumber);
                                                        selectExpression = exprValueNumber;
                                                        window.close();
                                                    } catch (NumberFormatException nfe) {
                                                        throw new NotValidExpressionValue("Value '" + value
                                                                + "' is not a number!");
                                                    }
                                                    break;
                                                case DATE:
                                                    try {
                                                        ExpressionValueTimestamp exprValueDate = new ExpressionValueTimestamp(
                                                                value);
                                                        exprValueDate.copyBasicExpressionInfo(expression);
                                                        expressions.getExpressions().set(currentModified, exprValueDate);
                                                        selectExpression = exprValueDate;
                                                        window.close();
                                                    } catch (ParseException e) {
                                                        throw new NotValidExpressionValue("Value '" + value
                                                                + "' is not a date!");
                                                    }
                                                    break;
                                                case POSTAL_CODE:
                                                    ExpressionValuePostalCode exprValuePostCode = new ExpressionValuePostalCode(
                                                            value);
                                                    exprValuePostCode.copyBasicExpressionInfo(expression);
                                                    expressions.getExpressions().set(currentModified, exprValuePostCode);
                                                    selectExpression = exprValuePostCode;
                                                    window.close();
                                                    break;
                                                case MULTI_TEXT:
                                                case TEXT:
                                                    ExpressionValueString exprValueString = new ExpressionValueString(value);
                                                    exprValueString.copyBasicExpressionInfo(expression);
                                                    expressions.getExpressions().set(currentModified, exprValueString);
                                                    selectExpression = exprValueString;
                                                    window.close();
                                                    break;
                                            }
                                            // Update expression
                                            updateExpression();
                                            setSelectedExpression(selectExpression);
                                        } catch (NotValidExpressionValue e1) {
                                            AbcdLogger.errorMessage(this.getClass().getName(), e1);
                                            MessageManager.showError(LanguageCodes.ERROR_INVALID_VALUE);
                                        }
                                    }
                                }
                            });
                            stringInputWindow.showCentered();
                            // For Global
                        } else if (expression instanceof ExpressionValueGlobalVariable) {
                            SelectGlobalConstantsWindow globalWindow = new SelectGlobalConstantsWindow();
                            globalWindow.showCentered();
                            globalWindow.setValue(((ExpressionValueGlobalVariable) expression).getValue());
                            globalWindow.addAcceptActionListener(window -> {
                                GlobalVariable globalVariable = ((SelectGlobalConstantsWindow) window).getValue();
                                if (globalVariable != null) {
                                    ((ExpressionValueGlobalVariable) expression).setVariable(globalVariable);
                                    window.close();
                                    updateExpression();
                                    setSelectedExpression(expression);
                                } else {
                                    MessageManager.showError(ServerTranslate
                                            .translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
                                }
                            });
                            // Form variables.
                        } else if (expression instanceof ExpressionValueCustomVariable) {
                            SelectFormElementVariableWindow variableWindow = new SelectFormElementVariableWindow();
                            variableWindow.showCentered();
                            variableWindow.collapseFrom(Category.class);
                            variableWindow.setValue((ExpressionValueCustomVariable) expression);
                            variableWindow.addAcceptActionListener(window -> {
                                ExpressionValueCustomVariable formReference = ((SelectFormElementVariableWindow) window)
                                        .getValue();
                                if (formReference != null) {
                                    // Update the already existing
                                    // expression.
                                    ((ExpressionValueCustomVariable) expression).setReference(formReference
                                            .getReference());
                                    ((ExpressionValueCustomVariable) expression).setVariable(formReference
                                            .getVariable());
                                    window.close();
                                    updateExpression();
                                    setSelectedExpression(expression);
                                } else {
                                    MessageManager.showError(ServerTranslate
                                            .translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
                                }
                            });
                        } else if (expression instanceof ExpressionValueTreeObjectReference) {
                            final SelectTreeObjectWindow selectElementWindow = new SelectTreeObjectWindow(
                                    UserSessionHandler.getFormController().getForm(), false);
                            selectElementWindow.showCentered();
                            selectElementWindow.collapseFrom(Category.class);
                            selectElementWindow.select(((ExpressionValueTreeObjectReference) expression).getReference());
                            selectElementWindow.addAcceptActionListener(window -> {
                                TreeObject treeObject = selectElementWindow.getSelectedTreeObject();
                                if (treeObject != null) {
                                    ((ExpressionValueTreeObjectReference) expression).setReference(treeObject);
                                    ((ExpressionValueTreeObjectReference) expression).setUnit(null);

                                    // Detect if it is a date question to
                                    // add units
                                    if ((treeObject instanceof Question)
                                            && ((((Question) treeObject).getAnswerFormat()) != null)
                                            && ((Question) treeObject).getAnswerFormat().equals(AnswerFormat.DATE)) {
                                        // Create a window for selecting the
                                        // unit and assign
                                        // it to the expression.
                                        WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
                                                .translate(LanguageCodes.EXPRESSION_DATE_CAPTION));
                                        windowDate.addAcceptActionListener(window1 -> {
                                            ((ExpressionValueTreeObjectReference) expression)
                                                    .setUnit(((WindowSelectDateUnit) window1).getValue());
                                            updateExpression();
                                            setSelectedExpression(expression);
                                            window1.close();
                                        });
                                        windowDate.showCentered();
                                    }
                                }
                                window.close();
                                updateExpression();
                                setSelectedExpression(expression);
                            });
                        } else if (expression instanceof ExpressionValueGenericCustomVariable) {
                            SelectFormGenericVariablesWindow genericVariableWindow = new SelectFormGenericVariablesWindow();
                            genericVariableWindow.showCentered();
                            genericVariableWindow.setvalue((ExpressionValueGenericCustomVariable) expression);
                            genericVariableWindow.addAcceptActionListener(window -> {
                                ExpressionValueGenericCustomVariable formReference = ((SelectFormGenericVariablesWindow) window)
                                        .getValue();
                                if (formReference != null) {
                                    // Update the already existing
                                    // expression.
                                    ((ExpressionValueGenericCustomVariable) expression).setType(formReference
                                            .getType());
                                    ((ExpressionValueGenericCustomVariable) expression).setVariable(formReference
                                            .getVariable());
                                    window.close();
                                    updateExpression();
                                    setSelectedExpression(expression);
                                } else {
                                    MessageManager.showError(ServerTranslate
                                            .translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
                                }
                            });
                        }
                    }
                }
            }
        });

        expressionOfElement.put(expressionElement, expression);
        lineLayout.addComponent(expressionElement);
        lineLayout.setExpandRatio(expressionElement, 0);
        setSelectedExpression(expression);
    }

    private void setSelectedExpression(Expression expression) {
        selectedExpression = expression;
        updateExpressionSelectionStyles();
    }

    /**
     * The selected expression is white.
     */
    protected void updateExpressionSelectionStyles() {
        for (int i = 0; i < rootLayout.getComponentCount(); i++) {
            if (rootLayout.getComponent(i) instanceof HorizontalLayout) {
                HorizontalLayout lineLayout = (HorizontalLayout) rootLayout.getComponent(i);
                for (int j = 0; j < lineLayout.getComponentCount(); j++) {
                    if (lineLayout.getComponent(j) instanceof ExpressionElement) {
                        if (!expressionOfElement.get(lineLayout.getComponent(j)).isEditable()) {
                            lineLayout.getComponent(j).addStyleName("expression-disabled");
                        } else if (isFocused()
                                && expressionOfElement.get(lineLayout.getComponent(j)).getComparationId().equals(selectedExpression.getComparationId())) {
                            lineLayout.getComponent(j).addStyleName("expression-selected");
                        } else {
                            lineLayout.getComponent(j).removeStyleName("expression-selected");
                        }
                    }
                }
            }
        }
    }

    public Expression getSelectedExpression() {
        return selectedExpression;
    }

    protected void selectNextExpression() {
        if (isFocused() && (getSelectedExpression() != null)) {
            // Select next expression.
            int index = expressions.getExpressions().indexOf(getSelectedExpression()) + 1;
            selectExpressionByIndex(index);
        }
    }

    protected void selectPreviousExpression() {
        if (isFocused() && (getSelectedExpression() != null)) {
            // Select next expression.
            int index = expressions.getExpressions().indexOf(getSelectedExpression()) - 1;
            selectExpressionByIndex(index);
        }
    }

    private void selectExpressionByIndex(int index) {
        Expression selected = null;
        if (index >= 0) {
            if (index < expressions.getExpressions().size()) {
                selected = expressions.getExpressions().get(index);
            } else if (!expressions.getExpressions().isEmpty()) {
                selected = expressions.getExpressions().get(expressions.getExpressions().size() - 1);
            }
        } else {
            selectExpressionByIndex(0);
        }
        if (selected != null) {
            setSelectedExpression(selected);
        }
    }

    public boolean removeSelectedExpression() {
        if (isFocused() && (getSelectedExpression() != null) && getSelectedExpression().isEditable()) {
            int index = expressions.getExpressions().indexOf(getSelectedExpression());
            expressions.removeExpression(getSelectedExpression());
            updateExpression();
            selectExpressionByIndex(index);
            return true;
        }
        return false;
    }

    /**
     * Clear all editable expressions.
     */
    public void clearExpression() {
        Iterator<Expression> expressionIterator = new ArrayList<>(expressions.getExpressions()).iterator();
        while (expressionIterator.hasNext()) {
            Expression expression = expressionIterator.next();
            if (expression.isEditable()) {
                expressions.removeExpression(expression);
            }
        }
        updateExpression();
    }

    /**
     * Adds a new element in the position of the selected element. Depending of the element, can be inserted after or
     * before.
     *
     * @param newElement
     */
    public void addElementToSelected(Expression newElement) {
        // Checks if there is at least one expression
        if (expressions != null) {
            int index = expressions.getExpressions().indexOf(getSelectedExpression()) + 1;
            if ((index >= 0) && (index < expressions.getExpressions().size())) {
                expressions.addExpression(index, newElement);
            } else {
                expressions.addExpression(newElement);
            }
            updateExpression();
            setSelectedExpression(newElement);
        } else {
            MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.WARNING_EXPRESSION_TABLE_EMPTY);
        }
    }

    public ExpressionChain getExpressions() {
        return expressions;
    }

    protected void updateEvaluator() {
        try {
            ExpressionValidator.validateActions(getExpressions());
            evaluatorOutput.setStyleName("expression-valid");
            evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_VALID));
        } catch (Exception e) {
            AbcdLogger.debug(ExpressionViewer.class.getName(), e.getMessage());
            evaluatorOutput.setStyleName("expression-invalid");
            evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_INVALID));
        }
    }

    private HorizontalLayout createEvaluatorLayout() {
        HorizontalLayout checkerLayout = new HorizontalLayout();
        checkerLayout.setMargin(false);
        checkerLayout.setSpacing(false);
        checkerLayout.setSizeFull();

        evaluatorOutput = new Label();
        evaluatorOutput.setSizeUndefined();
        checkerLayout.addComponent(evaluatorOutput);
        checkerLayout.setComponentAlignment(evaluatorOutput, Alignment.TOP_RIGHT);

        return checkerLayout;
    }

    private void addClickController() {
        final ExpressionViewer thisWindow = this;
        if (rootLayout != null) {
            rootLayout.addLayoutClickListener(new LayoutClickListener() {
                private static final long serialVersionUID = -4305606865801828692L;

                @Override
                public void layoutClick(LayoutClickEvent event) {
                    for (LayoutClickedListener listener : clickedListeners) {
                        listener.clickedAction(thisWindow);
                    }
                }
            });
        }
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public void addLayoutClickedListener(LayoutClickedListener listener) {
        clickedListeners.add(listener);
    }

    public void removeLayoutClickedListener(LayoutClickedListener listener) {
        clickedListeners.remove(listener);
    }

    public VerticalLayout getRootLayout() {
        return rootLayout;
    }

    protected Label getEvaluatorOutput() {
        return evaluatorOutput;
    }
}
