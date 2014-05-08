package com.biit.abcd.webpages.elements.formTable;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.elements.formTable.TableFormsCollapsible.CollapsibleCellLabel;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

class CollapsibleFormLeaf extends TableFormsCollapsible.CollapsibleCellLabel {
	/**
	 * 
	 */
	private final TableFormsCollapsible tableFormsCollapsible;
	private static final long serialVersionUID = 712210189357730840L;
	private Form form;
	private Label label;

	public CollapsibleFormLeaf(TableFormsCollapsible tableFormsCollapsible, Form form) {
		this.tableFormsCollapsible = tableFormsCollapsible;
		this.form = form;
		initUi();
		addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = -8168062755223513913L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				CollapsibleFormLeaf.this.tableFormsCollapsible.formTable.setValue(getForm());
			}
		});
	}

	Form getForm() {
		return form;
	}

	private void initUi() {
		setImmediate(true);
		setSpacing(false);
		setSizeUndefined();
		setMargin(new MarginInfo(false, false, false, true));

		if (form != null) {
			label = new Label(form.getName());
		} else {
			label = new Label("");
		}

		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	}

	@Override
	public int compareTo(CollapsibleCellLabel o) {
		if (o instanceof CollapsibleFormParent) {
			// It's a form list
			CollapsibleFormParent cell = (CollapsibleFormParent) o;
			if (cell.getName().equals(form.getName())) {
				// If they have the same name
				return 0;
			} else {
				return form.getName().compareTo(cell.getName());
			}
		} else {
			// It's a form
			CollapsibleFormLeaf cell = (CollapsibleFormLeaf) o;
			if (form.getName().equals(cell.getForm().getName())) {
				// if equals, then version number
				return form.getVersion().compareTo(cell.getForm().getVersion());
			} else {
				return form.getName().compareTo(cell.getForm().getName());
			}
		}
	}
}