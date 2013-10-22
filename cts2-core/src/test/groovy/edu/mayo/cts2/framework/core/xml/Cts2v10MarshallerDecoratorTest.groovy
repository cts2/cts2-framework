package edu.mayo.cts2.framework.core.xml

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import org.junit.Test

import javax.xml.transform.stream.StreamResult

/**
 */
class Cts2v10MarshallerDecoratorTest {

    @Test
    void testConvert(){
        def marshaller = new Cts2v10MarshallerDecorator(new DelegatingMarshaller())

        def cs = new CodeSystemCatalogEntry(codeSystemName: "test", about: "http://test")

        def result = new StringWriter()
        marshaller.marshal(cs, new StreamResult(result))

        print result
    }
}
