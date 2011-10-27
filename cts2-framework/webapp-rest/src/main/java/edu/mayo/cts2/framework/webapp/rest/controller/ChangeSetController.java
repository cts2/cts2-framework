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
package edu.mayo.cts2.framework.webapp.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.service.profile.ChangeSetService;

/**
 * The Class CodeSystemController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ChangeSetController extends AbstractServiceAwareController {
	
	@Cts2Service
	private ChangeSetService changeSetService;

	@RequestMapping(value="/changesets", method=RequestMethod.POST)
	@ResponseBody
	public ChangeSet createChangeSet() {
		
		return this.changeSetService.createChangeSet();
	}
	
	@RequestMapping(value="/changesets/{changeSetUri}", method=RequestMethod.GET)
	@ResponseBody
	public ChangeSet readChangeSet(@PathVariable String changeSetUri) {
		
		return this.changeSetService.readChangeSet(changeSetUri);
	}
}