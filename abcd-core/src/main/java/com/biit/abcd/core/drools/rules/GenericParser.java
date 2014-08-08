package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

public class GenericParser {

	private HashMap<TreeObject, String> treeObjectDroolsname;

	public GenericParser(){
		this.treeObjectDroolsname = new HashMap<TreeObject, String>();
	}

	public void putTreeObjectName(TreeObject treeObject, String value){
		this.treeObjectDroolsname.put(treeObject, value);
	}

	public String getTreeObjectName(TreeObject treeObject){
		return this.treeObjectDroolsname.get(treeObject);
	}

	public String createDroolsRule(List<Expression> conditions, List<Expression> actions) {
		String ruleCore = "";
		if(conditions != null) {
			ruleCore += this.parseConditions(conditions);
		}
		if(actions != null) {
			ruleCore += this.parseActions(actions);
		}
		return ruleCore;
	}

	public String createDroolsRule(List<Expression> conditions, List<Expression> actions, String extraConditions) {
		String ruleCore = "";
		if(extraConditions != null) {
			ruleCore += extraConditions;
		}
		if(conditions != null) {
			ruleCore += this.parseConditions(conditions);
			ruleCore = this.removeDuplicateLines(ruleCore);
		}
		if(actions != null) {
			// Expression rule
			if(conditions == null){
				ruleCore += this.parseExpressions(actions,extraConditions);
			}
			// Normal rule
			else{
				ruleCore += this.parseActions(actions);
			}
		}
		return ruleCore;
	}

	private String parseConditions(List<Expression> conditions){
		// Condition of type (something AND something AND ...)
		if(conditions.size() > 6){
			int startConditionIndex = 0;
			int endConditionIndex = 0;
			// Creation of substrings defined by the AND expressions
			// Each subCondition list is parsed individually
			List<List<Expression>> subConditions = new ArrayList<List<Expression>>();
			for(Expression condition: conditions){
				if((condition instanceof ExpressionOperatorLogic) &&
						((ExpressionOperatorLogic)condition).getValue().equals(AvailableOperator.AND)){
					subConditions.add(conditions.subList(startConditionIndex, endConditionIndex));
					startConditionIndex = endConditionIndex+1;
				}
				endConditionIndex++;
			}
			// Add the last subCondition
			subConditions.add(conditions.subList(startConditionIndex, endConditionIndex));
			if(startConditionIndex != 0) {
				return this.conditionsWithAnd(subConditions);
			}
		}
		// Condition of type (Question | Answer)
		if((conditions.size() == 2) &&
				(conditions.get(0) instanceof ExpressionValueTreeObjectReference) &&
				(conditions.get(1) instanceof ExpressionChain)){
			TreeObject questionObject = ((ExpressionValueTreeObjectReference)conditions.get(0)).getReference();
			if((questionObject != null) && (questionObject instanceof Question)) {
				List<Expression> answerExpressions = ((ExpressionChain)conditions.get(1)).getExpressions();
				return this.questionAnswerEqualsCondition((Question)questionObject, answerExpressions);
			}
		}
		// Condition of type (cat.score == value)
		else if((conditions.size() == 3) &&
				(conditions.get(0) instanceof ExpressionValueCustomVariable) &&
				(conditions.get(1) instanceof ExpressionOperatorLogic) &&
				(((ExpressionOperatorLogic)conditions.get(1)).getValue().equals(AvailableOperator.EQUALS)) &&
				(conditions.get(2) instanceof ExpressionValueNumber)){
			return this.scoreEqualsCondition(conditions);
		}
		// Condition of type (question1 == answer11)
		else if((conditions.size() == 3) &&
				(conditions.get(0) instanceof ExpressionValueTreeObjectReference) &&
				(conditions.get(1) instanceof ExpressionOperatorLogic) &&
				(((ExpressionOperatorLogic)conditions.get(1)).getValue().equals(AvailableOperator.EQUALS)) &&
				(conditions.get(2) instanceof ExpressionValueTreeObjectReference)){
			return this.questionAnswerEqualsCondition(conditions);
		}
		// Condition of type (question1 BETWEEN (answer11, answer12))
		else if((conditions.size() == 6) &&
				(conditions.get(0) instanceof ExpressionValueTreeObjectReference) &&
				(conditions.get(1) instanceof ExpressionFunction) &&
				(((ExpressionFunction)conditions.get(1)).getValue().equals(AvailableFunction.BETWEEN)) &&
				(conditions.get(2) instanceof ExpressionValueNumber) &&
				(conditions.get(4) instanceof ExpressionValueNumber)){
			return this.questionBetweenAnswersCondition(conditions);
		}
		// Condition of type (question1 IN (answer11, answer12, ...))
		else if((conditions.size() > 3) &&
				(conditions.get(0) instanceof ExpressionValueTreeObjectReference) &&
				(conditions.get(1) instanceof ExpressionFunction) &&
				(((ExpressionFunction)conditions.get(1)).getValue().equals(AvailableFunction.IN))){

			return this.answersInQuestionCondition(conditions);
		}
		return "";
	}

