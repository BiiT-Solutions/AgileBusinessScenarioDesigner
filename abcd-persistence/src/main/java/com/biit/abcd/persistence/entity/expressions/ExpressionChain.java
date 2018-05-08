package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * A concatenation of expressions: values, operators, ... that defines a more
 * complex expression.
 */
@Entity
@Table(name = "expressions_chain")
@Cacheable(true)
public class ExpressionChain extends Expression implements INameAttribute {
	private static final long serialVersionUID = -4354289639284022632L;

	private String name;

	// Orderby not works correctly but help the 2nd level cache to not unsort
	// elements.
	@OrderBy(value = "sortSeq ASC")
	@BatchSize(size = 500)
	// @SortComparator(value = ExpressionSort.class)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "expressions_chain_expressions", joinColumns = @JoinColumn(name = "expressions_chain", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "expressions", referencedColumnName = "id"))
	private List<Expression> expressions;

	public ExpressionChain() {
		super();
		expressions = new ArrayList<>();
	}

	public ExpressionChain(Expression... expressions) {
		this.expressions = new ArrayList<>();
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public ExpressionChain(List<Expression> expressions) {
		this.expressions = new ArrayList<>();
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public ExpressionChain(String name) {
		expressions = new ArrayList<>();
		setName(name);
	}

	public ExpressionChain(String name, Expression... expressions) {
		this.expressions = new ArrayList<>();
		setName(name);
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (expressions != null) {
			for (Expression expression : getExpressions()) {
				expression.resetIds();
			}
		}
	}

	public void addExpression(Expression expression) {
		expressions.add(expression);
	}

	public void addExpression(int index, Expression expression) {
		expressions.add(index, expression);
	}

	public Expression removeFirstExpression() {
		return expressions.remove(0);
	}

	public Expression removeLastExpression() {
		return expressions.remove(expressions.size() - 1);
	}

	public void addExpressions(Expression... expressions) {
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public void addExpressions(List<Expression> expressions) {
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	/**
	 * Some characters are not allowed in the Expression Evaluator.
	 * 
	 * @param expression
	 * @return
	 */
	private String filterVariables(Expression expression) {
		// To avoid problems with text values staring with special characters
		String cleanedExpression = expression.getExpression().replaceAll("[^a-zA-Z0-9_]", "_");
		if (cleanedExpression.startsWith("_")) {
			cleanedExpression = "a" + cleanedExpression;
		}
		return cleanedExpression;
	}

	/**
	 * Returns the expression in string format that can be evaluated by a
	 * Expression Evaluator.
	 * 
	 * @return
	 */
	@Override
	public String getExpression() {
		String result = "";
		for (int i = 0; i < expressions.size(); i++) {
			// Dots are not allowed in the Evaluator Expression.
			if ((expressions.get(i) instanceof ExpressionValueString) || (expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (expressions.get(i) instanceof ExpressionValueCustomVariable) || (expressions.get(i) instanceof ExpressionValueGlobalVariable)) {
				result += filterVariables(expressions.get(i)) + " ";
			} else {
				result += expressions.get(i).getExpression() + " ";
			}
		}
		return result.trim();
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void removeExpression(int index) {
		expressions.remove(index);
	}

	/**
	 * Only for using with hibernate.
	 * 
	 * @return
	 */
	public List<Expression> getExpressionsForInitializeSet() {
		return expressions;
	}

	@Override
	public String getName() {
		return name;
	}

	protected Set<TreeObject> getReferencedTreeObjects() {
		List<Expression> expressions = getExpressions();
		Set<TreeObject> references = new HashSet<>();
		for (Expression expression : expressions) {
			if (expression instanceof ExpressionValueTreeObjectReference) {
				references.add(((ExpressionValueTreeObjectReference) expression).getReference());
				continue;
			}
		}
		return references;
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		if (expressions.isEmpty()) {
			return "null";
		}

		String result = "";
		for (Expression expression : expressions) {
			result += expression.getRepresentation(showWhiteCharacter) + " ";
		}
		if (showWhiteCharacter) {
			return result.trim();
		} else {
			return result.trim().replace(" ,", ",").replace("( ", "(").replace(" )", ")");
		}
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = getReferencedTreeObjects();
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if (commonTreeObject.equals(treeObject)) {
				return true;
			}
		}
		return false;
	}

	public void removeAllExpressions() {
		expressions.clear();
	}

	public boolean removeExpression(Expression expression) {
		return expressions.remove(expression);
	}

	public void setExpressions(List<Expression> expressions) {
		removeAllExpressions();
		this.expressions.addAll(expressions);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void updateChildrenSortSeqs() {
		if (getExpressions() != null) {
			for (int i = 0; i < getExpressions().size(); i++) {
				Expression expression = getExpressions().get(i);
				expression.setSortSeq(i);
				if (expression instanceof ExpressionChain) {
					((ExpressionChain) expression).updateChildrenSortSeqs();
				}
			}
		}
	}

	@Override
	public String toString() {
		return getName() + expressions;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (getExpressions() != null) {
			for (Expression expression : getExpressions()) {
				innerStorableObjects.add(expression);
				innerStorableObjects.addAll(expression.getAllInnerStorableObjects());
			}
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionChain) {
			super.copyData(object);
			ExpressionChain expressionChain = (ExpressionChain) object;
			setName(expressionChain.getName());
			for (Expression expression : expressionChain.expressions) {
				try {
					Expression expressionCopied = expression.getClass().newInstance();
					expressionCopied.copyData(expression);
					addExpression(expressionCopied);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionChain.");
				}
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionChain.");
		}
	}

	@Override
	public Object getValue() {
		return getExpressions();
	}
}
