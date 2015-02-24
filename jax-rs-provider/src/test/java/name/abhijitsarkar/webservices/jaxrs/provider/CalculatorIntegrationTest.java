package name.abhijitsarkar.webservices.jaxrs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.shrinkwrap.api.Filters.exclude;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import name.abhijitsarkar.webservices.jaxrs.provider.client.CalculatorClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CalculatorIntegrationTest {
    private final CalculatorClient client = new CalculatorClient();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
	WebArchive app = create(WebArchive.class, "jax-rs-provider.war")
		.addPackages(true,
			exclude(CalculatorClient.class.getPackage()),
			JAXRSProviderApplication.class.getPackage());

	System.out.println(app.toString(true));

	return app;
    }

    @Test
    public void testAdd() throws Exception {
	assertEquals(3, client.request(APPLICATION_JSON, 1, 2));
    }
}
