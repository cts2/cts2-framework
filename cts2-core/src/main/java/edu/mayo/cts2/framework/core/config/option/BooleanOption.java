package edu.mayo.cts2.framework.core.config.option;


public class BooleanOption extends AbstractOption<Boolean>{

	public BooleanOption(String optionName, boolean optionValue) {
		super(optionName, optionValue, OptionType.BOOLEAN);
	}

	@Override
	public String optionValueToString(Boolean optionValue) {
		return Boolean.toString(optionValue);
	}

	@Override
	public Boolean stringToOptionValue(String optionAsString) {
		return Boolean.parseBoolean(optionAsString);
	}
	
}
