package com.biit.abcd.core.security.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.biit.webservice.rest.RestAuthenticationFilter;

public class AbcdRestAuthenticationFilter extends RestAuthenticationFilter {

	private AbcdRestAuthorizationService abcdRestAuthorizationService;

	/**
	 * The extended parent needs to know what authorization service must call, so this method passes the specific
	 * authorization class needed by the parent to filter the request.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException,
			ServletException {
		if (request instanceof HttpServletRequest) {
			request.setAttribute(AUTHORIZATION_INSTANCE, abcdRestAuthorizationService);
			super.doFilter(request, response, filter);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		abcdRestAuthorizationService = WebApplicationContextUtils.getRequiredWebApplicationContext(
				filterConfig.getServletContext()).getBean(AbcdRestAuthorizationService.class);
	}
}
