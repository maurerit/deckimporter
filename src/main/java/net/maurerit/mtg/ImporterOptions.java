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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple container to hold many options for a {@link DeckImporter}
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class ImporterOptions {
	private Map<String, String> options;
	
	/**
	 * Construct a basic ImporterOptions object.
	 */
	public ImporterOptions ( ) {
		options = new HashMap<String, String>();
	}
	
	/**
	 * Construct a ImporterOptions object and add a key and value.
	 * 
	 * @param option
	 * @param value
	 */
	public ImporterOptions ( String option, String value ) {
		this();
		options.put(option, value);
	}
	
	/**
	 * Add an option.
	 * 
	 * @param key
	 * @param value
	 */
	public void add ( String key, String value ) {
		options.put(key, value);
	}
	
	/**
	 * Get an option by its given key name.  This method may return null.
	 * 
	 * @param key
	 * @return
	 */
	public String get ( String key ) {
		return options.get(key);
	}
}
