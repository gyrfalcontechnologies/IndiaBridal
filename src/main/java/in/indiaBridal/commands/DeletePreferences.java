package in.indiaBridal.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;

public class DeletePreferences extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(DeletePreferences.class);
	private Statement stmt = null;
	private Connection conn = null;

	public HashMap<String, Object> getResponseHashMap(String subSel,String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		conn = getConnection();
		this.setConn(conn);
		logger.debug("memberID = "+memberID+" DB connection established. connection object = "+conn);
		logger.debug("memberID = "+memberID+" about to call update user data");
		HashMap<String, Object> responseMap = deletePreferences(subSel,memberID);
		closeResources();
		logger.debug("memberID = "+memberID+" DB resources closed");
		if(responseMap!=null && !responseMap.isEmpty())
		{
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				HashMap<String, Object> resultMap = ((HashMap<String, Object>) responseMap.get("resultMap"));
				HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
				JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(resultMap);
				JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("jsonArr", retJSONArr);
			}
			else
			{
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", responseMap.get("errorMsg"));
				returnMap.put("jsonArr", null);
			}
		}
		else
		{
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			returnMap.put("jsonArr", null);
		}
		return returnMap;
	}

	public HashMap<String, Object> deletePreferences(String userSel,String memberID)
	{
		logger.debug("memberID = "+memberID+" userSel = "+userSel);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		if(conn!=null)
		{
			try 
			{
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				stmt = conn.createStatement();
				this.setStmt(stmt);
				PreparedStatement prepStmt = null;
				logger.debug("memberID = "+memberID+"getting " +userSel+" DELETE STMT...");
				String query = getDeleteStmt(userSel,memberID);
				if(query!=null)
				{
					prepStmt = conn.prepareStatement(query);
					prepStmt.setString(1, memberID);
					prepStmt.executeUpdate();
					conn.commit();
					logger.debug("memberID = "+memberID+" RESULTS COMMITTED");
					stmt.close();
					resultMap.put("isUpdateSuccess", true);
					returnMap.put("status", "success");
					returnMap.put("errorMsg", "success");
					returnMap.put("resultMap", resultMap);
				}
				else
				{
					resultMap.put("isUpdateSuccess", false);
					returnMap.put("status", "success");
					returnMap.put("errorMsg", "success");
					returnMap.put("resultMap", null);
				}
				return returnMap;
			}
			catch (SQLException e) 
			{
				logger.debug("memberID = "+memberID+" Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
				closeResources();
				return returnMap;
			}
		}
		else
		{
			logger.debug("memberID = "+memberID+" DB CONNECTION IS NULL");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "ERROR IN JDBC CONNECTION");
			closeResources();
			return returnMap;
		}
	}

	public String getDeleteStmt(String subSel,String memberID)
	{
		String query = null;
		if(subSel!=null && !subSel.isEmpty() && memberID!=null && !memberID.isEmpty())
		{
			logger.debug("memberID = "+memberID+" subSel ="+subSel);
			if("martialStatus".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM martialstatuspreferences WHERE MemberID=?";
			}
			else if("lifeStyle".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM lifestylepreferences WHERE MemberID=?";
			}
			else if("profession".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM professionpreferences (professionID) VALUES(?) WHERE MemberID=?";
			}
			else if("qualification".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM qualificationPreferences (qualificationID) VALUES(?) WHERE MemberID=?";
			}
			else if("location".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM currentlocpreferences WHERE MemberID=?";
			}
			else if("religion".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM religionpreferences WHERE MemberID=?";
			}
			else if("motherTongue".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM mothertonguepreferences WHERE MemberID=?";
			}
			else if("caste".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM castepreferences WHERE MemberID=?";
			}
			else if("subcaste".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM subcastepreferences WHERE MemberID=?";
			}
			else if("raasi".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM raasipreferences WHERE MemberID=?";
			}
			else if("star".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM starpreferences WHERE MemberID=?";
			}
			else if("gothram".equalsIgnoreCase(subSel))
			{
				query = "DELETE FROM gothrampreferences WHERE MemberID=?";
			}
			else
				logger.debug("memberID = "+memberID+" NO VALID SUB SELECTION INPUT PASSED");
		}
		else
			logger.debug("memberID = "+memberID+" EMPTY OR NULL DATA PASSED");
		return query;
	}
}
