package in.indiaBridal.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceDataList
{
	private final String age_LL = "age_LL";
	private final String age_UL = "age_UL";
	private final String height_LL = "height_LL";
	private final String height_UL = "height_UL";
	private final String weight_LL = "weight_LL";
	private final String weight_UL = "weight_UL";
	private final String income_LL = "income_LL";
	private final String income_UL = "income_UL";
	private final String expectation = "expectation";
	private final String dosham = "dosham";
	private final String relationShipID = "relationShipID";
	private final String drinker = "drinker";
	private final String smoker = "smoker";
	private final String foodtypeid = "foodtypeid";
	private final String professionID = "professionID";
	private final String qualificationID = "qualificationID";
	private final String locationCoStCt = "locationCoStCt";
	private final String religionID = "religionID";
	private final String languageID = "languageID";
	private final String casteID = "casteID";
	private final String subCasteID = "subCasteID";
	private final String raasiID = "raasiID";
	private final String starID = "starID";
	private final String gothramID = "gothramID";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getValue(HashMap userData, String key)
	{
		ArrayList dataList = (ArrayList) userData.get(age_LL);
		return (String) dataList.get(0);
	}
	public boolean updateBasicInfo(String memberID, String currPage, HashMap userData)
	{
		HashMap dataToBeUpdated = new HashMap();
		dataToBeUpdated.put(age_LL, getValue(userData,age_LL));
		dataToBeUpdated.put(age_UL, getValue(userData,age_UL));
		dataToBeUpdated.put(height_LL, getValue(userData,height_LL));
		dataToBeUpdated.put(height_UL, getValue(userData,height_UL));
		dataToBeUpdated.put(weight_LL, getValue(userData,weight_LL));
		dataToBeUpdated.put(weight_UL, getValue(userData,weight_UL));
		dataToBeUpdated.put(income_LL, getValue(userData,income_LL));
		dataToBeUpdated.put(income_UL, getValue(userData,income_UL));
		dataToBeUpdated.put(expectation, getValue(userData,expectation));
		dataToBeUpdated.put(dosham, getValue(userData,dosham));
		dataToBeUpdated.put("subSel", "basicInfo");
		/*UpdateProfileAndPref updtPrefObj = new UpdateProfileAndPref();
		HashMap responseMap = updtPrefObj.getResponseHashMap(currPage, memberID, dataToBeUpdated);
		if(responseMap!=null && !responseMap.isEmpty())
		{
			if("success".equalsIgnoreCase((String) responseMap.get("status")))
			{
				return true;
			}
		}*/
		return false;
	}
	
	
	
}
