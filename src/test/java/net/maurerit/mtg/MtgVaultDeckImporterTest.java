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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import mage.tracker.domain.Card;
import mage.tracker.domain.CardEdition;
import mage.tracker.domain.Expansion;
import net.maurerit.mtg.deck.Deck;
import net.maurerit.mtg.deck.DeckImporter;
import net.maurerit.mtg.deck.DeckImporterFactory;
import net.maurerit.mtg.deck.impl.MageFileDeckSaver;
import net.maurerit.mtg.deck.impl.MtgVaultDeckImporter;

import org.junit.Test;

/**
 * Runs tests to test the {@link MtgVaultDeckImporter}.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MtgVaultDeckImporterTest
{
	@Test
	public void shouldFormat2MainBoardAnd2SideBoardCard ( ) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String expected = "NAME: DeckName" + System.getProperty("line.separator") +
				          "1 [ZEN:249] Forest" + System.getProperty("line.separator") +
				          "1 [SOM:116] Copperhorn Scout" + System.getProperty("line.separator") +
				          "SB: 1 [TMP:223] Blah" + System.getProperty("line.separator") +
				          "SB: 1 [SOM:116] Copperhorn Scout" + System.getProperty("line.separator") ;
		String actual = null;
		
		Deck deck = new Deck();
		
		deck.setName("DeckName");
		
		CardEdition forest = new CardEdition();
		Card innerForest = new Card();
		forest.setCardNumber("249");
		forest.setCard(innerForest);
		innerForest.setName("Forest");
		Expansion expansion = new Expansion();
		expansion.setCode("ZEN");
		forest.setExpansion(expansion);
		deck.getMainBoardCards().add(forest);
		
		CardEdition copperhorn = new CardEdition();
		Card innerCard = new Card();
		copperhorn.setCardNumber("116");
		copperhorn.setCard(innerCard);
		innerCard.setName("Copperhorn Scout");
		expansion = new Expansion();
		expansion.setCode("SOM");
		copperhorn.setExpansion(expansion);
		deck.getMainBoardCards().add(copperhorn);
		deck.getSideBoardCards().add(copperhorn);
		
		CardEdition blah = new CardEdition();
		Card innerBlah = new Card();
		blah.setCardNumber("223");
		blah.setCard(innerBlah);
		innerBlah.setName("Blah");
		expansion = new Expansion();
		expansion.setCode("TMP");
		blah.setExpansion(expansion);
		deck.getSideBoardCards().add(blah);
		
		MageFileDeckSaver saver = new MageFileDeckSaver();
		actual = DeckImporterTestUtils.invokeDeckSaversFormatMethod(saver, deck);
		
		assertEquals(expected, actual);
	}

	@Test
	public void shouldImportMtgVaultDeckId265945 ( ) throws MalformedURLException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String actual = null;
		
		DeckImporter importer = DeckImporterFactory.createDeckImporter(new URL("http://www.mtgvault.com/ViewDeck.aspx?DeckID=265945"));
		Deck importedDeck = importer.importDeck("http://www.mtgvault.com/ViewDeck.aspx?DeckID=265945");
		actual = DeckImporterTestUtils.invokeDeckSaversFormatMethod(new MageFileDeckSaver(), importedDeck);
		
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
