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

import java.util.ServiceLoader;

import net.maurerit.validation.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Javadoc Me.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class DeckImporterFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeckImporterFactory.class);
	
	public static DeckImporter createDeckImporter ( ImporterParams params ) {
		Validation.begin()
		          .notNull(params, "params")
		          .check();
		
		DeckImporter importer = null;
		ServiceLoader<DeckImporter> deckImporters = ServiceLoader.load(DeckImporter.class);
		
		for ( DeckImporter foundImporter : deckImporters ) {
			ImporterParams importerParams = foundImporter.importerFor();
			if ( importerParams != null ) {
				if ( importerParams.equals(params) ) {
					importer = foundImporter;
				}
			}
		}
		
		return importer;
	}
	
	public static DeckImporter createDeckImporter ( ImporterParams params, Class<? extends DeckSaver> saverClass ) {
		Validation.begin()
		          .notNull(params, "params")
		          .notNull(saverClass, "saverClass")
		          .check();
		
		DeckImporter importer = createDeckImporter(params);
		
		try {
			importer.setDeckSaver(saverClass.newInstance());
		} catch (InstantiationException e) {
			LOGGER.error("Could not instantiate DeckSaver [" + saverClass.getName() + "]", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Default constructor does not exist for DeckSaver [" + saverClass.getName() + "]", e);
		}
		
		return importer;
	}
}
