package edu.mayo.cts2.framework.model.exception.changeset;

public class ChangeSetIsNotOpenException extends ChangeSetException {

	private static final long serialVersionUID = 6960508355658830983L;

	public ChangeSetIsNotOpenException(String changeSetUri){
		super("ChangeSet: " + changeSetUri + " is not OPEN.");
	}
}
