package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.Form;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Pages that has a form definition and can access to other pages that also have a form.
 */
public abstract class FormWebPageComponent extends SecuredWebPageComponent {
	private static final long serialVersionUID = 5215781045989004097L;
	private AbstractOrderedLayout workingAreaLayout;
	private BottomMenu bottomMenu;
	private UpperMenu upperMenu;

	public FormWebPageComponent() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		setSizeFull();
		setAsOneWindowWithBottomMenu();
	}

	public void updateButtons(boolean enableFormButtons) {
		bottomMenu.updateButtons(enableFormButtons);
	}

	public void setAsOneWindowWithBottomMenu() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		setCompositionRoot(getRootLayout());
		setSizeFull();

		Panel mainPanel = new Panel();
		getRootLayout().addComponent(mainPanel);
		getRootLayout().setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);
		getRootLayout().setExpandRatio(mainPanel, 1);
		mainPanel.setSizeFull();

		workingAreaLayout = new VerticalLayout();
		workingAreaLayout.setMargin(false);
		workingAreaLayout.setSpacing(false);
		workingAreaLayout.setSizeFull();

		mainPanel.setContent(workingAreaLayout);
		mainPanel.setSizeFull();

		setButtonMenu();
		setUpperMenu(new UpperMenu());
	}

	public void setUpperMenu(UpperMenu upperMenu) {
		if (this.upperMenu != null) {
			this.getRootLayout().removeComponent(this.upperMenu);
		}
		this.upperMenu = upperMenu;
		this.getRootLayout().addComponent(upperMenu, 0);
		getRootLayout().setComponentAlignment(upperMenu, Alignment.BOTTOM_CENTER);
	}

	private void setButtonMenu() {
		bottomMenu = new BottomMenu() {
			private static final long serialVersionUID = -4195315137788949245L;

			@Override
			public void setSelectedForm(Form form) {
				setForm(form);
			}

			@Override
			public Form getSelectedForm() {
				return getForm();
			}
		};
		getRootLayout().addComponent(bottomMenu);
		getRootLayout().setComponentAlignment(bottomMenu, Alignment.BOTTOM_CENTER);
	}

	public abstract void setForm(Form form);

	public abstract Form getForm();

	/**
	 * The Working Area Layout is the layout where the page includes its own components.
	 * 
	 * @return
	 */
	public AbstractOrderedLayout getWorkingAreaLayout() {
		return workingAreaLayout;
	}
}
