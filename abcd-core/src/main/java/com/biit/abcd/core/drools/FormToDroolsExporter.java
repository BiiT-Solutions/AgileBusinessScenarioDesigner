package com.biit.abcd.core.drools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.engine.DroolsRulesEngine;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.form.submitted.ISubmittedForm;

public class FormToDroolsExporter {

	/**
	 * Parses the abcd form and loads the rules generated in the drools engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly
	 * defined. <br>
	 * 
	 * @param form
	 *            form to be parsed
	 * @param globalVariables
	 *            array with the global constants to be created
	 * @throws DroolsRuleGenerationException
	 * @throws ActionNotImplementedException
	 * @throws InvalidRuleException
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
	 * @throws InvalidExpressionException
	 */
	public DroolsRulesGenerator generateDroolRules(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
			DroolsRulesGenerator formRules;

			// Creation of the rules
			formRules = new DroolsRulesGenerator(form, globalVariables);
			try {
				Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedRules.drl"), formRules.getRules().getBytes("UTF-8"));
			} catch (IOException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			return formRules;

		}
		return null;
	}

	public String getDroolRules(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException, RuleNotImplementedException,
			NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
			DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidRuleException,
			ActionNotImplementedException, InvalidExpressionException {
		if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
			DroolsRulesGenerator formRules;
			// Creation of the rules
			formRules = new DroolsRulesGenerator(form, globalVariables);
			return formRules.getRules();
		}
		return null;
	}

	/**
	 * Process the test scenario. Orbeon not needed.
	 * 
	 * @param form
	 * @param globalVariables
	 * @return
	 * @throws DroolsRuleGenerationException
	 * @throws DroolsRuleExecutionException
	 * @throws ActionNotImplementedException
	 * @throws InvalidRuleException
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
	 * @throws InvalidExpressionException
	 */
	public ISubmittedForm processForm(Form form, List<GlobalVariable> globalVariables, ISubmittedForm iSubmittedForm) throws DroolsRuleGenerationException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Generate all drools rules.
		DroolsRulesGenerator rulesGenerator = generateDroolRules(form, globalVariables);
		// Obtain results
		if ((rulesGenerator != null) && (iSubmittedForm != null)) {
			DroolsRulesEngine droolsEngine = new DroolsRulesEngine();
			return droolsEngine.applyDrools(iSubmittedForm, rulesGenerator.getRules(),
					RuleGenerationUtils.convertGlobalVariablesToDroolsGlobalVariables(globalVariables));
		} else
			return null;
	}
}
