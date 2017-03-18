package in.indiaBridal.UtilityClasses;

import org.apache.log4j.Logger;

public class Env {
	private final static Logger logger = Logger.getLogger(Env.class);
	
	public static String getEnv()
	{
		logger.debug("reading property for current environment");
		ReadProperties readPropObj = new ReadProperties();
		String currEnv = readPropObj.readProperty("env");
		if(currEnv!=null && !currEnv.isEmpty())
			return currEnv;
		else
			return "DEV";
	}
}
