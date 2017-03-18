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

public class GetUserBasicInfoDetailsCommand extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(GetUserBasicInfoDetailsCommand.class);

	public HashMap<String, Object> getResponseHashMap(String memberID)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> responseMap = getDataFromDB(memberID);
		logger.debug("data received for all tables: response map = "+responseMap);
		if(!Utilities.isNullOrEmpty(responseMap))
		{
			logger.debug("response size and key size are equal");
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

	public String getUsrBasicInfoStmt()
	{
		logger.debug("get SELECT stmt to be executed.");
		String finalQuery =  "Select CreatedByID, gender, FullName, DOB, description, MartialStatusID, "
				+ "Height, PhysiqueID, Weight, ComplexionID, isDisabled, ReligionID, "
				+ "MotherTongueID, CasteID, SubCasteID, GothramID, BloodGroup FROM usermasterdetails WHERE MemberID = ?";
		logger.debug("select query to be executed = "+finalQuery);
		return finalQuery;
	}

	public HashMap<String, String> getDataFromDB(String memberID)
	{
		String sqlStmt = getUsrBasicInfoStmt();
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
				while(rs.next())
				{
					String createdByID = rs.getString("CreatedByID");
					String gender = rs.getString("gender");
					String fullName = rs.getString("FullName");
					String dob = rs.getDate("DOB").toString();
					String description = rs.getString("description");
					String martialStatusID = rs.getString("MartialStatusID");
					String height = rs.getString("Height");
					String physiqueID = rs.getString("PhysiqueID");
					String weight = rs.getString("Weight");
					String complexionID = rs.getString("ComplexionID");
					String isDisabled = rs.getString("isDisabled");
					String religionID = rs.getString("ReligionID");
					String motherTongueID = rs.getString("MotherTongueID");
					String casteID = rs.getString("CasteID");
					String subCasteID = rs.getString("SubCasteID");
					String gothramID = rs.getString("GothramID");
					String bloodGroup = rs.getString("BloodGroup");

					resultMap.put("createdByID",Utilities.isNullOrEmpty(createdByID)?"":createdByID);
					resultMap.put("gender",Utilities.isNullOrEmpty(gender)?"":gender);
					resultMap.put("fullName",Utilities.isNullOrEmpty(fullName)?"":fullName);
					resultMap.put("dob",Utilities.isNullOrEmpty(dob)?"":dob);
					resultMap.put("description",Utilities.isNullOrEmpty(description)?"":description);
					resultMap.put("martialStatusID",Utilities.isNullOrEmpty(martialStatusID)?"":martialStatusID);
					resultMap.put("height",Utilities.isNullOrEmpty(height)?"":height);
					resultMap.put("physiqueID",Utilities.isNullOrEmpty(physiqueID)?"":physiqueID);
					resultMap.put("weight",Utilities.isNullOrEmpty(weight)?"":weight);
					resultMap.put("complexionID",Utilities.isNullOrEmpty(complexionID)?"":complexionID);
					resultMap.put("isDisabled",Utilities.isNullOrEmpty(isDisabled)?"":isDisabled);
					resultMap.put("religionID",Utilities.isNullOrEmpty(religionID)?"":religionID);
					resultMap.put("languageID",Utilities.isNullOrEmpty(motherTongueID)?"":motherTongueID);
					resultMap.put("casteID",Utilities.isNullOrEmpty(casteID)?"":casteID);
					resultMap.put("subCasteID",Utilities.isNullOrEmpty(subCasteID)?"":subCasteID);
					resultMap.put("gothramID",Utilities.isNullOrEmpty(gothramID)?"":gothramID);
					resultMap.put("bloodGroup",Utilities.isNullOrEmpty(bloodGroup)?"":bloodGroup);
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
