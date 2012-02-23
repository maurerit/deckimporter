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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
public class MagicLeagueDeckImporterTest
{

	@Test
	public void shouldImportCanabiestsDeck ( ) throws MalformedURLException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String actual = null;
		String url = "http://www.magic-league.com/tournament/info.php?id=75915&view=decks";
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL(url));
		
		Deck importedDeck = importer.importDeck(url);
		actual = DeckImporterTestUtils.invokeDeckSaversFormatMethod(new MageFileDeckSaver(), importedDeck);
		
		assertNotNull(importedDeck);
		
		assertTrue("Should have '1 [SOM:225] Copperline Gorge'", actual.contains("1 [SOM:225] Copperline Gorge"));
	}

}
