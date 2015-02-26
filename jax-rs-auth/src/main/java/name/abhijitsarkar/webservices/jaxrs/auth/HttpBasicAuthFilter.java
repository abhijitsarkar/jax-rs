package name.abhijitsarkar.webservices.jaxrs.auth;

import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getEncoder;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

public class HttpBasicAuthFilter implements ClientRequestFilter {
    private static final String USERNAME_PASSWORD_DELIMITER = ":";
    private static final String AUTH_MODE = "Basic";
    private static final String AUTH_HEADER = "Authorization";

    private final String token;

    public HttpBasicAuthFilter(String username, String password) {
	token = join(USERNAME_PASSWORD_DELIMITER, username, password);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
	MultivaluedMap<String, Object> headers = requestContext.getHeaders();

	final String base64EncCipher = join(" ", AUTH_MODE, new String(
		getEncoder().encode(token.getBytes(UTF_8))));

	headers.putSingle(AUTH_HEADER, base64EncCipher);
    }
}