package edu.mayo.cts2.framework.model.util;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.entity.NamedIndividualDescription
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice



class ModelUtilsTest {
 
 
  @Test 
  void testToEntityDescription() {
    
    def namedIndividual = new NamedIndividualDescription()
    
    def util =  ModelUtils.toEntityDescription(namedIndividual);
    
    assertEquals(util.getChoiceValue(), namedIndividual)
   
  }  
 
  @Test
  void testNameOrURIFromEitherWithName() {
	 def n_u = ModelUtils.nameOrUriFromEither("some_name")
	 
	 assertNull n_u.getUri()
	 assertEquals "some_name", n_u.getName()
  }
  
  @Test
  void testNameOrURIFromEitherWithUri() {
	 def n_u = ModelUtils.nameOrUriFromEither("urn:oid:1.2.3.4.5")
	 
	 assertNull n_u.getName()
	 assertEquals "urn:oid:1.2.3.4.5", n_u.getUri()
  }

}