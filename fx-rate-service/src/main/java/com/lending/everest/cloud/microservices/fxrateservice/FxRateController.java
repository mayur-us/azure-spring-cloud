package com.lending.everest.cloud.microservices.fxrateservice;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FxRateController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment environment;
	
//	@Autowired
//	private FxExchangeValueRepository repository;
	
	@GetMapping("/fx-rate/from/{from}/to/{to}")
	public FxExchangeValue retrieveExchangeValue
		(@PathVariable String from, @PathVariable String to){
		
//		FxExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
		FxExchangeValue exchangeValue = new FxExchangeValue(1L, "USD", "INR", BigDecimal.valueOf(0.65));
		
//		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
//		exchangeValue.setPort(Integer.parseInt(environment.getProperty("WEBSITE_INSTANCE_ID")));
		exchangeValue.setInstanceId(environment.getProperty("WEBSITE_INSTANCE_ID"));
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		
		logger.info("{}", exchangeValue);
		
		return exchangeValue;
	}
}
