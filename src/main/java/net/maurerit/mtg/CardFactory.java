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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.mage.shared.xmldb.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Javadoc me
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 * @since 1.2.0
 */
public class CardFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeckImporterFactory.class);
	
	// TODO: This should be something better...
	private static final Map<String, CacheKey<String,Card>> cache = new HashMap<String, CacheKey<String,Card>>();

	public static Card findCard ( String cardName ) {
		Card foundCard = null;
		
		//Start with a dumb cache to help while importing tournaments.
		if ( !cache.containsKey(cardName) ) {
			ServiceLoader<CardProvider> cardProviders = ServiceLoader.load(CardProvider.class);
			
			for ( CardProvider provider : cardProviders ) {
				foundCard = provider.findCard(cardName);
				
				if ( foundCard != null ) {
					cache.put(cardName, new CacheKey<String,Card>(cardName,foundCard));
				}
			}
		}
		else {
			return cache.get(cardName).getObject();
		}
		
		return foundCard;
	}
	
	public static List<CacheHit<String, Integer>> getCacheHits ( ) {
		List<CacheHit<String, Integer>> cacheHits = new ArrayList<CacheHit<String, Integer>>();
		
		for ( Map.Entry<String, CacheKey<String,Card>> entry : cache.entrySet() ) {
			cacheHits.add(new CacheHit<String, Integer>(entry.getKey(), entry.getValue().getHits()));
		}
		
		return cacheHits;
	}
	
	public static class CacheHit<T, O> {
		private T type;
		private O object;
		
		CacheHit ( T type, O object ) {
			this.type = type;
			this.object = object;
		}

		/**
		 * @return the type
		 */
		public T getType ( ) {
			return type;
		}

		/**
		 * @return the object
		 */
		public O getObject ( ) {
			return object;
		}
	}

	private static class CacheKey<K, O> {
		private K key;
		private O object;
		
		private int keyHits;
		private int objectHits;
		
		CacheKey ( K key, O object ) {
			this.key = key;
			this.object = object;
			this.keyHits = 0;
			this.objectHits = 0;
		}

		/**
		 * @return the key
		 */
		public K getKey ( ) {
			keyHits++;
			return key;
		}

		/**
		 * @return the object
		 */
		public O getObject ( ) {
			objectHits++;
			return object;
		}

		/**
		 * @return the hitCounter
		 */
		public int getHits ( ) {
			return keyHits * 3 + objectHits;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode ( ) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals ( Object obj ) {
			if (this == obj) { return true; }
			if (obj == null) { return false; }
			if (!(obj instanceof CacheKey)) { return false; }
			@SuppressWarnings ( "rawtypes" )
			CacheKey other = (CacheKey) obj;
			//XXX: I want the key counts to matter somewhere.
			Object otherKey = other.getKey();
			Object myKey = getKey();
			if (myKey == null) {
				if (otherKey != null) { return false; }
			}
			else if (!myKey.equals(otherKey)) { return false; }
			if (object == null) {
				if (other.object != null) { return false; }
			}
			else if (!object.equals(other.object)) { return false; }
			return true;
		}
	}
}
