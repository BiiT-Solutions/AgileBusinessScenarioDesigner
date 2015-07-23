package com.biit.abcd.webpages.elements.formdesigner;

import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;

/**
 * Disable a list of elements if the user has no permissions to change it.
 */
public abstract class SecuredFormElementProperties<T> extends GenericFormElementProperties<T> {
	private static final long serialVersionUID = 8823149378553743547L;

	private IAbcdFormAuthorizationService securityService;

	public SecuredFormElementProperties(Class<? extends T> type) {
		super(type);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	public void setElement(Object element) {
		super.setElement(element);
		disableProtectedElements();
	}

	protected void disableProtectedElements() {
		if (securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			for (AbstractComponent component : getProtectedElements()) {
				if (component != null) {
					component.setEnabled(false);
				}
			}
		}
	}

	/**
	 * Returns a list of elements that would be protected if the user has not the correct rights.
	 * 
	 * @return
	 */
	protected abstract Set<AbstractComponent> getProtectedElements();

}
