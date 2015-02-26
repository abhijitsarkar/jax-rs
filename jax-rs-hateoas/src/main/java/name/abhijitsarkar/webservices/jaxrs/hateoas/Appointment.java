package name.abhijitsarkar.webservices.jaxrs.hateoas;

public class Appointment {
    private final int id;

    public Appointment(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }
}
