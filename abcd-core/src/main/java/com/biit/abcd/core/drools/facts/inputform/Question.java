package com.biit.abcd.core.drools.facts.inputform;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

	@Override
	public void setValue(String value){
		this.value = value;
	}

	@Override
	public Object getValue(){
		Object parsedValue = null;
		try{
//			System.out.println("TRY DOUBLE");
			parsedValue = Double.parseDouble(this.value);
		}catch(Exception e){
			try {
//				System.out.println("TRY DATE");
				parsedValue = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this.value);
			} catch (Exception e1) {
//				System.out.println("STRING");
				parsedValue = this.value;
			}
		}
//		System.out.println("PARSED VALUE: " + parsedValue);
//		System.out.println("PARSED VALUE CLASS: " + parsedValue.getClass());
		return parsedValue;
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return this.name;
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
		if(this.groupParent != null) {
			return this.groupParent;
		} else {
			return this.categoryParent;
		}
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		if(this.getParent() instanceof IGroup){
			if(((SubmittedForm)((Category)((Group)this.getParent()).getParent()).getParent()).hasScoreSet(this, varName)) {
				return true;
			} else {
				return false;
			}
		}else{
			if(((SubmittedForm)((Category)this.getParent()).getParent()).hasScoreSet(this, varName)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !this.isScoreSet(varName);
	}

	public boolean isScoreNotSet() {
		return !this.isScoreSet("");
	}

	public Object getVariableValue(String varName){
		if(this.getParent() instanceof IGroup){
			return ((SubmittedForm)((Category)((Group)this.getParent()).getParent()).getParent()).getVariableValue(this, varName);
		}else{
			return ((SubmittedForm)((Category)this.getParent()).getParent()).getVariableValue(this, varName);
		}
	}

	public Number getNumberVariableValue(String varName){
		if(this.getParent() instanceof IGroup){
			return ((SubmittedForm)((Category)((Group)this.getParent()).getParent()).getParent()).getNumberVariableValue(this, varName);
		}else{
			return ((SubmittedForm)((Category)this.getParent()).getParent()).getNumberVariableValue(this, varName);
		}
	}

	public void setVariableValue(String varName, Object value){
		if(this.getParent() instanceof IGroup){
			((SubmittedForm)((Category)((Group)this.getParent()).getParent()).getParent()).setVariableValue(this, varName, value);
		}else{
			((SubmittedForm)((Category)this.getParent()).getParent()).setVariableValue(this, varName, value);
		}

	}
}
