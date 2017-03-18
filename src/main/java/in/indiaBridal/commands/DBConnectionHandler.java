package in.indiaBridal.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import in.indiaBridal.UtilityClasses.Env;
import in.indiaBridal.UtilityClasses.ReadProperties;
import in.indiaBridal.UtilityClasses.Utilities;

public class DBConnectionHandler 
{
	private final static Logger logger = Logger.getLogger(DBConnectionHandler.class);
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	String DB_URL = "jdbc:mysql://localhost/indiazBridal_dev";

	//  Database credentials
	private String DBUsername;
	private String DBpassword;
	private String DBHostName;
	private String DBPort;
	private String DBName;
	private String env;

	public String getBaseAddress() {
		return baseAddress;
	}

	private String baseAddress;
	private Connection conn = null;
	private Statement stmt = null;

	protected DBConnectionHandler()
	{
		env = Env.getEnv();
		if(env==null || env.isEmpty())
			env="DEV";
		ReadProperties obj = new ReadProperties();
		ArrayList keyNames = new ArrayList();
		keyNames.add("DBHostName"+env);
		keyNames.add("DBPort"+env);
		keyNames.add("DBUsername"+env);
		keyNames.add("DBpassword"+env);
		keyNames.add("DBName"+env);
		keyNames.add("baseAddress"+env);
		
		HashMap propVals = obj.readMultiProperties(keyNames);
		if(propVals!=null && !propVals.isEmpty())
		{
			DBUsername = (String) propVals.get("DBUsername"+env);
			DBpassword = (String) propVals.get("DBpassword"+env);
			DBHostName = (String) propVals.get("DBHostName"+env);
			DBPort = (String) propVals.get("DBPort"+env);
			DBName = (String) propVals.get("DBName"+env);
			baseAddress=(String) propVals.get("baseAddress"+env);
			if(Utilities.isNullOrEmpty(DBUsername) || Utilities.isNullOrEmpty(DBpassword) || 
					Utilities.isNullOrEmpty(DBHostName)|| Utilities.isNullOrEmpty(DBPort) || Utilities.isNullOrEmpty(DBName) || Utilities.isNullOrEmpty(baseAddress))
			{
				DBUsername = "root";
				DBpassword = "RAJESH";
				DBHostName = "localhost";
				DBPort = "";
				DBName = "indiazBridal_dev";
				baseAddress="http://localhost:8080";
			}
		}
		else
		{
			DBUsername = "root";
			DBpassword = "RAJESH";
			DBHostName = "localhost";
			DBPort = "";
			DBName = "indiazBridal_dev";
			baseAddress="http://localhost:8080";
		}
		DB_URL = "jdbc:mysql://"+DBHostName+"/"+DBName;
	}
	
	public Statement getStmt() 
	{
		return stmt;
	}

	public void setStmt(Statement stmt) 
	{
		this.stmt = stmt;
		logger.debug("Statment object set");
	}

	public Connection getConn() 
	{
		logger.debug("DB connection object returned");
		return conn;
	}

	public void setConn(Connection conn) 
	{
		logger.debug("DB connection object set");
		this.conn = conn;
	}

	public Connection getConnection()
	{
		logger.debug("CREATE NEW DB connection object");
		//STEP 2: Register JDBC driver
		try 
		{
			Class.forName(JDBC_DRIVER);
			//STEP 3: Open a connection

			logger.debug("getting connection to database...");
			 conn = DriverManager.getConnection(DB_URL,DBUsername,DBpassword); 
						conn.setAutoCommit(false);
			return conn;
		} 
		catch (ClassNotFoundException e) 
		{
			conn = null;
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("ClassNotFoundException OCCURRED statck trace = "+stack);
		}
		catch(SQLException e)
		{
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("SQLException OCCURRED statck trace = "+stack);
			logger.debug("SQL EXCEPTION OCCURRED WHILE EXECUTING STATEMENT !!");
		}
		catch(Exception e)
		{
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("UNKNOWN Exception OCCURRED statck trace = "+stack);
			logger.debug("UNHANDLED EXCEPTION OCCURRED WHILE EXECUTING STATEMENT !!");
			//Handle errors for Class.forName
		}
		closeResources();
		return null;
	}

	public void closeResources()
	{
		logger.debug("CLOSE RESOURCES CALLED");
		Statement currStmt = this.getStmt();
		if(currStmt!=null)
		{
			try 
			{
				currStmt.close();
				logger.debug("JDBC statement closed successfully!!");
			} 
			catch (SQLException e) 
			{
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug("error closing JDBC statement... "+stack);
			}
		}

		Connection currConn = this.getConn();
		if(currConn!=null)
		{
			try
			{
				currConn.close();
				logger.debug("JDBC connection closed successfully!!");
			}
			catch(SQLException e)
			{
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug("error closing JDBC Connection... "+stack);
			}
		}
	}

}
