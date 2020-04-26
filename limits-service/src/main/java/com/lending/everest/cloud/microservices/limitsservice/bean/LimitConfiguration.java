package com.lending.everest.cloud.microservices.limitsservice.bean;

public class LimitConfiguration {
	private int maximum;
	private int minimum;
	
	private int port;
	

	protected LimitConfiguration() {

	}

	
	
	public LimitConfiguration(int maximum, int minimum) {
		super();
		this.maximum = maximum;
		this.minimum = minimum;
	}
	
	

	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}

	

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}



	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}



	public int getMaximum() {
		return maximum;
	}

	public int getMinimum() {
		return minimum;
	}

}
