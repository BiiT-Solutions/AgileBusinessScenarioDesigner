package com.biit.abcd.core.drools.facts.inputform;

import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IGroup;
import com.biit.abcd.core.drools.facts.interfaces.IQuestion;

public class Question extends CommonAttributes implements IQuestion {

	private String value;
	private String tag;
	private String name;
	private IGroup groupParent;
	private ICategory categoryParent;

	public Question(String tag){
		this.tag = tag;
	}

	public boolean isScoreSet() {
		// Retrieve the form which will have the variables
		if(getParent() instanceof IGroup){
			if(((SubmittedForm)((Category)((Group)getParent()).getParent()).getParent()).hasScoreSet(this)) {
				return true;
			} else {
				return false;
			}
		}else{
			if(((SubmittedForm)((Category)getParent()).getParent()).hasScoreSet(this)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isScoreNotSet() {
		return !isScoreSet();
	}

	@Override
	public void setValue(String value){
		this.value = value;
	}

	@Override
	public Object getValue(){
		Object parsedValue = null;
		try{
			parsedValue = Double.parseDouble(value);
		}catch(Exception e){
			parsedValue = value;
		}
		return parsedValue;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(Object parent){
		if(parent instanceof IGroup){
			this.groupParent = (IGroup) parent;
		}else{
			this.categoryParent = (ICategory) parent;
		}
	}

	public Object getParent(){
		if(groupParent != null) {
			return groupParent;
		} else {
			return categoryParent;
		}
	}
}
