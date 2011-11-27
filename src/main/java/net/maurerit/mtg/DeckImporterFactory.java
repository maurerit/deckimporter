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

/**
 * TODO: Javadoc Me.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class DeckImporterFactory
{
	public static DeckImporter createDeckImporter ( ImporterParams params ) {
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
}
