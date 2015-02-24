package name.abhijitsarkar.webservices.jaxrs.subresource.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Priorities.USER)
public class HttpLoggingContainerFilter implements
	ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(HttpLoggingContainerFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext,
	    ContainerResponseContext responseContext) throws IOException {
	LOGGER.info("Response: {}.\n", responseContext.getEntity());
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
	    throws IOException {
	UriInfo info = requestContext.getUriInfo();

	LOGGER.info("Request URI: {}.", info.getRequestUri());
    }
}
