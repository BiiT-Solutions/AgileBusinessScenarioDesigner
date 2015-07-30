package com.biit.abcd.core.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

/**
 * Rest services for check that is on-line.
 */
@Component
@Path("/")
public class CheckHealth {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/checkHealth")
	public Response checkHealth() {
		return Response.ok().build();
	}

}
