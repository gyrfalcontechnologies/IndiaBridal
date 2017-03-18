package in.indiaBridal.UtilityClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.indiaBridal.commands.InsertOtherDataCommand;

public class ReadJSONObject
{
	final static Logger logger = Logger.getLogger(ReadJSONObject.class);
	
	public static void main(String[] args)
	{
		
		String profileJSONString = "[{\"memberID\": \"1717171\",\"currPage\": \"lifeStyle\",\"userData\":{	\"smoker\": {\"isOthers\": \"false\",\"value\": \"no\"},\"drinker\": {"
				+"\"isOthers\": \"false\",\"value\": \"any\"},\"FoodTypeID\":{\"isOthers\": \"false\",\"value\": \"2\"}}}]";

		String partnerPrefJSONString = "[{\"memberID\": \"1717171\",\"currPage\": \"partnerPref\",\"userData\": {\"age_LL\": [{\"isOthers\": \"false\",	\"value\": \"18\"}],"
			+"\"age_UL\":[{\"isOthers\": \"false\",\"value\": \"24\"}],"
			+"\"height_LL\":[{\"isOthers\": \"false\",\"value\": \"165\"}],"
			+"\"height_UL\":[{\"isOthers\": \"false\",\"value\": \"175\"}],"
			+"\"weight_LL\":[{\"isOthers\": \"false\",\"value\": \"50\"}],"
			+"\"weight_UL\":[{\"isOthers\": \"false\",\"value\": \"60\"}],"
			+"\"income_LL\":[{\"isOthers\": \"false\",\"value\": \"4\"}],"
			+"\"income_UL\":[{\"isOthers\": \"false\",\"value\": \"6\"}],"
			+"\"expectation\":[{\"isOthers\": \"false\",\"value\": \"SOME COMMENTS\"}],"
			+"\"dosham\": [{\"isOthers\": \"false\",\"value\": \"no\"}],"
			+"\"relationShipID\":[{\"isOthers\": \"false\",\"value\": \"NEVER MARRIED\"},"
			+"{\"isOthers\": \"false\",\"value\": \"divorced\"}],"
			+"\"smoker\":[{\"isOthers\": \"false\",\"value\": \"NO\"}],"
			+"\"drinker\":[{\"isOthers\": \"false\",\"value\": \"NO\"	},"
			+"{\"isOthers\": \"false\",\"value\": \"ocassionally\"}],"
			+"\"foodtypeid\":[{\"isOthers\": \"false\",\"value\": \"VEG\"},"
			+"{\"isOthers\": \"false\",\"value\": \"EGGETERIAN\"}],"
			+"\"professionID\":[{\"isOthers\": \"false\",\"value\": \"BANKER\"},"
			+"{\"isOthers\": \"false\",\"value\": \"TEACHER\"}],"
			+"\"qualificationID\":[{\"isOthers\": \"false\",\"value\": \"BACHELOURS\"}],"
			+"\"locationCoStCt\":[{\"isOthers\": \"false\",\"value\": \"India,TamilNadu,Chennai\"},"
			+"{\"isOthers\": \"false\",\"value\": \"India,TamilNadu,Thiruvallur\"}],"
			+"\"religionID\":[{\"isOthers\": \"false\",\"value\": \"HINDU\"}],"
			+"\"languageID\":[{\"isOthers\": \"false\",\"value\": \"TELUGU\"}],"
			+"\"casteID\":[{\"isOthers\": \"false\",\"value\": \"NAIDU\"}],"
			+"\"subCasteID\":[{\"isOthers\": \"false\",\"value\": \"KAMMA\"}],"
			+"\"raasiID\":[{\"isOthers\": \"false\",\"value\": \"MIDUNAM\"},"
			+"{\"isOthers\": \"false\",\"value\": \"simha\"}],"
			+"\"starID\":[{\"isOthers\": \"false\",\"value\": \"MIRUGA\"}],"
			+"\"gothramID\":[{\"isOthers\": \"false\",\"value\": \"any\"}]"
			+"}}]";
		
		ReadJSONObject object = new ReadJSONObject();
		String testFileName = "uPatnerPref.txt";
		String sampleJSON = object.readFile("WebContent\\TestJSON\\"+testFileName);
		logger.debug("OUT FOR PART PREF "+object.readPartnerPrefJSONArr(sampleJSON));
		//logger.debug("out => "+object.readProfileJSONArr(profileJSONString));
		//logger.debug("out => "+object.readPartnerPrefJSONArr(partnerPrefJSONString));
	}
	public String readFile(String filename) {
	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	/*
	 * Method Name: readGenericJSONArr will help to the memeber id and currPage info from JSON data.
	 */
	public HashMap<String, String> readGenricJSONArr(String jsonRequestObj)
	{
		logger.debug("reading JSON OBJECT = "+jsonRequestObj);
		HashMap<String, String> jsonMap = new HashMap<String, String>();
		JSONArray jsonArray;
		try 
		{
			jsonArray = new JSONArray(jsonRequestObj);
			JSONObject jsonPersonData = jsonArray.getJSONObject(0);

			String memberID = (String) jsonPersonData.get("memberID");
			String currPage = (String) jsonPersonData.get("currPage");
			jsonMap.put("memberID", memberID);
			jsonMap.put("currPage", currPage);
			jsonMap.put("status", "SUCCESS");
			
		}
		catch (JSONException e)
		{
			logger.debug(" JSONException OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
			jsonMap.put("status", "FAILURE");
		}
		return jsonMap;
	}
	
	/*
	 * read the JSON and get the values for each parameters.
	 * */
	public HashMap<String, Object> readProfileJSONArr(String jsonProfileObj)
	{
		logger.debug("JSON array will be read here... ");
		logger.debug("if isothers flag is enabled for any data, then that will be pushed to respective table in db");
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		HashMap<String, String> userDataMap = new HashMap<String, String>();
		try 
		{
			JSONArray jsonArray = new JSONArray(jsonProfileObj);
			JSONObject jsonPersonData = jsonArray.getJSONObject(0);

			String memberID = (String) jsonPersonData.get("memberID");
			String currPage = (String) jsonPersonData.get("currPage");
			logger.debug("memberID = "+memberID+" currPage = "+currPage);
			jsonMap.put("memberID", memberID);
			jsonMap.put("currPage", currPage);
			Object usrDataObj =  jsonPersonData.get("userData");
			if (usrDataObj instanceof JSONObject )
			{
				String usrDatakeyNames[] = JSONObject.getNames((JSONObject)usrDataObj);
				for(int usrDatacount=0;usrDatacount<usrDatakeyNames.length;usrDatacount++)
				{
					Object usrSubDataObj =   ((JSONObject) usrDataObj).get(usrDatakeyNames[usrDatacount]);
					if (usrSubDataObj instanceof JSONObject )
					{
						String isOthers = ((JSONObject) usrSubDataObj).getString("isOthers");
						if("false".equalsIgnoreCase(isOthers))
						{
							String currVal = ((JSONObject) usrSubDataObj).getString("value");
							String fieldName = usrDatakeyNames[usrDatacount];
							userDataMap.put(fieldName, currVal);
							logger.debug("memberID = "+memberID+" currPage = "+currPage+" subkeyNames = "+usrDatakeyNames[usrDatacount]+" ---> "+currVal);
						}
						else
						{
							logger.debug("other data flag enabled ");
							String fieldName = usrDatakeyNames[usrDatacount];
							String currVal = ((JSONObject) usrSubDataObj).getString("value");
							logger.debug("field name = "+fieldName+" other value = "+currVal);
							logger.debug("calling InsertOtherDataCommand class to insert other data ");
							InsertOtherDataCommand insertOtherObj = new InsertOtherDataCommand();
							HashMap retMap = insertOtherObj.getResponseHashMap(memberID, currPage, fieldName, currVal);
							String status = (String) retMap.get("status");
							logger.debug("InsertOtherDataCommand completed with status = "+status);
							if("success".equalsIgnoreCase(status))
							{
								String isUpdateSuccess = (String) retMap.get("isUpdateSuccess");
								logger.debug("InsertOtherDataCommand isUpdateSuccess = "+isUpdateSuccess);
								if("true".equalsIgnoreCase(isUpdateSuccess))
								{
									String currID = (String) retMap.get("id");
									logger.debug("memberID = "+memberID+" currPage = "+currPage+" | Inserting other data SUCCESS for fieldName = "+fieldName+" current val = "+currVal+" RETURNED ID = "+currID);
									userDataMap.put(fieldName, currID);
								}
								else
								{
									logger.debug("memberID = "+memberID+" currPage = "+currPage+"| updating other data failed for fieldName = "+fieldName+" current val = "+currVal);
									return null;
								}
							}
							else
							{
								logger.debug("memberID = "+memberID+" currPage = "+currPage+" Inserting other data failed for fieldName = "+fieldName+" current val = "+currVal);
								return null;
							}
						}
					}
					else
					{
						logger.debug("memberID = "+memberID+" currPage = "+currPage+ "usrSubDataObj "+usrSubDataObj+" NOT A VALID JSON OBJECT");
						return null;
					}
				}
				jsonMap.put("userDataMap", userDataMap);
				return jsonMap;
			}
			else
			{
				logger.debug("memberID = "+memberID+" currPage = "+currPage+ "NO USER DATA FOUND - EXIT");
				return null;
			}
		} 
		catch (JSONException e)
		{
			logger.debug(" JSONException OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		}
		return null;
	}
	
	
	public HashMap<String, Object> readPartnerPrefJSONArr(String partnerPrefJSON)
	{
		logger.debug("partnerPrefJSON = "+partnerPrefJSON);
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		HashMap<String, Object> userDataMap = new HashMap<String, Object>();
		
		JSONArray jsonArray;
		try 
		{
			jsonArray = new JSONArray(partnerPrefJSON);
			JSONObject jsonPersonData = jsonArray.getJSONObject(0);

			String memberID = (String) jsonPersonData.get("memberID");
			String currPage = (String) jsonPersonData.get("currPage");
			logger.debug("memberID = "+memberID+" currPage = "+currPage);
			jsonMap.put("memberID", memberID);
			jsonMap.put("currPage", currPage);
			Object usrDataObj =  jsonPersonData.get("userData");
			if (usrDataObj instanceof JSONObject )
			{
				String usrDatakeyNames[] = JSONObject.getNames((JSONObject)usrDataObj);
				for(int usrDatacount=0;usrDatacount<usrDatakeyNames.length;usrDatacount++)
				{
					Object usrSubDataObj =   ((JSONObject) usrDataObj).get(usrDatakeyNames[usrDatacount]);
					if (usrSubDataObj instanceof JSONArray )
					{
						ArrayList multiList = new ArrayList();
						JSONArray usrPrefArr = (JSONArray) usrSubDataObj;
						for(int prefCount =0; prefCount<usrPrefArr.length();prefCount++)
						{
							JSONObject prefUserData = usrPrefArr.getJSONObject(prefCount);

							String isOthers = ((JSONObject) prefUserData).getString("isOthers");
							if("false".equalsIgnoreCase(isOthers))
							{
								String currVal = ((JSONObject) prefUserData).getString("value");
								String fieldName = usrDatakeyNames[usrDatacount];
								multiList.add(currVal);
								logger.debug("memberID = "+memberID+" currPage = "+currPage+" subkeyNames = "+usrDatakeyNames[usrDatacount]+" ---> "+currVal);
							}
							else
							{
								String fieldName = usrDatakeyNames[usrDatacount];
								String currVal = ((JSONObject) prefUserData).getString("value");
								InsertOtherDataCommand insertOtherObj = new InsertOtherDataCommand();
								HashMap retMap = insertOtherObj.getResponseHashMap(memberID, currPage, fieldName, currVal);
								String status = (String) retMap.get("status");
								if("success".equalsIgnoreCase(status))
								{
									String isUpdateSuccess = (String) retMap.get("isUpdateSuccess");
									if("true".equalsIgnoreCase(isUpdateSuccess))
									{
										String currID = (String) retMap.get("id");
										logger.debug("memberID = "+memberID+" currPage = "+currPage+" Inserting other data SUCCESS for fieldName = "+fieldName+" current val = "+currVal+" RETURNED ID = "+currID);
										multiList.add(currID);
									}
									else
									{
										logger.debug("memberID = "+memberID+" currPage = "+currPage+" updating other data failed for fieldName = "+fieldName+" current val = "+currVal);
										return null;
									}
								}
								else
								{
									logger.debug("memberID = "+memberID+" currPage = "+currPage+" Inserting other data failed for fieldName = "+fieldName+" current val = "+currVal);
									return null;
								}
							}
						
						}
						userDataMap.put(usrDatakeyNames[usrDatacount], multiList);
					}
					else
					{
						logger.debug("memberID = "+memberID+" currPage = "+currPage+ "usrSubDataObj "+usrSubDataObj+" NOT A VALID JSON OBJECT");
						return null;
					}
				}
				jsonMap.put("userDataMap", userDataMap);
				return jsonMap;
			}
			else
			{
				logger.debug("memberID = "+memberID+" currPage = "+currPage+ "NO USER DATA FOUND - EXIT");
				return null;
			}
		} 
		catch (JSONException e)
		{
			logger.debug(" JSONException OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		}
		return null;
	}
	
}
