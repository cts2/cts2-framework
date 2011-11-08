package edu.mayo.cts2.framework.webapp.rest.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {
	
	private UriUtils(){
		super();
	}
	
	public static boolean isValidUri(String uriCandidate){
		try {
			new URI(uriCandidate);
		} catch (URISyntaxException e) {
			return false;
		}
		
		return true;
	}
}
