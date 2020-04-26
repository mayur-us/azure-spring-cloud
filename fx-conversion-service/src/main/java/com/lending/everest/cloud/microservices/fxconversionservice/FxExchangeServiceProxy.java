package com.lending.everest.cloud.microservices.fxconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name="fx-rate-service")
//@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="fx-rate-service")
//@FeignClient(name="fx-rate-service")//, url="localhost:8000")
@FeignClient(name="api-gateway-server")//, url="localhost:8000")
public interface FxExchangeServiceProxy {
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
//	@GetMapping("/fx-rate/from/{from}/to/{to}")
	@GetMapping("/fx-rate-service/fx-rate/from/{from}/to/{to}")
	public FxConversionBean retrieveExchangeValue
		(@PathVariable("from") String from, @PathVariable("to") String to);
}
