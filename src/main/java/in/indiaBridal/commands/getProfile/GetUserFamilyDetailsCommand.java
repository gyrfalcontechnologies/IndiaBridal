package in.indiaBridal.commands.getProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;

public class GetUserFamilyDetailsCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserFamilyDetailsCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received : response map = "+responseMap);
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

	public String getUsrFamilyInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery =  "Select FatherStatus, MotherStatus, NativePlace, NoOfSiblings, "
								+ "FamilyTypeID, FamilyValue, FamilyStatus, CountryID, StateID, CityID FROM familydetails WHERE memberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, String> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrFamilyInfoStmt();
		if(sqlStmt!=null && !sqlStmt.isEmpty())
		{
			Connection conn = getConnection();
			this.setConn(conn);
			logger.debug("DB connection established ");
			try
			{
				PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
				prepStmt.setString(1, memberID);
				logger.debug("prepStmt to be executed = "+prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data");
				HashMap<String, String> resultMap = new HashMap<String, String>();
				if(rs.next())
				{
					String fatherStatus = rs.getString("FatherStatus");
					String motherStatus = rs.getString("MotherStatus");
					String nativePlace = rs.getString("NativePlace");
					String noOfSiblings = rs.getString("NoOfSiblings");
					String familyTypeID = rs.getString("FamilyTypeID");
					String familyValue = rs.getString("FamilyValue");
					String familyStatus = rs.getString("FamilyStatus");
					String countryID = rs.getString("CountryID");
					String stateID = rs.getString("StateID");
					String cityID = rs.getString("CityID");

					resultMap.put("fatherStatus",Utilities.isNullOrEmpty(fatherStatus)?"":fatherStatus);
					resultMap.put("motherStatus",Utilities.isNullOrEmpty(motherStatus)?"":motherStatus);
					resultMap.put("nativePlace",Utilities.isNullOrEmpty(nativePlace)?"":nativePlace);
					resultMap.put("noOfSiblings",Utilities.isNullOrEmpty(noOfSiblings)?"":noOfSiblings);
					resultMap.put("familyTypeID",Utilities.isNullOrEmpty(familyTypeID)?"":familyTypeID);
					resultMap.put("familyValue",Utilities.isNullOrEmpty(familyValue)?"":familyValue);
					resultMap.put("familyStatus",Utilities.isNullOrEmpty(familyStatus)?"":familyStatus);
					resultMap.put("countryID",Utilities.isNullOrEmpty(countryID)?"":countryID);
					resultMap.put("stateID",Utilities.isNullOrEmpty(stateID)?"":stateID);
					resultMap.put("cityID",Utilities.isNullOrEmpty(cityID)?"":cityID);
				}
				else
				{
					resultMap.put("fatherStatus","");
					resultMap.put("motherStatus","");
					resultMap.put("nativePlace","");
					resultMap.put("noOfSiblings","");
					resultMap.put("familyTypeID","");
					resultMap.put("familyValue","");
					resultMap.put("familyStatus","");
					resultMap.put("countryID","");
					resultMap.put("stateID","");
					resultMap.put("cityID","");
				}
				rs.close();
				closeResources();
				
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
