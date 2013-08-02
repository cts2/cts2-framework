package edu.mayo.cts2.framework.service.command.restriction;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;

public class TaggedCodeSystemRestriction {
	private NameOrURI codeSystem;
	private String tag;
	
	public NameOrURI getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(NameOrURI codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}	
}