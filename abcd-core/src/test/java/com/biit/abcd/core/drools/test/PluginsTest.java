package com.biit.abcd.core.drools.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;

public class PluginsTest extends KidsFormCreator {

	private final static String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private final static Class<?> HELLO_WORLD_PLUGIN_INTERFACE = com.biit.plugins.interfaces.IHelloWorld.class;
	private final static String HELLO_WORLD_PLUGIN_METHOD = "methodHelloWorld";

	@Test(groups = { "rules2" })
	public void helloWorldPluginTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			CustomVariable customvariableToAssign = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("helloWorldExpression", new ExpressionValueCustomVariable(
					getForm(), customvariableToAssign), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionPluginMethod(HELLO_WORLD_PLUGIN_INTERFACE, HELLO_WORLD_PLUGIN_METHOD));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
//			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT), "Hello World");
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

}
