
package com.XRewrite;


/**
 * This class holds all configuration data
 */
public class Configuration
{
	private static Configuration configuration;

	public static Configuration getInstance(){
		if(configuration==null)
			configuration = new Configuration();
		return configuration;
	}

	private boolean elimination = true;
	private boolean enableCommandline = true;
	private boolean enableShowTime = true;
	private boolean enableSubsume = true;

	public boolean isEnableSubsume() {
		return enableSubsume;
	}

	public void setEnableSubsume(boolean enableSubsume) {
		this.enableSubsume = enableSubsume;
	}

	public boolean isEnableShowTime() {
		return enableShowTime;
	}

	public void setEnableShowTime(boolean enableShowTime) {
		this.enableShowTime = enableShowTime;
	}

	public boolean isElimination() {
		return elimination;
	}

	public void setElimination(boolean elimination) {
		this.elimination = elimination;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(Configuration configuration) {
		Configuration.configuration = configuration;
	}

	public boolean isEnableCommandline() {
		return enableCommandline;
	}

	public void setEnableCommandline(boolean enableCommandline) {
		this.enableCommandline = enableCommandline;
	}
}
