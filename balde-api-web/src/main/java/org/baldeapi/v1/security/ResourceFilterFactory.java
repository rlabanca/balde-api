package org.baldeapi.v1.security;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;

@Provider
public class ResourceFilterFactory extends RolesAllowedResourceFilterFactory {

	private ApiKeyValidationFilter apiKeyValidationFilter = new ApiKeyValidationFilter();
	
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		
		List<ResourceFilter> rolesFilters = super.create(am);
		if (null == rolesFilters) {
			rolesFilters = new ArrayList<ResourceFilter>();
		}

		List<ResourceFilter> filters = new ArrayList<ResourceFilter>(
				rolesFilters);

		filters.add(0, apiKeyValidationFilter);

		return filters;
	}

}
