package in.indiaBridal.commands.crtProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.EnDecryptor;
import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.commands.DBConnectionHandler;
import in.indiaBridal.commands.SendEmailCommand;

public class CreateProfile extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(CreateProfile.class);

	public HashMap<String, Object> getResponseHashMap(String userSel,String memberID,HashMap<String, Object> userData)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		HashMap<String, Object> responseMap = updateUserData(userSel,memberID,userData);

		HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
		if(responseMap!=null && !responseMap.isEmpty()&& memberID!=null && !memberID.isEmpty())
		{
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				responseMap.put("memberID", memberID);
				JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
				JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("jsonArr", retJSONArr);
			}
			else
			{
				responseMap.put("memberID", "");
				JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
				JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", responseMap.get("errorMsg"));
				returnMap.put("jsonArr", retJSONArr);
			}
		}
		else
		{
			HashMap<String, Object> jsonHashMap = new HashMap<String, Object>();
			jsonHashMap.put("status", "failure");
			jsonHashMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			jsonHashMap.put("isEmailExists", "false");
			jsonHashMap.put("isPhNumExists", "false");
			jsonHashMap.put("isProfileCreated", "false");

			JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(jsonHashMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);

			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "NULL OR EMPTY RESPONSE");
			returnMap.put("jsonArr", retJSONArr);
		}
		return returnMap;
	}

	public HashMap<String, Object> updateUserData(String userSel,String memberID,HashMap<String, Object> userData)
	{
		String randomString = RandomStringUtils.randomAlphanumeric(30);
		Connection conn = getConnection();
		this.setConn(conn);
		logger.debug("memberID = "+memberID+" new DB connection established.");

		logger.debug("memberID = "+memberID+" userSel = "+userSel+" userData = "+userData);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		if(conn!=null)
		{
			try 
			{
				logger.debug("memberID = "+memberID+"getting " +userSel+" INSERT STMT...");

				String query = getInsertionStmt("checkEmailID",memberID);
				logger.debug("memberID = "+memberID+" sql stmt = "+query);
				
				PreparedStatement prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, (String) userData.get("emailID"));

				ResultSet rs = prepStmt.executeQuery();

				int emailIDCount = 0;
				while(rs.next())
				{
					emailIDCount = rs.getInt(1);					
				}
				rs.close();
				prepStmt.close();
				if(emailIDCount<1)
				{
					query = getInsertionStmt("checkPhNum",memberID);
					prepStmt = conn.prepareStatement(query);
					prepStmt.setString(1, (String) userData.get("phoneNum"));
					ResultSet phNumRS = prepStmt.executeQuery();

					int phNumCount = 0;
					while(phNumRS.next())
					{
						phNumCount = phNumRS.getInt(1);					
					}
					phNumRS.close();
					prepStmt.close();
					if(phNumCount<1)
					{
						query = getInsertionStmt("authentication",memberID);
						logger.debug("memberID = "+memberID+" sql stmt = "+query);
						
						EnDecryptor encObj = new EnDecryptor();
						String encPwd = encObj.encryption((String) userData.get("password"),true);
						logger.debug("encPwd = "+encPwd);
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("emailID"));
						prepStmt.setString(2, encPwd);
						prepStmt.setString(3, (String) userData.get("phoneNum"));
						prepStmt.setString(4, memberID);
						prepStmt.setString(5, "NO");
						prepStmt.setString(6, randomString);

						prepStmt.executeUpdate();
						prepStmt.close();

						query = getInsertionStmt("crtProfile",memberID);
						logger.debug("memberID = "+memberID+" sql stmt = "+query);
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, memberID);
						prepStmt.setString(2, (String) userData.get("createdByID"));
						prepStmt.setString(3, (String) userData.get("fullName"));
						prepStmt.setString(4, (String) userData.get("dob"));
						prepStmt.setString(5, (String) userData.get("gender"));
						prepStmt.setString(6, (String) userData.get("motherTongueID"));
						prepStmt.setString(7, (String) userData.get("casteID"));
						prepStmt.setString(8, "REL10000");
						prepStmt.setString(9, "CMP10000");
						prepStmt.setString(10, "PHY10000");
						prepStmt.setString(11, "RSI10000");
						prepStmt.setString(12, "STR10000");
						prepStmt.setString(13, "GOT10000");
						prepStmt.setString(14, "RGN10000");
						prepStmt.setString(15, "SCT10000");
						prepStmt.setString(16, "false");
						prepStmt.setString(17, "basicInfo");
						prepStmt.setString(18, "no");
						int rowsAffected1 = prepStmt.executeUpdate();
						prepStmt.close();
						logger.debug("rows affected while creating master Profile = "+rowsAffected1);
						if(rowsAffected1==1)
						{
							query = getInsertionStmt("famDetails",memberID);
							logger.debug("memberID = "+memberID+" sql stmt = "+query);
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, memberID);
							prepStmt.setString(2,"");
							prepStmt.setString(3,"");
							prepStmt.setString(4,"");
							prepStmt.setString(5,"");
							prepStmt.setString(6,"FTY10000");
							prepStmt.setString(7,"");
							prepStmt.setString(8,"");
							prepStmt.setString(9,"CNY10000");
							prepStmt.setString(10,"STY10000");
							prepStmt.setString(11,"CTY10000");
							int rowsAffected2 = prepStmt.executeUpdate();
							logger.debug("rows affected while creating family Profile = "+rowsAffected1);
							prepStmt.close();
							if(rowsAffected2==1)
							{
								query = getInsertionStmt("profDetails",memberID);
								logger.debug("memberID = "+memberID+" sql stmt = "+query);
								prepStmt = conn.prepareStatement(query);
								prepStmt.setString(1, memberID);
								prepStmt.setString(2, "QAL10000");
								prepStmt.setString(3,"PRF10000");
								int rowsAffected3 = prepStmt.executeUpdate();
								prepStmt.close();
								logger.debug("profession profile created successfully. rowsAffected3 = "+rowsAffected3);
								if(rowsAffected3==1)
								{
									query = getInsertionStmt("lifeStyle",memberID);
									logger.debug("memberID = "+memberID+" sql stmt = "+query);
									prepStmt = conn.prepareStatement(query);
									prepStmt.setString(1, memberID);
									prepStmt.setString(2, "FOT10000");
									int rowsAffected4 = prepStmt.executeUpdate();
									prepStmt.close();
									logger.debug("lifeStyle profile created successfully. rowsAffected3 = "+rowsAffected3);
									
									if(rowsAffected4==1)
									{
										query = getInsertionStmt("partnerPref",memberID);
										prepStmt = conn.prepareStatement(query);
										prepStmt.setString(1, "18");
										prepStmt.setString(2, "60");
										prepStmt.setString(3, "150");
										prepStmt.setString(4, "220");
										prepStmt.setString(5, "50");
										prepStmt.setString(6, "150");
										prepStmt.setString(7, "0");
										prepStmt.setString(8, "0");
										prepStmt.setString(9, "No");
										prepStmt.setString(10, "");
										prepStmt.setString(11, memberID);
										int rowsAffected5 = prepStmt.executeUpdate();
										prepStmt.close();
										logger.debug("memberID = "+memberID+" sql stmt = "+query);
										conn.commit();
										logger.debug("complete profile created successfully");
										closeResources();
									}
									else
									{
										returnMap.put("isProfileCreated", "false");
										returnMap.put("isEmailExists", "false");
										returnMap.put("isPhNumExists", "false");
										returnMap.put("status", "failure");
										returnMap.put("errorMsg", "error inserting default preferences");
										returnMap.put("isProfileCreated", "false");
										closeResources();
									}
								}
								else
								{
									returnMap.put("isProfileCreated", "false");
									returnMap.put("isEmailExists", "false");
									returnMap.put("isPhNumExists", "false");
									returnMap.put("status", "failure");
									returnMap.put("errorMsg", "error creating life style profile");
									returnMap.put("isProfileCreated", "false");
									closeResources();
								}
							}
							else
							{
								returnMap.put("isProfileCreated", "false");
								returnMap.put("isEmailExists", "false");
								returnMap.put("isPhNumExists", "false");
								returnMap.put("status", "failure");
								returnMap.put("errorMsg", "error creating family profile");
								returnMap.put("isProfileCreated", "false");
								closeResources();
							}
							
						}
						else
						{
							returnMap.put("isProfileCreated", "false");
							returnMap.put("isEmailExists", "false");
							returnMap.put("isPhNumExists", "false");
							returnMap.put("status", "failure");
							returnMap.put("errorMsg", "error creating master profile");
							returnMap.put("isProfileCreated", "false");
							closeResources();
						}

						SendEmailCommand emailObj = new SendEmailCommand();
						String mailBody = getRegisterBodyContent(randomString);
						String receiverEmailID = (String) userData.get("emailID");
						emailObj.sendEmail(receiverEmailID, "Greetings from IndiazBridal",mailBody);

						logger.debug("email sent successfully");
						 
						returnMap.put("isProfileCreated", "true");
						returnMap.put("isEmailExists", "false");
						returnMap.put("isPhNumExists", "false");
						returnMap.put("status", "success");
						returnMap.put("errorMsg", "success");
						logger.debug("memberID = "+memberID+" RESULTS COMMITTED");
					}
					else
					{
						returnMap.put("isProfileCreated", "false");
						returnMap.put("isEmailExists", "false");
						returnMap.put("isPhNumExists", "true");
						returnMap.put("status", "failure");
						returnMap.put("errorMsg", "email id or ph num exists");
						closeResources();
					}
				}
				else
				{
					returnMap.put("isProfileCreated", "false");
					returnMap.put("isEmailExists", "true");
					returnMap.put("isPhNumExists", "false");
					returnMap.put("status", "failure");
					returnMap.put("errorMsg", "email id or ph num exists");
					returnMap.put("isProfileCreated", "false");
					closeResources();
				}
				return returnMap;
			}
			catch (SQLException e) 
			{
				closeResources();
				logger.debug("memberID = "+memberID+" Error accessing DB or executing SQL Queries. Error Code = "+e.getErrorCode()+"Error Code = "+e.getMessage());
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", "SQL EXCEPTION OCCURRED");
				returnMap.put("isProfileCreated", "false");
				returnMap.put("isEmailExists", "false");
				returnMap.put("isPhNumExists", "false");
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
			returnMap.put("isProfileCreated", "false");
			returnMap.put("isEmailExists", "false");
			returnMap.put("isPhNumExists", "false");
			return returnMap;
		}
	}

	public String getInsertionStmt(String stage,String memberID)
	{
		if(stage!=null && !stage.isEmpty() && memberID!=null && !memberID.isEmpty())
		{
			logger.debug("memberID = "+memberID+" profile creation stage = "+stage);
			if("crtProfile".equalsIgnoreCase(stage))
				return "INSERT INTO usermasterdetails "
				+ "(MemberID, CreatedByID, FullName, DOB, gender, MotherTongueID, CasteID, "
				+ "MartialStatusID, ComplexionID, PhysiqueID, RaasiID, StarID, GothramID, ReligionID, SubCasteID, "
				+ "isPrefUpdated, userCurrPage, isPremiumMember) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			else if("authentication".equalsIgnoreCase(stage))
				return "INSERT INTO authentication "
				+ "(emailID, password, phoneNum, MemberID,userActive,activateId) VALUES (?,?,?,?,?,?)";
			else if("checkEmailID".equalsIgnoreCase(stage))
				return "SELECT COUNT(emailID) FROM authentication WHERE emailID=? ";
			else if("checkPhNum".equalsIgnoreCase(stage))
				return "SELECT COUNT(phoneNum) FROM authentication WHERE phoneNum=? ";
			else if("famDetails".equalsIgnoreCase(stage))
				return "INSERT INTO familydetails (memberID, FatherStatus, MotherStatus, NativePlace, NoOfSiblings, "
						+ "FamilyTypeID, FamilyValue, FamilyStatus, CountryID, StateID, CityID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			else if("profDetails".equalsIgnoreCase(stage))
				return "INSERT INTO professionaldetails (MemberID, QualificationID, ProfessionID) VALUES(?,?,?)";
			else if("lifeStyle".equalsIgnoreCase(stage))
				return "INSERT INTO lifestyle (MemberID, FoodTypeID) VALUES(?,?)";
			else if("partnerPref".equalsIgnoreCase(stage))
				return "INSERT INTO masterpreferences (age_LL, age_UL, height_LL, height_UL, weight_LL, weight_UL, Income_ll, "
						+ "income_ul, Dosham, Expectations, MemberID) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			else
				logger.debug("memberID = "+memberID+" NO VALID SELECTION INPUT PASSED");
		}
		else
			logger.debug("memberID = "+memberID+" EMPTY OR NULL DATA PASSED");
		return null;
	}	
	
	public String getRegisterBodyContent(String randomId)
	{
		String url = getBaseAddress()+"/activateAccount/"+randomId;
		String emailBody = "Hello,<br><br>"
				+ "Welcome to IndiazBridal. <br><br>"
				+ "Thanks for registering with us. Soon you will receive a verification email to verify your email id.<br><br>"
				+"<a href=\""+url+"\">ClickHere</a>"
				+"Please complete the registration process to help us in finding your better half. <br><br>"
				+ " Regards, <br>IndiazBridal Team";

		return emailBody;
	}
}