	/**
	 * Parse the actions of the rule <br>
	 * Accepts actions with the patterns : <br>
	 * &nbsp&nbsp&nbsp Var = value | string <br>
	 * &nbsp&nbsp&nbsp Var = Var mathOperator value <br>
	 * &nbsp&nbsp&nbsp Var = value mathOperator Var
	 *
	 * @param actions
	 *            list of expressions to be parsed
	 * @return RHS of the rule, and sometimes a modified LHS
	 */
	private String parseActions(List<Expression> actions){
		// Action type =>  (cat.scoreText = "someText") || (cat.score = someValue)
		if ((actions.size() == 3) &&
				(actions.get(0) instanceof ExpressionValueCustomVariable) &&
				(actions.get(1) instanceof ExpressionOperatorMath) &&
				(((ExpressionOperatorMath)actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION)) &&
				((actions.get(2) instanceof ExpressionValueString) ||
						(actions.get(2) instanceof ExpressionValueNumber))) {
			return this.assignationAction(actions);

		}
		// Action type =>  cat.scoreText = cat.scoreText + 1
		else if ((actions.size() == 5) &&
				(actions.get(0) instanceof ExpressionValueCustomVariable) &&
				(actions.get(1) instanceof ExpressionOperatorMath) &&
				(((ExpressionOperatorMath)actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION)) &&
				((actions.get(2) instanceof ExpressionValueCustomVariable) || (actions.get(2) instanceof ExpressionValueNumber)) &&
				(actions.get(3) instanceof ExpressionOperatorMath) &&
				((actions.get(4) instanceof ExpressionValueCustomVariable) || (actions.get(4) instanceof ExpressionValueNumber))) {
			return this.modifyVariableAction(actions);

		}
		return "";
	}

	/**
	 * Parse the actions of the rule <br>
	 * Accepts actions with the patterns : <br>
	 * &nbsp&nbsp&nbsp Var = value | string <br>
	 * &nbsp&nbsp&nbsp Var = Var mathOperator value <br>
	 * &nbsp&nbsp&nbsp Var = value mathOperator Var
	 *
	 * @param actions
	 *            list of expressions to be parsed
	 * @return RHS of the rule, and sometimes a modified LHS
	 */
	private String parseExpressions(List<Expression> actions, String extraConditions){
		// A.k.a Expression
		// Action type => cat.score = min(q1.score, q2.score, ...)
		if ((actions.get(0) instanceof ExpressionValueCustomVariable) &&
				(actions.get(1) instanceof ExpressionOperatorMath) &&
				(((ExpressionOperatorMath)actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION)) &&
				(actions.get(2) instanceof ExpressionFunction)) {
			return this.assignationFunctionAction(actions, extraConditions);
		}
		return "";
	}

