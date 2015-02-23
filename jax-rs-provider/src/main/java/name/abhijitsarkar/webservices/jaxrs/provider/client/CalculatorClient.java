package name.abhijitsarkar.webservices.jaxrs.provider.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CalculatorClient {
	private static final String CALC_RESOURCE_URI = "http://localhost:8080/jax-rs-provider/calc";

	public static void main(String[] args) {
		CalculatorClient cClient = new CalculatorClient();

		System.out.println(cClient.request(MediaType.APPLICATION_XML, 1, 2));
		System.out.println(cClient.request(MediaType.APPLICATION_JSON, 1, 2));
	}

	public String request(String mediaType, int arg0, int arg1) {
		Client client = ClientBuilder.newClient();

		WebTarget wt = client.target(CALC_RESOURCE_URI);
		Builder builder = wt.queryParam("arg0", arg0).queryParam("arg1", arg1)
				.request();

		String response = builder.accept(mediaType).get(String.class);

		client.close();

		return response;
	}
}
