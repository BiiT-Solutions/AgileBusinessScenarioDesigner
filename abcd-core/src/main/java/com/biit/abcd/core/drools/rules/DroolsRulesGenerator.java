package com.biit.abcd.core.drools.rules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.drools.DroolsGlobalVariable;
import com.biit.abcd.core.drools.json.globalvariables.JSonConverter;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

public class DroolsRulesGenerator {

	private Form form;
	private String rules;
	private List<GlobalVariable> globalVariables;
	private List<DroolsGlobalVariable> droolsGlobalVariables;

	public DroolsRulesGenerator(Form form, List<GlobalVariable> globalVariables) throws ExpressionInvalidException,
			RuleInvalidException, RuleNotImplementedException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		this.form = form;
		this.globalVariables = globalVariables;
		this.droolsGlobalVariables = new ArrayList<DroolsGlobalVariable>();
		this.initParser();
	}

	private void initParser() throws ExpressionInvalidException, RuleInvalidException, RuleNotImplementedException,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		if (this.form != null) {
			this.rules = "package com.biit.drools \n\n";
			this.rules += "import com.biit.abcd.core.drools.facts.inputform.* \n";
			this.rules += "import com.biit.abcd.core.drools.utils.* \n";
			this.rules += "import java.lang.Math \n";
			this.rules += "import java.util.Date \n";
			this.rules += "import java.util.List \n";
			this.rules += "import java.util.ArrayList \n";
			this.rules += "import com.biit.abcd.logger.AbcdLogger \n\n";
			// Creation of the global variables
			if ((this.globalVariables != null) && !this.globalVariables.isEmpty()) {
				this.rules += this.parseGlobalVariables();
				this.rules += "\n";
			}

			// Follow the diagram to parse and launch the rules
			Set<Diagram> diagrams = this.form.getDiagrams();
			if (diagrams != null) {
				// Look for the root diagrams
				List<Diagram> rootDiagrams = new ArrayList<Diagram>();
				for (Diagram diagram : diagrams) {
					if (this.form.getDiagramParent(diagram) == null) {
						rootDiagrams.add(diagram);
					}
				}
				DiagramParser diagParser = new DiagramParser();
				// Parse the root diagrams
				for (Diagram diagram : rootDiagrams) {
					this.rules += diagParser.getDroolsRulesAsText(diagram);
				}
			}
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
						if ((currentTime.after(initTime) && (endTime == null))
								|| (currentTime.after(initTime) && currentTime.before(endTime))) {
							this.droolsGlobalVariables.add(new DroolsGlobalVariable(globalVariable.getName(),
									globalVariable.getFormat(), variableData.getValue()));
							break;
						}
					}
				}
			}
		}
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
		return this.rules;
	}

}
