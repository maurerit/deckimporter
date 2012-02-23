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
package net.maurerit.mtg;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.mage.shared.xmldb.Card;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class MtgVault implements CardProvider
{
	public static final String BASE_CARD_URL = "http://www.mtgvault.com/ViewCard.aspx?CardName={1}";
	public static final Pattern MTGVAULT_SET_PATTERN = Pattern.compile(".*Edition=([A-Z0-9]{3})");

	public Card findCard ( String cardName ) {
		try {
			return findCardByName(cardName);
		}
		catch (IOException e) {
			//TODO: Log this somewhere
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @deprecated Use {@link CardFactory#findCard(String)} instead.
	 * 
	 * @param cardName
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static Card findCardByName ( String cardName ) throws IOException {
		Card foundCard = null;
		//XXX: Build up a card database for easier querying of card meta data.
		Document cardDoc = Jsoup.connect(BASE_CARD_URL.replace("{1}", cardName.replace(" ", "%20"))).get();
		
		//This is the set of the latest version of this card, but its a readable set and its
		//in some HTML somewhat similar to <td class="cardinfo"><b>Edition:</b>" {SET_FULL_NAME}"</td>
		//we'll dig into that structure and trim out the leading " " and just have a {SET_FULL_NAME} which
		//we'll use to find the code for in the list of available editions further down the page in subsequent $("td.cardinfo")'s
		//after the 12th index of that select.
		String setNameToFind = ((TextNode)cardDoc.select("td.cardinfo").get(3).childNode(1)).getWholeText().trim();
		String setCode = null;
		
		//Find the latest set by selecting the img tag with an immediate a sibling that contains the set we're looking for			
		//TODO: Might want to offer a way to inspect some preferences for people that like to play with certain images.
		Element setLink = cardDoc.select("td.cardinfo > img ~ a:contains(" + setNameToFind + ")").get(0);
		Matcher setMatcher = MTGVAULT_SET_PATTERN.matcher(setLink.attr("href"));
		
		if ( setMatcher.matches() &&
			 ((TextNode)setLink.childNode(0)).getWholeText().equalsIgnoreCase(setNameToFind) )
		{
			setCode = setMatcher.group(1);
		}
		
		if ( setCode != null ) {
			foundCard = new Card();
			foundCard.setName(cardName);
			foundCard.setExpansionSetCode(setCode);
			foundCard.setCardNumber(MtgVaultDeckImporter.getCardNumber(cardDoc.select("td.cardinfo").get(8).text()));
		}
		
		return foundCard;
	}
	
}
