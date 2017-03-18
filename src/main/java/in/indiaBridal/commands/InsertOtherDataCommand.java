package in.indiaBridal.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class InsertOtherDataCommand extends DBConnectionHandler 
{
	private final static Logger logger = Logger.getLogger(InsertOtherDataCommand.class);
	HashMap<String, String> tableMap = new HashMap<String, String>();
	
	public HashMap<String, Object> getResponseHashMap(String userCurrPage,String memberID, String fieldName, String data)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String tableNameKey =  fieldName+"TableName";
		logger.debug("memberID = "+memberID+" userCurrPage = "+userCurrPage+" fieldName = "+fieldName+" data = "+data);
		logger.debug("memberID = "+memberID+" tableNameKey = "+tableNameKey);
		HashMap<String, String> tableNameMap = getTableNameMap();
		String tableName = tableNameMap.get(tableNameKey);
		logger.debug("memberID = "+memberID+" tableName = "+tableName);
		if(tableName!=null && !tableName.isEmpty() && fieldName!=null && !fieldName.isEmpty())
		{
			logger.debug("memberID = "+memberID+" get sqlStmt for tableName = "+tableName+" fieldName = "+fieldName);
			if(fieldName.equalsIgnoreCase("motherTongueID"))
				fieldName = "LanguageID";
			String sqlStmt = getSelectQuery(tableName,fieldName);
			logger.debug("sqlStmt = "+sqlStmt);
			Connection conn = getConnection();
			logger.debug("memberID = "+memberID+" DB connection established. connection object = "+conn);
			this.setConn(conn);
			try
			{
				PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
				logger.debug("getting MAX ID for " +tableName+" details...");
				ResultSet rs = prepStmt.executeQuery(sqlStmt);
				if(rs.next())
				{
					String maxID = rs.getString(1);
					prepStmt.close();
					if(maxID!=null && !maxID.isEmpty())
					{
						int id = Integer.parseInt(maxID.substring(3, maxID.length()));
						id++;
						String currMaxid = maxID.substring(0,3)+id;
						sqlStmt = getInsertQuery(tableName);
						prepStmt = conn.prepareStatement(sqlStmt);
						prepStmt.setString(1, currMaxid);
						prepStmt.setString(2, data);
						logger.debug("current preared stmt = "+prepStmt);
						prepStmt.executeUpdate();
						prepStmt.close();
						conn.commit();
						closeResources();
						logger.debug("insert other data success - statement committed  MAXID = "+currMaxid);
						returnMap.put("id", currMaxid);
						returnMap.put("status", "success");
						returnMap.put("errorMsg", "success");
						returnMap.put("isUpdateSuccess", "true");
					}
					else
					{
						closeResources();
						logger.debug("statement executed but MAXID is empty or null");
						returnMap.put("isUpdateSuccess", "false");
						returnMap.put("status", "success");
						returnMap.put("errorMsg", "success");
					}
				}
				else
				{
					rs.close();
					closeResources();
					returnMap.put("isUpdateSuccess", "false");
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "Error getting max ID");
					logger.debug("memberID = "+memberID+" Error getting max ID");
				}
			}
			catch (SQLException e)
			{
				closeResources();
				returnMap.put("isUpdateSuccess", "false");
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
				logger.debug("memberID = "+memberID+" SQL EXCEPTION OCCURRED WHILE INSERTING OTHER DATA INTO DB");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug("memberID = "+memberID+" statck trace = "+stack);
			}
		}
		else
		{
			returnMap.put("isUpdateSuccess", "false");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY INPUTS");
			logger.debug("memberID = "+memberID+" NOT A VALID TABLE NAME OR FIELD NAME");
		}
		return returnMap;
	}

	public String getSelectQuery(String tableName, String colName)
	{
		String finalQuery = null;
		if(tableName!=null && !tableName.isEmpty() && colName!=null && !colName.isEmpty())
			finalQuery = "SELECT max("+colName+") FROM "+tableName;
		logger.debug("select query details = "+ finalQuery);
		return finalQuery;
	}

	public String getInsertQuery(String tableName)
	{
		String finalQuery = null;
		if(tableName!=null && !tableName.isEmpty())
		{
			if("PROFESSIONS".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO PROFESSIONS (ProfessionID,ProfessionName) VALUES(?,?)";
			else if("QUALIFICATIONS".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO QUALIFICATIONS (QualificationID,QualificationName) VALUES(?,?)";
			else if("RELIGIONDETAILS".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO RELIGIONDETAILS (ReligionID,ReligionName) VALUES(?,?)";
			else if("LANGUAGE".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO LANGUAGE (LanguageID,LanguageName) VALUES(?,?)";
			else if("CASTE".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO CASTE (CasteID,casteName) VALUES(?,?)";
			else if("SUBCASTE".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO SUBCASTE (SubCasteID, SubCasteName) VALUES(?,?)";
			else if("RAASI".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO RAASI (RaasiID,RaasiName) VALUES(?,?)";
			else if("STARDETAILS".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO STARDETAILS (StarID,StarName) VALUES(?,?)";
			else if("GOTHRAM".equalsIgnoreCase(tableName))
				finalQuery = "INSERT INTO GOTHRAM (GothramID,GothramName) VALUES(?,?)";
		}
		logger.debug("insert query details = "+ finalQuery);
		return finalQuery;
	}
	
	public HashMap<String, String> getTableNameMap()
	{
		tableMap.put("professionIDTableName","PROFESSIONS");
		tableMap.put("ProfessionIDTableName","PROFESSIONS");
		tableMap.put("QualificationIDTableName","QUALIFICATIONS");
		tableMap.put("ReligionIDTableName","RELIGIONDETAILS");
		tableMap.put("motherTongueIDTableName","LANGUAGE");
		tableMap.put("CastIDTableName","CASTE");
		tableMap.put("casteIDTableName","CASTE");
		tableMap.put("SubCasteIDTableName","SUBCASTE");
		tableMap.put("RaasiIDTableName","RAASI");
		tableMap.put("StarIDTableName","STARDETAILS");
		tableMap.put("GothramIDTableName","GOTHRAM");
		return tableMap;
	}
}
