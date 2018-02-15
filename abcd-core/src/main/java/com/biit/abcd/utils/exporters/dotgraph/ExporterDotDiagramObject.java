package com.biit.abcd.utils.exporters.dotgraph;

import java.io.File;

import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;

public class ExporterDotDiagramObject extends ExporterDot<DiagramObject> {

	@Override
	public String export(DiagramObject diagramObject) {
		return null;
	}

	@Override
	public String generateDotNodeList(DiagramObject diagramObject) {
		if (diagramObject instanceof DiagramElement) {
			String dotElement = new String();
			dotElement = "\t" + getDotName((DiagramElement) diagramObject) + ";\n";
			return dotElement;
		}
		return "";
	}

	@Override
	public String generateDotNodeFlow(DiagramObject diagramObject) {
		return null;
	}

	@Override
	public String generateDotNodeChilds(DiagramObject diagramObject) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the code with the title of the question.
	 * 
	 * @param diagramElement
	 * @return
	 */
	private String getDotName(DiagramElement diagramElement) {
		if (diagramElement.getType().equals(DiagramObjectType.SOURCE)) {
			return "subgraph cluster_" + getDotId(diagramElement) + " {label=\"" + filterDotLanguage(diagramElement.getText().getText()) + "\"; "
					+ getDotId(diagramElement) + " [image=\"" + getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon() + "\"];}";
		} else if (diagramElement.getType().equals(DiagramObjectType.SINK)) {
			return "subgraph cluster_" + getDotId(diagramElement) + " {label=\"" + filterDotLanguage(diagramElement.getText().getText()) + "\"; "
					+ getDotId(diagramElement) + " [image=\"" + getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon() + "\"];}";
		} else if (diagramElement.getType().equals(DiagramObjectType.FORK)) {
			return "subgraph cluster_" + getDotId(diagramElement) + " {label=\"" + filterDotLanguage(diagramElement.getText().getText()) + "\"; "
					+ getDotId(diagramElement) + " [image=\"" + getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon() + "\"];}";
		} else if (diagramElement.getType().equals(DiagramObjectType.DIAGRAM_CHILD)) {
			if (((DiagramChild) diagramElement).getDiagram() != null) {
				return "subgraph cluster_" + getDotId(diagramElement) + " {label=\""
						+ filterDotLanguage(((DiagramChild) diagramElement).getDiagram().getName()) + "\"; " + getDotId(diagramElement) + " [image=\""
						+ getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon() + "\"];}";
			}
		} else if (diagramElement.getType().equals(DiagramObjectType.RULE)) {
			if (((DiagramRule) diagramElement).getRule() != null) {
				return "subgraph cluster_" + getDotId(diagramElement) + " {label=\"" + filterDotLanguage(((DiagramRule) diagramElement).getRule().getName())
						+ "\"; " + getDotId(diagramElement) + " [image=\"" + getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon()
						+ "\"];}";
			}
		} else if (diagramElement.getType().equals(DiagramObjectType.TABLE)) {
			if (((DiagramTable) diagramElement).getTable() != null) {
				return "subgraph cluster_" + getDotId(diagramElement) + " {label=\"" + filterDotLanguage(((DiagramTable) diagramElement).getTable().getName())
						+ "\"; " + getDotId(diagramElement) + " [image=\"" + getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon()
						+ "\"];}";
			}
		} else if (diagramElement.getType().equals(DiagramObjectType.CALCULATION)) {
			if (((DiagramExpression) diagramElement).getExpression() != null) {
				return "subgraph cluster_" + getDotId(diagramElement) + " {label=\""
						+ filterDotLanguage(((DiagramExpression) diagramElement).getExpression().getName()) + "\"; " + getDotId(diagramElement) + " [image=\""
						+ getIconsFolder() + File.separator + diagramElement.getType().getGraphvizIcon() + "\"];}";
			}
		}
		return new String();
	}

	protected String getDotId(DiagramObject element) {
		return filterDotLanguageId("id_" + element.getJointjsId());
	}

	private String getIconsFolder() {
		return DotImageCreator.ICON_TEMP_FOLDER.toString();
	}
}
