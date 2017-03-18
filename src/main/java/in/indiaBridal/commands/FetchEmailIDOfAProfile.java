package in.indiaBridal.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class FetchEmailIDOfAProfile extends DBConnectionHandler
{
	final static Logger logger = Logger.getLogger(FetchEmailIDOfAProfile.class);
	public String getEmailID(String memberID)
	{
		Connection conn = getConnection();
		this.setConn(conn);
		logger.debug("memberID = "+memberID+" new DB connection established.");
		if(conn!=null)
		{
			try 
			{
				String emailID = null;
				logger.debug("memberID = "+memberID+"getting SQL STMT...");
				String query = getSQLStmt();
				logger.debug("memberID = "+memberID+" sql stmt = "+query);
				PreparedStatement prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, memberID);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data");
				while(rs.next())
				{
					emailID = rs.getString("emailID");
				}
				rs.close();
				prepStmt.close();
				closeResources();
				return emailID;
			}
			catch (SQLException e) 
			{
				closeResources();
				logger.debug("memberID = "+memberID+" Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
				logger.debug("memberID = "+memberID+" SQL EXCEPTION OCCURRED");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug("memberID = "+memberID+" statck trace = "+stack);
				return null;
			}
		}
		else
		{
			logger.debug("memberID = "+memberID+" DB CONNECTION IS NULL");
			return null;
		}
	}
	
	public String getSQLStmt()
	{
		String query = "SELECT emailID FROM authentication WHERE MemberID = ?";
		return query;
	}
}
