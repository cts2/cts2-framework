package edu.mayo.cts2.framework.core.xml;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.UnmarshallingFailureException
import org.springframework.oxm.ValidationFailureException

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg
import edu.mayo.cts2.framework.model.service.core.UpdateResourceVersionDescription

class DelegatingMarshallerTest {
	
	DelegatingMarshaller marshaller = new DelegatingMarshaller()
	
	Resource valid = new ClassPathResource("xml/codeSystemValid.xml");
	Resource invalid = new ClassPathResource("xml/codeSystemInvalid.xml");
	Resource msgValid = new ClassPathResource("xml/codeSystemMsgValid.xml");
	Resource updateNull = new ClassPathResource("xml/updateRequestNull.xml");
	Resource updateEmpty = new ClassPathResource("xml/updateRequestEmpty.xml");
	Resource rxNorm = new ClassPathResource("xml/rxNorm.xml");
	Resource emptyVersions = new ClassPathResource("xml/emptyVersions.xml");
	
	Resource changeSetMetaNil = new ClassPathResource("xml/updateChangeSetMetadataNil.xml");
	
	@Test
	void "Test Unmarshall Valid"(){
		def stream = valid.getInputStream()
		
		assertNotNull marshaller.unmarshal(new StreamSource(stream))
	}
	
	@Test(expected=UnmarshallingFailureException.class)
	void "Test Unmarshall InValid"(){
		def stream = invalid.getInputStream()
		
		marshaller.unmarshal(new StreamSource(stream))
	}
	
	@Test(expected=ValidationFailureException.class)
	void "Test Marshall InValid"(){
		marshaller.marshal(new CodeSystemCatalogEntry(), new StreamResult(new StringWriter()))

	} 
	
	@Test
	void "Test Unmarshall Valid Msg"(){
		def stream = msgValid.getInputStream()
		
		def o = marshaller.unmarshal(new StreamSource(stream))

	}
	
	@Test
	void "Test Unmarshall Update Null"(){
		def stream = updateNull.getInputStream()
		
		UpdateResourceVersionDescription o = marshaller.unmarshal(new StreamSource(stream))
		
		assertNull o.getUpdatedAdditionalDocumentation()

	}
	
	@Test
	void "Test Unmarshall Update Empty"(){
		def stream = updateEmpty.getInputStream()
		
		UpdateResourceVersionDescription o = marshaller.unmarshal(new StreamSource(stream))
		
		assertEquals 0, o.getUpdatedAdditionalDocumentation().getUriCount()

	}
	
	@Test
	void "Test Unmarshall RxNorm"(){
		def stream = rxNorm.getInputStream()
		
		CodeSystemCatalogEntry o = marshaller.unmarshal(new StreamSource(stream))

	}
	
	@Test
	void "Test Unmarshall with Null Versions"(){
		def stream = valid.getInputStream()
		
		CodeSystemCatalogEntry o = marshaller.unmarshal(new StreamSource(stream))
		
		assertNull o.getVersions()

	}
	
	@Test
	void "Test Unmarshall with Empty Versions"(){
		def stream = emptyVersions.getInputStream()
		
		CodeSystemCatalogEntry o = marshaller.unmarshal(new StreamSource(stream))
		
		assertEquals "",  o.getVersions()

	}
	
	@Test
	void "Test Marshall with Null Versions"(){
		def stream = valid.getInputStream()
		
		def sw = new StringWriter();
		
		marshaller.marshal(
			new CodeSystemCatalogEntry(codeSystemName:"n",about:"about"), new StreamResult(sw))

		
		assertFalse sw.toString().contains("<versions>");

	}
	
	@Test
	void "Test Marshall with Empty Versions"(){
		def stream = emptyVersions.getInputStream()
		
		def sw = new StringWriter();
		
		marshaller.marshal(
			new CodeSystemCatalogEntry(codeSystemName:"n",about:"about",versions:""), new StreamResult(sw))

		println sw.toString()
		
		assertTrue sw.toString().contains("<versions></versions>");

	}
}
