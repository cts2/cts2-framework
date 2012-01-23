package edu.mayo.cts2.framework.model.service.core;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractNameToString {

	public String toString() {
		String returnString;
		if (StringUtils.isNotBlank(this.getName())) {
			returnString = "Name: " + this.getName();
		} else {
			returnString = "URI: " + this.getUri();
		}

		return returnString;
	}

	public abstract String getName();

	public abstract String getUri();
}
