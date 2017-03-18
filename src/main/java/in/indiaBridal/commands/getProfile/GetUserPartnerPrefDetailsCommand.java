package in.indiaBridal.commands.getProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;

public class GetUserPartnerPrefDetailsCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserPartnerPrefDetailsCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, HashSet<String>> responseMap = getDataFromDB(memberID);
		logger.debug("data received for all tables: response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("response size and key size are equal");
			returnMap.put("status", "success");
			returnMap.put("errorMsg", "success");
			returnMap.put("userData", responseMap);
		}
		else
		{
			logger.debug("response map is null or empty.");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
		}
		logger.debug("returnMap = "+returnMap);
		return returnMap;
	}

	public String getUsrPartPrefInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery = "SELECT distinct mp.memberID, gp.GothramID, smoker, lsp.FoodTypeID, Drinker, msp.RelationShipID, "
				+ "Age_LL, Age_UL, Height_LL, Height_UL, Weight_LL, Weight_UL, Income_UL, Income_LL, Dosham, "
				+ "Expectations, mtp.LanguageID, pp.ProfessionID, qp.QualificationID, rp.RaasiID, rlp.ReligionID, sp.StarID, cp.CasteID, "
				+ "scp.SubCasteID, lp.countryID, lp.StateID, lp.CityID, "
				// for getting the ID names. will be used in searching partner
				+ "gt.GothramName, ms.RelationShipName, ln.LanguageName, rs.RaasiName, rd.ReligionName, sd.StarName, ct.casteName, "
				+ "sc.SubCasteName, cn.CountryName, ql.QualificationName, pr.ProfessionName, ft.FoodType, cy.CityName, st.StateName "
				+ "FROM castepreferences cp "
				+ "JOIN currentlocpreferences lp ON cp.MemberID = lp.MemberID "
				+ "JOIN gothrampreferences gp ON cp.MemberID = gp.MemberID "
				+ "JOIN lifestylepreferences lsp ON cp.MemberID = lsp.MemberID "
				+ "JOIN martialstatuspreferences msp ON cp.MemberID = msp.MemberID "
				+ "JOIN masterpreferences mp ON cp.MemberID = mp.MemberID "
				+ "JOIN mothertonguepreferences mtp ON cp.MemberID = mtp.MemberID "
				+ "JOIN professionpreferences pp ON cp.MemberID = pp.MemberID "
				+ "JOIN qualificationlpreferences qp ON cp.MemberID = qp.MemberID "
				+ "JOIN raasipreferences rp ON cp.MemberID = rp.MemberID "
				+ "JOIN religionpreferences rlp ON cp.MemberID = rlp.MemberID "
				+ "JOIN starpreferences sp ON cp.MemberID = sp.MemberID "
				+ "JOIN subcastepreferences scp ON cp.MemberID = scp.MemberID "
