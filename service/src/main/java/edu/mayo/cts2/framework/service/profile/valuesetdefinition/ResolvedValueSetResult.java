package edu.mayo.cts2.framework.service.profile.valuesetdefinition;

import java.util.List;

import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader;

public class ResolvedValueSetResult extends DirectoryResult<EntitySynopsis>{
	
	private ResolvedValueSetHeader resolvedValueSetHeader;

	public ResolvedValueSetResult(
			ResolvedValueSetHeader resolvedValueSetHeader,
			List<EntitySynopsis> entries, 
			boolean atEnd) {
		super(entries, atEnd);
		this.setResolvedValueSetHeader(resolvedValueSetHeader);
	}

	public ResolvedValueSetHeader getResolvedValueSetHeader() {
		return resolvedValueSetHeader;
	}

	public void setResolvedValueSetHeader(ResolvedValueSetHeader resolvedValueSetHeader) {
		this.resolvedValueSetHeader = resolvedValueSetHeader;
	}
}
