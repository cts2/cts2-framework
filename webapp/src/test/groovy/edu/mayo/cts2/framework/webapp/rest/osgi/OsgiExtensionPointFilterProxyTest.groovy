package edu.mayo.cts2.framework.webapp.rest.osgi;

import static org.junit.Assert.*

import org.junit.Test
import org.osgi.framework.ServiceReference

class OsgiExtensionPointFilterProxyTest {
	
	@Test
	void TestFilterOrder(){
		def first = [
			getProperty : { "FIRST" }
		] as ServiceReference
		def last = [
			getProperty : { "LAST" }
		] as ServiceReference
		def none = [
			getProperty : { null }
		] as ServiceReference
	
		def arr = [none,last,first] as ServiceReference[]	
		Arrays.sort(arr,
			OsgiExtensionPointFilterProxy.SERVICE_REF_COMPARATOR)
		
		assertEquals arr[0].getProperty("ORDER"), "FIRST"
		assertNull arr[1].getProperty("ORDER")
		assertEquals arr[2].getProperty("ORDER"), "LAST"
		
	}

	
}
