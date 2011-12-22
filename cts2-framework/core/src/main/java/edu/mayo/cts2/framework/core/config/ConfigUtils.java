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
	
	public static OptionHolder propertiesToOptionHolder(Properties properties){
		Set<StringOption> stringOptions = new HashSet<StringOption>();
		
		for(Entry<Object, Object> entry : properties.entrySet()){
			
			String name = new String((String) entry.getKey());
			String value = new String((String) entry.getValue());
			
			stringOptions.add(new StringOption(name, value));
		}
		
		OptionHolder holder = new OptionHolder(stringOptions);
		
		return holder;
	}
	
	public static File createDirectory(String path){
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return file;
	}
	
	public static File createFile(String path){
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
	public static Properties loadProperties(File file) {
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
	
	protected static void addProperty(String propertyName, String propertyValue, File propsFile) {
		doModifyPropertiesFile(propertyName, propertyValue, propsFile, PropertiesModifyAction.FAIL_IF_FOUND);
	}
	
	protected static void addOrUpdateProperty(String propertyName, String propertyValue, File propsFile) {
		doModifyPropertiesFile(propertyName, propertyValue, propsFile, PropertiesModifyAction.ADD_OR_UPDATE);
	}
	
	protected static void addPropertyIfNotFound(String propertyName, String propertyValue, File propsFile) {
		doModifyPropertiesFile(propertyName, propertyValue, propsFile, PropertiesModifyAction.ADD_IF_NOT_FOUND);
	}
	
	public static void updateProperty(String propertyName, String propertyValue, File propsFile) {
		doModifyPropertiesFile(propertyName, propertyValue, propsFile, PropertiesModifyAction.FAIL_IF_NOT_FOUND);
	}

	private enum PropertiesModifyAction {FAIL_IF_FOUND, FAIL_IF_NOT_FOUND, ADD_OR_UPDATE, ADD_IF_NOT_FOUND}
	
	private static void doModifyPropertiesFile(
			String propertyName, 
			String propertyValue, 
			File propsFile,
			PropertiesModifyAction action) {

		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(propsFile);

			boolean exists = config.containsKey(propertyName);
			
			if(exists && action.equals(PropertiesModifyAction.FAIL_IF_FOUND)){
				throw new RuntimeException("Property: " + propertyName + " already exists. It cannot be added.");
			}
			
			if(!exists && action.equals(PropertiesModifyAction.FAIL_IF_NOT_FOUND)){
				throw new RuntimeException("Property: " + propertyName + " does not exists. It cannot be updated.");
			}
				
			//if its already there, don't overwrite it
			if(exists && action.equals(PropertiesModifyAction.ADD_IF_NOT_FOUND)){
				return;
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
