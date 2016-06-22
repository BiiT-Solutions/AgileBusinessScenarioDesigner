package com.biit.abcd.utils.exporters.dotgraph;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;

public class ExporterDotForm extends ExporterDotFormBasic<Diagram> {

	@Override
	public String export(Diagram diagram) {
		String dotCode = new String();
		dotCode += "digraph G {\n";
		dotCode += "size=\"" + getSizeLimit() + "\";\n";
		dotCode += "\tgraph [ resolution=60, fontsize=" + getSmallFontSize() + ",bgcolor=transparent ];\n";
		dotCode += "\tnode [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tedge [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tpagedir=\"TL\";\n";
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
		String dotFlow = new String();
		for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
			if (diagramObject instanceof DiagramLink) {
				dotFlow += generateDotRule((DiagramLink) diagramObject);
			}
		}

		return dotFlow;
	}

	@Override
	public String generateDotNodeChilds(Diagram diagram) {
		String dotNodes = new String();
		for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
			dotNodes += (new ExporterDotDiagramObject()).generateDotNodeList(diagramObject);
		}
		return dotNodes;
	}

}
