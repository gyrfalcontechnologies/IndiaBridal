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

public class GetUserAstroDetailsCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserAstroDetailsCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received : response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("responseMap for User astro details = "+responseMap);
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
		String finalQuery =  "Select RaasiID, StarID, Dosham, HoroscopeLoc FROM usermasterdetails WHERE MemberID = ?";
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
					String starID = rs.getString("StarID");
					String raasiID = rs.getString("RaasiID");
					String dosham = rs.getString("Dosham");
					String horLoc = rs.getString("HoroscopeLoc");

					resultMap.put("starID",Utilities.isNullOrEmpty(starID)?"":starID);
					resultMap.put("raasiID",Utilities.isNullOrEmpty(raasiID)?"":raasiID);
					resultMap.put("dosham",Utilities.isNullOrEmpty(dosham)?"":dosham);
					resultMap.put("horLoc",Utilities.isNullOrEmpty(horLoc)?"":horLoc);
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
