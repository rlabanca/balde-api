package org.baldeapi.v1.security;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.baldeapi.v1.representation.ErrorRepresentationV1;
import org.baldeapi.v1.representation.ErrorType;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;

@Provider
public class ResourceFilterFactory extends RolesAllowedResourceFilterFactory {

	private ApiKeyValidationFilter apiKeyValidationFilter = new ApiKeyValidationFilter();
	private UserValidationFilter userValidationFilter = new UserValidationFilter();
	private BasicAuthValidationFilter basicAuthValidationFilter = new BasicAuthValidationFilter();
	
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		
		
		List<ResourceFilter> filters;
		try {
			List<ResourceFilter> rolesFilters = super.create(am);
			if (null == rolesFilters) {
				rolesFilters = new ArrayList<ResourceFilter>();
			}

			filters = new ArrayList<ResourceFilter>(
					rolesFilters);

			filters.add(0, apiKeyValidationFilter);
			// if("dev".equalsIgnoreCase(System.getProperty("app.environment")))  filters.add(1, buscapeSingleSignOnValidationFilter);
		} catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.PERMISSION_DENIED, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}

//<<<<<<< HEAD
//=======
//		List<ResourceFilter> filters = new ArrayList<ResourceFilter>(
//				rolesFilters);
//
//		filters.add(0, apiKeyValidationFilter);
//		filters.add(1, basicAuthValidationFilter);
//		filters.add(2, userValidationFilter);
////		filters.add(1, buscapeSingleSignOnValidationFilter);

//>>>>>>> 0ddd90a8d00370e69f0d314e0fc9445f87a9be85
		return filters;
	}

}
