package edu.mayo.cts2.framework.webapp.soap.directoryuri;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.service.core.FilterComponent;

public class DirectoryUriUtils {

	private static final String DIRECTORY_URI_NSI = "directoryuri";
	private static final String URN = "urn";
	private static final String DIRECTORY_URI_PREFIX = URN + ":" + DIRECTORY_URI_NSI + ":";

	private DirectoryUriUtils() {
		super();
	}

	public static SoapDirectoryUriRequest<?> deserialize(String serializedDirectoryUri) {
		String directoryUri = StringUtils.substringAfter(serializedDirectoryUri, DIRECTORY_URI_PREFIX);
		
		byte[] bytes = Base64.decodeBase64(directoryUri.getBytes());

		return (SoapDirectoryUriRequest<?>) SerializationUtils.deserialize(bytes);
	}


	public static String serialize(SoapDirectoryUriRequest<?> directoryUri) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		SerializationUtils.serialize(directoryUri, baos);

		String serializedDirectoryUri = new String(Base64.encodeBase64(baos.toByteArray()));
		
		return DIRECTORY_URI_PREFIX + serializedDirectoryUri;
	}
	
	public static <T> SoapDirectoryUriRequest<T> buildSoapDirectoryUriRequest(
			int page,
			Set<FilterComponent> filters, 
			T restrictions){
		DefaultSoapDirectoryUri<T> uri = new DefaultSoapDirectoryUri<T>();
		uri.setFilterComponents(filters);
		uri.setRestrictions(restrictions);
		
		DefaultSoapDirectoryUriRequest<T> request = new DefaultSoapDirectoryUriRequest<T>();
		request.setPage(page);
		request.setSoapDirectoryUri(uri);
		
		return request;
	}

}
