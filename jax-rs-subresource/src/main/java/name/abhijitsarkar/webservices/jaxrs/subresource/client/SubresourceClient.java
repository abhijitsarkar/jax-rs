package name.abhijitsarkar.webservices.jaxrs.subresource.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

public class SubresourceClient {
    private static final String CAR_RESOURCE_URI = "http://localhost:8080/jax-rs-subresource/car";

    private final Client client;

    public SubresourceClient() {
	this.client = ClientBuilder.newClient();
    }

    /* Client should be closed to avoid resource leak */
    public void close() {
	this.client.close();
    }

    public static void main(String[] args) {
	SubresourceClient srClient = new SubresourceClient();

	String uri = null;

	uri = "/subresource/basicPathParam/Mercedes";
	srClient.request(buildUri(uri));

	uri = "/subresource/multiplePathParams/Mercedes-C250-2012";
	srClient.request(buildUri(uri));

	uri = "/subresource/multiplePathSegments/Mercedes/C250;"
		+ "loaded=true/yes;moonroof/AMG/2012?"
		+ "make=Mercedes&model=C250";
	srClient.multiplePathSegmentsRequest(buildUri(uri));

	// Won't match if there're more than one matrix params
	uri = "/subresource/matrixParam/C250/2012;color=black";
	srClient.request(buildUri(uri));

	uri = "/subresource/queryParam";
	srClient.queryParamRequest(buildUri(uri), "make", "Mercedes");

	uri = "/subresource/cookieParam";
	srClient.cookieParamRequest(buildUri(uri), "make",
		"Mercedes");

	uri = "/subresource/headerParam";
	srClient.headerParamRequest(buildUri(uri), "make",
		"Mercedes");

	uri = "/subresource/formParam";
	srClient.formParamRequest(buildUri(uri), "make", "Mercedes");

	uri = "/subresource/ctx/Mercedes/C250";
	srClient.request(buildUri(uri));

	uri = "/ctx/Mercedes/C250";
	srClient.request(buildUri(uri));

	srClient.close();
    }

    public static String buildUri(String path) {
	return CAR_RESOURCE_URI + path;
    }

    public String multiplePathSegmentsRequest(String uri) {
	WebTarget wt = client.target(uri);
	Builder builder = wt.request();

	return request(uri, "POST", builder);
    }

    public String queryParamRequest(String uri, String queryParamKey,
	    String queryParamVal) {
	WebTarget wt = client.target(uri);
	Builder builder = wt.queryParam(queryParamKey, queryParamVal).request();

	return request(uri, builder);
    }

    public String cookieParamRequest(String uri, String cookieName,
	    String cookieVal) {
	WebTarget wt = client.target(uri);
	Builder builder = wt.request().cookie(cookieName, cookieVal);

	return request(uri, builder);
    }

    public String headerParamRequest(String uri, String headerName,
	    String headerVal) {
	WebTarget wt = client.target(uri);
	Builder builder = wt.request().header(headerName, headerVal);

	return request(uri, builder);
    }

    public String formParamRequest(String uri, String formParamKey,
	    String formParamVal) {
	Form f = new Form();
	f.param(formParamKey, formParamVal);

	WebTarget wt = client.target(uri);

	/* Form must be POSTed */
	return wt.request(MediaType.APPLICATION_JSON).post(Entity.form(f),
		String.class);
    }

    public String request(String uri) {
	WebTarget wt = client.target(uri);
	Builder builder = wt.request();

	return request(uri, builder);
    }

    private String request(String uri, Builder builder) {
	return request(uri, "GET", builder);
    }

    private String request(String uri, String method, Builder builder) {
	String response = null;

	if ("GET".equals(method)) {
	    response = builder.accept(MediaType.APPLICATION_JSON).get(
		    String.class);
	} else if ("POST".equals(method)) {
	    // The Entity class encapsulates the Java object we want to send
	    // with the POST request
	    response = builder.accept(MediaType.APPLICATION_JSON).post(
		    Entity.text("POST request"), String.class);
	}

	return response;
    }
}
