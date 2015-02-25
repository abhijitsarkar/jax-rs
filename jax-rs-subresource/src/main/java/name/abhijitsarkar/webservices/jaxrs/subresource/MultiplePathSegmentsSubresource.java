package name.abhijitsarkar.webservices.jaxrs.subresource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

public class MultiplePathSegmentsSubresource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{make}/{features : .+}/{year : \\d+}")
    public Map<String, Object> handleMultiplePathSegments(
	    @PathParam("make") String make,
	    @PathParam("features") List<PathSegment> features,
	    @PathParam("year") int year) {
	Map<String, Object> response = new HashMap<>();
	response.put("make", make);
	response.put("year", year);
	response.put("features", features);

	return response;
    }
}
