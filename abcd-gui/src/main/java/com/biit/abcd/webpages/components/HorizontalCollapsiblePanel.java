package com.biit.abcd.webpages.components;

import java.util.Iterator;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
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
	private Component content;
	private CollapseMenuTab visibleTab;
	private boolean collapsed;

	public HorizontalCollapsiblePanel(boolean collapsed) {
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

		setSizeFull();
		setCompositionRoot(rootLayout);

		setCollapsed(collapsed);
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		updateLeftMenuState();
		updateMenuButtons();
	}

	private void updateMenuButtons() {
		if (collapsed) {
			Iterator<Component> itr = buttonMenu.iterator();
			while (itr.hasNext()) {
				CollapseMenuTab tab = (CollapseMenuTab) itr.next();
				tab.setValue(false);
			}
		} else {
			Iterator<Component> itr = buttonMenu.iterator();
			while (itr.hasNext()) {
				CollapseMenuTab tab = (CollapseMenuTab) itr.next();
				if (tab.equals(visibleTab)) {
					tab.setValue(true);
				} else {
					tab.setValue(false);
				}
			}
		}
	}

	private boolean isCollapsed() {
		return collapsed;
	}

	private void updateLeftMenuState() {
		menuLayout.removeAllComponents();
		if (!collapsed) {
			if (visibleTab != null) {
				menuLayout.addComponent(visibleTab.getComponent());
			}
			menuLayout.setWidth(menuWidth);
			menuLayout.setStyleName(CLASSNAME_OPENED);
		} else {
			menuLayout.setWidth(menuWidthClosed);
			menuLayout.setStyleName(CLASSNAME_CLOSED);
		}
	}

	public void setVisibleMenu(CollapseMenuTab tab) {
		if (!isCollapsed()) {
			menuLayout.removeAllComponents();
			menuLayout.addComponent(tab.getComponent());
			updateMenuButtons();
		}
		visibleTab = tab;
	}

	public CollapseMenuTab createMenuTab(Component component, ThemeIcon enabledIcon, ThemeIcon disabledIcon,
			LanguageCodes enabledTooltip, LanguageCodes disabledTooltip) {
		final CollapseMenuTab collapseButtonTab = new CollapseMenuTab(component, enabledIcon, disabledIcon,
				enabledTooltip, disabledTooltip);
		collapseButtonTab.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -754510955831311296L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (isCollapsed()) {
					// The menu is collapsed, then expand and show your
					// component.
					setVisibleMenu(collapseButtonTab);
					setCollapsed(false);
				} else {
					if (collapseButtonTab.equals(visibleTab)) {
						setCollapsed(true);
					} else {
						setVisibleMenu(collapseButtonTab);
					}
				}
			}
		});

		return collapseButtonTab;
	}

	public void setContent(Component component) {
		this.content = component;
		content.setSizeFull();
	}

	public void createMenu(Component component) {
		CollapseMenuTab tab = createMenuTab(component, ThemeIcon.LEFT_MENU_COLLAPSE, ThemeIcon.LEFT_MENU_EXPAND,
				LanguageCodes.COLLAPSABLE_PANEL_COLLAPSE_TOOLTIP, LanguageCodes.COLLAPSABLE_PANEL_EXPAND_TOOLTIP);
		buttonMenu.addComponent(tab);
		setVisibleMenu(tab);
	}

	@Override
	public void attach() {
		super.attach();
		rootLayout.addComponent(menuLayout);
		rootLayout.addComponent(buttonMenu);
		rootLayout.addComponent(content);
		rootLayout.markAsDirty();
	}

	private class CollapseMenuTab extends IconButton {
		private static final long serialVersionUID = -443120664603138986L;
		private static final String CLASSNAME_ENABLED = "v-button-tab-enabled";
		private static final String CLASSNAME_DISABLED = "v-button-tab-disabled";
		private ThemeIcon enabledIcon;
		private ThemeIcon disabledIcon;
		private LanguageCodes enabledTooltip;
		private LanguageCodes disabledTooltip;
		private Component component;

		public CollapseMenuTab(Component component, ThemeIcon enabledIcon, ThemeIcon disabledIcon,
				LanguageCodes enabledTooltip, LanguageCodes disabledTooltip) {
			super(disabledIcon, IconSize.MEDIUM, disabledTooltip);
			this.enabledIcon = enabledIcon;
			this.disabledIcon = disabledIcon;
			this.enabledTooltip = enabledTooltip;
			this.disabledTooltip = disabledTooltip;

			setImmediate(true);
			this.component = component;
		}

		public void setValue(boolean enabled) {
			if (enabled) {
				setStyleName(CLASSNAME_ENABLED);
				setIcon(this.enabledIcon);
				setDescription(ServerTranslate.translate(enabledTooltip));
			} else {
				setStyleName(CLASSNAME_DISABLED);
				setIcon(this.disabledIcon);
				setDescription(ServerTranslate.translate(disabledTooltip));
			}
		}

		public Component getComponent() {
			return component;
		}
	}

}
