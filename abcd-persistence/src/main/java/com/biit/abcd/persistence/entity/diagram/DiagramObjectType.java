package com.biit.abcd.persistence.entity.diagram;

public enum DiagramObjectType {

	SOURCE("biit.SourceNode"),
	SINK("biit.SinkNode"),	
	FORK("biit.ForkNode"),
	RULE("biit.RuleNode"),
	TABLE("biit.TableNode"),
	CALCULATION("biit.CalculationNode"),
	LINK("link");
	
	private String jsonTypeName;
	
	DiagramObjectType(String value){
		jsonTypeName = value;
	}
	
	public String getJsonType(){
		return jsonTypeName;
	}
	
	public static DiagramObjectType getFromJsonType(String jsonType){
		for(DiagramObjectType value: values()){
			if(value.jsonTypeName.equals(jsonType)){
				return value;
			}
		}
		return null;
	}
}
