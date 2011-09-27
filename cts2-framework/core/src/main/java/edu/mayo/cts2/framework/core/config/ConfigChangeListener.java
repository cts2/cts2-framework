/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The listener interface for receiving configChange events.
 * The class that is interested in processing a configChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addConfigChangeListener<code> method. When
 * the configChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ConfigChangeEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ConfigChangeListener implements ConfigChangeObservable {
	
	private Long contextConfigFileLastChange;
	private Long pluginDirLastChange;
	
	private int fileCheckSeconds = 20;
	
	private File pluginsDir;
	private File contextConfigFile;
	
	private Set<ConfigChangeObserver> observers = new HashSet<ConfigChangeObserver>();
	
	/**
	 * Instantiates a new config change listener.
	 *
	 * @param pluginsDir the plugins dir
	 * @param contextConfigFile the context config file
	 */
	protected ConfigChangeListener(File pluginsDir, File contextConfigFile){
		this.pluginsDir = pluginsDir;
		this.contextConfigFile = contextConfigFile;
	}
	
	/**
	 * Start.
	 */
	public void start(){
	
		this.startTimer(pluginsDir, new LastUpdateTime(){

			public void setLastUpdateTime(long time) {
				contextConfigFileLastChange = time;
			}

			public Long getLastUpdateTime() {
				return contextConfigFileLastChange; 
			}
			
		}, 0, 1000L * fileCheckSeconds);
		
		this.startTimer(contextConfigFile, new LastUpdateTime(){

			public void setLastUpdateTime(long time) {
				pluginDirLastChange = time;
			}

			public Long getLastUpdateTime() {
				return pluginDirLastChange; 
			}
			
		}, 0, 1000L * fileCheckSeconds);
	}

	/**
	 * The Interface LastUpdateTime.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private interface LastUpdateTime {
		public void setLastUpdateTime(long time);
		public Long getLastUpdateTime();
	}
	
	/**
	 * Start timer.
	 *
	 * @param file the file
	 * @param lastUpdateTimeBean the last update time bean
	 * @param start the start
	 * @param period the period
	 */
	protected void startTimer(final File file, final LastUpdateTime lastUpdateTimeBean, long start, long period){
		Timer contextConfigFileTimer = new Timer();
		contextConfigFileTimer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				long lastUpdateTime = file.lastModified();
				Long lastRecordedUpdateTime = lastUpdateTimeBean.getLastUpdateTime();
				if(lastRecordedUpdateTime == null){
					lastUpdateTimeBean.setLastUpdateTime(lastUpdateTime);
				} else {
					if(lastUpdateTime > lastRecordedUpdateTime){
						lastUpdateTimeBean.setLastUpdateTime(lastUpdateTime);
						configFileChange();
					}
				}
			}
			
		}, start, period);
	}
	
	/**
	 * Config file change.
	 */
	protected void configFileChange(){
		for(ConfigChangeObserver observer : this.observers){
			observer.onContextPropertiesFileChange();
		}
	}
	
	/**
	 * Plugin dir change.
	 */
	protected void pluginDirChange(){
		for(ConfigChangeObserver observer : this.observers){
			observer.onPluginsDirectoryChange();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.sdk.core.config.ConfigChangeObservable#registerListener(edu.mayo.cts2.sdk.core.config.ConfigChangeObserver)
	 */
	public void registerListener(ConfigChangeObserver observer) {
		observers.add(observer);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.sdk.core.config.ConfigChangeObservable#unregisterListener(edu.mayo.cts2.sdk.core.config.ConfigChangeObserver)
	 */
	public void unregisterListener(ConfigChangeObserver observer) {
		observers.remove(observer);
	}

}
