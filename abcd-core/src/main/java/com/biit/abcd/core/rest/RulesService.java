package com.biit.abcd.core.rest;

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

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.DroolsZipGenerator;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.PluginInvocationException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@Path("/forms/rules")
public class RulesService {
    @Autowired
    private IFormDao formDao;

    @Autowired
    private ISimpleFormViewDao simpleFormViewDao;

    @Autowired
    private IGlobalVariablesDao globalVariablesDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/zip")
    @Path("/zip")
    public Response getFormZip(String petition) {
        FormDescription parsedPetition;
        AbcdLogger.info(RulesService.class.getName(), "Requesting Form using endpoint '/forms/rules/zip' with payload '{}'.", petition);
        try {
            parsedPetition = parsePetition(petition);
            Form form = getForm(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            byte[] rulesZip = new DroolsZipGenerator().getInformationData(form, globalVariablesDao.getAll());
            AbcdLogger.debug(RulesService.class.getName(), "Rules retrieved successfully!");
            return Response.ok(rulesZip, MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException ex) {
            AbcdLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Rules error\"}").build();
        } catch (IOException | TreeObjectInstanceNotRecognizedException | ExpressionInvalidException |
                 DroolsRuleGenerationException
                 | BetweenFunctionInvalidException | DroolsRuleCreationException | DateComparisonNotPossibleException |
                 InvalidRuleException | NullExpressionValueException | NullTreeObjectException | PrattParserException |
                 PluginInvocationException | RuleNotImplementedException | NullCustomVariableException |
                 TreeObjectParentNotValidException | NotCompatibleTypeException | ActionNotImplementedException |
                 InvalidExpressionException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Error generating the rules. " +
                    "Check the logs for extra information.\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("")
    public Response getFormRules(String petition) {
        FormDescription parsedPetition;
        AbcdLogger.info(RulesService.class.getName(), "Requesting Form using endpoint '/forms/rules/drl' with payload '{}'.", petition);
        try {
            parsedPetition = parsePetition(petition);
            Form form = getForm(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            String rules = FormToDroolsExporter.getDroolRules(form, globalVariablesDao.getAll());
            AbcdLogger.debug(RulesService.class.getName(), "Rules retrieved successfully!");
            return Response.ok(rules, MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException ex) {
            AbcdLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Rules error\"}").build();
        } catch (TreeObjectInstanceNotRecognizedException | ExpressionInvalidException | DroolsRuleGenerationException
                 | BetweenFunctionInvalidException | DroolsRuleCreationException | DateComparisonNotPossibleException |
                 InvalidRuleException | NullExpressionValueException | NullTreeObjectException | PrattParserException |
                 PluginInvocationException | RuleNotImplementedException | NullCustomVariableException |
                 TreeObjectParentNotValidException | NotCompatibleTypeException | ActionNotImplementedException |
                 InvalidExpressionException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Error generating the rules. " +
                    "Check the logs for extra information.\"}").build();
        }
    }

    private FormDescription parsePetition(String petition) throws JsonSyntaxException {
        if (petition == null || petition.length() == 0) {
            throw new JsonSyntaxException("Empty parameter not allowed.");
        }
        return new Gson().fromJson(petition, FormDescription.class);
    }

    private Form getForm(String name, Integer version, Long organizationId) {
        final SimpleFormViewWithContent simpleFormView = simpleFormViewDao.getSimpleFormViewByLabelAndVersionAndOrganization(name, version, organizationId);
        return getForm(simpleFormView);
    }

    private Form getForm(SimpleFormViewWithContent simpleFormView) {
        if (simpleFormView == null) {
            return null;
        }
        if (simpleFormView.getJson() != null && !simpleFormView.getJson().isEmpty()) {
            try {
                return Form.fromJson(simpleFormView.getJson());
            } catch (JsonProcessingException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
                return formDao.get(simpleFormView.getId());
            }
        }
        return formDao.get(simpleFormView.getId());
    }

    static class FormDescription {
        private String formName;
        private Integer version;
        private Long organizationId;

        public FormDescription(String formName, Integer version, Long organizationId) {
            this.formName = formName;
            this.version = version;
            this.organizationId = organizationId;
        }

        public String getFormName() {
            return formName;
        }

        public void setFormName(String formName) {
            this.formName = formName;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Long getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(Long organizationId) {
            this.organizationId = organizationId;
        }
    }
}

