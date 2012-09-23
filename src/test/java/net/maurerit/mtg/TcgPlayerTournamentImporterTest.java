package net.maurerit.mtg;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.maurerit.mtg.deck.Deck;
import net.maurerit.mtg.tourny.impl.TcgPlayerTournamentImporter;

import org.junit.Test;

public class TcgPlayerTournamentImporterTest {
	
	@Test
	public void importPTQRavnicaLindenhurst07282012 ( ) {
		TcgPlayerTournamentImporter importer = new TcgPlayerTournamentImporter();
		String[] playerActual = {"Matt Hoey", "Joshua Revard", "Andrew Baeckstrom", "Josh Mcclain", "Eddie Song", "Zach Watts", "Edward Kim", "Stephen Berrios"};
		String[] finishedActual = {"1st", "2nd", "3rd - 4th", "3rd - 4th", "5th - 8th", "5th - 8th", "5th - 8th", "5th - 8th"};
		
		List<Deck> importedDecks = importer.importTournament("http://magic.tcgplayer.com/db/deck_search_result.asp?Location=2012%20PTQ%20Ravnica%20-%20Lindenhurst,%20IL%20-%207/28");
		
		assertEquals("Incorrect number of imported decks.", 8, importedDecks.size());
		
		for ( int idx = 0; idx < importedDecks.size(); idx++ ) {
			assertEquals("Player should be: " + playerActual[idx], playerActual[idx], importedDecks.get(idx).getAuthor());
			assertEquals("Finished should be: " + finishedActual[idx], finishedActual[idx], importedDecks.get(idx).getFinished());
		}
	}

}
