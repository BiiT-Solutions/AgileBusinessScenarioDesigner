package com.biit.abcd.pdfgenerator.utils;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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
