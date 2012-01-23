package edu.mayo.cts2.framework.filter.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.mayo.cts2.framework.filter.match.SoundexMatcher;

public class SoundexMatcherTest {
	
	private SoundexMatcher matcher;

	@Before
	public void setUpMatcher(){
		this.matcher = new SoundexMatcher();
	}
	
	@Test
	public void testMatchSimilarSound(){
		float score = this.matcher.matchScore("heart", "hart");
		
 		assertEquals(1,score, 0f);
	}
	
	@Test
	public void testMatchNonSimilarSound(){
		float score = this.matcher.matchScore("bike", "ear");
		
 		assertEquals(0,score, 0f);
	}
	
	@Test
	public void testMatchSimilarSoundMultiWord(){
		float score = this.matcher.matchScore("hart atak", "heart attack");
		
 		assertEquals(1,score, 0f);
	}
	
	@Test
	public void testMatchDifferentNumberOfWords(){
		float score = this.matcher.matchScore("hart atak", "patient with heart attack and arrest with coma");
		
 		assertEquals(0.25f,score, 0f);
	}
	
	@Test
	public void testMatchDifferentNumberOfWordsHalfMatch(){
		float score = this.matcher.matchScore("feevr", "fever");
		
 		assertTrue(0.5f < score);
	}
}
