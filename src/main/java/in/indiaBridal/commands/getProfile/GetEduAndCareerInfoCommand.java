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

public class GetEduAndCareerInfoCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetEduAndCareerInfoCommand.class);
	ArrayList<String> keyList = new ArrayList<String>();
	HashMap<String, String> usrEDetails = new HashMap<String, String>();

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
					responseMap.put("navPage", "eduAndCar");
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
					responseMap.put("navPage", "eduAndCar");
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
				responseMap.put("navPage", "eduAndCar");
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
			responseMap.put("navPage", "eduAndCar");
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
		keyList.add("education");
		keyList.add("profession");

		logger.debug("key list to hit different tables set");

		GetUserEduAndCareerInfoCommand getUsrEDetObj = new GetUserEduAndCareerInfoCommand();
		HashMap<String, Object> usrEDetResMap = getUsrEDetObj.getResponseHashMap(memberID);

		if(!Utilities.isNullOrEmpty(usrEDetResMap))
		{
			if("success".equalsIgnoreCase((String) usrEDetResMap.get("status")))
			{
				usrEDetails = (HashMap<String, String>) usrEDetResMap.get("userData");
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
		Set<String> userDataRemKeys = usrEDetails.keySet();
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
			if("education".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT QualificationID, QualificationName FROM qualifications;";
			}
			else if("profession".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT ProfessionID, ProfessionName FROM professions;";
			}
		}
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, Object> getDataFromDB(String keyName)
	{
		String userValue = usrEDetails.get(keyName+"ID");
		usrEDetails.remove(keyName+"ID");
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
				logger.debug(" stack trace = "+stack);
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
		String userValue = usrEDetails.get(keyName);
		logger.debug("key name = "+keyName+" value = "+userValue);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, String> defaultMap = new HashMap<String, String>();
		HashMap<String, String> userValMap = new HashMap<String, String>();
		//defaultMap.put("", "");
		userValMap.put("value", userValue);
		userValMap.put("key", userValue);
		//resultMap.put("defaultValues", defaultMap);
		resultMap.put("userValues", userValMap);
		return resultMap;
	}
}
