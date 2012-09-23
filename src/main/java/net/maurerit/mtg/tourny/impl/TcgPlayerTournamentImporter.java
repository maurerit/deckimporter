/**
 * 
 */
package net.maurerit.mtg.tourny.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.maurerit.mtg.ImportException;
import net.maurerit.mtg.ImporterParams;
import net.maurerit.mtg.deck.Deck;
import net.maurerit.mtg.deck.DeckImporter;
import net.maurerit.mtg.deck.impl.TcgPlayerDeckImporter;
import net.maurerit.mtg.tourny.TournamentImporter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TODO: Javadoc Me.
 * 
 * @author @author Matthew L. Maurer maurer.it@gmail.com
 */
public class TcgPlayerTournamentImporter implements TournamentImporter
{

	private static final int DECK_LINK_INDEX = 1;
	private static final int FINISHED_CHILD_INDEX = 4;
	private static final int DATE_CHILD_INDEX = 5;
	
	private static final DateFormat format = new SimpleDateFormat("mm/dd/yyyy");

	/* (non-Javadoc)
	 * @see net.maurerit.mtg.tourny.TournamentImporter#importerFor()
	 */
	@Override
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "magic.tcgplayer.com");
	}

	/* (non-Javadoc)
	 * @see net.maurerit.mtg.tourny.TournamentImporter#importTournament(java.lang.String)
	 */
	@Override
	public List<Deck> importTournament ( String url ) {
		List<Deck> decks = new ArrayList<Deck>();
		
		try {
			Elements elements = Jsoup.connect(url).get().select("table[width=100%] tr:has(td > a[href^=/db/deck.asp])");
			
			DeckImporter importer = new TcgPlayerDeckImporter();
			
			for ( Element element : elements ) {
				String deckUrl = "http://magic.tcgplayer.com" + element.select("a[href^=/db/deck.asp]").attr("href");
				Deck importedDeck = importer.importDeck(deckUrl);
				
				//Set some attribtues of the deck from the various other children of element
				importedDeck.setAuthor(element.select("a[href*=Creator]").text());
				importedDeck.setLocation(element.select("a[href*=Location]").text());
				importedDeck.setFinished(element.child(FINISHED_CHILD_INDEX).text());
				Calendar cal = Calendar.getInstance();
				cal.setTime(format.parse(element.child(DATE_CHILD_INDEX).text()));
				importedDeck.setDate(cal);
				
				decks.add(importedDeck);
			}
		} catch (IOException e) {
			throw new ImportException("IO Exception while parsing tournament", e);
		} catch (ParseException e) {
			throw new ImportException("Parse Exception while parsing the event date.", e);
		}
		
		return decks;
	}

}
