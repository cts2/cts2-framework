package edu.mayo.cts2.framework.core.config.option;


public class StringOption extends AbstractOption<String>{
	
	public boolean isPassword = false;

	public StringOption(String optionName, String optionValue) {
		this(optionName, optionValue, false);
	}
	
	public StringOption(String optionName, String optionValue, boolean isPassword) {
		super(optionName, optionValue, OptionType.STRING);
		this.isPassword = isPassword;
	}

	@Override
	public String optionValueToString(String optionValue) {
		return optionValue;
	}

	@Override
	public String stringToOptionValue(String optionAsString) {
		return optionAsString;
	}
	
	public boolean isPassword(){
		return this.isPassword;
	}
}
