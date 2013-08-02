package edu.mayo.cts2.framework.core.xml

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.ChangeType
import org.apache.commons.io.IOUtils
import org.junit.Ignore
import org.springframework.oxm.castor.CastorMarshaller;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.UnmarshallingFailureException
import org.springframework.oxm.ValidationFailureException

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.service.core.UpdateResourceVersionDescription
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteValueSetReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry

class DelegatingMarshallerTest {

    class TestCodeSystem
        extends CodeSystemCatalogEntry
        implements MarshallSuperClass {

    }
	 
	DelegatingMarshaller marshaller = new DelegatingMarshaller()
	
	Resource valid = new ClassPathResource("xml/codeSystemValid.xml");
	Resource invalid = new ClassPathResource("xml/codeSystemInvalid.xml");
	Resource msgValid = new ClassPathResource("xml/codeSystemMsgValid.xml");
	Resource updateNull = new ClassPathResource("xml/updateRequestNull.xml");
	Resource updateEmpty = new ClassPathResource("xml/updateRequestEmpty.xml");
	Resource rxNorm = new ClassPathResource("xml/rxNorm.xml");
	Resource emptyVersions = new ClassPathResource("xml/emptyVersions.xml");
	Resource getAllCodeSystemsResponse = new ClassPathResource("xml/GetAllCodeSystemsResponse.xml");

    @Test
    void "Test Get Marshaller"(){
        assertTrue CastorMarshaller.class != marshaller.getMarshaller(TestCodeSystem).class
    }

    @Ignore("I think this is a Castor bug.")
    @Test(expected=ValidationFailureException.class)
    void "Test Marshall Invalid Proxy"(){
        def result = new StringWriter()
        marshaller.marshal(new TestCodeSystem(), new StreamResult(result))

        print result
    }

	@Test
	void "Test Unmarshall Valid"(){
		def stream = valid.getInputStream()
		
		assertNotNull marshaller.unmarshal(new StreamSource(stream))
	}
	
	@Test
	void "Test Unmarshall GetAllCodeSystemsRequest"(){
		def stream = getAllCodeSystemsResponse.getInputStream()
		
		assertNotNull marshaller.unmarshal(new StreamSource(stream))
	}
	
	@Test(expected=UnmarshallingFailureException.class)
	void "Test Unmarshall InValid"(){
		def stream = invalid.getInputStream()
		
		marshaller.unmarshal(new StreamSource(stream))
	}
	
	@Test
	void "Test Unmarshall InValid With NON_VALIDATING"(){
		def stream = invalid.getInputStream()
		
		def m = new DelegatingMarshaller(false)
		
		m.unmarshal(new StreamSource(stream))
	}
	
	
	@Test(expected=UnmarshallingFailureException.class)
	void "Test Unmarshall InValid With WITH_VALIDATING"(){
		def stream = invalid.getInputStream()
		
		def m = new DelegatingMarshaller(true)
		
		m.unmarshal(new StreamSource(stream))
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
	void "Test Marshall ValueSetDefinition"(){
		def stream = valid.getInputStream()
		
		def sw = new StringWriter();
		
		marshaller.marshal(
			new ValueSetDefinition(
				documentURI:"test",
				about:"test",
				definedValueSet:new ValueSetReference(),
				entry: [
					new ValueSetDefinitionEntry(
						operator:SetOperator.UNION,
						entryOrder:1,
						completeValueSet:new CompleteValueSetReference(valueSet:new ValueSetReference()))	
				],
				sourceAndNotation:new SourceAndNotation()), new StreamResult(sw))

		
		println sw.toString()
		
		assertNotNull sw.toString()

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

    @Test
    void "Test Marshall with ChangeDescription"(){
        def stream = emptyVersions.getInputStream()

        def sw = new StringWriter();

        def entry = new CodeSystemCatalogEntry(
            codeSystemName:"n",
            about:"about",versions:"",
            changeableElementGroup:
                new ChangeableElementGroup( changeDescription:
                        new ChangeDescription(
                                changeDate: new Date(),
                                changeType: ChangeType.CREATE,
                                containingChangeSet: "asdf") )
        )

        marshaller.marshal(entry, new StreamResult(sw))

        println sw.toString()

        marshaller.unmarshal(new StreamSource(IOUtils.toInputStream(sw.toString())))
    }
}
