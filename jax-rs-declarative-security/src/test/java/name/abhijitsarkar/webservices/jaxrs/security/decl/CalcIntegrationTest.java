package name.abhijitsarkar.webservices.jaxrs.security.decl;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import name.abhijitsarkar.webservices.jaxrs.auth.HttpBasicAuthFilter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CalcIntegrationTest {
    private static final String WEB_APP_PATH = "src/main/webapp";

    private WebTarget path;

    /*
     * For error "Could not invoke deployment method", check if all artifacts
     * that're being added, exist. Arquillian doesn't say anything useful.
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
	WebArchive war = ShrinkWrap
		.create(WebArchive.class, "jax-rs-declarative-security.war")
		.addPackages(true, CalcDeclarative.class.getPackage())
		.addAsWebInfResource(new File(WEB_APP_PATH, "WEB-INF/web.xml"))
		.addAsWebInfResource(
			new File(WEB_APP_PATH, "WEB-INF/jboss-web.xml"));

	System.out.println(war.toString(true));

	return war;
    }

    @Before
    public void initClient() {
	Client client = ClientBuilder.newClient();
	WebTarget root = client
		.target("http://localhost:8080/jax-rs-declarative-security");

	path = root.path("calc/add").queryParam("augend", 1)
		.queryParam("addend", 2);
    }

    @Test
    public void testAddAuthorized() {
	Builder builder = path.register(
		new HttpBasicAuthFilter("abhijit", "abhijit")).request(
		APPLICATION_JSON_TYPE);

	Response resp = builder.get();

	assertEquals(OK.getStatusCode(), resp.getStatus());

	assertEquals(Integer.valueOf(3), resp.readEntity(int.class));
    }

    @Test
    public void testAddUnauthorized() {
	Builder builder = path
		.register(new HttpBasicAuthFilter("junk", "junk")).request(
			APPLICATION_JSON_TYPE);

	Response resp = builder.get();

	assertEquals(UNAUTHORIZED.getStatusCode(), resp.getStatus());
    }
}
