/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.service.profile;

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.StatusReference;

/**
 * The Class UpdateChangeableMetadataRequest.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UpdateChangeableMetadataRequest {
	
    /**
     * the state of this model element in an externally defined
     * workflow
     */
	private StatusReference status;
	
    /**
     * a note, set of instructions and other information about the
     * nature and purpose of this change
     */
    private OpaqueData changeNotes;

    /**
     * the person or organization responsible for this change
     */
    private SourceReference changeSource;

	public StatusReference getStatus() {
		return status;
	}

	public void setStatus(StatusReference status) {
		this.status = status;
	}

	public OpaqueData getChangeNotes() {
		return changeNotes;
	}

	public void setChangeNotes(OpaqueData changeNotes) {
		this.changeNotes = changeNotes;
	}

	public SourceReference getChangeSource() {
		return changeSource;
	}

	public void setChangeSource(SourceReference changeSource) {
		this.changeSource = changeSource;
	}

}
