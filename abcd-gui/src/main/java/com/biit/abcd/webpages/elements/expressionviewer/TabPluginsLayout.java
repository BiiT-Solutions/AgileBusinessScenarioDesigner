package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.Collection;

import com.biit.abcd.core.PluginController;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.plugins.interfaces.IPlugin;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

public class TabPluginsLayout extends TabLayout {
	private static final long serialVersionUID = -5476757280678114649L;
	private static final String NAME_PROPERTY = "Name";

	private Table pluginsTable;
	private Label pluginParametersInformation;

	public TabPluginsLayout() {
		createContent();
	}

	private void createContent() {
		// Create the generic tree objects table
		createPluginsTable();
		setSpacing(true);
		getPluginsTable().setPageLength(8);
		addComponent(getPluginsTable());
		setExpandRatio(getPluginsTable(), 0.5f);
		// addTreeObjectButton = new Button(
		// ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_GENERIC_ELEMENT));
		// addTreeObjectButton.addClickListener(new ClickListener() {
		// private static final long serialVersionUID = -4754466212065015629L;
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// if (getPluginsTable().getValue() != null) {
		// addExpression(new ExpressionValueString();
		// }
		// }
		// });
		// addComponent(addTreeObjectButton);
		// setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);

		createPluginInformation();

	}

	@SuppressWarnings("unchecked")
	private void createPluginsTable() {
		setPluginsTable(new Table());
		getPluginsTable().addContainerProperty(NAME_PROPERTY, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		getPluginsTable().setCaption(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_GENERIC_ELEMENTS));
		getPluginsTable().setSizeFull();

		Collection<IPlugin> plugins = PluginController.getInstance().getAllPlugins();
		for (IPlugin plugin : plugins) {
			Item item = getPluginsTable().addItem(plugin);
			item.getItemProperty(NAME_PROPERTY).setValue(plugin.getName());
		}

		getPluginsTable().addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6333216923592191221L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				getPluginParametersInformation().setCaption(
						((IPlugin) getPluginsTable().getValue()).parametersInformation());
			}
		});

		getPluginsTable().setSelectable(true);
		getPluginsTable().setNullSelectionAllowed(false);
		getPluginsTable().setImmediate(true);
	}

	private void createPluginInformation() {
		setPluginParametersInformation(new Label());
	}

	private Table getPluginsTable() {
		return pluginsTable;
	}

	private void setPluginsTable(Table pluginsTable) {
		this.pluginsTable = pluginsTable;
	}

	private Label getPluginParametersInformation() {
		return pluginParametersInformation;
	}

	private void setPluginParametersInformation(Label pluginParametersInformation) {
		this.pluginParametersInformation = pluginParametersInformation;
	}
}
