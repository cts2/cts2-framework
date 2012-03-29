package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.GetAllCodeSystems
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.GetAllCodeSystemsResponse
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Resolve
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase

class CodeSystemCatalogQueryServicesEndpointTestIT extends SoapEndpointTestBase {

		String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemCatalogQueryService";
	
		@Test
	    void TestGetAllCodeSystemsResponse() {
			MockServiceProvider.cts2Service = [
				getResourceSummaries:{query,sort,page-> return null}
			] as CodeSystemQueryService

			def request = new GetAllCodeSystems();
			
			def response = this.doSoapCall(uri, request)
			
			assertNotNull response
			
			assertTrue response instanceof GetAllCodeSystemsResponse	
		}
		
		@Test
		void TestResolve() {
			MockServiceProvider.cts2Service = [
				getResourceSummaries:{query,sort,page-> return new DirectoryResult(
					[new CodeSystemCatalogEntrySummary(about:"about", codeSystemName:"name")],
					true)}
			] as CodeSystemQueryService

			def request = new GetAllCodeSystems();
			
			def response = this.doSoapCall(uri, request)
			
			def results = this.doSoapCall(uri, new Resolve(directory:response.return));
			
			assertNotNull results
			
			assertEquals 1, results.codeSystemCatalogEntryDirectory.entry.size()
			
			assertEquals "name", results.codeSystemCatalogEntryDirectory.getEntry(0).codeSystemName
		}
}
