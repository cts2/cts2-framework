package edu.mayo.cts2.framework.model.exception.changeset;

import edu.mayo.cts2.framework.model.exception.Cts2Exception;

public abstract class ChangeSetException extends Cts2Exception {

	private static final long serialVersionUID = 7183795355166459578L;

	public ChangeSetException(String message){
		super(message);
	}

}
