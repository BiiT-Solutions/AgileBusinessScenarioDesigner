package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleParser {

	private HashMap<TreeObject, String> treeObjectDroolsname;

	public RuleParser(){
		this.treeObjectDroolsname = new HashMap<TreeObject, String>();
	}

	public String parse(Rule rule) throws RuleInvalidException {
		String newRule = "";
		String ruleName = rule.getName();

//		RuleChecker.checkRuleValid(rule);
		newRule += Utils.getStartRuleString(ruleName);
		newRule += Utils.getAttributes();
		newRule += Utils.getWhenRuleString();
		newRule += this.createDroolsRule(rule.getConditions(), rule.getActions().getExpressions());
		newRule += Utils.getEndRuleString();
		return newRule;
	}

	public String createDroolsRule(List<Expression> conditions, List<Expression> actions) {
		String ruleCore = "";
		ruleCore += this.parseConditions(conditions);
		ruleCore += this.parseActions(actions);
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
		// Condition of type (cat.score == value)
		if((conditions.size() == 3) &&
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
	private  String parseActions(List<Expression> actions){
		// Action of type (cat.scoreText = "someText") || (cat.score = someValue)
		if ((actions.size() == 3) &&
				(actions.get(0) instanceof ExpressionValueCustomVariable) &&
				(actions.get(1) instanceof ExpressionOperatorMath) &&
				(((ExpressionOperatorMath)actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION)) &&
				((actions.get(2) instanceof ExpressionValueString) ||
						(actions.get(2) instanceof ExpressionValueNumber))) {
			return this.assignationAction(actions);

		}else if ((actions.size() == 5) &&
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
	 * Parse conditions like => Score == value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'), getVariablevalue('cScore') == value )
	 * @param conditions
	 * @return LHS of the rule
	 */
	private  String scoreEqualsCondition(List<Expression> conditions){
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) conditions.get(0);
		ExpressionValueNumber valueNumber = (ExpressionValueNumber) conditions.get(2);

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if(scope instanceof Form) {
			this.treeObjectDroolsname.put(scope, scope.getComparationId().toString());
			ruleCore += "	$"+scope.getComparationId().toString()+" : SubmittedForm( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+")\n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			this.treeObjectDroolsname.put(form, form.getComparationId().toString());
			this.treeObjectDroolsname.put(scope, scope.getComparationId().toString());
			ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			ruleCore += "	$"+scope.getComparationId().toString()+" : Category( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+") from $"+form.getComparationId().toString()+".getCategory('" + scope.getName() + "')\n";

		} else if (scope instanceof Group) {
			TreeObject category = scope.getParent();
			TreeObject form = category.getParent();
			this.treeObjectDroolsname.put(form, form.getComparationId().toString());
			this.treeObjectDroolsname.put(category, category.getComparationId().toString());
			this.treeObjectDroolsname.put(scope, scope.getComparationId().toString());
			ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
			ruleCore += "	$"+scope.getComparationId().toString()+" : Group( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+category.getComparationId().toString()+".getGroup('" + scope.getName() + "')\n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			if (questionParent instanceof Category){
				TreeObject form = questionParent.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(scope, scope.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				ruleCore += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
				ruleCore += "	$"+scope.getComparationId().toString()+" : Question( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";

			}else if(questionParent instanceof Group){
				TreeObject category = questionParent.getParent();
				TreeObject form = category.getParent();
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				this.treeObjectDroolsname.put(category, category.getComparationId().toString());
				this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
				this.treeObjectDroolsname.put(scope, scope.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				ruleCore += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
				ruleCore += "	$"+scope.getComparationId().toString()+" : Question( isScoreSet('"+varName+"'), getNumberVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
			}
		}
		return ruleCore;
	}

	/**
	 * Parse conditions of type Question == answer. <br>
	 * Create drools rule of type Question(getValue()=="answer.getName()")
	 * @param conditions
	 * @return LHS of the rule
	 */
	private  String questionAnswerEqualsCondition(List<Expression> conditions){
		String ruleCore = "";
		ExpressionValueTreeObjectReference expVal1 = (ExpressionValueTreeObjectReference) conditions.get(0);
		ExpressionValueTreeObjectReference expVal2 = (ExpressionValueTreeObjectReference) conditions.get(2);

		// Check if the first object is a question
		TreeObject treeObject1 = expVal1.getReference();
		if((treeObject1 != null) && (treeObject1 instanceof Question)){
			Question question = (Question)treeObject1;
			// Check the second object is an answer
			TreeObject treeObject2 = expVal2.getReference();
			if((treeObject2 != null)){
				// Check the parent of the question
				TreeObject questionParent = question.getParent();
				if (questionParent instanceof Category){
					TreeObject form = questionParent.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() == '" + treeObject2.getName() + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
				}else if(questionParent instanceof Group){
					TreeObject category = questionParent.getParent();
					TreeObject form = category.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(category, category.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() == '" + treeObject2.getName() + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
				}
			}
		}
		return ruleCore;
	}

	/**
	 * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getValue() >= answer.getValue()) && (getValue() <= answer.getValue()))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private  String questionBetweenAnswersCondition(List<Expression> conditions){
		String ruleCore = "";
		ruleCore += "	$form : SubmittedForm()\n";

		ExpressionValueTreeObjectReference expVal1 = (ExpressionValueTreeObjectReference) conditions.get(0);
		ExpressionValueNumber expVal2 = (ExpressionValueNumber) conditions.get(2);
		ExpressionValueNumber expVal3 = (ExpressionValueNumber) conditions.get(4);

		// Check if the first object is a question
		TreeObject treeObject1 = expVal1.getReference();
		if((treeObject1 != null) && (treeObject1 instanceof Question)){
			Question question = (Question)treeObject1;
			Double value1 = expVal2.getValue();
			Double value2 = expVal3.getValue();
			if((value1 != null) && (value2 != null)){
				// Check the parent of the question
				TreeObject questionParent = question.getParent();
				if (questionParent instanceof Category){
					TreeObject form = questionParent.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() >= '" + value1 + "' || <= '" + value2 + "') from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
				}else if(questionParent instanceof Group){
					TreeObject category = questionParent.getParent();
					TreeObject form = category.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(category, category.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() >= '" + value1 + "' || <= '" + value2 + "') from $" + questionParent.getComparationId().toString() + ".getQuestions()\n";
				}
			}
		}
		return ruleCore;
	}

	/**
	 * Parse conditions like => Question IN(answer1, ...) <br>
	 * Create drools rule like => Question( (getValue() in (answer1, ...))
	 * @param conditions
	 * @return LHS of the rule
	 */
	private  String answersInQuestionCondition(List<Expression> conditions){
		String ruleCore = "";
		ExpressionValueTreeObjectReference expVal1 = (ExpressionValueTreeObjectReference) conditions.get(0);
		String inValues = "";
		// Store the values inside the IN condition in a String
		for(int i=2; i<(conditions.size()-1); i+=2){
			ExpressionValueTreeObjectReference ansVal = (ExpressionValueTreeObjectReference)conditions.get(i);
			inValues += "'" + ansVal.getReference().getName() + "', ";
		}
		// Remove the last comma
		inValues = inValues.substring(0, inValues.length()-2);

		// Check if the first object is a question
		TreeObject treeObject1 = expVal1.getReference();
		if((treeObject1 != null) && (treeObject1 instanceof Question)){
			Question question = (Question)treeObject1;
			if(!inValues.isEmpty()){
				// Check the parent of the question
				TreeObject questionParent = treeObject1.getParent();
				if (questionParent instanceof Category){
					TreeObject form = questionParent.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() in( " + inValues + " )) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
				}else if(questionParent instanceof Group){
					TreeObject category = questionParent.getParent();
					TreeObject form = category.getParent();
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					this.treeObjectDroolsname.put(category, category.getComparationId().toString());
					this.treeObjectDroolsname.put(questionParent, questionParent.getComparationId().toString());
					this.treeObjectDroolsname.put(question, question.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
					ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
					ruleCore += "	$"+questionParent.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + questionParent.getName() + "')\n";
					ruleCore += "	$"+question.getComparationId().toString()+" : Question( getValue() in( " + inValues + " )) from $"+questionParent.getComparationId().toString()+".getQuestions()\n";
				}
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
			TreeObject form = treeObject.getParent();
			if(this.treeObjectDroolsname.get(form)== null){
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			}
			if(this.treeObjectDroolsname.get(treeObject)== null){
				this.treeObjectDroolsname.put(treeObject, treeObject.getComparationId().toString());
				ruleCore += "	$"+treeObject.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + treeObject.getName() + "')\n";
			}

		} else if (treeObject instanceof Group) {
			TreeObject category = treeObject.getParent();
			TreeObject form = category.getParent();
			if(this.treeObjectDroolsname.get(form)== null){
				this.treeObjectDroolsname.put(form, form.getComparationId().toString());
				ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
			}
			if(this.treeObjectDroolsname.get(category)== null){
				this.treeObjectDroolsname.put(category, category.getComparationId().toString());
				ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
			}
			if(this.treeObjectDroolsname.get(treeObject)== null){
				this.treeObjectDroolsname.put(treeObject, treeObject.getComparationId().toString());
				ruleCore += "	$"+treeObject.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + treeObject.getName() + "')\n";
			}

		} else if (treeObject instanceof Question) {
			TreeObject questionParent = treeObject.getParent();
			if (questionParent instanceof Category){
				TreeObject category = questionParent;
				TreeObject form = category.getParent();
				if(this.treeObjectDroolsname.get(form)== null){
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				}
				if(this.treeObjectDroolsname.get(category)== null){
					this.treeObjectDroolsname.put(category, category.getComparationId().toString());
					ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				}
				if(this.treeObjectDroolsname.get(treeObject)== null){
					this.treeObjectDroolsname.put(treeObject, treeObject.getComparationId().toString());
					ruleCore += "	$"+treeObject.getComparationId().toString()+" : Question() from $"+category.getComparationId().toString()+".getQuestion('" + treeObject.getName() + "')\n";
				}

			}else if(questionParent instanceof Group){
				TreeObject group = questionParent;
				TreeObject category = group.getParent();
				TreeObject form = category.getParent();
				if(this.treeObjectDroolsname.get(form)== null){
					this.treeObjectDroolsname.put(form, form.getComparationId().toString());
					ruleCore += "	$"+form.getComparationId().toString()+" : SubmittedForm()\n";
				}
				if(this.treeObjectDroolsname.get(category)== null){
					this.treeObjectDroolsname.put(category, category.getComparationId().toString());
					ruleCore += "	$"+category.getComparationId().toString()+" : Category() from $"+form.getComparationId().toString()+".getCategory('" + category.getName() + "')\n";
				}
				if(this.treeObjectDroolsname.get(group)== null){
					this.treeObjectDroolsname.put(group, category.getComparationId().toString());
					ruleCore += "	$"+group.getComparationId().toString()+" : Group() from $"+category.getComparationId().toString()+".getGroup('" + group.getName() + "')\n";
				}
				if(this.treeObjectDroolsname.get(treeObject)== null){
					this.treeObjectDroolsname.put(treeObject, treeObject.getComparationId().toString());
					ruleCore += "	$"+treeObject.getComparationId().toString()+" : Question() from $"+group.getComparationId().toString()+".getQuestion('" + treeObject.getName() + "')\n";
				}
			}
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
			ruleCore += "	$"+this.treeObjectDroolsname.get(var.getReference())+".setVariableValue('" + var.getVariable().getName() + "', '"
					+ valueString.getValue() + "');\n";
			ruleCore += "	System.out.println( \"Variable modified (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ruleCore += "	$"+this.treeObjectDroolsname.get(var.getReference())+".setVariableValue('" + var.getVariable().getName() + "', "
					+ valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable modified (" + var.getReference().getName() + ", "
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
			ruleCore += "	$"+this.treeObjectDroolsname.get(var.getReference())+".setVariableValue('" + customVarName + "', "
					+ "(Double)$"+this.treeObjectDroolsname.get(var2.getReference())+".getNumberVariableValue('" + customVarName + "') "
					+ operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable modified: " + customVarName + "\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(4);
			ruleCore += this.checkVariableAssignation(var2);

			ruleCore += Utils.getThenRuleString();
			ruleCore += "	$"+this.treeObjectDroolsname.get(var.getReference())+".setVariableValue('" + customVarName + "', "
					+ "(Double)$"+this.treeObjectDroolsname.get(var2.getReference())+".getNumberVariableValue('" + customVarName + "') "
					+ operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	System.out.println( \"Variable modified: " + customVarName + "\");\n";

		}
		return ruleCore;
	}

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
	 * Checks the existence of a binding in drools with the the reference of the variable passed
	 * If there is no binding, creates a new one (i.e. $var : Question() ...)
	 * @param expValVariable
	 */
	private String checkVariableAssignation(ExpressionValueCustomVariable expValVariable){
		if (this.treeObjectDroolsname.get(expValVariable.getReference()) == null) {
			// The variable don't exists and can't have a value assigned
			return this.addConditionVariable(expValVariable);
		}
		return "";
	}
}
