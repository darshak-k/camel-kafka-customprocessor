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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TestHybridProviderAppCtx {

	@Bean
	public DirectCamelEndpointLinkProvider testDirectCamelEndpointLinkProvider() {
		return new DirectCamelEndpointLinkProvider();
	}

	@Bean(name = "resourceProviders")
	public List<Object> resourceProviders() {
		return List.of(testDirectCamelEndpointLinkProvider());
	}
}
