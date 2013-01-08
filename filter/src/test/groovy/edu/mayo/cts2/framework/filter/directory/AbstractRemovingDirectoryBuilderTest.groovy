package edu.mayo.cts2.framework.filter.directory;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.filter.match.AttributeResolver
import edu.mayo.cts2.framework.filter.match.Matcher
import edu.mayo.cts2.framework.filter.match.ResolvableMatchAlgorithmReference
import edu.mayo.cts2.framework.filter.match.ResolvablePropertyReference
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName

class AbstractRemovingDirectoryBuilderTest {

	@Test
	void testGetCandidateText(){

		def builder = new TestAbstractRemovingDirectoryBuilder()
		

		def filter = new ResolvedFilter(matchValue:"test")
		def name = new PropertyReference(referenceTarget:new URIAndEntityName(name:"string", uri:"http://string/"))
		filter.setPropertyReference(name);
		
		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvablePropertyReference modelRef = new ResolvablePropertyReference(attributeResolver)
		modelRef.setReferenceTarget(new URIAndEntityName(name:"string", uri:"http://string/"))
	
		builder.addResolvablePropertyReference(modelRef);

		def result = builder.getCandidateText(modelRef, "one")

		def expected = ["one"]

		assertEquals expected, result
	}

	@Test
	void Test_doRestrict_score_over(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new ResolvedFilter(matchValue:"test")
		def name = new PropertyReference(referenceTarget:new URIAndEntityName(name:"string", uri:"http://string/"))
		filter.setPropertyReference(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvablePropertyReference modelRef = new ResolvablePropertyReference(attributeResolver)
		modelRef.setReferenceTarget(new URIAndEntityName(name:"string", uri:"http://string/"))
	
		builder.addResolvablePropertyReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.6f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithmReference(matchAlgorithm)
		filter.setPropertyReference(modelRef)

		def result = builder.doRestrict(filter, 0.5)

		def expected = ["one", "two"]

		assertEquals expected.size(), result.size()
	}
	
	@Test
	void Test_doRestrict_score_under(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new ResolvedFilter(matchValue:"test")
		def name = new PropertyReference(referenceTarget:new URIAndEntityName(name:"string", uri:"http://string/"))
		filter.setPropertyReference(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvablePropertyReference modelRef = new ResolvablePropertyReference(attributeResolver)
		modelRef.setReferenceTarget(new URIAndEntityName(name:"string", uri:"http://string/"))
	
		builder.addResolvablePropertyReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.4f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithmReference(matchAlgorithm)
		filter.setPropertyReference(modelRef)

		def result = builder.doRestrict(filter, 0.5)

		def expected = []

		assertEquals expected.size(), result.size()
	}
	
	@Test
	void Test_doRestrict_score_equals(){
		def builder = new TestAbstractRemovingDirectoryBuilder()

		def filter = new ResolvedFilter(matchValue:"test")
		def name = new PropertyReference(referenceTarget:new URIAndEntityName(name:"string", uri:"http://string/"))
		filter.setPropertyReference(name);

		def attributeResolver = [resolveAttribute: { obj -> return [obj] }]  as AttributeResolver
		ResolvablePropertyReference modelRef = new ResolvablePropertyReference(attributeResolver)
		modelRef.setReferenceTarget(new URIAndEntityName(name:"string", uri:"http://string/"))
	
		builder.addResolvablePropertyReference(modelRef);
		
		def algorithm = [ matchScore : { matchtext,comparestring -> 0.5f } ] as Matcher
		
		def matchAlgorithm = new ResolvableMatchAlgorithmReference("test","http://uri/test",algorithm)
		
		builder.addSupportedMatchAlgorithmReference(matchAlgorithm)
		
		filter.setMatchAlgorithmReference(matchAlgorithm)
		filter.setPropertyReference(modelRef)

		def result = builder.doRestrict(filter, 0.5)

		def expected = ["one","two"]

		assertEquals expected.size(), result.size()
	}
	
	@Test
	void Test_createDirectoryResult_size(){
		def builder = new TestAbstractRemovingDirectoryBuilder()
		
		def results = builder.createDirectoryResult(["one","two"])
		
		assertEquals 2, results.getEntries().size()
	}
	
	@Test
	void Test_createDirectoryResult_is_not_at_end(){
		def builder = new TestAbstractRemovingDirectoryBuilder()
		builder.addStart(0)
		builder.addMaxToReturn(1)
		
		def results = builder.createDirectoryResult(["one","two"])
		
		assertFalse results.isAtEnd()
	}
	
	@Test
	void Test_createDirectoryResult_is_at_end(){
		def builder = new TestAbstractRemovingDirectoryBuilder()
		builder.addStart(0)
		builder.addMaxToReturn(10)
		
		def results = builder.createDirectoryResult(["one","two"])
		
		assertTrue results.isAtEnd()
	}

    @Test
    void Test_createDirectoryResult_is_at_end_with_start(){
        def builder = new TestAbstractRemovingDirectoryBuilder()
        builder.addStart(2)
        builder.addMaxToReturn(10)

        def results = builder.createDirectoryResult(["one","two","three","four","five"])

        assertTrue results.isAtEnd()
    }
}

class TestAbstractRemovingDirectoryBuilder extends AbstractRemovingDirectoryBuilder<String,String> {

	TestAbstractRemovingDirectoryBuilder() {
		super(Arrays.asList("one", "two"))
	}

	@Override
	List<String> transformResults(List results) {
		return results
	}
}


