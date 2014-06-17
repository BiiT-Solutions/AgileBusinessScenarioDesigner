package com.biit.abcd.core;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * As suggested in https://vaadin.com/wiki/-/wiki/Main/Spring%20Integration, to access your Spring managed beans, you
 * will need a helper class capable of using your Vaadin Application class to get the relevant session information, and
 * thus the Spring context:
 */
public class SpringContextHelper {

	private ApplicationContext context;

	public SpringContextHelper(ServletContext servletContext) {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	public ApplicationContext getContext() {
		return context;
	}

	public Object getBean(final String beanRef) {
		return context.getBean(beanRef);
	}
}
