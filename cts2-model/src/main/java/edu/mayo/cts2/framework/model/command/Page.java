/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.model.command;

/**
 * A request for a 'page' of CTS2 resources.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Page implements Cloneable {

	private int DEFAULT_PAGE_SIZE = 50;
	private int DEFAULT_PAGE = 0;
			
	private int page = DEFAULT_PAGE;
	private int maxtoreturn = DEFAULT_PAGE_SIZE;
	
	public Page(){
		this.maxtoreturn = DEFAULT_PAGE_SIZE;
	}
	
	public Page(int maxtoreturn){
		this.maxtoreturn = maxtoreturn;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getMaxToReturn() {
		return maxtoreturn;
	}
	public void setMaxToReturn(int maxtoreturn) {
		if(maxtoreturn == 0){
			throw new IllegalArgumentException("Cannot ask for zero results.");
		}
		this.maxtoreturn = maxtoreturn;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Page clone() throws CloneNotSupportedException {
		Page page = new Page(maxtoreturn);
		page.setPage(this.getPage());
		page.setMaxToReturn(this.getMaxToReturn());
		return page;
	}
	
	public int getStart(){
		return this.getPage() * this.getMaxToReturn();
	}
	
	public int getEnd(){
		return (this.getPage() + 1) * this.getMaxToReturn();
	}
}
