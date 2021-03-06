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

import mage.tracker.domain.Card;
import mage.tracker.domain.CardEdition;
import mage.tracker.domain.Expansion;
import net.maurerit.mtg.ImportException;
import net.maurerit.mtg.ImporterOptions;
import net.maurerit.mtg.ImporterParams;
import net.maurerit.mtg.deck.Deck;
import net.maurerit.mtg.deck.DeckImporter;
import net.maurerit.mtg.deck.DeckSaver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Georgi Koshov koshov_at_gmail.com
 */
public class MtgVaultDeckImporter implements DeckImporter {
	
	private DeckSaver saver = new MageFileDeckSaver();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "mtgvault.com");
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
		return this.saver;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck importDeck ( String url ) {
		Deck importedDeck = new Deck();
		
		try {
			Document deckInfo = Jsoup.connect(url).get();
	        Elements cards = deckInfo.select("#main_deck td.card");
	        
	        for (int i = 0; i < cards.size(); i++) {
                String cardData = cards.get(i).text(); //of the form "1x Card Name"
                String cardLink = "http://www.mtgvault.com/" + cards.get(i).select("a").first().attr("href");
                
                if (cardData != null && !cardData.isEmpty()) {
                	int total = Integer.parseInt(cardData.substring(0, cardData.indexOf('x')));
                	
                	int editionInd = cardLink.indexOf("Edition=") + 8; //starting index of the edition key in the URL
            		Document cardInfo = Jsoup.connect(cardLink.replace(" ", "%20")).get();
                	
                	for ( int idx = 0; idx < total; idx++ ) {
                		Card innerCard = new Card();
                		CardEdition card = new CardEdition();
                		card.setCard(innerCard);
                		innerCard.setName(cardData.substring(cardData.indexOf('x') + 2));
                		card.setCardNumber(getCardNumber(cardInfo.select("td[class$=cardinfo]").get(8).text()));
                		Expansion expansion = new Expansion();
                		expansion.setCode(cardLink.substring(editionInd, editionInd + 3));
                		card.setExpansion(expansion);
                		
                		importedDeck.getMainBoardCards().add(card);
                	}
                }
	        }
	        
	        cards = deckInfo.select("#right_column td.card");
	        
	        //TODO: Push this out to a utility method thats sideboard/mainboard switchable.
	        for (int i = 0; i < cards.size(); i++) {
                String cardData = cards.get(i).text(); //of the form "1x Card Name"
                String cardLink = "http://www.mtgvault.com/" + cards.get(i).select("a").first().attr("href");
                
                if (cardData != null && !cardData.isEmpty()) {
                	int total = Integer.parseInt(cardData.substring(0, cardData.indexOf('x')));
                	
                	int editionInd = cardLink.indexOf("Edition=") + 8; //starting index of the edition key in the URL
            		Document cardInfo = Jsoup.connect(cardLink.replace(" ", "%20")).get();
                	
                	for ( int idx = 0; idx < total; idx++ ) {
                		Card innerCard = new Card();
                		CardEdition card = new CardEdition();
                		card.setCard(innerCard);
                		innerCard.setName(cardData.substring(cardData.indexOf('x') + 2));
                		card.setCardNumber(getCardNumber(cardInfo.select("td.cardinfo").get(8).text()));
                		Expansion expansion = new Expansion();
                		expansion.setCode(cardLink.substring(editionInd, editionInd + 3));
                		card.setExpansion(expansion);
                		
                		importedDeck.getSideBoardCards().add(card);
                	}
                }
	        }
		}
		catch ( IOException e ) {
			throw new ImportException("IOException while parsing deck.", e);
		}
		
		return importedDeck;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck importDeck(String url, ImporterOptions options) {
		// TODO Auto-generated method stub
		return null;
	}
    
    public static String getCardNumber(String s) {
        int from = s.indexOf("Number: ") + 8;
        int to = s.indexOf(" /");
        String str = s.substring(from, to);
        return str;
    }
}
