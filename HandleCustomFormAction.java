package com.aem.core;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

public interface HandleCustomFormAction {

	public void injestFormData(SlingHttpServletRequest request, Resource resource);
}
