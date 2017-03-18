package in.indiaBridal.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import in.indiaBridal.UtilityClasses.GenerateFailureJSONResponse;
import in.indiaBridal.UtilityClasses.GenerateMemberID;
import in.indiaBridal.UtilityClasses.ReadJSONObject;
import in.indiaBridal.UtilityClasses.Utilities;
import in.indiaBridal.commands.analyseMatches.PerformMatchAnalysisCommand;
import in.indiaBridal.commands.auth.AuthenticateCommand;
import in.indiaBridal.commands.crtProfile.CreateProfile;
import in.indiaBridal.commands.getProfile.GetAstroDetailsCommand;
import in.indiaBridal.commands.getProfile.GetBasicInfoFormData;
import in.indiaBridal.commands.getProfile.GetEduAndCareerInfoCommand;
import in.indiaBridal.commands.getProfile.GetFamilyInfoCommand;
import in.indiaBridal.commands.getProfile.GetLifeStyleInfoCommand;
import in.indiaBridal.commands.getProfile.GetPreferencesInfoCommand;
import in.indiaBridal.commands.preReg.GetRegistrationFormData;
import in.indiaBridal.commands.searchBasicInfoMatches.FetchAllProfiles;
import in.indiaBridal.commands.searchBasicInfoMatches.FetchBasicInfoForInterestedProfilesCommand;
import in.indiaBridal.commands.searchBasicInfoMatches.SearchFactoryForBasicMatchInfo;
import in.indiaBridal.commands.shortList.SendInterestToAProfileCommand;
import in.indiaBridal.commands.shortList.ShortListAPofileCommand;
import in.indiaBridal.commands.updateProfile.UpdateProfileAndPref;

public class PerformActionCommand
{
	final static Logger logger = Logger.getLogger(PerformActionCommand.class);
	private static String basicInfo = "[{\"memberID\": \"\",\"currPage\": \"register\","
			+ "	\"userData\":{\"createdByID\": {\"isOthers\": \"false\",\"value\": \"CRD10001\"	},"
			+ "\"fullName\":{\"isOthers\": \"false\",\"value\": \"RAJESH K M\"},"
			+ "\"dob\":{\"isOthers\": \"false\",\"value\": \"1991-04-09 00:00:00\"},"
			+ "\"gender\":{	\"isOthers\": \"false\",\"value\": \"MALE\"	},"
			+ "\"motherTongueID\":{\"isOthers\": \"false\",\"value\": \"LAN10013\"},"
			+ "\"casteID\":{\"isOthers\": \"true\",\"value\": \"Naidu\"},"
			+ "\"emailID\":{\"isOthers\": \"false\",\"value\": \"rajeshkm0001@infy.com\"},"
			+ "\"password\":{\"isOthers\": \"false\",\"value\": \"Raje@2012\"},"
			+ "\"phoneNum\":{\"isOthers\": \"false\",\"value\": \"9940559735\"},"
			+ "	}"
			+ "}]";
	/*private static String regForm = "[{\"memberID\": \"\",\"currPage\": \"regForm\","
			+ "	\"userData\":{\"\": {\"isOthers\": \"false\",\"value\": \"\"}}"
						+ "}]";
	 */
	private static String auth = "[{\"memberID\": \"\",\"currPage\": \"authenticate\","
			+ "	\"userData\":{\"userName\": {\"isOthers\": \"false\",\"value\": \"9940559735\"	},"
			+ "\"password\":{\"isOthers\": \"false\",\"value\": \"Raje@2012\"},"
			+ "\"userNameType\":{\"isOthers\": \"false\",\"value\": \"phoneNum\"}"
			+ "	}"
			+ "}]";

