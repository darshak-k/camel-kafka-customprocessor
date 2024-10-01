/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2024 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package org.example.camel.hybrid;

import ca.cdr.api.camel.ICamelRouteEndpointSvc;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * Resource provider using an {@link ICamelRouteEndpointSvc} to send messages to a camel module route
 */
public class DirectCamelEndpointLinkProvider implements IResourceProvider {

	public static final String MODULE_NAME = "camel";
	public static final String ROUTE_NAME = "test-route1";

	@Autowired
	private ICamelRouteEndpointSvc myICamelRouteEndpointSvc;

	@Read(version = true)
	public Patient readPatient(@IdParam IdType theId) {
		// In a real use case patient would be fetched from some repository
		Patient pt = new Patient();
		pt.setId(theId);
		pt.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");

		myICamelRouteEndpointSvc.sendToCamelRoute(MODULE_NAME, ROUTE_NAME, pt, Collections.emptyMap());

		return pt;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Patient.class;
	}
}
