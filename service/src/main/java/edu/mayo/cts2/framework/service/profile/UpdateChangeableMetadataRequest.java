package edu.mayo.cts2.framework.service.profile;

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.StatusReference;

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
