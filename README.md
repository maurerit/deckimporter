M:tG Deck Importer
==================

A simple to use Magic the Gathering deck importer which easily supports any online deck
'repository'.  Currently only a two implementations exists which are for mtgvault.com and
magic.tcgplayer.com decks.

The main components:

Deck and M:tG portable java data model
	org.mage.shared.xml specified by the src/main/resources/MageData.xsd schema file.

DeckImporter
	The responsibility of these is to import decks into the org.mage.shared.xml.Deck deck format

DeckSaver
	The responsibility of these is to save the org.mage.shared.xml.Deck to a file, webservice
	or any other backing store.

DeckImporterFactory
	Builds a DeckImporter that matches a particular ImporterParams input and optionally has a reference
	to a specific DeckSaver (most implementations provided here will build sensible DeckSaver defaults).

	This factory will at first utilize the Java ServiceLoader to find DeckImporter implementations
	and these can be easily registered with the JVM via metainf-services
	(http://weblogs.java.net/blog/kohsuke/archive/2009/03/my_project_of_t.html)

Unit tests are provided for the current implementations in this library in the standard maven location.
