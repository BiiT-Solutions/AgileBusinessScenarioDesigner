package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
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
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.drools.engine.DroolsHelper;
import com.biit.plugins.PluginController;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main class in charge of calling all the methods that generate the drools
 * rules string.<br>
 * It also appends all the necessary libraries to the drools file.<br>
 */
public class DroolsRulesGenerator {

    // Provides some extra functionalities to the drools parser
    private DroolsHelper droolsHelper;

    private Form form;
    private StringBuilder builder;
    private List<GlobalVariable> globalVariables;

    public DroolsRulesGenerator(Form form, List<GlobalVariable> globalVariables) throws RuleNotImplementedException,
            NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
            TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
            DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidRuleException,
            ActionNotImplementedException, InvalidExpressionException {
        this.form = form;
        this.globalVariables = globalVariables;
        droolsHelper = new DroolsHelper(form);
        initParser();
    }

    private void initParser() throws RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException,
            NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
            NullExpressionValueException, BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException,
            DroolsRuleCreationException, PrattParserException, InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
        if (form != null) {
            // Define imports
            importsDeclaration();
            // Define internal types
            typesDeclaration();
            // Define the global variables
            globalVariablesDeclaration();

            try {
                setCustomVariablesDefaultValues();
            } catch (NullTreeObjectException | TreeObjectInstanceNotRecognizedException |
                     TreeObjectParentNotValidException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }

            // Follow the diagram to parse and launch the rules
            Set<Diagram> diagrams = form.getDiagrams();
            if (diagrams != null) {
                // Look for the root diagrams
                List<Diagram> rootDiagrams = new ArrayList<>();
                for (Diagram diagram : diagrams) {
                    if (form.getDiagramParent(diagram) == null) {
                        rootDiagrams.add(diagram);
                    }
                }
                DiagramParser diagParser = new DiagramParser(droolsHelper);
                // Parse the root diagrams
                if (!rootDiagrams.isEmpty()) {
                    getRulesBuilder().append("//******************************************************************************\n");
                    getRulesBuilder().append("//*                                FORM RULES                                  *\n");
                    getRulesBuilder().append("//******************************************************************************\n");
                    for (Diagram diagram : rootDiagrams) {
                        getRulesBuilder().append(diagParser.getDroolsRulesAsText(diagram));
                    }
                }
            }
        }
    }

    /**
     * Defines the packages used by the drools file
     */
    private void importsDeclaration() {
        getRulesBuilder().append("package com.biit.drools \n\n");

        getRulesBuilder().append("import java.lang.Math \n");
        getRulesBuilder().append("import java.util.Date \n");
        getRulesBuilder().append("import java.util.List \n");
        getRulesBuilder().append("import java.util.ArrayList \n");

        getRulesBuilder().append("import com.biit.form.submitted.* \n");
        getRulesBuilder().append("import com.biit.drools.form.* \n");
        getRulesBuilder().append("import com.biit.orbeon.form.* \n");
        getRulesBuilder().append("import com.biit.drools.utils.* \n");

        if (PluginController.getInstance().existsPlugins()) {
            getRulesBuilder().append("import com.biit.plugins.PluginController \n");
            getRulesBuilder().append("import com.biit.plugins.interfaces.IPlugin \n");
            getRulesBuilder().append("import java.lang.reflect.Method \n");
        }
        getRulesBuilder().append("import com.biit.drools.logger.DroolsEngineLogger \n\n");
        getRulesBuilder().append("import com.biit.drools.logger.DroolsRulesLogger \n\n");
    }

    /**
     * Defines the types (classes) used internally in the drools file
     */
    private void typesDeclaration() {
        // Internal type declaration
        getRulesBuilder().append("declare FiredRule\n");
        getRulesBuilder().append("\truleName : String\n");
        getRulesBuilder().append("end\n\n");
    }

    /**
     * Defines the global variables that can be used in the drools file
     */
    private void globalVariablesDeclaration() {
        if ((globalVariables != null) && !globalVariables.isEmpty()) {
            getRulesBuilder().append("//******************************************************************************\n");
            getRulesBuilder().append("//*                              GLOBAL VARIABLES                              *\n");
            getRulesBuilder().append("//******************************************************************************\n");
            getRulesBuilder().append(parseGlobalVariables());
            getRulesBuilder().append("\n");
        }
    }

    /**
     * Sets the default value introduces by the user to the custom variables of
     * the form.<br>
     * It only sets the value if the variable is actively used in the diagram.<br>
     *
     * @throws NullTreeObjectException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws TreeObjectParentNotValidException
     */
    private void setCustomVariablesDefaultValues() throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        // Look for the custom variables in the diagrams
        Set<Diagram> diagrams = form.getDiagrams();
        if (diagrams != null) {
            // Look for the root diagrams
            List<Diagram> rootDiagrams = new ArrayList<>();
            for (Diagram diagram : diagrams) {
                if (form.getDiagramParent(diagram) == null) {
                    rootDiagrams.add(diagram);
                }
            }
            // Look for the custom variables
            List<ExpressionValueCustomVariable> customVariablesList = new ArrayList<>();
            for (Diagram diagram : rootDiagrams) {
                customVariablesList.addAll(RuleGenerationUtils.lookForCustomVariablesInDiagram(diagram));
            }
            // Create the drools rules based on the expression value custom
            // variable found
            Set<String> variablesList = new HashSet<>();

            if (!customVariablesList.isEmpty()) {
                getRulesBuilder().append("//******************************************************************************\n");
                getRulesBuilder().append("//*                           DEFAULT VALUE VARIABLES                          *\n");
                getRulesBuilder().append("//******************************************************************************\n");

                for (ExpressionValueCustomVariable expressionValueCustomVariable : customVariablesList) {
                    String customVariableRule = createDefaultValueDroolsRules(variablesList, expressionValueCustomVariable);
                    if (customVariableRule != null) {
                        getRulesBuilder().append(customVariableRule);
                    }
                }
            }
        }
    }

    private String createDefaultValueDroolsRules(Set<String> variablesList, ExpressionValueCustomVariable expressionValueCustomVariable)
            throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
        StringBuilder defaultCustomVariableValue = new StringBuilder();
        String ruleName = "";
        if ((expressionValueCustomVariable != null) && (expressionValueCustomVariable.getReference() != null)
                && (expressionValueCustomVariable.getVariable() != null) && (expressionValueCustomVariable.getVariable().getDefaultValue() != null)
                && (!(expressionValueCustomVariable.getVariable().getDefaultValue()).equals(""))) {

            String customVariableDefaultValue = "";
            switch (expressionValueCustomVariable.getVariable().getType()) {
                case STRING:
                    customVariableDefaultValue = "\'" + expressionValueCustomVariable.getVariable().getDefaultValue() + "\'";
                    break;
                case NUMBER:
                    customVariableDefaultValue = expressionValueCustomVariable.getVariable().getDefaultValue() + "d";
                    break;
                case DATE:
                    try {
                        SimpleDateFormat userInputFormat = new SimpleDateFormat("dd/mm/yyyy");
                        customVariableDefaultValue = "(new Date(" + userInputFormat.parse(expressionValueCustomVariable.getVariable().getDefaultValue()).getTime()
                                + "l))";
                    } catch (ParseException e) {
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                    }
                    break;
            }

            // Rule name
            ruleName = RuleGenerationUtils.getRuleName(expressionValueCustomVariable.getVariable().getName() + "_default_value");
            // Default rules must be executed first.
            ruleName += "salience " + Salience.VARIABLES_SALIENCE + " \n";
            //Avoid the re-activation of a rule NO MATTER what the cause is. (Mainly by the update that is below).
            ruleName += "lock-on-active\n";
            // Conditions
            defaultCustomVariableValue.append("when\n");
            defaultCustomVariableValue.append("\t$droolsForm: DroolsForm()\n");

            defaultCustomVariableValue.append(SimpleConditionsGenerator.getTreeObjectConditions(expressionValueCustomVariable.getReference()));
            // Actions
            defaultCustomVariableValue.append("then\n");
            defaultCustomVariableValue.append("\t$" + TreeObjectDroolsIdMap.get(expressionValueCustomVariable.getReference()) + ".setVariableValue('"
                    + expressionValueCustomVariable.getVariable().getName() + "', " + customVariableDefaultValue + ");\n");
            defaultCustomVariableValue.append("\tDroolsRulesLogger.info(\"DroolsRule\", \"Default variable value set ("
                    + expressionValueCustomVariable.getReference().getName() + ", " + expressionValueCustomVariable.getVariable().getName() + ", "
                    + customVariableDefaultValue + ")\");\n");
            //Force the re-execution from drools as the variable has been changed.
            //defaultCustomVariableValue.append("\tupdate($droolsForm)\n");
            defaultCustomVariableValue.append("end\n\n");
        }
        if (!variablesList.contains(defaultCustomVariableValue.toString())) {
            variablesList.add(defaultCustomVariableValue.toString());
            return ruleName + defaultCustomVariableValue;
        } else {
            return null;
        }

    }

    /**
     * Creates the global constants for the drools session.<br>
     * Stores in memory the values to be inserted before the facts and generates
     * the global variables export file
     *
     * @return The global constants in drools
     */
    private String parseGlobalVariables() {
        StringBuilder globalConstants = new StringBuilder();
        // In the GUI are called global variables, but regarding the forms are
        // constants
        if ((this.globalVariables != null) && !this.globalVariables.isEmpty()) {

            for (GlobalVariable globalVariable : this.globalVariables) {
                // First check if the data inside the variable has a valid date
                List<VariableData> varDataList = globalVariable.getVariableData();
                if ((varDataList != null) && !varDataList.isEmpty()) {
                    for (VariableData variableData : varDataList) {

                        Timestamp currentTime = new Timestamp(new Date().getTime());
                        Timestamp initTime = variableData.getValidFrom();
                        Timestamp endTime = variableData.getValidTo();
                        // Sometimes endtime can be null, meaning that the
                        // variable data has no ending time
                        if ((currentTime.after(initTime) && (endTime == null)) || (currentTime.after(initTime) && currentTime.before(endTime))) {
                            globalConstants.append(this.globalVariableString(globalVariable));
                            break;
                        }
                    }
                }
            }
        }
        return globalConstants.toString();
    }

    /**
     * This method returns the basic global data types needed by drools.<br>
     *
     * @param globalVariable
     * @return
     */
    private String globalVariableString(GlobalVariable globalVariable) {
        switch (globalVariable.getFormat()) {
            case DATE:
                return "global java.util.Date " + globalVariable.getName() + "\n";
            case TEXT:
            case MULTI_TEXT:
            case POSTAL_CODE:
                return "global java.lang.String " + globalVariable.getName() + "\n";
            case NUMBER:
                return "global java.lang.Number " + globalVariable.getName() + "\n";
        }
        return "";
    }

    public String getRules() {
        return getRulesBuilder().toString();
    }

    private StringBuilder getRulesBuilder() {
        if (this.builder == null) {
            this.builder = new StringBuilder();
        }
        return this.builder;
    }

}
