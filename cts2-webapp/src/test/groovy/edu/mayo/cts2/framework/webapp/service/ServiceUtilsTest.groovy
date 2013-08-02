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


class ServiceUtilsTest {

	@Test
	void testFindAnnotations(){
		def functionalConformance = ServiceUtils.findAnnotations(TestCsReadService.class, FunctionalConformance.class)
		
		def structuralConformance = ServiceUtils.findAnnotations(TestCsReadService.class, StructuralConformance.class)
		
		assertNotNull functionalConformance
		assertNotNull structuralConformance
	}
	
	@Test
	void testAddTemporalAnnotation(){
		def functionalConformance = ServiceUtils.findAnnotations(TestCsReadService.class, FunctionalConformance.class)
		
		assertNotNull functionalConformance
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
