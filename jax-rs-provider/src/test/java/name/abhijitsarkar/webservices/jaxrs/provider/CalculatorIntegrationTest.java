package name.abhijitsarkar.webservices.jaxrs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.xml.bind.JAXB.unmarshal;
import static org.jboss.shrinkwrap.api.Filters.exclude;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import name.abhijitsarkar.webservices.jaxrs.provider.client.CalculatorClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Arquillian.class)
public class CalculatorIntegrationTest {
    private final CalculatorClient client = new CalculatorClient();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
	WebArchive app = create(WebArchive.class, "jax-rs-provider.war")
		.addPackages(true,
			exclude(CalculatorClient.class.getPackage()),
			ProviderApplication.class.getPackage());

	System.out.println(app.toString(true));

	return app;
    }

    @Test
    public void testAddAcceptingJSON() throws JsonParseException,
	    JsonMappingException, IOException {
	@SuppressWarnings("unchecked")
	Map<String, Integer> response = new ObjectMapper().readValue(
		client.request(APPLICATION_JSON, 1, 2), Map.class);

	assertEquals(Integer.valueOf(3), response.get("value"));
    }

    @Test
    public void testAddAcceptingXML() {
	String response = client.request(APPLICATION_XML, 1, 2);

	assertEquals(Integer.valueOf(3),
		unmarshal(new StringReader(response), Integer.class));
    }
}
