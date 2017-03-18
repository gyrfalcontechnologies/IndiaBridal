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

public class GetUserLifeStyleInfoCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserLifeStyleInfoCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received : response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("response map for user life style = "+responseMap);
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

	public HashMap<String, String> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrLifeStyleInfoStmt();
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
					String drinker = rs.getString("Drinker");
					String foodTypeID = rs.getString("FoodTypeID");
					String smoker = rs.getString("Smoker");

					resultMap.put("drinker",Utilities.isNullOrEmpty(drinker)?"":drinker);
					resultMap.put("foodTypeID",Utilities.isNullOrEmpty(foodTypeID)?"":foodTypeID);
					resultMap.put("smoker",Utilities.isNullOrEmpty(smoker)?"":smoker);
				}
				else
				{
					resultMap.put("drinker","");
					resultMap.put("foodTypeID","");
					resultMap.put("smoker","");
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
	
	public String getUsrLifeStyleInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery =  "Select Drinker, FoodTypeID, Smoker FROM lifestyle WHERE MemberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}
}
