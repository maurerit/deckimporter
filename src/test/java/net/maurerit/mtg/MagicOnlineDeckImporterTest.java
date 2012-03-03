/**
 *  Copyright 2008 - 2012
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.mage.shared.xmldb.Deck;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MagicOnlineDeckImporterTest
{
	@Test
	public void shouldImportBejatosDeck ( ) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
		String actual = null;
		
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365"));
		ImporterOptions options = new ImporterOptions(DeckImporter.PLAYER_NAME_OPTION, "bejato");
		Deck importedDeck = importer.importDeck("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365", options);
		actual = DeckImporterTestUtils.invokeDeckSaversFormatMethod(new MageFileDeckSaver(), importedDeck);
		
		assertTrue("Should have '4 [M12:126] Chandra's Phoenix'", actual.contains("4 [M12:126] Chandra's Phoenix"));
		assertTrue("Should have '4 [M12:156] Stormblood Berserker'", actual.contains("4 [M12:156] Stormblood Berserker"));
		assertTrue("Should have '4 [NPH:153] Shrine of Burning Rage'", actual.contains("4 [NPH:153] Shrine of Burning Rage"));
		assertTrue("Should have '4 [NPH:100] Volt Charge'", actual.contains("4 [NPH:100] Volt Charge"));
		assertTrue("Should have '3 [SOM:81] Arc Trail'", actual.contains("3 [SOM:81] Arc Trail"));
		assertTrue("Should have '3 [SOM:91] Galvanic Blast'", actual.contains("3 [SOM:91] Galvanic Blast"));
		assertTrue("Should have '2 [M12:145] Grim Lavamancer'", actual.contains("2 [M12:145] Grim Lavamancer"));
		assertTrue("Should have '4 [ISD:164] Stromkirk Noble'", actual.contains("4 [ISD:164] Stromkirk Noble"));
		assertTrue("Should have '3 [NPH:86] Gut Shot'", actual.contains("3 [NPH:86] Gut Shot"));
		assertTrue("Should have '4 [ISD:159] Reckless Waif'", actual.contains("4 [ISD:159] Reckless Waif"));
		assertTrue("Should have '2 [ISD:132] Brimstone Volley'", actual.contains("2 [ISD:132] Brimstone Volley"));
		assertTrue("Should have '2 [M12:139] Goblin Fireslinger'", actual.contains("2 [M12:139] Goblin Fireslinger"));
		assertTrue("Should have '21 [ISD:259] Mountain'", actual.contains("21 [ISD:259] Mountain"));
		assertTrue("Should have 'SB: 3 [ISD:166] Traitorous Blood'", actual.contains("SB: 3 [ISD:166] Traitorous Blood"));
		assertTrue("Should have 'SB: 3 [NPH:101] Vulshok Refugee'", actual.contains("SB: 3 [NPH:101] Vulshok Refugee"));
		assertTrue("Should have 'SB: 2 [M12:150] Manabarbs'", actual.contains("SB: 2 [M12:150] Manabarbs"));
		assertTrue("Should have 'SB: 3 [M12:151] Manic Vandal'", actual.contains("SB: 3 [M12:151] Manic Vandal"));
		assertTrue("Should have 'SB: 3 [M12:206] Dragon's Claw'", actual.contains("SB: 3 [M12:206] Dragon's Claw"));
		assertTrue("Should have 'SB: 1 [SOM:81] Arc Trail'", actual.contains("SB: 1 [SOM:81] Arc Trail"));
	}
	
	@Test
	public void shouldImportSlivernewsDeck ( ) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
		String actual = null;
		
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365"));
		ImporterOptions options = new ImporterOptions(DeckImporter.PLAYER_NAME_OPTION, "slivernew");
		Deck importedDeck = importer.importDeck("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365", options);
		actual = DeckImporterTestUtils.invokeDeckSaversFormatMethod(new MageFileDeckSaver(), importedDeck);
		
		assertTrue("Should have '4 [NPH:48] Vapor Snag'", actual.contains("4 [NPH:48] Vapor Snag"));
		assertTrue("Should have '2 [ISD:231] Runechanter's Pike'", actual.contains("2 [ISD:231] Runechanter's Pike"));
		assertTrue("Should have '3 [ISD:244] Moorland Haunt'", actual.contains("3 [ISD:244] Moorland Haunt"));
		assertTrue("Should have '1 [ISD:250] Plains'", actual.contains("1 [ISD:250] Plains"));
		assertTrue("Should have '9 [ISD:253] Island'", actual.contains("9 [ISD:253] Island"));
		assertTrue("Should have '1 [NPH:43] Psychic Barrier'", actual.contains("1 [NPH:43] Psychic Barrier"));
		assertTrue("Should have '4 [ISD:51] Delver of Secrets'", actual.contains("4 [ISD:51] Delver of Secrets"));
		assertTrue("Should have '4 [M12:227] Glacial Fortress'", actual.contains("4 [M12:227] Glacial Fortress"));
		assertTrue("Should have '3 [M12:70] Phantasmal Bear'", actual.contains("3 [M12:70] Phantasmal Bear"));
		assertTrue("Should have '4 [NPH:86] Gut Shot'", actual.contains("4 [NPH:86] Gut Shot"));
		assertTrue("Should have '3 [ISD:22] Midnight Haunting'", actual.contains("3 [ISD:22] Midnight Haunting"));
		assertTrue("Should have '4 [M12:73] Ponder'", actual.contains("4 [M12:73] Ponder"));
		assertTrue("Should have '4 [ISD:78] Snapcaster Mage'", actual.contains("4 [ISD:78] Snapcaster Mage"));
		assertTrue("Should have '4 [NPH:35] Gitaxian Probe'", actual.contains("4 [NPH:35] Gitaxian Probe"));
		assertTrue("Should have '4 [SOM:229] Seachrome Coast'", actual.contains("4 [SOM:229] Seachrome Coast"));
		assertTrue("Should have '4 [M12:63] Mana Leak'", actual.contains("4 [M12:63] Mana Leak"));
		assertTrue("Should have '2 [ISD:213] Geist of Saint Traft'", actual.contains("2 [ISD:213] Geist of Saint Traft"));
		assertTrue("Should have 'SB: 1 [M12:72] Phantasmal Image'", actual.contains("SB: 1 [M12:72] Phantasmal Image"));
		assertTrue("Should have 'SB: 2 [M12:27] Oblivion Ring'", actual.contains("SB: 2 [M12:27] Oblivion Ring"));
		assertTrue("Should have 'SB: 2 [M12:11] Celestial Purge'", actual.contains("SB: 2 [M12:11] Celestial Purge"));
		assertTrue("Should have 'SB: 3 [M12:40] Timely Reinforcements'", actual.contains("SB: 3 [M12:40] Timely Reinforcements"));
		assertTrue("Should have 'SB: 1 [M12:69] Negate'", actual.contains("SB: 1 [M12:69] Negate"));
		assertTrue("Should have 'SB: 1 [ISD:53] Dissipate'", actual.contains("SB: 1 [ISD:53] Dissipate"));
		assertTrue("Should have 'SB: 3 [NPH:38] Mental Misstep'", actual.contains("SB: 3 [NPH:38] Mental Misstep"));
		assertTrue("Should have 'SB: 1 [NPH:161] Sword of War and Peace'", actual.contains("SB: 1 [NPH:161] Sword of War and Peace"));
		assertTrue("Should have 'SB: 1 [ISD:213] Geist of Saint Traft'", actual.contains("SB: 1 [ISD:213] Geist of Saint Traft"));
	}
	
	@Test
	public void shouldFailToImportNoOnesDeck ( ) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365"));
		ImporterOptions options = new ImporterOptions(DeckImporter.PLAYER_NAME_OPTION, "noone");
		
		try {
			importer.importDeck("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/3193365", options);
			fail ( "Should have thrown an ImportException due to noone's deck not existing.");
		}
		catch ( ImportException e ) { /* Good Test Case */ }
	}
}
