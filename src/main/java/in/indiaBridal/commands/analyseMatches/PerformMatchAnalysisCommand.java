package in.indiaBridal.commands.analyseMatches;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;
import in.indiaBridal.commands.getProfile.GetUserPartnerPrefDetailsCommand;

public class PerformMatchAnalysisCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(PerformMatchAnalysisCommand.class);
	int totalMatchedPrefForEachProfile = 0;
	
	public static void main(String[] args)
	{
		PerformMatchAnalysisCommand searchObj = new PerformMatchAnalysisCommand();
		HashMap resMap = searchObj.generateResponseJSON("IBM10000004", "IBM10000002");
		logger.debug("resMap = "+resMap);
	}
	
	public HashMap generateResponseJSON(String memberID, String partMmeberID)
	{
		HashMap responseMap = getResponseHashMap(memberID, partMmeberID);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("jsonArr", retJSONArr);
		return returnMap;
	}
	
	public HashMap getResponseHashMap(String memberID, String partMemberID)
	{
		HashMap returnMap = new HashMap();
		PartnerPrefDetails partPrefDetObj =  getPartnerPreferences(memberID);
		if(partPrefDetObj.getStatus().equalsIgnoreCase("success"))
		{
			ArrayList<MatchedPartnerDetails> matchdPartDetlsListObj= executeQuery(buildQueryToGetCurrProfInfo(partPrefDetObj, memberID, partMemberID));
			if(matchdPartDetlsListObj!=null && !matchdPartDetlsListObj.isEmpty())
			{
				matchdPartDetlsListObj = buildQueryToGetIDNames(matchdPartDetlsListObj);
				int totalProfiles = matchdPartDetlsListObj.size();
				HashMap matchedProfPrefResMap = new HashMap();
				HashMap matchedProfInfoResMap = new HashMap();
				for(int count=0;count<totalProfiles;count++)
				{
					HashMap	matchedProfilesPrefMap = new HashMap();
					matchedProfilesPrefMap.put("gothram", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getGothramID","getGothramName"));
					matchedProfilesPrefMap.put("relationShip", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getRelationShipID","getRelationShipName"));
					matchedProfilesPrefMap.put("language", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getLanguageID","getLanguageName"));
					matchedProfilesPrefMap.put("raasi", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getRaasiID","getRaasiName"));
					matchedProfilesPrefMap.put("religion", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getReligionID","getReligionName"));
					matchedProfilesPrefMap.put("star", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getStarID","getStarName"));
					matchedProfilesPrefMap.put("caste", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getCasteID","getCasteName"));
					matchedProfilesPrefMap.put("subCaste", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getSubCasteID","getSubCasteName"));
					matchedProfilesPrefMap.put("country", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getCountryID","getCountryName"));
					matchedProfilesPrefMap.put("state", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getStateID","getStateName"));
					matchedProfilesPrefMap.put("city", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getCityID","getCityName"));
					matchedProfilesPrefMap.put("qualification", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getQualificationID","getQualificationName"));
					matchedProfilesPrefMap.put("profession", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getProfessionID","getProfessionName"));
					matchedProfilesPrefMap.put("foodType", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getFoodTypeID","getFoodType"));
					matchedProfilesPrefMap.put("dosham", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getDosham",null));
					matchedProfilesPrefMap.put("drinker", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getDrinker",null));
					matchedProfilesPrefMap.put("smoker", analyzeMatchedPrefs(partPrefDetObj, matchdPartDetlsListObj.get(count), "getSmoker",null));
					matchedProfilesPrefMap.put("age", analyzeIntValPrefs(getCurrPrefCond(partPrefDetObj.getAge_LL(),""), getCurrPrefCond(partPrefDetObj.getAge_UL(),""),  matchdPartDetlsListObj.get(count).getAge()));
					matchedProfilesPrefMap.put("height", analyzeIntValPrefs(getCurrPrefCond(partPrefDetObj.getHeight_LL(),""), getCurrPrefCond(partPrefDetObj.getHeight_UL(),""),  matchdPartDetlsListObj.get(count).getHeight()));
					matchedProfilesPrefMap.put("weight", analyzeIntValPrefs(getCurrPrefCond(partPrefDetObj.getWeight_LL(),""), getCurrPrefCond(partPrefDetObj.getWeight_UL(),""),  matchdPartDetlsListObj.get(count).getWeight()));
					matchedProfilesPrefMap.put("income", analyzeIntValPrefs(getCurrPrefCond(partPrefDetObj.getIncome_LL(),""), getCurrPrefCond(partPrefDetObj.getIncome_UL(),""),  matchdPartDetlsListObj.get(count).getIncome_LL(), matchdPartDetlsListObj.get(count).getIncome_UL()));

					matchedProfPrefResMap.put("match"+count, matchedProfilesPrefMap);

					HashMap matchedProfilesInfoMap = new HashMap();
					matchedProfilesInfoMap.put("memberID",matchdPartDetlsListObj.get(count).getMemberID());
					matchedProfilesInfoMap.put("fullName",matchdPartDetlsListObj.get(count).getFullName());
					matchedProfilesInfoMap.put("description",matchdPartDetlsListObj.get(count).getDescription());
					matchedProfilesInfoMap.put("FatherStatus",matchdPartDetlsListObj.get(count).getFatherStatus());
					matchedProfilesInfoMap.put("MotherStatus",matchdPartDetlsListObj.get(count).getMotherStatus());
					matchedProfilesInfoMap.put("NativePlace",matchdPartDetlsListObj.get(count).getNativePlace());
					matchedProfilesInfoMap.put("NoOfSiblings",matchdPartDetlsListObj.get(count).getNoOfSiblings());
					matchedProfilesInfoMap.put("FamilyTypeID",matchdPartDetlsListObj.get(count).getFamilyTypeID());
					matchedProfilesInfoMap.put("FamilyValue",matchdPartDetlsListObj.get(count).getFamilyValue());
					matchedProfilesInfoMap.put("FamilyStatus",matchdPartDetlsListObj.get(count).getFamilyStatus());
					matchedProfilesInfoMap.put("bloodGroup",matchdPartDetlsListObj.get(count).getBloodGroup());
					matchedProfilesInfoMap.put("gender",matchdPartDetlsListObj.get(count).getGender());
					matchedProfilesInfoMap.put("horoPics",matchdPartDetlsListObj.get(count).getHoroPics());
					matchedProfilesInfoMap.put("isPremiumMember",matchdPartDetlsListObj.get(count).getIsPremiumMember());
					matchedProfilesInfoMap.put("matchedProfileExpecs",matchdPartDetlsListObj.get(count).getMatchedProfileExpecs());
					matchedProfilesInfoMap.put("totalPrefMatched", totalMatchedPrefForEachProfile+"");

					matchedProfInfoResMap.put("match"+count, matchedProfilesInfoMap);
					totalMatchedPrefForEachProfile = 0;
				}
				HashMap navPageData = new HashMap();
				navPageData.put("totalProfiles", totalProfiles+"");
				navPageData.put("matchedPrefData", matchedProfPrefResMap);
				navPageData.put("matchedprofData", matchedProfInfoResMap);

				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("isnavPageDataAvailable", "true");
				returnMap.put("navPageData", navPageData);
			}
			else
			{
				if(matchdPartDetlsListObj==null)
				{
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "failed to get partner pref details");
					returnMap.put("isnavPageDataAvailable", "false");
				}
				else
				{
					HashMap navPageData = new HashMap();
					navPageData.put("totalProfiles", 0+"");
					navPageData.put("matchedPrefData", new HashMap());
					navPageData.put("matchedprofData", new HashMap());

					returnMap.put("status", "success");
					returnMap.put("errorMsg", "No profiles found");
					returnMap.put("isnavPageDataAvailable", "true");
					returnMap.put("navPageData", navPageData);
				}
			}
		}
		else
		{
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "failed to get partner pref details");
			returnMap.put("isnavPageDataAvailable", "false");
		}
		return returnMap;
	}

	public PartnerPrefDetails getPartnerPreferences(String memberID)
	{
		GetUserPartnerPrefDetailsCommand getPartPrefObj = new GetUserPartnerPrefDetailsCommand();
		HashMap returnMap = getPartPrefObj.getResponseHashMap(memberID);
		PartnerPrefDetails partPrefDetObj = new PartnerPrefDetails();
		if(returnMap !=null && !returnMap.isEmpty())
		{
			String status = (String) returnMap.get("status");
			if(status.equalsIgnoreCase("success"))
			{
				HashMap<String, HashSet<String>> responseMap = ((HashMap<String, HashSet<String>>) returnMap.get("userData"));
				if(responseMap!=null && !responseMap.isEmpty())
				{
					partPrefDetObj.setGothramID(responseMap.get("gothramID"));
					partPrefDetObj.setSmoker(responseMap.get("smoker"));
					partPrefDetObj.setFoodTypeID(responseMap.get("foodTypeID"));
					partPrefDetObj.setDrinker(responseMap.get("drinker"));
					partPrefDetObj.setRelationShipID(responseMap.get("martialStatusID"));
					partPrefDetObj.setAge_LL(responseMap.get("age_LL"));
					partPrefDetObj.setAge_UL(responseMap.get("age_UL"));
					partPrefDetObj.setHeight_LL(responseMap.get("height_LL"));
					partPrefDetObj.setHeight_UL(responseMap.get("height_UL"));
					partPrefDetObj.setWeight_LL(responseMap.get("weight_LL"));
					partPrefDetObj.setWeight_UL(responseMap.get("weight_UL"));
					partPrefDetObj.setIncome_UL(responseMap.get("income_UL"));
					partPrefDetObj.setIncome_LL(responseMap.get("income_LL"));
					partPrefDetObj.setDosham(responseMap.get("dosham"));
					partPrefDetObj.setExpectations(responseMap.get("expectations"));
					partPrefDetObj.setLanguageID(responseMap.get("languageID"));
					partPrefDetObj.setProfessionID(responseMap.get("professionID"));
					partPrefDetObj.setQualificationID(responseMap.get("educationID"));
					partPrefDetObj.setRaasiID(responseMap.get("raasiID"));
					partPrefDetObj.setReligionID(responseMap.get("religionID"));
					partPrefDetObj.setStarID(responseMap.get("starID"));
					partPrefDetObj.setCasteID(responseMap.get("casteID"));
					partPrefDetObj.setSubCasteID(responseMap.get("subCasteID"));
					partPrefDetObj.setCountryID(responseMap.get("countryID"));
					partPrefDetObj.setStateID(responseMap.get("stateID"));
					partPrefDetObj.setCityID(responseMap.get("cityID"));

					partPrefDetObj.setGothramName(responseMap.get("gothramName"));
					partPrefDetObj.setRelationShipName(responseMap.get("relationShipName"));
					partPrefDetObj.setLanguageName(responseMap.get("languageName"));
					partPrefDetObj.setRaasiName(responseMap.get("raasiName"));
					partPrefDetObj.setReligionName(responseMap.get("religionName"));
					partPrefDetObj.setStarName(responseMap.get("starName"));
					partPrefDetObj.setCasteName(responseMap.get("casteName"));
					partPrefDetObj.setSubCasteName(responseMap.get("subCasteName"));
					partPrefDetObj.setCountryName(responseMap.get("countryName"));
					partPrefDetObj.setQualificationName(responseMap.get("qualificationName"));
					partPrefDetObj.setProfessionName(responseMap.get("professionName"));
					partPrefDetObj.setFoodType(responseMap.get("foodType"));
					partPrefDetObj.setStateName(responseMap.get("stateName"));
					partPrefDetObj.setCityName(responseMap.get("cityName"));

					partPrefDetObj.setStatus("success");
					partPrefDetObj.setErrorMsg("success");
				}
				else
				{
					partPrefDetObj.setStatus("failure");
					partPrefDetObj.setErrorMsg("partner pref is null or empty");
				}
			}
			else
			{
				partPrefDetObj.setStatus("failure");
				partPrefDetObj.setErrorMsg("failed to get partner pref");
			}
		}
		else
		{
			partPrefDetObj.setStatus("failure");
			partPrefDetObj.setErrorMsg("null or empty map returned");
		}
		return partPrefDetObj;
	}

	public String buildQueryToGetCurrProfInfo(PartnerPrefDetails partPrefDetObj, String memberID, String partnerMemID)
	{
		StringBuilder selClauseSB = new StringBuilder();

		selClauseSB.append("SELECT ud.memberID,mp.Expectations, ud.isPremiumMember, ud.FullName, DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), ud.DOB)), \"%Y\")+0 AS age, "
				+ "ud.GothramID, ud.MartialStatusID, ud.DOB, ud.Height, "
				+ "ud.Weight, ud.dosham, ud.description, ud.MotherTongueID, ud.RaasiID, "
				+ "ud.ReligionID, ud.StarID, ud.CasteID, "
				+ "ud.SubCasteID, fd.CountryID, fd.StateID, fd.CityID, "
				+ "pd.QualificationID, pd.College, pd.ProfessionID, "
				+ "pd.AnnualIncome_UL, pd.AnnualIncome_LL,  ls.Drinker, ls.FoodTypeID, "
				+ "ud.HoroscopeLoc, ls.Smoker, fd.FatherStatus, fd.MotherStatus, fd.NativePlace, "
				+ "fd.NoOfSiblings, fd.FamilyTypeID, fd.FamilyValue, fd.FamilyStatus, ud.BloodGroup, ud.gender "
				+ "FROM usermasterdetails ud "
				+ "JOIN masterpreferences mp ON mp.MemberID = ud.memberID "
				+ "JOIN lifestyle ls ON ud.MemberID = ls.MemberID "
				+ "JOIN professionaldetails pd ON ud.MemberID = pd.MemberID "
				+ "JOIN familydetails fd ON ud.MemberID = fd.memberID "
				+ " WHERE ud.MemberID = '"+partnerMemID+"' LIMIT 1");
		return selClauseSB.toString();
	}

	public ArrayList<MatchedPartnerDetails> executeQuery(String sqlQuery)
	{

		if(sqlQuery!=null && !sqlQuery.isEmpty())
		{
			Connection conn = getConnection();
			this.setConn(conn);
			logger.debug("DB connection established ");
			try
			{
				PreparedStatement prepStmt = conn.prepareStatement(sqlQuery);
				logger.debug("prepStmt to be executed = "+prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data");
				ArrayList<MatchedPartnerDetails> matchedPartList = new ArrayList<MatchedPartnerDetails>();

				if(rs.next())
				{
					MatchedPartnerDetails matchPartObj = new MatchedPartnerDetails();
					matchPartObj.setMemberID(Utilities.isNullOrEmpty(rs.getString("memberID"))?"":rs.getString("memberID"));
					matchPartObj.setFullName(Utilities.isNullOrEmpty(rs.getString("FullName"))?"":rs.getString("FullName"));
					matchPartObj.setAge(Utilities.isNullOrEmpty(rs.getString("age"))?"":rs.getString("age"));
					matchPartObj.setGothramID(Utilities.isNullOrEmpty(rs.getString("GothramID"))?"":rs.getString("GothramID"));
					matchPartObj.setRelationShipID(Utilities.isNullOrEmpty(rs.getString("MartialStatusID"))?"":rs.getString("MartialStatusID"));
					matchPartObj.setDob(Utilities.isNullOrEmpty(rs.getString("DOB"))?"":rs.getString("DOB"));
					matchPartObj.setHeight(Utilities.isNullOrEmpty(rs.getString("Height"))?"":rs.getString("Height"));
					matchPartObj.setWeight(Utilities.isNullOrEmpty(rs.getString("Weight"))?"":rs.getString("Weight"));
					matchPartObj.setDosham(Utilities.isNullOrEmpty(rs.getString("dosham"))?"":rs.getString("dosham"));
					matchPartObj.setDescription(Utilities.isNullOrEmpty(rs.getString("description"))?"":rs.getString("description"));
					matchPartObj.setLanguageID(Utilities.isNullOrEmpty(rs.getString("MotherTongueID"))?"":rs.getString("MotherTongueID"));
					matchPartObj.setRaasiID(Utilities.isNullOrEmpty(rs.getString("RaasiID"))?"":rs.getString("RaasiID"));
					matchPartObj.setReligionID(Utilities.isNullOrEmpty(rs.getString("ReligionID"))?"":rs.getString("ReligionID"));
					matchPartObj.setStarID(Utilities.isNullOrEmpty(rs.getString("StarID"))?"":rs.getString("StarID"));
					matchPartObj.setCasteID(Utilities.isNullOrEmpty(rs.getString("CasteID"))?"":rs.getString("CasteID"));
					matchPartObj.setSubCasteID(Utilities.isNullOrEmpty(rs.getString("SubCasteID"))?"":rs.getString("SubCasteID"));
					matchPartObj.setCountryID(Utilities.isNullOrEmpty(rs.getString("CountryID"))?"":rs.getString("CountryID"));
					matchPartObj.setStateID(Utilities.isNullOrEmpty(rs.getString("fd.StateID"))?"":rs.getString("fd.StateID"));
					matchPartObj.setCityID(Utilities.isNullOrEmpty(rs.getString("CityID"))?"":rs.getString("CityID"));
					matchPartObj.setQualificationID(Utilities.isNullOrEmpty(rs.getString("QualificationID"))?"":rs.getString("QualificationID"));
					matchPartObj.setProfessionID(Utilities.isNullOrEmpty(rs.getString("pd.ProfessionID"))?"":rs.getString("pd.ProfessionID"));
					matchPartObj.setIncome_UL(Utilities.isNullOrEmpty(rs.getString("AnnualIncome_UL"))?"":rs.getString("AnnualIncome_UL"));
					matchPartObj.setIncome_LL(Utilities.isNullOrEmpty(rs.getString("AnnualIncome_LL"))?"":rs.getString("AnnualIncome_LL"));
					matchPartObj.setDrinker(Utilities.isNullOrEmpty(rs.getString("Drinker"))?"":rs.getString("Drinker"));
					matchPartObj.setFoodTypeID(Utilities.isNullOrEmpty(rs.getString("FoodTypeID"))?"":rs.getString("FoodTypeID"));
					matchPartObj.setSmoker(Utilities.isNullOrEmpty(rs.getString("Smoker"))?"":rs.getString("Smoker"));
					matchPartObj.setFatherStatus(Utilities.isNullOrEmpty(rs.getString("FatherStatus"))?"":rs.getString("FatherStatus"));
					matchPartObj.setMotherStatus(Utilities.isNullOrEmpty(rs.getString("MotherStatus"))?"":rs.getString("MotherStatus"));
					matchPartObj.setNativePlace(Utilities.isNullOrEmpty(rs.getString("NativePlace"))?"":rs.getString("NativePlace"));
					matchPartObj.setNoOfSiblings(Utilities.isNullOrEmpty(rs.getString("NoOfSiblings"))?"":rs.getString("NoOfSiblings"));
					matchPartObj.setFamilyValue(Utilities.isNullOrEmpty(rs.getString("FamilyValue"))?"":rs.getString("FamilyValue"));
					matchPartObj.setFamilyStatus(Utilities.isNullOrEmpty(rs.getString("FamilyStatus"))?"":rs.getString("FamilyStatus"));
					matchPartObj.setBloodGroup(Utilities.isNullOrEmpty(rs.getString("BloodGroup"))?"":rs.getString("BloodGroup"));
					matchPartObj.setGender(Utilities.isNullOrEmpty(rs.getString("gender"))?"":rs.getString("gender"));
					matchPartObj.setHoroPics(Utilities.isNullOrEmpty(rs.getString("HoroscopeLoc"))?"":rs.getString("HoroscopeLoc"));
					matchPartObj.setIsPremiumMember(Utilities.isNullOrEmpty(rs.getString("isPremiumMember"))?"no":rs.getString("isPremiumMember"));
					matchPartObj.setMatchedProfileExpecs(Utilities.isNullOrEmpty(rs.getString("Expectations"))?"":rs.getString("Expectations"));
					matchedPartList.add(matchPartObj);
				}
				rs.close();
				closeResources();

				logger.debug("matchedPartList = "+matchedPartList);
				return matchedPartList;
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

	public HashMap<String,String> analyzeMatchedPrefs(Object partPrefDetsObj, Object matchdPartDetlsObj,String methodIDName, String methodValName)
	{
		logger.debug("");
		logger.debug("evealuvating new preference.");
		logger.debug("about to call methodIDName = "+methodIDName+" methodValName = "+methodValName);
		HashMap<String,String> currPrefResMap = new HashMap<String,String>();
		try 
		{
			Method  currPartPrefIDMethod = partPrefDetsObj.getClass().getMethod(methodIDName);
			String currPrefID = (String)getCurrPrefCond((HashSet<String>)currPartPrefIDMethod.invoke(partPrefDetsObj),"");
			logger.debug("out = "+currPrefID);
			if(currPrefID!=null && !currPrefID.isEmpty())
			{
				Method  currMatchdPrefIDMethod = matchdPartDetlsObj.getClass().getMethod(methodIDName);
				String currMatchID = (String) currMatchdPrefIDMethod.invoke(matchdPartDetlsObj);
				logger.debug("currMatchID method invoked");
				String currPrefVal = currPrefID;
				if(methodValName!=null)
				{
					Method currPartPrefValMethod = partPrefDetsObj.getClass().getMethod(methodValName);
					currPrefVal =(String) getCurrPrefCond((HashSet<String>)currPartPrefValMethod.invoke(partPrefDetsObj),"");
				}
				logger.debug("currPrefVal = "+currPrefVal);
				currPrefResMap.put("currPrefVal",currPrefVal);

				if(currMatchID!=null && !currMatchID.isEmpty())
				{
					String currMatchVal = currMatchID;
					if(methodValName!=null)
					{
						Method currMatchdPrefValMethod = matchdPartDetlsObj.getClass().getMethod(methodValName);
						logger.debug("About to execute method = "+methodValName);
						currMatchVal =(String) currMatchdPrefValMethod.invoke(matchdPartDetlsObj);
					}
					logger.debug("currMatchVal = "+currMatchVal);
					currPrefResMap.put("currMatchVal",currMatchVal);
					if(evaluvatePrefs(currPrefID, currMatchID))
						currPrefResMap.put("isMatched","true");
					else
						currPrefResMap.put("isMatched","false");
				}
				else
				{
					currPrefResMap.put("currMatchVal","");
					currPrefResMap.put("isMatched","false");
				}
				return currPrefResMap;
			}
			else
			{
				currPrefResMap.put("currPrefVal","");
				currPrefResMap.put("currMatchVal","");
				currPrefResMap.put("isMatched","false");
				return currPrefResMap;
			}
		} 
		catch (SecurityException e)
		{
			logger.debug("SecurityException while calling methods using reflection");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		} 
		catch (NoSuchMethodException e)
		{
			logger.debug("NoSuchMethodException while calling methods using reflection");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		} 
		catch (IllegalAccessException e) 
		{
			logger.debug("IllegalAccessException while calling methods using reflection");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		} 
		catch (IllegalArgumentException e) 
		{
			logger.debug("IllegalArgumentException while calling methods using reflection");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		} 
		catch (InvocationTargetException e) 
		{
			logger.debug("InvocationTargetException while calling methods using reflection");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		}
		currPrefResMap.put("currPrefVal","");
		currPrefResMap.put("currMatchVal","");
		currPrefResMap.put("isMatched","false");
		return currPrefResMap;
	}

	public HashMap<String,String> analyzeIntValPrefs(String prefLL, String prefUL, String matchedProfVal)
	{
		HashMap<String,String> currPrefResMap = new HashMap<String,String>();
		if(prefUL!=null && prefLL!=null && !prefUL.isEmpty() && !prefLL.isEmpty())
		{
			currPrefResMap.put("currPrefVal",prefLL+" - "+ prefUL);
			if(matchedProfVal!=null && !matchedProfVal.isEmpty())
			{
				currPrefResMap.put("currMatchVal",matchedProfVal);
				int prefLL_INT = Integer.parseInt(prefLL);
				int prefUL_INT = Integer.parseInt(prefUL);
				int matchedProfVal_INT = Integer.parseInt(matchedProfVal);
				if(matchedProfVal_INT>prefLL_INT && matchedProfVal_INT<prefUL_INT)
				{
					totalMatchedPrefForEachProfile = totalMatchedPrefForEachProfile +1;
					currPrefResMap.put("isMatched","true");
					logger.debug("preference matched. pref = "+prefLL_INT+" "+prefUL_INT+" matched val = "+matchedProfVal_INT);
				}
				else
					currPrefResMap.put("isMatched","false");	
			}
			else
			{
				currPrefResMap.put("currMatchVal","");
				currPrefResMap.put("isMatched","false");	
			}
		}
		else
		{
			currPrefResMap.put("currPrefVal","");
			currPrefResMap.put("currMatchVal","");
			currPrefResMap.put("isMatched","false");	
		}
		return currPrefResMap;
	}

	public HashMap<String,String> analyzeIntValPrefs(String prefLL, String prefUL, String matchedProfVal_LL, String matchedProfVal_UL)
	{
		HashMap<String,String> currPrefResMap = new HashMap<String,String>();
		if(prefUL!=null && prefLL!=null && !prefUL.isEmpty() && !prefLL.isEmpty())
		{
			currPrefResMap.put("currPrefVal",prefLL+" - "+ prefUL);
			if(matchedProfVal_LL!=null && !matchedProfVal_LL.isEmpty() && matchedProfVal_UL!=null && !matchedProfVal_UL.isEmpty())
			{
				currPrefResMap.put("currMatchVal",matchedProfVal_LL+" - "+matchedProfVal_UL);
				int prefLL_INT = Integer.parseInt(prefLL);
				int prefUL_INT = Integer.parseInt(prefUL);
				int matchedProfVal_LLINT = Integer.parseInt(matchedProfVal_LL);
				int matchedProfVal_ULINT = Integer.parseInt(matchedProfVal_UL);
				if(matchedProfVal_LLINT>prefLL_INT && matchedProfVal_ULINT<prefUL_INT)
				{
					totalMatchedPrefForEachProfile = totalMatchedPrefForEachProfile +1;
					currPrefResMap.put("isMatched","true");
					logger.debug("preference matched. pref = "+prefLL_INT+" "+prefUL_INT+" matched val = "+matchedProfVal_LLINT+" - "+matchedProfVal_ULINT);
				}
				else
					currPrefResMap.put("isMatched","false");	
			}
			else
			{
				currPrefResMap.put("currMatchVal","");
				currPrefResMap.put("isMatched","false");	
			}
		}
		else
		{
			currPrefResMap.put("currMatchVal","");
			currPrefResMap.put("currMatchVal","");
			currPrefResMap.put("isMatched","false");	
		}
		return currPrefResMap;
	}

	public boolean evaluvatePrefs(String partPref, String matchdPref)
	{
		boolean isMatched = false;
		if(partPref!=null && matchdPref!=null && !matchdPref.isEmpty() && !partPref.isEmpty())
		{
			logger.debug("values to be evaluvated are good");
			String partPrefArr[] = partPref.split(",");
			for(int count=0;count<partPrefArr.length;count++)
			{
				logger.debug("partPrefArr val = "+partPrefArr[count]+ " matchdPref = "+matchdPref);
				if(matchdPref.equalsIgnoreCase(partPrefArr[count]))
				{
					logger.debug("preference matched. pref = "+partPref+" matchdPref val = "+matchdPref);
					isMatched = true;
					totalMatchedPrefForEachProfile = totalMatchedPrefForEachProfile +1;
					break;
				}
				else
					logger.debug("No matches found");
			}
		}
		return isMatched;
	}

	public String getCurrPrefCond(HashSet<String> currPrefSet,String encloseChar) 
	{
		return checkForNullEmptyValHashSet(currPrefSet, encloseChar);
	}

	public String checkForNullEmptyValHashSet(HashSet<String> currHashSet, String encloseChar)
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

	public ArrayList<MatchedPartnerDetails> buildQueryToGetIDNames(ArrayList<MatchedPartnerDetails> partDets)
	{
			String query = "";
			int count = 0; // just only one memberID
			String GothramIDKey = partDets.get(count).getGothramID();
			String RelationShipIDKey = partDets.get(count).getRelationShipID();
			String LanguageIDKey = partDets.get(count).getLanguageID();
			String RaasiIDKey = partDets.get(count).getRaasiID();
			String ReligionIDKey = partDets.get(count).getReligionID();
			String StarIDKey = partDets.get(count).getStarID();
			String CasteIDKey = partDets.get(count).getCasteID();
			String SubCasteIDKey = partDets.get(count).getSubCasteID();
			String CountryIDKey = partDets.get(count).getCountryID();
			String StateIDKey = partDets.get(count).getStateID();
			String CityIDKey = partDets.get(count).getCityID();
			String QualificationIDKey = partDets.get(count).getQualificationID();
			String ProfessionIDKey = partDets.get(count).getProfessionID();
			String FoodTypeIDKey = partDets.get(count).getFoodTypeID();

			query = "SELECT gt.GothramName, ms.RelationShipName, ln.LanguageName, rs.RaasiName, rd.ReligionName,"
					+ " sd.StarName, ct.casteName, sc.SubCasteName, cn.CountryName, st.StateName, cy.CityName, "
					+ "ql.QualificationName, pr.ProfessionName, ft.FoodType FROM gothram gt, martialstatus ms, "
					+ "language ln, raasi rs, religiondetails rd, stardetails sd, caste ct, subcaste sc, "
					+ "country cn, state st, city cy, qualifications ql, professions pr, foodtype ft "
					+ "WHERE gt.GothramID = '"+GothramIDKey+"'"
					+ " AND ms.RelationShipID = '"+RelationShipIDKey+"'"
					+ " AND ln.LanguageID = '"+LanguageIDKey+"'"
					+ " AND rs.RaasiID = '"+RaasiIDKey+"'"
					+ " AND rd.ReligionID = '"+ReligionIDKey+"'"
					+ " AND sd.StarID = '"+StarIDKey+"'"
					+ " AND ct.CasteID = '"+CasteIDKey+"'"
					+ " AND sc.SubCasteID = '"+SubCasteIDKey+"'"
					+ " AND cn.CountryID = '"+CountryIDKey+"'"
					+ " AND st.StateID = '"+StateIDKey+"'"
					+ " AND cy.CityID = '"+CityIDKey+"'"
					+ " AND ql.QualificationID = '"+QualificationIDKey+"'"
					+ " AND pr.ProfessionID = '"+ProfessionIDKey+"'"
					+ " AND ft.FoodTypeID = '"+FoodTypeIDKey+"'";
			return executeQueryToGetIDNames(query, partDets);
	}

	public ArrayList<MatchedPartnerDetails> executeQueryToGetIDNames(String sqlQuery, ArrayList<MatchedPartnerDetails> partDets)
	{
		Connection conn = getConnection();
		this.setConn(conn);
		logger.debug("DB connection established ");
		try
		{
			PreparedStatement prepStmt = conn.prepareStatement(sqlQuery);
			logger.debug("prepStmt to be executed = "+prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			logger.debug("parsing result set data FOR GETTING ID NAMES. sqlQuery = "+sqlQuery);
			while(rs.next())
			{
				partDets.get(0). setGothramName(rs.getString("GothramName"));
				partDets.get(0). setRelationShipName(rs.getString("RelationShipName"));
				partDets.get(0). setLanguageName(rs.getString("LanguageName"));
				partDets.get(0). setRaasiName(rs.getString("RaasiName"));
				partDets.get(0). setReligionName(rs.getString("ReligionName"));
				partDets.get(0). setStarName(rs.getString("StarName"));
				partDets.get(0). setCasteName(rs.getString("casteName"));
				partDets.get(0). setSubCasteName(rs.getString("SubCasteName"));
				partDets.get(0). setCountryName(rs.getString("CountryName"));
				partDets.get(0). setStateName(rs.getString("StateName"));
				partDets.get(0). setCityName(rs.getString("CityName"));
				partDets.get(0). setQualificationName(rs.getString("QualificationName"));
				partDets.get(0). setProfessionName(rs.getString("ProfessionName"));
				partDets.get(0). setFoodType(rs.getString("FoodType"));
			}
			rs.close();
			closeResources();
		}
		catch (SQLException e)
		{
			closeResources();
			logger.debug(" SQL EXCEPTION OCCURRED while parsing result set");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		}
		return partDets;
	}
}
