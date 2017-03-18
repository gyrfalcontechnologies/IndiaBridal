package in.indiaBridal.commands.getProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;

public class GetPreferencesInfoCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetPreferencesInfoCommand.class);
	ArrayList<String> keyList = new ArrayList<String>();
	HashMap<String, HashSet<String>> usrPDetails = new HashMap<String, HashSet<String>>();

	public static void main(String[] args) {
		GetPreferencesInfoCommand obj = new GetPreferencesInfoCommand();
		System.out.println(obj.getResponseJSON("IBM10000004"));
	}
	
	public HashMap<String, Object> getResponseJSON(String memberID)
	{
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, Object> nextPageResMap = getResponseHashMap(memberID);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		if(!Utilities.isNullOrEmpty(nextPageResMap))
		{
			if("success".equalsIgnoreCase((String) nextPageResMap.get("status")))
			{
				HashMap navPageData = (HashMap) nextPageResMap.get("navPageData");
				logger.debug("nav page data = "+navPageData);
				if(!Utilities.isNullOrEmpty(navPageData))
				{
					responseMap.put("navPage", "partPrefInfo");
					responseMap.put("isnavPageDataAvailable", "true");
					responseMap.put("navPageData", navPageData);
					JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
					JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
					returnMap.put("status", "success");
					returnMap.put("errorMsg", "success");
					returnMap.put("jsonArr", retJSONArr);
				}
				else
				{
					responseMap.put("navPage", "partPrefInfo");
					responseMap.put("isnavPageDataAvailable", "false");
					responseMap.put("navPageData", "");
					JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
					JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
					returnMap.put("status", "success");
					returnMap.put("errorMsg", "success");
					returnMap.put("jsonArr", retJSONArr);
				}
			}
			else
			{
				responseMap.put("navPage", "partPrefInfo");
				responseMap.put("isnavPageDataAvailable", "false");
				responseMap.put("navPageData", "");
				JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
				JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("jsonArr", retJSONArr);
			}
		}
		else
		{
			responseMap.put("navPage", "partPrefInfo");
			responseMap.put("isnavPageDataAvailable", "false");
			responseMap.put("navPageData", "");
			JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
			returnMap.put("status", "success");
			returnMap.put("errorMsg", "success");
			returnMap.put("jsonArr", retJSONArr);
		}
		return returnMap;
	}
	
	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		keyList.add("martialStatus");
		keyList.add("foodType");
		keyList.add("education");
		keyList.add("profession");
		keyList.add("country");
		keyList.add("state");
		keyList.add("city");
		keyList.add("religion");
		keyList.add("language");
		keyList.add("caste");
		keyList.add("subCaste");
		keyList.add("raasi");
		keyList.add("star");
		keyList.add("gothram");

		logger.debug("key list to hit different tables set");

		GetUserPartnerPrefDetailsCommand getUsrpDetObj = new GetUserPartnerPrefDetailsCommand();
		HashMap<String, Object> usrPDetResMap = getUsrpDetObj.getResponseHashMap(memberID);

		if(!Utilities.isNullOrEmpty(usrPDetResMap))
		{
			if("success".equalsIgnoreCase((String) usrPDetResMap.get("status")))
			{
				usrPDetails = (HashMap<String, HashSet<String>>) usrPDetResMap.get("userData");
				HashMap<String, Object> responseMap = generateDataResponse();
				logger.debug("data received for all tables: response map = "+responseMap);
				if(!Utilities.isNullOrEmpty(responseMap))
				{
					logger.debug("response size and key size are equal");
					returnMap.put("status", "success");
					returnMap.put("errorMsg", "success");
					returnMap.put("navPageData", responseMap);
					logger.debug("navpage data = "+responseMap);
				}
				else
				{
					logger.debug("response map is null or empty.");
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
				}
			}
		}
		else
		{
			logger.debug("user details map is null or empty.");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE FROM USER B-DETAILS");
		}

		logger.debug("returnMap = "+returnMap);
		return returnMap;
	}

	public HashMap<String, Object> generateDataResponse()
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		logger.debug("iterate over keys to get the data from respective tables.");
		for(int count=0;count<keyList.size();count++)
		{
			String currKey = keyList.get(count);
			logger.debug("curr key = "+currKey);
			HashMap<String, Object> dataMap = getDataFromDB(currKey);
			if(!Utilities.isNullOrEmpty(dataMap))
			{
				returnMap.put(currKey, dataMap);
				System.out.println("currKey = "+currKey);
				System.out.println("dataMap = "+dataMap);
			}
			else
			{
				System.out.println("currKey = "+currKey);
				System.out.println("dataMap = "+dataMap);
			}
		}
		Set<String> userDataRemKeys = usrPDetails.keySet();
		Iterator<String> keyIter = userDataRemKeys.iterator();
		while(keyIter.hasNext())
		{
			String currKey = keyIter.next();
			System.out.println("currKey in itr = "+currKey);
			HashMap<String, Object> dataMap = generateDummyVals(currKey);
			System.out.println("dataMap for currKey "+currKey+"  in itr = "+dataMap);
			returnMap.put(currKey, dataMap);
		}
		return returnMap;
	}

	public String getSelectStmt(String currSel)
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery = null;
		if(currSel!=null && !currSel.isEmpty())
		{
			if("martialStatus".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT RelationShipID, RelationShipName FROM martialstatus;";
			}
			else if("foodType".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT FoodTypeID, FoodType FROM foodtype;";
			}
			else if("education".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT QualificationID, QualificationName FROM qualifications;";
			}
			else if("profession".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT ProfessionID, ProfessionName FROM professions;";
			}
			else if("country".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CountryID, CountryName FROM country;";
			}
			else if("state".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT StateID, StateName FROM state;";
			}
			else if("city".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CityID, CityName FROM city";
			}
			else if("religion".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT ReligionID, ReligionName FROM religiondetails;";
			}
			else if("language".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT LanguageID, LanguageName FROM language";
			}
			else if("caste".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CasteID, casteName FROM caste;";
			}
			else if("subCaste".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT SubCasteID, SubCasteName FROM subcaste;";
			}
			else if("raasi".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT RaasiID, RaasiName FROM raasi;";
			}
			else if("star".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT StarID, StarName FROM stardetails;";
			}
			else if("gothram".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT GothramID, GothramName FROM gothram";
			}
		}
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, Object> getDataFromDB(String keyName)
	{
		HashSet<String> userValueSet = usrPDetails.get(keyName+"ID");
		if(keyName.equalsIgnoreCase("foodType"));
		usrPDetails.remove(keyName); // found 2 entries which results in only user data hashmap
		
		usrPDetails.remove(keyName+"ID");
		String sqlStmt = getSelectStmt(keyName);
		if(sqlStmt!=null && !sqlStmt.isEmpty())
		{
			Connection conn = getConnection();
			this.setConn(conn);
			logger.debug("DB connection established ");
			try
			{
				PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
				logger.debug("prepStmt to be executed = "+prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data");
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				HashMap<String, String> defaultMap = new HashMap<String, String>();
				HashMap<String, Object> userValMap = new HashMap<String, Object>();
				int keyCount = 1;
				while(rs.next())
				{
					HashMap<String, String> userMulValMap = new HashMap<String, String>();
					String val1 = rs.getString(1);
					String val2 = rs.getString(2);
					if(val1!=null && !val1.isEmpty() && val2!=null && !val2.isEmpty()&& !"SELECT".equalsIgnoreCase(val2))
					{
						defaultMap.put(val1, val2);
						Iterator<String> usrValItr = userValueSet.iterator();
						while(usrValItr.hasNext())
						{
							if(userValueSet.contains(val1))
							{
								userValueSet.remove(val1);
								userMulValMap.put("key", val1);
								userMulValMap.put("value", val2);
								userValMap.put("keyName"+keyCount, userMulValMap);
								keyCount++;
								break;
							}
							usrValItr.next();
						}
					}
					logger.debug(("val1 = "+val1+" val2 = "+val2));
				}
				rs.close();
				closeResources();
				
				if(userValMap.isEmpty())
				{
					HashMap<String, Object> userMulValMap = new HashMap<String, Object>();
					userMulValMap.put("key", "");
					userMulValMap.put("value", "");
					userValMap.put("keyName1", userMulValMap);
				}
				resultMap.put("defaultValues", defaultMap);
				resultMap.put("userValues", userValMap);
				logger.debug("resultMap = "+resultMap);
				return resultMap;
			}
			catch (SQLException e)
			{
				closeResources();
				logger.debug(" SQL EXCEPTION OCCURRED while parsing result set");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug(" statck trace = "+stack);
				return null;
			}
		}
		else
		{
			logger.debug(" SQL stmt is null or empty");
			return null;
		}
	}

	public HashMap<String, Object> generateDummyVals(String keyName)
	{
		HashSet<String> userValueSet = usrPDetails.get(keyName);
		logger.debug("key name = "+keyName+" value = "+userValueSet);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> userValMap = new HashMap<String, Object>();
		Iterator<String> usrValItr = userValueSet.iterator();
		while(usrValItr.hasNext())
		{
			String userValue = usrValItr.next();
			HashMap<String, String> userMulValMap = new HashMap<String, String>();
			userMulValMap.put("key", "");
			userMulValMap.put("value", userValue);
			userValMap.put("keyName1", userMulValMap);
		}
		resultMap.put("userValues", userValMap);
		logger.debug("resultMap = "+resultMap);
		return resultMap;
	}
}
