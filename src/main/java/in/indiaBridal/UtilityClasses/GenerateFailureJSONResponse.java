package in.indiaBridal.UtilityClasses;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenerateFailureJSONResponse 
{
	final static Logger logger = Logger.getLogger(GenerateFailureJSONResponse.class);
	public JSONArray generateFailedProfileCreation(String errorMsg)
	{
		logger.debug("error message = "+errorMsg);
		HashMap jsonHashMap = new HashMap();
		jsonHashMap.put("status", "failure");
		jsonHashMap.put("errorMsg", errorMsg);
		jsonHashMap.put("isProfileCreated", "false");
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(jsonHashMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		return retJSONArr;
	}
	
	public JSONArray generateFailedRegForm(String errorMsg)
	{
		logger.debug("generating failure response for register form. errorMsg = "+errorMsg);
		HashMap jsonHashMap = new HashMap();
		jsonHashMap.put("status", "failure");
		jsonHashMap.put("errorMsg", errorMsg);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(jsonHashMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		return retJSONArr;
	}
}
