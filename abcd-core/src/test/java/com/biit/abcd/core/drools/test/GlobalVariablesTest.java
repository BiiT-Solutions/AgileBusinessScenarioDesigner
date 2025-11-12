package com.biit.abcd.core.drools.test;

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

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.*;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.dom4j.DocumentException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Tests the correct creation of the global variables <br>
 * It also test the introduction of the variables in the drools engine
 */
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class GlobalVariablesTest extends KidsFormCreator {
	private final static String GLOBAL_VALUE = "globalValue";

	@Test(groups = { "droolsGlobalVariables" })
	public void testGlobVarsInDroolsEngine() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, ActionNotImplementedException, DocumentException,
			CharacterNotAllowedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, BetweenFunctionInvalidException, ElementIsReadOnly,
			DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create the form and the variables
		Form form = createForm();
		createExpressions(form);
		// Create the rules and launch the engine
		DroolsForm submittedForm = createAndRunDroolsRules(form);

		// Check if the operation was correct
		Assert.assertEquals(
				((DroolsSubmittedForm) submittedForm.getDroolsSubmittedForm()).getVariableValue(GLOBAL_VALUE),
				55.4 / (21.0 / 100.0));
	}

	private void createExpressions(Form form) {
		CustomVariable auxCustomVariable = new CustomVariable(form, GLOBAL_VALUE, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		// Mathematical expression
		ExpressionChain expression = new ExpressionChain("testCalculation",
				new ExpressionValueCustomVariable(form, auxCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "weight")),
				new ExpressionOperatorMath(AvailableOperator.DIVISION),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueGlobalVariable(getGlobalVariableNumber()),
				new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionValueNumber(100.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);

		// Creation of a simple diagram to load the table rule
		form.addDiagram(createExpressionsSubdiagram(form));
	}

	private Diagram createExpressionsSubdiagram(Form form) {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : form.getExpressionChains()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramExpression diagramExpression = new DiagramExpression();
			diagramExpression.setExpression(expressionChain);
			diagramExpression.setJointjsId(IdGenerator.createId());
			diagramExpression.setType(DiagramObjectType.CALCULATION);
			Node nodeRule = new Node(diagramExpression.getJointjsId());

			DiagramSink diagramSink = new DiagramSink();
			diagramSink.setJointjsId(IdGenerator.createId());
			diagramSink.setType(DiagramObjectType.SINK);
			Node nodeSink = new Node(diagramSink.getJointjsId());

			DiagramLink diagramLinkSourceRule = new DiagramLink(nodeSource, nodeRule);
			diagramLinkSourceRule.setJointjsId(IdGenerator.createId());
			diagramLinkSourceRule.setType(DiagramObjectType.LINK);
			DiagramLink diagramLinkRuleSink = new DiagramLink(nodeRule, nodeSink);
			diagramLinkRuleSink.setJointjsId(IdGenerator.createId());
			diagramLinkRuleSink.setType(DiagramObjectType.LINK);

			subDiagram.addDiagramObject(diagramSource);
			subDiagram.addDiagramObject(diagramExpression);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}
}
