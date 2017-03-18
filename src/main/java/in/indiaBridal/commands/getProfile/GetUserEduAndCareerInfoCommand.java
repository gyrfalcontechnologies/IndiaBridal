package in.indiaBridal.commands.getProfile;

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

public class GetUserEduAndCareerInfoCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserEduAndCareerInfoCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received : response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("responseMap for user prof det = "+responseMap);
			returnMap.put("status", "success");
			returnMap.put("errorMsg", "success");
			returnMap.put("userData", responseMap);
		}
		else
		{
			logger.debug("response map is null or empty.");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
		}
		logger.debug("returnMap = "+returnMap);
		return returnMap;
	}

	public String getUsrProfessionInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery =  "Select QualificationID, ProfessionID, AnnualIncome_UL, AnnualIncome_LL "
								+ "FROM professionaldetails WHERE MemberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, String> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrProfessionInfoStmt();
		if(sqlStmt!=null && !sqlStmt.isEmpty())
		{
			Connection conn = getConnection();
			this.setConn(conn);
			logger.debug("DB connection established ");
			try
			{
				PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
				prepStmt.setString(1, memberID);
				logger.debug("prepStmt to be executed = "+prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				logger.debug("parsing result set data");
				HashMap<String, String> resultMap = new HashMap<String, String>();
				if(rs.next())
				{
					String educationID = rs.getString("QualificationID");
					String professionID = rs.getString("ProfessionID");
					String annualIncome_UL = rs.getString("AnnualIncome_UL");
					String annualIncome_LL = rs.getString("AnnualIncome_LL");

					resultMap.put("educationID",Utilities.isNullOrEmpty(educationID)?"":educationID);
					resultMap.put("professionID",Utilities.isNullOrEmpty(professionID)?"":professionID);
					resultMap.put("annualIncome_UL",Utilities.isNullOrEmpty(annualIncome_UL)?"":annualIncome_UL);
					resultMap.put("annualIncome_LL",Utilities.isNullOrEmpty(annualIncome_LL)?"":annualIncome_LL);
				}
				else
				{
					resultMap.put("educationID","");
					resultMap.put("professionID","");
					resultMap.put("annualIncome_UL","");
					resultMap.put("annualIncome_LL","");
				}
				rs.close();
				closeResources();
				
				logger.debug("resultMap = "+resultMap);
				return resultMap;
			}
			catch (SQLException e)
			{
				closeResources();
				logger.debug(" SQL EXCEPTION OCCURRED while parsing result set");
				StringWriter stack = new StringWriter();
				e.printStackTrace(new PrintWriter(stack));
				logger.debug(" statck trace = "+stack);
				return null;
			}
		}
		else
		{
			logger.debug(" SQL stmt is null or empty");
			return null;
		}
	}
}
