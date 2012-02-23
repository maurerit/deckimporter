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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.maurerit.validation.Validation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.mage.shared.xmldb.Card;
import org.mage.shared.xmldb.Deck;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MagicLeagueDeckImporter implements DeckImporter
{
	public static final Pattern COUNT_NAME_PATTERN = Pattern.compile("\\n*(\\d+) (([,' a-zA-Z]+)*(/[,' a-zA-Z]+)*)*");
	
	private DeckSaver saver = new MageFileDeckSaver();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImporterParams importerFor ( ) {
		// http://www.magic-league.com/tournament/info.php?id=75915&view=decks
		return new ImporterParams("http", "www.magic-league.com");
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
	 * {@inheritDoc}
	 */
	@Override
	public Deck importDeck ( String url ) {
		Validation.begin()
		  		  .notEmpty(url, "url")
		  		  .check();
		Deck importedDeck = null;
		
		try {
			Element element = Jsoup.connect(url).get().select("table.deck").get(0);
			importedDeck = importDeck(element);
		}
		catch (IOException e) {
			throw new ImportException("IOException while parsing deck.", e);
		}
		
		return importedDeck;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck importDeck ( String url, ImporterOptions options ) {
		// TODO Auto-generated method stub
		return null;
	}

	private Deck importDeck ( Element node ) {
		Deck parsedDeck = new Deck();
		
		Elements mainDeck = node.select("td.MD");
		Elements sideBoard = node.select("td.SB");
		
		try {
			List<Card> cards = parseCards(mainDeck);
			parsedDeck.getMainBoardCards().addAll(cards);
			cards = parseCards(sideBoard);
			parsedDeck.getSideBoardCards().addAll(cards);
		}
		catch ( IOException e ) {
			throw new ImportException("IOException caught while parsing some cards.", e);
		}
		
		return parsedDeck;
	}
	
	private List<Card> parseCards ( Elements elements ) throws IOException {
		List<Card> cards = new ArrayList<Card>();
		
		for ( Element element : elements ) {
			for ( Node lowestLevel : element.childNodes() ) {
				if ( lowestLevel instanceof TextNode ) {
					Matcher matcher = COUNT_NAME_PATTERN.matcher(((TextNode) lowestLevel).getWholeText().trim());
					
					if ( matcher.matches() ) {
						Card card = CardFactory.findCard(matcher.group(3));
						cards.add(card);
					}
				}
			}
		}
		
		return cards;
	}
}
