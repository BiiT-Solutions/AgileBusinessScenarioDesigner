package com.biit.abcd.webservice.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.AuthenticationService;
import com.google.gson.Gson;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

/**
 * Get appointment examinations This method receives a Json string asking for a
 * concrete appointment information and returns an array of examinations.
 *
 */
@Component
@Path("/")
public class AbcdFormRestService {

	private static final String PARAMETER_NAME = "parameter";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private ISimpleFormViewDao simpleFormViewDao;

	/**
	 * This method receives 1 parameters:<br>
	 * 1st parameter: user email<br>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllSimpleFormViewsByUserEmail")
	public Response getAllSimpleFormViewsByUserEmail(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
		if ((parameters != null) && (parameters.size() == 1)) {
			Set<Organization> organizations = getUserOrganizations(parameters.get(0));
			List<SimpleFormView> simpleForms = new ArrayList<>();
			for (Organization organization : organizations) {
				simpleForms.addAll(simpleFormViewDao.getSimpleFormViewByOrganization(organization.getOrganizationId()));
			}
			return Response.ok(parseSimpleFormViewList(simpleForms), MediaType.APPLICATION_JSON).build();
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
			return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
		}
	}

	/**
	 * This method receives 3 parameters:<br>
	 * 1st parameter: user email<br>
	 * 2nd parameter: form label<br>
	 * 3rd parameter: organization id of the form
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllSimpleFormViewByLabelAndOrganization")
	public Response getAllSimpleFormViewByLabelAndOrganization(
			@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
		if ((parameters != null) && (parameters.size() == 3)) {
			Long formOrganization = Long.parseLong(parameters.get(2));

			// First check if the user and the form belong to the same
			// organization
			Set<Organization> userOrganizations = getUserOrganizations(parameters.get(0));
			if (userOrganizations != null) {
				for (Organization organization : userOrganizations) {
					if (organization.getOrganizationId() == formOrganization) {
						// Get the simple form information
						List<SimpleFormView> simpleForms = simpleFormViewDao.getSimpleFormViewByLabelAndOrganization(
								parameters.get(1), formOrganization);
						return Response.ok(parseSimpleFormViewList(simpleForms), MediaType.APPLICATION_JSON).build();
					}
				}
			}
			AbcdLogger.errorMessage(this.getClass().getName(), "User not allowed to access form");
			return Response.serverError().entity("{\"error\":\"User not allowed to access form\"}").build();
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
			return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
		}
	}

	/**
	 * This method receives 2 parameters:<br>
	 * 1st parameter: user email<br>
	 * 2nd parameter: form ID<br>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFormById")
	public Response getFormById(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
		if ((parameters != null) && (parameters.size() == 2)) {

			// First check if the user and the form belong to the same
			// organization
			Set<Organization> userOrganizations = getUserOrganizations(parameters.get(0));
			Form formById = formDao.get(Long.parseLong(parameters.get(1)));
			if (userOrganizations != null) {
				for (Organization organization : userOrganizations) {
					if (organization.getOrganizationId() == formById.getOrganizationId()) {
						// Get the form information
						return Response.ok(formById.toJson(), MediaType.APPLICATION_JSON).build();
					}
				}
			}
			AbcdLogger.errorMessage(this.getClass().getName(), "User not allowed to access form");
			return Response.serverError().entity("{\"error\":\"User not allowed to access form\"}").build();
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
			return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
		}
	}

	/**
	 * This method receives 4 parameters:<br>
	 * 1st parameter: user email<br>
	 * 2nd parameter: form label<br>
	 * 3rd parameter: organization id of the form<br>
	 * 4th parameter: version of the form
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFormByLabelOrganizationAndVersion")
	public Response getFormByLabelOrganizationAndVersion(
			@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
		if ((parameters != null) && (parameters.size() == 4)) {
			Long formOrganization = Long.parseLong(parameters.get(2));
			// First check if the user and the form belong to the same
			// organization
			Set<Organization> userOrganizations = getUserOrganizations(parameters.get(0));
			if (userOrganizations != null) {
				for (Organization organization : userOrganizations) {
					if (organization.getOrganizationId() == formOrganization) {
						// Get the form information
						Form form = formDao.getForm(parameters.get(1), Integer.parseInt(parameters.get(3)),
								Long.parseLong(parameters.get(2)));
						return Response.ok(form.toJson(), MediaType.APPLICATION_JSON).build();
					}
				}
			}
			AbcdLogger.errorMessage(this.getClass().getName(), "User not allowed to access form");
			return Response.serverError().entity("{\"error\":\"User not allowed to access form\"}").build();
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
			return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
		}
	}

	/**
	 * This method receives 1 parameter:<br>
	 * 1st parameter: organization id of the forms<br>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFormsByOrganization")
	public Response getFormsByOrganization(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
		if ((parameters != null) && (parameters.size() == 1)) {
			List<Form> formList = formDao.getAll(Long.parseLong(parameters.get(0)));
			return Response.ok(parseFormList(formList), MediaType.APPLICATION_JSON).build();
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
			return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/checkHealth")
	public Response checkHealth() {
		return Response.ok().build();
	}

	private String parseSimpleFormViewList(List<SimpleFormView> simpleForms) {
		Gson gson = new Gson();
		return gson.toJson(simpleForms);
	}

	private String parseFormList(List<Form> forms) {
		StringBuilder builder = new StringBuilder("{");
		for (Form form : forms) {
			builder.append(form + ",");
		}
		builder.deleteCharAt(builder.length());
		builder.append("}");
		return builder.toString();
	}

	private Set<Organization> getUserOrganizations(String userEmail) {
		User user = null;
		try {
			user = AuthenticationService.getInstance().getUserByEmail(userEmail);
		} catch (NotConnectedToWebServiceException | UserDoesNotExistException | IOException | AuthenticationRequired
				| WebServiceAccessError e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
		if (user != null) {
			try {
				return AbcdAuthorizationService.getInstance().getUserOrganizations(user);
			} catch (IOException | AuthenticationRequired e) {
				AbcdLogger.errorMessage(this.getClass().getName(), "User organization not found");
				return null;
			}
		} else {
			AbcdLogger.errorMessage(this.getClass().getName(), "Liferay user not found");
			return null;
		}
	}

}
