package in.indiaBridal.UtilityClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import in.indiaBridal.commands.DBConnectionHandler;

public class GenerateMemberID extends DBConnectionHandler 
{
	private final static Logger logger = Logger.getLogger(GenerateMemberID.class);
	private Statement stmt = null;
	private Connection conn = null;
	HashMap<String, String> tableMap = new HashMap<String, String>();

	public GenerateMemberID()
	{
		super();
	}
	
	public String getMemberID()
	{
		String sqlStmt = getSelectQuery("authentication","MemberID");
		logger.debug("sqlStmt = "+sqlStmt);
		conn = getConnection();
		logger.debug(" DB connection established. connection object = "+conn);
		this.setConn(conn);
		try
		{
			stmt = conn.createStatement();
			this.setStmt(stmt);
			PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
			logger.debug("getting MAX ID for authentication details...");
			ResultSet rs = prepStmt.executeQuery(sqlStmt);
			while(rs.next())
			{
				String maxID = rs.getString(1);
				conn.commit();
				stmt.close();
				if(maxID!=null && !maxID.isEmpty())
				{
					int id = Integer.parseInt(maxID.substring(3, maxID.length()));
					id++;
					String currMaxid = maxID.substring(0,3)+id;
					return currMaxid;
				}
				else
				{
					return "IBM10000001";
				}
			}
		}
		catch (SQLException e)
		{
			logger.debug(" Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
			closeResources();
			return null;
		}
		return null;
	}

	public String getSelectQuery(String tableName, String colName)
	{
		String finalQuery = null;
		if(tableName!=null && !tableName.isEmpty() && colName!=null && !colName.isEmpty())
			finalQuery = "SELECT max("+colName+") FROM "+tableName;
		logger.debug("select query details = "+ finalQuery);
		return finalQuery;
	}
}
