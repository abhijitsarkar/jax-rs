package name.abhijitsarkar.webservices.jaxrs.provider.filter;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

// A Java EE container can scan and register classes annotated with @Provider; Servlet containers like Jetty can't so
// Providers need to be registered with the Application class.

@Provider
@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
// Add XML to the list of acceptable content types as that's the only custom
// EntityProvider we support
public class XMLMediaTypeAppendingContainerFilter implements
	ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext)
	    throws IOException {
	List<MediaType> mediaTypes = requestContext.getAcceptableMediaTypes();

	if (mediaTypes.contains(MediaType.APPLICATION_XML_TYPE)) {
	    return;
	}

	MultivaluedMap<String, String> requestHdrs = requestContext
		.getHeaders();

	List<String> originalAcceptHdr = requestHdrs.get("Accept");

	requestHdrs.addAll("Original-Accept", originalAcceptHdr);

	List<String> acceptHdr = new ArrayList<String>(originalAcceptHdr);
	/* Add XML content type but with low priority. */
	acceptHdr.add(APPLICATION_XML + ";q=0.1");

	requestHdrs.put("Accept", acceptHdr);
    }
}
