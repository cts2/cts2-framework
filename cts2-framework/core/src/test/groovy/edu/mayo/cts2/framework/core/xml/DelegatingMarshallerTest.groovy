package edu.mayo.cts2.framework.core.xml;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamSource

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.UnmarshallingFailureException

import edu.mayo.cts2.framework.core.xml.DelgatingMarshaller;

class DelegatingMarshallerTest {
	
	DelgatingMarshaller marshaller = new DelgatingMarshaller()
	
	Resource valid = new ClassPathResource("xml/codeSystemValid.xml");
	Resource invalid = new ClassPathResource("xml/codeSystemInvalid.xml");
	Resource msgValid = new ClassPathResource("xml/codeSystemMsgValid.xml");
	
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
		
		assertNotNull marshaller.unmarshal(new StreamSource(stream))
	}
}
