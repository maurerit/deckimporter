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
package net.maurerit.mtg.deck.impl;

import java.io.IOException;
import java.util.List;

import net.maurerit.mtg.ImportException;
import net.maurerit.mtg.ImporterOptions;
import net.maurerit.mtg.ImporterParams;
import net.maurerit.mtg.deck.DeckImporter;
import net.maurerit.mtg.deck.DeckImporterUtils;
import net.maurerit.mtg.deck.DeckSaver;
import net.maurerit.validation.Validation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.mage.shared.xmldb.Deck;

/**
 * Of course one of the most useful sites I've found for tournament information
 * has one of the stupidest HTML formats that one could EVER dream up...  This
 * has not been easy to parse but I think its finally done.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class TcgPlayerDeckImporter implements DeckImporter
{

	private DeckSaver deckSaver;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "magic.tcgplayer.com");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeckSaver ( DeckSaver saver ) {
		this.deckSaver = saver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DeckSaver getDeckSaver ( ) {
		return this.deckSaver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck importDeck ( String url ) {
		Deck importedDeck = new Deck();
		
		try {
			Document deckInfo = Jsoup.connect(url).get();
			Elements result = deckInfo.select("td.default_9[rowspan=3]");
			
			Validation.begin()
					  .noGreaterThan(result.size(), 1, "result.size")
					  .check()
					  .noLessThan(result.get(0).childNodes().size(), DeckImporterUtils.CARD_TAG_COUNT, "result.childNodes.size")
					  .check();
			
			Element cardTd = result.get(0);
			List<Node> possibleDeck = cardTd.childNodes();
			
			DeckImporterUtils.parseCardsFromNodes(possibleDeck, importedDeck);
		}
		catch ( IOException e ) {
			throw new ImportException("IOException while parsing deck.", e);
		}
		
		return importedDeck;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Deck importDeck ( String url, ImporterOptions options ) {
		return importDeck(url);
	}
}