	/**
	 * Parse conditions of type Question == answer. <br>
	 * Create drools rule of type Question(getValue()=="answer.getName()")
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionAnswerEqualsCondition(List<Expression> conditions){
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);

		// Check if the first object is a question
		TreeObject treeObject = expVal.getReference();
		if((treeObject != null) && (treeObject instanceof Question)){
			// Sublist from 2 , because the expression list received is Q1 == Answer
			droolsConditions += this.questionAnswerEqualsCondition((Question)treeObject, conditions.subList(2, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions of type Answer. <br>
	 * Create drools rule of type Question(getValue()=="answer.getName()")
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionAnswerEqualsCondition(Question question, List<Expression> answerExpressions){
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal2 = (ExpressionValueTreeObjectReference) answerExpressions.get(0);

		TreeObject treeObject2 = expVal2.getReference();
		if((treeObject2 != null)){
			// Check the parent of the question
			TreeObject questionParent = question.getParent();
			if (questionParent instanceof Category){
				TreeObject form = questionParent.getParent();
				this.putTreeObjectName(form, form.getComparationId().toString());
				this.putTreeObjectName(questionParent, questionParent.getComparationId().toString());
				this.putTreeObjectName(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() == '" + treeObject2.getName() + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}else if(questionParent instanceof Group){
				TreeObject category = questionParent.getParent();
				TreeObject form = category.getParent();
				this.putTreeObjectName(form, form.getComparationId().toString());
				this.putTreeObjectName(category, category.getComparationId().toString());
				this.putTreeObjectName(questionParent, questionParent.getComparationId().toString());
				this.putTreeObjectName(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() == '" + treeObject2.getName() + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getValue() >= answer.getValue()) && (getValue() <= answer.getValue()))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionBetweenAnswersCondition(List<Expression> conditions){
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);

		// Check if the first object is a question
		TreeObject treeObject = expVal.getReference();
		if((treeObject != null) && (treeObject instanceof Question)){
			// Sublist from 1 , because the expression list received is Q1 BETWEEN( answer1, ... )
			droolsConditions += this.questionBetweenAnswersCondition((Question) treeObject, conditions.subList(1, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getValue() >= answer.getValue()) && (getValue() <= answer.getValue()))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionBetweenAnswersCondition(Question question, List<Expression> answerExpressions){
		// Get the values of the between expression
		Double value1 = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
		Double value2 = ((ExpressionValueNumber) answerExpressions.get(3)).getValue();
		String droolsConditions = "";
		if((value1 != null) && (value2 != null)){
			TreeObject questionParent = question.getParent();
			// Check the parent of the question
			if (questionParent instanceof Category){
				TreeObject form = questionParent.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() >= '" + value1 + "' || <= '" + value2 + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}else if(questionParent instanceof Group){
				TreeObject category = questionParent.getParent();
				TreeObject form = category.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(category, category.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() >= '" + value1 + "' || <= '" + value2 + "') from $" + questionParent.getComparationId().toString() + ".getQuestions()\n";
			}
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Question IN(answer1, ...) <br>
	 * Create drools rule like => Question( (getValue() in (answer1, ...))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String answersInQuestionCondition(List<Expression> conditions){
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);
		TreeObject treeObject = expVal.getReference();
		if((treeObject != null) && (treeObject instanceof Question)){
			// Sublist from 1 , because the expression list received is Q1 IN( answer1, ... )
			droolsConditions += this.answersInQuestionCondition((Question)treeObject, conditions.subList(1, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => IN(answer1, ...) <br>
	 * Create drools rule like => Question( (getValue() in (answer1, ...))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String answersInQuestionCondition(Question question, List<Expression> answerExpressions){
		String droolsConditions = "";
		String inValues = "";
		// Store the values inside the IN condition in a String
		for(int i=1; i<(answerExpressions.size()-1); i+=2){
			ExpressionValueTreeObjectReference ansVal = (ExpressionValueTreeObjectReference)answerExpressions.get(i);
			if(ansVal instanceof ExpressionValueCustomVariable){
				//TODO
			}else{
				inValues += "'" + ansVal.getReference().getName() + "', ";
			}
		}
		// Remove the last comma
		inValues = inValues.substring(0, inValues.length()-2);
		if(!inValues.isEmpty()){
			// Check the parent of the question
			TreeObject questionParent = question.getParent();
			if (questionParent instanceof Category){
				TreeObject form = questionParent.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() in( " + inValues + " )) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}else if(questionParent instanceof Group){
				TreeObject category = questionParent.getParent();
				TreeObject form = category.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(category, category.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(question, question.getComparationId().toString());
				droolsConditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				droolsConditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				droolsConditions += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
				droolsConditions += "	$"+question.getComparationId().toString()+" : Question( getValue() in( " + inValues + " )) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}

		}
		return droolsConditions;
	}

	/**
	 * Due to the independent parsing of the conditions of the rule, sometimes
	 * the algorithm generates repeated rules <br>
	 * This method the lines that are equals in the rule<br>
	 * It should be used before sending the rules to the engine <br>
	 *
	 * @param ruleCore
	 * @return
	 */
	private String removeDuplicateLines(String ruleCore){
		// Parse the resulting rule to delete lines that are equal
		String[] auxSplit = ruleCore.split("\n");
		List<String> auxRule = new ArrayList<String>();
		for(int i=0; i<auxSplit.length; i++){
			if(i!= 0){
				boolean stringRepeated = false;
				for(int j=0; j<auxRule.size(); j++){
					if(auxRule.get(j).equals(auxSplit[i])){
						stringRepeated = true;
						break;
					}
				}
				if(!stringRepeated){
					auxRule.add(auxSplit[i]);
				}
			}else{
				auxRule.add(auxSplit[i]);
			}
		}
		// Parse the resulting rule to add an index to separate equal assignation
		// Example $cat : ... \n $cat : ... \n will be converted to $cat : ... \n $cat1 : ... \n
		// (Separated from the previous to make it more understandable)
		String previousVariable = "";
		String auxRuleCore = "";
		int indexVariable = 1;
		for (String auxPart : auxRule) {
			String[] auxRuleArray = auxPart.split(" : ");
			if(auxRuleArray[0].equals(previousVariable)){
				auxRuleArray[0] = auxRuleArray[0] + indexVariable;
				indexVariable++;
			}else{
				previousVariable = auxRuleArray[0];
			}
			if(auxRuleArray.length>1) {
				auxRuleCore += auxRuleArray[0] + " : " + auxRuleArray[1] + "\n";
			}else{
				auxRuleCore += auxRuleArray[0];
			}
		}
		return auxRuleCore;
	}

