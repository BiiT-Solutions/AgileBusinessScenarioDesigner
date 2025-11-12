package com.biit.abcd.utils.exporters.dotgraph;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;

public class ExporterDotForm extends ExporterDotFormBasic<Diagram> {

	@Override
	public String export(Diagram diagram) {
		String dotCode = new String();
		dotCode += "digraph G {\n";
		dotCode += "\tsize=\"" + getSizeLimit() + "\";\n";
		dotCode += "\tratio=compress;\n";
		dotCode += "\tranksep=1.25;\n";
		dotCode += "\tgraph [resolution=60, fontsize=" + getMediumFontSize()
				+ ", bgcolor=white, penwidth=1, labelloc=\"b\"];\n";
		dotCode += "\tnode [shape=plaintext, fontsize=" + getMediumFontSize() + ", label=\"\"];\n";
		dotCode += "\tedge [arrowsize=1, fontsize=" + getMediumFontSize() + "];\n";
		dotCode += "\tpagedir=\"TR\";\n";
		if (showAsLandscape(diagram)) {
			dotCode += "\trankdir=\"LR\";\n";
		}
		dotCode += createLegend(diagram);
		dotCode += generateDotNodeList(diagram);
		dotCode += generateDotNodeFlow(diagram);
		dotCode += "}\n";

		return dotCode;
	}

	/**
	 * For representation in paper pages, show it vertically or horizontally.
	 *
	 * @return
	 */
	private boolean showAsLandscape(Diagram diagram) {
		int startingNodes = 0;
		int elements = 0;
		for (DiagramObject object : diagram.getDiagramObjects()) {
			if (object instanceof DiagramSource) {
				startingNodes++;
			}
			if (!(object instanceof DiagramLink)) {
				elements++;
			}
		}

		// Several flows in same diagram.
		if (startingNodes > 2 && (elements / startingNodes) < 6) {
			return true;
		}
		return false;
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
