package com.biit.abcd.persistence.entity.expressions;

import java.util.Comparator;
import java.util.List;

/**
 * Order an expression chain by the tree objects that compose the expression. Used in Table Decision Rules. Example:
 * question1 = answer1, question1 = answer2, question2 = answer1, question2 = answer2 ....
 */
public class ExpressionChainHierarchyComparator implements Comparator<ExpressionChain> {

	@Override
	public int compare(ExpressionChain expressionChain1, ExpressionChain expressionChain2) {
		//This two condition can't happen. still we guard it.
		if(expressionChain1==null){
			return -1;
		}
		if(expressionChain2==null){
			return 1;
		}
		
		List<Expression> expressions1 = expressionChain1.getExpressions();
		List<Expression> expressions2 = expressionChain2.getExpressions();
		
		for (int i = 0; i < Math.min(expressions1.size(), expressions2.size()); i++) {
			int comparation = 0;
			
			if (expressions1.get(i) instanceof ExpressionValueTreeObjectReference && expressions2.get(i) instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference reference1 = (ExpressionValueTreeObjectReference) expressions1.get(i);
				ExpressionValueTreeObjectReference reference2 = (ExpressionValueTreeObjectReference) expressions2.get(i);
				
				//If both are null do nothing (comparation = 0)
				if(reference1.getReference()==null && reference2.getReference()==null){
					comparation = 0;
				}else if(reference1.getReference()==null && reference2.getReference()!=null){
					return -1;
				}else if(reference1.getReference()!=null && reference2.getReference()==null){
					return 1;
				}else {
					comparation = reference1.getReference().compareTo(reference2.getReference());
				}
			} else if (expressions1.get(i) instanceof ExpressionChain && expressions2.get(i) instanceof ExpressionChain) {
				ExpressionChain chain1 = (ExpressionChain) expressions1.get(i);
				ExpressionChain chain2 = (ExpressionChain) expressions2.get(i);
								
				if(chain1.getExpressions().isEmpty()){
					return -1;
				}else if(chain2.getExpressions().isEmpty()){
					return 1;
				}
				
				// No differences among TreeObjects. Use representation as comparation
				comparation = chain1.getRepresentation().compareTo(chain2.getRepresentation());
			}
			if (comparation != 0) {
				return comparation;
			}
		}
				
		return 0;
	}
}
