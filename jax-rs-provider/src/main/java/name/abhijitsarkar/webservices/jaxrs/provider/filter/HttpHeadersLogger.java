package name.abhijitsarkar.webservices.jaxrs.provider.filter;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeadersLogger {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(HttpHeadersLogger.class);

    public static void log(
	    MultivaluedMap<String, ? extends Object> multivaluedMap,
	    String message) {
	if (multivaluedMap != null && !multivaluedMap.isEmpty()) {
	    LOGGER.info(message);
	    LOGGER.info("HTTP headers: {}.", multivaluedMap);
	} else {
	    LOGGER.info("No HTTP headers found.");
	}
    }
}
