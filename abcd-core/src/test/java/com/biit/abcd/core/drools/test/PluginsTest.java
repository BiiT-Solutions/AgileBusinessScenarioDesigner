package com.biit.abcd.core.drools.test;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.drools.plugins.PluginController;
import com.biit.plugins.interfaces.IPlugin;

/**
 * For executing this test correctly the plugins must be placed in the specified
 * path by the settings.conf file
 * 
 */
public class PluginsTest extends KidsFormCreator {

	private final static String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private final static Class<?> PLUGIN_INTERFACE = com.biit.plugins.interfaces.IPlugin.class;
	private final static String HELLO_WORLD_PLUGIN_NAME = "HelloWorld";
	private final static String HELLO_WORLD_PLUGIN_RETURN = "Hello World";
	private final static String HELLO_WORLD_PLUGIN_METHOD = "methodHelloWorld";
	private final static String DROOLS_PLUGIN_NAME = "DroolsFunctions";
	private final static String DROOLS_PLUGIN_METHOD = "methodSumParameters";
	private final static String LIFERAY_PLUGIN_NAME = "LiferayKnowledgeBasePlugin";
	private final static String LIFERAY_PLUGIN_METHOD = "methodGetLatestArticleContent";
	private final static Double LIFERAY_ARTICLE_RESOURCE_PRIMARY_KEY = 26383d;
	private final static String LIFERAY_PLUGIN_METHOD_BY_PROPERTY = "methodGetLatestArticleContentByProperty";
	private final static String LIFERAY_ARTICLE_PROPERTY = "Article1";
	private final static String LIFERAY_RESULT = "Basis Sportmedisch Onderzoek\nBasis Sportmedisch OnderzoekWhy to read this article...only if you want to know everything about the Basic Examination...";

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginSelectionTest1() {
		try {
			// Calling the first plugin
			IPlugin pluginInterface = PluginController.getInstance().getPlugin(PLUGIN_INTERFACE,
					HELLO_WORLD_PLUGIN_NAME);
			Method method = ((IPlugin) pluginInterface).getPluginMethod(HELLO_WORLD_PLUGIN_METHOD);
			Assert.assertEquals(method.invoke(pluginInterface), HELLO_WORLD_PLUGIN_RETURN);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginOneCallTest() {
		try {
			// Calling the hello world plugin with only one call
			Assert.assertEquals(
					PluginController.getInstance().executePluginMethod(PLUGIN_INTERFACE, HELLO_WORLD_PLUGIN_NAME,
							HELLO_WORLD_PLUGIN_METHOD), HELLO_WORLD_PLUGIN_RETURN);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginDroolsCallWithoutParametersTest() throws FieldTooLongException,
			CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		CustomVariable customvariableToAssign = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("helloWorldExpression", new ExpressionValueCustomVariable(
				getForm(), customvariableToAssign), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionPluginMethod(PLUGIN_INTERFACE, HELLO_WORLD_PLUGIN_NAME, HELLO_WORLD_PLUGIN_METHOD),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
				"Hello World");
	}

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginDroolsCallWithParametersTest() throws FieldTooLongException,
			CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		CustomVariable customVariableToAssign = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("helloWorldExpression", new ExpressionValueCustomVariable(
				getForm(), customVariableToAssign), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionPluginMethod(PLUGIN_INTERFACE, DROOLS_PLUGIN_NAME, DROOLS_PLUGIN_METHOD),
				new ExpressionValueNumber(4.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						4.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
				8.);
	}

	@Test(groups = { "pluginsTest" })
	public void liferayKnowledgeBasePlugin() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		CustomVariable customVariableToAssign = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("liferayExpression", new ExpressionValueCustomVariable(
				getForm(), customVariableToAssign), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionPluginMethod(PLUGIN_INTERFACE, LIFERAY_PLUGIN_NAME, LIFERAY_PLUGIN_METHOD),
				new ExpressionValueNumber(LIFERAY_ARTICLE_RESOURCE_PRIMARY_KEY), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
				LIFERAY_RESULT);
	}

	@Test(groups = { "pluginsTest" })
	public void liferayKnowledgeBasePluginBySettings() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		CustomVariable customVariableToAssign = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("liferayExpression", new ExpressionValueCustomVariable(
				getForm(), customVariableToAssign), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionPluginMethod(PLUGIN_INTERFACE, LIFERAY_PLUGIN_NAME, LIFERAY_PLUGIN_METHOD_BY_PROPERTY),
				new ExpressionValueString(LIFERAY_ARTICLE_PROPERTY),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
				LIFERAY_RESULT);
	}
}
