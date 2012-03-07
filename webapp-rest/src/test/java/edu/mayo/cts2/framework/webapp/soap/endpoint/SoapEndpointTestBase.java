package edu.mayo.cts2.framework.webapp.soap.endpoint;

import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory;

public class SoapEndpointTestBase {
	
	public static boolean serverRunning = false;
	
	protected static DelegatingMarshaller marshaller;
	protected static WebServiceTemplate template;
	
	DelegatingMarshaller getMarshaller(){
		return marshaller;
	}
	
	static {
		try {
			marshaller = new DelegatingMarshaller();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		template = new WebServiceTemplate(marshaller, marshaller);
		template.setMessageSender(new HttpUrlConnectionMessageSender());
		SaajSoapMessageFactory mf = new SaajSoapMessageFactory();
		mf.afterPropertiesSet();
		template.setMessageFactory(mf);
		template.afterPropertiesSet();
	}

	@BeforeClass
    public static void setUp() throws Exception {

		if(!serverRunning){
			System.setProperty("cts2.config.dir", "target/testconfigdir");
			System.setProperty("test.plugins.dir", "target/test-plugins");
			System.setProperty(ServiceProviderFactory.USE_CLASSPATH_PROVIDER_PROP, "true");
			
	        Server server = new Server(8081);
	        WebAppContext ctx = new WebAppContext();
	        ctx.setClassLoader(Thread.currentThread().getContextClassLoader());
	        ctx.setContextPath("/webapp-rest");
	        ctx.setWar("/Users/m091355/Dropbox/code/cts2/cts2-framework/webapp-rest/src/main/webapp");
	        ctx.setServer(server);
	        server.setHandler(ctx);
	        server.start();
	        
	        Thread.sleep(5000);
	        
	        serverRunning = true;
		}
    }
	
	protected Object doSoapCall(String uri, Object request){
		return template.marshalSendAndReceive(uri, request);
	}
}
