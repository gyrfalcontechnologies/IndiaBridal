package in.indiaBridal.UtilityClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Utilities 
{
	public static boolean isNullOrEmpty(String value)
	{
		if(value!=null && !value.isEmpty())
			return false;
		return true;
	}
	public static boolean isNullOrEmpty(HashMap map)
	{
		if(map!=null && !map.isEmpty())
			return false;
		return true;
	}
	public static String stripLastChars(String newStr, String charsToRemove)
	{
		String strippedOut = "";
		if(newStr.endsWith(charsToRemove))
		{
			int lastInd = newStr.lastIndexOf(charsToRemove);
			strippedOut = newStr.substring(0,lastInd);
		}
		else
			strippedOut = newStr;

		return strippedOut;
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
	
	public static String getCurrPrefCond(HashSet<String> currPrefSet,String encloseChar) 
	{
		return checkForNullEmptyValHashSet(currPrefSet, encloseChar);
	}

	public static String checkForNullEmptyValHashSet(HashSet<String> currHashSet, String encloseChar)
	{
		String whereCond = "";
		if(currHashSet!=null && !currHashSet.isEmpty())
		{
			Iterator<String> itr = currHashSet.iterator();
			while(itr.hasNext())
			{
				String currVal = itr.next();
				if(currVal!=null && !currVal.trim().isEmpty())
					whereCond = whereCond+encloseChar+currVal+encloseChar+",";

			}
		}
		return Utilities.stripLastChars(whereCond.toString(),",");
	}
}
