package in.indiaBridal.commands.auth;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.EnDecryptor;
import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;
import in.indiaBridal.commands.auth.FetchLastPrefPageCommand;
import in.indiaBridal.commands.getProfile.FetchProfilePicAndNameInfo;
import in.indiaBridal.commands.getProfile.GetBasicInfoFormData;
import in.indiaBridal.commands.searchBasicInfoMatches.SearchFactoryForBasicMatchInfo;

public class AuthenticateCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(AuthenticateCommand.class);

	public HashMap<String, Object> getResponseHashMap(String userSel,HashMap<String, Object> userData)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String userNameType = (String) userData.get("userNameType");
		String userName = (String) userData.get("userName");
		String pwd = (String) userData.get("password");
		logger.debug("yet to authenticate the following inputs. userNameType = "+userNameType+" userName = "+userName+" raw password = ******" );
		HashMap<String, Object> responseMap = authUser(userNameType, userName, pwd);

		logger.debug("response after authenticating the user = "+responseMap);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		if(responseMap!=null && !responseMap.isEmpty()&& userName!=null && !userName.isEmpty())
		{
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				String memberID = (String) responseMap.get("memberID");
				FetchLastPrefPageCommand lastPageObj = new FetchLastPrefPageCommand();
				logger.debug("fetching user last updated preference page from DB. MEMBER ID = "+memberID);
				HashMap nextPageInfoMap = lastPageObj.getUserPageInfo(memberID);
				logger.debug("nextPageInfoMap for member id = "+memberID+" nextPageInfoMap = "+nextPageInfoMap);
				HashMap<String, Object> nextPageResMap = null;
				String userCurrPage = "";
				if(!Utilities.isNullOrEmpty(nextPageInfoMap))
				{
					String lastPageStat = (String) nextPageInfoMap.get("status");
					logger.debug("last page response status = "+lastPageStat);
					if(lastPageStat!=null && !lastPageStat.isEmpty())
					{
						String isPrefUpdated = (String) nextPageInfoMap.get("isPrefUpdated");
						logger.debug("is pref updation completed = "+isPrefUpdated);
						// Check if preferences are updated. 
						//if all the preference and details are updated then route to the search results page
						if(!Utilities.isNullOrEmpty(isPrefUpdated)) 
						{
							if("false".equalsIgnoreCase(isPrefUpdated))
							{
								userCurrPage = (String) nextPageInfoMap.get("userCurrPage");
								// get only basic info details after successful auth - dont depend on userCurrPage
								// other page requests will be fired from front end individually.
								
								logger.debug("userCurrPage = "+userCurrPage);
								if(Utilities.isNullOrEmpty(userCurrPage))
									userCurrPage = "basicInfo";
								logger.debug("getting basic info details");
								GetBasicInfoFormData getBasicInfoObj = new GetBasicInfoFormData();
								nextPageResMap = getBasicInfoObj.getResponseHashMap(memberID);
								nextPageResMap.put("isPrefUpdated", isPrefUpdated);
							}
							else
							{
								userCurrPage = "partnerProfiles";
								SearchFactoryForBasicMatchInfo basicSearchInfoObj = new SearchFactoryForBasicMatchInfo();
								nextPageResMap = basicSearchInfoObj.getResponseHashMap(memberID);
							}
						}
					}				
				}
				if(!Utilities.isNullOrEmpty(nextPageResMap))
				{
					if("success".equalsIgnoreCase((String) nextPageResMap.get("status")))
					{
						logger.debug("nextPageResMap = "+nextPageResMap);
						HashMap navPageData = (HashMap) nextPageResMap.get("navPageData");
						logger.debug("nav page data = "+navPageData);
						if(!Utilities.isNullOrEmpty(navPageData))
						{
							FetchProfilePicAndNameInfo profPicNameObj = new FetchProfilePicAndNameInfo();
							HashMap profPicInfoMap = profPicNameObj.getResponseHashMap(memberID);
							if(!Utilities.isNullOrEmpty(profPicInfoMap))
							{
								if("success".equalsIgnoreCase((String) profPicInfoMap.get("status")))
									navPageData.put("profPicAndName", profPicInfoMap.get("userData"));
								else
									navPageData.put("profPicAndName", "");
							}
							else
								navPageData.put("profPicAndName", "");
							
							responseMap.put("navPage", userCurrPage);
							responseMap.put("isnavPageDataAvailable", "true");
							responseMap.put("navPageData", navPageData);
							responseMap.put("isPrefUpdated", nextPageResMap.get("isPrefUpdated"));
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
			}
			else
			{
				responseMap.put("navPage", "basicInfo");
				responseMap.put("isnavPageDataAvailable", "false");
				responseMap.put("navPageData", "");
				JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
				JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", responseMap.get("errorMsg"));
				returnMap.put("jsonArr", retJSONArr);
			}
		}
		else
		{
			HashMap<String, Object> jsonHashMap = new HashMap<String, Object>();
			jsonHashMap.put("status", "failure");
			jsonHashMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			jsonHashMap.put("isAuthenticated", "false");
			jsonHashMap.put("navPage", "basicInfo");
			jsonHashMap.put("isnavPageDataAvailable", "false");
			jsonHashMap.put("navPageData", "");
			JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(jsonHashMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);

			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			returnMap.put("jsonArr", retJSONArr);
		}
		logger.debug("end of auth command. returning map to calling command. return map = "+returnMap);
		return returnMap;
	}

	public HashMap<String, Object> authUser(String userNameType,String userName, String pwd)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		PreparedStatement prepStmt = null;
		String query = null;
		if(!Utilities.isNullOrEmpty(userNameType) && !Utilities.isNullOrEmpty(userName) && !Utilities.isNullOrEmpty(pwd))
		{
			logger.debug("userNameType = "+userNameType+" authenticating the user with following  = "+userName);
			if("phoneNum".equalsIgnoreCase(userNameType))
			{
				query = "SELECT COUNT(phoneNum), MemberID,userActive FROM authentication WHERE phoneNum=? AND password=?";
			}
			else if("email".equalsIgnoreCase(userNameType))
			{
				query = "SELECT COUNT(emailID), MemberID,userActive FROM authentication WHERE emailID=? AND password=?";
			}
			else if("memberID".equalsIgnoreCase(userNameType))
			{
				query = "SELECT COUNT(MemberID), MemberID,userActive FROM authentication WHERE MemberID=? AND password=?";
			}
			else
			{
				logger.debug("userName = "+userName+" NO VALID SELECTION INPUT PASSED");
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "no DB Connection found");
				returnMap.put("isAuthenticated", "false");
				returnMap.put("memberID", "");
				return returnMap;
			}
		}
		else
		{
			logger.debug("userName = "+userName+" EMPTY OR NULL DATA PASSED");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "Invalid data passed");
			returnMap.put("isAuthenticated", "false");
			returnMap.put("memberID", "");
			return returnMap;
		}

		Connection conn = getConnection();
		this.setConn(conn);
		try
		{
			if(!Utilities.isNullOrEmpty(query))
			{
				EnDecryptor encObj = new EnDecryptor();
				String encPwd = encObj.encryption(pwd,true);
				prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, userName);
				prepStmt.setString(2, encPwd); 
			}
			logger.debug("auth stmt = "+query);
			ResultSet authRS = prepStmt.executeQuery();
			while(authRS.next())
			{
				int recCount = authRS.getInt(1);
				String memberID = authRS.getString(2);
				String userActive =  authRS.getString(3);
				if(recCount == 1 && !Utilities.isNullOrEmpty(memberID))
				{
					if(userActive.equalsIgnoreCase("NO")){
						returnMap.put("status", "failure");
						returnMap.put("errorMsg", "Please activate your account");
						returnMap.put("isAuthenticated", "false");
						returnMap.put("memberID", "");
					}else {
						returnMap.put("status", "success");
						returnMap.put("errorMsg", "success");
						returnMap.put("isAuthenticated", "true");
						returnMap.put("memberID", memberID);
					}
				}
				else
				{
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "profile not found");
					returnMap.put("isAuthenticated", "false");
					returnMap.put("memberID", "");
				}
				return returnMap;
			}
			authRS.close();
			closeResources();
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "failed to get query");
			returnMap.put("isAuthenticated", "false");
			returnMap.put("memberID", "");
			return returnMap;
		}
		catch (SQLException e) 
		{
			closeResources();
			logger.debug("userName = "+userName+" SQL EXCEPTION OCCURRED while getting prepared statement for authentication");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("userName = "+userName+" statck trace = "+stack);
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
			returnMap.put("isAuthenticated", "false");
			returnMap.put("memberID", "");
			return returnMap;
		}	
	}	
}
