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

/**
 * Defines an exception thrown while attempting to save a {@link Library}
 * from an attempted import.
 *
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class SaveException extends Exception
{
	private static final long serialVersionUID = 6814335021137728721L;
	
	public SaveException ( String message, Throwable cause ) {
		super(message, cause);
	}
}
