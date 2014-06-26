package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;

@Entity
@Table(name = "TREE_FORMS_EXPRESSIONS")
public class FormExpression extends StorableObject {

	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	List<ExprBasic> expressions;

	public FormExpression() {
		expressions = new ArrayList<>();
	}

	public List<ExprBasic> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<ExprBasic> expressions) {
		this.expressions = expressions;
	}

	public void addExpression(ExprBasic expression) {
		this.expressions.add(expression);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
