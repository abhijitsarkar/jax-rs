package name.abhijitsarkar.webservices.jaxrs.provider.resource;

import static javax.ws.rs.core.Response.ok;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import name.abhijitsarkar.webservices.jaxrs.provider.domain.Sum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("calc")
public class Calculator {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(Calculator.class);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response add(@QueryParam("arg0") int arg0,
	    @QueryParam("arg1") int arg1, @HeaderParam("Accept") String accept) {

	LOGGER.info("Received Accept header: {}.", accept);

	return ok(new Sum(arg0, arg1), accept).build();
    }
}
