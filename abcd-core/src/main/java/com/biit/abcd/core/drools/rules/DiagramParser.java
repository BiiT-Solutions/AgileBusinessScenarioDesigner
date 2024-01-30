package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.PluginInvocationException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.core.drools.rules.validators.RuleChecker;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.drools.engine.DroolsHelper;
import com.biit.form.entity.TreeObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiagramParser {

    private DroolsHelper droolsHelper;

    public DiagramParser(DroolsHelper droolsHelper) {
        setDroolsHelper(droolsHelper);
    }

    /**
     * Method called to convert the expressions to drools rules.<br>
     * Gathers all the exceptions generated and creates a new exception that
     * represents all of them.
     *
     * @param diagram The diagram to be parsed
     * @return The string with all the rules generated
     * @throws DroolsRuleGenerationException
     * @throws PrattParserException
     * @throws DroolsRuleCreationException
     * @throws PluginInvocationException
     * @throws DateComparisonNotPossibleException
     * @throws BetweenFunctionInvalidException
     * @throws NullExpressionValueException
     * @throws NullCustomVariableException
     * @throws TreeObjectParentNotValidException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws NullTreeObjectException
     * @throws ExpressionInvalidException
     * @throws NotCompatibleTypeException
     * @throws RuleNotImplementedException
     * @throws ActionNotImplementedException
     * @throws InvalidRuleException
     * @throws InvalidExpressionException
     */
    public String getDroolsRulesAsText(Diagram diagram) throws RuleNotImplementedException, NotCompatibleTypeException,
            ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
            NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException, DateComparisonNotPossibleException,
            PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidRuleException, ActionNotImplementedException,
            InvalidExpressionException {
        final List<DroolsRule> newRules = parse(diagram, null);
        return DroolsParser.createDroolsRule(newRules, getDroolsHelper());
    }

    private List<DroolsRule> parse(Diagram diagram, ExpressionChain extraConditions) throws ExpressionInvalidException, InvalidRuleException,
            RuleNotImplementedException, ActionNotImplementedException, NotCompatibleTypeException, PrattParserException, InvalidExpressionException {
        return parse(diagram, extraConditions, Salience.DIAGRAM_STARTING_SALIENCE);
    }

    private List<DroolsRule> parse(Diagram diagram, ExpressionChain extraConditions, Integer salience) throws ExpressionInvalidException, InvalidRuleException,
            RuleNotImplementedException, ActionNotImplementedException, NotCompatibleTypeException, PrattParserException, InvalidExpressionException {
        List<DroolsRule> newRules = new ArrayList<>();
        Set<DiagramObject> diagramNodes = diagram.getDiagramObjects();
        int diagramSalience = salience != null ? salience - 100 : 0;
        for (DiagramObject diagramNode : diagramNodes) {
            diagramSalience--;
            // Start the algorithm for each diagram source defined in the main diagram.
            if (diagramNode instanceof DiagramSource) {
                parseDiagramElement((DiagramElement) diagramNode, extraConditions, newRules, diagramSalience);
            }
        }
        return newRules;
    }

    /**
     * Parses the nodes of the diagram using a depth search algorithm
     *
     * @param node the node being parsed
     * @throws ExpressionInvalidException
     * @throws InvalidRuleException
     * @throws RuleNotImplementedException
     * @throws ActionNotImplementedException
     * @throws NotCompatibleTypeException
     * @throws InvalidExpressionException
     * @throws PrattParserException
     */
    private List<DroolsRule> parseDiagramElement(DiagramElement node, ExpressionChain extraConditions, List<DroolsRule> newRules, Integer salience)
            throws ExpressionInvalidException, RuleNotImplementedException, ActionNotImplementedException, NotCompatibleTypeException,
            PrattParserException, InvalidExpressionException, InvalidRuleException {
        List<ExpressionChain> forkConditions = new ArrayList<>();
        // Parse the corresponding node
        switch (node.getType()) {
            case TABLE:
                DiagramTable tableNode = (DiagramTable) node;
                if (tableNode.getTable() != null) {
                    List<DroolsRule> rulesList = TableRuleToDroolsRule.parse(node, tableNode.getTable(), extraConditions, salience);
                    for (DroolsRule droolsRule : rulesList) {
                        newRules.addAll(RuleToDroolsRule.parse(node, droolsRule, salience, getDroolsHelper()));
                    }
                }
                break;
            case RULE:
                DiagramRule ruleNode = (DiagramRule) node;
                if (ruleNode.getRule() != null) {
                    // Check the rule generated by the user
                    RuleChecker.checkRule(node, ruleNode.getRule());
                    newRules.addAll(RuleToDroolsRule.parse(node, ruleNode.getRule(), extraConditions, salience, getDroolsHelper()));
                }
                break;
            case CALCULATION:
            case SINK:
                DiagramExpression expressionNode = (DiagramExpression) node;
                if (expressionNode.getExpression() != null) {
                    // Check the rule generated by the user
                    RuleChecker.checkRule(node, new Rule(expressionNode.getExpression().getName(), null, expressionNode.getExpression()));
                    // Create the complete rule
                    Rule rule = new Rule(expressionNode.getExpression().getName(), extraConditions, expressionNode.getExpression());
                    newRules.addAll(RuleToDroolsRule.parse(node, rule, null, salience, getDroolsHelper()));
                }
                break;
            case DIAGRAM_CHILD:
                DiagramChild diagramNode = (DiagramChild) node;
                if (diagramNode.getDiagram() != null) {
                    newRules.addAll(parse(diagramNode.getDiagram(), extraConditions, salience));
                }
                break;
            case FORK:
                forkConditions = completeForkExpressions((DiagramFork) node, extraConditions);
                // Check the rule generated by the fork
                for (ExpressionChain expressionChain : forkConditions) {
                    RuleChecker.checkRule(node, new Rule(expressionChain.getName(), expressionChain, null));
                }
                break;
            default:
                break;
        }
        int linkNumber = 0;

        // When checking the diagram links we have to manage the possible forks,
        // because they will add extra conditions to the following rules
        for (DiagramLink outLink : node.getOutgoingLinks()) {
            if (node.getType().equals(DiagramObjectType.FORK) && (forkConditions.size() > linkNumber)) {
                parseDiagramElement(outLink.getTargetElement(), forkConditions.get(linkNumber), newRules, salience != null ? salience - 1 : null);
            } else {
                parseDiagramElement(outLink.getTargetElement(), extraConditions, newRules, salience != null ? salience - 1 : null);
            }
            linkNumber++;
        }
        return newRules;
    }

    /**
     * A fork adds some extra condition or conditions to the rules that happen
     * after <br>
     * A fork and its outgoing links define a condition that a question or a
     * score must fulfill
     *
     * @return
     */
    private List<ExpressionChain> completeForkExpressions(DiagramFork forkNode, ExpressionChain previousConditions) {
        List<ExpressionChain> forkConditions = new ArrayList<>();

        // Get the element to be checked
        ExpressionValueTreeObjectReference forkNodeExpression = forkNode.getReference();
        // For each outgoing link a new condition is created
        for (DiagramLink outLink : forkNode.getOutgoingLinks()) {
            ExpressionChain expressionOfLinkCopy = (ExpressionChain) outLink.getExpressionChain().generateCopy();
            if (expressionOfLinkCopy.getExpressions().size() == 1) {
                expressionOfLinkCopy.removeAllExpressions();
            }
            if (forkNodeExpression != null) {
                TreeObject treeObject = forkNodeExpression.getReference();
                if ((treeObject instanceof Question)) {
                    Question questionObject = (Question) treeObject;
                    switch (questionObject.getAnswerType()) {
                        case RADIO:
                            // We have 'sex male' as expression in diagram links. We
                            // need to add an equals to create a
                            // complete expression 'sex == male'.
                            if ((expressionOfLinkCopy.getExpressions().size() > 1)
                                    && !(expressionOfLinkCopy.getExpressions().get(1) instanceof ExpressionOperatorLogic)) {
                                expressionOfLinkCopy.addExpression(1, new ExpressionOperatorLogic(AvailableOperator.EQUALS));
                            } else {
                                expressionOfLinkCopy.removeAllExpressions();
                            }
                            break;
                        case MULTI_CHECKBOX:
                            // Fork with multicheckbox can cause problems. User can
                            // select answers from both different
                            // flows.
                            // TODO Add exception to warn the user
                            break;
                        case INPUT:
                            // In case of input we only have to add a copy of the
                            // link, which we do at the bottom of the loop.
                            break;
                    }
                }
                // Add the condition of the fork path to the array of conditions
                forkConditions.add(expressionOfLinkCopy);
            }
        }

        int forkConditionToRemove = 0;
        for (ExpressionChain forkExpressionChain : forkConditions) {
            // Only one condition implies empty link "others"
            // We have to negate the other conditions of the fork
            if (forkExpressionChain.getExpressions().isEmpty()) {
                ExpressionChain resultOfNegation = new ExpressionChain();
                for (ExpressionChain forkExpressionChainToNegate : forkConditions) {
                    if (!forkExpressionChainToNegate.equals(forkExpressionChain)) {

                        if (chekForAndsOrOrs(forkExpressionChainToNegate)) {
                            resultOfNegation.addExpressions(negateAndOrConditions(forkExpressionChainToNegate).getExpressions());
                            resultOfNegation.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                        } else {
                            resultOfNegation.addExpression(new ExpressionFunction(AvailableFunction.NOT));
                            resultOfNegation.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
                            resultOfNegation.addExpression(forkExpressionChainToNegate.generateCopy());
                            resultOfNegation.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
                            resultOfNegation.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                        }
                    }
                }
                // Remove the last AND
                if (resultOfNegation.getExpressions().size() > 1) {
                    resultOfNegation.removeExpression(resultOfNegation.getExpressions().size() - 1);
                }
                forkConditions.set(forkConditionToRemove, resultOfNegation);
            }
            forkConditionToRemove++;
        }

        if ((previousConditions != null) && (previousConditions.getExpressions() != null) && !(previousConditions.getExpressions().isEmpty())) {
            for (ExpressionChain forkExpressionChain : forkConditions) {
                forkExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                forkExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
                forkExpressionChain.addExpressions(previousConditions.getExpressions());
                forkExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
            }
        }
        return forkConditions;
    }

    private static boolean chekForAndsOrOrs(ExpressionChain expressionChain) {
        for (Expression expression : expressionChain.getExpressions()) {
            if (expression instanceof ExpressionOperatorLogic) {
                if (((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.AND)
                        || ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ExpressionChain negateAndOrConditions(ExpressionChain expressionChain) {
        ExpressionChain negatedExpressionChain = new ExpressionChain();
        negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
        negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
        for (Expression expression : expressionChain.getExpressions()) {
            if ((expression instanceof ExpressionOperatorLogic) && ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
                negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
                negatedExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
                negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
                negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));

            } else if ((expression instanceof ExpressionOperatorLogic) && ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.AND)) {
                negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
                negatedExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.OR));
                negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
                negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));

            } else {
                negatedExpressionChain.addExpression(expression);
            }
        }
        negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
        return negatedExpressionChain;
    }

    private DroolsHelper getDroolsHelper() {
        return droolsHelper;
    }

    private void setDroolsHelper(DroolsHelper droolsHelper) {
        this.droolsHelper = droolsHelper;
    }
}
