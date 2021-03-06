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
package net.maurerit.mtg.deck;

import java.net.URL;

import net.maurerit.mtg.ImportException;
import net.maurerit.mtg.ImporterOptions;
import net.maurerit.mtg.ImporterParams;

/**
 * A DeckImporter is any object that can import a set of cards from a remote host,
 * local file, web service or any other source of information that pertain to
 * Magic the Gathering decks.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public interface DeckImporter
{
	public static final String PLAYER_NAME_OPTION = "PlayerName";
	
	/**
	 * Implementations must return at least one {@link ImporterParams}
	 * which will be used in the matching process when a consumer is
	 * attempting to find a DeckImporter for a given source of information.
	 * 
	 * @return Never null and always a valid {@link ImporterParams} object.
	 */
	ImporterParams importerFor ( );
	
	/**
	 * A method which can be used to register a new or already existing
	 * {@link DeckSaver} with this DeckImporter.  Clients may wish to import
	 * from something and save to a local file, a remote host, etc.  Nothing
	 * is forced on the DeckImporter to actually do anything with this or to
	 * even create one during construction as clients will determine what
	 * they want to do with the imported deck.
	 * 
	 * @param saver
	 */
	void setDeckSaver ( DeckSaver saver );
	
	/**
	 * Gets the {@link DeckSaver} if one was created by this DeckImporter
	 * or registered at a later time.
	 * 
	 * @see #setDeckSaver(DeckSaver)
	 * @return Null or a valid {@link DeckSaver}
	 */
	DeckSaver getDeckSaver ( );
	
	/**
	 * The main operation that a DeckSaver is meant to perform.  Implementations
	 * may reach out to the internet, to a database or just read data from a local
	 * file system.
	 * 
	 * @param url The {@link URL} of the deck that should be imported.
	 * @return The imported {@link Deck} if everything went well.
	 * @throws ImportException if an import failed miserably.
	 */
	Deck importDeck ( String url );
	
	/**
	 * Some importers may have the need to have some options specified before they
	 * begin importing a single deck.  For instance there may be 'repositories' that
	 * don't single decks at a given URL buy many, so one may need to specify which
	 * deck to import from the specified URL.
	 * 
	 * @param url The {@link URL} of the deck that should be imported.
	 * @param options 
	 * @return
	 */
	Deck importDeck ( String url, ImporterOptions options );
}
