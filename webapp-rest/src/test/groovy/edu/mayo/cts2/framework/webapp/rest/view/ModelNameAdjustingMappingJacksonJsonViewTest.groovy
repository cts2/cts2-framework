package edu.mayo.cts2.framework.webapp.rest.view;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.core.constants.ModelAndViewInterface
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.webapp.rest.view.ModelNameAdjustingMappingJacksonJsonView


class ModelNameAdjustingMappingJacksonJsonViewTest {

	@Test
	void testAdjustedMapSize(){
		def view = new ModelNameAdjustingMappingJacksonJsonView()
		def csv = new CodeSystemVersionCatalogEntry()
		
		def resultMap = view.filterModel( [(ModelAndViewInterface.CTS2_MODEL_OBJECT): csv] )
		
		assertEquals 1, resultMap.size()
	}
	
	@Test
	void testAdjustedMapContainsKey(){
		def view = new ModelNameAdjustingMappingJacksonJsonView()
		def csv = new CodeSystemVersionCatalogEntry()
		
		def resultMap = view.filterModel( [(ModelAndViewInterface.CTS2_MODEL_OBJECT): csv] )
		
		assertTrue resultMap.containsKey("CodeSystemVersionCatalogEntry")
	}
	
	@Test
	void testAdjustedMapValue(){
		def view = new ModelNameAdjustingMappingJacksonJsonView()
		def csv = new CodeSystemVersionCatalogEntry()
		
		def resultMap = view.filterModel( [(ModelAndViewInterface.CTS2_MODEL_OBJECT): csv] )
		
		assertEquals csv, resultMap.get("CodeSystemVersionCatalogEntry")
	}
}
