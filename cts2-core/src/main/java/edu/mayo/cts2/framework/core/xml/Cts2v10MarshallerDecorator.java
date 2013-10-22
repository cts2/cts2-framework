package edu.mayo.cts2.framework.core.xml;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

/**
 */
public class Cts2v10MarshallerDecorator implements Cts2Marshaller {

    private static final String CTS211to10_XSL = "CTS211to10.xsl";

    private Cts2Marshaller delegate;

    public Cts2v10MarshallerDecorator(Cts2Marshaller delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    public Properties getCastorBuilderProperties() {
        return this.delegate.getCastorBuilderProperties();
    }

    @Override
    public Properties getNamespaceLocationProperties() {
        return this.delegate.getNamespaceLocationProperties();
    }

    @Override
    public Properties getNamespaceMappingProperties() {
        return this.delegate.getNamespaceMappingProperties();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return this.delegate.supports(clazz);
    }

    @Override
    public Object unmarshal(Source source) throws IOException, XmlMappingException {
        throw new UnsupportedOperationException("Cannot Unmarshal CTS2 1.0 XML yet.");
    }

    @Override
    public void marshal(Object graph, Result result) throws IOException, XmlMappingException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new URIResolver(){

            @Override
            public Source resolve(String href, String base)
                    throws TransformerException {
                try {
                    return new StreamSource(new ClassPathResource(href).getInputStream());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        });

        Transformer transformer ;
        try {
            transformer = transformerFactory.newTransformer(
                new StreamSource(new ClassPathResource(CTS211to10_XSL).getInputStream()));
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException(e);
        }

        StringWriter writer = new StringWriter();
        StreamResult superResult = new StreamResult(writer);

        this.delegate.marshal(graph, superResult);

        try {
            transformer.transform(new StreamSource(IOUtils.toInputStream(writer.toString(), "UTF-8")), result);
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }
}
