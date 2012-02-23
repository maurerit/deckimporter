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

import java.io.IOException;
import java.util.List;

import net.maurerit.validation.Validation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.mage.shared.xmldb.Deck;

/**
 * TODO: Javadoc me
 * 
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MagicOnlineDeckImporter implements DeckImporter
{
	public static final String	PLAYER_NAME_OPTION	= "PlayerName";

	private DeckSaver			saver				= new MageFileDeckSaver();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "www.wizards.com");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeckSaver ( DeckSaver saver ) {
		this.saver = saver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DeckSaver getDeckSaver ( ) {
		return saver;
	}

	/**
	 * This method will parse the very first deck found on the page.
	 */
	@Override
	public Deck importDeck ( String url ) {
		Validation.begin()
				  .notEmpty(url, "url")
				  .check();
		Deck importedDeck = null;

		try {
			Element element = Jsoup.connect(url).get().select("div.deck").get(0);
			importedDeck = importDeck(element);
		}
		catch (IOException e) {
			throw new ImportException("IOException while parsing deck.", e);
		}

		return importedDeck;
	}

	/**
	 * This implementation will select the deck who's author matches the option
	 * with key equal {@link #PLAYER_NAME_OPTION}
	 */
	@Override
	public Deck importDeck ( String url, ImporterOptions options ) {
		Validation.begin()
				  .notNull(options, "options")
				  .notEmpty(options.get(PLAYER_NAME_OPTION), "options[" + PLAYER_NAME_OPTION + "]")
				  .check();
		Deck importedDeck = null;
		String deckName = options.get(PLAYER_NAME_OPTION);

		try {
			Elements elements = Jsoup.connect(url).get().select("div.deck");
			Elements players = elements.select("div.main heading");
			Element foundDeck = null;

			// Search through the player Elements collection to find the one with a
			// text value beginning with the passed in options[PLAYER_OPTION_NAME].
			for ( Element element : players ) {
				if (element.childNode(0) instanceof TextNode) {
					String wholeText = ((TextNode) element.childNode(0)).getWholeText();
					String playerName = wholeText.substring(0, wholeText.indexOf(' '));
					if ( deckName.equalsIgnoreCase(playerName) ) {
						foundDeck = element.parents().select("div.deck").first();
						// Don't spend unneccessary time in this loop.
						break;
					}
				}
			}
			
			if ( foundDeck == null ) {
				throw new ImportException("Could not find a deck to parse.");
			}
			
			importedDeck = importDeck(foundDeck);
		}
		catch (IOException e) {
			throw new ImportException("IOException while parsing deck.", e);
		}

		return importedDeck;
	}

	private Deck importDeck ( Element node ) throws IOException {
		Deck importedDeck = new Deck();
		
		// Pass one is a portion of the main deck
		Element deckTds = node.select("table.cardgroup tr:gt(0) > td").first();
		List<Node> possibleDeck = deckTds.childNodes();
		
		DeckImporterUtils.parseCardsFromNodes(possibleDeck, importedDeck);
		
		// Pass two is the rest of the main deck and the sideboard.
		deckTds = node.select("table.cardgroup tr:gt(0) > td").get(1);
		possibleDeck = deckTds.childNodes();
		
		DeckImporterUtils.parseCardsFromNodes(possibleDeck, importedDeck);
		
		return importedDeck;
	}
}
