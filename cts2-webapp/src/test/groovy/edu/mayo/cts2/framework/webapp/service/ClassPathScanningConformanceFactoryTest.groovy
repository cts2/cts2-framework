package edu.mayo.cts2.framework.webapp.service;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.core.annotation.AnnotationUtils

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.OpaqueData
import edu.mayo.cts2.framework.model.core.SourceReference
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile
import edu.mayo.cts2.framework.service.profile.FunctionalConformance
import edu.mayo.cts2.framework.service.profile.StructuralConformance
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

class ClassPathScanningConformanceFactoryTest {
	
	def factory = new ClassPathScanningConformanceFactory()
	
	@Test
	void testDoScan(){
		def classes = factory.doScan();
		
		assertTrue classes.size() > 0
	}
	
	@Test
	void testDoScanHaveFunctionalAnnotation(){
		def classes = factory.doScan();
		
		
		classes.each {
			def annotation = AnnotationUtils.findAnnotation(it, FunctionalConformance.class)
			assertNotNull it.name, annotation
		}
	}

	@FunctionalConformance(FunctionalProfile.FP_TEMPORAL)
	class TestCsReadService implements CodeSystemReadService {

		@Override
		public String getServiceName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public OpaqueData getServiceDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getServiceVersion() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SourceReference getServiceProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<DocumentedNamespaceReference> getKnownNamespaceList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CodeSystemCatalogEntry read(NameOrURI identifier,
				ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean exists(NameOrURI identifier,
				ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
