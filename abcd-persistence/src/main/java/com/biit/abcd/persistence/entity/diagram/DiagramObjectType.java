package com.biit.abcd.persistence.entity.diagram;

public enum DiagramObjectType {

	SOURCE("biit.SourceNode", "source.png"),
	SINK("biit.SinkNode", "sink.png"),	
	FORK("biit.ForkNode", "fork.png"),
	DIAGRAM_CHILD("biit.Diagram", "diagram.png"),
	RULE("biit.RuleNode", "rule.png"),
	TABLE("biit.TableNode", "table.png"),
	CALCULATION("biit.CalculationNode", "expression.png"),
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
	
	public static DiagramObjectType getFromJsonType(String jsonType){
		for(DiagramObjectType value: values()){
			if(value.jsonTypeName.equals(jsonType)){
				return value;
			}
		}
		return null;
	}

	public String getGraphvizIcon() {
		return graphvizIcon;
	}
}
