package com.lending.everest.cloud.microservices.fxconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FxConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FxExchangeServiceProxy fxProxy;
	
	@Autowired
	private LimitConfigurationServiceProxy limitsProxy;

	@GetMapping("/fx-converter/from/{from}/to/{to}/quantity/{quantity}")
	public FxConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<FxConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/fx-rate/from/{from}/to/{to}", FxConversionBean.class,
				uriVariables);

		FxConversionBean response = responseEntity.getBody();

		return new FxConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort(),response.getInstanceId());
	}
	
	@GetMapping("/fx-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public FxConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		FxConversionBean response = fxProxy.retrieveExchangeValue(from, to);

		logger.info("{}", response);
		
		return new FxConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort(),response.getInstanceId());
	}
	
	@GetMapping("/fx-converter/validatelimits")
	public LimitConfigurationBean validateLimits() {

		
		ResponseEntity<LimitConfigurationBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8080/limits-service/limits", LimitConfigurationBean.class);

		LimitConfigurationBean response = responseEntity.getBody();

		return new LimitConfigurationBean(response.getMinimum(),response.getMaximum(),response.getPort());
	}
	
	
	@GetMapping("/fx-converter-feign/validatelimits")
	public LimitConfigurationBean validateLimitsFeign() {

		LimitConfigurationBean response = limitsProxy.retrieveLimitsFromConfigurations();

		return new LimitConfigurationBean(response.getMinimum(),response.getMaximum(),response.getPort());
	}


	

}
