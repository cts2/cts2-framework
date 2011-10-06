package edu.mayo.cts2.framework.core.config.option;


public class StringOption extends AbstractOption<String>{

	public StringOption(String optionName, String optionValue) {
		super(optionName, optionValue, OptionType.STRING);
	}

	@Override
	public String optionValueToString(String optionValue) {
		return optionValue;
	}

	@Override
	public String stringToOptionValue(String optionAsString) {
		return optionAsString;
	}
}
