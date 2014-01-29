package com.winston.response;

public class ResponseObject {

	public String status;
	public String time;
	long timeLong;
	long start;
	long stop;
	public Object data;
	
	//================================================================================
	// Constructor
	//================================================================================
	public ResponseObject() {
		status = "OK";
	}
	
	//================================================================================
	// Start timer
	//================================================================================
	public void startTimer() {
		start = System.currentTimeMillis();
	}
	
	//================================================================================
	// Stop timer
	//================================================================================
	public void stopTimer() {
		stop = System.currentTimeMillis();
		
		timeLong = stop - start;
		
		time = timeLong + " ms";
	}
	
	//================================================================================
	// Add data
	//================================================================================
	public void addData(Object object) {
		data = object;
	}
}
