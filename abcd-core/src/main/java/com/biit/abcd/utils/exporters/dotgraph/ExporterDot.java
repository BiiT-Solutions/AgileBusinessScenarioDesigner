package com.biit.abcd.utils.exporters.dotgraph;

/**
 * Generic class to export dot graph code.
 *
 * @param <T>
 */
public abstract class ExporterDot<T> {

	private final static String GREY = "#808080";
	private final static String LIGHT_GRAY = "#d3d3d3";
	private final static String BLACK = "#000000";

	private final static String READ_ONLY_ALPHA = "44";

	protected static final String DEFAULT_FILL_COLOR = LIGHT_GRAY;
	protected static final String DEFAULT_FONT_COLOR = BLACK;
	protected static final String DEFAULT_SHAPE_COLOR = GREY;
	protected static final String DEFAULT_LINK_COLOR = BLACK;

	protected String fillColor = DEFAULT_FILL_COLOR;
	protected String shapeColor = DEFAULT_SHAPE_COLOR;
	protected String fontColor = DEFAULT_FONT_COLOR;
	protected String linkColor = DEFAULT_LINK_COLOR;
	protected String penWidth = "1.0";

	protected String sizeLimit = "23.30,33.10";
	protected String smallFontSize = "8";
	protected String mediumFontSize = "16";
	protected String bigFontSize = "26";

	/**
	 * Return the string version of the dot graph code for a T structure.
	 * 
	 * @param structure
	 * @return
	 */
	public abstract String export(T structure);

	/**
	 * Generates the list of nodes for a T structure.
	 * 
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeList(T structure);

	/**
	 * Generates the flow between nodes.
	 * 
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeFlow(T structure);

	/**
	 * Generate the nodes enclosed in a element.
	 * 
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeChilds(T structure);

	/**
	 * Limit sizes of the drawing
	 * 
	 * @return
	 */
	public String getSizeLimit() {
		return sizeLimit;
	}

	/**
	 * Set the limits of the drawing
	 * 
	 * @param sizeLimit
	 */
	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public String getSmallFontSize() {
		return smallFontSize;
	}

	public void setSmallFontSize(String smallFontSize) {
		this.smallFontSize = smallFontSize;
	}

	public String getFillColor(boolean readOnly) {
		if (readOnly && fillColor.equals(DEFAULT_FILL_COLOR)) {
			return "\"" + fillColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + fillColor + "\"";
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	protected String filterDotLanguage(String dotCode) {
		return dotCode.replace("\"", "\\\"");
	}

	protected String filterDotLanguageId(String dotCodeId) {
		return dotCodeId.replace("-", "_");
	}

	public String getShapeColor(boolean readOnly) {
		if (readOnly && shapeColor.equals(DEFAULT_SHAPE_COLOR)) {
			return "\"" + shapeColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + shapeColor + "\"";
	}

	public void setShapeColor(String shapeColor) {
		this.shapeColor = shapeColor;
	}

	public String getFontColor(boolean readOnly) {
		if (readOnly && fontColor.equals(DEFAULT_FONT_COLOR)) {
			return "\"" + fontColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + fontColor + "\"";
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getLinkColor(boolean readOnly) {
		if (readOnly && linkColor.equals(DEFAULT_LINK_COLOR)) {
			return "\"" + linkColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + linkColor + "\"";
	}

	public void setLinkColor(String linkColor) {
		this.linkColor = linkColor;
	}

	public String getPenWidth() {
		return penWidth;
	}

	public void setPenWidth(String penWidth) {
		this.penWidth = penWidth;
	}

	public String getMediumFontSize() {
		return mediumFontSize;
	}

	public String getBigFontSize() {
		return bigFontSize;
	}

}
