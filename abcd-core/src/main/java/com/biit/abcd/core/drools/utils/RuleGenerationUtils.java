package com.biit.abcd.core.drools.utils;

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

import com.biit.abcd.core.drools.json.globalvariables.AbcdGlobalVariablesToJson;
import com.biit.abcd.core.drools.rules.DroolsRuleGroup;
import com.biit.abcd.core.drools.rules.DroolsRuleGroupEndRule;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.drools.global.variables.DroolsGlobalVariable;
import com.biit.drools.global.variables.json.DroolsGlobalVariablesFromJson;
import com.biit.form.entity.TreeObject;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This class gathers method that ease the drools rule definition and simplifies
 * the 'DroolsParser' class code.
 */
public class RuleGenerationUtils {

    public static String getRuleName(String name) {
        return name + "_" + getUniqueId();
    }

    public static String getRuleName(String name, ExpressionChain extraConditions) {
        if (extraConditions == null) {
            return "rule \"" + name + "_" + getUniqueId() + "\"\n";
        } else {
            return "rule \"" + name + "_" + extraConditions.getExpression().replaceAll("[^a-zA-Z0-9_]", "_") + "_"
                    + getUniqueId() + "\"\n";
        }
    }

    public static String getWhenRuleString() {
        return "when\n";
    }

    public static String getAttributes() {
        return "";
    }

    public static String getThenRuleString() {
        return "then\n";
    }

    public static String getEndRuleString() {
        return "end\n\n";
    }

    private static String getUniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getGroupEndRuleExtraCondition(DroolsRuleGroupEndRule rule) {
        if (rule.getName().startsWith("rule \"")) {
            return "\tnot(FiredRule(getRuleName() == '"
                    + rule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "')) and\n";
        } else {
            return "\tnot(FiredRule( getRuleName() == '" + rule.getName() + "')) and\n";
        }
    }

    public static String getGroupRuleActions(DroolsRuleGroup rule) {
        String groupAction = "";
        if (rule.getName().startsWith("rule \"")) {
            groupAction = "\tDroolsRulesLogger.debug(\"RuleFired\", \"Rule "
                    + rule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + " fired\");\n";
            groupAction += "\tinsert(new FiredRule(\""
                    + rule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n";
        } else {
            groupAction = "\tDroolsRulesLogger.debug(\"RuleFired\", \"Rule " + rule.getName() + " fired\");\n";
            groupAction += "\tinsert(new FiredRule(\"" + rule.getName() + "\"));\n";
        }
        return groupAction;
    }

    /**
     * Due to the independent parsing of the conditions of the rule, sometimes
     * the algorithm generates repeated rules <br>
     * This method the lines that are equals in the rule<br>
     * It should be used before sending the rules to the engine <br>
     *
     * @param ruleCore
     * @return the rules without duplicated
     */
    public static String removeDuplicateLines(String ruleCore) {
        StringBuilder result = new StringBuilder();
        // Parse the resulting rule to delete lines that are equal
        String[] auxSplit = ruleCore.split("\n");
        for (int i = 0; i < auxSplit.length; i++) {
            if ((auxSplit[i] != null)) {
                String compareTo = auxSplit[i];
                for (int j = i + 1; j < auxSplit.length; j++) {
                    if ((auxSplit[j] != null) && !auxSplit[i].equals("\tand") && !auxSplit[i].equals("\t(")
                            && !auxSplit[i].equals("\t)") && !auxSplit[i].trim().equals("(")
                            && !auxSplit[i].trim().equals(")") && !auxSplit[i].equals("\tor")
                            && !auxSplit[i].equals("\tnot(")) {
                        if (auxSplit[j].equals(compareTo)) {
                            auxSplit[j] = null;
                        }
                    }
                }
                // In the last row, remove the ands (if any)
                if ((i < (auxSplit.length - 1)) && (auxSplit[i + 1] != null) && auxSplit[i + 1].equals("then")
                        && compareTo.endsWith("and")) {
                    result.append(compareTo, 0, compareTo.length() - 3).append("\n");
                } else {
                    result.append(compareTo).append("\n");
                }
            }
        }
        return result.toString();
    }

