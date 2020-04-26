package com.lending.springboot.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Service
public class InstanceInformationService {

	private static final String ENV_INSTANCE_GUID = "WEBSITE_INSTANCE_ID";

	private static final String DEFAULT_ENV_INSTANCE_GUID = "UNKNOWN";
	

	@Autowired
	private Environment environment;
	

	//@Value(${ENVIRONMENT_VARIABLE_NAME:DEFAULT_VALUE})
	@Value("${" + ENV_INSTANCE_GUID + ":" + DEFAULT_ENV_INSTANCE_GUID + "}")
	private String instanceGuid;

	public String retrieveInstanceInfo() {
		return environment.getProperty("WEBSITE_INSTANCE_ID");
		
	}

}

