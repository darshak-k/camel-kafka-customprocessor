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
import ca.cdr.api.camel.ITxLogStepsProvider;
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
 * Custom processor showing lazily generated transaction log step addition
 */
public class SampleCustomProcessorAddingTxLogStepsLazily implements Processor, ITxLogStepsProvider {
	@Autowired
	private ICamelProcessorTxLogHelper myTransactionLogHelper;

	@Override
	public void process(Exchange exchange) throws Exception {
		// if transaction log was already started, adds provided step to it, otherwise do nothing
		myTransactionLogHelper.addStepIfTxLogActive(exchange, this);
	}

	// this method is invoked lazily. Useful when steps generation involve heavy process
	@Override
	public List<TransactionLogStepJson> provideTxLogSteps(Exchange theExchange) {
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
