package edu.mayo.cts2.framework.filter.directory;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.filter.match.AttributeResolver
import edu.mayo.cts2.framework.filter.match.Matcher
import edu.mayo.cts2.framework.filter.match.ResolvableMatchAlgorithmReference
import edu.mayo.cts2.framework.filter.match.ResolvableModelAttributeReference
import edu.mayo.cts2.framework.model.core.FilterComponent
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType

class AbstractRemovingDirectoryBuilderTest {

	@Test
	void testGetCandidateText(){

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

		def expected = ["one"]

		assertEquals expected, result
	}

	@Test
	void Test_doRestrict_score_over(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new FilterComponent(matchValue:"test")
		def name = new URIAndEntityName(name:"string", uri:"http://string/")
		filter.setReferenceTarget(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvableModelAttributeReference modelRef = new ResolvableModelAttributeReference(attributeResolver)
		modelRef.setContent("string")
		modelRef.setUri("http://string/")
		builder.addSupportedModelAttributeReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.6f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithm(matchAlgorithm)
		filter.setReferenceType(TargetReferenceType.ATTRIBUTE)

		def result = builder.doRestrict(filter, 0.5)

		def expected = ["one", "two"]

		assertEquals expected.size(), result.size()
	}
	
	@Test
	void Test_doRestrict_score_under(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new FilterComponent(matchValue:"test")
		def name = new URIAndEntityName(name:"string", uri:"http://string/")
		filter.setReferenceTarget(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvableModelAttributeReference modelRef = new ResolvableModelAttributeReference(attributeResolver)
		modelRef.setContent("string")
		modelRef.setUri("http://string/")
		builder.addSupportedModelAttributeReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.4f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithm(matchAlgorithm)
		filter.setReferenceType(TargetReferenceType.ATTRIBUTE)

		def result = builder.doRestrict(filter, 0.5)

		def expected = []

		assertEquals expected.size(), result.size()
	}
	
	@Test
	void Test_doRestrict_score_equals(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new FilterComponent(matchValue:"test")
		def name = new URIAndEntityName(name:"string", uri:"http://string/")
		filter.setReferenceTarget(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvableModelAttributeReference modelRef = new ResolvableModelAttributeReference(attributeResolver)
		modelRef.setContent("string")
		modelRef.setUri("http://string/")
		builder.addSupportedModelAttributeReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.5f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithm(matchAlgorithm)
		filter.setReferenceType(TargetReferenceType.ATTRIBUTE)

		def result = builder.doRestrict(filter, 0.5)

		def expected = ["one","two"]

		assertEquals expected.size(), result.size()
	}
}

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


