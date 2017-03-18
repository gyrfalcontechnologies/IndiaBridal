package in.indiaBridal.commands.searchBasicInfoMatches;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.log4j.Logger;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;
import in.indiaBridal.commands.analyseMatches.MatchedPartnerDetails;
import in.indiaBridal.commands.analyseMatches.PartnerPrefDetails;
import in.indiaBridal.commands.getProfile.GetUserPartnerPrefDetailsCommand;

public class QueryExecutorCommand extends DBConnectionHandler
{
	final static Logger logger = Logger.getLogger(QueryExecutorCommand.class);
	int totalMatchedPrefForEachProfile = 0;
	static HashMap<String,String> IDValMap = new HashMap<String,String>();

	public HashMap getResponseHashMap(String memberID, String sqlQuery)
	{
		HashMap returnMap = new HashMap();
		ArrayList<MatchedPartnerDetails> matchdPartDetlsListObj= executeQuery(sqlQuery);
		if(matchdPartDetlsListObj!=null && !matchdPartDetlsListObj.isEmpty())
		{
			int totalProfiles = matchdPartDetlsListObj.size();
			buildQueryToGetIDNames(matchdPartDetlsListObj);
			HashMap matchedProfBasicInfoMap = new HashMap();
			for(int count=0;count<totalProfiles;count++)
			{						
				HashMap	currMatchedProfileBasicInfoMap = new HashMap();
				currMatchedProfileBasicInfoMap.put("relationShip", IDValMap.get(matchdPartDetlsListObj.get(count).getRelationShipID()));
				currMatchedProfileBasicInfoMap.put("raasi",  IDValMap.get(matchdPartDetlsListObj.get(count).getRaasiID()));
				currMatchedProfileBasicInfoMap.put("religion",  IDValMap.get(matchdPartDetlsListObj.get(count).getReligionID()));
				currMatchedProfileBasicInfoMap.put("star",  IDValMap.get(matchdPartDetlsListObj.get(count).getStarID()));
				currMatchedProfileBasicInfoMap.put("caste",  IDValMap.get(matchdPartDetlsListObj.get(count).getCasteID()));
				currMatchedProfileBasicInfoMap.put("profession",  IDValMap.get(matchdPartDetlsListObj.get(count).getProfessionID()));
				currMatchedProfileBasicInfoMap.put("age",  matchdPartDetlsListObj.get(count).getAge());
				currMatchedProfileBasicInfoMap.put("height",  matchdPartDetlsListObj.get(count).getHeight());
				currMatchedProfileBasicInfoMap.put("weight",  matchdPartDetlsListObj.get(count).getWeight());
				currMatchedProfileBasicInfoMap.put("income",  matchdPartDetlsListObj.get(count).getIncome_LL() +" - "+matchdPartDetlsListObj.get(count).getIncome_UL());
				currMatchedProfileBasicInfoMap.put("memberID",  matchdPartDetlsListObj.get(count).getMemberID());
				currMatchedProfileBasicInfoMap.put("fullName",  matchdPartDetlsListObj.get(count).getFullName());
				currMatchedProfileBasicInfoMap.put("gender",  matchdPartDetlsListObj.get(count).getGender());
				currMatchedProfileBasicInfoMap.put("userPics",  matchdPartDetlsListObj.get(count).getUserPics());
				currMatchedProfileBasicInfoMap.put("isPremiumMember",  matchdPartDetlsListObj.get(count).getIsPremiumMember());

				matchedProfBasicInfoMap.put("match"+count, currMatchedProfileBasicInfoMap);
				totalMatchedPrefForEachProfile = 0;
			}
			HashMap navPageData = new HashMap();
			navPageData.put("totalProfiles", totalProfiles+"");
			navPageData.put("matchedBasicProfData", matchedProfBasicInfoMap);

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
				navPageData.put("matchedBasicProfData", new HashMap());

				returnMap.put("status", "success");
				returnMap.put("errorMsg", "No profiles found");
				returnMap.put("isnavPageDataAvailable", "true");
				returnMap.put("navPageData", navPageData);
			}
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
				while(rs.next())
				{
					MatchedPartnerDetails matchPartObj = new MatchedPartnerDetails();
					matchPartObj.setMemberID(Utilities.isNullOrEmpty(rs.getString("memberID"))?"":rs.getString("memberID"));
					matchPartObj.setFullName(Utilities.isNullOrEmpty(rs.getString("FullName"))?"":rs.getString("FullName"));
					matchPartObj.setAge(Utilities.isNullOrEmpty(rs.getString("age"))?"":rs.getString("age"));
					matchPartObj.setRelationShipID(Utilities.isNullOrEmpty(rs.getString("MartialStatusID"))?"":rs.getString("MartialStatusID"));
					matchPartObj.setHeight(Utilities.isNullOrEmpty(rs.getString("Height"))?"":rs.getString("Height"));
					matchPartObj.setWeight(Utilities.isNullOrEmpty(rs.getString("Weight"))?"":rs.getString("Weight"));
					matchPartObj.setRaasiID(Utilities.isNullOrEmpty(rs.getString("RaasiID"))?"":rs.getString("RaasiID"));
					matchPartObj.setReligionID(Utilities.isNullOrEmpty(rs.getString("ReligionID"))?"":rs.getString("ReligionID"));
					matchPartObj.setStarID(Utilities.isNullOrEmpty(rs.getString("StarID"))?"":rs.getString("StarID"));
					matchPartObj.setCasteID(Utilities.isNullOrEmpty(rs.getString("CasteID"))?"":rs.getString("CasteID"));
					matchPartObj.setProfessionID(Utilities.isNullOrEmpty(rs.getString("pd.ProfessionID"))?"":rs.getString("pd.ProfessionID"));
					matchPartObj.setIncome_UL(Utilities.isNullOrEmpty(rs.getString("AnnualIncome_UL"))?"":rs.getString("AnnualIncome_UL"));
					matchPartObj.setIncome_LL(Utilities.isNullOrEmpty(rs.getString("AnnualIncome_LL"))?"":rs.getString("AnnualIncome_LL"));
					matchPartObj.setGender(Utilities.isNullOrEmpty(rs.getString("gender"))?"":rs.getString("gender"));
					matchPartObj.setUserPics(Utilities.isNullOrEmpty(rs.getString("Pic1"))?"":rs.getString("Pic1"));
					matchPartObj.setIsPremiumMember(Utilities.isNullOrEmpty(rs.getString("isPremiumMember"))?"no":rs.getString("isPremiumMember"));
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

	public void buildQueryToGetIDNames(ArrayList<MatchedPartnerDetails> partDets)
	{
		for(int count=0;count<partDets.size();count++)
		{
			String query = "";
			String currIDKey = partDets.get(count).getRelationShipID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select ms.RelationShipName, ms.RelationShipID from martialstatus ms where ms.RelationShipID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}

			currIDKey = partDets.get(count).getRaasiID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select rs.RaasiName, rs.RaasiID from raasi rs where rs.RaasiID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}

			currIDKey = partDets.get(count).getReligionID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select rd.ReligionName, rd.ReligionID from religiondetails rd where rd.ReligionID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}

			currIDKey = partDets.get(count).getStarID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select sd.StarName, sd.StarID from stardetails sd where sd.StarID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}

			currIDKey = partDets.get(count).getCasteID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select ct.casteName, ct.CasteID from caste ct where ct.CasteID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}

			currIDKey = partDets.get(count).getProfessionID();
			if(IDValMap.get(currIDKey)==null)
			{
				query = "select pr.ProfessionName, pr.ProfessionID from professions pr where pr.ProfessionID = '"+currIDKey+"'";
				executeQueryToGetIDNames(query);
			}
		}
	}

	public void executeQueryToGetIDNames(String sqlQuery)
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
				IDValMap.put(rs.getString(2), rs.getString(1));
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
	}
}
