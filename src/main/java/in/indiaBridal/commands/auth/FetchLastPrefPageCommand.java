package in.indiaBridal.commands.auth;

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

public class FetchLastPrefPageCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(FetchLastPrefPageCommand.class);

	public HashMap<String, Object> getUserPageInfo(String memberID)
	{
		logger.debug("getting user page info for memberid = "+memberID);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String query = "SELECT isPrefUpdated, userCurrPage FROM usermasterdetails WHERE MemberID=?";
		
		try
		{
			if(!Utilities.isNullOrEmpty(query))
			{
				Connection conn = getConnection();
				if(conn!=null)
				{
					this.setConn(conn);
					PreparedStatement prepStmt = conn.prepareStatement(query);
					prepStmt.setString(1, memberID);
					
					logger.debug("prepared stmt to be executed = "+prepStmt);
					ResultSet userPageInfoRS = prepStmt.executeQuery();
					while(userPageInfoRS.next())
					{
						String isPrefUpdated = userPageInfoRS.getString(1);
						String userCurrPage = userPageInfoRS.getString(2);
						logger.debug("memberID = "+memberID+" isPrefUpdated = "+isPrefUpdated+" userCurrPage = "+userCurrPage);
						if(!Utilities.isNullOrEmpty(isPrefUpdated) && !Utilities.isNullOrEmpty(userCurrPage))
						{
							returnMap.put("status", "success");
							returnMap.put("errorMsg", "success");
							returnMap.put("isPrefUpdated", isPrefUpdated);
							returnMap.put("userCurrPage", userCurrPage);
						}
						else
						{
							returnMap.put("status", "failure");
							returnMap.put("errorMsg", "NULL or empty data");
							returnMap.put("isPrefUpdated", "");
							returnMap.put("userCurrPage", "");
						}
						closeResources();
						return returnMap;
					}
					// if no data in result set
					logger.debug("no data in result set");
					closeResources();
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "empty resuts from DB");
					returnMap.put("isPrefUpdated", "");
					returnMap.put("userCurrPage", "");
					return returnMap;
				}
				logger.debug("no data in result set");
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "NO DB connection");
				returnMap.put("isPrefUpdated", "");
				returnMap.put("userCurrPage", "");
				return returnMap;
			}
			else
			{
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "failed to get query");
				returnMap.put("isPrefUpdated", "");
				returnMap.put("userCurrPage", "");
				return returnMap;
			}
		}
		catch (SQLException e) 
		{
			closeResources();
			logger.debug("memberID = "+memberID+" SQL EXCEPTION OCCURRED while getting prepared statement for authentication");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("memberID = "+memberID+" statck trace = "+stack);
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
			returnMap.put("isAuthenticated", "false");
			return returnMap;
		}
	}
}
