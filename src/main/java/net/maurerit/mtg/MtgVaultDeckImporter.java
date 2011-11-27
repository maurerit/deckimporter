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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.kohsuke.MetaInfServices;
import org.mage.shared.xml.Library;

/**
 *
 * @author Georgi Koshov koshov_at_gmail.com
 */
@MetaInfServices(DeckImporter.class)
public class MtgVaultDeckImporter implements DeckImporter {
	
	public ImporterParams importerFor ( ) {
		return new ImporterParams("http", "mtgvault.com");
	}
	
	public Library importDeck ( String url ) {
		return null;
	}
	
    public static String validate(String link) {
        if (isURL(link)){
            if (link.contains("mtgvault.com")){
                return "valid";
            }else {
                return "src";
            }
        }else {
            return "url";
        }
    }
    
    private static boolean isURL(String s) {
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        try {
            Pattern patt = Pattern.compile(regex);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
        return false;
        }  
    
    }

    public static File getDeck(String deckLink) {
        try {
            // Create temp file.
            File temp = File.createTempFile("generatedDeck", ".dck");

            // Delete temp file when program exits.
            temp.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.write(htmlParser(deckLink));
            out.close();
            return temp;
        } catch (IOException e) {
            return null;
        }
    }
    
    private static String htmlParser(String link) {
        
        String output = "";
        
        try {
            Document doc = Jsoup.connect(link).get();
            Elements card = doc.select("td[class$=card]");
            for (int i = 0; i < card.size(); i++) {
                String curLine = "";
                String cardData = card.get(i).text(); //of the form "1x Card Name"
                String cardLink = "http://www.mtgvault.com/" + card.get(i).select("a").first().attr("href");
                
                if (cardData != null && !cardData.isEmpty()) {
                    curLine = cardData.substring(0, cardData.indexOf("x")) //first character of the string is the number of occurrences
                              + getCardCode(cardLink) //given a link returns string of the form: [ABC:123], corresponding to edition and card number
                              + cardData.substring(cardData.indexOf("x") + 1) //the remaining part of the string is the name of the card
                              + "\n";
                } else {
                    System.out.println("No card data at index " + i);
                }
                
                output += curLine;
            }
        } catch (IOException ex) {
            System.out.println("Exception when parsing the MTGVault deck page: " + ex.getMessage());
        }
        
        System.out.println(output);
        return output;
    }
    
    private static String getCardCode(String link) {
        String output = "";
        try {
            int editionInd = link.indexOf("Edition=") + 8; //starting index of the edition key in the URL
            Document doc = Jsoup.connect(link.replace(" ", "%20")).get();
            output = " [" 
                     + link.substring(editionInd, editionInd + 3)
                     + ":"
                     + getCardNumber( doc.select("td[class$=cardinfo]").get(8).text() )
                     + "]";
            
        }catch (IOException ex) {
            System.out.println("Exception when parsing the MTGVault card page: " + ex.getMessage());
        }
        return output;
    }
    
    private static String getCardNumber(String s) {
        int from = s.indexOf("Number: ") + 8;
        int to = s.indexOf(" /");
        String str = s.substring(from, to);
        return str;
    }
}
