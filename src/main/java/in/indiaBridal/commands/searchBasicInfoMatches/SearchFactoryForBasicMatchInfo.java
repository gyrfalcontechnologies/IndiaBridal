package in.indiaBridal.commands.searchBasicInfoMatches;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.commands.analyseMatches.PartnerPrefDetails;

public class SearchFactoryForBasicMatchInfo 
{
	final static Logger logger = Logger.getLogger(SearchFactoryForBasicMatchInfo.class);
	PartnerPrefDetails partPrefDetObj = null;
	public static void main(String args[])
	{
		SearchFactoryForBasicMatchInfo searchObj = new SearchFactoryForBasicMatchInfo();
		HashMap resMap = searchObj.generateResponseJSON("IBM10000004");
		logger.debug("resMap = "+resMap);
		
		resMap = searchObj.generateResponseJSON("IBM10000004", "getFeaturedProfiles", 0);
		logger.debug("resMap = "+resMap);
	}

	public HashMap generateResponseJSON(String memberID)
	{
		HashMap responseMap = getResponseHashMap(memberID);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("jsonArr", retJSONArr);
		return returnMap;
	}

	public HashMap generateResponseJSON(String memberID, String currPage, int offsetVal)
	{
		HashMap responseMap = getResponseHashMap(memberID, currPage, offsetVal);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("jsonArr", retJSONArr);
		return returnMap;
	}
	
	public HashMap getResponseHashMap(String memberID)
	{
		HashMap returnMap = new HashMap();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("isnavPageDataAvailable", "true");
		returnMap.put("isPrefUpdated", "true");
		HashMap navPageData = new HashMap();

		HashMap bestMatchesPageMap = getResHashMapForProfilesBasedOnPref(memberID, 0);
		navPageData.put("bestMatches", (bestMatchesPageMap==null?"":bestMatchesPageMap));

		HashMap featuredMatchesPageMap = getResHashMapForFeaturedProfiles(memberID, 0);
		navPageData.put("featuredProf", (featuredMatchesPageMap==null?"":featuredMatchesPageMap));

		HashMap shortlistedMatchesPageMap = getResHashMapForShortlistedProfiles(memberID, 0);
		navPageData.put("shortlistedProf", (shortlistedMatchesPageMap==null?"":shortlistedMatchesPageMap));

		HashMap shortlistedYouMatchesPageMap = getResHashMapForProfilesShorlistedYou(memberID, 0);
		navPageData.put("profShortlistedYou", (shortlistedYouMatchesPageMap==null?"":shortlistedYouMatchesPageMap));

		returnMap.put("navPageData", navPageData);
		return returnMap;
	}

	public HashMap getResponseHashMap(String memberID, String currPage, int offsetVal)
	{
		HashMap returnMap = new HashMap();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("isnavPageDataAvailable", "true");
		returnMap.put("isPrefUpdated", "true");
		HashMap navPageData = new HashMap();

		if("getBestMatchedProfiles".equalsIgnoreCase(currPage))
		{
			HashMap bestMatchesPageMap = getResHashMapForProfilesBasedOnPref(memberID, offsetVal);
			navPageData.put("bestMatches", (bestMatchesPageMap==null?"":bestMatchesPageMap));
		}
		else if("getFeaturedProfiles".equalsIgnoreCase(currPage))
		{
			HashMap featuredMatchesPageMap = getResHashMapForFeaturedProfiles(memberID, offsetVal);
			navPageData.put("featuredProf", (featuredMatchesPageMap==null?"":featuredMatchesPageMap));
		}
		else if("getShortlistedProfiles".equalsIgnoreCase(currPage))
		{
			HashMap shortlistedMatchesPageMap = getResHashMapForShortlistedProfiles(memberID, offsetVal);
			navPageData.put("shortlistedProf", (shortlistedMatchesPageMap==null?"":shortlistedMatchesPageMap));
		}
		else if("getProfilesShortlistedYou".equalsIgnoreCase(currPage))
		{
			HashMap shortlistedYouMatchesPageMap = getResHashMapForProfilesShorlistedYou(memberID, offsetVal);
			navPageData.put("profShortlistedYou", (shortlistedYouMatchesPageMap==null?"":shortlistedYouMatchesPageMap));
		}
		returnMap.put("navPageData", navPageData);
		return returnMap;
	}


	public HashMap getResHashMapForProfilesBasedOnPref(String memberID, int offsetVal)
	{
		FetchBasicInfoForProfilesBasedOnPrefsCommand basicInfoOnPrefObj = new FetchBasicInfoForProfilesBasedOnPrefsCommand();
		HashMap responseMap = basicInfoOnPrefObj.getResponseHashMap(memberID, offsetVal);
		partPrefDetObj = (PartnerPrefDetails) responseMap.get("partPrefDet");
		responseMap.remove("partPrefDet");
		HashMap navPageMap = parseNavPageData(responseMap);
		if(navPageMap!=null && !navPageMap.isEmpty())
			return navPageMap;
		else return null;
	}

	public HashMap getResHashMapForFeaturedProfiles(String memberID, int offsetVal)
	{
		FetchBasicInfoForFeateuredProfilesCommand basicInfoOnFeaturdProfObj = new FetchBasicInfoForFeateuredProfilesCommand();
		HashMap navPageMap = parseNavPageData(basicInfoOnFeaturdProfObj.getResponseHashMap(memberID, partPrefDetObj, offsetVal));
		if(navPageMap!=null && !navPageMap.isEmpty())
			return navPageMap;
		else return null;
	}

	public HashMap getResHashMapForShortlistedProfiles(String memberID, int offsetVal)
	{
		FetchBasicInfoForShortlistedProfilesCommand fetchshortlistedProfObj = new FetchBasicInfoForShortlistedProfilesCommand();
		HashMap navPageMap = parseNavPageData(fetchshortlistedProfObj.getResponseHashMap(memberID, offsetVal));
		if(navPageMap!=null && !navPageMap.isEmpty())
			return navPageMap;
		else return null;
	}

	public HashMap getResHashMapForProfilesShorlistedYou(String memberID, int offsetVal)
	{
		FetchBasicInfoForProfilesShortlistedYouCommand fetchProfshortlistedYouObj = new FetchBasicInfoForProfilesShortlistedYouCommand();
		HashMap navPageMap = parseNavPageData(fetchProfshortlistedYouObj.getResponseHashMap(memberID, offsetVal));
		if(navPageMap!=null && !navPageMap.isEmpty())
			return navPageMap;
		else return null;
	}

	public HashMap parseNavPageData(HashMap navMap)
	{	
		if(navMap!=null && !navMap.isEmpty())
		{
			if("success".equalsIgnoreCase((String) navMap.get("status")))
			{
				if("true".equalsIgnoreCase((String) navMap.get("isnavPageDataAvailable")))
				{
					HashMap navPageMap = (HashMap) navMap.get("navPageData");
					if(navPageMap!=null && !navPageMap.isEmpty())
						return navPageMap;
					else
						return null;
				}
				else return null;
			}
			else return null;
		}
		else return null;
	}

}

