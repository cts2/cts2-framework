package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class CodeSystemCatalogReadServicesEndpointTestIT extends SoapEndpointTestBase {

	String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemCatalogReadService";
	
	@Test
	public void TestRead() throws Exception{
	
		MockServiceProvider.cts2Service = new CodeSystemReadService(){

			@Override
			public CodeSystemCatalogEntry read(NameOrURI identifier,
					ResolvedReadContext readContext) {
				CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry();
				entry.setCodeSystemName("test");
				entry.setAbout("testAbout");
				return entry;
			}

			@Override
			public boolean exists(NameOrURI identifier, ReadContext readContext) {
				throw new UnsupportedOperationException();
			}
			
		};
		
		Read readRequest = new Read();
		readRequest.setCodeSystemId(ModelUtils.nameOrUriFromName("test"));
		
		ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
		
		assertEquals(response.getReturn().getCodeSystemName(), "test");
		
	}


}

