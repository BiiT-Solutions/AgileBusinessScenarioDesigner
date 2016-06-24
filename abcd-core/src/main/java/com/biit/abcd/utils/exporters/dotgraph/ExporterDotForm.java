package com.biit.abcd.utils.exporters.dotgraph;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;

public class ExporterDotForm extends ExporterDotFormBasic<Diagram> {

	@Override
	public String export(Diagram diagram) {
		String dotCode = new String();
		dotCode += "digraph G {\n";
		dotCode += "\tsize=\"" + getSizeLimit() + "\";\n";
		dotCode += "\tratio=compress;\n";
		dotCode += "\tranksep=1.25;\n";
		dotCode += "\tgraph [resolution=60, fontsize=" + getMediumFontSize() + ",bgcolor=transparent, penwidth=0, labelloc=\"b\"];\n";
		dotCode += "\tnode [shape=plaintext, fontsize=" + getMediumFontSize() + ", label=\"\"];\n";
		dotCode += "\tedge [arrowsize=1, fontsize=" + getMediumFontSize() + "];\n";
		dotCode += "\tpagedir=\"TR\";\n";
		dotCode += createLegend(diagram);
		dotCode += generateDotNodeList(diagram);
		dotCode += generateDotNodeFlow(diagram);
		dotCode += "}\n";

		return dotCode;
	}

	@Override
	public String generateDotNodeList(Diagram diagram) {
		return generateDotNodeChilds(diagram);
	}

	@Override
	public String generateDotNodeFlow(Diagram diagram) {
		String dotFlow = new String("\n\t/* Relationships */\n");
		for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
			if (diagramObject instanceof DiagramLink) {
				dotFlow += generateDotRule((DiagramLink) diagramObject);
			}
		}

		return dotFlow;
	}

	@Override
	public String generateDotNodeChilds(Diagram diagram) {
		String dotNodes = new String("\n\t/* Nodes */\n");
		for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
			dotNodes += (new ExporterDotDiagramObject()).generateDotNodeList(diagramObject);
		}
		return dotNodes;
	}

}
