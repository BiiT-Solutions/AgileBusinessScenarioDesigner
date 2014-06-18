package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_WITH_CHILDS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExprWithChilds extends ExprAtomic {

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "PARENT_OF_EXPRESSION")
	protected List<ExprBasic> childs;

	public ExprWithChilds() {
		super();
		childs = new ArrayList<ExprBasic>();
	}

	public List<ExprBasic> getChilds() {
		return childs;
	}

}
