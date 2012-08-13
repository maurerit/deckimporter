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
package net.maurerit.mtg.card;

import org.mage.shared.xmldb.Card;

/**
 * Runtime Pluggable beans which can provide fully 'hydrated' {@link Card}
 * instances from very little information (assuming sensible defaults) <code>yada yada</code>.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 * @since 1.2.0
 */
public interface CardProvider
{
	/**
	 * TODO: Javadoc Me
	 * 
	 * @param cardName
	 * @return
	 */
	Card findCard ( String cardName );
}
