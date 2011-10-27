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
  void testGetChangeableElementGroup() {
	  def crc = new ChangeableResourceChoice()
	  
	  def group = new ChangeableElementGroup()
	  crc.setCodeSystem(new CodeSystemCatalogEntry(changeableElementGroup: group))
	  
	  
	  assertEquals group, ModelUtils.getChangeableElementGroup(crc)
  }
}