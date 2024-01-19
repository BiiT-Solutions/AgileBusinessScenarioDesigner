package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Column;
import java.io.Serializable;

public class DiagramText implements Serializable {
    private static final long serialVersionUID = 1533578154303621298L;
    private String text;
    private String fill;

    @Column(name = "font_size")
    private String fontSize;

    private String stroke;

    @Column(name = "stroke_width")
    private String strokeWidth;

    public DiagramText() {
        // text = "";
        fill = "#000000";
        fontSize = "16";
        stroke = "#000000";
        strokeWidth = "0";
    }

    public DiagramText(String text) {
        this();
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public String toString() {
        return "{text: " + text + ", fill: " + fill + ", font-size:" + fontSize + ", stroke:" + stroke
                + ", stroke-width:" + strokeWidth;
    }
}
