package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;

public class SecuredEditCellComponent extends EditCellComponent {

	private static final long serialVersionUID = -2609821632560785408L;

	private IAbcdFormAuthorizationService securityService;

	protected SecuredEditCellComponent() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	protected void addButtons() {
		if (getEditButton().getParent() == null) {
			if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getUser())) {
				if (!isOnlyEdit()) {
					getRootLayout().addComponent(getRemoveButton(), 0);
				}
				getRootLayout().addComponent(getEditButton(), 0);
			}
		}
	}

	@Override
	protected void removeButtons() {
		if (getEditButton().getParent() != null) {
			if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getUser())) {
				getRootLayout().removeComponent(getEditButton());
				if (!isOnlyEdit()) {
					getRootLayout().removeComponent(getRemoveButton());
				}
			}
		}
	}

	@Override
	protected void doubleClickElement() {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.doubleClickElement();
		}
	}
}
