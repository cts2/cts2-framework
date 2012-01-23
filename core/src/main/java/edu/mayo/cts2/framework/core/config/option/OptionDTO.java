package edu.mayo.cts2.framework.core.config.option;

public class OptionDTO extends AbstractOption<String>{

	public OptionDTO(
			String optionName, 
			String optionValue,
			OptionType optionType) {
		super(optionName, optionValue, optionType);
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
