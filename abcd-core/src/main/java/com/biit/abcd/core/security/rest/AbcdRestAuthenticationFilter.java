package com.biit.abcd.core.security.rest;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
