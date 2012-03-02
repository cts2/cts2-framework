package edu.mayo.cts2.framework.webapp.rest.exception;

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass;
import edu.mayo.cts2.framework.model.service.core.types.LoggingLevel;
import edu.mayo.cts2.framework.model.service.exception.CTS2Exception;
import edu.mayo.cts2.framework.model.service.exception.types.ExceptionType;
import edu.mayo.cts2.framework.model.util.ModelUtils;

public class StatusSettingCts2RestException extends CTS2Exception implements MarshallSuperClass {

	private static final long serialVersionUID = 8198403323810182068L;

	private int statusCode;

	public StatusSettingCts2RestException(
			String message, 
			ExceptionType exceptionType, 
			int statusCode){
		this.statusCode = statusCode;
		this.setSeverity(LoggingLevel.ERROR);
		this.setExceptionType(exceptionType);
		this.setCts2Message(ModelUtils.createOpaqueData(message));
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
		
}