	/**
	 * Parse conditions like => Score == value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'), getVariablevalue('cScore') == value )
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String scoreEqualsCondition(List<Expression> conditions){
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) conditions.get(0);
		ExpressionValueNumber valueNumber = (ExpressionValueNumber) conditions.get(2);

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if(scope instanceof Form) {
			this.putTreeObjectName(scope, scope.getComparationId().toString());
			ruleCore += "	$"+scope.getComparationId().toString()+" : SubmittedForm( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+")\n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			this.putTreeObjectName(form, form.getComparationId().toString());
			this.putTreeObjectName(scope, scope.getComparationId().toString());
			ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			ruleCore += "	$"+scope.getComparationId().toString()+" : Category( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+") from $"+form.getComparationId().toString()+".getCategory('" + scope.getName() + "')\n";

		} else if (scope instanceof Group) {
			TreeObject category = scope.getParent();
			TreeObject form = category.getParent();
			this.putTreeObjectName(form, form.getComparationId().toString());
			this.putTreeObjectName(category, category.getComparationId().toString());
			this.putTreeObjectName(scope, scope.getComparationId().toString());
			ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
			ruleCore += "	$"+scope.getComparationId().toString()+" : Group( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+category.getComparationId().toString()+".getGroup('" + scope.getName() + "')\n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			if (questionParent instanceof Category){
				TreeObject form = questionParent.getParent();
				this.putTreeObjectName(form, form.getComparationId().toString());
				this.putTreeObjectName(questionParent, questionParent.getComparationId().toString());
				this.putTreeObjectName(scope, scope.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				ruleCore += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
				ruleCore += "	$"+scope.getComparationId().toString()+" : Question( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";

			}else if(questionParent instanceof Group){
				TreeObject category = questionParent.getParent();
				TreeObject form = category.getParent();
				this.putTreeObjectName(form, form.getComparationId().toString());
				this.putTreeObjectName(category, category.getComparationId().toString());
				this.putTreeObjectName(questionParent, questionParent.getComparationId().toString());
				this.putTreeObjectName(scope, scope.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				ruleCore += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
				ruleCore += "	$"+scope.getComparationId().toString()+" : Question( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}
		}
		return ruleCore;
	}

	/**
	 * Parse a list of conditions like => Conditions AND Conditions AND ... <br>
	 * Create drools rule like => $conditions1... \n $conditions2 ...
	 *
	 * @param conditions
	 * @return LHS of the rule
	 */
	private  String conditionsWithAnd(List<List<Expression>> conditions){
		String ruleCore = "";

		for(List<Expression> condition: conditions){
			ruleCore += this.parseConditions(condition);
		}
		return this.removeDuplicateLines(ruleCore);
	}

