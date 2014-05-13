package com.biit.abcd.webpages;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.components.UpperMenu;

public class FormDiagramBuilderUpperMenu extends UpperMenu{

	private static final long serialVersionUID = 1619944897938816750L;
	private IconButton clearButton, saveButton, undoButton, redoButton, toFrontButton, toBackButton, toSvgButton, toPngButton; 
	
	public FormDiagramBuilderUpperMenu(){
		super();
		defineMenu();
	}
	
	private void defineMenu(){
		
		clearButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_CLEAR_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_CLEAR_TOOLTIP);
		saveButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_SAVE_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_SAVE_TOOLTIP);
		undoButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_UNDO_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_UNDO_TOOLTIP);
		redoButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_REDO_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_REDO_TOOLTIP);
		toFrontButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOFRONT_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOFRONT_TOOLTIP);
		toBackButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOBACK_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOBACK_TOOLTIP);
		toSvgButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOSVG_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOSVG_TOOLTIP);
		toPngButton = new IconButton(LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOPNG_CAPTION,ThemeIcons.ACCEPT,LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_TOPNG_TOOLTIP);
		
	}

}
