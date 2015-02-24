package name.abhijitsarkar.webservices.jaxrs.subresource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class MultiplePathParamsSubresource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{make}-{model}-{year}")
    public Map<String, String> handleMultiplePathParams(
	    @PathParam("make") String make, @PathParam("model") String model,
	    @PathParam(value = "year") int year) {
	Map<String, String> response = new HashMap<>();

	response.put("make", make);
	response.put("model", model);
	response.put("year", String.valueOf(year));

	return response;
    }
}
