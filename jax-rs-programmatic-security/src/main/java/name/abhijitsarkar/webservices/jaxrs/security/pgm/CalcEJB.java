package name.abhijitsarkar.webservices.jaxrs.security.pgm;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import name.abhijitsarkar.webservices.jaxrs.security.pgm.MustBeMathematicianFeature.MustBeMathematician;

/* JAX-RS annotations are supported on local interfaces or no-interface beans of stateless session or singleton beans. */
@Stateless
@Path("calc")
public class CalcEJB {
    @MustBeMathematician
    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int add(@QueryParam("augend") int augend,
	    @QueryParam("addend") int addend) {
	return augend + addend;
    }

    @GET
    @Path("subtract")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int subtract(@QueryParam("minuend") int minuend,
	    @QueryParam("subtrahend") int subtrahend) {
	return minuend - subtrahend;
    }
}
