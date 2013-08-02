package edu.mayo.cts2.framework.core.config.option;

public abstract class AbstractOption<T> implements Option {
	
	private String optionName;
	
	private String optionValue;
	
	private OptionType optionType;

	protected AbstractOption(String optionName, T optionValue, OptionType optionType){
		this.optionName = optionName;
		this.optionValue = this.optionValueToString(optionValue);
		
		this.optionType = optionType;
	}

	@Override
	public String getOptionValueAsString() {
		return this.optionValue;
	}

	protected abstract String optionValueToString(T optionValue);

	protected abstract T stringToOptionValue(String optionValue);
	
	public String getOptionName() {
		return optionName;
	}

	public T getOptionValue() {
		return this.stringToOptionValue(optionValue);
	}

	public OptionType getOptionType() {
		return optionType;
	}
}
