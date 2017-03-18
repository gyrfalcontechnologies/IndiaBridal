package in.indiaBridal.commands.preReg;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.commands.DBConnectionHandler;

public class GetRegistrationFormData extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetRegistrationFormData.class);

	public HashMap<String, Object> getResponseHashMap()
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		HashMap<String, Object> responseMap = getDataFromDB();
		
		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		if(responseMap!=null && !responseMap.isEmpty())
		{
			JSONObject retJSONObj = conToJSONObj.nestedHMapToJSONObj(responseMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("jsonArr", retJSONArr);
				logger.debug("retJSONArr = "+retJSONArr);
			}
			else
			{
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", responseMap.get("errorMsg"));
				returnMap.put("jsonArr", null);
			}
			logger.debug("responseMap = "+responseMap);
		}
		else
		{
			HashMap jsonHashMap = new HashMap();
			jsonHashMap.put("status", "failure");
			jsonHashMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(jsonHashMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
			
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			returnMap.put("jsonArr", retJSONArr);
		}

		return returnMap;
	}

	public HashMap<String, Object> getDataFromDB()
	{
		Connection conn = getConnection();
		setConn(conn);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		if(conn!=null)
		{
			try 
			{
				HashMap<String, String> casteMap = new HashMap<String, String>();
				HashMap<String, String> createdByMap = new HashMap<String, String>();
				HashMap<String, String> langMap = new HashMap<String, String>();

				String sqlStmt = getSelectStmt("caste");
				if(sqlStmt!=null)
				{
					PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
					ResultSet casteRS = prepStmt.executeQuery(sqlStmt);
					if(casteRS.next())
					{
						casteMap = getDataResultSet(casteRS);
						casteRS.close();
						prepStmt.close();
						if(casteMap!=null)
						{
							sqlStmt = getSelectStmt("createdBy");
							if(sqlStmt!=null && !sqlStmt.isEmpty())
							{
								prepStmt = conn.prepareStatement(sqlStmt);
								ResultSet crtdIdRs = prepStmt.executeQuery(sqlStmt);
								if(crtdIdRs.next())
								{
									createdByMap = getDataResultSet(crtdIdRs);
									crtdIdRs.close();
									prepStmt.close();
									if(createdByMap!=null)
									{
										sqlStmt = getSelectStmt("language");
										logger.debug("sqlstmt for language = "+sqlStmt);
										if(sqlStmt!=null && !sqlStmt.isEmpty())
										{
											prepStmt = conn.prepareStatement(sqlStmt);
											logger.debug("prepStmt for language = "+prepStmt);
											ResultSet langRs = prepStmt.executeQuery(sqlStmt);
											if(langRs.next())
											{
												langMap = getDataResultSet(langRs);
												langRs.close();
												prepStmt.close();
												closeResources();
												if(langMap!=null)
												{
													returnMap.put("caste", casteMap);
													returnMap.put("createdBy", createdByMap);
													returnMap.put("motherTongue", langMap);
													returnMap.put("status", "success");
													returnMap.put("errorMsg", "success");
													logger.debug("return map = "+returnMap);
													return returnMap;
												}
												else
												{
													returnMap.put("caste", null);
													returnMap.put("createdBy", null);
													returnMap.put("motherTongue", null);
													returnMap.put("status", "failure");
													returnMap.put("errorMsg", "error getting languages!!");
													return returnMap;
												}
											}
											else
											{
												closeResources();
												returnMap.put("caste", null);
												returnMap.put("createdBy", null);
												returnMap.put("motherTongue", null);
												returnMap.put("status", "failure");
												returnMap.put("errorMsg", "NO languages found!!");
												return returnMap;
											}
										}
										else
										{
											closeResources();
											returnMap.put("caste", null);
											returnMap.put("createdBy", null);
											returnMap.put("motherTongue", null);
											returnMap.put("status", "failure");
											returnMap.put("errorMsg", "failed to get SQL stmt for languages!!");
											return returnMap;
										}
									}
									else
									{
										closeResources();
										returnMap.put("caste", null);
										returnMap.put("createdBy", null);
										returnMap.put("motherTongue", null);
										returnMap.put("status", "failure");
										returnMap.put("errorMsg", "error getting created BY values!!");
										return returnMap;
									}
								}
								else
								{
									closeResources();
									returnMap.put("caste", null);
									returnMap.put("createdBy", null);
									returnMap.put("motherTongue", null);
									returnMap.put("status", "failure");
									returnMap.put("errorMsg", "NO Created By values found!!");
									return returnMap;
								}
							}
							else
							{
								closeResources();
								returnMap.put("caste", null);
								returnMap.put("createdBy", null);
								returnMap.put("motherTongue", null);
								returnMap.put("status", "failure");
								returnMap.put("errorMsg", "failed to get SQL stmt for created by id!!");
								return returnMap;
							}
						}
						else
						{
							closeResources();
							returnMap.put("caste", null);
							returnMap.put("createdBy", null);
							returnMap.put("motherTongue", null);
							returnMap.put("status", "failure");
							returnMap.put("errorMsg", "error getting CASTE values!!");
							return returnMap;
						}
					}
					else
					{
						closeResources();
						returnMap.put("caste", null);
						returnMap.put("createdBy", null);
						returnMap.put("motherTongue", null);
						returnMap.put("status", "failure");
						returnMap.put("errorMsg", "NO CASTE values found!!");
						return returnMap;
					}
				}
				else
				{
					returnMap.put("caste", null);
					returnMap.put("createdBy", null);
					returnMap.put("motherTongue", null);
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "failed to get SQL stmt for caste!!");
					closeResources();
					return returnMap;
				}
			}
			catch (SQLException e) 
			{
				logger.debug("Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
				logger.debug(" SQL EXCEPTION OCCURRED while getting registration form data from DB");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug(" stack trace = "+stack);
				closeResources();
				return returnMap;
			}
		}
		else
		{
			logger.debug("Error getting connection");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "ERROR GETTING JDBC CONNECTION");
			closeResources();
			return returnMap;
		}
	}

	public String getSelectStmt(String currSel)
	{
		String finalQuery = null;
		if(currSel!=null && !currSel.isEmpty())
		{
			if("caste".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CasteID, casteName FROM caste";
			}
			else if("createdBy".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT CreatedByID, CreatedBy FROM createdby";
			}
			else if("language".equalsIgnoreCase(currSel))
			{
				finalQuery = "SELECT LanguageID, LanguageName FROM language";
			}
		}
		return finalQuery;
	}

	public HashMap<String, String> getDataResultSet(ResultSet rs)
	{
		logger.debug("parsing result set data");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		try
		{
			while(rs.next())
			{
				String val1 = rs.getString(1);
				String val2 = rs.getString(2);
				if(val1!=null && !val1.isEmpty() && val2!=null && !val2.isEmpty())
					resultMap.put(val1, val2);
				logger.debug(("val1 = "+val1+" val2 = "+val2));
			}
			logger.debug("resultMap = "+resultMap);
			return resultMap;
		}
		catch (SQLException e)
		{
			logger.debug(" SQL EXCEPTION OCCURRED while parsing result set");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug(" statck trace = "+stack);
		}
		return null;
	}
}
