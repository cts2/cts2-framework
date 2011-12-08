package edu.mayo.cts2.framework.model.exception;

public abstract class Cts2Exception extends RuntimeException {

	private static final long serialVersionUID = 7183795355166459578L;
	
	public Cts2Exception(String message){
		super(message);
	}

}
