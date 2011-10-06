package edu.mayo.cts2.framework.core.config.option;

public interface Option<T> {
	
	public enum OptionType {STRING, BOOLEAN}
	
	public String getOptionName();
	
	public String getOptionValueAsString();
	
	public OptionType getOptionType();

}
