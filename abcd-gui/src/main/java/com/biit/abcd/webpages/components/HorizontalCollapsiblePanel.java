package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class HorizontalCollapsiblePanel extends CustomComponent {
	private static final long serialVersionUID = -6807839112376876952L;
	private static final String CLASSNAME_OPENED = "v-horizontal-collapsible-panel-opened";
	private static final String CLASSNAME_CLOSED = "v-horizontal-collapsible-panel-closed";
	private static final String CLASSNAME_BUTTON_LAYOUT = "v-horizontal-collapsible-panel-button-layout";
	private static final String menuWidth = "300px";
	private static final String menuWidthClosed = "0px";
	private VerticalLayout menuLayout;
	private VerticalLayout buttonMenu;
	private CssLayout rootLayout;
	private HorizontalLayout titleLayout;
	private Component content;

	public HorizontalCollapsiblePanel() {

		rootLayout = new CssLayout();
		rootLayout.setSizeFull();

		menuLayout = new VerticalLayout();
		menuLayout.setWidth(menuWidthClosed);
		menuLayout.setHeight("100%");
		menuLayout.setStyleName(CLASSNAME_CLOSED);

		buttonMenu = new VerticalLayout();
		buttonMenu.setWidth(null);
		buttonMenu.setHeight(null);
		buttonMenu.setMargin(true);
		buttonMenu.setSpacing(true);
		buttonMenu.setStyleName(CLASSNAME_BUTTON_LAYOUT);

		VerticalLayout testLayout = new VerticalLayout();
		testLayout.setHeight("100%");
		addMenuTab(testLayout, ThemeIcons.COLLAPSE, ThemeIcons.EXPAND,
				LanguageCodes.COLLAPSABLE_PANEL_COLLAPSE_TOOLTIP, LanguageCodes.COLLAPSABLE_PANEL_EXPAND_TOOLTIP);

		setSizeFull();
		setCompositionRoot(rootLayout);
	}

	public void addMenuTab(Component component, ThemeIcons enabledIcon, ThemeIcons disabledIcon,
			LanguageCodes enabledTooltip, LanguageCodes disabledTooltip) {
		final CollapseButtonTab collapseButtonTab = new CollapseButtonTab(component, enabledIcon, disabledIcon,
				enabledTooltip, disabledTooltip);
		buttonMenu.addComponent(collapseButtonTab);
		collapseButtonTab.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -754510955831311296L;

			@Override
			public void buttonClick(ClickEvent event) {
				menuLayout.removeAllComponents();
				menuLayout.setWidth(menuWidthClosed);
				menuLayout.setStyleName(CLASSNAME_CLOSED);
				if (!collapseButtonTab.isValue()) {
					menuLayout.addComponent(collapseButtonTab.getComponent());
					menuLayout.setWidth(menuWidth);
					menuLayout.setStyleName(CLASSNAME_OPENED);
				}
				collapseButtonTab.setValue(!collapseButtonTab.isValue());
			}
		});
	}

	public void setContent(Component component) {
		this.content = component;
		content.setSizeFull();
	}

	@Override
	public void attach() {
		super.attach();
		rootLayout.addComponent(menuLayout);
		rootLayout.addComponent(buttonMenu);
		rootLayout.addComponent(content);
		rootLayout.markAsDirty();

	}

	private class CollapseButtonTab extends IconButton {
		private static final long serialVersionUID = -443120664603138986L;
		private static final String CLASSNAME_ENABLED = "v-button-tab-enabled";
		private static final String CLASSNAME_DISABLED = "v-button-tab-disabled";
		private ThemeIcons enabledIcon;
		private ThemeIcons disabledIcon;
		private LanguageCodes enabledTooltip;
		private LanguageCodes disabledTooltip;
		private Component component;
		private boolean currentValue;

		public CollapseButtonTab(Component component, ThemeIcons enabledIcon, ThemeIcons disabledIcon,
				LanguageCodes enabledTooltip, LanguageCodes disabledTooltip) {
			super(disabledIcon, IconSize.MEDIUM, disabledTooltip);
			this.enabledIcon = enabledIcon;
			this.disabledIcon = disabledIcon;
			this.enabledTooltip = enabledTooltip;
			this.disabledTooltip = disabledTooltip;

			setImmediate(true);
			setValue(false);
			this.component = component;
		}

		public void setValue(boolean enabled) {
			currentValue = enabled;
			if (enabled) {
				setStyleName(CLASSNAME_ENABLED);
				setIcon(this.enabledIcon);
				setDescription(ServerTranslate.tr(enabledTooltip));
			} else {
				setStyleName(CLASSNAME_DISABLED);
				setIcon(this.disabledIcon);
				setDescription(ServerTranslate.tr(disabledTooltip));
			}
		}

		public boolean isValue() {
			return currentValue;
		}

		public Component getComponent() {
			return component;
		}
	}

	// titleLayout = new HorizontalLayout();
	// titleLayout.setWidth("100%");
	//
	// collapse = new IconButton(ThemeIcons.COLLAPSE, IconSize.BIG,
	// LanguageCodes.COLLAPSABLE_PANEL_COLLAPSE_TOOLTIP);
	// collapse.addClickListener(new ClickListener() {
	// private static final long serialVersionUID = -7827258204263400187L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// setStyleName(CLASSNAME_COLLAPSE);
	// }
	// });
	// expand = new IconButton(ThemeIcons.EXPAND, IconSize.BIG,
	// LanguageCodes.COLLAPSABLE_PANEL_EXPAND_TOOLTIP);
	//
	// titleLayout.addComponent(collapse);
	//
	// rootComponent.addComponent(titleLayout);
	// rootComponent.setExpandRatio(titleLayout, 0.0f);
	//
	// setCompositionRoot(rootComponent);

}
