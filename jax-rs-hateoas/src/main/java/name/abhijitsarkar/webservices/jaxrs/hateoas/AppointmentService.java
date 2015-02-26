package name.abhijitsarkar.webservices.jaxrs.hateoas;

import java.util.List;
import java.util.Optional;

public class AppointmentService {

    public List<Appointment> getAppointments() {
	return null;
    }

    public void newAppointment(int id) {
	// TODO Auto-generated method stub
    }

    public void deleteAppointment(int id) {
	// TODO Auto-generated method stub

    }

    public Optional<Appointment> findAppointmentById(int id) {
	return getAppointments().stream().filter(appt -> appt.getId() == id)
		.findFirst();
    }
}
