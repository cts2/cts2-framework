package edu.mayo.cts2.framework.core.xml;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

public class ModelXmlPropertiesHandler {

    public static final String CASTOR_PROPS = "castor.properties";
    public static final String CASTORBUILDER_PROPS = "castorbuilder.properties";
	public static final String NAMESPACE_LOCATION_PROPS = "namespaceLocations.properties";
	public static final String NAMESPACE_MAPPINGS_PROPS = "namespaceMappings.properties";

    private String castorProps = CASTOR_PROPS;
    private String castorbuilderProps = CASTORBUILDER_PROPS;
    private String namespaceLocationProps = NAMESPACE_LOCATION_PROPS;
    private String namespaceMappingsProps = NAMESPACE_MAPPINGS_PROPS;

    public ModelXmlPropertiesHandler() {
        super();
    }

    public ModelXmlPropertiesHandler(
            String castorProps,
            String castorbuilderProps,
            String namespaceLocationProps,
            String namespaceMappingsProps) {
        super();

        if(StringUtils.isNotBlank(castorProps)) {
            this.castorProps = castorProps;
        }
        if(StringUtils.isNotBlank(castorbuilderProps)) {
            this.castorbuilderProps = castorbuilderProps;
        }
        if(StringUtils.isNotBlank(namespaceLocationProps)) {
            this.namespaceLocationProps = namespaceLocationProps;
        }
        if(StringUtils.isNotBlank(namespaceMappingsProps)) {
            this.namespaceMappingsProps = namespaceMappingsProps;
        }
    }

    public Properties getCastorProperties(){
        return this.getProperties(castorProps);
    }

    public Properties getCastorBuilderProperties(){
		return this.getProperties(castorbuilderProps);
	}
	
	public Properties getNamespaceLocationProperties(){
		return this.getProperties(namespaceLocationProps);
	}
	
	public Properties getNamespaceMappingProperties(){
		return this.getProperties(namespaceMappingsProps);
	}
	
	protected Properties getProperties(String path){
		Properties props = new Properties();
		try {
			props.load(new ClassPathResource(path).getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return props;
	}
}
