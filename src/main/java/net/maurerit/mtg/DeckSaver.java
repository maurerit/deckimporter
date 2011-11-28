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

import java.io.IOException;
import java.net.URL;

import org.mage.shared.xml.Deck;
import org.mage.shared.xml.Library;

/**
 * This interface defines the methods to save an imported {@link Library}
 * to locations specified by {@link URL}'s.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public interface DeckSaver
{
	/**
	 * Given a {@link URL} and a {@link Library} implementations of this
	 * interface must attempt to save the <code>library</code> to the given
	 * <code>url</code>
	 * 
	 * @param url
	 * @param library
	 * @return True or false if the save succeeded
	 * @throws IOException if the save operation fails.
	 */
	void save ( URL url, Deck library ) throws SaveException;
	
	/* TODO: Implement me later */
//	void save ( URL url, Deck library, DeckFormat format );
}
