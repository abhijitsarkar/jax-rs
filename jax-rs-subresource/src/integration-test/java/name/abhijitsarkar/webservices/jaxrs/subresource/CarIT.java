package name.abhijitsarkar.webservices.jaxrs.subresource;

import static name.abhijitsarkar.webservices.jaxrs.subresource.client.SubresourceClient.buildUri;
import static org.jboss.shrinkwrap.api.Filters.exclude;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
public class CarIT {
    private static SubresourceClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
	WebArchive app = create(WebArchive.class, "jax-rs-subresource.war")
		.addPackages(true,
			exclude(SubresourceClient.class.getPackage()),
			CarApplication.class.getPackage()).addAsResource(
			new File("src/main/resources/logback.xml"));

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
    @SuppressWarnings("unchecked")
    public void testMultiplePathSegments() throws JsonParseException,
	    JsonMappingException, IOException {
	String uri = buildUri("/subresource/multiplePathSegments/Mercedes/C250;"
		+ "loaded=true/yes;moonroof/AMG/2012?"
		+ "make=Mercedes&model=C250");

	/*
	 * Response is a map with 3 keys, 'make', 'year' and 'features', the
	 * last is a List of type PathSegment.
	 */
	Map<String, Object> response = objectMapper.readValue(
		client.multiplePathSegmentsRequest(uri), Map.class);

	assertEquals(3, response.size());

	Object obj = response.get("features");

	assertTrue(obj instanceof List);

	/*
	 * Each feature of type PathSegment is a map with 3 keys, the 'path',
	 * 'originalPath' and 'matrixParameters'. 'matrixParameters' is a List
	 * itself. 'originalPath' is not part of the PathSegment interface but
	 * I've not looked into how is it getting included. I don't want to
	 * spend time on customizing the PathSegment serialization into JSON so
	 * whatever the JSON provider does by default works for me, as long as
	 * the keys I expect are there.
	 */
	List<Map<String, Object>> features = (List<Map<String, Object>>) obj;

	/*
	 * According to the test data above, there should be 3 features
	 * corresponding to 3 paths - namely C250, yes and AMG.
	 */
	assertEquals(3, features.size());

	/*
	 * Find each PathSegment and verify their corresponding matrix
	 * parameters. Throw an exception if a path segment is not found because
	 * according to the test data above, it should be there.
	 */
	Map<String, Object> aFeature = findFeatureWithPath(features, "C250");
	Map<String, List<String>> matrixParams = (Map<String, List<String>>) aFeature
		.get("matrixParameters");
	verifyMatrixParams(matrixParams, "loaded", "true");

	aFeature = findFeatureWithPath(features, "yes");
	matrixParams = (Map<String, List<String>>) aFeature
		.get("matrixParameters");
	verifyMatrixParams(matrixParams, "moonroof", "");

	aFeature = findFeatureWithPath(features, "AMG");
	matrixParams = (Map<String, List<String>>) aFeature
		.get("matrixParameters");
	assertTrue(matrixParams.isEmpty());
    }

    private Map<String, Object> findFeatureWithPath(
	    List<Map<String, Object>> features, String path) {
	for (Map<String, Object> aFeature : features) {
	    if (path.equals(aFeature.get("path"))) {
		return aFeature;
	    }
	}

	throw new NoSuchElementException("No feature found with path: " + path);
    }

    private void verifyMatrixParams(Map<String, List<String>> matrixParams,
	    String matrixParamKey, String expectedValue) {
	assertNotNull(matrixParams);
	assertArrayEquals(new String[] { expectedValue },
		matrixParams.get(matrixParamKey).toArray(new String[] {}));
    }

    @Test
    public void testMatrixParam() {
	String uri = buildUri("/subresource/matrixParam/C250/2012;color=black");

	assertEquals("black", client.request(uri));
    }

    @Test
    public void testQueryParam() {
	String uri = buildUri("/subresource/queryParam");

	assertEquals("Mercedes",
		client.queryParamRequest(uri, "make", "Mercedes"));
    }

    @Test
    public void testCookieParam() {
	String uri = buildUri("/subresource/cookieParam");

	assertEquals("Mercedes",
		client.cookieParamRequest(uri, "make", "Mercedes"));
    }

    @Test
    public void testHeaderParam() {
	String uri = buildUri("/subresource/headerParam");

	assertEquals("Mercedes",
		client.headerParamRequest(uri, "make", "Mercedes"));
    }

    @Test
    public void testFormParam() {
	String uri = buildUri("/subresource/formParam");

	assertEquals("Mercedes",
		client.formParamRequest(uri, "make", "Mercedes"));
    }

    @Test
    public void testContext1() {
	String uri = buildUri("/subresource/ctx/Mercedes/C250");

	assertEquals("Mercedes", client.request(uri));
    }

    @Test
    public void testContext2() {
	String uri = buildUri("/ctx/Mercedes/C250");

	assertEquals("Mercedes", client.request(uri));
    }
}
