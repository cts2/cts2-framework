package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.config.option.StringOption;

public class ConfigUtils {
	
	protected static OptionHolder propertiesToOptionHolder(Properties properties){
		Set<StringOption> stringOptions = new HashSet<StringOption>();
		
		for(Entry<Object, Object> entry : properties.entrySet()){
			
			String name = new String((String) entry.getKey());
			String value = new String((String) entry.getValue());
			
			stringOptions.add(new StringOption(name, value));
		}
		
		OptionHolder holder = new OptionHolder(stringOptions);
		
		return holder;
	}
	
	protected static File createDirectory(String path){
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return file;
	}
	
	protected static File createFile(String path){
		File file = new File(path);
		createDirectory(file.getParent());
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return file;
	}
	
	protected static Properties loadProperties(String filePath) {
		File file = new File(filePath);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		return loadProperties(file);
	}
	/**
	 * Do load properties.
	 *
	 * @param file the file
	 * @return the properties
	 */
	protected static Properties loadProperties(File file) {
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			props.load(fis);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		return props;
	}

	protected static void setProperty(String propertyName, String propertyValue, File propsFile ) {
		
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(propsFile);
			if(! config.containsKey(propertyName)){
				config = new PropertiesConfiguration(propsFile);
				if(!config.containsKey(propertyName)){
					throw new RuntimeException("Property: " + propertyName + " not found.");
				}
			}
		} catch (ConfigurationException e) {
			throw new IllegalStateException(e);
		}
		
		config.setProperty(propertyName, propertyValue);
	
		try {
			config.save();
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
}
