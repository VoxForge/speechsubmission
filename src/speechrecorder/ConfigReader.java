package speechrecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;


/**
 * 
 *  Reads and writes configuration files.
 *  
    Copyright (C) 2014  Joseph Lewis

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 * 
 * @author Joseph Lewis <joehms22@gmail.com>

 */
public class ConfigReader
{
	static ConfigReader myCR;
	public Properties m_properties;
	private final File settingsFile;
	
	public ConfigReader(File settingsFile) throws IOException
	{		
		this.settingsFile = settingsFile;
		
		if(! settingsFile.exists())
		{
				try{
					settingsFile.getParentFile().mkdirs();
				}catch(Exception e)
				{}
				
				settingsFile.createNewFile();
		}
		
		m_properties = new Properties();
		FileReader r = null;
		try
		{
			r = new FileReader(settingsFile);
			m_properties.load(r);
		} catch (IOException e)
		{
		} finally {
			try
			{
				if(r != null)
					r.close();
			} catch (IOException e)
			{
				System.err.println("Error, couldn't close stream to: " + settingsFile.getPath());
			}
		}
	}
	
	/**
	 * Gets the int at the given property.
	 * @param pname -- The name of the property to fetch.
	 * @param defaultValue -- The default value, if one doesn't exist in the file.
	 * @return
	 */
	public int getInt(String pname, int defaultValue)
	{
		String p = m_properties.getProperty(pname);
		
		try {
			return Integer.parseInt(p);
		} catch(Exception e) {
			put(pname, defaultValue);
			return defaultValue;
		}
	}
	
	/**
	 * Gets the long at the given property.
	 * @param pname -- The name of the property to fetch.
	 * @param defaultValue -- The default value, if one doesn't exist in the file.
	 * @return
	 */
	public long getLong(String pname, long defaultValue)
	{
		String p = m_properties.getProperty(pname);
		
		try {
			return Long.parseLong(p);
		} catch(Exception e) {
			put(pname, defaultValue);
			return defaultValue;
		}
	}
	
	
	/**
	 * Gets the string at the given property.
	 * @param pname -- The name of the property to fetch.
	 * @param defaultValue -- The default value, if one doesn't exist in the file.
	 * @return
	 */
	public String getString(String pname, String defaultValue)
	{
		String p = m_properties.getProperty(pname);
		
		if(p != null)
			return p;
		
		put(pname, defaultValue);
		return defaultValue;
	}
	
	/**
	 * Stores the given object at the given location in the configuration
	 * @param pname - the path for the object
	 * @param pvalue - the object to store in the properties file
	 * @return true if config is saved, false if it is not.
	 */
	public boolean put(String pname, Object pvalue)
	{
		m_properties.put(pname, pvalue.toString());

		//try(FileWriter f = new FileWriter(settingsFile)) // needs Java 1.7
		//{
		try {		
			//FileWriter f = new FileWriter(settingsFile);
			FileOutputStream f = new FileOutputStream(settingsFile);		
			//m_properties.store(f, "Config File");
			m_properties.store(f, "Config File");			
		} catch (IOException e) 
		{
			return false;
		}
		
		return true;
	}
	
	
	public String getAll()
	{
		StringBuilder sb = new StringBuilder();
		for(Entry<Object, Object> objs : m_properties.entrySet())
		{
			sb.append(objs.getKey());
			sb.append("\t");
			sb.append(objs.getValue());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
