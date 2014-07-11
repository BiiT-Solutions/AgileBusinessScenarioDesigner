package com.biit.abcd.persistence.entity.globalvariables;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA_NUMBER")
public class VariableDataNumber extends VariableData{

	private Double value;
	
	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData{
		if(!checkType(value)){
			throw new NotValidTypeInVariableData("The type '" + value.getClass()
					+ "' is not allowed in this variable."); 
		}
	}
	
	public boolean checkType(Object value){
		Double aux = null;
		if(value instanceof String){
			try{
				aux = Double.parseDouble((String)value);
			}
			catch(Exception e){
			}
		}else
			aux = (Double)value;
		
		if(aux instanceof Double){
			this.value = aux;
			return true;
		}else
			return false;
	}
	
	/**
	 * Removes trailing zeros. 
	 */
	@Override
	public String toString(){
		return getValue().toString().indexOf(".") < 0 ? getValue().toString() : getValue().toString().replaceAll("0*$", "").replaceAll("\\.$", "");
	}

}
