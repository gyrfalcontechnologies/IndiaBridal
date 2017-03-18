package in.indiaBridal.commands.searchBasicInfoMatches;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;

public class FetchBasicInfoForShortlistedProfilesCommand 
{
	final static Logger logger = Logger.getLogger(FetchBasicInfoForShortlistedProfilesCommand.class);

	public static void main(String args[])
	{
		FetchBasicInfoForShortlistedProfilesCommand searchObj = new FetchBasicInfoForShortlistedProfilesCommand();
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

	public HashMap getResponseHashMap(String memberID, int offsetVal)
	{
		QueryExecutorCommand fetchBasicPrefInfoObj = new QueryExecutorCommand();
		String sqlQuery = buildQueryBasedOnPref(memberID, offsetVal);
		return fetchBasicPrefInfoObj.getResponseHashMap(memberID, sqlQuery);
	}

	public String buildQueryBasedOnPref(String memberID, int offsetVal)
	{
		StringBuilder selClauseSB = new StringBuilder();

		selClauseSB.append("SELECT ud.memberID, ud.isPremiumMember, ud.FullName, DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), ud.DOB)), \"%Y\")+0 AS age, "
				+ "ud.MartialStatusID, ud.Height, ud.Weight, ud.RaasiID, ud.ReligionID, ud.StarID, ud.CasteID, ud.Pic1, ud.gender, "
				+ "pd.ProfessionID, pd.AnnualIncome_UL, pd.AnnualIncome_LL "
				+ "FROM usermasterdetails ud "
				+ "JOIN lifestyle ls ON ud.MemberID = ls.MemberID "
				+ "JOIN professionaldetails pd ON ud.MemberID = pd.MemberID "
				+ "JOIN familydetails fd ON ud.MemberID = fd.memberID "
				+ "WHERE ud.memberID IN (SELECT shorlistedMemID FROM useractivity where memberID = '"+memberID+"') "
				+ "AND ud.memberID NOT IN (SELECT shorlistedMemID FROM useractivity WHERE memberID ='"+memberID+"') "
				+ "LIMIT 10 OFFSET "+offsetVal);
				
		return selClauseSB.toString();
	}
}
