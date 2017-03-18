package in.indiaBridal.commands.shortList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.DBConnectionHandler;
import in.indiaBridal.commands.FetchEmailIDOfAProfile;
import in.indiaBridal.commands.SendEmailCommand;

public class SendInterestToAProfileCommand extends DBConnectionHandler
{
	final static Logger logger = Logger.getLogger(SendInterestToAProfileCommand.class);
	public HashMap generateResponseJSON(String memberID, HashMap userDataMap)
	{
		String memIDToShortlist = (String) userDataMap.get("memIDToShortList");
		HashMap responseMap = getResponseHashMap(memberID,memIDToShortlist);
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
		JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "success");
		returnMap.put("errorMsg", "success");
		returnMap.put("jsonArr", retJSONArr);
		return returnMap;
	}
	
	public HashMap getResponseHashMap(String memberID, String memIDToSendIntrest)
	{
		Connection conn = getConnection();
		this.setConn(conn);
		logger.debug("memberID = "+memberID+" new DB connection established.");

		logger.debug("memberID = "+memberID+" MemIDToShortlist = "+memIDToSendIntrest);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		if(conn!=null)
		{
			try 
			{
				logger.debug("memberID = "+memberID+"getting INSERT STMT...");
				String query = getInsertionStmt();
				logger.debug("memberID = "+memberID+" sql stmt = "+query);
				PreparedStatement prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, memberID);
				prepStmt.setString(2, memIDToSendIntrest);
				prepStmt.setTimestamp(3,Utilities.getCurrentTimeStamp());
				int rowsAffected = prepStmt.executeUpdate();
				prepStmt.close();
				closeResources();
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("isIntrestSent", rowsAffected==1?"true":"false");
				if(rowsAffected==1)
				{
					FetchEmailIDOfAProfile emailIDObj = new FetchEmailIDOfAProfile();
					String toEmailID = emailIDObj.getEmailID(memIDToSendIntrest);
					if(toEmailID!=null && !toEmailID.isEmpty())
					{
						SendEmailCommand sendEmailObj = new SendEmailCommand();
						sendEmailObj.sendEmail(toEmailID, "Received a new Request from "+memberID, getHTMLBodyContent());
						logger.debug("send interest email complete");
					}
				}
				return returnMap;
			}
			catch (SQLException e) 
			{
				closeResources();
				logger.debug("memberID = "+memberID+" Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
				returnMap.put("isProfileShortlistedSuccess", "false");
				logger.debug("memberID = "+memberID+" SQL EXCEPTION OCCURRED");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug("memberID = "+memberID+" statck trace = "+stack);
				return returnMap;
			}
		}
		else
		{
			logger.debug("memberID = "+memberID+" DB CONNECTION IS NULL");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "ERROR IN JDBC CONNECTION");
			returnMap.put("isProfileShortlistedSuccess", "false");
			return returnMap;
		}
	}
	
	public String getInsertionStmt()
	{
		String query = "INSERT INTO useractivity  "
				+ "(memberID, interestedMemID, interestedMemIDTS) "
				+ "VALUES (?,?,?)";
		return query;
	}
	
	public String getHTMLBodyContent()
	{
		return "";
	}
	
}
