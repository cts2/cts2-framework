package edu.mayo.cts2.framework.model.exception;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.service.exception.QueryTimeout
import edu.mayo.cts2.framework.model.service.exception.UnsupportedMatchAlgorithm

class ExceptionFactoryTest {
	
	@Test
	void test_createUnsupportedMatchAlgorithm(){
		def nm1 = new NameAndMeaningReference(content:"test1", uri:"http://test1.org")
		def nm2 = new NameAndMeaningReference(content:"test2", uri:"http://test2.org")
		def ex = ExceptionFactory.createUnsupportedMatchAlgorithm("testNone", [nm1,nm2])
		
		assert ex instanceof UnsupportedMatchAlgorithm
	}
	
	@Test
	void test_createTimeoutException(){
		
		def ex = ExceptionFactory.createTimeoutException()
		
		assert ex instanceof QueryTimeout
	}


}
