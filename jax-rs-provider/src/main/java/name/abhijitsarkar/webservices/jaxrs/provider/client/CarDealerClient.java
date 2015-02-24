package name.abhijitsarkar.webservices.jaxrs.provider.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import name.abhijitsarkar.webservices.jaxrs.provider.domain.Car;
import name.abhijitsarkar.webservices.jaxrs.provider.readerNwriter.CarsUnmarshaler;

public class CarDealerClient {
    private static final String DEALER_RESOURCE_URI = "http://localhost:8080/jax-rs-provider/dealer";

    public static void main(String[] args) {
	CarDealerClient dealerClient = new CarDealerClient();

	System.out.println(dealerClient.request(MediaType.APPLICATION_XML));
    }

    public List<Car> request(String mediaType) {
	Client client = ClientFactory.newClient();

	client.register(CarsUnmarshaler.class);

	WebTarget wt = client.target(DEALER_RESOURCE_URI);
	Builder builder = wt.request();

	GenericType<List<Car>> cars = new GenericType<List<Car>>() {
	};

	List<Car> response = builder.accept(mediaType).get(cars);

	client.close();

	return response;
    }
}
