package edu.mayo.cts2.framework.model.util;

import org.scalatest.junit.AssertionsForJUnit
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import edu.mayo.cts2.framework.model.entity.NamedIndividualDescription

class RestModelUtilsTest extends AssertionsForJUnit {
 
 
  @Test def testToEntityDescription() {
    
    var namedIndividual = new NamedIndividualDescription()
    
    var util =  RestModelUtils.toEntityDescription(namedIndividual);
    
    assertEquals(util.getChoiceValue(), namedIndividual)
   
  }  
}