	/**
	 * Adds condition rows to the rule that manages the assignation of a
	 * variable in the action<br>
	 *
	 * @param variable
	 *            variable to be added to the LHS of the rule
	 * @return LHS of the rule modified with the new variables
	 */
	private  String addConditionVariable(ExpressionValueCustomVariable variable){
		String ruleCore = "";

		// Add the variable assignation to the rule
		TreeObject treeObject = variable.getReference();
		if (treeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) treeObject);

		} else if (treeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) treeObject);

		} else if (treeObject instanceof Question) {
			ruleCore += this.simpleQuestionConditions((Question) treeObject);
		}
		return ruleCore;
	}

	/**
	 * Parse actions like => Score = stringValue || Score = numberValue <br>
	 * Create drools rule like => setVariableValue(scopeOfVariable,
	 * variableName, stringVariableValue )
	 *
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private  String assignationAction(List<Expression> actions) {
		String ruleCore ="";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		// Check if the reference exists in the rule, if not, it creates a new reference
		ruleCore += this.checkVariableAssignation(var);

		ruleCore += Utils.getThenRuleString();

		if (actions.get(2) instanceof ExpressionValueString) {
			ExpressionValueString valueString = (ExpressionValueString) actions.get(2);
			ruleCore += "	$"+this.getTreeObjectName(var.getReference())+".setVariableValue('" + var.getVariable().getName() + "', '"
					+ valueString.getValue() + "');\n";
			ruleCore += "	System.out.println( \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ruleCore += "	$"+this.getTreeObjectName(var.getReference())+".setVariableValue('" + var.getVariable().getName() + "', "
					+ valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueNumber.getValue() + ")\");\n";
		}
		return ruleCore;
	}

	/**
	 * Parse actions like => Score = Score (+|-|/|*) numberValue <br>
	 * Create drools rule like => setVariableValue(scopeOfVariable,
	 * variableName, getVariablevalue(variableName)+numberValue ) <br>
	 * The variables to the right and to the left of the assignation can be
	 * different (i.e. a = b+1) <br>
	 *
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private  String modifyVariableAction(List<Expression> actions) {
		String ruleCore ="";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		ExpressionOperatorMath operator = (ExpressionOperatorMath) actions.get(3);

		// Get the variable name
		String customVarName = var.getVariable().getName();
		// Check if the reference exists in the rule, if not, it creates a new reference
		ruleCore += this.checkVariableAssignation(var);

		if (actions.get(2) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(2);
			ruleCore += this.checkVariableAssignation(var2);
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(4);

			ruleCore += Utils.getThenRuleString();
			ruleCore += "	$"+this.getTreeObjectName(var.getReference())+".setVariableValue('" + customVarName + "', "
					+ "(Double)$"+this.getTreeObjectName(var2.getReference())+".getNumberVariableValue('" + customVarName + "') "
					+ operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable updated ("+var.getReference().getName()+", " + customVarName + ", "+valueNumber.getValue()+"\"));\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(4);
			ruleCore += this.checkVariableAssignation(var2);

			ruleCore += Utils.getThenRuleString();
			ruleCore += "	$"+this.getTreeObjectName(var.getReference())+".setVariableValue('" + customVarName + "', "
					+ "(Double)$"+this.getTreeObjectName(var2.getReference())+".getNumberVariableValue('" + customVarName + "') "
					+ operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable updated ("+var.getReference().getName()+", " + customVarName + ", "+valueNumber.getValue()+"\"));\n";

		}
		return ruleCore;
	}

	/**
	 * Expression parser. An expression is a rule without the condition part in
	 * the definition, but not in the drools engine.<br> Parse actions like =>
	 * Cat.score = min(q1.score, q2.score, ...) <br>
	 * Create drools rule like => <br>
	 * &nbsp&nbsp&nbsp $var : List() from collect( some conditions )<br>
	 * &nbsp&nbsp&nbsp accumulate((Question($score : getScore()) from $var);
	 * $sol : min($value) )
	 *
	 * @param actions
	 *            the expression being parsed
	 * @param extraConditions
	 * @return the rule
	 */
	private  String assignationFunctionAction(List<Expression> actions, String extraConditions) {
		String ruleCore = "";
		if(extraConditions != null) {
			ruleCore += extraConditions;
		}
		// LHS
		// We will have some expression of type Category.score = (Min | Max | Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		// The rule is different if the variable to assign is a Form, a Category or a Group
		TreeObject treeObject = variableToCalculate.getReference();
		if (treeObject instanceof Form) {
			ruleCore += this.simpleFormCondition((Form) treeObject);

		} else if (treeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) treeObject);

		} else if (treeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) treeObject);
		}

		int varIndex = 1;
		String scopeClass = "";
		TreeObject parent = null;
		CustomVariable cVar = null;
		for (int i = 3; i < actions.size(); i++) {
			Expression expression = actions.get(i);
			if (expression instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable aux = (ExpressionValueCustomVariable) expression;
				TreeObject to = aux.getReference();
				cVar = aux.getVariable();
				if (to instanceof Question) {
					scopeClass = "Question";
					parent = to.getParent();
				} else if (to instanceof Group) {
					scopeClass = "Group";
					parent = to.getParent();
				} else if (to instanceof Category) {
					scopeClass = "Category";
					parent = to.getParent();
				}
				if (varIndex == 1) {
					ruleCore += "	$var : List() from collect( " + scopeClass + "(isScoreSet('" + cVar.getName() + "'), getTag() == '" + to.getName() + "' || ";
				} else {
					ruleCore += "== '" + to.getName() + "' || ";
				}
				varIndex++;
			}
		}
		// Finish the line of the condition
		ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
		if (scopeClass.equals("Question")) {
			ruleCore += ") from $" + parent.getComparationId().toString() + ".getQuestions())\n";
		} else if (scopeClass.equals("Group")) {
			ruleCore += ") from $" + parent.getComparationId().toString() + ".getGroups())\n";
		} else if (scopeClass.equals("Category")) {
			ruleCore += ") from $" + parent.getComparationId().toString() + ".getCategories())\n";
		}

		String getVarValue = "getVariableValue('" + cVar.getName() + "')";
		ExpressionFunction function = (ExpressionFunction) actions.get(2);
		if (function.getValue().equals(AvailableFunction.MAX)) {
			ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : max($value)) \n";
		} else if (function.getValue().equals(AvailableFunction.MIN)) {
			ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : min($value)) \n";
		} else if (function.getValue().equals(AvailableFunction.AVG)) {
			ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : average($value)) \n";
		}

		ruleCore = this.removeDuplicateLines(ruleCore);
		ruleCore += Utils.getThenRuleString();

		// RHS
		if(variableToCalculate != null){
			ruleCore += "	$"+this.getTreeObjectName(variableToCalculate.getReference())+".setVariableValue('"
					+ variableToCalculate.getVariable().getName()
					+ "', $sol);\n";
			ruleCore += "	System.out.println(\"Variable set (" +variableToCalculate.getReference().getName()+", "+ variableToCalculate.getVariable().getName() + ", \" + $sol +\")\");\n";
		}
		return ruleCore;
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the variable passed
	 * If there is no binding, creates a new one (i.e. $var : Question() ...)
	 * @param expValVariable
	 */
	private String checkVariableAssignation(ExpressionValueCustomVariable expValVariable){
		if (this.getTreeObjectName(expValVariable.getReference()) == null) {
			// The variable don't exists and can't have a value assigned
			return this.addConditionVariable(expValVariable);
		}
		return "";
	}

	private String simpleFormCondition(Form form){
		if(this.getTreeObjectName(form)== null){
			this.putTreeObjectName(form, form.getComparationId().toString());
			return "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
		}
		return "";
	}

	private String simpleCategoryConditions(Category category){
		String conditions = "";
		Form form = (Form) category.getParent();
		if(this.getTreeObjectName(form)== null){
			this.putTreeObjectName(form, form.getComparationId().toString());
			conditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
		}
		if(this.getTreeObjectName(category)== null){
			this.putTreeObjectName(category, category.getComparationId().toString());
			conditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
		}
		return conditions;
	}
	private String simpleGroupConditions(Group group){
		String conditions = "";
		TreeObject category = group.getParent();
		TreeObject form = category.getParent();
		if(this.getTreeObjectName(form)== null){
			this.putTreeObjectName(form, form.getComparationId().toString());
			conditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
		}
		if(this.getTreeObjectName(category)== null){
			this.putTreeObjectName(category, category.getComparationId().toString());
			conditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
		}
		if(this.getTreeObjectName(group)== null){
			this.putTreeObjectName(group, group.getComparationId().toString());
			conditions += "	$"+group.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + group.getName() + "')\n";
		}
		return conditions;
	}
	private String simpleQuestionConditions(Question question){
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category){
			TreeObject category = questionParent;
			TreeObject form = category.getParent();
			if(this.getTreeObjectName(form)== null){
				this.putTreeObjectName(form, form.getComparationId().toString());
				conditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			}
			if(this.getTreeObjectName(category)== null){
				this.putTreeObjectName(category, category.getComparationId().toString());
				conditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
			}
			if(this.getTreeObjectName(question)== null){
				this.putTreeObjectName(question, question.getComparationId().toString());
				conditions += "	$"+question.getComparationId().toString()+" : Question() from $"+category.getComparationId().toString()+".getQuestion('" + question.getName() + "')\n";
			}

		}else if(questionParent instanceof Group){
			TreeObject group = questionParent;
			TreeObject category = group.getParent();
			TreeObject form = category.getParent();
			if(this.getTreeObjectName(form)== null){
				this.putTreeObjectName(form, form.getComparationId().toString());
				conditions += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			}
			if(this.getTreeObjectName(category)== null){
				this.putTreeObjectName(category, category.getComparationId().toString());
				conditions += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
			}
			if(this.getTreeObjectName(group)== null){
				this.putTreeObjectName(group, category.getComparationId().toString());
				conditions += "	$"+group.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + group.getName() + "')\n";
			}
			if(this.getTreeObjectName(question)== null){
				this.putTreeObjectName(question, question.getComparationId().toString());
				conditions += "	$"+question.getComparationId().toString()+" : Question() from $"+group.getComparationId().toString()+".getQuestion('" + question.getName() + "')\n";
			}
		}
		return conditions;
	}

}
