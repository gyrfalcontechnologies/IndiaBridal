package in.indiaBridal.commands.updateProfile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import in.indiaBridal.UtilityClasses.HashMapToJSONObjects;
import in.indiaBridal.commands.DBConnectionHandler;

public class UpdateProfileAndPref extends DBConnectionHandler 
{
	final static Logger logger = Logger.getLogger(UpdateProfileAndPref.class);

	public HashMap<String, Object> getResponseHashMap(String userSel,String memberID,HashMap<String, Object> userData)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		logger.debug("memberID = "+memberID+" about to call update user data");
		HashMap<String, Object> responseMap = updateUserData(userSel, memberID, userData);
		if(responseMap!=null && !responseMap.isEmpty())
		{
			HashMapToJSONObjects conToJSONObj = new HashMapToJSONObjects();
			JSONObject retJSONObj = conToJSONObj.hashMapToJSONObj(responseMap);
			JSONArray retJSONArr = conToJSONObj.jsonObjToJSONArr(retJSONObj);
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				returnMap.put("status", "success");
				returnMap.put("errorMsg", "success");
				returnMap.put("jsonArr", retJSONArr);
			}
			else
			{
				returnMap.put("status", "failure");
				returnMap.put("errorMsg", responseMap.get("errorMsg"));
				returnMap.put("jsonArr", retJSONArr);
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

	public HashMap<String, Object> updateUserData(String currSel,String memberID, HashMap<String, Object> userData)
	{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		boolean isUpdateSuccess = true;
		Connection conn = getConnection();
		this.setConn(conn);
		PreparedStatement prepStmt = null;
		String query = null;
		try 
		{
			if(conn!=null)
			{
				if(currSel!=null && !currSel.isEmpty() && userData!=null && !userData.isEmpty() && memberID!=null && !memberID.isEmpty())
				{
					logger.debug("memberID = "+memberID+" current selection = "+currSel+" userData = "+userData);
					if("uBasicInfo".equalsIgnoreCase(currSel))
					{
						query = "UPDATE usermasterdetails SET description=?, MartialStatusID=?, Height=?, PhysiqueID=?, "
								+ " Weight=?, ComplexionID=?, isDisabled=?, ReligionID=?, SubCasteID=?, GothramID=?,  "
								+ "BloodGroup=?, CreatedByID=?, DOB=?, gender=?, MotherTongueID=?, "
								+ "CasteID=? WHERE MemberID=?";
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("description"));
						prepStmt.setString(2, (String) userData.get("maritalStatus"));
						prepStmt.setString(3, (String) userData.get("height"));
						prepStmt.setString(4, (String) userData.get("physique"));
						prepStmt.setString(5, (String) userData.get("weight"));
						prepStmt.setString(6, (String) userData.get("complexion"));
						prepStmt.setString(7, (String) userData.get("isDisabled"));
						prepStmt.setString(8, (String) userData.get("religion"));
						prepStmt.setString(9, (String) userData.get("subcaste"));
						prepStmt.setString(10, (String) userData.get("gothram"));
						prepStmt.setString(11, (String) userData.get("bloodGroup"));
						prepStmt.setString(12, (String) userData.get("createdBy"));
						//prepStmt.setString(13, (String) userData.get("fullName"));
						prepStmt.setString(13, (String) userData.get("dob"));
						prepStmt.setString(14, (String) userData.get("gender"));
						prepStmt.setString(15, (String) userData.get("language"));
						prepStmt.setString(16, (String) userData.get("caste"));
						prepStmt.setString(17, memberID);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uFamilyInfo".equalsIgnoreCase(currSel))
					{
						query = "UPDATE familydetails SET FatherStatus=?, MotherStatus=?, countryID=?, StateID=?, CityID=?,"
								+ "NativePlace=?,  NoOfSiblings=?, FamilyTypeID=?, FamilyValue=?, FamilyStatus=? WHERE memberID=?";
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("fatherStatus"));
						prepStmt.setString(2, (String) userData.get("motherStatus"));
						prepStmt.setString(3, (String) userData.get("country"));
						prepStmt.setString(4, (String) userData.get("state"));
						prepStmt.setString(5, (String) userData.get("city"));
						prepStmt.setString(6, (String) userData.get("nativePlace"));
						prepStmt.setString(7, (String) userData.get("noOfSiblings"));
						prepStmt.setString(8, (String) userData.get("familyTypeID"));
						prepStmt.setString(9, (String) userData.get("familyValue"));
						prepStmt.setString(10, (String) userData.get("familyStatus"));
						prepStmt.setString(11, memberID);
						logger.debug("prep stmt = "+prepStmt);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uAstroDet".equalsIgnoreCase(currSel))
					{
						query = "UPDATE usermasterdetails SET RaasiID=?, StarID=?, Dosham=?, HoroscopeLoc=? "
								+ "WHERE MemberID = ?";

						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("raasi"));
						prepStmt.setString(2, (String) userData.get("star"));
						prepStmt.setString(3, (String) userData.get("dosham"));
						prepStmt.setString(4, (String) userData.get("horoscopeLoc"));
						prepStmt.setString(5, memberID);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uEduAndCar".equalsIgnoreCase(currSel))
					{
						query = "UPDATE professionaldetails SET QualificationID=?,  ProfessionID=?,  AnnualIncome_UL=?,"
								+ " AnnualIncome_LL=? WHERE MemberID = ?";

						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("education"));
						prepStmt.setString(2, (String) userData.get("profession"));
						prepStmt.setString(3, (String) userData.get("annualIncome_UL"));
						prepStmt.setString(4, (String) userData.get("annualIncome_LL"));
						prepStmt.setString(5, memberID);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uLifeStyle".equalsIgnoreCase(currSel))
					{
						query = "UPDATE lifestyle SET Smoker=?, Drinker=?, FoodTypeID=? WHERE MemberID = ?";
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("smoker"));
						prepStmt.setString(2, (String) userData.get("drinker"));
						prepStmt.setString(3, (String) userData.get("foodType"));
						prepStmt.setString(4, memberID);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uNameAndPic".equalsIgnoreCase(currSel))
					{
						query = "UPDATE usermasterdetails SET FullName=?, Pic1=? WHERE MemberID = ?";
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, (String) userData.get("name"));
						prepStmt.setString(2, (String) userData.get("picStream"));
						prepStmt.setString(3, memberID);
						prepStmt.executeUpdate();
						prepStmt.close();
					}
					else if("uPartnerPref".equalsIgnoreCase(currSel))
					{
						logger.debug("memberID = "+memberID+" current selection = "+currSel);
						query = "DELETE mp, lp, pp, qp, clp, rlp, mtp, cp, scp, rp, sp, gp FROM masterpreferences msp "
								+ " LEFT JOIN martialstatuspreferences mp ON msp.MemberID = mp.MemberID"
								+ " LEFT JOIN lifestylepreferences lp ON msp.MemberID =lp.MemberID"
								+ " LEFT JOIN professionpreferences pp ON msp.MemberID = pp.MemberID"
								+ " LEFT JOIN qualificationlpreferences qp ON msp.MemberID = qp.MemberID"
								+ " LEFT JOIN currentlocpreferences clp ON msp.MemberID = clp.MemberID"
								+ " LEFT JOIN religionpreferences rlp ON msp.MemberID = rlp.MemberID"
								+ " LEFT JOIN mothertonguepreferences mtp ON msp.MemberID = mtp.MemberID"
								+ " LEFT JOIN castepreferences cp ON msp.MemberID = cp.MemberID"
								+ " LEFT JOIN subcastepreferences scp ON msp.MemberID = scp.MemberID"
								+ " LEFT JOIN raasipreferences rp ON msp.MemberID = rp.MemberID"
								+ " LEFT JOIN starpreferences sp ON msp.MemberID = sp.MemberID"
								+ " LEFT JOIN gothrampreferences gp ON msp.MemberID = gp.MemberID"
								+ " WHERE mp.MemberID = ?";
						prepStmt = conn.prepareStatement(query);
						prepStmt.setString(1, memberID);
						logger.debug("old preferences to be deleted.. query to be executed = "+prepStmt);						prepStmt.executeUpdate();
						prepStmt.close();
						
						logger.debug("old preferences deleted.");
						query = "UPDATE masterpreferences SET age_LL=?,  age_UL=?,  height_LL=?,  height_UL=?,  weight_LL=?,  weight_UL=?,"
								+ "Income_ll=?,  income_ul=?,  Dosham=?, Expectations=? WHERE MemberID=?";

						prepStmt = conn.prepareStatement(query);
						prepStmt.setInt(1, Integer.parseInt((String) ((ArrayList) userData.get("age_LL")).get(0)));
						prepStmt.setInt(2, Integer.parseInt((String) ((ArrayList) userData.get("age_UL")).get(0)));
						prepStmt.setInt(3, Integer.parseInt((String) ((ArrayList) userData.get("height_LL")).get(0)));
						prepStmt.setInt(4, Integer.parseInt((String) ((ArrayList) userData.get("height_UL")).get(0)));
						prepStmt.setInt(5, Integer.parseInt((String) ((ArrayList) userData.get("weight_LL")).get(0)));
						prepStmt.setInt(6, Integer.parseInt((String) ((ArrayList) userData.get("weight_UL")).get(0)));
						prepStmt.setInt(7, Integer.parseInt((String) ((ArrayList) userData.get("income_LL")).get(0)));
						prepStmt.setInt(8, Integer.parseInt((String) ((ArrayList) userData.get("income_UL")).get(0)));
						prepStmt.setString(9,(String) ((ArrayList) userData.get("dosham")).get(0));
						prepStmt.setString(10, (String) ((ArrayList) userData.get("expectation")).get(0));
						prepStmt.setString(11, memberID);
						logger.debug("master preferences query to be executed = "+prepStmt);
						prepStmt.executeUpdate();
						prepStmt.close();
						logger.debug("master preferences updated");

						ArrayList relShipPrefList = (ArrayList) userData.get("relationShipID");
						for(int count=0;count<relShipPrefList.size();count++)
						{
							query = "INSERT INTO martialstatuspreferences (relationShipID, MemberID) VALUES(?,?)";

							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) relShipPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("martialstatuspreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("martialstatuspreferences updated");
						}

						query = "INSERT INTO lifestylepreferences (smoker, drinker, foodtypeid, MemberID) VALUES (?,?,?,?)";
						prepStmt = conn.prepareStatement(query);

						prepStmt.setString(1, (String) ((ArrayList) userData.get("smoker")).get(0));
						prepStmt.setString(2, (String) ((ArrayList) userData.get("drinker")).get(0));
						prepStmt.setString(3, (String) ((ArrayList) userData.get("foodtypeid")).get(0));
						prepStmt.setString(4, memberID);
						logger.debug("lifestylepreferences to be updated.. query to be executed = "+prepStmt);
						prepStmt.executeUpdate();
						prepStmt.close();
						logger.debug("lifestylepreferences updated. rowsUpdtd = ");

						ArrayList profPrefList = (ArrayList) userData.get("professionID");
						for(int count=0;count<profPrefList.size();count++)
						{
							query = "INSERT INTO professionpreferences (professionID,MemberID) VALUES(?,?)";

							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) profPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("professionpreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("professionpreferences updated.");
						}

						ArrayList qualPrefList = (ArrayList) userData.get("QualificationID");
						query = "INSERT INTO qualificationlpreferences (qualificationID,MemberID) VALUES(?,?)";
						for(int count=0;count<qualPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) qualPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("qualificationPreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("qualificationPreferences updated.");
						}

						int locCount = Integer.parseInt((String) ((ArrayList) userData.get("locCount")).get(0));
						for(int count=1;count<=locCount;count++)
						{
							ArrayList locCnyPrefList = (ArrayList) userData.get("countryID"+count);
							ArrayList locStyPrefList = (ArrayList) userData.get("StateID"+count);
							ArrayList locCtyPrefList = (ArrayList) userData.get("CityID"+count);
							query = "INSERT INTO currentlocpreferences (countryID, StateID, CityID,MemberID) VALUES (?,?,?,?)";

							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) locCnyPrefList.get(0));
							prepStmt.setString(2, (String) locStyPrefList.get(0));
							prepStmt.setString(3, (String) locCtyPrefList.get(0));
							prepStmt.setString(4, memberID);
							logger.debug("currentlocpreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("currentlocpreferences updated.");
						}

						ArrayList relPrefList = (ArrayList) userData.get("ReligionID");
						query = "INSERT INTO religionpreferences (religionid,MemberID) VALUES(?,?)";

						for(int count=0;count<relPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) relPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("religionpreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("religionpreferences updated.");
						}

						ArrayList lanPrefList = (ArrayList) userData.get("motherTongueID");
						query = "INSERT INTO mothertonguepreferences (languageID,MemberID) VALUES(?,?)";
						for(int count=0;count<lanPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) lanPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("mothertonguepreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("mothertonguepreferences updated.");
						}

						ArrayList cstPrefList = (ArrayList) userData.get("casteID");
						query = "INSERT INTO castepreferences (casteID,MemberID) VALUES(?,?)";
						for(int count=0;count<cstPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) cstPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("castepreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("castepreferences updated.");
						}

						ArrayList scstPrefList = (ArrayList) userData.get("SubCasteID");
						query = "INSERT INTO subcastepreferences (SubCasteID,MemberID) VALUES(?,?)";
						for(int count=0;count<scstPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) scstPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("subcastepreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("subcastepreferences updated.");
						}

						ArrayList rsiPrefList = (ArrayList) userData.get("RaasiID");
						query = "INSERT INTO raasipreferences (raasiID,MemberID) VALUES(?,?)";
						for(int count=0;count<rsiPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) rsiPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("raasipreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("raasipreferences updated.");
						}

						ArrayList strPrefList = (ArrayList) userData.get("StarID");
						query = "INSERT INTO starpreferences (starID,MemberID) VALUES(?,?)";
						for(int count=0;count<strPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) strPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("starpreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("starpreferences updated.");
						}

						ArrayList gotPrefList = (ArrayList) userData.get("GothramID");
						query = "INSERT INTO gothrampreferences (gothramID,MemberID) VALUES(?,?)";
						for(int count=0;count<gotPrefList.size();count++)
						{
							prepStmt = conn.prepareStatement(query);
							prepStmt.setString(1, (String) gotPrefList.get(count));
							prepStmt.setString(2, memberID);
							logger.debug("gothrampreferences to be updated.. query to be executed = "+prepStmt);
							prepStmt.executeUpdate();
							prepStmt.close();
							logger.debug("gothrampreferences updated.");
						}
					}
					else
						logger.debug("memberID = "+memberID+" NO VALID SELECTION INPUT PASSED");
					
					if(true)
					{
						if(!"uNameAndPic".equalsIgnoreCase(currSel))
						{
							if("uPartnerPref".equalsIgnoreCase(currSel))
							{
								query = "UPDATE usermasterdetails SET userCurrPage=?, isPrefUpdated=? WHERE MemberID = ?";

								prepStmt = conn.prepareStatement(query);
								prepStmt.setString(1, currSel);
								prepStmt.setString(2, "true");
								prepStmt.setString(3, memberID);
								prepStmt.executeUpdate();
								prepStmt.close();
								conn.commit();
							}
							else
							{
								query = "UPDATE usermasterdetails SET userCurrPage=? WHERE MemberID = ?";

								prepStmt = conn.prepareStatement(query);
								prepStmt.setString(1, currSel);
								prepStmt.setString(2, memberID);
								prepStmt.executeUpdate();
								prepStmt.close();
								conn.commit();
							}
						}
						else
							conn.commit();
						
					}
				}
				else
					logger.debug("memberID = "+memberID+" EMPTY OR NULL DATA PASSED");
			}
			else
				logger.debug("memberID = "+memberID+" DB CONNECTION = NULL");
			closeResources();
			returnMap.put("isUpdateSuccess", isUpdateSuccess?"true":"false");
			returnMap.put("status", "success");
			returnMap.put("errorMsg", "success");
		} 
		catch (SQLException e) 
		{
			closeResources();
			returnMap.put("isUpdateSuccess", "false");
			returnMap.put("status", "failure");
			returnMap.put("errorMsg", "SQL Error Occurred");
			logger.debug("memberID = "+memberID+" SQL EXCEPTION OCCURRED");
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.debug("memberID = "+memberID+" statck trace = "+stack);
		}
		logger.debug("memberID = "+memberID+" isUpdateSuccess = "+isUpdateSuccess);
		return returnMap;
	}
}
