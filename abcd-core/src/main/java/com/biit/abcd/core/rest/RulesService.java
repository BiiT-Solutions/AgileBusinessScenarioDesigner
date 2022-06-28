package com.biit.abcd.core.rest;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.DroolsZipGenerator;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.abcd.persistence.entity.Form;
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
            Form form = formDao.get(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            byte[] rulesZip = DroolsZipGenerator.getInformationData(form, globalVariablesDao.getAll());
            AbcdLogger.debug(RulesService.class.getName(), "Rules retrieved successfully!");
            return Response.ok(rulesZip, MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException ex) {
            AbcdLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Rules error\"}").build();
        } catch (MultiplesFormsFoundException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Multiples forms match the search criteria. " +
                    "Please define some extra parameters\"}").build();
        } catch (IOException | TreeObjectInstanceNotRecognizedException | ExpressionInvalidException | DroolsRuleGenerationException
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
    @Path("/")
    public Response getFormRules(String petition) {
        FormDescription parsedPetition;
        AbcdLogger.info(RulesService.class.getName(), "Requesting Form using endpoint '/forms/rules/drl' with payload '{}'.", petition);
        try {
            parsedPetition = parsePetition(petition);
            Form form = formDao.get(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
            String rules = droolsExporter.getDroolRules(form, globalVariablesDao.getAll());
            AbcdLogger.debug(RulesService.class.getName(), "Rules retrieved successfully!");
            return Response.ok(rules, MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException ex) {
            AbcdLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Rules error\"}").build();
        } catch (MultiplesFormsFoundException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Multiples forms match the search criteria. " +
                    "Please define some extra parameters\"}").build();
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

