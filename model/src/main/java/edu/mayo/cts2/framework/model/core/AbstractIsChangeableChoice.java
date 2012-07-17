package edu.mayo.cts2.framework.model.core;

import edu.mayo.cts2.framework.model.BaseCts2ModelObject;
import edu.mayo.cts2.framework.model.core.types.EntryState;

public abstract class AbstractIsChangeableChoice extends BaseCts2ModelObject implements IsChangeable {
	
	@Override
	public ChangeableElementGroup getChangeableElementGroup() {
		return getIsChangeable().getChangeableElementGroup();
	}

	@Override
	public EntryState getEntryState() {
		return getIsChangeable().getEntryState();
	}
	
	@Override
	public void setChangeableElementGroup(ChangeableElementGroup group) {
		getIsChangeable().setChangeableElementGroup(group);
	}

	@Override
	public void setEntryState(EntryState entryState) {
		getIsChangeable().setEntryState(entryState);
	}

	private IsChangeable getIsChangeable(){
		Object choice = this.getChoiceValue();
		try {
			return (IsChangeable) choice;
		} catch (ClassCastException e) {
			throw new RuntimeException("Class: "+ choice.getClass().getName() + " does not implement 'IsChangeable'.");
		}
	}

	public abstract Object getChoiceValue();

}
