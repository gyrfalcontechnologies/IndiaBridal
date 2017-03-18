package in.indiaBridal.commands.getProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;

public class GetBasicInfoFormData extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetBasicInfoFormData.class);
	ArrayList<String> keyList = new ArrayList<String>();
	HashMap<String, String> usrBDetails = new HashMap<String, String>();

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
					responseMap.put("navPage", "basicInfo");
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
					responseMap.put("navPage", "basicInfo");
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
				responseMap.put("navPage", "basicInfo");
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
			responseMap.put("navPage", "basicInfo");
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
		keyList.add("physique");
		keyList.add("complexion");
		keyList.add("religion");
		keyList.add("language");
		keyList.add("caste");
		keyList.add("subCaste");
		keyList.add("gothram");
		keyList.add("createdBy");

		logger.debug("key list to hit different tables set");

		GetUserBasicInfoDetailsCommand getUsrBDetObj = new GetUserBasicInfoDetailsCommand();
		HashMap<String, Object> usrBDetResMap = getUsrBDetObj.getResponseHashMap(memberID);

		if(!Utilities.isNullOrEmpty(usrBDetResMap))
		{
			if("success".equalsIgnoreCase((String) usrBDetResMap.get("status")))
			{
				usrBDetails = (HashMap<String, String>) usrBDetResMap.get("userData");
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
				returnMap.put(currKey, dataMap);
		}
		Set<String> userDataRemKeys = usrBDetails.keySet();
		Iterator<String> keyIter = userDataRemKeys.iterator();
		while(keyIter.hasNext())
		{
			String currKey = keyIter.next();
			HashMap<String, Object> dataMap = generateDummyVals(currKey);
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
			else if("physique".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT PhysiqueID, PhysiqueType FROM physique;";
			}
			else if("complexion".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT ComplexionID, complexionName FROM complexion";
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
			else if("gothram".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT GothramID, GothramName FROM gothram";
			}
			else if("createdBy".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CreatedByID, CreatedBy FROM createdby";
			}
		}
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, Object> getDataFromDB(String keyName)
	{
		String userValue = usrBDetails.get(keyName+"ID");
		usrBDetails.remove(keyName+"ID");
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
				HashMap<String, String> userValMap = new HashMap<String, String>();
				while(rs.next())
				{
					String val1 = rs.getString(1);
					String val2 = rs.getString(2);
					if(val1!=null && !val1.isEmpty() && val2!=null && !val2.isEmpty()&& !"SELECT".equalsIgnoreCase(val2))
					{
						defaultMap.put(val1, val2);
						if(userValue.equalsIgnoreCase(val1))
						{
							userValMap.put("key", val1);
							userValMap.put("value", val2);
						}
					}
					logger.debug(("val1 = "+val1+" val2 = "+val2));
				}
				rs.close();
				closeResources();
				
				if(userValMap.isEmpty())
				{
					userValMap.put("key", "");
					userValMap.put("value", "");
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
		String userValue = usrBDetails.get(keyName);
		logger.debug("key name = "+keyName+" value = "+userValue);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, String> defaultMap = new HashMap<String, String>();
		HashMap<String, String> userValMap = new HashMap<String, String>();
		//defaultMap.put("", "");
		userValMap.put("value", userValue);
		userValMap.put("key", "");
		//resultMap.put("defaultValues", defaultMap);
		resultMap.put("userValues", userValMap);
		logger.debug("resultMap = "+resultMap);
		return resultMap;
	}
}
