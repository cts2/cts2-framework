package edu.mayo.cts2.framework.service.profile.update;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectoryEntry;
import edu.mayo.cts2.framework.service.command.restriction.ChangeSetQueryExtensionRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;

public interface ChangeSetQueryExtension extends BaseQueryService {

	/**
	 * Gets the resource summaries.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @param readContext 
	 * @param page the page
	 * @return the resource summaries
	 */
	public DirectoryResult<ChangeSetDirectoryEntry> getResourceSummaries(
			ChangeSetQuery changeSetQuery,
			SortCriteria sort,
			Page page);

	/**
	 * Count.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @return the int
	 */
	public int count(
			Query query,
			Set<ResolvedFilter> filterComponent,
			ChangeSetQueryExtensionRestrictions restrictions);
}
