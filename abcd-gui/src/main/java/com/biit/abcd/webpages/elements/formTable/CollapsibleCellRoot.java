package com.biit.abcd.webpages.elements.formTable;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

class CollapsibleCellRoot extends CollapsibleCellLabel {
	/**
	 * 
	 */
	private final FormsCollapsibleTable tableFormsCollapsible;
	private static final long serialVersionUID = 3002037698058869287L;
	private List<Form> forms;
	private Button collapseButton;
	private Button expandButton;
	private Label label;

	public CollapsibleCellRoot(FormsCollapsibleTable tableFormsCollapsible, List<Form> forms) {
		this.tableFormsCollapsible = tableFormsCollapsible;
		this.forms = forms;
		initUi();
		addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = -8168062755223513913L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				CollapsibleCellRoot.this.tableFormsCollapsible.getFormTable().setValue(getForms());
			}
		});
	}

	public List<Form> getForms() {
		return forms;
	}

	public String getName() {

		String name;
		if (forms != null && forms.get(0) != null) {
			name = new String(forms.get(0).getName());
		} else {
			name = new String("");
		}
		return name;
	}

	private void initUi() {
		setImmediate(true);
		setSpacing(true);
		setSizeUndefined();

		expandButton = new IconButton(ThemeIcons.EXPAND.getFile(), "Expand forms", IconSize.SMALL, new ClickListener() {
			private static final long serialVersionUID = -8790931587642079896L;

			@Override
			public void buttonClick(ClickEvent event) {
				uncollapse();
			}
		});

		collapseButton = new IconButton(ThemeIcons.COLLAPSE.getFile(), "Contract forms", IconSize.SMALL, new ClickListener() {
			private static final long serialVersionUID = 3722311031488330957L;

			@Override
			public void buttonClick(ClickEvent event) {
				collapse();
			}
		});

		if (forms != null && forms.get(0) != null) {
			label = new Label(forms.get(0).getName());
		} else {
			label = new Label("");
		}

		addComponent(expandButton);
		setComponentAlignment(expandButton, Alignment.MIDDLE_LEFT);
		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	}

	private void collapse() {
		removeAllComponents();
		addComponent(expandButton);
		setComponentAlignment(expandButton, Alignment.MIDDLE_LEFT);
		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		markAsDirty();
		this.tableFormsCollapsible.removeChildRows(forms);
	}

	void uncollapse() {
		removeAllComponents();
		addComponent(collapseButton);
		setComponentAlignment(collapseButton, Alignment.MIDDLE_LEFT);
		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		markAsDirty();
		this.tableFormsCollapsible.addChildRows(forms);
	}

	public int compareTo(CollapsibleCellRoot o) {
		String ori = "";
		String dest = "";

		if (forms != null && forms.get(0) != null) {
			ori = forms.get(0).getName();
		}
		if (o.forms != null && o.forms.get(0) != null) {
			dest = o.forms.get(0).getName();
		}

		return ori.compareTo(dest);
	}

	public int compareTo(CollapsibleCellLeaf o) {
		String ori = "";
		String dest = "";

		ori = getName();

		dest = o.getForm().getName();

		if (ori.equals(dest)) {
			return 0;
		} else {
			return ori.compareTo(dest);
		}
	}

	@Override
	public int compareTo(CollapsibleCellLabel o) {
		if (o instanceof CollapsibleCellRoot) {
			return compareTo((CollapsibleCellRoot) o);
		} else {
			return compareTo((CollapsibleCellLeaf) o);
		}
	}
}