/**
 *  Copyright 2008 - 2011
 *            Matthew L. Maurer maurer.it@gmail.com
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.maurerit.mtg;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.mage.shared.xml.Deck;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class TcgPlayerDeckImporterTest
{

	@Test
	public void shouldGetTcgPlayerDeckImporter ( ) throws MalformedURLException {
		String url = "http://magic.tcgplayer.com";
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL(url));
		
		assertTrue("DeckImporter should be an instance of TcgPlayerDeckImporter", importer instanceof TcgPlayerDeckImporter);
	}
	
	@Test
	public void shouldParseSetsProperly ( ) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = TcgPlayerDeckImporter.class.getDeclaredField("SET_PATTERN");
		field.setAccessible(true);
		Pattern setPattern = (Pattern) field.get(new TcgPlayerDeckImporter());
		
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
		Field field = TcgPlayerDeckImporter.class.getDeclaredField("CARD_COUNT_PATTERN");
		field.setAccessible(true);
		Pattern cardCountPattern = (Pattern) field.get(new TcgPlayerDeckImporter());
		
		String[] tests = { "1 ", "\n\n1 ", "12 ", "\n\n12 " };
		Integer[] expecteds = { 1, 1, 12, 12 };
		
		for ( int idx = 0; idx < tests.length; idx++ ) {
			Matcher matcher = cardCountPattern.matcher(tests[idx]);
			
			if ( matcher.matches() ) {
				Integer parsedInt = Integer.parseInt(matcher.group(1));
				assertEquals("Cound count should match " + expecteds[idx], expecteds[idx], parsedInt);
			}
			else {
				fail("Index of tests: " + idx + " with value: " + tests[idx] + " did not match.");
			}
		}
	}
	
	@Test
	public void shouldImportDeck943886 ( ) throws MalformedURLException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String url = "http://magic.tcgplayer.com/db/deck.asp?deck_id=943886";
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL(url));
		
		Deck deck = importer.importDeck(url);
		String actual = DeckImporterUtils.invokeDeckSaversFormatMethod(new MageFileDeckSaver(), deck);
		
		assertTrue("Should have '1 [M12:165] Birds of Paradise'", actual.contains("1 [M12:165] Birds of Paradise"));
		assertTrue("Should have '4 [M12:147] Inferno Titan'", actual.contains("4 [M12:147] Inferno Titan"));
		assertTrue("Should have '4 [M12:188] Primeval Titan'", actual.contains("4 [M12:188] Primeval Titan"));
		assertTrue("Should have '4 [M12:217] Solemn Simulacrum'", actual.contains("4 [M12:217] Solemn Simulacrum"));
		assertTrue("Should have '1 [MBS:92] Thrun, the Last Troll'", actual.contains("1 [MBS:92] Thrun, the Last Troll"));
		assertTrue("Should have '2 [ISD:140] Devil's Play'", actual.contains("2 [ISD:140] Devil's Play"));
		assertTrue("Should have '4 [SOM:91] Galvanic Blast'", actual.contains("4 [SOM:91] Galvanic Blast"));
		assertTrue("Should have '2 [MBS:81] Green Sun's Zenith'", actual.contains("2 [MBS:81] Green Sun's Zenith"));
		assertTrue("Should have '4 [M12:190] Rampant Growth'", actual.contains("4 [M12:190] Rampant Growth"));
		assertTrue("Should have '1 [M12:154] Shock'", actual.contains("1 [M12:154] Shock"));
		assertTrue("Should have '3 [MBS:75] Slagstorm'", actual.contains("3 [MBS:75] Slagstorm"));
		assertTrue("Should have '4 [MBS:134] Sphere of the Suns'", actual.contains("4 [MBS:134] Sphere of the Suns"));
		assertTrue("Should have '4 [SOM:225] Copperline Gorge'", actual.contains("4 [SOM:225] Copperline Gorge"));
		assertTrue("Should have '5 [ISD:262] Forest'", actual.contains("5 [ISD:262] Forest"));
		assertTrue("Should have '4 [MBS:145] Inkmoth Nexus'", actual.contains("4 [MBS:145] Inkmoth Nexus"));
		assertTrue("Should have '3 [ISD:243] Kessig Wolf Run'", actual.contains("3 [ISD:243] Kessig Wolf Run"));
		assertTrue("Should have '6 [ISD:259] Mountain'", actual.contains("6 [ISD:259] Mountain"));
		assertTrue("Should have '4 [M12:228] Rootbound Crag'", actual.contains("4 [M12:228] Rootbound Crag"));
		assertTrue("Should have 'SB: 2 [ISD:127] Ancient Grudge'", actual.contains("SB: 2 [ISD:127] Ancient Grudge"));
		assertTrue("Should have 'SB: 4 [M12:164] Autumn's Veil'", actual.contains("SB: 4 [M12:164] Autumn's Veil"));
		assertTrue("Should have 'SB: 1 [NPH:103] Beast Within'", actual.contains("SB: 1 [NPH:103] Beast Within"));
		assertTrue("Should have 'SB: 1 [MBS:75] Slagstorm'", actual.contains("SB: 1 [MBS:75] Slagstorm"));
		assertTrue("Should have 'SB: 2 [MBS:138] Sword of Feast and Famine'", actual.contains("SB: 2 [MBS:138] Sword of Feast and Famine"));
		assertTrue("Should have 'SB: 2 [MBS:92] Thrun, the Last Troll'", actual.contains("SB: 2 [MBS:92] Thrun, the Last Troll"));
		assertTrue("Should have 'SB: 2 [ISD:207] Tree of Redemption'", actual.contains("SB: 2 [ISD:207] Tree of Redemption"));
		assertTrue("Should have 'SB: 1 [MBS:94] Viridian Corrupter'", actual.contains("SB: 1 [MBS:94] Viridian Corrupter"));
	}

}
