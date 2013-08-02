package edu.mayo.cts2.framework.webapp.soap.directoryuri;

import java.io.Serializable;

public interface SoapDirectoryUriRequest<T> extends Serializable {
	
	public int getPage();
	
	public SoapDirectoryUri<T> getSoapDirectoryUri();
}
