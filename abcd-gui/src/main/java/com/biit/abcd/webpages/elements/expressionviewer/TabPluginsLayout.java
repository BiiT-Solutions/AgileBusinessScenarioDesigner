package com.biit.abcd.webpages.elements.expressionviewer;

import java.lang.reflect.Method;
import java.util.Collection;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.drools.plugins.PluginController;
import com.biit.plugins.interfaces.IPlugin;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

public class TabPluginsLayout extends TabLayout {
	private static final long serialVersionUID = -5476757280678114649L;
	private static final String NAME_PROPERTY = "Name";

	private TreeTable pluginsTable;
	private Panel pluginParametersInformation;
	private Button addCustomFunctionButton;

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
		addCustomFunctionButton = new Button(ServerTranslate.translate(LanguageCodes.PLUGINS_TAB_ADD_FUNCTION_BUTTON_CAPTION));
		addCustomFunctionButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4754466212065015629L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (getPluginsTable().getValue() != null) {
					if (getPluginsTable().getValue() instanceof Method) {
						Method methodSelected = (Method) getPluginsTable().getValue();
						IPlugin pluginSelected = (IPlugin) getPluginsTable().getParent(methodSelected);
						// The interface implemented is always IPLUGIN
						Class<?> pluginInterface = IPlugin.class;
						addExpression(new ExpressionPluginMethod(pluginInterface, pluginSelected.getPluginName(),
								methodSelected.getName()));
					}
				}
			}
		});
		addCustomFunctionButton.setEnabled(false);
		addComponent(addCustomFunctionButton);
		setComponentAlignment(addCustomFunctionButton, Alignment.TOP_RIGHT);

		
		createPluginInformation();
		addComponent(getPluginParametersInformation());
	}

	private void createPluginsTable() {
		setPluginsTable(new TreeTable());
		getPluginsTable().addContainerProperty(NAME_PROPERTY, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		getPluginsTable().setCaption(ServerTranslate.translate(LanguageCodes.PLUGINS_TAB_TABLE_CAPTION));
		getPluginsTable().setSizeFull();

		Collection<IPlugin> plugins = PluginController.getInstance().getAllPlugins();
		for (IPlugin plugin : plugins) {
			Object pluginEntry = getPluginsTable().addItem(new Object[] { plugin.getPluginName() }, plugin);
			for (Method method : plugin.getPluginMethods()) {
				// Remove the 'method' prefix of the methods name
				Object methodEntry = getPluginsTable().addItem(new Object[] { method.getName().substring(6) }, method);
				if (methodEntry != null) {
					getPluginsTable().setParent(methodEntry, pluginEntry);
					getPluginsTable().setChildrenAllowed(methodEntry, false);
				}
			}
		}

		getPluginsTable().addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6333216923592191221L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				VerticalLayout verticalLayout = new VerticalLayout();
				if (event.getProperty().getValue() instanceof Method) {
					Method methodSelected = (Method) event.getProperty().getValue();
					IPlugin pluginSelected = (IPlugin) getPluginsTable().getParent(methodSelected);
					for (String parameterString : pluginSelected.getPluginMethodParametersString(methodSelected)) {
						verticalLayout.addComponent(new Label(parameterString));
					}
					// Enable/disable the add function button based on the number of plugins available
					addCustomFunctionButton.setEnabled(true);
				}else{
					addCustomFunctionButton.setEnabled(false);
				}
				getPluginParametersInformation().setContent(verticalLayout);
			}
		});
		getPluginsTable().setSelectable(true);
		getPluginsTable().setNullSelectionAllowed(false);
		getPluginsTable().setImmediate(true);
	}

	private void createPluginInformation() {
		Panel methodInfoPanel = new Panel(ServerTranslate.translate(LanguageCodes.PLUGINS_TAB_INFO_PANEL_CAPTION));
		methodInfoPanel.setStyleName(Runo.PANEL_LIGHT);
		setPluginParametersInformation(methodInfoPanel);
	}

	private TreeTable getPluginsTable() {
		return pluginsTable;
	}

	private void setPluginsTable(TreeTable pluginsTable) {
		this.pluginsTable = pluginsTable;
	}

	private Panel getPluginParametersInformation() {
		return pluginParametersInformation;
	}

	private void setPluginParametersInformation(Panel pluginParametersInformation) {
		this.pluginParametersInformation = pluginParametersInformation;
	}
}
