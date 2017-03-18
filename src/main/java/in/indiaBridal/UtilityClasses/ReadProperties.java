package in.indiaBridal.UtilityClasses;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ReadProperties 
{
	InputStream input = null;
	private final static Logger logger = Logger.getLogger(ReadProperties.class);
	public String readProperty(String key)
	{
		logger.debug("reading property for "+key);
		Properties prop = new Properties();
		try
		{
			input = getClass().getClassLoader().getResourceAsStream("indiaBridal.properties");
			prop.load(input);
			logger.debug("DONE reading property");
			return prop.getProperty(key);
		} 
		catch (IOException e)
		{
			logger.debug("reading property for "+key+" failed. IO EXCEPTION OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("IO Exception OCCURRED statck trace = "+stack);
		} 
		finally
		{
			if (input != null)
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public HashMap readMultiProperties(ArrayList keyList)
	{
		HashMap retMap = new HashMap();
		Properties prop = new Properties();
		try
		{
			input = getClass().getClassLoader().getResourceAsStream("indiaBridal.properties");
			logger.debug("input = "+input);
			// load a properties file
			prop.load(input);
			
			if(keyList!=null && !keyList.isEmpty())
			{
				String key = "";
				String value = "";
				for(int count=0;count<keyList.size();count++)
				{
					key = (String) keyList.get(count);
					if(key!=null && !key.isEmpty())
					{
						value = prop.getProperty(key);
						logger.debug(key+"  = "+value);
						retMap.put(key, value);
					}
				}
			}
			return retMap;
		} 
		catch (IOException e)
		{
			logger.debug("failed reading multi properties. IO EXCEPTION OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("IO Exception OCCURRED statck trace = "+stack);
		} 
		finally
		{
			if (input != null)
			{
				try 
				{
					input.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}