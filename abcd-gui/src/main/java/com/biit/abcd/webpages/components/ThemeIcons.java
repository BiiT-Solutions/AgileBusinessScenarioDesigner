package com.biit.abcd.webpages.components;

import com.vaadin.server.ThemeResource;

public enum ThemeIcons {

	ACCEPT("appbar.check.svg"),

	CANCEL("appbar.close.svg"),

	ADD("appbar.add.svg"),

	DELETE("appbar.delete.svg"),
	
	EDIT("appbar.edit.svg"),

	LINK("appbar.link.svg"),

	SAVE("appbar.save.svg"),

	CONFIGURE("appbar.settings.svg"),

	SEARCH("appbar.magnify.svg"),

	ZOOM_IN("appbar.magnify.add.svg"),

	ZOOM_OUT("appbar.magnify.minus.svg"),

	PAGE_ADD("appbar.page.add.svg"),

	ARROW_LEFT("appbar.arrow.left.svg"),

	ARROW_RIGHT("appbar.arrow.right.svg"),

	USER("appbar.user.svg"),

	EXPAND("appbar.section.expand.svg"),

	COLLAPSE("appbar.section.collapse.svg"),
	
	FORM_MANAGER_PAGE("appbar.column.one.svg"),
	
	FORM_MANAGER_ADD_FORM("appbar.form.add.svg"),
	
	TREE_DESIGNER_PAGE("appbar.form.edit.svg"),
	
	TREE_DESIGNER_ADD_CATEGORY("appbar.category.add.svg"),
	
	TREE_DESIGNER_ADD_QUESTION("appbar.question.add.svg"),
	
	TREE_DESIGNER_ADD_GROUP("appbar.group.add.svg"),
	
	TREE_DESIGNER_ADD_ANSWER("appbar.answer.add.svg"),
	
	DIAGRAM_BUILDER_PAGE("appbar.diagram.svg"),
	
	DROOLS_RULE_EDITOR_PAGE("appbar.page.code.svg"),
	
	CLEAN("appbar.clean.svg"),
	
	UNDO("appbar.undo.svg"),
	
	REDO("appbar.redo.svg"),
	
	TO_PNG("appbar.page.png.svg"),
	
	TO_SVG("appbar.page.svg.svg"),
	
	TO_BACK("appbar.toBack.svg"),
	
	TO_FRONT("appbar.toFront.svg"),
	
	MOVE_UP("appbar.chevron.up.svg"),
	
	MOVE_DOWN("appbar.chevron.down.svg"),
	
	PAPER("appbar.paper.svg"),
	
	ADD_COLUMN("appbar.table.add.svg"),
	
	REMOVE_COLUMN("appbar.table.delete.svg"),
	
	ADD_ROW("appbar.table.add.row.svg"),
	
	REMOVE_ROW("appbar.table.delete.row.svg"), 
	
	CALCULATOR("appbar.calculator.svg");
	
	private String value;

	ThemeIcons(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public ThemeResource getThemeResource() {
		return new ThemeResource(value);
	}
	
	public String getFile(){
		return value;
	}
}
