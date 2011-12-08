package edu.mayo.cts2.framework.model.exception.changeset;

public class UnknownChangeSetException extends ChangeSetException {

	private static final long serialVersionUID = 6960508355658830983L;
	
	public UnknownChangeSetException(String changeSetUri) {
		super("ChangeSet: " + changeSetUri + " is not UNKNOWN to the service.");
	}
	
	public UnknownChangeSetException() {
		super("ChangeSetURI was not specified.");
	}


}
