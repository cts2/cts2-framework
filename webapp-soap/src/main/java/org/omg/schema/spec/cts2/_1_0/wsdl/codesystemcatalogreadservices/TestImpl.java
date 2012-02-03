package org.omg.schema.spec.cts2._1_0.wsdl.codesystemcatalogreadservices;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;

public class TestImpl implements CodeSystemCatalogReadServicePortType {

	@Override
	public CodeSystemCatalogEntry read(NameOrURI codeSystemId,
			QueryControl queryControl, ReadContext context) {

		CodeSystemCatalogEntry e = new CodeSystemCatalogEntry();
		e.setAbout("asdfb");
		e.setCodeSystemName("asdf");
		
		return e;
	}

}
