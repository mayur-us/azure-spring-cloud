package com.lending.everest.cloud.microservices.fxconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name="fx-rate-service")
//@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="limits-service")
@FeignClient(name="limits-service")//, url="localhost:8000")
public interface LimitConfigurationServiceProxy {
	
	@GetMapping("/limits-service/limits")
	public LimitConfigurationBean retrieveLimitsFromConfigurations();
}
