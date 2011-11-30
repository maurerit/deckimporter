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

import java.net.URL;
import java.util.ServiceLoader;

import net.maurerit.validation.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for {@link DeckImporters}.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class DeckImporterFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeckImporterFactory.class);
	
	/**
	 * Creates a {@link DeckImporter} that can import decks from the given {@link URL}
	 * 
	 * @param url The {@link URL} that the desired {@link DeckImporter} supports.
	 * @return A valid {@link DeckImporter} or null if none can be found.
	 * @throws ParameterException if <code>url</code> is null
	 */
	public static DeckImporter createDeckImporter ( URL url ) {
		Validation.begin()
		          .notNull(url, "url")
		          .check();
		String protocol = url.getProtocol();
		String host = url.getHost();
		return createDeckImporter(new ImporterParams(protocol, host));
	}
	
	/**
	 * Given the {@link ImporterParams} this factory method will inspect all
	 * the registered {@link DeckImporter}'s and return the first that matches.
	 * 
	 * @param params The {@link ImporterParams} that the desired {@link DeckImporter} supports.
	 * @return A {@link DeckImporter} or null if none can be found.
	 * @throws ParameterException if <code>params</code> is null
	 */
	public static DeckImporter createDeckImporter ( ImporterParams params ) {
		Validation.begin()
		          .notNull(params, "params")
		          .check();
		
		DeckImporter importer = null;
		ServiceLoader<DeckImporter> deckImporters = ServiceLoader.load(DeckImporter.class);
		
		for ( DeckImporter foundImporter : deckImporters ) {
			ImporterParams importerParams = foundImporter.importerFor();
			if ( importerParams != null ) {
				if ( importerParams.matches(params) ) {
					importer = foundImporter;
				}
			}
		}
		
		return importer;
	}
	
	/**
	 * Creates a {@link DeckImporter} for the given {@link ImporterParams} and will
	 * attempt to create a new instance of the {@link DeckSaver} implementation.  If
	 * the saver can not be created an error will be logged to the logger.  One of
	 * two errors will be logged depending on the type of exception caught.  These are:
	 * 
	 * <ul>
	 *   <li>InstantiationException caught while instantiating DeckSaver [FQN of saverClass]</li>
	 *   <li>IllegalAccessException caught while instantiating DeckSaver [FQN of saverClass]</li>
	 * </ul>
	 * 
	 * @param params The {@link ImporterParams} that the desired {@link DeckImporter} supports.
	 * @param saverClass The {@link DeckSaver} to be created and injected into the {@link DeckImpoter}
	 * @return A {@link DeckImporter} or null if none can be found.
	 * @throws ParameterException is <code>params</code> or <code>saverClass</code> are null.
	 * @throws MultiParameterException if both <code>params</code> and <code>saverClass</code> are null.
	 */
	public static DeckImporter createDeckImporter ( ImporterParams params, Class<? extends DeckSaver> saverClass ) {
		Validation.begin()
		          .notNull(params, "params")
		          .notNull(saverClass, "saverClass")
		          .check();
		
		DeckImporter importer = createDeckImporter(params);
		
		try {
			importer.setDeckSaver(saverClass.newInstance());
		} catch (InstantiationException e) {
			LOGGER.error("InstantiationException caught while instantiating DeckSaver [" + saverClass.getName() + "]", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("IllegalAccessException caught while instantiating DeckSaver [" + saverClass.getName() + "]", e);
		}
		
		return importer;
	}
}
