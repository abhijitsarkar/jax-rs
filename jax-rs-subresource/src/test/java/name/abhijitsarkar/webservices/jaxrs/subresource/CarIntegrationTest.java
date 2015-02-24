package name.abhijitsarkar.webservices.jaxrs.subresource;

import static name.abhijitsarkar.webservices.jaxrs.subresource.client.SubresourceClient.buildUri;
import static org.jboss.shrinkwrap.api.Filters.exclude;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.PathSegment;

import name.abhijitsarkar.webservices.jaxrs.subresource.client.SubresourceClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Arquillian.class)
public class CarIntegrationTest {
    private static SubresourceClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
	WebArchive app = create(WebArchive.class, "jax-rs-subresource.war")
		.addPackages(true,
			exclude(SubresourceClient.class.getPackage()),
			CarApplication.class.getPackage());

	System.out.println(app.toString(true));

	return app;
    }

    @BeforeClass
    public static void initClient() {
	client = new SubresourceClient();
    }

    @AfterClass
    public static void closeClient() {
	client.close();
    }

    @Test
    public void testBasicPathParam() {
	String uri = buildUri("/subresource/basicPathParam/Mercedes");

	assertEquals("Mercedes", client.request(uri));
    }

    @Test
    public void testMultiplePathParams() throws JsonParseException,
	    JsonMappingException, IOException {
	String uri = buildUri("/subresource/multiplePathParams/Mercedes-C250-2012");

	@SuppressWarnings("unchecked")
	Map<String, String> response = objectMapper.readValue(
		client.request(uri), Map.class);

	assertEquals("Mercedes", response.get("make"));
	assertEquals("C250", response.get("model"));
	assertEquals("2012", response.get("year"));
    }

    @Test
    public void testMultiplePathSegments() throws JsonParseException,
	    JsonMappingException, IOException {
	String uri = buildUri("/subresource/multiplePathSegments/Mercedes/C250;"
		+ "loaded=true/yes;moonroof/AMG/2012?"
		+ "make=Mercedes&model=C250");

	@SuppressWarnings("unchecked")
	Map<String, Object> response = objectMapper.readValue(
		client.multiplePathSegmentsRequest(uri), Map.class);

	assertEquals(3, response.size());

	Object obj = response.get("features");

	assertTrue(obj instanceof List);

	@SuppressWarnings("unchecked")
	List<PathSegment> features = objectMapper.readValue(obj.toString(),
		List.class);

	assertEquals(3, features.size());

	verifyPathSegment(features.get(0), "C250", "loaded", "true");
	verifyPathSegment(features.get(1), "yes", "moonroof", "");
	verifyPath(features.get(2), "AMG");

	assertEquals("2012", response.get("year"));
	assertEquals("Mercedes", response.get("make"));
    }

    private void verifyPathSegment(PathSegment segment, String path,
	    String matrixParamKey, String matrixParamValue) {
	verifyPath(segment, path);
	verifyMatrixParams(segment, matrixParamKey, matrixParamValue);
    }

    private void verifyPath(PathSegment segment, String path) {
	assertEquals(path, segment.getPath());
    }

    private void verifyMatrixParams(PathSegment segment, String matrixParamKey,
	    String matrixParamValue) {
	assertEquals(1, segment.getMatrixParameters().size());
	assertEquals(matrixParamValue,
		segment.getMatrixParameters().get(matrixParamKey));
    }
}
