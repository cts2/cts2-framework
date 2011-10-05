package edu.mayo.cts2.framework.core.config.option;

public class AbstractOption<T> {
	
	private String optionName;
	
	private T optionValue;
	
	protected AbstractOption(String optionName, T optionValue){
		this.optionName = optionName;
		this.optionValue = optionValue;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public T getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(T optionValue) {
		this.optionValue = optionValue;
	}
}
