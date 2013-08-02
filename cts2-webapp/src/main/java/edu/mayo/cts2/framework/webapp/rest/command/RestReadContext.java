package edu.mayo.cts2.framework.webapp.rest.command;

import java.util.List;

import edu.mayo.cts2.framework.model.core.LanguageReference;
import edu.mayo.cts2.framework.model.service.core.types.ActiveOrAll;

public class RestReadContext {

	private ActiveOrAll active = ActiveOrAll.ACTIVE_ONLY;

	private LanguageReference language;

	private String changesetcontext;

	private java.util.Date referencetime;
	
    private List<String> contentfilter;

	public ActiveOrAll getActive() {
		return active;
	}

	public void setActive(ActiveOrAll active) {
		this.active = active;
	}

	public LanguageReference getLanguage() {
		return language;
	}

	public void setLanguage(LanguageReference language) {
		this.language = language;
	}

	public String getChangesetcontext() {
		return changesetcontext;
	}

	public void setChangesetcontext(String changesetcontext) {
		this.changesetcontext = changesetcontext;
	}

	public java.util.Date getReferencetime() {
		return referencetime;
	}

	public void setReferencetime(java.util.Date referencetime) {
		this.referencetime = referencetime;
	}
	
    public List<String> getContentfilter() {
        return contentfilter;
    }

    public void setContentfilter(List<String> contentfilter) {
        this.contentfilter = contentfilter;
    }
	
}
