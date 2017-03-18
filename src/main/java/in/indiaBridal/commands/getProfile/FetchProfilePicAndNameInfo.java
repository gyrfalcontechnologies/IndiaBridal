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

public class FetchProfilePicAndNameInfo extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(FetchProfilePicAndNameInfo.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received for all tables: response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("response size and key size are equal");
			returnMap.put("status", "success");
			returnMap.put("errorMsg", "success");
			returnMap.put("userData", responseMap==null?"":responseMap);
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

	public String getUsrBasicInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery =  "Select Pic1, FullName FROM usermasterdetails WHERE MemberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, String> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrBasicInfoStmt();
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
				while(rs.next())
				{
					String profilePic = rs.getString("Pic1");
					String fullName = rs.getString("FullName");
				

					resultMap.put("profilePic",Utilities.isNullOrEmpty(profilePic)?"":profilePic);
					resultMap.put("fullName",Utilities.isNullOrEmpty(fullName)?"":fullName);
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
