package edu.mayo.cts2.framework.core.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class FixedRestTemplate extends RestTemplate
{
	public FixedRestTemplate()
	{
		super();
	}
	
	public FixedRestTemplate(ClientHttpRequestFactory requestFactory)
	{
		super(requestFactory);
	}

	@Override
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException
	{
		//The Rest URL parsers are broken, and think that _any_ # symbol is a fragment.. and they don't encode it right.
		//Hack the incoming URLS by manually encoding any # that occurs after a ? (our most common use case)
		//Probably a more elegant way to do this...
		String temp = url.toString();
		int pos = temp.indexOf('?');
		if (pos > 0 && temp.indexOf("#", pos) > 0)
		{
			String newUrl = temp.substring(0, pos);
			newUrl += temp.substring(pos).replaceAll("#", "%23");
			try
			{
				url = new URI(newUrl);
			}
			catch (URISyntaxException e)
			{
				throw new RuntimeException("This shouldn't happen");
			}
		}
		return super.doExecute(url, method, requestCallback, responseExtractor);
	}

}
