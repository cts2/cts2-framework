/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.core.xml;

import javax.xml.transform.dom.DOMSource;

import org.exolab.castor.xml.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The Class PatchedCastorMarshaller.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PatchedCastorMarshaller extends CastorMarshaller {
	
	private static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";

	/* (non-Javadoc)
	 * @see org.springframework.oxm.castor.CastorMarshaller#customizeMarshaller(org.exolab.castor.xml.Marshaller)
	 */
	@Override
	protected void customizeMarshaller(Marshaller marshaller) {
		super.customizeMarshaller(marshaller);
		marshaller.setInternalContext(marshaller.getInternalContext());
	}
	
    /* (non-Javadoc)
     * @see org.springframework.oxm.support.AbstractMarshaller#unmarshalDomSource(javax.xml.transform.dom.DOMSource)
     *
     * Fix for: http://jira.codehaus.org/browse/CASTOR-2903
     */
    protected Object unmarshalDomSource(DOMSource domSource) throws XmlMappingException {
        
        Node node = domSource.getNode();
        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element)node;
            
            Node parent = node.getParentNode();
            while (parent != null) {
                NamedNodeMap atts = parent.getAttributes();
                if (atts != null) {
                    for (int i=0, j=atts.getLength(); i<j; i++) {
                        
                        Attr att = (Attr) atts.item(i);
                        if (XMLNS_NS.equals(att.getNamespaceURI())) {
                            String name = att.getName();
                            String value = att.getValue();     
                            if (!element.hasAttributeNS(XMLNS_NS, name)) {
                                element.setAttributeNS(XMLNS_NS, name, value);
                            }
                        }
                        
                    }
                }
                parent = parent.getParentNode();
            }
        }
        
        return super.unmarshalDomSource(domSource);
    }
}