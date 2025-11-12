package com.biit.abcd.core;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.core.providers.FormProvider;
import com.biit.abcd.core.utils.TableRuleUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.abcd.persistence.utils.FormComparator;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.InvalidNameException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormController {
    private IUser<Long> user;
    private Form form;
    private TreeObject lastAccessTreeObject;
    private Diagram lastAccessDiagram;
    private ExpressionChain lastAccessExpression;
    private TableRule lastAccessTable;
    private Rule lastAccessRule;
    private List<TableRuleRow> copiedRows;
    private Form originalForm;

    private FormProvider formProvider;
    private Set<Object> collapsedStatus;

    public FormController(IUser<Long> user, SpringContextHelper helper) {
        this.setUser(user);
        this.formProvider = (FormProvider) helper.getBean("formProvider");
    }

    public void setUser(IUser<Long> user) {
        this.user = user;
    }

    public void save() throws DuplicatedVariableException, UnexpectedDatabaseException,
            ElementCannotBePersistedException, InvalidNameException {
        checkVariables();
        if (getForm() != null) {
            getForm().setUpdatedBy(getUser());
            getForm().setUpdateTime();

            this.form = formProvider.saveForm(getForm());

            try {
                originalForm = (Form) getForm().generateCopy(true, true);
                originalForm.resetIds();
            } catch (NotValidStorableObjectException | CharacterNotAllowedException e) {
            }

            updateLastAccessedReferences();
        }
    }

    private void updateLastAccessedReferences() {
        if (lastAccessTreeObject != null) {
            lastAccessTreeObject = getForm().findByComparationId(lastAccessTreeObject.getComparationId());
        }
        if (lastAccessDiagram != null) {
            for (Diagram diagram : getForm().getDiagrams()) {
                if (diagram.getComparationId().equals(lastAccessDiagram.getComparationId())) {
                    lastAccessDiagram = diagram;
                    break;
                }
            }
        }
        if (lastAccessExpression != null) {
            for (ExpressionChain expression : getForm().getExpressionChains()) {
                if (expression.getComparationId().equals(lastAccessExpression.getComparationId())) {
                    lastAccessExpression = expression;
                    break;
                }
            }
        }
        if (lastAccessTable != null) {
            for (TableRule table : getForm().getTableRules()) {
                if (table.getComparationId().equals(lastAccessTable.getComparationId())) {
                    lastAccessTable = table;
                    break;
                }
            }
        }
        if (lastAccessRule != null) {
            for (Rule rule : getForm().getRules()) {
                if (rule.getComparationId().equals(lastAccessRule.getComparationId())) {
                    lastAccessRule = rule;
                    break;
                }
            }
        }
    }

    public void checkVariables() throws DuplicatedVariableException, InvalidNameException {
        Set<CustomVariable> customVariables = getForm().getCustomVariables();
        List<CustomVariable> customVariablesList = new ArrayList<>(customVariables);
        for (int i = 0; i < customVariablesList.size(); i++) {
            if (customVariablesList.get(i).getName() == null || customVariablesList.get(i).getName().length() == 0) {
                throw new InvalidNameException("Name value cannot be empty");
            }
            for (int j = i + 1; j < customVariablesList.size(); j++) {
                if (customVariablesList.get(i).hasSameNameAndScope(customVariablesList.get(j))) {
                    throw new DuplicatedVariableException("Duplicated variable in form variables.");
                }
            }
        }
    }

    public void updateForm(Form form, String label) {
        try {
            if (!form.getLabel().equals(label)) {
                logInfoStart("updateForm", form, label);
            }
            form.setLabel(label);
            form.setUpdatedBy(getUser());
            form.setUpdateTime();
        } catch (FieldTooLongException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    private void logInfoStart(String functionName, Object... parameters) {
        AbcdLogger
                .info(FormController.class.getName(), getUserInfo() + " " + getFunctionInfo(functionName, parameters));
    }

    protected String getUserInfo() {
        String userInfo = new String("IUser<Long>: ");
        if (getUser() == null) {
            return userInfo + "NO USER";
        } else {
            return userInfo + getUser().getEmailAddress();
        }
    }

    protected String getFunctionInfo(String functionName, Object... parameters) {
        String functionInfo = new String(functionName + "(");
        int i = 0;
        for (Object parameter : parameters) {
            String parameterString = new String();
            if (i > 0) {
                parameterString += ", ";
            }
            parameterString += "arg" + i + ": '" + parameter + "'";
            functionInfo += parameterString;
            i++;
        }
        functionInfo += ")";
        return functionInfo;
    }

    public void remove() throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
        if (getForm() != null) {
            formProvider.deleteForm(getForm());
        }
    }

    public void remove(TreeObject treeObject) throws DependencyExistException, ElementIsReadOnly {
        if ((getForm() != null) && (treeObject != null) && (treeObject.getParent() != null)) {
            treeObject.remove();
        }
    }

    public void moveUp(TreeObject treeObject) throws ChildrenNotFoundException {
        if (getForm() != null) {
            if ((treeObject.getParent() != null) && (treeObject.getParent().getChildren().indexOf(treeObject) > 0)) {
                treeObject.getParent().switchChildren(treeObject.getParent().getChildren().indexOf(treeObject),
                        treeObject.getParent().getChildren().indexOf(treeObject) - 1, this.user);
            }
        }
    }

    public void moveDown(TreeObject object) throws ChildrenNotFoundException {
        if (getForm() != null) {
            if ((object.getParent() != null)
                    && (object.getParent().getChildren().indexOf(object) < (object.getParent().getChildren().size() - 1))) {
                object.getParent().switchChildren(object.getParent().getChildren().indexOf(object),
                        object.getParent().getChildren().indexOf(object) + 1, this.user);
            }
        }
    }

    public Form getForm() {
        return form;
    }

    public void clearUnsavedChangesChecker() {
        originalForm = null;
    }

    public void setForm(Form form) {
        this.form = form;
        this.collapsedStatus = null;
        try {
            if (form == null) {
                this.originalForm = null;
            } else {
                this.originalForm = (Form) form.generateCopy(true, true);
            }
        } catch (NotValidStorableObjectException | CharacterNotAllowedException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            this.form = null;
            originalForm = null;
        }
        clearWorkVariables();
    }

    public void checkUnsavedChanges() throws TreeObjectNotEqualsException, StorableObjectNotEqualsException,
            FormNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException,
            CustomVariableNotEqualsException, ExpressionNotEqualsException, TableRuleNotEqualsException,
            RuleNotEqualsException, DiagramNotEqualsException, DiagramObjectNotEqualsException, NodeNotEqualsException,
            SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException,
            GlobalVariableNotEqualsException, VariableDataNotEqualsException {
        if (form != null && originalForm != null) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                new FormComparator(false).compare(form, originalForm);
            } finally {
                stopWatch.stop();
                AbcdLogger.timeLog(stopWatch.getTotalTimeMillis(),
                        this.getClass().getName() + ".checkUnsavedChanges()", null);
            }
        }
    }

    public IUser<Long> getUser() {
        return this.user;
    }

    public TreeObject getLastAccessTreeObject() {
        return this.lastAccessTreeObject;
    }

    public void setLastAccessTreeObject(TreeObject lastAccessTreeObject) {
        this.lastAccessTreeObject = lastAccessTreeObject;
    }

    public Diagram getLastAccessDiagram() {
        return this.lastAccessDiagram;
    }

    public void setLastAccessDiagram(Diagram lastAccessDiagram) {
        this.lastAccessDiagram = lastAccessDiagram;
    }

    public ExpressionChain getLastAccessExpression() {
        return this.lastAccessExpression;
    }

    public void setLastAccessExpression(ExpressionChain lastAccessExpression) {
        this.lastAccessExpression = lastAccessExpression;
    }

    public TableRule getLastAccessTable() {
        return this.lastAccessTable;
    }

    public void setLastAccessTable(TableRule lastAccessTable) {
        this.lastAccessTable = lastAccessTable;
    }

    public Rule getLastAccessRule() {
        return this.lastAccessRule;
    }

    public void setLastAccessRule(Rule lastAccessRule) {
        this.lastAccessRule = lastAccessRule;
    }

    /**
     * Gets rules with treeObject as the common element for all the references
     * in the rule.
     *
     * @param treeObject
     * @return a set of rules
     */
    public Set<Rule> getRulesAssignedToTreeObject(TreeObject treeObject) {
        Set<Rule> assignedRules = new HashSet<>();

        Set<Rule> rules = getForm().getRules();
        for (Rule rule : rules) {
            if (rule.isAssignedTo(treeObject)) {
                assignedRules.add(rule);
            }
        }
        return assignedRules;
    }

    /**
     * Gets expressionChains that reference to a particular element.
     *
     * @param element
     * @return a set with the expressions
     */
    public Set<ExpressionChain> getFormExpressionChainsAssignedToTreeObject(TreeObject element) {
        Set<ExpressionChain> expressionChains = new HashSet<>();

        Set<ExpressionChain> expressions = getForm().getExpressionChains();
        for (ExpressionChain expression : expressions) {
            if (expression.isAssignedTo(element)) {
                expressionChains.add(expression);
            }
        }
        return expressionChains;
    }

    public void copyTableRuleRows(final TableRule origin, Collection<TableRuleRow> rowsToCopy) {
        copiedRows = TableRuleUtils.copyTableRuleRows(origin, rowsToCopy);
    }

    public void pasteTableRuleRowsAsNew(TableRule selectedTableRule) {
        TableRuleUtils.pasteTableRuleRows(selectedTableRule, copiedRows);
    }

    public int rowsToCopy() {
        if (copiedRows == null) {
            return 0;
        }
        return copiedRows.size();
    }

    private void clearWorkVariables() {
        this.lastAccessTreeObject = null;
        this.lastAccessDiagram = null;
        this.lastAccessExpression = null;
        this.lastAccessTable = null;
        this.lastAccessRule = null;
        this.copiedRows = null;
        this.collapsedStatus = null;
    }

    public Set<Object> getCollapsedStatus() {
        return collapsedStatus;
    }

    public void setCollapsedStatus(Set<Object> collapsedStatus) {
        this.collapsedStatus = collapsedStatus;
    }
}
