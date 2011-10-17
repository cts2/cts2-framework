package edu.mayo.cts2.framework.webapp.rest.controller;

import edu.mayo.cts2.framework.model.core.Message;

public interface MessageFactory<R> {
	
	public Message createMessage(R resource);

}
