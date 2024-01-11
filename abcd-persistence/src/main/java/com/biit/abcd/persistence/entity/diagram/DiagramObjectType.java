package com.biit.abcd.persistence.entity.diagram;

public enum DiagramObjectType {

	SOURCE("biit.SourceNode", "source.svg"),
	SINK("biit.SinkNode", "sink.svg"),	
	FORK("biit.ForkNode", "fork.svg"),
	DIAGRAM_CHILD("biit.Diagram", "diagram.svg"),
	RULE("biit.RuleNode", "rule.svg"),
	TABLE("biit.TableNode", "table.svg"),
	CALCULATION("biit.CalculationNode", "expression.svg"),
	REPEAT("biit.BaseRepeatNode", ""),
	LINK("link", "");
	
	private String jsonTypeName;
	private String graphvizIcon;
	
	DiagramObjectType(String jsonTypeName, String graphvizIcon){
		this.jsonTypeName = jsonTypeName;
		this.graphvizIcon = graphvizIcon;
	}
	
	public String getJsonType(){
		return jsonTypeName;
	}
	
	public static DiagramObjectType get(String jsonType){
		for(DiagramObjectType value: values()){
			if(value.jsonTypeName.equalsIgnoreCase(jsonType)){
				return value;
			}
		}
		return null;
	}

	public String getGraphvizIcon() {
		return graphvizIcon;
	}
}
