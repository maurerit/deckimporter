package net.maurerit.mtg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DeckImporterUtilsTest
{

	@Test
	public void shouldParseSetsProperly ( ) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Pattern setPattern = DeckImporterUtils.MTGVAULT_SET_PATTERN;
		
		String[] tests = { "Edition=M12", "Edition=ISD", "Edition=WWK", "ViewCard.aspx?CardName=Thrun,+the+Last+Troll&Edition=MBS" };
		String[] expecteds = { "M12", "ISD", "WWK", "MBS" };
		
		for ( int idx = 0; idx < tests.length; idx++ ) {
			Matcher matcher = setPattern.matcher(tests[idx]);
			if ( matcher.matches() ) {
				String actual = matcher.group(1);
				
				assertEquals("Parsed set should match " + expecteds[idx], expecteds[idx], actual);
			}
			else {
				fail("Index of tests: " + idx + " with value: " + tests[idx] + " did not match.");
			}
		}
	}
	
	@Test
	public void shouldParseCountsProperly ( ) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Pattern cardCountPattern = DeckImporterUtils.CARD_COUNT_PATTERN;
		
		String[] tests = { "1 ", "\n\n1 ", "12 ", "\n\n12 ", "4 \n\n\t\t", "21 \n\n\t\t" };
		Integer[] expecteds = { 1, 1, 12, 12, 4, 21 };
		
		for ( int idx = 0; idx < tests.length; idx++ ) {
			Matcher matcher = cardCountPattern.matcher(tests[idx]);
			
			if ( matcher.matches() ) {
				Integer parsedInt = Integer.parseInt(matcher.group(1));
				assertEquals("Card count should match " + expecteds[idx], expecteds[idx], parsedInt);
			}
			else {
				fail("Index of tests: " + idx + " with value: " + tests[idx] + " did not match.");
			}
		}
	}

}
