package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.security.AbcdFormAuthorizationService;

public class SecuredEditCellComponent extends EditCellComponent {

	private static final long serialVersionUID = -2609821632560785408L;

	@Override
	protected void addButtons() {
		if (getEditButton().getParent() == null) {
			if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
					UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
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
			if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
					UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
				getRootLayout().removeComponent(getEditButton());
				if (!isOnlyEdit()) {
					getRootLayout().removeComponent(getRemoveButton());
				}
			}
		}
	}

	@Override
	protected void doubleClickElement() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.doubleClickElement();
		}
	}
}
