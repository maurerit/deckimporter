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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.maurerit.validation.Validation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.mage.shared.xml.Card;
import org.mage.shared.xml.CardList;
import org.mage.shared.xml.Deck;
import org.mage.shared.xml.MainBoard;
import org.mage.shared.xml.SideBoard;

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
	private static final Pattern CARD_COUNT_PATTERN = Pattern.compile("\\n*(\\d+) ");
	private static final Pattern SET_PATTERN = Pattern.compile(".*Edition=([A-Z0-9]{3})");
	private static final String BASE_CARD_URL = "http://www.mtgvault.com/ViewCard.aspx?CardName={1}";
	private static final int DECK_TAG_COUNT = 3;

	@Override
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "magic.tcgplayer.com");
	}

	@Override
	public void setDeckSaver ( DeckSaver saver ) {
		this.deckSaver = saver;
	}

	@Override
	public DeckSaver getDeckSaver ( ) {
		return this.deckSaver;
	}

	@Override
	public Deck importDeck ( String url ) {
		Deck importedDeck = new Deck();
		importedDeck.setMainBoard(new MainBoard());
		importedDeck.setSideBoard(new SideBoard());
		
		try {
			Document deckInfo = Jsoup.connect(url).get();
			Elements result = deckInfo.select("td.default_9[rowspan=3]");
			
			Validation.begin()
					  .noGreaterThan(result.size(), 1, "result.size")
					  .check()
					  .noLessThan(result.get(0).childNodes().size(), DECK_TAG_COUNT, "result.childNodes.size")
					  .check();
			
			Element cardTd = result.get(0);
			List<Node> possibleDeck = cardTd.childNodes();
			boolean startedSideBoard = false;

			for ( int idx = 0; idx < possibleDeck.size(); idx++ ) {
				List<Node> subList = new ArrayList<Node>(possibleDeck.subList(idx, possibleDeck.size() - 1));
				
				startedSideBoard = startingSideBoard(subList) || startedSideBoard;
				
				if ( startsCard(subList) ) {
					List<Card> cards = parseCard(new ArrayList<Node>(possibleDeck.subList(idx, idx + 3)));
					CardList cardList = importedDeck.getMainBoard();
					
					if ( startedSideBoard ) {
						cardList = importedDeck.getSideBoard();
					}
					
					cardList.getCards().addAll(cards);
					
					idx += 2;
				}
			}
		}
		catch ( IOException e ) {
			throw new ImportException("IOException while parsing deck.", e);
		}
		
		return importedDeck;
	}
	
	/**
	 * Given a {@link List} of {@link Node}'s this method will check if it matches a pattern
	 * of:
	 * 
	 * <ul>
	 *   <li>TextNode</li>
	 *   <li>a Element</li>
	 *   <li>br Element</li>
	 * </ul>
	 * 
	 * @param elements
	 * @return
	 */
	private boolean startsCard ( List<Node> elements ) {
		boolean startsCard = false;
		
		//Firstly check that we have at least 3 child nodes
		if ( elements != null && !elements.isEmpty() && elements.size() >= DECK_TAG_COUNT ) {
			//The top three nodes should be of pattern:
			//	TextNode - this contains the card count
			//	a Element - a link to the card
			//	br Element - the end of this card element
			//TODO: Utilize jQuery Selectors here.
			if ( elements.get(0) instanceof TextNode &&
				 elements.get(1) instanceof Element &&
				 ((Element)elements.get(1)).tag().getName().equalsIgnoreCase("a") &&
				 elements.get(2) instanceof Element &&
				 ((Element)elements.get(2)).tag().getName().equalsIgnoreCase("br") )
			{
				Matcher matcher = CARD_COUNT_PATTERN.matcher(((TextNode)elements.get(0)).getWholeText());
				if ( matcher.matches() ) {
					startsCard = true;
				}
			}
		}
		
		return startsCard;
	}
	
	private boolean startingSideBoard ( List<Node> nodes ) {
		boolean startingSideBoard = false;
		
		if ( nodes != null && nodes.size() > 0 && nodes.get(0) instanceof Element) {
			Elements bElements = ((Element)nodes.get(0)).select(" b");
			if ( !bElements.isEmpty() ) {
				startingSideBoard = ((TextNode)bElements.get(0).childNode(0)).getWholeText().equalsIgnoreCase("SIDEBOARD");
			}
		}
		
		return startingSideBoard;
	}
	
	private List<Card> parseCard ( List<Node> nodes ) throws IOException {
		Validation.begin()
				  .noLessThan(nodes.size(), DECK_TAG_COUNT, "nodes.size")
				  .check();
		List<Card> cards = new ArrayList<Card>();
		
		Matcher matcher = CARD_COUNT_PATTERN.matcher(((TextNode)nodes.get(0)).getWholeText());
		if ( matcher.matches() ) {
			//XXX: Build up a card database for easier querying of card meta data.
			String cardName = ((TextNode)nodes.get(1).childNode(0)).getWholeText();
			Document cardDoc = Jsoup.connect(BASE_CARD_URL.replace("{1}", cardName.replace(" ", "%20"))).get();
			
			//This is the set of the latest version of this card, but its a readable set and its
			//in some HTML somwhat similar to <td class="cardinfo"><b>Edition:</b>" {SET_FULL_NAME}"</td>
			//we'll dig into that structure and trim out the leading " " and just have a {SET_FULL_NAME} which
			//we'll use to find code for in the list of available editions further down the page in subsequent $("td.cardinfo")'s
			//after the 12th index of that select.
			String setNameToFind = ((TextNode)cardDoc.select("td.cardinfo").get(3).childNode(1)).getWholeText().trim();
			String setCode = null;
			
			//Find the latest set by selecting the img tag with a immediate a sibling that contains the set we're looking for			
			//TODO: Might want to offer a way to inspect some preferences for people that like to play with certain images.
			Element setLink = cardDoc.select("td.cardinfo > img ~ a:contains(" + setNameToFind + ")").get(0);
			Matcher setMatcher = SET_PATTERN.matcher(setLink.attr("href"));
			
			if ( setMatcher.matches() &&
				 ((TextNode)setLink.childNode(0)).getWholeText().equalsIgnoreCase(setNameToFind) )
			{
				setCode = setMatcher.group(1);
			}
			
			if ( setCode != null ) {
				Card card = new Card();
				card.setName(cardName);
				card.setExpansionSetCode(setCode);
				card.setCardNumber(MtgVaultDeckImporter.getCardNumber(cardDoc.select("td.cardinfo").get(8).text()));
				
				int cardCount = Integer.parseInt(matcher.group(1));
				for ( int idx = 0; idx < cardCount; idx++ ) {
					cards.add(card);
				}
			}
		}
		
		return cards;
	}
}
