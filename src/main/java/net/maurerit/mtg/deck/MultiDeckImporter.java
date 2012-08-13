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

import java.util.List;
import java.util.Map;


/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public interface MultiDeckImporter extends DeckImporter
{
	/**
	 * Informs the {@link DeckImporter} where its deck list url is that we
	 * are interested in.
	 * 
	 * @param url Location of the deck list.
	 * @return Usually {this} to enable chained calls.  For instance
	 * <pre>
	 * multiDeckImporter
	 *                  .connect("http://www.magic-league.com/tournament/info.php?id=75915&view=decks")
	 *                  .getDeckLists();
	 * </pre>
	 */
	public MultiDeckImporter connect ( String url );
	
	/**
	 * 
	 * @return
	 */
	public List<Map<String, String>> getDeckLists ( );
}