	public static void main(String[] args) {
		PerformActionCommand perfObj = new PerformActionCommand();
		String testFileName = "uNameAndPic.txt";
		//String testFileName = "uLifeStyle.txt";
		//String testFileName = "uEduAndCar.txt";
		//String testFileName = "UAstroDetails.txt";
		//String testFileName = "UFamilyInfo.txt";
		//String testFileName = "UBasicInfo.txt";
		String sampleJSON = perfObj.readFile("WebContent\\TestJSON\\"+testFileName);
		logger.debug("sampleJSON = "+sampleJSON);
		logger.debug(perfObj.performAction(sampleJSON));
	}
	public String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public JSONArray performAction(String userRequestJSON)
	{
		logger.debug("NEW REQUEST RECEIVED");
		ReadJSONObject readJSONObj =  new ReadJSONObject();
		logger.debug("userRequestJSON = "+userRequestJSON);
		HashMap<String, String> responseMap = readJSONObj.readGenricJSONArr(userRequestJSON);
		String status = responseMap.get("status");
		if("SUCCESS".equalsIgnoreCase(status))
		{
			String currPage = responseMap.get("currPage");
			if(currPage!=null && !currPage.isEmpty())
			{
				// if any page other than partner preference page perform the below action 
				if("basicInfo".equalsIgnoreCase(currPage) ||   "register".equalsIgnoreCase(currPage) ||
						"familyInfo".equalsIgnoreCase(currPage) || "astroDet".equalsIgnoreCase(currPage) ||
						"eduAndCar".equalsIgnoreCase(currPage)  || "lifeStyle".equalsIgnoreCase(currPage)||
						"regForm".equalsIgnoreCase(currPage)    || "authenticate".equalsIgnoreCase(currPage)||
						"PartnerPref".equalsIgnoreCase(currPage)||
						"uBasicInfo".equalsIgnoreCase(currPage) || "uFamilyInfo".equalsIgnoreCase(currPage) ||
						"uAstroDet".equalsIgnoreCase(currPage) || "uEduAndCar".equalsIgnoreCase(currPage) ||
						"uLifeStyle".equalsIgnoreCase(currPage) || "uPartnerPref".equalsIgnoreCase(currPage) ||
						"uNameAndPic".equalsIgnoreCase(currPage) || "analyseCurrProfile".equalsIgnoreCase(currPage) ||
						"getFeaturedProfiles".equalsIgnoreCase(currPage) || "getBestMatchedProfiles".equalsIgnoreCase(currPage) ||
						"getShortlistedProfiles".equalsIgnoreCase(currPage) || "getProfilesShortlistedYou".equalsIgnoreCase(currPage) ||
						"shorlistAMember".equalsIgnoreCase(currPage) || "getAllProfiles".equalsIgnoreCase(currPage) || 
						"shortlistAProfile".equalsIgnoreCase(currPage) || "intrestedProfiles".equalsIgnoreCase(currPage) 
						|| "sendARequest".equalsIgnoreCase(currPage) || "searchMatchingProfiles".equalsIgnoreCase(currPage))
				{
					HashMap<String, Object> jsonMap = null;
					if("uPartnerPref".equalsIgnoreCase(currPage))
						jsonMap = readJSONObj.readPartnerPrefJSONArr(userRequestJSON);
					else
						jsonMap = readJSONObj.readProfileJSONArr(userRequestJSON);
					if(jsonMap!=null)
					{
						HashMap retMap = null;
						String memberID = (String) jsonMap.get("memberID");
						String userCurrPage = (String) jsonMap.get("currPage");
						HashMap<String, Object> userData = (HashMap <String,Object>) jsonMap.get("userDataMap");
						if("register".equalsIgnoreCase(userCurrPage))
						{
							GenerateMemberID genMemIDObj = new GenerateMemberID();
							memberID = genMemIDObj.getMemberID();
							if(memberID!=null)
							{
								CreateProfile crtProfObj = new CreateProfile();
								retMap = crtProfObj.getResponseHashMap(userCurrPage, memberID, userData);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profile creation SUCCESS - transfer command to FRONT END");
									else
										logger.debug("profile creation FAILED - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedProfileCreation("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedProfileCreation("Error Creating MemberID");
							}
						}
						else if("regForm".equalsIgnoreCase(userCurrPage))
						{
							GetRegistrationFormData getRegFormObj = new GetRegistrationFormData();
							retMap = getRegFormObj.getResponseHashMap();
							if(retMap!=null && !retMap.isEmpty())
							{
								if("success".equalsIgnoreCase((String) retMap.get("status")))
									logger.debug("REG FORM data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
								else
									logger.debug("FAILED TO GET REG FORM data from DB - transfer command to FRONT END");
								return (JSONArray) retMap.get("jsonArr");
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
							}
						}
						else if("authenticate".equalsIgnoreCase(userCurrPage))
						{
							AuthenticateCommand authObj = new AuthenticateCommand();
							retMap = authObj.getResponseHashMap(userCurrPage, userData);
							if(retMap!=null && !retMap.isEmpty())
							{
								if("success".equalsIgnoreCase((String) retMap.get("status")))
									logger.debug("Auth success - transfer command to FRONT END");
								else
									logger.debug("Auth failed - transfer command to FRONT END");
								return (JSONArray) retMap.get("jsonArr");
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
							}

						}
						else if("basicInfo".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetBasicInfoFormData basicInfoObj = new GetBasicInfoFormData();
								retMap = basicInfoObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("basic info form data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO BASIC INFO FORM data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}
						}
						else if("familyInfo".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetFamilyInfoCommand famInfoObj = new GetFamilyInfoCommand();
								retMap = famInfoObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("family info data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO GET FAMILY INFO data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}
						}
						else if("astroDet".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetAstroDetailsCommand astroDetObj = new GetAstroDetailsCommand();
								retMap = astroDetObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("ASTRO DETAILS data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO GET ASTRO DETAILS data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}
						}
						else if("eduAndCar".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetEduAndCareerInfoCommand eduAndCarObj = new GetEduAndCareerInfoCommand();
								retMap = eduAndCarObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("EDU AND CARRER data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO GET EDU AND CARRER data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("lifeStyle".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetLifeStyleInfoCommand lifeStyleObj = new GetLifeStyleInfoCommand();
								retMap = lifeStyleObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("Life style data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO life style data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("PartnerPref".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								GetPreferencesInfoCommand prefInfoObj = new GetPreferencesInfoCommand();
								retMap = prefInfoObj.getResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("Partner Pref data obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO obtain part pref data from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}
						}
						else if("analyseCurrProfile".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								PerformMatchAnalysisCommand searchInfoObj = new PerformMatchAnalysisCommand();
								String partMemID = (String) userData.get("partMemID");
								if(partMemID!=null && !partMemID.isEmpty())
								{
									retMap = searchInfoObj.generateResponseJSON(memberID,partMemID);
									if(retMap!=null && !retMap.isEmpty())
									{
										if("success".equalsIgnoreCase((String) retMap.get("status")))
											logger.debug("profile data for partner search  obtained from DB SUCCESSFULLY - transfer command to FRONT END");
										else
											logger.debug("FAILED TO obtain profile data for partner search from DB - transfer command to FRONT END");
										return (JSONArray) retMap.get("jsonArr");
									}
									else
									{
										GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
										return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
									}
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("INVALID PARTNER MEMEBER ID");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("intrestedProfiles".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								FetchBasicInfoForInterestedProfilesCommand searchInfoObj = new FetchBasicInfoForInterestedProfilesCommand();
								String offSetValAsStr = (String) userData.get("offsetVal");
								int offsetVal = 0;
								if(offSetValAsStr!=null && !offSetValAsStr.isEmpty())
								{
									offsetVal = Integer.parseInt((String) userData.get("offsetVal"));
								}
								retMap = searchInfoObj.generateResponseJSON(memberID,offsetVal);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profile data for intrested Profiles  obtained from DB SUCCESSFULLY - transfer command to FRONT END");
									else
										logger.debug("FAILED TO obtain profile data for intrested Profiles from DB - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("shortlistAProfile".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								ShortListAPofileCommand shortListProfObj = new ShortListAPofileCommand();
								retMap =  shortListProfObj.generateResponseJSON(memberID, userData);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profile shortlisted successfully- transfer command to FRONT END");
									else
										logger.debug("FAILED TO short list a profile - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("sendARequest".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								SendInterestToAProfileCommand sendIntToAProfObj = new SendInterestToAProfileCommand();
								retMap =  sendIntToAProfObj.generateResponseJSON(memberID, userData);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("sent Interest successfully- transfer command to FRONT END");
									else
										logger.debug("FAILED TO send Interest profile - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}

						}
						else if("getAllProfiles".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								String offSetValAsStr = (String) userData.get("offsetVal");
								int offsetVal = 0;
								if(offSetValAsStr!=null && !offSetValAsStr.isEmpty())
								{
									offsetVal = Integer.parseInt((String) userData.get("offsetVal"));
								}
								FetchAllProfiles shortListProfObj = new FetchAllProfiles();
								retMap =  shortListProfObj.generateResponseJSON(memberID, offsetVal);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profile shortlisted successfully- transfer command to FRONT END");
									else
										logger.debug("FAILED TO short list a profile - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}							
						}
						else if("searchMatchingProfiles".equalsIgnoreCase(userCurrPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								SearchFactoryForBasicMatchInfo searchMatchingProfilesObj = new SearchFactoryForBasicMatchInfo();
								retMap =  searchMatchingProfilesObj.generateResponseJSON(memberID);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profiles searched successfully- transfer command to FRONT END");
									else
										logger.debug("FAILED TO searched a profile - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}							
						}
						else if("getFeaturedProfiles".equalsIgnoreCase(currPage) || "getBestMatchedProfiles".equalsIgnoreCase(currPage) ||
								"getShortlistedProfiles".equalsIgnoreCase(currPage) || "getProfilesShortlistedYou".equalsIgnoreCase(currPage))
						{
							if(!Utilities.isNullOrEmpty(memberID))
							{
								int offsetVal = 0;
								String offsetValAsStr = (String) userData.get("offsetVal");
								if(offsetValAsStr!=null)
								{
									offsetVal = Integer.parseInt(offsetValAsStr);
								}
								SearchFactoryForBasicMatchInfo getBasicProInfoObj = new SearchFactoryForBasicMatchInfo();
								retMap =  getBasicProInfoObj.generateResponseJSON(memberID,currPage,offsetVal);
								if(retMap!=null && !retMap.isEmpty())
								{
									if("success".equalsIgnoreCase((String) retMap.get("status")))
										logger.debug("profile shortlisted successfully- transfer command to FRONT END");
									else
										logger.debug("FAILED TO short list a profile - transfer command to FRONT END");
									return (JSONArray) retMap.get("jsonArr");
								}
								else
								{
									GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
									return genFailJSONObj.generateFailedRegForm("NO RESPONSE");
								}
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedRegForm("INVALID MEMEBER ID");
							}							
						}
						else if("uBasicInfo".equalsIgnoreCase(userCurrPage) || "uFamilyInfo".equalsIgnoreCase(userCurrPage) ||
								"uAstroDet".equalsIgnoreCase(userCurrPage) || "uEduAndCar".equalsIgnoreCase(userCurrPage) ||
								"uLifeStyle".equalsIgnoreCase(userCurrPage) || "uPartnerPref".equalsIgnoreCase(userCurrPage) ||
								"uNameAndPic".equalsIgnoreCase(userCurrPage))
						{
							UpdateProfileAndPref updateProfObj = new UpdateProfileAndPref();
							retMap = updateProfObj.getResponseHashMap(userCurrPage, memberID, userData);
							if(retMap!=null && !retMap.isEmpty())
							{
								if("success".equalsIgnoreCase((String) retMap.get("status")))
									logger.debug("profile update SUCCESS - transfer command to FRONT END");
								else
									logger.debug("profile update FAILED - transfer command to FRONT END");
								return (JSONArray) retMap.get("jsonArr");
							}
							else
							{
								GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
								return genFailJSONObj.generateFailedProfileCreation("NO RESPONSE");
							}
						}
						else
						{
							GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
							return genFailJSONObj.generateFailedProfileCreation("Incorrect User Page");
						}
						/*else
						{
							UpdateProfileAndPref updateProfileObj = new UpdateProfileAndPref();
							retMap = updateProfileObj.getResponseHashMap(userCurrPage, memberID, userData);
						}*/
					}
					else
					{
						GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
						return genFailJSONObj.generateFailedProfileCreation("failed parsing user data");
					}
				}
				else
				{
					GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
					return genFailJSONObj.generateFailedProfileCreation("Incorrect User Page");
				}
			}
			else
			{
				GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
				return genFailJSONObj.generateFailedProfileCreation("User Page value is NULL or EMPTY");
			}
		}
		else
		{
			GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
			return genFailJSONObj.generateFailedProfileCreation("parsing generic user request JSON failed");
		}
	}

}
