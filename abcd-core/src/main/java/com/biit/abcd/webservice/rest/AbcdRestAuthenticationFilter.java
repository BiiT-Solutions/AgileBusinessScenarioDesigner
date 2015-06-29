package com.biit.abcd.webservice.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.biit.webservice.rest.RestAuthenticationFilter;

public class AbcdRestAuthenticationFilter extends RestAuthenticationFilter {

	/**
	 * The parent extended needs to know what authorization service must call,
	 * so this method passes the specific authorization class needed by the
	 * parent to filter the request.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException,
			ServletException {
		if (request instanceof HttpServletRequest) {
			request.setAttribute(AUTHORIZATION_INSTANCE, new AbcdRestAuthorizationService());
			super.doFilter(request, response, filter);
		}
	}
}
