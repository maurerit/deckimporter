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

import net.maurerit.validation.Validation;

/**
 * TODO: Javadoc Me.
 * 
 * @author Matthew L. Maurer maurer.it@gmail.com
 */
public class ImporterParams {
	private String urlType;
	private String pathContains;

	public ImporterParams(String urlType, String pathContains) {
		super();
		this.urlType = urlType;
		this.pathContains = pathContains;
	}

	/**
	 * @return the urlType
	 */
	public String getUrlType() {
		return urlType;
	}

	/**
	 * @return the pathContains
	 */
	public String getPathContains() {
		return pathContains;
	}
	
	public boolean matches ( ImporterParams params ) {
		Validation.begin()
		          .notNull(params, "params")
		          .notEmpty(params.getPathContains(), "params.pathContains")
		          .notEmpty(params.getUrlType(), "params.urlType")
		          .check();
		boolean matches = false;
		
		if ( params.getPathContains().contains(this.pathContains) &&
			 params.getUrlType().equalsIgnoreCase(this.urlType) )
		{
			matches = true;
		}
		
		return matches;
	}
}
