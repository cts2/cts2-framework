package edu.mayo.cts2.framework.model.entity;

import org.junit.Test
import static org.junit.Assert.*

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup

public class EntityDescriptionTest {

	@Test
	public void getChangeDescription(){
		
		def ed = new EntityDescription(
			namedEntity:new NamedEntityDescription(
				changeableElementGroup:new ChangeableElementGroup()))
		
	
		assertNotNull ed.getChangeableElementGroup()
	}
}