// for getting the ID names
+ "JOIN gothram gt ON gt.GothramID = gp.GothramID "
+ "JOIN martialstatus ms ON ms.RelationShipID = msp.RelationShipID "
+ "JOIN language ln ON ln.LanguageID = mtp.LanguageID "
+ "JOIN raasi rs ON rs.RaasiID = rp.RaasiID "
+ "JOIN religiondetails rd ON rd.ReligionID = rlp.ReligionID "
+ "JOIN stardetails sd ON sd.StarID = sp.StarID "
+ "JOIN caste ct ON ct.CasteID = cp.CasteID "
+ "JOIN subcaste sc ON sc.SubCasteID = scp.SubCasteID "
+ "JOIN country cn ON cn.CountryID = lp.countryID "
+ "JOIN state st ON st.StateID = lp.StateID "
+ "JOIN city cy ON cy.CityID = lp.CityID "
+ "JOIN qualifications ql ON ql.QualificationID = qp.QualificationID "
+ "JOIN professions pr ON pr.ProfessionID = pp.ProfessionID "
+ "JOIN foodtype ft ON ft.FoodTypeID = lsp.FoodTypeID"
				+ " WHERE mp.MemberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, HashSet<String>> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrPartPrefInfoStmt();
		if(sqlStmt!=null && !sqlStmt.isEmpty())
		{
			logger.debug("DB connection established ");
			try
			{
				HashMap<String, HashSet<String>> resultMap = new HashMap<String, HashSet<String>>();
				HashSet<String> gothramID_list = new HashSet<String>();
				HashSet<String> smoker_list = new HashSet<String>();
				HashSet<String> foodTypeID_list = new HashSet<String>();
				HashSet<String> drinker_list = new HashSet<String>();
				HashSet<String> relationShipID_list = new HashSet<String>();
				HashSet<String> age_LL_list = new HashSet<String>();
				HashSet<String> age_UL_list = new HashSet<String>();
				HashSet<String> height_LL_list = new HashSet<String>();
				HashSet<String> height_UL_list = new HashSet<String>();
				HashSet<String> weight_LL_list = new HashSet<String>();
				HashSet<String> weight_UL_list = new HashSet<String>();
				HashSet<String> income_UL_list = new HashSet<String>();
				HashSet<String> income_LL_list = new HashSet<String>();
				HashSet<String> dosham_list = new HashSet<String>();
				HashSet<String> expectations_list = new HashSet<String>();
				HashSet<String> languageID_list = new HashSet<String>();
				HashSet<String> professionID_list = new HashSet<String>();
				HashSet<String> qualificationID_list = new HashSet<String>();
				HashSet<String> raasiID_list = new HashSet<String>();
				HashSet<String> religionID_list = new HashSet<String>();
				HashSet<String> starID_list = new HashSet<String>();
				HashSet<String> casteID_list = new HashSet<String>();
				HashSet<String> subCasteID_list = new HashSet<String>();
				HashSet<String> countryID_list = new HashSet<String>();
				HashSet<String> stateID_list = new HashSet<String>();
				HashSet<String> cityID_list = new HashSet<String>();
				
				HashSet<String> gothramName_list = new HashSet<String>();
				HashSet<String> relationShipName_list = new HashSet<String>();
				HashSet<String> languageName_list = new HashSet<String>();
				HashSet<String> raasiName_list = new HashSet<String>();
				HashSet<String> religionName_list = new HashSet<String>();
				HashSet<String> starName_list = new HashSet<String>();
				HashSet<String> casteName_list = new HashSet<String>();
				HashSet<String> subCasteName_list = new HashSet<String>();
				HashSet<String> countryName_list = new HashSet<String>();
				HashSet<String> qualificationName_list = new HashSet<String>();
				HashSet<String> professionName_list = new HashSet<String>();
				HashSet<String> foodType_list = new HashSet<String>();
				HashSet<String> stateName_list = new HashSet<String>();
				HashSet<String> cityName_list = new HashSet<String>();

				Connection conn = getConnection();
				this.setConn(conn);
				PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
				prepStmt.setString(1, memberID);
				logger.debug("prepStmt to be executed = "+prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data. result set = "+rs);
				while(rs.next())
				{
					String gothramID = rs.getString("gothramID");
					String smoker = rs.getString("smoker");
					String foodTypeID = rs.getString("foodTypeID");
					String drinker = rs.getString("drinker");
					String relationShipID = rs.getString("relationShipID");
					String age_LL = rs.getString("Age_LL");
					String age_UL = rs.getString("Age_UL");
					String height_LL = rs.getString("Height_LL");
					String height_UL = rs.getString("Height_UL");
					String weight_LL = rs.getString("Weight_LL");
					String weight_UL = rs.getString("Weight_UL");
					String income_UL = rs.getString("Income_UL");
					String income_LL = rs.getString("Income_LL");
					String dosham = rs.getString("Dosham");
					String expectations = rs.getString("Expectations");
					String languageID = rs.getString("LanguageID");
					String professionID = rs.getString("ProfessionID");
					String qualificationID = rs.getString("QualificationID");
					String raasiID = rs.getString("RaasiID");
					String religionID = rs.getString("ReligionID");
					String starID = rs.getString("StarID");
					String casteID = rs.getString("CasteID");
					String subCasteID = rs.getString("SubCasteID");
					String countryID = rs.getString("countryID");
					String stateID = rs.getString("StateID");
					String cityID = rs.getString("CityID");

					String gothramName = rs.getString("GothramName");
					String relationShipName = rs.getString("RelationShipName");
					String languageName = rs.getString("LanguageName");
					String raasiName = rs.getString("RaasiName");
					String religionName = rs.getString("ReligionName");
					String starName = rs.getString("StarName");
					String casteName = rs.getString("casteName");
					String subCasteName = rs.getString("SubCasteName");
					String countryName = rs.getString("CountryName");
					String qualificationName = rs.getString("QualificationName");
					String professionName = rs.getString("ProfessionName");
					String foodType = rs.getString("FoodType");
					String stateName = rs.getString("StateName");
					String cityName = rs.getString("CityName");
					
					gothramID_list.add(Utilities.isNullOrEmpty(gothramID)?"":gothramID);
					smoker_list.add(Utilities.isNullOrEmpty(smoker)?"":smoker);
					foodTypeID_list.add(Utilities.isNullOrEmpty(foodTypeID)?"":foodTypeID);
					drinker_list.add(Utilities.isNullOrEmpty(drinker)?"":drinker);
					relationShipID_list.add(Utilities.isNullOrEmpty(relationShipID)?"":relationShipID);
					age_LL_list.add(Utilities.isNullOrEmpty(age_LL)?"":age_LL);
					age_UL_list.add(Utilities.isNullOrEmpty(age_UL)?"":age_UL);
					height_LL_list.add(Utilities.isNullOrEmpty(height_LL)?"":height_LL);
					height_UL_list.add(Utilities.isNullOrEmpty(height_UL)?"":height_UL);
					weight_LL_list.add(Utilities.isNullOrEmpty(weight_LL)?"":weight_LL);
					weight_UL_list.add(Utilities.isNullOrEmpty(weight_UL)?"":weight_UL);
					income_UL_list.add(Utilities.isNullOrEmpty(income_UL)?"":income_UL);
					income_LL_list.add(Utilities.isNullOrEmpty(income_LL)?"":income_LL);
					dosham_list.add(Utilities.isNullOrEmpty(dosham)?"":dosham);
					expectations_list.add(Utilities.isNullOrEmpty(expectations)?"":expectations);
					languageID_list.add(Utilities.isNullOrEmpty(languageID)?"":languageID);
					professionID_list.add(Utilities.isNullOrEmpty(professionID)?"":professionID);
					qualificationID_list.add(Utilities.isNullOrEmpty(qualificationID)?"":qualificationID);
					raasiID_list.add(Utilities.isNullOrEmpty(raasiID)?"":raasiID);
					religionID_list.add(Utilities.isNullOrEmpty(religionID)?"":religionID);
					starID_list.add(Utilities.isNullOrEmpty(starID)?"":starID);
					casteID_list.add(Utilities.isNullOrEmpty(casteID)?"":casteID);
					subCasteID_list.add(Utilities.isNullOrEmpty(subCasteID)?"":subCasteID);
					countryID_list.add(Utilities.isNullOrEmpty(countryID)?"":countryID);
					stateID_list.add(Utilities.isNullOrEmpty(stateID)?"":stateID);
					cityID_list.add(Utilities.isNullOrEmpty(cityID)?"":cityID);
					
					gothramName_list.add(Utilities.isNullOrEmpty(gothramName)?"":gothramName);
					relationShipName_list.add(Utilities.isNullOrEmpty(relationShipName)?"":relationShipName);
					languageName_list.add(Utilities.isNullOrEmpty(languageName)?"":languageName);
					raasiName_list.add(Utilities.isNullOrEmpty(raasiName)?"":raasiName);
					religionName_list.add(Utilities.isNullOrEmpty(religionName)?"":religionName);
					starName_list.add(Utilities.isNullOrEmpty(starName)?"":starName);
					casteName_list.add(Utilities.isNullOrEmpty(casteName)?"":casteName);
					subCasteName_list.add(Utilities.isNullOrEmpty(subCasteName)?"":subCasteName);
					countryName_list.add(Utilities.isNullOrEmpty(countryName)?"":countryName);
					qualificationName_list.add(Utilities.isNullOrEmpty(qualificationName)?"":qualificationName);
					professionName_list.add(Utilities.isNullOrEmpty(professionName)?"":professionName);
					foodType_list.add(Utilities.isNullOrEmpty(foodType)?"":foodType);
					stateName_list.add(Utilities.isNullOrEmpty(stateName)?"":stateName);
					cityName_list.add(Utilities.isNullOrEmpty(cityName)?"":cityName);
				}
				rs.close();
				closeResources();
				resultMap.put("gothramID",gothramID_list);
				resultMap.put("smoker",smoker_list);
				resultMap.put("foodTypeID",foodTypeID_list);
				resultMap.put("drinker",drinker_list);
				resultMap.put("martialStatusID",relationShipID_list);
				resultMap.put("age_LL",age_LL_list);
				resultMap.put("age_UL",age_UL_list);
				resultMap.put("height_LL",height_LL_list);
				resultMap.put("height_UL",height_UL_list);
				resultMap.put("weight_LL",weight_LL_list);
				resultMap.put("weight_UL",weight_UL_list);
				resultMap.put("income_UL",income_UL_list);
				resultMap.put("income_LL",income_LL_list);
				resultMap.put("dosham",dosham_list);
				resultMap.put("expectations",expectations_list);
				resultMap.put("languageID",languageID_list);
				resultMap.put("professionID",professionID_list);
				resultMap.put("educationID",qualificationID_list);
				resultMap.put("raasiID",raasiID_list);
				resultMap.put("religionID",religionID_list);
				resultMap.put("starID",starID_list);
				resultMap.put("casteID",casteID_list);
				resultMap.put("subCasteID",subCasteID_list);
				resultMap.put("countryID",countryID_list);
				resultMap.put("stateID",stateID_list);
				resultMap.put("cityID",cityID_list);

				resultMap.put("gothramName",gothramName_list);
				resultMap.put("relationShipName",relationShipName_list);
				resultMap.put("languageName",languageName_list);
				resultMap.put("raasiName",raasiName_list);
				resultMap.put("religionName",religionName_list);
				resultMap.put("starName",starName_list);
				resultMap.put("casteName",casteName_list);
				resultMap.put("subCasteName",subCasteName_list);
				resultMap.put("countryName",countryName_list);
				resultMap.put("qualificationName",qualificationName_list);
				resultMap.put("professionName",professionName_list);
				resultMap.put("foodType",foodType_list);
				resultMap.put("stateName",stateName_list);
				resultMap.put("cityName",cityName_list);

				
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
}
