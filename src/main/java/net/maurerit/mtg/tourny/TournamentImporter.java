/**
 * 
 */
package net.maurerit.mtg.tourny;

import java.util.List;

import net.maurerit.mtg.ImporterParams;
import net.maurerit.mtg.deck.Deck;

/**
 * TODO: Javadoc Me.
 * 
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public interface TournamentImporter
{
	/**
	 * TODO: Javadoc Me.
	 * 
	 * @return
	 */
	ImporterParams importerFor ( );
	
	/**
	 * TODO: Javadoc Me.
	 * 
	 * @param url
	 * @return
	 */
	List<Deck> importTournament ( String url );
}
