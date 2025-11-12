package com.biit.abcd.webpages.elements.diagram.builder;

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

import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class FormDiagramBuilderUpperMenu extends UpperMenu {

	private static final long serialVersionUID = 1619944897938816750L;
	private IconButton newDiagramButton, deleteDiagramButton, clearButton, saveButton, undoButton, redoButton,
			toFrontButton, toBackButton, toSvgButton, toPngButton;

	public FormDiagramBuilderUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_SAVE_TOOLTIP);
		newDiagramButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_NEW_DIAGRAM_CAPTION,
				ThemeIcon.ADD_DIAGRAM, LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_NEW_DIAGRAM_TOOLTIP);
		deleteDiagramButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_DELETE_DIAGRAM_CAPTION,
				ThemeIcon.REMOVE_DIAGRAM, LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_DELETE_DIAGRAM_TOOLTIP);
		clearButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_CLEAR_CAPTION, ThemeIcon.CLEAN,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_CLEAR_TOOLTIP);
		undoButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_UNDO_CAPTION, ThemeIcon.UNDO,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_UNDO_TOOLTIP);
		redoButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_REDO_CAPTION, ThemeIcon.REDO,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_REDO_TOOLTIP);
		toFrontButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOFRONT_CAPTION,
				ThemeIcon.TO_FRONT, LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOFRONT_TOOLTIP);
		toBackButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOBACK_CAPTION,
				ThemeIcon.TO_BACK, LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOBACK_TOOLTIP);
		toSvgButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOSVG_CAPTION, ThemeIcon.TO_SVG,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOSVG_TOOLTIP);
		toPngButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOPNG_CAPTION, ThemeIcon.TO_PNG,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOPNG_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newDiagramButton);
		addIconButton(deleteDiagramButton);
		addIconButton(clearButton);
		addIconButton(undoButton);
		addIconButton(redoButton);
		addIconButton(toFrontButton);
		addIconButton(toBackButton);
		addIconButton(toSvgButton);
		addIconButton(toPngButton);

		for (Button button : getDisabledButtons()) {
			if (!button.equals(toSvgButton) && !button.equals(toPngButton)) {
				button.setEnabled(false);
			}
		}
	}

	public void addNewDiagramButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newDiagramButton)) {
			newDiagramButton.addClickListener(listener);
		}
	}

	public void addDeleteNewDiagramButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(deleteDiagramButton)) {
			deleteDiagramButton.addClickListener(listener);
		}
	}

	public void addClearButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(clearButton)) {
			clearButton.addClickListener(listener);
		}
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(saveButton)) {
			saveButton.addClickListener(listener);
		}
	}

	public void addUndoButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(undoButton)) {
			undoButton.addClickListener(listener);
		}
	}

	public void addRedoButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(redoButton)) {
			redoButton.addClickListener(listener);
		}
	}

	public void addToFrontButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(toFrontButton)) {
			toFrontButton.addClickListener(listener);
		}
	}

	public void addToBackButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(toBackButton)) {
			toBackButton.addClickListener(listener);
		}
	}

	public void addToSvgButtonClickListener(Button.ClickListener listener) {
		toSvgButton.addClickListener(listener);
	}

	public void addToPngButtonClickListener(Button.ClickListener listener) {
		toPngButton.addClickListener(listener);
	}

	@Override
	public Set<Button> getSecuredButtons() {
		// Disable all buttons.
		return getButtons();
	}
}
