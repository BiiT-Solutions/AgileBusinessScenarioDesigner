package com.biit.abcd.core.rest;

import com.biit.abcd.core.security.ISecurityService;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Rest services used to communicate ABCD with WebForms
 */
@Component
@Path("/")
public class AbcdFormRestService {

    private static final String PARAMETER_NAME = "parameter";

    @Autowired
    private IFormDao formDao;

    @Autowired
    private ISimpleFormViewDao simpleFormViewDao;

    @Autowired
    private ISecurityService securityService;

    /**
     * This method receives 1 parameter:<br>
     * &emsp; 1st parameter: user email<br>
     * Returns a list of SimpleFormView
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllSimpleFormViewsByUserEmail")
    public Response getAllSimpleFormViewsByUserEmail(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
        if ((parameters != null) && (parameters.size() == 1)) {
            Set<IGroup<Long>> organizations = getUserOrganizations(parameters.get(0));
            if (organizations == null) {
                return Response.serverError().entity("{\"error\":\"User not allowed to access form\"}").build();
            }
            List<SimpleFormView> simpleForms = new ArrayList<>();
            for (IGroup<Long> organization : organizations) {
                simpleForms.addAll(simpleFormViewDao.getSimpleFormViewByOrganization(organization.getUniqueId()));
            }
            return Response.ok(parseSimpleFormViewList(simpleForms), MediaType.APPLICATION_JSON).build();
        } else {
            AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
            return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
        }
    }

    /**
     * This method receives 3 parameters:<br>
     * &emsp; 1st parameter: user email<br>
     * &emsp; 2nd parameter: form label<br>
     * &emsp; 3rd parameter: organization id of the form<br>
     * Returns a list of SimpleFormView
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllSimpleFormViewByLabelAndOrganization")
    public Response getAllSimpleFormViewByLabelAndOrganization(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
        if ((parameters != null) && (parameters.size() == 3)) {
            Long formOrganizationId = Long.parseLong(parameters.get(2));

            // First check if the user and the form belong to the same
            // organization
            Set<IGroup<Long>> userOrganizations = getUserOrganizations(parameters.get(0));
            if (userOrganizations != null) {
                for (IGroup<Long> organization : userOrganizations) {
                    if (organization.getUniqueId().equals(formOrganizationId)) {
                        // Get the simple form information
                        List<SimpleFormView> simpleForms = simpleFormViewDao.getSimpleFormViewByLabelAndOrganization(parameters.get(1), formOrganizationId);
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
     * &emsp; 1st parameter: user email<br>
     * &emsp; 2nd parameter: form ID<br>
     * Returns a complete Form
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFormById")
    public Response getFormById(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
        if ((parameters != null) && (parameters.size() == 2)) {

            // First check if the user and the form belong to the same
            // organization
            Set<IGroup<Long>> userOrganizations = getUserOrganizations(parameters.get(0));
            Form formById = formDao.get(Long.parseLong(parameters.get(1)));
            if (userOrganizations != null) {
                for (IGroup<Long> organization : userOrganizations) {
                    if (organization.getUniqueId().equals(formById.getOrganizationId())) {
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
     * &emsp; 1st parameter: user email<br>
     * &emsp; 2nd parameter: form label<br>
     * &emsp; 3rd parameter: organization id of the form<br>
     * &emsp; 4th parameter: version of the form<br>
     * Returns a complete Form
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFormByLabelOrganizationAndVersion")
    public Response getFormByLabelOrganizationAndVersion(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
        if ((parameters != null) && (parameters.size() == 4)) {
            Long formOrganization = Long.parseLong(parameters.get(2));
            // First check if the user and the form belong to the same
            // organization
            Set<IGroup<Long>> userOrganizations = getUserOrganizations(parameters.get(0));
            if (userOrganizations != null) {
                for (IGroup<Long> organization : userOrganizations) {
                    if (organization.getUniqueId().equals(formOrganization)) {
                        // Get the form information
                        Form form = formDao.getForm(parameters.get(1), Integer.parseInt(parameters.get(3)), Long.parseLong(parameters.get(2)));
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
     * &emsp; 1st parameter: organization id of the forms<br>
     * Returns a list of Forms
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFormsByOrganization")
    public Response getFormsByOrganization(@QueryParam(value = PARAMETER_NAME) final List<String> parameters) {
        if ((parameters != null) && (parameters.size() == 2)) {
            Set<IGroup<Long>> userOrganizations = getUserOrganizations(parameters.get(0));
            Long organizationId = Long.parseLong(parameters.get(1));
            for (IGroup<Long> userOrganization : userOrganizations) {
                if (userOrganization.getUniqueId().equals(organizationId)) {
                    List<Form> formList = formDao.getAll(organizationId);
                    return Response.ok(parseFormList(formList), MediaType.APPLICATION_JSON).build();
                }
            }
            AbcdLogger.errorMessage(this.getClass().getName(), "User not allowed to access form");
            return Response.serverError().entity("{\"error\":\"User not allowed to access form\"}").build();
        } else {
            AbcdLogger.errorMessage(this.getClass().getName(), "Invalid input parameters");
            return Response.serverError().entity("{\"error\":\"Invalid input parameters\"}").build();
        }
    }

    private String parseSimpleFormViewList(List<SimpleFormView> simpleForms) {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(simpleForms);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseFormList(List<Form> forms) {
        StringBuilder builder = new StringBuilder("[");
        if ((forms != null) && !forms.isEmpty()) {
            for (Form form : forms) {
                builder.append(form.toJson() + ",");
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }

    private Set<IGroup<Long>> getUserOrganizations(String userEmail) {
        IUser<Long> user = null;
        try {
            user = securityService.getUserByEmail(userEmail);
        } catch (UserManagementException | UserDoesNotExistException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            return null;
        }
        if (user != null) {
            try {
                return securityService.getUserOrganizations(user);
            } catch (UserManagementException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), "User organization not found");
                return null;
            }
        } else {
            AbcdLogger.errorMessage(this.getClass().getName(), "Liferay user not found");
            return null;
        }
    }

}
