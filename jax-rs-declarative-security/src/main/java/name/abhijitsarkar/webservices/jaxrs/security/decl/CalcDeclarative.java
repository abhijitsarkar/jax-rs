package name.abhijitsarkar.webservices.jaxrs.security.decl;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("calc")
@DeclareRoles({ "mathematician" })
public class CalcDeclarative {
    @RolesAllowed("mathematician")
    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int add(@QueryParam("augend") int augend,
	    @QueryParam("addend") int addend) {
	return augend + addend;
    }

    @DenyAll
    @GET
    @Path("subtract")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int subtract(@QueryParam("minuend") int minuend,
	    @QueryParam("subtrahend") int subtrahend) {
	return minuend - subtrahend;
    }
}