    public static String checkForDuplicatedVariables(String ruleCore) {
        StringBuilder cleanedResults = new StringBuilder();
        // boolean insideRHS = false;
        HashSet<String> variablesAssigned = new HashSet<>();
        String[] lines = ruleCore.split("\n");
        // int sameVariableIndex = 0;
        for (String line : lines) {
            // if (line.equals("then")) {
            // insideRHS = true;
            // }
            String[] auxRuleArray = line.split(" : ");
            if (auxRuleArray.length > 1) {
                if (variablesAssigned.contains(auxRuleArray[0].replace("(\t", ""))) {
                    auxRuleArray[0] = "\t";
                    // if (insideRHS) {
                    // auxRuleArray[0] = auxRuleArray[0] + "0";
                    // } else {
                    // auxRuleArray[0] = auxRuleArray[0] + sameVariableIndex;
                    // sameVariableIndex++;
                    // }
                } else {
                    variablesAssigned.add(auxRuleArray[0].replace("(\t", ""));
                }

                if (!auxRuleArray[0].equals("\t")) {
                    cleanedResults.append(auxRuleArray[0]).append(" : ");
                } else {
                    cleanedResults.append(auxRuleArray[0]);
                }
                for (int i = 1; i < auxRuleArray.length; i++) {
                    if (i == (auxRuleArray.length - 1)) {
                        cleanedResults.append(auxRuleArray[i]).append("\n");
                    } else {
                        cleanedResults.append(auxRuleArray[i]).append(" : ");
                    }
                }
            } else {
                cleanedResults.append(line).append("\n");
            }
        }
        return cleanedResults.toString();
    }

