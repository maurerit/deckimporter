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

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.junit.Test;
import org.mage.shared.xmldb.Deck;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MageFileDeckSaverTest {

	@Test
	public void shouldSaveMtgVaultDeck265945ToTmpDir() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, SaveException {
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL("http://www.mtgvault.com/ViewDeck.aspx?DeckID=265945"));
		Deck importedDeck = importer.importDeck("http://www.mtgvault.com/ViewDeck.aspx?DeckID=265945");
		
		File tmpFile = File.createTempFile("test", ".dck");
		tmpFile.deleteOnExit();
		
		DeckSaver saver = new MageFileDeckSaver();
		
		saver.save(tmpFile, importedDeck);
		
		//TODO: BGN Push this out to a utility method.
		BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
		
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ( ( line = reader.readLine() ) != null ) {
			sb.append(line)
			  .append(System.getProperty("line.separator"));
		}
		
		reader.close();
		//TODO: END Push this out to a utility method.
		
		String actual = sb.toString();
		
		assertTrue("Should have '4 [M12:219] Swiftfoot Boots'", actual.contains("4 [M12:219] Swiftfoot Boots"));
		assertTrue("Should have '4 [MBS:14] Mirran Crusader'", actual.contains("4 [MBS:14] Mirran Crusader"));
		assertTrue("Should have '2 [M12:63] Mana Leak'", actual.contains("2 [M12:63] Mana Leak"));
		assertTrue("Should have '4 [ISD:60] Invisible Stalker'", actual.contains("4 [ISD:60] Invisible Stalker"));
		assertTrue("Should have '2 [ISD:227] Inquisitor's Flail'", actual.contains("2 [ISD:227] Inquisitor's Flail"));
		assertTrue("Should have '4 [MBS:8] Hero of Bladehold'", actual.contains("4 [MBS:8] Hero of Bladehold"));
		assertTrue("Should have '4 [M12:227] Glacial Fortress'", actual.contains("4 [M12:227] Glacial Fortress"));
		assertTrue("Should have '3 [NPH:4] Blade Splicer'", actual.contains("3 [NPH:4] Blade Splicer"));
		assertTrue("Should have '2 [M12:39] Sun Titan'", actual.contains("2 [M12:39] Sun Titan"));
		assertTrue("Should have '3 [ISD:244] Moorland Haunt'", actual.contains("3 [ISD:244] Moorland Haunt"));
		assertTrue("Should have '3 [M12:27] Oblivion Ring'", actual.contains("3 [M12:27] Oblivion Ring"));
		assertTrue("Should have '4 [M12:3] Angelic Destiny'", actual.contains("4 [M12:3] Angelic Destiny"));
		assertTrue("Should have '3 [ISD:213] Geist of Saint Traft'", actual.contains("3 [ISD:213] Geist of Saint Traft"));
		assertTrue("Should have '4 [ISD:253] Island'", actual.contains("4 [ISD:253] Island"));
		assertTrue("Should have '13 [ISD:250] Plains'", actual.contains("13 [ISD:250] Plains"));
		assertTrue("Should have '3 [M12:19] Grand Abolisher'", actual.contains("3 [M12:19] Grand Abolisher"));
		assertTrue("Should have 'SB: 3 [SOM:96] Ratchet Bomb'", actual.contains("SB: 3 [SOM:96] Ratchet Bomb"));
		assertTrue("Should have 'SB: 2 [M12:63] Mana Leak'", actual.contains("SB: 2 [M12:63] Mana Leak"));
		assertTrue("Should have 'SB: 4 [M12:40] Timely Reinforcements'", actual.contains("SB: 4 [M12:40] Timely Reinforcements"));
		assertTrue("Should have 'SB: 1 [MBS:138] Sword of Feast and Famine'", actual.contains("SB: 1 [MBS:138] Sword of Feast and Famine"));
		assertTrue("Should have 'SB: 2 [SOM:208] Sword of Body and Mind'", actual.contains("SB: 2 [SOM:208] Sword of Body and Mind"));
		assertTrue("Should have 'SB: 1 [NPH:161] Sword of War and Peace'", actual.contains("SB: 1 [NPH:161] Sword of War and Peace"));
		assertTrue("Should have 'SB: 2 [ROE:21] Gideon Jura'", actual.contains("SB: 2 [ROE:21] Gideon Jura"));
	}

}
