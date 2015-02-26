package name.abhijitsarkar.webservices.jaxrs.security.pgm;

import static javax.ws.rs.Priorities.AUTHORIZATION;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
public class MustBeMathematicianFeature implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
	Method method = resourceInfo.getResourceMethod();

	boolean mustBeMathematician = (method != null && method
		.getAnnotation(MustBeMathematician.class) != null);

	if (mustBeMathematician) {
	    context.register(new MustBeMathematicianFilter());
	}
    }

    /* authorization filter - should go after any authentication filters */
    @Priority(AUTHORIZATION)
    private static class MustBeMathematicianFilter implements
	    ContainerRequestFilter {
	private static final String ROLE_MATEMATICIAN = "mathematician";

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {
	    SecurityContext secContext = requestContext.getSecurityContext();

	    if (secContext.isUserInRole(ROLE_MATEMATICIAN)) {
		return;
	    }

	    throw new ForbiddenException(secContext.getUserPrincipal()
		    + " is not authorized for this operation.");
	}
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MustBeMathematician {
    }
}
