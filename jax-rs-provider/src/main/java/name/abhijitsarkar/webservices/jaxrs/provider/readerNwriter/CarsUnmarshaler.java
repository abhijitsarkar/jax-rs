package name.abhijitsarkar.webservices.jaxrs.provider.readerNwriter;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import name.abhijitsarkar.webservices.jaxrs.provider.domain.Car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* We don't need to register this as an entity provider because in this case, it's the client that's unmarshaling, 
 * not the server. If the client were sending a list of cars and the server were accepting it as a method parameter,
 * then we'd need to register this as an entity provider.
 * This class needs to be manually registered on the client side. 
 */
@Consumes(MediaType.APPLICATION_XML)
public class CarsUnmarshaler implements MessageBodyReader<List<Car>> {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(CarsUnmarshaler.class);

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType) {
	return ProviderUtil.isManageable(type, genericType, annotations,
		mediaType);
    }

    @Override
    public List<Car> readFrom(Class<List<Car>> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType,
	    MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {

	return readXML(type, genericType, mediaType, entityStream);
    }

    private List<Car> readXML(Class<List<Car>> type, Type genericType,
	    MediaType mediaType, InputStream entityStream) {
	LOGGER.info("Reading a list of cars from XML.");

	List<Car> cars = new ArrayList<Car>();

	try {
	    Transformer tf = TransformerFactory.newInstance().newTransformer();

	    Source source = new StreamSource(entityStream);
	    DOMResult result = new DOMResult();

	    tf.transform(source, result);

	    Node rootNode = result.getNode().getFirstChild();

	    NodeList children = rootNode.getChildNodes();

	    int numChildren = children.getLength();

	    Node child = null;
	    Class<?> clazz = null;
	    Car car = null;
	    String classnameAttr = "classname";

	    for (int i = 0; i < numChildren; i++) {
		child = children.item(i);

		clazz = Class.forName(child.getAttributes()
			.getNamedItem(classnameAttr).getNodeValue());

		car = (Car) clazz.newInstance();

		car.setName(child.getTextContent());

		cars.add(car);
	    }

	} catch (Exception e) {
	    throw new WebApplicationException(
		    "Can't unmarshal content for media type="
			    + mediaType.toString() + ", type=" + type
			    + ", genericType=" + genericType,
		    Status.INTERNAL_SERVER_ERROR);
	}

	return cars;
    }
}
