package in.indiaBridal.commands;

import in.indiaBridal.UtilityClasses.Env;
import in.indiaBridal.UtilityClasses.ReadProperties;
import in.indiaBridal.UtilityClasses.Utilities;
import org.apache.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthick_S19 on 10/15/2016.
 */
public class DBUtility {
    private final static Logger logger = Logger.getLogger(DBConnectionHandler.class);
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost/indiazBridal_dev";

    //  Database credentials
    private static String DBUsername;
    private static String DBpassword;
    private static String DBHostName;
    private static String DBPort;
    private static String DBName;
    private static String env;
    private static Connection conn = null;
    private static Statement stmt = null;

    static
    {

        if(env==null || env.isEmpty())
            env="DEV";
        ReadProperties obj = new ReadProperties();
        ArrayList keyNames = new ArrayList();
        keyNames.add("DBHostName"+env);
        keyNames.add("DBPort"+env);
        keyNames.add("DBUsername"+env);
        keyNames.add("DBpassword"+env);
        keyNames.add("DBName"+env);

        HashMap propVals = obj.readMultiProperties(keyNames);
        if(propVals!=null && !propVals.isEmpty())
        {
            DBUsername = (String) propVals.get("DBUsername"+env);
            DBpassword = (String) propVals.get("DBpassword"+env);
            DBHostName = (String) propVals.get("DBHostName"+env);
            DBPort = (String) propVals.get("DBPort"+env);
            DBName = (String) propVals.get("DBName"+env);
            if(Utilities.isNullOrEmpty(DBUsername) || Utilities.isNullOrEmpty(DBpassword) ||
                    Utilities.isNullOrEmpty(DBHostName)|| Utilities.isNullOrEmpty(DBPort) || Utilities.isNullOrEmpty(DBName))
            {
                DBUsername = "root";
                DBpassword = "root";
                DBHostName = "localhost";
                DBPort = "";
                DBName = "indiazBridal_dev";
            }
        }
        else
        {
            DBUsername = "root";
            DBpassword = "root";
            DBHostName = "localhost";
            DBPort = "";
            DBName = "indiazBridal_dev";
        }
        DB_URL = "jdbc:mysql://"+DBHostName+"/"+DBName;
    }

    public static Statement getStmt()
    {
        return stmt;
    }

    public void setStmt(Statement stmt)
    {
        this.stmt = stmt;
        logger.debug("Statment object set");
    }

    public static Connection getConn()
    {
        logger.debug("DB connection object returned");
        if (conn ==null){
            conn = getConnection();
        }
        return conn;
    }

    public void setConn(Connection conn)
    {
        logger.debug("DB connection object set");
        this.conn = conn;
    }

    private static Connection getConnection()
    {
        logger.debug("CREATE NEW DB connection object");
        //STEP 2: Register JDBC driver
        try
        {
            Class.forName(JDBC_DRIVER);
            //STEP 3: Open a connection

            logger.debug("getting connection to database...");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/indiazBridal_dev",DBUsername,DBpassword);
            conn.setAutoCommit(true);
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

    public static void closeResources()
    {
        logger.debug("CLOSE RESOURCES CALLED");
        Statement currStmt = getStmt();
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

        Connection currConn = getConn();
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
