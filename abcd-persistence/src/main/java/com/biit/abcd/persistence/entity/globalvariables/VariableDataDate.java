package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA_DATE")
public class VariableDataDate extends VariableData{

	private Timestamp value;
	
	@Override
	public Timestamp getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if(checkType(value)){
			this.value = new Timestamp(((Date)value).getTime());
		}else{
			throw new NotValidTypeInVariableData("The type '" + value.getClass()
					+ "' is not allowed in this variable."); 
		}
	}
	
	public boolean checkType(Object value){
		if(value instanceof Date)
			return true;
		else
			return false;
	}

}
