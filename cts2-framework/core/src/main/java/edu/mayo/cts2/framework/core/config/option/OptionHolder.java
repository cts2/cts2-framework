package edu.mayo.cts2.framework.core.config.option;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class OptionHolder {

	private Map<String,AbstractOption<?>> options = 
			new HashMap<String,AbstractOption<?>>();
	
	public OptionHolder(Set<StringOption> stringOptions){
		if(CollectionUtils.isNotEmpty(stringOptions)){
			for(StringOption option : stringOptions){
				this.options.put(option.getOptionName(), option);
			}
		}
	}
	public StringOption getStringOption(String optionName){
		StringOption option = (StringOption) options.get(optionName);
		
		if(option == null){
			option = new StringOption(optionName, null);
		}
		
		return option;
	}
}
