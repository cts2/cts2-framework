package edu.mayo.cts2.framework.filter.directory;

import org.junit.Test;

class AbstractRemovingDirectoryBuilderTest {
	
	@Test
	void testGetCandidateText(){
		/* TODO: Make this pass on the CI groovy environment...
		 
		def builder = new TestAbstractRemovingDirectoryBuilder()
		
		def filter = new FilterComponent(matchValue:"test")
		def name = new URIAndEntityName(name:"string", uri:"http://string/")
		filter.setReferenceTarget(name);
		
		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvableModelAttributeReference modelRef = new ResolvableModelAttributeReference(attributeResolver)
		modelRef.setContent("string")
		modelRef.setUri("http://string/")
		builder.addSupportedModelAttributeReference(modelRef);
		
		def result = builder.getCandidateText(filter, TargetReferenceType.ATTRIBUTE, "one")
		
		assertEquals( ["one"], result )
		*/
	}
	
	/*
	class TestAbstractRemovingDirectoryBuilder extends AbstractRemovingDirectoryBuilder<String,String> {

		public TestAbstractRemovingDirectoryBuilder() {
			super(Arrays.asList("one", "two"));
		}

		@Override
		protected List<String> transformResults(List results) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	*/
}
