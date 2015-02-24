package name.abhijitsarkar.webservices.jaxrs.provider.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import name.abhijitsarkar.webservices.jaxrs.provider.filter.HttpHeadersLoggingClientFilter;

public class ClientFactory {
    public static Client newClient() {
	Client client = ClientBuilder.newClient();
	
	client.register(HttpHeadersLoggingClientFilter.class);

	return client;
    }
}
