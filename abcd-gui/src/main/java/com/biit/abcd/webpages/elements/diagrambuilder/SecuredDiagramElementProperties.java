package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;

public abstract class SecuredDiagramElementProperties<T> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = 3707582105881695713L;

	private IAbcdFormAuthorizationService securityService;

	public SecuredDiagramElementProperties(Class<? extends T> type) {
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
