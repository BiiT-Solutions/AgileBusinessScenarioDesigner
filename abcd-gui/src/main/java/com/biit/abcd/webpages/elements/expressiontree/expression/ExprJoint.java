package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.util.HashSet;
import java.util.Set;

public abstract class ExprJoint extends ExprBasic{
	
	public class JointValue{
		
		private String value;
		private String caption;
		
		public JointValue(String value, String caption) {
			this.setValue(value);
			this.setCaption(caption);
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}
		
	}
	
	private Set<JointValue> acceptedValues;
	private JointValue value; 

	public ExprJoint() {
		acceptedValues = new HashSet<>();
		value = null;
	}
	
	public abstract String getValueNullCaption();

	@Override
	public String getExpressionTableString() {
		if(value==null){
			return " "+getValueNullCaption()+" ";
		}else{
			return " "+value.getCaption()+" ";
		}
	}

	public Set<JointValue> getAcceptedValues() {
		return acceptedValues;
	}

	public JointValue getValue() {
		return value;
	}

	public void setValue(JointValue value) {
		this.value = value;
	}
	
}
