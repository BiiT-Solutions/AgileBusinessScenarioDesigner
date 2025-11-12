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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.Node;

/**
 * Base abstract class to generate dot graph code for forms and filtered forms
 *
 * @param <T>
 */
public abstract class ExporterDotFormBasic<T> extends ExporterDot<T> {

	protected String generateDotRule(DiagramLink diagramObject) {
		String dotRule = new String();
		String origin = getDotId(diagramObject.getSource());
		String destiny = getDotId(diagramObject.getTarget());
		String label = "";

		dotRule += "\t" + origin + " -> " + destiny + " [label = \"" + label + "\", fontcolor=" + getFontColor(false) + ", color="
				+ getLinkColor(false) + ", penwidth=" + getPenWidth() + "];\n";

		return dotRule;
	}

	protected String createLegend(Diagram diagram) {
		return "\tsubgraph cluster_0{rank = sink; Legend [shape=none, margin=0, label=<<table border=\"0\"><tr><td>" + diagram.getName()
				+ "</td></tr></table>> fontsize=" + getBigFontSize() + "]}\n";
	}

	protected String getTimestampFormattedString(Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		return formatter.format(timestamp);
	}

	protected String getDotId(Node element) {
		return filterDotLanguageId("id_" + element.getJointjsId());
	}

}
