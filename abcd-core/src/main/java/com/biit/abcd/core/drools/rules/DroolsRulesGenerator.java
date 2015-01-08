package com.biit.abcd.core.drools.rules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.PluginController;
import com.biit.abcd.core.drools.DroolsGlobalVariable;
import com.biit.abcd.core.drools.DroolsHelper;
import com.biit.abcd.core.drools.json.globalvariables.JSonConverter;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.utils.DroolsUtils;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

public class DroolsRulesGenerator {

	// Provides some extra functionalities to the drools parser
	private DroolsHelper droolsHelper;

	private Form form;
	private StringBuilder builder;
	private List<GlobalVariable> globalVariables;
	private List<DroolsGlobalVariable> droolsGlobalVariables;

	public DroolsRulesGenerator(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException {
		this.form = form;
		this.globalVariables = globalVariables;
		this.droolsGlobalVariables = new ArrayList<DroolsGlobalVariable>();
		droolsHelper = new DroolsHelper(form);
		initParser();
	}

	private void initParser() throws DroolsRuleGenerationException {
		if (form != null) {
			// Define imports
			importsDeclaration();
			// Define internal types
			typesDeclaration();
			// Define the global variables
			globalVariablesDeclaration();

			try {
				setCustomVariablesDefaultValues();
			} catch (NullTreeObjectException | TreeObjectInstanceNotRecognizedException
					| TreeObjectParentNotValidException e) {
				e.printStackTrace();
			}

			// Follow the diagram to parse and launch the rules
			Set<Diagram> diagrams = form.getDiagrams();
			if (diagrams != null) {
				// Look for the root diagrams
				List<Diagram> rootDiagrams = new ArrayList<Diagram>();
				for (Diagram diagram : diagrams) {
					if (form.getDiagramParent(diagram) == null) {
						rootDiagrams.add(diagram);
					}
				}
				DiagramParser diagParser = new DiagramParser(droolsHelper);
				// Parse the root diagrams
				if (!rootDiagrams.isEmpty()) {
					getRulesBuilder().append(
							"//******************************************************************************\n");
					getRulesBuilder().append(
							"//*                                FORM RULES                                  *\n");
					getRulesBuilder().append(
							"//******************************************************************************\n");
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
		getRulesBuilder().append("import com.biit.abcd.core.drools.facts.inputform.* \n");
		getRulesBuilder().append("import com.biit.abcd.core.drools.utils.* \n");
		getRulesBuilder().append("import java.lang.Math \n");
		getRulesBuilder().append("import java.util.Date \n");
		getRulesBuilder().append("import java.util.List \n");
		getRulesBuilder().append("import java.util.ArrayList \n");
		getRulesBuilder().append("import com.biit.orbeon.form.* \n");
		if (PluginController.getInstance().existsPlugins()) {
			getRulesBuilder().append("import com.biit.abcd.core.PluginController \n");
			getRulesBuilder().append("import net.xeoh.plugins.base.Plugin \n");
			getRulesBuilder().append("import com.biit.plugins.interfaces.IPlugin \n");
			getRulesBuilder().append("import java.lang.reflect.Method \n");
		}
		getRulesBuilder().append("import com.biit.abcd.logger.AbcdLogger \n\n");
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
			getRulesBuilder().append(
					"//******************************************************************************\n");
			getRulesBuilder().append(
					"//*                              GLOBAL VARIABLES                              *\n");
			getRulesBuilder().append(
					"//******************************************************************************\n");
			getRulesBuilder().append(parseGlobalVariables());
			getRulesBuilder().append("\n");
		}
	}

	private void setCustomVariablesDefaultValues() throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		// Look for the custom variables in the diagrams
		Set<Diagram> diagrams = form.getDiagrams();
		if (diagrams != null) {
			// Look for the root diagrams
			List<Diagram> rootDiagrams = new ArrayList<Diagram>();
			for (Diagram diagram : diagrams) {
				if (form.getDiagramParent(diagram) == null) {
					rootDiagrams.add(diagram);
				}
			}
			// Look for the custom variables
			List<ExpressionValueCustomVariable> customVariablesList = new ArrayList<ExpressionValueCustomVariable>();
			for (Diagram diagram : rootDiagrams) {
				customVariablesList.addAll(RulesUtils.lookForCustomVariablesInDiagram(diagram));
			}
			// Create the drools rules based on the expression value custom
			// variable found
			Set<String> variablesList = new HashSet<String>();

			if (!customVariablesList.isEmpty()) {
				getRulesBuilder().append(
						"//******************************************************************************\n");
				getRulesBuilder().append(
						"//*                           DEFAULT VALUE VARIABLES                          *\n");
				getRulesBuilder().append(
						"//******************************************************************************\n");

				for (ExpressionValueCustomVariable expressionValueCustomVariable : customVariablesList) {
					String customVariableRule = createDefaultValueDroolsRules(variablesList,
							expressionValueCustomVariable);
					if (customVariableRule != null) {
						getRulesBuilder().append(customVariableRule);
					}
				}
			}
		}
	}

	private String createDefaultValueDroolsRules(Set<String> variablesList,
			ExpressionValueCustomVariable expressionValueCustomVariable) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		StringBuilder defaultCustomVariableValue = new StringBuilder();
		String ruleName = "";
		if ((expressionValueCustomVariable != null) && (expressionValueCustomVariable.getReference() != null)
				&& (expressionValueCustomVariable.getVariable() != null)
				&& (expressionValueCustomVariable.getVariable().getDefaultValue() != null)) {

			String customVariableDefaultValue = "";
			switch (expressionValueCustomVariable.getVariable().getType()) {
			case STRING:
				customVariableDefaultValue = "\'" + expressionValueCustomVariable.getVariable().getDefaultValue()
						+ "\'";
				break;
			case NUMBER:
				try {
					// To ensure the value is a double value, because the user
					// can put an integer and drools doesn't like it
					Double value = Double.parseDouble(expressionValueCustomVariable.getVariable().getDefaultValue());
					customVariableDefaultValue = value.toString();
				} catch (NullPointerException | NumberFormatException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
				break;
			case DATE:
				customVariableDefaultValue = "\'" + expressionValueCustomVariable.getVariable().getDefaultValue()
						+ "\'";
				break;
			}

			// Rule name
			ruleName = RulesUtils.getRuleName(expressionValueCustomVariable.getVariable().getName()
					+ "_default_value");
			// Conditions
			defaultCustomVariableValue.append("when\n");
			defaultCustomVariableValue.append("\t$droolsForm: DroolsForm()\n");

			defaultCustomVariableValue.append(SimpleConditionsGenerator
					.getTreeObjectConditions(expressionValueCustomVariable.getReference()));
			// Actions
			defaultCustomVariableValue.append("then\n");
			defaultCustomVariableValue.append("\t$"
					+ TreeObjectDroolsIdMap.get(expressionValueCustomVariable.getReference()) + ".setVariableValue('"
					+ expressionValueCustomVariable.getVariable().getName() + "', " + customVariableDefaultValue
					+ ");\n");
			defaultCustomVariableValue.append("\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
					+ expressionValueCustomVariable.getReference().getName() + ", "
					+ expressionValueCustomVariable.getVariable().getName() + ", " + customVariableDefaultValue
					+ ")\");\n");
			defaultCustomVariableValue.append("end\n\n");
		}
		if (!variablesList.contains(defaultCustomVariableValue.toString())) {
			variablesList.add(defaultCustomVariableValue.toString());
			return ruleName + defaultCustomVariableValue.toString();
		}else{
			return null;
		}
		
	}

	/**
	 * Creates the global constants for the drools session.<br>
	 * Stores in memory the values to be inserted before the facts and generates
	 * the global variables export file
	 * 
	 * 
	 * @return The global constants in drools
	 */
	private String parseGlobalVariables() {
		String globalConstants = "";
		// In the GUI are called global variables, but regarding the forms are
		// constants
		if ((this.globalVariables != null) && !this.globalVariables.isEmpty()) {
			// Create the Json file with the exported information
			exportGlobalVariables(this.globalVariables);

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
						if ((currentTime.after(initTime) && (endTime == null))
								|| (currentTime.after(initTime) && currentTime.before(endTime))) {
							globalConstants += this.globalVariableString(globalVariable);
							this.droolsGlobalVariables.add(new DroolsGlobalVariable(globalVariable.getName(),
									globalVariable.getFormat(), variableData.getValue()));
							break;
						}
					}
				}
			}
		}
		return globalConstants;
	}

	/**
	 * Sets the global variable array that is going to be used in the drools
	 * engine<br>
	 * It does not create the drools rules
	 * 
	 * @param globalVariables
	 */
	public void setGlobalVariables(List<GlobalVariable> globalVariables) {
		// In the GUI are called global variables, but regarding the forms are
		// constants
		droolsGlobalVariables = DroolsUtils.calculateDroolsGlobalVariables(globalVariables);
	}

	/**
	 * Export the global variables of the abcd to a json file
	 * 
	 * @param globalVariablesList
	 */
	public static void exportGlobalVariables(List<GlobalVariable> globalVariablesList) {
		// Create the global variables export file
		String globalVariablesJson = JSonConverter.convertGlobalVariableListToJson(globalVariablesList);
		try {
			Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "globalVariables.json"),
					globalVariablesJson.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String globalVariableString(GlobalVariable globalVariable) {
		switch (globalVariable.getFormat()) {
		case DATE:
			return "global java.util.Date " + globalVariable.getName() + "\n";
		case TEXT:
			return "global java.lang.String " + globalVariable.getName() + "\n";
		case POSTAL_CODE:
			return "global java.lang.String " + globalVariable.getName() + "\n";
		case NUMBER:
			return "global java.lang.Number " + globalVariable.getName() + "\n";
		}
		return "";
	}

	public List<DroolsGlobalVariable> getGlobalVariables() {
		return this.droolsGlobalVariables;
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
