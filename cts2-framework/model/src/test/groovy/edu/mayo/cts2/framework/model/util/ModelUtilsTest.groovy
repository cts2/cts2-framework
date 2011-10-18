package edu.mayo.cts2.framework.model.util;

import org.scalatest.junit.AssertionsForJUnit
import scala.collection.mutable.ListBuffer
import static org.junit.Assert.*
import org.junit.Test
import edu.mayo.cts2.framework.model.entity.NamedIndividualDescription



class ModelUtilsTest {
 
 
  @Test 
  void testToEntityDescription() {
    
    def namedIndividual = new NamedIndividualDescription()
    
    def util =  ModelUtils.toEntityDescription(namedIndividual);
    
    assertEquals(util.getChoiceValue(), namedIndividual)
   
  }  
}