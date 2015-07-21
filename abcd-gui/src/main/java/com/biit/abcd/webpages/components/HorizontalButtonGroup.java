package com.biit.abcd.webpages.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalButtonGroup extends CustomComponent {

	private static final long serialVersionUID = 4862986305501412362L;
	private static String CLASSNAME = "v-horizontal-button-group";
	protected HorizontalLayout rootLayout;
	private String size;
	private boolean contractIcons;
	private Set<Button> buttons;

	private IAbcdFormAuthorizationService securityService;

	public HorizontalButtonGroup() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
		buttons = new HashSet<>();
		initHorizontalButtonGroup();
		setIconSizeWithAttachListener();
	}

	protected void initHorizontalButtonGroup() {
		setStyleName(CLASSNAME);

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(false);
		setCompositionRoot(rootLayout);
		setSizeFull();

		contractIcons = false;
	}

	protected void setIconSizeWithAttachListener() {
		addAttachListener(new AttachListener() {
			private static final long serialVersionUID = -2513076537414804598L;

			@Override
			public void attach(AttachEvent event) {
				setIconSize();
			}
		});
	}

	public void addIconButton(IconButton button) {
		buttons.add(button);
		rootLayout.addComponent(button);
		button.setSizeFull();
	}

	public void setContractIcons(boolean contractIcons) {
		this.contractIcons = contractIcons;
		this.size = null;
		rootLayout.setWidth(null);
	}

	public void setContractIcons(boolean contractIcons, String size) {
		this.contractIcons = contractIcons;
		this.size = size;
		rootLayout.setWidth(null);
	}

	private void setIconSize() {
		Iterator<Component> itr = rootLayout.iterator();
		if (contractIcons) {
			rootLayout.setWidth(null);
		}

		while (itr.hasNext()) {
			Component component = itr.next();
			rootLayout.setExpandRatio(component, 0.0f);
			if (contractIcons) {
				component.setWidth(size);
			} else {
				component.setWidth("100%");
			}
		}

		markAsDirtyRecursive();
	}

	public Set<Button> getButtons() {
		return buttons;
	}

	protected IAbcdFormAuthorizationService getSecurityService() {
		return securityService;
	}

}
