package in.indiaBridal.UtilityClasses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HashMapToJSONObjects
{
	final static Logger logger = Logger.getLogger(HashMapToJSONObjects.class);
	public JSONObject hashMapToJSONObj(HashMap<String, Object> resultMap)
	{
		JSONObject dataObj = new JSONObject();
		if(resultMap!=null)
		{
			Set<String> keySet = resultMap.keySet();
			Iterator<String> keySetItr = keySet.iterator();
			while(keySetItr.hasNext())
			{
				String key = keySetItr.next().toString();
				try 
				{
					dataObj.put(key, (String)resultMap.get(key));
				} 
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
		return dataObj;
	}
	
	public JSONArray jsonObjToJSONArr(JSONObject dataObj)
	{
		JSONArray retJSONArr = new JSONArray();
		if(dataObj!=null)
		{
			retJSONArr = new JSONArray();
			retJSONArr.put(dataObj);
		}
		return retJSONArr;
	}
	
	public JSONObject nestedHMapToJSONObj(HashMap<String, Object> resultMap)
	{
		JSONObject dataObj = new JSONObject();
		if(resultMap!=null)
		{
			Set<String> keySet = resultMap.keySet();
			Iterator<String> keySetItr = keySet.iterator();
			while(keySetItr.hasNext())
			{
				String key = keySetItr.next();
				Object currVal = resultMap.get(key);
				try
				{
					if (currVal instanceof HashMap )
					{
						logger.debug("key = "+key);
						dataObj.put(key,nestedHMapToJSONObj((HashMap<String, Object>) currVal));
					}
					else
					{
						logger.debug("key = "+key+" value = "+resultMap.get(key));
						dataObj.put(key, (String)resultMap.get(key));
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
		return dataObj;
	}
}
