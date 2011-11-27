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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pathContains == null) ? 0 : pathContains.hashCode());
		result = prime * result + ((urlType == null) ? 0 : urlType.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImporterParams other = (ImporterParams) obj;
		if (pathContains == null) {
			if (other.pathContains != null)
				return false;
		} else if (!pathContains.contains(other.pathContains))
			return false;
		if (urlType == null) {
			if (other.urlType != null)
				return false;
		} else if (!urlType.equalsIgnoreCase(other.urlType))
			return false;
		return true;
	}
}
