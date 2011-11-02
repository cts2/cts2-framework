package edu.mayo.cts2.framework.filter.directory;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.FilterComponent

class AbstractDirectoryBuilderTest {
	
	@Test
	void testRestrict(){
		def builder = [addStart: {} ] as AbstractDirectoryBuilder
		
		def filter = new ResolvedFilter(matchValue:"test")
		
		builder = builder.restrict([filter])
		
		assertEquals 1, builder.filterComponents.size()
	}
	
	@Test
	void testRestrictNullFilterValue(){
		def builder = [addStart: {} ] as AbstractDirectoryBuilder
		
		def filter = new ResolvedFilter(matchValue:null)
		
		builder = builder.restrict(filter)
		
		assertEquals 0, builder.filterComponents.size()
	}
	
	@Test
	void testRestrictBlankFilterValue(){
		def builder = [addStart: {} ] as AbstractDirectoryBuilder
		
		def filter = new ResolvedFilter(matchValue:" ")
		
		builder = builder.restrict(filter)
		
		assertEquals 0, builder.filterComponents.size()
	}
}
