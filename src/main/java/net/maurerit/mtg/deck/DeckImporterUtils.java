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
package net.maurerit.mtg.deck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mage.tracker.domain.CardEdition;
import net.maurerit.mtg.card.CardFactory;
import net.maurerit.validation.Validation;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * Methods in this class are capable of parsing decks out of magic.tcgplayer.com
 * and magic online tournament formats and other possible websites that follow a
 * format that can be interpreted from the following node pattern:
 * 
 * <ul>
 *   <li>TextNode - Represents the card count and is parsed from a pattern like: \n*(\d+)\W*</li>
 *   <li>a Element - The text of this node will be used as the card name and the latest print will be used.</li>
 *   <li>br Element - Marks the end of the card.</li>
 * </ul>
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public final class DeckImporterUtils
{

	public static final Pattern CARD_COUNT_PATTERN = Pattern.compile("\\n*(\\d+)\\W*");
	public static final int CARD_TAG_COUNT = 3;
	
	private DeckImporterUtils ( ) { }
	
	/**
	 * Iterates through the <code>possibleDeck</code> parameter and attempts to parse out cards
	 * and find their latest set.  This method is capable of parsing cards when the <code>possibleDeck</code>
	 * {@link Node node list} follows the following format:
	 * 
	 * <ul>
	 *   <li>TextNode - Represents the card count and is parsed from a pattern like: \n*(\d+)\W*</li>
	 *   <li>a Element - The text of this node will be used as the card name and the latest print will be used.</li>
	 *   <li>br Element - Marks the end of the card.</li>
	 * </ul>
	 * 
	 * @param possibleDeck
	 * @param importedDeck
	 * @throws IOException
	 */
	public static void parseCardsFromNodes ( List<Node> possibleDeck, Deck importedDeck ) throws IOException {
		boolean startedSideBoard = false;

		for ( int idx = 0; idx < possibleDeck.size(); idx++ ) {
			List<Node> subList = new ArrayList<Node>(possibleDeck.subList(idx, possibleDeck.size() - 1));
			
			startedSideBoard = startingSideBoard(subList) || startedSideBoard;
			
			if ( startsCard(subList) ) {
				List<CardEdition> cards = parseCard(new ArrayList<Node>(possibleDeck.subList(idx, idx + CARD_TAG_COUNT)));
				List<CardEdition> cardList = importedDeck.getMainBoardCards();
				
				if ( startedSideBoard ) {
					cardList = importedDeck.getSideBoardCards();
				}
				
				cardList.addAll(cards);
				
				idx += 2;
			}
		}
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
	public static boolean startsCard ( List<Node> elements ) {
		boolean startsCard = false;
		
		//Firstly check that we have at least 3 child nodes
		if ( elements != null && !elements.isEmpty() && elements.size() >= CARD_TAG_COUNT ) {
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

	/**
	 * Give the set of {@link Node}'s that should represent a card, this method will
	 * parse out the card and then also connect to a predetermined URL which will help
	 * find us the latest set easier than from Tcg Player's card info pages.
	 * 
	 * @param nodes
	 * @return
	 * @throws IOException
	 */
	public static List<CardEdition> parseCard ( List<Node> nodes ) throws IOException {
		Validation.begin()
				  .noLessThan(nodes.size(), CARD_TAG_COUNT, "nodes.size")
				  .check();
		List<CardEdition> cards = new ArrayList<CardEdition>();
		
		Matcher matcher = CARD_COUNT_PATTERN.matcher(((TextNode)nodes.get(0)).getWholeText());
		if ( matcher.matches() ) {
			String cardName = ((TextNode)nodes.get(1).childNode(0)).getWholeText();
			CardEdition foundCard = CardFactory.findCard(cardName);
			
			int cardCount = Integer.parseInt(matcher.group(1));
			for ( int idx = 0; idx < cardCount; idx++ ) {
				cards.add(foundCard);
			}
		}
		
		return cards;
	}
	
	/**
	 * When given the next set of nodes that are potentially a card, this method
	 * will return true the furst node is an {@link Element} node and if it has
	 * a child {@link TextNode} with text of 'Sideboard' ignoring the case.
	 * 
	 * @param nodes
	 * @return
	 */
	public static boolean startingSideBoard ( List<Node> nodes ) {
		boolean startingSideBoard = false;
		
		if ( nodes != null && nodes.size() > 0 && nodes.get(0) instanceof Element) {
			Elements possibleSbElements = ((Element)nodes.get(0)).select("b");
			
			// Account for possible i child nodes of the b element because we're expecting a TextNode
			// directly after this.
			Elements iElements = ((Element)nodes.get(0)).select("i");
			if ( iElements != null && !iElements.isEmpty() ) {
				possibleSbElements = iElements;
			}
			
			if ( !possibleSbElements.isEmpty() ) {
				startingSideBoard = ((TextNode)possibleSbElements.get(0).childNode(0)).getWholeText().equalsIgnoreCase("SIDEBOARD");
			}
		}
		
		return startingSideBoard;
	}
}
