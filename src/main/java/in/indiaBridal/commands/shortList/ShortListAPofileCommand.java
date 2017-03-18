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

public class ShortListAPofileCommand extends DBConnectionHandler
{
	final static Logger logger = Logger.getLogger(ShortListAPofileCommand.class);
	
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
	
	public HashMap getResponseHashMap(String memberID, String memIDToShortlist)
	{
		Connection conn = getConnection();
		this.setConn(conn);
		logger.debug("memberID = "+memberID+" new DB connection established.");

		logger.debug("memberID = "+memberID+" MemIDToShortlist = "+memIDToShortlist);
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
				prepStmt.setString(2, memIDToShortlist);
				prepStmt.setTimestamp(3,Utilities.getCurrentTimeStamp());
				int rowsAffected = prepStmt.executeUpdate();
				prepStmt.close();
				closeResources();
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("isProfileShortlistedSuccess", rowsAffected==1?"true":"false");
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
				+ "(memberID, shorlistedMemID, shortListedMemIDTS) "
				+ "VALUES (?,?,?)";
		return query;
	}
}
