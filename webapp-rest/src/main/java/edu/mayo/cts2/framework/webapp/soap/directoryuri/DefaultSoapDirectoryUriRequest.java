package edu.mayo.cts2.framework.webapp.soap.directoryuri;

public class DefaultSoapDirectoryUriRequest<T> implements SoapDirectoryUriRequest<T>{

	private static final long serialVersionUID = 749234657373420959L;
	
	private int page = 0;
	private SoapDirectoryUri<T> soapDirectoryUri;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public SoapDirectoryUri<T> getSoapDirectoryUri() {
		return soapDirectoryUri;
	}
	
	public void setSoapDirectoryUri(SoapDirectoryUri<T> soapDirectoryUri) {
		this.soapDirectoryUri = soapDirectoryUri;
	}

}
