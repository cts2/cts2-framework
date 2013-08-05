package edu.mayo.cts2.framework.model.service.core;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.BaseCts2ModelObject;

public abstract class AbstractNameToString extends BaseCts2ModelObject {

	public String toString() {
		String returnString;
		if (StringUtils.isNotBlank(this.getName())) {
			if (StringUtils.isNotBlank(this.getUri())){
				returnString = "Name: '" + this.getName() + "' URI: '" + this.getUri() + "'";
			}
			else {
				returnString = "Name: '" + this.getName() + "'";
			}
		} else {
			returnString = "URI: '" + this.getUri() + "'";
		}

		return returnString;
	}

	public abstract String getName();

	public abstract String getUri();
}
