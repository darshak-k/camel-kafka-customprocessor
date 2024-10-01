/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2024 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package com.example.camel.transaction;

import ca.cdr.api.camel.ICamelProcessorTxLogHelper;
import ca.cdr.api.model.enm.TransactionLogBodyTypeEnum;
import ca.cdr.api.model.enm.TransactionLogOutcomeEnum;
import ca.cdr.api.model.enm.TransactionLogStepTypeEnum;
import ca.cdr.api.model.json.TransactionLogStepJson;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Custom processor showing transaction log step addition
 */
public class SampleCustomProcessorAddingTxLogSteps implements Processor {
	@Autowired
	private ICamelProcessorTxLogHelper myTransactionLogHelper;

	@Override
	public void process(Exchange exchange) throws Exception {
		// if transaction log was already started, adds provided step to it, otherwise do nothing
		// steps are eagerly generated
		myTransactionLogHelper.addStepIfTxLogActive(exchange, buildTxLogSteps(exchange));
	}

	public List<TransactionLogStepJson> buildTxLogSteps(Exchange theExchange) {
		TransactionLogStepJson step = new TransactionLogStepJson();
		step.setInitialTimestamp(new Date());
		step.setType(TransactionLogStepTypeEnum.PROCESSING);
		step.setOutcome(TransactionLogOutcomeEnum.SUCCESS);
		step.setBody(theExchange.getMessage().getBody(String.class));
		step.setBodyType(TransactionLogBodyTypeEnum.JSON);

		// a string you provide to identify your processor in the transaction log step
		step.setRequestUrl(this.getClass().getSimpleName());

		return List.of(step);
	}
}
