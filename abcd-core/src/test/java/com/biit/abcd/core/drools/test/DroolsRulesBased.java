package com.biit.abcd.core.drools.test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
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
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.engine.DroolsRulesEngine;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.form.submitted.ISubmittedForm;

import java.util.ArrayList;
import java.util.List;

public class DroolsRulesBased {

    private String createDroolsRules(Form form) throws RuleNotImplementedException, NotCompatibleTypeException,
            ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
            NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException, DateComparisonNotPossibleException,
            PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidRuleException, ActionNotImplementedException,
            DroolsRuleGenerationException, InvalidExpressionException {
        FormToDroolsExporter formDrools = new FormToDroolsExporter();
        DroolsRulesGenerator rulesGenerator = formDrools.generateDroolRules(form, new ArrayList<>());
        return rulesGenerator.getRules();
    }

    private DroolsForm runDroolsRules(ISubmittedForm submittedForm, String droolsRules, List<GlobalVariable> globalVariables) throws DroolsRuleExecutionException {
        // Test the rules with the submitted form and returns a DroolsForm
        DroolsRulesEngine droolsEngine = new DroolsRulesEngine();
        return droolsEngine.applyDrools(submittedForm, droolsRules,
                new ArrayList<>(RuleGenerationUtils.convertGlobalVariablesToDroolsGlobalVariables(globalVariables)));
    }

    protected DroolsForm executeDroolsEngine(Form form, ISubmittedForm submittedForm, List<GlobalVariable> globalVariables) throws BetweenFunctionInvalidException, PluginInvocationException, ActionNotImplementedException,
            ExpressionInvalidException, NullCustomVariableException, PrattParserException, NullTreeObjectException,
            TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException, NullExpressionValueException,
            DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException,
            InvalidExpressionException, InvalidRuleException, DroolsRuleExecutionException {
        String droolsRules = createDroolsRules(form);
        return runDroolsRules(submittedForm, droolsRules, globalVariables);
    }

    protected void defineDiagram(Form form) {
        Diagram diagramD1 = new Diagram("d1");

        // Create the elements of the diagram
        DiagramSource diagramStartNode = new DiagramSource();
        diagramStartNode.setJointjsId(IdGenerator.createId());
        diagramStartNode.setType(DiagramObjectType.SOURCE);

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);

        List<DiagramElement> diagramElements = new ArrayList<>();

        //First expressions, later rules.
        for (ExpressionChain expressionChain : form.getExpressionChains()) {
            DiagramExpression diagramExpression = new DiagramExpression();
            diagramExpression.setExpression(expressionChain);
            diagramExpression.setJointjsId(IdGenerator.createId());
            diagramExpression.setType(DiagramObjectType.CALCULATION);
            diagramElements.add(diagramExpression);
        }

        for (Rule rule : form.getRules()) {
            DiagramRule diagramRule = new DiagramRule();
            diagramRule.setRule(rule);
            diagramRule.setJointjsId(IdGenerator.createId());
            diagramRule.setType(DiagramObjectType.RULE);
            diagramElements.add(diagramRule);
        }

        //Convert elements to diagram nodes
        List<Node> diagramNodes = new ArrayList<>();

        Node nodeSource = new Node(diagramStartNode.getJointjsId());
        diagramD1.addDiagramObject(diagramStartNode);

        for (DiagramElement element : diagramElements) {
            diagramNodes.add(new Node(element.getJointjsId()));
            diagramD1.addDiagramObject(element);
        }

        Node nodeSink = new Node(diagramEndNode.getJointjsId());
        diagramD1.addDiagramObject(diagramEndNode);

        // Create the links between nodes
        DiagramLink start_firstNode = new DiagramLink(nodeSource, diagramNodes.get(0));
        start_firstNode.setJointjsId(IdGenerator.createId());
        start_firstNode.setType(DiagramObjectType.LINK);
        diagramD1.addDiagramObject(start_firstNode);

        for (int i = 1; i < diagramNodes.size(); i++) {
            DiagramLink node_node = new DiagramLink(diagramNodes.get(i - 1), diagramNodes.get(i));
            node_node.setJointjsId(IdGenerator.createId());
            node_node.setType(DiagramObjectType.LINK);
            diagramD1.addDiagramObject(node_node);
        }

        DiagramLink lastNode_End = new DiagramLink(diagramNodes.get(diagramNodes.size() - 1), nodeSink);
        lastNode_End.setJointjsId(IdGenerator.createId());
        lastNode_End.setType(DiagramObjectType.LINK);
        diagramD1.addDiagramObject(lastNode_End);

        form.addDiagram(diagramD1);
    }
}
