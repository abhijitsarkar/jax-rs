package name.abhijitsarkar.webservices.jaxrs.provider.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class HttpHeadersLoggingContainerFilter implements
	ContainerRequestFilter, ContainerResponseFilter {
    private static final String CLASS_NAME = "HttpHeadersLoggingContainerFilter";

    @Override
    public void filter(ContainerRequestContext requestContext,
	    ContainerResponseContext responseContext) throws IOException {
	HttpHeadersLogger.log(responseContext.getHeaders(), CLASS_NAME
		+ " - Response HTTP headers.");

    }

    @Override
    public void filter(ContainerRequestContext requestContext)
	    throws IOException {
	HttpHeadersLogger.log(requestContext.getHeaders(), CLASS_NAME
		+ " - Request HTTP headers.");
    }
}
