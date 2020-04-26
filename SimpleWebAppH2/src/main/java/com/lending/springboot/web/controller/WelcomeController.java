package com.lending.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lending.springboot.web.InstanceInformationService;

@Controller
public class WelcomeController {
	
	@Autowired
	private InstanceInformationService instanceService;
	
	@Autowired
	private Environment environment;

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String showWelcomePage(ModelMap model) {
//		model.put("name : ", getLoggedinUserName());
////		model.put("instanceId ==> ", environment.getProperty("WEBSITE_INSTANCE_ID"));
//		return "welcome";
//	}
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showWelcomePage(ModelMap model) {
		model.put("name", getLoggedinUserName());
		model.put("instanceId", environment.getProperty("WEBSITE_INSTANCE_ID"));
		return "welcome";
	}

	private String getLoggedinUserName() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		
		return principal.toString();
	}

}
