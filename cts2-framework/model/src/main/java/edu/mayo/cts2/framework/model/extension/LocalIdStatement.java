package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.statement.Statement;

public class LocalIdStatement extends LocalIdResource<Statement> {

	public LocalIdStatement(Statement resource) {
		super(resource);
	}

	public LocalIdStatement(String localID,
			Statement resource) {
		super(localID, resource);
	}

}
