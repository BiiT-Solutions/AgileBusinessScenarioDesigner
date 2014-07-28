package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class TableRuleParser {

	public static String parseTableRule(Form form, TableRule tableRule){
		String newRules = "";
		String tableRuleName = tableRule.getName();
		int i = 0;

		// One rule for each row
		for(TableRuleRow row : tableRule.getRules()){
			newRules += Utils.getStartRuleString(tableRuleName+"_row_"+i);
			newRules += createAttributes();
			newRules += Utils.getWhenRuleString();
			newRules += createRule(row);
			newRules += Utils.getEndRuleString();
			i++;
		}

		return newRules;
	}

	private static String createAttributes(){
		return "";
	}

	private static String createRule(TableRuleRow row){
		String ruleCore = "";
		List<Expression> auxExpressions = new ArrayList<Expression>();

		// Conditions
		List<Expression> conditionList = row.getConditions();
		for(Expression condition : conditionList){
			if(condition instanceof ExpressionValueTreeObjectReference){
				auxExpressions.add(condition);
			}else if(condition instanceof ExpressionChain){
				for(Expression answerExpression : ((ExpressionChain)condition).getExpressions()){
					if(answerExpression instanceof ExpressionValueTreeObjectReference){
						auxExpressions.add(answerExpression);
					}
				}
			}
		}
		// Actions
		ExpressionChain action = row.getAction();
		List<Expression> expressionList = action.getExpressions();
		// If the third operator is a ValueNumber
		if(expressionList.get(2) instanceof ExpressionValueNumber){
			// Then we have a simple assignation action
			auxExpressions.add(expressionList.get(2));
		}

		// Drools rule creation
		if((auxExpressions.size() > 2)){
			ruleCore += "	$q : Question(isScoreNotSet(), getTag() == \"" +
					((Question)((ExpressionValueTreeObjectReference)auxExpressions.get(0)).getReference()).getName() +
					"\", getAnswer().getValue() == \""+
					((Answer)((ExpressionValueTreeObjectReference)auxExpressions.get(1)).getReference()).getName()
					+"\") from $form.getQuestions()\n";
			ruleCore += "then\n";
			ruleCore += "	$q.getAnswer().setScore("+
					((ExpressionValueNumber)auxExpressions.get(2)).getValue().toString()
					+");\n";
			ruleCore += "	out.println( \"Score modified: \" + $q.getTag() + \" = \" + $q.getAnswer().getScore()); \n";
		}

		return ruleCore;
	}

	//	public static String createQuestion(ExpressionValueTreeObjectReference questionExpression){
	//		String questionString = "";
	//		Object questionReference = questionExpression.getReference();
	//		if(questionReference != null){
	//			Question question = (Question)questionReference;
	//			questionString += "	$qTo : ExpressionValueTreeObjectReference() from $trr.getConditions()\n";
	//			questionString += "	Question( name == \"" + question.getName() + "\" ) from $qTo.reference\n";
	//		}
	//		return questionString;
	//	}
	//
	//	public static String createSimpleAnswer(ExpressionValueTreeObjectReference answerExpression){
	//		String answerString = "";
	//		Object answerReference = answerExpression.getReference();
	//		if(answerReference != null){
	//			Answer answer = (Answer)answerReference;
	//			answerString += "	$ec : ExpressionChain() from $trr.getConditions()\n";
	//			answerString += "	$aTo : ExpressionValueTreeObjectReference() from $ec.getExpressions()\n";
	//			answerString += "	$ar : Answer( name == \"" + answer.getName() + "\" ) from $aTo.reference\n";
	//		}
	//		return answerString;
	//	}
	//
	//	public static String createSimpleAssignationAction(ExpressionValueFormCustomVariable customVariable, ExpressionValueNumber valueNumber) {
	//		String actionString = "";
	//		actionString += "	$a : ExpressionChain() from $trr.getAction()\n";
	//		actionString += "	$aVar : ExpressionValueFormCustomVariable() from $a.getExpressions()\n";
	//		actionString += "	$aCustomVar : CustomVariable() from $aVar.getVariable()\n";
	//		actionString += "	$aVal : ExpressionValueNumber() from $a.getExpressions()\n";
	//		actionString += "then\n";
	//		// Create the form variable
	//		actionString += "	FormVariable formVar = new FormVariable(); \n";
	//		actionString += "	formVar.setCustomVariable($aCustomVar); \n";
	//		actionString += "	formVar.setValue($aVal.getValue()); \n";
	//		actionString += "	insert(formVar); \n";
	//		return actionString;
	//	}
}
