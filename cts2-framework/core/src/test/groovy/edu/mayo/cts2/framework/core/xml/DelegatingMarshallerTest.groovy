package edu.mayo.cts2.framework.core.xml;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamSource

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.UnmarshallingFailureException

import edu.mayo.cts2.framework.model.service.core.UpdateChangeSetMetadataRequest
import edu.mayo.cts2.framework.model.service.core.UpdateResourceVersionDescription

class DelegatingMarshallerTest {
	
	DelegatingMarshaller marshaller = new DelegatingMarshaller()
	
	Resource valid = new ClassPathResource("xml/codeSystemValid.xml");
	Resource invalid = new ClassPathResource("xml/codeSystemInvalid.xml");
	Resource msgValid = new ClassPathResource("xml/codeSystemMsgValid.xml");
	Resource updateNull = new ClassPathResource("xml/updateRequestNull.xml");
	Resource updateEmpty = new ClassPathResource("xml/updateRequestEmpty.xml");
	
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
}