    public static String removeExtraParenthesis(String ruleCore) {
        StringBuilder cleanedResults = new StringBuilder();
        String[] lines = ruleCore.split("\n");
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex];
            if (line.equals(")")) {
                // If the previous line was also a parenthesis remove them
                if (lineIndex > 0 && lines[lineIndex - 1].equals("(")) {
                    lines[lineIndex - 1] = null;
                    lines[lineIndex] = null;
                }
            }
        }
        for (String line : lines) {
            if (line != null) {
                cleanedResults.append(line).append("\n");
            }
        }
        return cleanedResults.toString();
    }

    public static String addThenIfNeeded(String ruleCore) {
        StringBuilder cleanedResults = new StringBuilder();
        boolean thenClause = false;
        String[] lines = ruleCore.split("\n");
        for (String line : lines) {
            if (line.equals("then")) {
                thenClause = true;
                break;
            }
        }
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            if ((lineIndex == (lines.length - 1)) && (!thenClause)) {
                cleanedResults.append(getThenRuleString());
            }
            cleanedResults.append(lines[lineIndex]).append("\n");
        }
        return cleanedResults.toString();
    }

    public static String removeLastNLines(String ruleCore, int n) {
        StringBuilder cleanedResults = new StringBuilder();
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < (lines.length - n); i++) {
            cleanedResults.append(lines[i]).append("\n");
        }
        return cleanedResults.toString();
    }

    public static Integer getNumberOfLines(String ruleCore) {
        String[] lines = ruleCore.split("\n");
        return lines.length;
    }

    public static String replaceLine(String ruleCore, String lineToSet, int lineNumber) {
        StringBuilder cleanedResults = new StringBuilder();
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == lineNumber) {
                cleanedResults.append(lineToSet).append("\n");
            } else {
                cleanedResults.append(lines[i]).append("\n");
            }
        }
        return cleanedResults.toString();
    }

    public static String replaceLastLine(String ruleCore, String lineToSet) {
        StringBuilder cleanedResults = new StringBuilder();
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == (lines.length - 1)) {
                cleanedResults.append(lineToSet).append("\n");
            } else {
                cleanedResults.append(lines[i]).append("\n");
            }
        }
        return cleanedResults.toString();
    }

    public static String getLine(String ruleCore, int lineNumber) {
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == lineNumber) {
                return lines[i];
            }
        }
        return "";
    }

    public static String getLastLine(String ruleCore) {
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == (lines.length - 1)) {
                return lines[i];
            }
        }
        return "";
    }

    public static String fixOrCondition(String ruleCore) {
        StringBuilder cleanedResults = new StringBuilder();
        int finishCondition = -1;
        HashSet<Integer> skipLines = new HashSet<>();

        String[] lines = ruleCore.split("\n");
        label:
        for (int i = 0; i < lines.length; i++) {
            switch (lines[i]) {
                case "\tor":
                    skipLines.add(i - 1);
                    skipLines.add(i);
                    skipLines.add(i + 1);
                    break;
                case "\tnot(":
                    skipLines.add(i);
                    break;
                case "then":
                    skipLines.add(i - 1);
                    finishCondition = i;
                    break label;
            }
        }
        for (int i = 0; i < lines.length; i++) {
            // In the action part, stop the algorithm
            if (finishCondition == i) {
                break;
            }
            // If the previous line belongs to an OR but it has more lines after
            // it, add an and
            if (skipLines.contains(i - 1) && !skipLines.contains(i) && !lines[i - 1].equals("\tnot(")) {
                lines[i - 1] = lines[i - 1] + " and";
                lines[i] = lines[i] + " and";
            }// If the line don't belong to an or, add an and
            else if (!skipLines.contains(i)) {
                lines[i] = lines[i] + " and";
            }
        }
        for (String line : lines) {
            cleanedResults.append(line).append("\n");
        }

        return cleanedResults.toString();
    }

    public static String addAndToMultipleConditionsAction(String ruleCore) {
        StringBuilder cleanedResults = new StringBuilder();
        String[] lines = ruleCore.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equals("then")) {
                if (i > 0) {
                    // Remove the "and"
                    lines[i - 1] = lines[i - 1].substring(0, lines[i - 1].length() - 3);
                }
                break;
            } else {
                lines[i] = lines[i].concat(" and");
            }
        }
        for (String line : lines) {
            cleanedResults.append(line).append("\n");
        }
        return cleanedResults.toString();
    }

    public static String returnSimpleTreeObjectNameFunction(TreeObject treeObject) {
        return "getText() == '" + treeObject.getName();
    }

    public static String addFinalCommentsIfNeeded(TreeObject treeObject) {
        return "";
    }

    public static String createRuleName(String ruleName, ExpressionChain extraConditions, String valueToAppend) {
        if (ruleName != null && ruleName.startsWith("rule \"")) {
            if (valueToAppend != null) {
                ruleName = "rule \"" + ruleName.split(" ")[1].replace("\n", "").replace("\"", "").concat(valueToAppend)
                        + "\"\n";
            }
        } else {
            ruleName = getRuleName(ruleName, extraConditions);
        }
        return ruleName;
    }

    public static String createRuleName(Rule rule, ExpressionChain extraConditions, String valueToAppend) {
        return createRuleName(rule.getName(), extraConditions, valueToAppend);
    }

    public static String createRuleName(Rule rule, ExpressionChain extraConditions) {
        return createRuleName(rule.getName(), extraConditions, null);
    }

    public static String createRuleName(Rule rule, String valueToAppend) {
        return createRuleName(rule, null, valueToAppend);
    }

    public static String createRuleName(Rule rule) {
        return createRuleName(rule.getName(), null, null);
    }

    /**
     * Return the rule name without the "rule " part
     *
     * @param ruleName
     */
    public static String getCleanRuleName(String ruleName) {
        if (ruleName.startsWith("rule \"")) {
            return ruleName.split(" ")[1].replace("\n", "").replace("\"", "");
        } else {
            return ruleName;
        }
    }

    /**
     * Looks for the class that has an enum equals to the one passed in the
     * parameter
     *
     * @param expressionChain
     * @param classToSearch
     * @param enumType
     * @return if exists.
     */
    public static boolean searchClassInExpressionChain(ExpressionChain expressionChain, Class<?> classToSearch,
                                                       Object enumType) {
        for (Expression expression : expressionChain.getExpressions()) {
            if (expression instanceof ExpressionChain) {
                searchClassInExpressionChain((ExpressionChain) expression, classToSearch, enumType);
            } else {
                if (classToSearch.isInstance(expression)) {
                    if (expression.getValue().equals(enumType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the type of answer for the question in the parameter
     *
     * @return the type as string.
     */
    public static String getTreeObjectAnswerType(TreeObject treeObject) {
        if (treeObject instanceof Question) {
            Question question = (Question) treeObject;
            switch (question.getAnswerType()) {
                case RADIO:
                case MULTI_CHECKBOX:
                    return AnswerFormat.TEXT.toString();
                case INPUT:
                    return question.getAnswerFormat().toString();
            }
            return "";
        }
        return "";
    }

    /**
     * Takes the table rule conditions and returns a expression chain without
     * expression chains inside
     *
     * @param expressionChain the expression.
     * @return the expression chain filtered
     */
    public static ExpressionChain flattenExpressionChain(ExpressionChain expressionChain) {
        ExpressionChain flattenedExpressionChain = new ExpressionChain();
        for (Expression expression : expressionChain.getExpressions()) {
            if (expression instanceof ExpressionChain) {
                ExpressionChain auxExpressionFlattened = flattenExpressionChain((ExpressionChain) expression);
                for (Expression insideExpression : auxExpressionFlattened.getExpressions()) {
                    flattenedExpressionChain.addExpression(insideExpression);
                }
            } else {
                flattenedExpressionChain.addExpression(expression);
            }
        }
        return flattenedExpressionChain;
    }

    /**
     * If there is a NOT function used, we add the required left parenthesis
     *
     * @param expressionChain the expression.
     */
    public static void fixNotConditions(ExpressionChain expressionChain) {
        for (int index = 0; index < expressionChain.getExpressions().size(); index++) {
            Expression expression = expressionChain.getExpressions().get(index);
            if ((expression instanceof ExpressionFunction)
                    && (((ExpressionFunction) expression).getValue().equals(AvailableFunction.NOT))) {

                if ((expressionChain.getExpressions().size() > index + 1)
                        && (expressionChain.getExpressions().get(index + 1) instanceof ExpressionSymbol)
                        && (((ExpressionSymbol) expressionChain.getExpressions().get(index + 1)).getValue()
                        .equals(AvailableSymbol.LEFT_BRACKET))) {
                } else {
                    expressionChain.getExpressions().add(index + 1, new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
                }

            }
        }
    }

    public static Set<ExpressionValueCustomVariable> lookForCustomVariablesInDiagram(Diagram diagram) {
        Set<ExpressionValueCustomVariable> customVariables = new HashSet<>();
        Set<DiagramObject> diagramNodes = diagram.getDiagramObjects();
        for (DiagramObject diagramNode : diagramNodes) {
            if (diagramNode instanceof DiagramSource) {
                customVariables.addAll(lookForCustomVariablesInDiagramNode((DiagramElement) diagramNode));
            }
        }
        //Remove duplicated variables. As hashcode and equals is not working here as comparationId is different.
        final HashSet<Integer> seen = new HashSet<>();
        customVariables.removeIf(c -> !seen.add(Objects.hash(c.getVariableId(), c.getReferenceId())));
        return customVariables;
    }

    private static Set<ExpressionValueCustomVariable> lookForCustomVariablesInDiagramNode(DiagramElement diagramNode) {
        Set<ExpressionValueCustomVariable> customVariablesList = new HashSet<>();
        switch (diagramNode.getType()) {
            case TABLE:
                DiagramTable tableNode = (DiagramTable) diagramNode;
                if (tableNode.getTable() != null) {
                    for (TableRuleRow tableRuleRow : tableNode.getTable().getRules()) {
                        if (tableRuleRow.getConditions() != null) {
                            customVariablesList
                                    .addAll(lookForCustomVariableInExpressionChain(tableRuleRow.getConditions()));
                        }
                        if (tableRuleRow.getActions() != null) {
                            customVariablesList.addAll(lookForCustomVariableInExpressionChain(tableRuleRow.getActions()));
                        }
                    }
                }
                break;
            case RULE:
                DiagramRule ruleNode = (DiagramRule) diagramNode;
                if (ruleNode.getRule() != null && ruleNode.getRule().getConditions() != null) {
                    customVariablesList.addAll(lookForCustomVariableInExpressionChain(ruleNode.getRule().getConditions()));
                }
                if (ruleNode.getRule() != null && ruleNode.getRule().getActions() != null) {
                    customVariablesList.addAll(lookForCustomVariableInExpressionChain(ruleNode.getRule().getActions()));
                }
                break;
            case CALCULATION:
            case SINK:
                DiagramExpression expressionNode = (DiagramExpression) diagramNode;
                if (expressionNode.getExpression() != null) {
                    customVariablesList.addAll(lookForCustomVariableInExpressionChain(expressionNode.getExpression()));
                }
                break;
            case DIAGRAM_CHILD:
                DiagramChild childDiagramNode = (DiagramChild) diagramNode;
                if (childDiagramNode.getDiagram() != null) {
                    customVariablesList.addAll(lookForCustomVariablesInDiagram(childDiagramNode.getDiagram()));
                }
                break;
            case FORK:
                DiagramFork forkNode = (DiagramFork) diagramNode;
                if (forkNode.getReference() instanceof ExpressionValueCustomVariable) {
                    customVariablesList.add((ExpressionValueCustomVariable) forkNode.getReference());
                }
                for (DiagramLink outLink : forkNode.getOutgoingLinks()) {
                    customVariablesList.addAll(lookForCustomVariableInExpressionChain(outLink
                            .getExpressionChain()));
                }
                break;
            default:
                break;
        }
        for (DiagramLink outLink : diagramNode.getOutgoingLinks()) {
            customVariablesList.addAll(lookForCustomVariablesInDiagramNode(outLink.getTargetElement()));
        }
        return customVariablesList;
    }

    private static Set<ExpressionValueCustomVariable> lookForCustomVariableInExpressionChain(
            ExpressionChain expressionChain) {
        Set<ExpressionValueCustomVariable> customVariablesList = new HashSet<>();
        for (Expression expression : expressionChain.getExpressions()) {
            if (expression instanceof ExpressionChain) {
                lookForCustomVariableInExpressionChain((ExpressionChain) expression);

            } else if (expression instanceof ExpressionValueCustomVariable) {
                customVariablesList.add((ExpressionValueCustomVariable) expression);

            } else if (expression instanceof ExpressionValueGenericCustomVariable) {

                ExpressionValueGenericCustomVariable genericCustomVariable = (ExpressionValueGenericCustomVariable) expression;

                CustomVariable customVariable = genericCustomVariable.getVariable();
                Form form = customVariable.getForm();
                switch (customVariable.getScope()) {
                    case FORM:
                        customVariablesList.add(new ExpressionValueCustomVariable(form, customVariable));
                        break;
                    case CATEGORY:
                    case GROUP:
                    case QUESTION:
                        List<? extends TreeObject> treeObjects = form.getAll(customVariable.getScope().getScope());
                        for (TreeObject treeObject : treeObjects) {
                            customVariablesList.add(new ExpressionValueCustomVariable(treeObject, customVariable));
                        }
                        break;
                }
            }
        }
        return customVariablesList;
    }

    public static List<DroolsGlobalVariable> convertGlobalVariablesToDroolsGlobalVariables(
            List<GlobalVariable> globalVariables) {
        // We use the json serializer/deserializer to transform the variables
        return DroolsGlobalVariablesFromJson.fromJson(AbcdGlobalVariablesToJson.toJson(globalVariables));
    }
}
