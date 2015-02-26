package name.abhijitsarkar.webservices.jaxrs.hateoas;

import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("slot")
public class AvailabilityResource {
    private MultivaluedMap<String, Slot> availableSlots;

    @GET
    public Response getSlot(@QueryParam("date") String date) {
	List<Slot> slots = availableSlots.get(date);

	if (slots != null && !slots.isEmpty()) {
	    return ok().entity(slots).build();
	}

	return noContent().status(NOT_FOUND).build();
    }
}
