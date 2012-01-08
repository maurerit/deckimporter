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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mage.shared.xml.Deck;

/**
 * Holds utility methods I've found useful for deck importer tests.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class DeckImporterTestUtils {
	public static String invokeDeckSaversFormatMethod ( MageFileDeckSaver obj, Deck deck ) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = obj.getClass().getDeclaredMethod("formatDeck", new Class[] { Deck.class });
		method.setAccessible(true);
		return (String)method.invoke(obj, new Object[] { deck });
	}
	
	public static String makeReadableStacktrace ( Throwable throwable ) {
		StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
