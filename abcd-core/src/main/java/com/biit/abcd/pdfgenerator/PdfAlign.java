package com.biit.abcd.pdfgenerator;

/**
 * Repurposed alignment codes to be used as enumerator elements.
 *
 */
public enum PdfAlign {

	ALIGN_LEFT(com.lowagie.text.Element.ALIGN_LEFT),

	ALIGN_RIGHT(com.lowagie.text.Element.ALIGN_RIGHT),

	ALIGN_CENTER(com.lowagie.text.Element.ALIGN_CENTER),

	ALIGN_JUSTIFIED(com.lowagie.text.Element.ALIGN_JUSTIFIED),

	;

	private int alignment;

	private PdfAlign(int alignment) {
		this.alignment = alignment;
	}

	public int getAlignment() {
		return alignment;
	}
}
