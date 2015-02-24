package name.abhijitsarkar.webservices.jaxrs.provider.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

/* Client side filters are not picked up using @Provider annotation; they need to be manually registered. */
@Priority(Priorities.USER)
public class HttpHeadersLoggingClientFilter implements ClientRequestFilter,
	ClientResponseFilter {
    private static final String CLASS_NAME = "HttpHeadersLoggingClientFilter";

    @Override
    public void filter(ClientRequestContext requestContext,
	    ClientResponseContext responseContext) throws IOException {
	HttpHeadersLogger.log(responseContext.getHeaders(), CLASS_NAME
		+ " - Response HTTP headers.");
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
	HttpHeadersLogger.log(requestContext.getHeaders(), CLASS_NAME
		+ " - Request HTTP headers.");
    }
}
