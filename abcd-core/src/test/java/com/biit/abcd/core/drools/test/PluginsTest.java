package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.plugins.PluginController;
import com.biit.plugins.exceptions.DuplicatedPluginFoundException;
import com.biit.plugins.exceptions.InvalidMethodParametersException;
import com.biit.plugins.exceptions.MethodInvocationException;
import com.biit.plugins.exceptions.NoMethodFoundException;
import com.biit.plugins.exceptions.NoPluginFoundException;
import com.biit.plugins.interfaces.IPlugin;
import com.biit.utils.configuration.IPropertiesSource;

/**
 * For executing this test correctly the plugins must be placed in the specified
 * path by the settings.conf file
 * 
 */
public class PluginsTest extends KidsFormCreator {

	private final static String CUSTOM_VARIABLE_RESULT = "KB-Antropometrie-Text";
	private final static String HELLO_WORLD_PLUGIN_NAME = "hello-world";
	private final static String HELLO_WORLD_PLUGIN_RETURN = "Hello World";
	private final static String HELLO_WORLD_PLUGIN_METHOD = "methodHelloWorld";
	private final static String DROOLS_PLUGIN_NAME = "drools-test";
	private final static String DROOLS_PLUGIN_METHOD = "methodSumParameters";
	private final static String LIFERAY_PLUGIN_NAME = "liferay-article";
	private final static String LIFERAY_PLUGIN_METHOD = "methodGetLatestArticleContent";
	private final static Double LIFERAY_ARTICLE_RESOURCE_PRIMARY_KEY = 21582d;
	private final static String LIFERAY_PLUGIN_METHOD_BY_PROPERTY = "methodGetLatestArticleContentByProperty";
	private final static String LIFERAY_PLUGIN_METHOD_GET_PROPERTIES_SOURCES = "methodGetPropertiesSources";
	private final static String LIFERAY_ARTICLE_PROPERTY = "fat-goal-description";
	private final static String LIFERAY_RESULT = "Basis Sportmedisch OnderzoekWhy to read this article...only if you want to know everything about the Basic Examination...";
	private final static String LIFERAY_RESULT2 = "A short description to explain the graphics below and what is this intended to achieve.";
	private final static String LIFERAY_CONFIG_FILE = "abcd-core/src/test/resources/plugins/liferay-article-plugin-jar-with-dependencies.conf";

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginSelectionTest1() {
		try {
			// Calling the first plugin
			IPlugin pluginInterface = PluginController.getInstance().getPlugin(IPlugin.class, HELLO_WORLD_PLUGIN_NAME);
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
			Assert.assertEquals(PluginController.getInstance().executePluginMethod(IPlugin.class, HELLO_WORLD_PLUGIN_NAME, HELLO_WORLD_PLUGIN_METHOD),
					HELLO_WORLD_PLUGIN_RETURN);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginDroolsCallWithoutParametersTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Restart the form to avoid test cross references
		Form form = createForm();
		CustomVariable customvariableToAssign = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("helloWorldExpression", new ExpressionValueCustomVariable(form, customvariableToAssign),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionPluginMethod(IPlugin.class, HELLO_WORLD_PLUGIN_NAME,
						HELLO_WORLD_PLUGIN_METHOD), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), "Hello World");
	}

	@Test(groups = { "pluginsTest" })
	public void helloWorldPluginDroolsCallWithParametersTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Restart the form to avoid test cross references
		Form form = createForm();
		CustomVariable customVariableToAssign = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("helloWorldExpression", new ExpressionValueCustomVariable(form, customVariableToAssign),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionPluginMethod(IPlugin.class, DROOLS_PLUGIN_NAME, DROOLS_PLUGIN_METHOD),
				new ExpressionValueNumber(4.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(4.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), 8.);
	}

	/**
	 * Difficult to mantain as the article ID changes between servers. 
	 */
	@Test(groups = { "pluginsTest" })
	public void liferayKnowledgeBasePlugin() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Restart the form to avoid test cross references
		Form form = createForm();
		CustomVariable customVariableToAssign = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("liferayExpression", new ExpressionValueCustomVariable(form, customVariableToAssign),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionPluginMethod(IPlugin.class, LIFERAY_PLUGIN_NAME, LIFERAY_PLUGIN_METHOD), new ExpressionValueNumber(
						LIFERAY_ARTICLE_RESOURCE_PRIMARY_KEY), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), LIFERAY_RESULT);
	}

	/**
	 * Difficult to mantain as the article ID changes between servers. 
	 */
	@Test(groups = { "pluginsTest" })
	public void liferayKnowledgeBasePluginBySettings() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Restart the form to avoid test cross references
		Form form = createForm();
		CustomVariable customVariableToAssign = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("liferayExpression", new ExpressionValueCustomVariable(form, customVariableToAssign),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionPluginMethod(IPlugin.class, LIFERAY_PLUGIN_NAME,
						LIFERAY_PLUGIN_METHOD_BY_PROPERTY), new ExpressionValueString(LIFERAY_ARTICLE_PROPERTY), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), LIFERAY_RESULT2);
	}

	@Test(groups = { "pluginsTest" })
	@SuppressWarnings("unchecked")
	public void liferayKnowledgeBasePluginConfigurationFoundInJarFolder() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, NoPluginFoundException, DuplicatedPluginFoundException,
			NoMethodFoundException, InvalidMethodParametersException, MethodInvocationException {
		List<IPropertiesSource> propertiesFiles = (List<IPropertiesSource>) PluginController.getInstance().executePluginMethod(IPlugin.class,
				LIFERAY_PLUGIN_NAME, LIFERAY_PLUGIN_METHOD_GET_PROPERTIES_SOURCES);
		boolean existConfigFile = false;
		for (IPropertiesSource propertyFile : propertiesFiles) {
			if (propertyFile.toString().contains(LIFERAY_CONFIG_FILE)) {
				existConfigFile = true;
			}
		}

		Assert.assertTrue(existConfigFile);
	}
}
