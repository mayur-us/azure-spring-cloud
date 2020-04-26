package com.lending.everest.cloud.microservices.fxconversionservice;

public class LimitConfigurationBean {
	private int maximum;
	private int minimum;
	
	private int port;

	protected LimitConfigurationBean() {

	}

	public LimitConfigurationBean(int maximum, int minimum , int port) {
		super();
		this.maximum = maximum;
		this.minimum = minimum;
		this.port = port;
	}

	public int getMaximum() {
		return maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	

}
