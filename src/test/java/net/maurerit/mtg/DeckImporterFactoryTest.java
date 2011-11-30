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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import net.maurerit.validation.MultiParameterException;
import net.maurerit.validation.ParameterException;

import org.junit.Test;

/**
 * TODO: Javadoc me
 *
 * @author MM66053
 */
public class DeckImporterFactoryTest {

	@Test
	public void shouldFindMtgDeckImporter() {
		ImporterParams params = new ImporterParams("http", "mtgvault.com");
		DeckImporter importer = DeckImporterFactory.createDeckImporter(params);
		
		assertTrue("DeckImporter should be an instance of MtgVaultDeckImporter instead recieved: [" + importer + "].", importer instanceof MtgVaultDeckImporter);
	}

	@Test
	public void shouldThrowParameterExceptionNullUrl ( ) {
		try {
			DeckImporterFactory.createDeckImporter((URL)null);
			fail("Should have thrown a ParameterException");
		}
		catch ( ParameterException e ) { /* Good test case */ }
	}
	
	@Test
	public void shouldThrowParameterExceptionNullParams ( ) {
		try {
			DeckImporterFactory.createDeckImporter((ImporterParams)null);
			fail("Should have thrown a ParameterException");
		}
		catch ( ParameterException e ) { /* Good test case */ }
	}
	
	@Test
	public void shouldThrowParameterExceptionNullSaver ( ) {
		try {
			DeckImporterFactory.createDeckImporter(new ImporterParams("", ""), null);
			fail("Should have thrown a ParameterException");
		}
		catch ( ParameterException e ) { /* Good test case */ }
	}
	
	@Test
	public void shouldThrowMultiParameterException ( ) {
		try {
			DeckImporterFactory.createDeckImporter(null, null);
			fail("Should have thrown a MultiParameterException.");
		}
		catch ( MultiParameterException e ) { /* Good test case */ }
	}
}
