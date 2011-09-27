package edu.mayo.cts2.framework.filter.match;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.mayo.cts2.framework.filter.match.WildcardMatcher;

public class WildcardMatcherTest {
	
	private WildcardMatcher matcher;

	@Before
	public void setUpMatcher(){
		this.matcher = new WildcardMatcher();
	}
	
	@Test
	public void noMatch(){
		float score = this.matcher.matchScore("some* text", "other stuff");
		
 		assertEquals(0f,score, 0f);
	}
	
	@Test
	public void exactMatch(){
		float score = this.matcher.matchScore("some", "some");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void exactMatchMultiWord(){
		float score = this.matcher.matchScore("some text", "some text");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void noWildcard(){
		float score = this.matcher.matchScore("string", "some text in a string");
		
 		assertEquals(0f,score, 0f);
	}
	
	@Test
	public void withLeadingWildcard(){
		float score = this.matcher.matchScore("*string", "some text in a string");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void withTrailingWildcard(){
		
		float score = this.matcher.matchScore("some*", "some text in a string");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void withTwoTrailingWildcards(){
		
		float score = this.matcher.matchScore("some* str*", "some text in a string");
		
 		assertEquals(1f,score, 0f);
	}
}
