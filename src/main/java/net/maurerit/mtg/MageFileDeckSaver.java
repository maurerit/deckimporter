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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import net.maurerit.validation.Validation;

import org.mage.shared.xmldb.Card;
import org.mage.shared.xmldb.Deck;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MageFileDeckSaver implements DeckSaver {

	/*
	 * (non-Javadoc)
	 * @see net.maurerit.mtg.DeckSaver#save(java.net.URL, org.mage.shared.xml.Deck)
	 */
	@Override
	public void save ( URL url, Deck deck ) throws SaveException {
		Validation.begin()
		          .notNull(url, "url")
		          .notNull(deck, "deck")
		          .check();
		BufferedWriter bw = null;
		try {
			File file = new File(url.toURI());
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(this.formatDeck(deck));
			bw.close();
		}
		catch ( URISyntaxException e ) {
			throw new SaveException("Invalid file URI.", e);
		}
		catch ( IOException e ) {
			throw new SaveException("IOException encountered during save.", e);
		}
		finally {
			if ( bw != null ) {
				try {
					bw.close();
				} catch (IOException e) {
					throw new SaveException("IOException encountered while closing the stream.", e);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.maurerit.mtg.DeckSaver#save(java.io.File, org.mage.shared.xml.Deck)
	 */
	@Override
	public void save ( File file, Deck deck ) throws SaveException {
		try {
			this.save(file.toURI().toURL(), deck);
		} catch (MalformedURLException e) {
			throw new SaveException("Could not find valid URL from file parameter.", e);
		}
	}

	/**
	 * Transforms the given {@link Deck} into the
	 * <a href="http://code.google.com/p/mage">Mage</a> mwdeck format.
	 * 
	 * @param deck
	 * @return
	 */
	private String formatDeck ( Deck deck ) {
		Validation.begin()
		          .notNull(deck.getMainBoardCards(), "deck.mainBoard")
		          .notNull(deck.getSideBoardCards(), "deck.sideBoard")
		          .check();
		StringBuilder sb = new StringBuilder();
		Map<String, Map.Entry<Card, Integer>> mainBoardCardCounts = new HashMap<String, Map.Entry<Card, Integer>>();
		Map<String, Map.Entry<Card, Integer>> sideBoardCardCounts = new HashMap<String, Map.Entry<Card, Integer>>();
		
		for ( Card card : deck.getMainBoardCards() ) {
			Map.Entry<Card, Integer> entry = getCardEntryFromMap(card, mainBoardCardCounts);
			entry.setValue(entry.getValue() + 1);
		}
		
		for ( Card card : deck.getSideBoardCards() ) {
			Map.Entry<Card, Integer> entry = getCardEntryFromMap(card, sideBoardCardCounts);
			entry.setValue(entry.getValue() + 1);
		}
		
		sb.append("NAME: ")
		  .append(deck.getName())
		  .append(System.getProperty("line.separator"));
		
		for ( Map.Entry<Card, Integer> entry : mainBoardCardCounts.values() ) {
			this.addCardEntry(false, sb, entry);
		}
		
		for ( Map.Entry<Card, Integer> entry : sideBoardCardCounts.values() ) {
			this.addCardEntry(true, sb, entry);
		}
		
		return sb.toString();
	}
	
	/**
	 * Attempts to find a {@link Map.Entry} for the given card in the passed in
	 * {@link Map} and if one is found it returns it.  If none are found, one is
	 * created, put into the map and that entry is returned.
	 * 
	 * @param card The card to find an entry for in the Map.
	 * @param map The map to search for a card.
	 * @return The found or created entry for the card.
	 */
	private Map.Entry<Card, Integer> getCardEntryFromMap ( Card card, Map<String, Map.Entry<Card, Integer>> map ) {
//		Validation.begin()
//		          .notNull(map, "map")
//		          .check();
		Map.Entry<Card, Integer> entry = map.get(card.getName());
		
		if ( entry == null ) {
			entry = new AbstractMap.SimpleEntry<Card, Integer>(card, 0);
			map.put(card.getName(), entry);
		}
		
		return entry;
	}
	
	/**
	 * Adds a formatted card entry to the passed in {@link StringBuilder} and
	 * sets it to be in the side board if <code>sideBoard</code>
	 * 
	 * @param sideBoard Whether this card should be in the side board.
	 * @param sb The StringBuilder to manipulate.
	 * @param entry The entry containing the card and its count.
	 */
	private void addCardEntry ( boolean sideBoard, StringBuilder sb, Map.Entry<Card, Integer> entry ) {
		if ( sideBoard ) {
			sb.append("SB: ");
		}
		
		sb.append(entry.getValue())
		  .append(" ")
		  .append("[")
		  .append(entry.getKey().getExpansionSetCode())
		  .append(":")
		  .append(entry.getKey().getCardNumber())
		  .append("] ")
		  .append(entry.getKey().getName())
		  .append(System.getProperty("line.separator"));
	}
}
