package in.indiaBridal.commands.searchBasicInfoMatches;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.analyseMatches.PartnerPrefDetails;

public class FetchBasicInfoForFeateuredProfilesCommand 
{
	final static Logger logger = Logger.getLogger(FetchBasicInfoForFeateuredProfilesCommand.class);
	PartnerPrefDetails partPrefDetObj;
	
	public static void main(String args[])
	{
		FetchBasicInfoForFeateuredProfilesCommand searchObj = new FetchBasicInfoForFeateuredProfilesCommand();
		HashMap resMap = searchObj.generateResponseJSON("IBM10000004", 0);
		logger.debug("resMap = "+resMap);
	}
	
	public HashMap generateResponseJSON(String memberID, int offsetVal)
	{
		HashMap responseMap = getResponseHashMap(memberID, offsetVal);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("jsonArr", retJSONArr);
		return returnMap;
	}
	
	public HashMap getResponseHashMap(String memberID, PartnerPrefDetails partPrefDetObj, int offsetVal)
	{
		HashMap returnMap = new HashMap();
		logger.debug("in fetching featured profiles");
		if(partPrefDetObj==null)
		{
			QueryExecutorCommand fetchBasicPrefInfoObj = new QueryExecutorCommand();
			partPrefDetObj = fetchBasicPrefInfoObj.getPartnerPreferences(memberID);
		}
		if(partPrefDetObj!=null && partPrefDetObj.getStatus().equalsIgnoreCase("success"))
		{
			String sqlQuery = buildQueryBasedOnPref(partPrefDetObj,memberID, offsetVal);
			logger.debug("sqlQuery = "+sqlQuery);
			QueryExecutorCommand fetchBasicPrefInfoObj = new QueryExecutorCommand();
			return fetchBasicPrefInfoObj.getResponseHashMap(memberID, sqlQuery);
		}
		else
		{
			logger.debug("partPrefDetObj is null or empty");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "failed to get partner pref details");
			returnMap.put("isnavPageDataAvailable", "false");
			return returnMap;
		}
	}
	
	public HashMap getResponseHashMap(String memberID, int offsetVal)
	{
		HashMap returnMap = new HashMap();
		QueryExecutorCommand fetchBasicPrefInfoObj = new QueryExecutorCommand();
		partPrefDetObj = fetchBasicPrefInfoObj.getPartnerPreferences(memberID);
		if(partPrefDetObj!=null && partPrefDetObj.getStatus().equalsIgnoreCase("success"))
		{
			String sqlQuery = buildQueryBasedOnPref(partPrefDetObj,memberID, offsetVal);
			return fetchBasicPrefInfoObj.getResponseHashMap(memberID, sqlQuery);
		}
		else
		{
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "failed to get partner pref details");
			returnMap.put("isnavPageDataAvailable", "false");
			return returnMap;
		}
	}
	
	public String buildQueryBasedOnPref(PartnerPrefDetails partPrefDetObj, String memberID, int offsetVal)
	{

		StringBuilder selClauseSB = new StringBuilder();

		selClauseSB.append("SELECT ud.memberID, ud.isPremiumMember, ud.FullName, DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), ud.DOB)), \"%Y\")+0 AS age, "
				+ "ud.MartialStatusID, ud.Height, ud.Weight, ud.RaasiID, ud.ReligionID, ud.StarID, ud.CasteID, ud.Pic1, ud.gender, "
				+ "pd.ProfessionID, pd.AnnualIncome_UL, pd.AnnualIncome_LL "
				+ "FROM usermasterdetails ud "
				+ "JOIN lifestyle ls ON ud.MemberID = ls.MemberID "
				+ "JOIN professionaldetails pd ON ud.MemberID = pd.MemberID "
				+ "JOIN familydetails fd ON ud.MemberID = fd.memberID "
				+ "WHERE gender NOT IN (SELECT gender FROM usermasterdetails where MemberID = '"+memberID+"') "
				+ "AND ud.memberID NOT IN (SELECT shorlistedMemID FROM useractivity WHERE memberID ='"+memberID+"') "
				+ "AND LOWER(isPrefUpdated) LIKE LOWER('%true%') AND LOWER(isPremiumMember) LIKE LOWER('%yes%') AND");
		StringBuilder wherClauseSB = new StringBuilder();
		String currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getGothramID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.GothramID IN ("+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getRelationShipID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.MartialStatusID IN ("+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getAge_LL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), ud.DOB)), \"%Y\")+0 > "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getAge_UL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), ud.DOB)), \"%Y\")+0 < "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getHeight_LL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.Height > "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getHeight_UL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.Height < "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getWeight_LL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.Weight > "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getWeight_UL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.Weight < "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getDosham(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.dosham = "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getLanguageID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.MotherTongueID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getRaasiID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.RaasiID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getReligionID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.ReligionID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getStarID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.StarID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getCasteID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.CasteID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getSubCasteID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ud.SubCasteID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getCountryID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" fd.CountryID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getStateID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" fd.StateID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getCityID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" fd.CityID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getQualificationID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" pd.QualificationID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getProfessionID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" pd.ProfessionID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getIncome_LL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" pd.AnnualIncome_LL > "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getIncome_UL(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" pd.AnnualIncome_UL < "+currWhereCond+" OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getDrinker(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ls.Drinker IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getFoodTypeID(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ls.FoodTypeID IN ( "+currWhereCond+") OR");

		currWhereCond = Utilities.getCurrPrefCond(partPrefDetObj.getSmoker(),"'");
		if(!currWhereCond.isEmpty())
			wherClauseSB.append(" ls.Smoker IN ( "+currWhereCond+")");

		String wherClause = Utilities.stripLastChars(wherClauseSB.toString(),"OR");
		if(wherClause.length() > 0)
		{
			selClauseSB.append("(");
			selClauseSB.append(wherClause);
			selClauseSB.append(" )");
			selClauseSB.append(" LIMIT 10 OFFSET "+offsetVal);
			return selClauseSB.toString();
		}
		else
		{
			String currQuery = Utilities.stripLastChars(selClauseSB.toString(),"AND");
			logger.debug("currQuery = "+currQuery);
			currQuery = currQuery +" LIMIT 10 OFFSET "+offsetVal;
			return currQuery;
		}
	}
}
