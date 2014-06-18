package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

public abstract class ExprWChilds extends ExprBasic{

	protected List<ExprBasic> childs;
	
	public ExprWChilds() {
		super();
		childs = new ArrayList<ExprBasic>();
	}
	
	public List<ExprBasic> getChilds(){
		return childs;
	}
	
}
