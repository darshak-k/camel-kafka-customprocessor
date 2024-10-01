/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2024 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package com.example.camel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelCustomProcessorCtx {
	@Bean
	public CamelResourceProcessor testCustomProcessor() {
		return new CamelResourceProcessor();
	}
}
