package com.biit.abcd.utils.exporters.dotgraph;

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
