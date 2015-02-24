package name.abhijitsarkar.webservices.jaxrs.subresource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("car")
public class CarResource {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(CarResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ctx/{make}/{model}")
    public String handleContext(@Context UriInfo info) {
	LOGGER.debug("Path: {}, absolute path: {}, base URI: {}.",
		info.getPath(), info.getAbsolutePath(), info.getBaseUri());

	List<PathSegment> pathSegments = info.getPathSegments();

	LOGGER.debug("Path segments: {}.", pathSegments);

	if (pathSegments != null && pathSegments.size() >= 3) {
	    return pathSegments.get(3).getPath();
	}

	return info.getPath();
    }

    @Path("subresource/{subresourceLocator}")
    // Subresources must not be annotated with @Path; they need not be
    // registered with JAX-RS. They only need to have annotated methods that can
    // handle delegated requests appropriately
    public Object locateSubresource(
	    @PathParam(value = "subresourceLocator") String subresourceLocator) {
	if (subresourceLocator == null
		|| subresourceLocator.trim().length() == 0
		|| subresourceLocator.equalsIgnoreCase("basicPathParam")) {
	    return new PathParamSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("multiplePathParams")) {
	    return new MultiplePathParamsSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("multiplePathSegments")) {
	    return new MultiplePathSegmentsSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("matrixParam")) {
	    return new MatrixParamSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("queryParam")) {
	    return new QueryParamSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("cookieParam")) {
	    return new CookieParamSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("headerParam")) {
	    return new HeaderParamSubresource();
	} else if (subresourceLocator.equalsIgnoreCase("formParam")) {
	    return new FormParamSubresource();
	}

	return new ContextSubresource();
    }
}