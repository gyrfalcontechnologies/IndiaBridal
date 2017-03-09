app.controller('profileEditController', ['$scope', '$rootScope', '$timeout', '$location', 'loggedUserService', '$cookieStore', ProfileEditController]);

function ProfileEditController($scope, $rootScope, $timeout, $location, loggedUserService, $cookieStore){
    
    var self = this;
    this.loggedUserService = loggedUserService;
    this.$scope = $scope;
    this.$rootScope = $rootScope;
    this.$timeout = $timeout; 
    this.$location = $location;
    var uname = $cookieStore.get('IB-uName');
    var memberID = $cookieStore.get('IB-ID');  
    var ppp = $cookieStore.get("IB-uPWD"); 
    if(memberID === "" && uname==="") {
        $location.path("/profile");
    }
    else{
        this.$rootScope.memberID = memberID;
        this.profIndex = 0;
        this.profilePics = [];
        this.HoroPics = [];
        this.$rootScope.$broadcast("homeBroadCast", false);
        this.$rootScope.$broadcast("profileBroadCast", true);  
        this.uReligion = "AA_DEFAULT";
        this.uMotherTongue = "AA_DEFAULT";
        this.uCommunity = "AA_DEFAULT";
        this.uSubCommunity = "AA_DEFAULT"; 
        this.uGothra = "AA_DEFAULT"; 
        this.uAstroRaasi = "AA_DEFAULT"; 
        this.uNakshatra = "AA_DEFAULT"; 
        this.uEducation = "AA_DEFAULT"; 
        this.uJobProfile = "AA_DEFAULT"; 
        this.uPProfessional = "AA_DEFAULT"; 
        this.uPQualification = "AA_DEFAULT"; 
        this.uPReligion = "AA_DEFAULT"; 
        this.uPMotherTongue = "AA_DEFAULT"; 
        this.uPCommunity = "AA_DEFAULT"; 
        this.uPSubCommunity = "AA_DEFAULT"; 
        this.uPRaasi = "AA_DEFAULT"; 
        this.uPNakshatra = "AA_DEFAULT"; 
        this.uPGothra = "AA_DEFAULT"; 
        this.bloodGroup = "AA_DEFAULT";
        this.fatherStatus = "AA_DEFAULT";
        this.motherStatus = "AA_DEFAULT";
        this.currentCountry = "AA_DEFAULT";
        this.currentState = "AA_DEFAULT";
        this.currentCity = "AA_DEFAULT";
        this.affluenceLevel = "AA_DEFAULT";
        this.incomeLL = "from";
        this.incomeUL = "to";
        this.ageLL = "from";
        this.ageUL = "to";
        this.heightLL = "from";
        this.heightUL = "to";
        this.weightLL = "from";
        this.weightUL = "to";
        this.uIncomeLL = "from";
        this.uIncomeUL = "to";

        this.showPartnerForm = false;
        this.showLifeStyleForm = false;
        this.showEDUForm = false;
        this.showAstroForm = false;
        this.showFamilyForm = false;
        this.showBasicForm = false;

        $('#login-modal').modal('hide');

        this.loggedUserService.getCurrentUserProfileInfo().then(function(resp){
          self.currentUserProfileInfo = resp.data;
          self.currentUserProfileInfo.name = self.$rootScope.logedUserName;
        });
        this.levels = [
                        {
                            "isLevel":false,
                            "showLevel": true
                        },{
                            "isLevel":false,
                            "showLevel": false
                        },{
                            "isLevel":false,
                            "showLevel": false
                        },{
                            "isLevel":false,
                            "showLevel": false
                        },{
                            "isLevel":false,
                            "showLevel": false
                        },{
                            "isLevel":false,
                            "showLevel": false
                        },
                    ];

        this.sLevels = {"uBasicInfo":1, "uFamilyInfo":2, "uAstroDet":3, "uEduAndCar":4, "uLifeStyle":5, "uPartnerPref":6}
        setTimeout(function() {      
            $("html,body").animate({'scrollTop':$(".top-banner").position().top});}, 800);
        this.b_type = "";
        this.s_tone = "";
        this.disability = ""; 
        this.hideAllOThersField();
        this.levelsKeyList = {"basicInfo" : 0}
        if(this.$rootScope.userRegData){   
            this.userData = this.$rootScope.userRegData[0]; 
            this.memberID = this.$rootScope.memberID;
            /*this.loggedUserService.basicInfoData = this.userData;
            this.bloodGroup = (this.userData.navPageData.bloodGroup.userValues.value!=='')? 
                               this.userData.navPageData.bloodGroup.userValues.value : this.bloodGroup;*/
            var index = (this.userData.navPage==="basicInfo")? 0 : 
                        (this.userData.navPage==="partnerProfiles")? this.sLevels.uPartnerPref : this.sLevels[this.userData.navPage];
            //var index = 6;
            this.$rootScope.currIndex = this.currIndex = index;
            _.each(this.levels, function(l,i){
                l.isLevel = (i<index && self.userData.navPage!=="basicInfo")? true : l.isLevel;
                l.showLevel = (i===(index-1))? true : l.showLevel;
            });
            index = (index===6)? 5 : index;
            this.switchToLevel(index);
            //FOR BASIC INFO
            this.currentEditView = this.levelsKeyList[this.userData.navPage];
            this.maritalStatus = this.uReligion = this.uMotherTongue = this.uCommunity =
            this.uGothra = this.uSubCommunity = "AA_DEFAULT";

            //FOR FAMILY INFO
            this.currentCountry = this.currentState =  this.currentCity = "AA_DEFAULT";
            this.countryList = {"AA_DEFAULT" : "Select your Country"};
            this.stateList = {"AA_DEFAULT" : "Select your State"};
            this.cityList = {"AA_DEFAULT" : "Select your City"};
            
            //FOR ASTRO 
            this.uAstroRaasi = this.uNakshatra = "AA_DEFAULT";

            //FOR EDUCATION
            this.uEducation = this.uJobProfile = "AA_DEFAULT";

            //FOR PARTNER PREFERENCE
            this.partnerMaritalStatus = "AA_DEFAULT";
            this.uPProfessional = this.uPQualification = this.uPReligion = this.uPMotherTongue = "AA_DEFAULT";
            this.uPCommunity = this.uPSubCommunity = this.uPRaasi = this.uPNakshatra = this.uPGothra = "AA_DEFAULT";        
            this.pCurrentCountry = this.pCurrentState =  this.pCurrentCity = "AA_DEFAULT";
            this.pcountryList = {"AA_DEFAULT" : "Select your Country"};
            this.pstateList = {"AA_DEFAULT" : "Select your State"};
            this.pcityList = {"AA_DEFAULT" : "Select your City"};
            //FOR BASIC INFO
            this.maritalStatusList = {"AA_DEFAULT" : "Your Marital Status"};
            this.religionList = {"AA_DEFAULT" : "Your Religion"};
            this.motherTongueList = {"AA_DEFAULT" : "Your Mother Tongue"};
            this.communityList = {"AA_DEFAULT" : "Your Community"};
            this.subCommunityList = {"AA_DEFAULT" : "Your Sub Community"};
            this.gothraList = {"AA_DEFAULT" : "Your Gothram"};

            //FOR ASTRO 
            this.astroRaasiList = {"AA_DEFAULT" : "Your Raasi"};
            this.nakshatraList = {"AA_DEFAULT" : "Your Nakshatra"};

            //FOR EDUCATION
            this.educationList = {"AA_DEFAULT" : "Your Qualification"};
            this.jobProfileList = {"AA_DEFAULT" : "Your Profession"};

            //FOR PARTNER PREFERENCE
            this.partnerMaritalStatusList = {"AA_DEFAULT" : "Your Marital Status"};
            this.partnerProfessionalList = {"AA_DEFAULT" : "Your Educational Prefernce"};
            this.partnerQualificationList = {"AA_DEFAULT" : "Your Qualification Prefernce"};
            this.partnerReligionList = {"AA_DEFAULT" : "Your Religious Prefernce"};
            this.partnerMotherTongueList = {"AA_DEFAULT" : "Your Mother Tongue Prefernce"};
            this.partnerCommunityList = {"AA_DEFAULT" : "Your Community Prefernce"};
            this.partnerSubCommunityList = {"AA_DEFAULT" : "Your Sub Community Prefernce"};
            this.partnerRaasiList = {"AA_DEFAULT" : "Your Raasi Prefernce"};
            this.partnerNakshatraList = {"AA_DEFAULT" : "Your Star Prefernce"};
            this.partnerGothraList = {"AA_DEFAULT" : "Your Gothram Prefernce"};

            this.profilePics = this.$rootScope.profilePics;
            this.profIndex = this.$rootScope.ppIndex;


            this.renderViewData();    
        }
        else{
            $rootScope.$broadcast("re-enter", uname, ppp, true);
            $location.path('/fetch');
        } 
        $('.partner-pref-info').on('change', 'select', function (e) {
            var errList ={"uPProfessional" : "partnerProfeesionalErr", "uPGothra" : "partnerGothraErr", "uPNakshatra" : "partnerStarErr",
                          "uPRaasi" : "partnerRassiErr", "uPSubCommunity" : "partnerSubCommunityErr", "uPCommunity" : "partnerCommunityErr",
                          "uPMotherTongue": "partnerMotherTongueErr", "uPReligion" : "partnerReligionErr", "uPQualification" : "partnerQualificationErr" };
            var val = $(e.target).val();  
            var id = e.target.id.split("select-")[1];

            if(val === "Others"){
                self.enableDisableText(val, id, errList[id.split("-")[0]]);
            }
        });
    }
}

ProfileEditController.prototype = {  
    renderViewData : function(){

        //FOR BASIC INFO
        this.getBasicInfoDetails();
        
        //FOR PARTNER PREFERENCE
        this.getPartnerPrefernceDetails();

        
        
        //FOR FAMILY DETAILS        
        this.getFamilyInfoDetails();

        //FOR ASTRO 
        this.getAstroDetails(); 

        //FOR EDUCATIONAL DETAIL
        this.getEducationalDetails();   

        //FOR LIFE STYLE
        this.getLifeStyleDetails();
    },
    renderJobProfileList : function(){
        var self = this; 
        _.each(this.loggedUserService.educationalInfo.navPageData.profession.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.jobProfileList[key] = val;
        });
        setTimeout(function() {
            self.uJobProfile = (self.loggedUserService.educationalInfo.navPageData.profession.userValues && 
                                self.loggedUserService.educationalInfo.navPageData.profession.userValues.value!=="")? 
                            self.loggedUserService.educationalInfo.navPageData.profession.userValues.key :
                            self.uJobProfile;
        });
    },
    renderEducationList : function(){
        var self = this; 
        _.each(this.loggedUserService.educationalInfo.navPageData.education.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.educationList[key] = val;
        });
        setTimeout(function() {
            self.uEducation = (self.loggedUserService.educationalInfo.navPageData.education.userValues && 
                                self.loggedUserService.educationalInfo.navPageData.education.userValues.value!=="")? 
                            self.loggedUserService.educationalInfo.navPageData.education.userValues.key :
                            self.uEducation;
        });
    },
    renderPartnerProfessionalList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.profession.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerProfessionalList[key] = val;
        });
        setTimeout(function() {
            self.uPProfessional = (self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues.keyName1.key :
                            self.uPProfessional;
            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues, function(val, key){
                self["uPProfessional"+count] = "AA_DEFAULT";
                self["partnerProfessionalList"+count] = self.partnerProfessionalList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues, function(val, key){ 
                self["uPProfessional"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues && 
                                    self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues[key].value!=="")? 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.profession.userValues[key].key :
                                self["uPProfessional"+count];
                count++;
            });
            self.alterSelectList("professionID");
        });

    },
    renderPartnerQualificationList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.education.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerQualificationList[key] = val;
        });
        setTimeout(function() {
            self.uPQualification = (self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues.keyName1.key :
                            self.uPQualification;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues, function(val, key){
                self["uPQualification"+count] = "AA_DEFAULT";
                self["partnerQualificationList"+count] = self.partnerQualificationList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues, function(val, key){ 
                self["uPQualification"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.education.userValues[key].key :
                            self["uPQualification"+count];
                count++;
            });
            self.alterSelectList("QualificationID");
        });
    },
    renderPartnerReligionList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.religion.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerReligionList[key] = val;
        });
        setTimeout(function() {
            self.uPReligion = (self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues.keyName1.key :
                            self.uPReligion;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues, function(val, key){
                self["uPReligion"+count] = "AA_DEFAULT";
                self["partnerReligionList"+count] = self.partnerReligionList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues, function(val, key){ 
                self["uPReligion"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.religion.userValues[key].key :
                            self["uPReligion"+count];
                count++;
            });

            self.alterSelectList("ReligionID");
        });
    },
    renderPartnerMotherTongueList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.language.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerMotherTongueList[key] = val;
        });
        setTimeout(function() {
            self.uPMotherTongue = (self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues.keyName1.key :
                            self.uPMotherTongue;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues, function(val, key){
                self["uPMotherTongue"+count] = "AA_DEFAULT";
                self["partnerMotherTongueList"+count] = self.partnerMotherTongueList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues, function(val, key){ 
                self["uPMotherTongue"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.language.userValues[key].key :
                            self["uPMotherTongue"+count];
                count++;
            });

            self.alterSelectList("motherTongueID");
        });
    },
    renderPartnerCommunityList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.caste.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerCommunityList[key] = val;
        });
        setTimeout(function() {
            self.uPCommunity = (self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues.keyName1.key :
                            self.uPCommunity;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues, function(val, key){
                self["uPCommunity"+count] = "AA_DEFAULT";
                self["partnerCommunityList"+count] = self.partnerCommunityList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues, function(val, key){ 
                self["uPCommunity"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.caste.userValues[key].key :
                            self["uPCommunity"+count];
                count++;
            });
            self.alterSelectList("casteID");
        });
    },
    renderPartnerSubCommunityList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerSubCommunityList[key] = val;
        });
        setTimeout(function() {
            self.uPSubCommunity = (self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues.keyName1.key :
                            self.uPSubCommunity;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues, function(val, key){
                self["uPSubCommunity"+count] = "AA_DEFAULT";
                self["partnerSubCommunityList"+count] = self.partnerSubCommunityList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues, function(val, key){ 
                self["uPSubCommunity"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.subCaste.userValues[key].key :
                            self["uPSubCommunity"+count];
                count++;
            });
            self.alterSelectList("SubCasteID");
        });
    },
    renderPartnerRaasiList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.raasi.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerRaasiList[key] = val;
        });
        setTimeout(function() {
            self.uPRaasi = (self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues.keyName1.key :
                            self.uPRaasi; 

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues, function(val, key){
                self["uPRaasi"+count] = "AA_DEFAULT";
                self["partnerRaasiList"+count] = self.partnerRaasiList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues, function(val, key){ 
                self["uPRaasi"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.raasi.userValues[key].key :
                            self["uPRaasi"+count];
                count++;
            });
            self.alterSelectList("RaasiID");

        });
    },
    renderPartnerNakshatraList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.star.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerNakshatraList[key] = val;
        });
        setTimeout(function() {
            self.uPNakshatra = (self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues.keyName1.key :
                            self.uPNakshatra;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues, function(val, key){
                self["uPNakshatra"+count] = "AA_DEFAULT";
                self["partnerNakshatraList"+count] = self.partnerNakshatraList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues, function(val, key){ 
                self["uPNakshatra"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.star.userValues[key].key :
                            self["uPNakshatra"+count];
                count++;
            });
            self.alterSelectList("StarID");
        });
    },
    renderPartnerGothraList :function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.gothram.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerGothraList[key] = val;
        });
        setTimeout(function() {
            self.uPGothra = (self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues.keyName1.key :
                            self.uPGothra;

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues, function(val, key){
                self["uPGothra"+count] = "AA_DEFAULT";
                self["partnerGothraList"+count] = self.partnerGothraList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues, function(val, key){ 
                self["uPGothra"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.gothram.userValues[key].key :
                            self["uPGothra"+count];
                count++;
            });
            self.alterSelectList("GothramID");
        });
    },
    renderAstroNakshatraList : function(){
        var self = this; 
        _.each(this.loggedUserService.astroInfo.navPageData.star.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.nakshatraList[key] = val;
        });
        setTimeout(function() {
            self.uNakshatra = (self.loggedUserService.astroInfo.navPageData.star.userValues && 
                                self.loggedUserService.astroInfo.navPageData.star.userValues.value!=="")? 
                            self.loggedUserService.astroInfo.navPageData.star.userValues.key :
                            self.uNakshatra;
        });
    },
    renderCountryList : function(){
        var self = this; 
        _.each(this.loggedUserService.familyInfo.navPageData.country.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.countryList[key] = val;
        });
        setTimeout(function() {
            self.currentCountry = (self.loggedUserService.familyInfo.navPageData.country.userValues && 
                                self.loggedUserService.familyInfo.navPageData.country.userValues.value!=="")? 
                            self.loggedUserService.familyInfo.navPageData.country.userValues.key :
                            self.currentCountry; 
        });
    },
    renderPCountryList : function(){
        var self = this; 

        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.country.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.pcountryList[key] = val;
        });
        setTimeout(function() {

            self.pCurrentCountry = (self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues.key :
                            self.pCurrentCountry; 

            var count = 1;

            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues, function(val, key){
                self["pCurrentCountry"+count] = "AA_DEFAULT";
                self["countryList"+count] = self.pcountryList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues, function(val, key){ 
                self["pCurrentCountry"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues[key].key :
                            self["pCurrentCountry"+count];
                count++;
            });
            self.alterSelectList("countryID1");
        });
    },
    renderStateList : function(){
        var self = this; 
        _.each(this.loggedUserService.familyInfo.navPageData.state.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.stateList[key] = val;
        });
        setTimeout(function() {
            self.currentState = (self.loggedUserService.familyInfo.navPageData.state.userValues && 
                                self.loggedUserService.familyInfo.navPageData.state.userValues.value!=="")? 
                            self.loggedUserService.familyInfo.navPageData.state.userValues.key :
                            self.currentState;
        });
    },
    renderPStateList : function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.state.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.pstateList[key] = val;
        });

        setTimeout(function() {

            self.pCurrentState = (self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues.key :
                            self.pCurrentState;

            var count = 1;

            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues, function(val, key){
                self["pCurrentState"+count] = "AA_DEFAULT";
                self["stateList"+count] = self.pstateList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues, function(val, key){ 
                self["pCurrentState"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues[key].key :
                            self["pCurrentState"+count];
                count++;
            });            
            self.alterSelectList("StateID1");
        });
    },
    renderCityList : function(){
        var self = this; 
        _.each(this.loggedUserService.familyInfo.navPageData.city.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.cityList[key] = val;
        });
        setTimeout(function() {
            self.currentCity = (self.loggedUserService.familyInfo.navPageData.city.userValues && 
                                self.loggedUserService.familyInfo.navPageData.city.userValues.value!=="")? 
                            self.loggedUserService.familyInfo.navPageData.city.userValues.key :
                            self.currentCity;
        });
    },
    renderPCityList : function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.city.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.pcityList[key] = val;
        });
        setTimeout(function() {
            self.pCurrentCity = (self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues.key :
                            self.pCurrentCity;
            var count = 1;

            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues, function(val, key){
                self["pCurrentCity"+count] = "AA_DEFAULT";
                self["cityList"+count] = self.pcityList;
                count++;
            });

            var count = 1;
            _.each(self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues, function(val, key){ 
                self["pCurrentCity"+count] = (self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues[key].value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues[key].key :
                            self["pCurrentCity"+count];
                count++;
            });           
            self.alterSelectList("CityID1");
        });
    },
    renderAstroRaasiList : function(){
        var self = this; 
        _.each(this.loggedUserService.astroInfo.navPageData.raasi.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.astroRaasiList[key] = val;
        });
        setTimeout(function() {
            self.uAstroRaasi = (self.loggedUserService.astroInfo.navPageData.raasi.userValues && 
                                self.loggedUserService.astroInfo.navPageData.raasi.userValues.value!=="")? 
                            self.loggedUserService.astroInfo.navPageData.raasi.userValues.key :
                            self.uAstroRaasi;
        });
    },
    renderMaritalData : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.martialStatus.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.maritalStatusList[key] = val;
        });
        setTimeout(function() {
            self.maritalStatus = (self.loggedUserService.basicInfoData.navPageData.martialStatus.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.martialStatus.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.martialStatus.userValues.key :
                            self.maritalStatus;
        });
    },
    renderPartnerMaritalData : function(){
        var self = this; 
        _.each(this.loggedUserService.partnerPreferenceInfo.navPageData.martialStatus.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.partnerMaritalStatusList[key] = val;
        });
        setTimeout(function() {
            self.partnerMaritalStatus = (self.loggedUserService.partnerPreferenceInfo.navPageData.martialStatus.userValues && 
                                self.loggedUserService.partnerPreferenceInfo.navPageData.martialStatus.userValues.keyName1.value!=="")? 
                            self.loggedUserService.partnerPreferenceInfo.navPageData.martialStatus.userValues.keyName1.key :
                            self.partnerMaritalStatus;
        });
    },
    renderUserReligionData : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.religion.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.religionList[key] = val;
        });
        setTimeout(function() {
            self.uReligion = (self.loggedUserService.basicInfoData.navPageData.religion.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.religion.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.religion.userValues.key :
                            self.uReligion;
        });
    },
    renderUserMotherToungueList : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.language.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.motherTongueList[key] = val;
        });
        setTimeout(function() {
            self.uMotherTongue = (self.loggedUserService.basicInfoData.navPageData.language.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.language.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.language.userValues.key :
                            self.uMotherTongue;
                        });
    },
    renderUserCommunityList : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.caste.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.communityList[key] = val;
        });
        setTimeout(function() {
            self.uCommunity = (self.loggedUserService.basicInfoData.navPageData.caste.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.caste.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.caste.userValues.key :
                            self.uCommunity;
                        });
    },
    renderUserSubCommunityList : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.subCaste.defaultValues, function(val, key){
            self.subCommunityList[key] = val;
        });
        setTimeout(function() {
            self.uSubCommunity = (self.loggedUserService.basicInfoData.navPageData.subCaste.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.subCaste.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.subCaste.userValues.key :
                            self.uSubCommunity;
                        });
    },
    renderUserGothraList : function(){
        var self = this; 
        _.each(this.loggedUserService.basicInfoData.navPageData.gothram.defaultValues, function(val, key){
            key = (val==="Others")? "Others" : key;
            self.gothraList[key] = val;
        });
        setTimeout(function() {
            self.showBasicForm = true;
            self.uGothra = (self.loggedUserService.basicInfoData.navPageData.gothram.userValues && 
                                self.loggedUserService.basicInfoData.navPageData.gothram.userValues.value!=="")? 
                            self.loggedUserService.basicInfoData.navPageData.gothram.userValues.key :
                            self.uGothra; 
                        });
    },
    enableDisableText : function(event){ 
        /*var inputs = event.currentTarget.parentNode.parentNode.querySelectorAll("input[type='text']");        
        _.each(inputs, function(i){
            i.disabled = !i.disabled;
        });*/
    },
    checkBIinputStatus: function(id, view){
        var id = id;
        var isTextBoxFull = true;
        var isRadioSelected = (this.b_type!==""&&this.s_tone!==""&&this.disability!=="")? true : false; 
        var textBox = document.querySelectorAll("#"+id+" input[type='text']"); 
        var select = document.querySelectorAll("#"+id+" select");
        var isSelectChosen = true;
        _.each(select,function(text){
            if(text.value==="Select your Choice"){
                isSelectChosen = false;
            }
        }); 

        _.each(textBox,function(text){
            if(text.value.length===0){
                isTextBoxFull = false;
            }
        }); 

        this.levels[this.currentEditView].isLevel = (isTextBoxFull && isRadioSelected && isSelectChosen)? true : 
                                                    false;               
        this.currentEditView = view;    
        this.switchToLevel(view) ;
    },
    switchToLevel : function(idx, fromPending){
        var self = this;
        if(idx > this.currIndex && fromPending){
             this.$rootScope.registrStatus = "Please click Save to proceed once done with entering all the details..."
            $("#reg-succs-modal").modal('show');
        }
        else{
            _.each(this.levels, function(level, index){
                level.showLevel = (idx === index)? true : false;
                level.isLevel = (self.$rootScope.isPrefUpdated)? true : level.isLevel;
            });
            this.hideAllOThersField();
        }
        if(idx === 2){
            //this.getFamilyInfoDetails(); 
        }
        if(idx === 3){
           //this.getAstroDetails();
        }        
        if(idx === 4){
            //this.getEducationalDetails();
        }     
        if(idx === 5){
            //this.getLifeStyleDetails();
        }             
        if(idx === 6){
            //this.getPartnerPrefernceDetails();
        }

        this.currentEditView = idx;  
        setTimeout(function() {      
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});});
    },
    getBasicInfoDetails : function(){
        var self = this; 
        var basicInfoReq = [{"memberID": this.memberID,"currPage": "basicInfo",  "userData":{"": {"isOthers": "false","value": ""}}}];
        this.showBasicForm = false;
        var requestJSON ="requestJSON="+JSON.stringify(basicInfoReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data); 
                   if(data[0].isnavPageDataAvailable==="true"){  
                        self.loggedUserService.basicInfoData = data[0];
                        self.bloodGroup = (data[0].navPageData.bloodGroup.userValues.value!=='')? 
                        data[0].navPageData.bloodGroup.userValues.value : self.bloodGroup;
                        self.renderMaritalData();
                        self.renderUserReligionData();
                        self.renderUserMotherToungueList();
                        self.renderUserCommunityList();
                        self.renderUserSubCommunityList();
                        self.renderUserGothraList(); 

                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
    },
    getFamilyInfoDetails : function(){
        var self = this; 
        var familyInfoReq =[{"memberID": this.memberID,"currPage": "familyInfo",  "userData":{"": {"isOthers": "false","value": ""}}}];
        this.showFamilyForm = false;
        var requestJSON ="requestJSON="+JSON.stringify(familyInfoReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                    self.showFamilyForm = true;
                   if(data[0].isnavPageDataAvailable==="true"){  
                        self.loggedUserService.familyInfo = data[0];
                        self.renderCountryList();
                        self.renderStateList();
                        self.renderCityList();
                            self.fatherStatus = (self.loggedUserService.familyInfo.navPageData.fatherStatus.userValues.value!=='')? 
                                                self.loggedUserService.familyInfo.navPageData.fatherStatus.userValues.value : self.fatherStatus;
                            self.motherStatus = (self.loggedUserService.familyInfo.navPageData.motherStatus.userValues.value!=='')? 
                                               self.loggedUserService.familyInfo.navPageData.motherStatus.userValues.value : self.motherStatus;
                            self.currentCountry = (self.loggedUserService.familyInfo.navPageData.country.userValues.value!=='')? 
                                               self.loggedUserService.familyInfo.navPageData.country.userValues.key : self.currentCountry;
                            self.currentState = (self.loggedUserService.familyInfo.navPageData.state.userValues.value!=='')? 
                                               self.loggedUserService.familyInfo.navPageData.state.userValues.key : self.currentState;
                            self.currentCity = (self.loggedUserService.familyInfo.navPageData.city.userValues.value!=='')? 
                                               self.loggedUserService.familyInfo.navPageData.city.userValues.key : self.currentCity;
                            self.affluenceLevel = (self.loggedUserService.familyInfo.navPageData.familyStatus.userValues.value!=='')? 
                                               self.loggedUserService.familyInfo.navPageData.familyStatus.userValues.value : self.affluenceLevel;
                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
    },
    getAstroDetails : function(){
        var self = this; 
        var astroDetailsReq =[{"memberID": this.memberID,"currPage": "astroDet",  "userData":{"": {"isOthers": "false","value": ""}}}];
        this.showAstroForm = false;
        var requestJSON ="requestJSON="+JSON.stringify(astroDetailsReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                    self.showAstroForm = true;
                   if(data[0].isnavPageDataAvailable==="true"){  
                        self.loggedUserService.astroInfo = data[0]; 
                        self.renderAstroRaasiList();
                        self.renderAstroNakshatraList();
                        var horoPics = self.loggedUserService.astroInfo.navPageData.horLoc.userValues.value.split("||");
                        self.HoroPics = [];
                        _.each(horoPics, function(img){
                            var src = img.split(" ").join("+");
                            if(img.length>5){
                                self.HoroPics.push({url: src, resized : {dataURL : src}});
                            }
                        });
                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
    },
    getEducationalDetails : function(){
        var self = this; 
        var eduDetailsReq =[{"memberID": this.memberID,"currPage": "eduAndCar",  "userData":{"": {"isOthers": "false","value": ""}}}];
        this.showEDUForm = false;
        var requestJSON ="requestJSON="+JSON.stringify(eduDetailsReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                    self.showEDUForm = true;
                   if(data[0].isnavPageDataAvailable==="true"){  
                        self.loggedUserService.educationalInfo = data[0];  
                        self.renderEducationList();
                        self.renderJobProfileList(); 
                        self.incomeLL = (self.loggedUserService.educationalInfo.navPageData.annualIncome_LL.userValues.value!=='')? 
                                               self.loggedUserService.educationalInfo.navPageData.annualIncome_LL.userValues.value : self.incomeLL;
                        self.incomeUL = (self.loggedUserService.educationalInfo.navPageData.annualIncome_UL.userValues.value!=='')? 
                                               self.loggedUserService.educationalInfo.navPageData.annualIncome_UL.userValues.value : self.incomeUL;
                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
    },
    getLifeStyleDetails : function(){
        var self = this; 
        var eduDetailsReq =[{"memberID": this.memberID,"currPage": "lifeStyle",  "userData":{"": {"isOthers": "false","value": ""}}}];
        this.showLifeStyleForm = false;
        var requestJSON ="requestJSON="+JSON.stringify(eduDetailsReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                    self.showLifeStyleForm = true;
                   if(data[0].isnavPageDataAvailable==="true"){  
                        self.loggedUserService.lifeStyleInfo = data[0];   
                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
    },
    getPartnerPrefernceDetails : function(){
        this.showPartnerForm = false;
        var self = this; 
        var partnerPrefReq =[{"memberID": this.memberID,"currPage": "partnerPref",  "userData":{"": {"isOthers": "false","value": ""}}}];
        
        var requestJSON ="requestJSON="+JSON.stringify(partnerPrefReq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                   if(data[0].isnavPageDataAvailable==="true"){ 
                        self.showPartnerForm = true; 
                        self.loggedUserService.partnerPreferenceInfo = data[0];                        
                        self.ageLL = (self.loggedUserService.partnerPreferenceInfo.navPageData.age_LL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.age_LL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.age_LL.userValues.keyName1.value : self.ageLL;
                        self.ageUL = (self.loggedUserService.partnerPreferenceInfo.navPageData.age_UL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.age_UL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.age_UL.userValues.keyName1.value : self.ageUL;  
                                                                       
                        self.heightLL = (self.loggedUserService.partnerPreferenceInfo.navPageData.height_LL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.height_LL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.height_LL.userValues.keyName1.value : self.heightLL;
                        self.heightUL = (self.loggedUserService.partnerPreferenceInfo.navPageData.height_UL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.height_UL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.height_UL.userValues.keyName1.value : self.heightUL;   
                                                                       
                        self.weightLL = (self.loggedUserService.partnerPreferenceInfo.navPageData.weight_LL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.weight_LL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.weight_LL.userValues.keyName1.value : self.weightLL;
                        self.weightUL = (self.loggedUserService.partnerPreferenceInfo.navPageData.weight_UL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.weight_UL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.weight_UL.userValues.keyName1.value : self.weightUL;   
                                                                       
                        self.uIncomeLL = (self.loggedUserService.partnerPreferenceInfo.navPageData.income_LL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.income_LL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.income_LL.userValues.keyName1.value : self.uIncomeLL;
                        self.uIncomeUL = (self.loggedUserService.partnerPreferenceInfo.navPageData.income_UL.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.income_UL.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.income_UL.userValues.keyName1.value : self.uIncomeUL;   
                                               
                       /* self.pCurrentCountry = (self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.country.userValues.keyName1.key : self.pCurrentCountry;  
                                               
                        self.pCurrentState = (self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.state.userValues.keyName1.key : self.pCurrentState;  
                                               
                        self.pCurrentCity = (self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues.keyName1 && self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues.keyName1.value!=='')? 
                                               self.loggedUserService.partnerPreferenceInfo.navPageData.city.userValues.keyName1.key : self.pCurrentCity;   */


                        self.renderPartnerMaritalData();
                        self.renderPartnerProfessionalList();
                        self.renderPartnerQualificationList();
                        self.renderPartnerReligionList();
                        self.renderPartnerMotherTongueList();
                        self.renderPartnerCommunityList();
                        self.renderPartnerSubCommunityList();
                        self.renderPartnerRaasiList();
                        self.renderPartnerNakshatraList();
                        self.renderPartnerGothraList();
                        self.renderPCountryList();
                        self.renderPStateList();
                        self.renderPCityList();                        
                        self.hideAllOThersField();
                        setTimeout(function() {                            
                            var clonnedSel = document.querySelectorAll("#clonned-select");
                            _.each(clonnedSel, function(sel){
                                $(sel).remove();
                            });
                        }, 100);
                   }
                   else{ 
                   }
                  })
                .error(function () {
                     //console.log("error"); 
                }); 

    },
    checkRIinputStatus : function(id, view){
        var isAllSelected = true;
        var select = document.querySelectorAll("#"+id+" select");

        _.each(select,function(text){
            if(text.value==="Select your Choice"){
                isAllSelected = false;
            }
        }); 

        this.levels[this.currentEditView].isLevel = (isAllSelected)? true :  false;
        this.switchToLevel(view) ;
    },
    cloneSelect : function(event, id, errHolder){ 
        var self = this;             
        var allSelect = document.querySelectorAll('select[name='+id+']');
        var isNoValueSelected = false;
        _.each(allSelect, function(select){
            isNoValueSelected = (select.value === "AA_DEFAULT")? true : isNoValueSelected;
        });

        if(isNoValueSelected){
            this[errHolder] = "Please Select a value";
            setTimeout(function() {
                self[errHolder] = "";
            }, 500);
        }
        else{
            this[errHolder] = "";
            var element = document.createElement('span'); 
            element.id = "clonned-select";
            //var el = event.currentTarget.parentNode.children[0];
            var el = allSelect[allSelect.length-1];
            var cl = el.cloneNode(true);
            $(cl).removeClass('user-value'); 
            var split = $(cl)[0].id.split("-");
            $(cl)[0].id = split[0] + "-" + split[1] + "-o";
            console.log($(cl).attr("id")) ;
            element.appendChild(cl);  

            $(element).insertBefore(event.currentTarget);             
            //console.log($('select[name='+id+'] option:selected'));
            $(element.children[0]).children('option').removeAttr("selected"); 

            var iElement = document.createElement('input');
            iElement.name = id + "1";
            iElement.id = split[1] + "-o"; 
            iElement.className = "others-text";
            iElement.type = "text";
            iElement.placeholder = "Enter your preference"; 
            $(iElement).hide();
            $(iElement).removeAttr("required"); 

            $(iElement).insertBefore(event.currentTarget); 

            _.each(allSelect, function(allS){
                _.each(element.children[0].options, function(opt){
                    opt.disabled = (opt.value === allS.value)? true : opt.disabled;
                });
            });
        }
    },
    cloneLocationSelect : function(event, errHolder){ 
        
        var self = this;
        var country = document.querySelectorAll('#pCountry');
        var state = document.querySelectorAll('#pState');
        var city = document.querySelectorAll('#pCity');

        var isNoValueSelected = false;
        _.each(country, function(select){
            isNoValueSelected = (select.value === "AA_DEFAULT")? true : isNoValueSelected;
        }); 
        _.each(state, function(select){
            isNoValueSelected = (select.value === "AA_DEFAULT")? true : isNoValueSelected;
        }); 
        _.each(city, function(select){
            isNoValueSelected = (select.value === "AA_DEFAULT")? true : isNoValueSelected;
        });

        if(isNoValueSelected){
            this[errHolder] = "Please Select All the Values";
            setTimeout(function() {
                self[errHolder] = "";
            }, 500);
        }
        else{            
            var element = document.createElement('span');        
            var s1 = country[0];
            var s2 = state[0];
            var s3 = city[0];

            var c1 = s1.cloneNode(true);
            var c2 = s2.cloneNode(true);
            var c3 = s3.cloneNode(true);

            $(c1).children('option').removeAttr("selected"); $(c1).removeClass('user-value');   
            $(c2).children('option').removeAttr("selected"); $(c2).removeClass('user-value');   
            $(c3).children('option').removeAttr("selected"); $(c3).removeClass('user-value');

            element.appendChild(c1); 
            element.appendChild(c2); 
            element.appendChild(c3);  

            _.each(country, function(allS){
                _.each(element.children[0].options, function(opt){
                    opt.disabled = (opt.value === allS.value)? true : opt.disabled;
                });
            });

            _.each(state, function(allS){
                _.each(element.children[1].options, function(opt){
                    opt.disabled = (opt.value === allS.value)? true : opt.disabled;
                });
            });

            _.each(city, function(allS){
                _.each(element.children[2].options, function(opt){
                    opt.disabled = (opt.value === allS.value)? true : opt.disabled;
                });
            }); 

            $(element).insertBefore(event.currentTarget);
        }
    },
    insertProfToList : function(img, index){
        if(this.profilePics.length<4){        
            this.profilePics.reverse()[this.profilePics.length] = img;   
        }
    },
    insertHoroToList : function(img, index){
        if(this.HoroPics.length<4){   
            this.HoroPics.reverse()[this.HoroPics.length] = img;   
        }
    },
    triggerFileEvent : function(){
        $('#uploading').trigger('click');
    },
    triggerHororFileEvent : function(){
        $('#upload-horo').trigger('click');
    },
    removeGalleryImage : function(index){
        this.profilePics.reverse().splice(index, 1);
        this.profIndex = 0;
    },
    removeHoroGalleryImage : function(index){
        this.HoroPics.reverse().splice(index, 1); 
    },
    focusInput : function() {
        this.statusDisabled=false;
        setTimeout(function() {
            $('.status-holder').focus();
        });
    },
    hideAllOThersField : function(){ 
        setTimeout(function() {
            var othersText = document.querySelectorAll(".others-text");
            _.each(othersText, function(text){
                $(text).hide();
                $(text).removeAttr("required"); 
            });
        }, 500);
    },
    enableDisableText : function(value, id, errHolder){
        this[errHolder] = "";
        if(value==="Others"){
            $("#"+ id).show();
            $("#"+ id).attr('required', 'required');
        }
        else{
            $("#"+ id).hide();   
        }
    },
    sendBasicInfoFormData : function() { 
        var self = this;
       if( this.maritalStatus !== "AA_DEFAULT" && this.uReligion !== "AA_DEFAULT" &&
            this.uMotherTongue !== "AA_DEFAULT" && this.uCommunity !== "AA_DEFAULT" &&
            this.uSubCommunity !== "AA_DEFAULT" && this.uGothra !== "AA_DEFAULT" && this.bloodGroup !== "AA_DEFAULT"){
                this.maritalStatusErr = "";
                this.religionListErr = "";
                this.motherTongueListErr = "";
                this.communityListErr = "";
                this.subCommunityListErr = "";
                this.gothraListErr = "";
                this.bloodGroupErr = "";
                var serializedFormData = $('.basic-info').serializeArray();
                //console.log(serializedFormData);
                var basicInfoData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uBasicInfo",
                    "userData" : this.formatBasicInfoData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                //console.log(basicInfoData);
                this.$rootScope.registrStatus = "Processing...";
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(basicInfoData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[0].showLevel = true;                                
                                self.levels[0].isLevel = true;
                                setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);
                                self.currIndex += 1;
                                self.switchToLevel(1);
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }                           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        });
       }
       else{

                this.maritalStatusErr = (this.maritalStatus === "AA_DEFAULT")? "*Please select your Marital Status" : "";
                this.religionListErr = (this.uReligion === "AA_DEFAULT")? "*Please select your Religion" : "";
                this.motherTongueListErr = (this.uMotherTongue === "AA_DEFAULT")? "*Please select your Mother Tongue" : "";
                this.communityListErr = (this.uCommunity === "AA_DEFAULT")? "*Please select your Community List" : "";
                this.subCommunityListErr = (this.uSubCommunity === "AA_DEFAULT")? "*Please select your Sub Community" : "";
                this.gothraListErr = (this.uGothra === "AA_DEFAULT")? "*Please select your Gothra List" : "";
                this.bloodGroupErr = (this.bloodGroup === "AA_DEFAULT")? "*Please select your Blood Group" : "";

       }

    }, 
    sendFamilyInfoData : function() { 
        var self = this;
       if( this.fatherStatus !== "Your Father's Status" &&
            this.motherStatus !== "Your Mother's Status" &&
            this.currentCountry !== "Select your Country" &&
            this.currentState !== "Select your State" &&
            this.currentCity !== "Select your City" &&
            this.affluenceLevel !== "Your Affluence Level"){
                this.fatherStatusErr = "";
                this.motherStatusErr = "";
                this.affluenceErr = ""; 
                this.currentLocErr = "";

                var serializedFormData = $('.family-info').serializeArray();

                var familyInfoData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uFamilyInfo",
                    "userData" : this.formatFamilyInfoData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                //console.log(familyInfoData);
                this.$rootScope.registrStatus = "Processing..."
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(familyInfoData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[1].showLevel = true;                                
                                self.levels[1].isLevel = true;
                                setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);
                                self.currIndex += 1;
                                self.switchToLevel(2);
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        });
       }
       else{

                this.fatherStatusErr = (this.fatherStatus === "Your Father's Status")? "*Please select your Father Status" : "";
                this.motherStatusErr =(this.motherStatus === "Your Mother's Status")? "*Please select your Mother Status" : "";
                this.affluenceErr =(this.affluenceLevel === "Your Affluence Level")? "*Please select your Affluence Level" : ""; 
                this.currentLocErr = (this.currentCity === "Select your City")? "*Please select your City" : this.currentLocErr; 
                this.currentLocErr = (this.currentState === "Select your State")? "*Please select your State" : this.currentLocErr;
                this.currentLocErr = (this.currentCountry === "Select your Country")? "*Please select your Country" : this.currentLocErr        
                this.currentLocErr = (this.currentCountry !== "Select your Country" && 
                                      this.currentState !== "Select your State" && this.currentCity !== "Select your City")? "" : this.currentLocErr;
       }

    },
    sendAstroInfoDetails : function(){        
        var self = this;
        if(this.uAstroRaasi !== "AA_DEFAULT" && this.uNakshatra !== "AA_DEFAULT"){
            this.rassiErr = "";
            this.starErr =  "";
            var serializedFormData = $('.astro-info').serializeArray();

                var astroInfoData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uAstroDet",
                    "userData" : this.formatAstroInfoData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                //console.log(astroInfoData);
                this.$rootScope.registrStatus = "Processing..."
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(astroInfoData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[2].showLevel = true;                                
                                self.levels[2].isLevel = true;
                                setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);
                                self.currIndex += 1;
                                self.switchToLevel(3);
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        });
        }
        else{
            this.rassiErr = (this.uAstroRaasi === "AA_DEFAULT")? "*Please select your Raasi" : "";
            this.starErr = (this.uNakshatra === "AA_DEFAULT")? "*Please select your Birth Star" : "";
        }
    },
    sendEducationalInfoData : function(){
        var self = this;
        if(this.uEducation !== "AA_DEFAULT" && this.uJobProfile !== "AA_DEFAULT" && this.incomeLL !== "from" 
                && this.incomeUL!== "to"){
            this.educationErr = "";
            this.jobProfileErr =  "";
            this.upperLimErr = "";
            this.lowerLimErr= "";
            var serializedFormData = $('.edu-info').serializeArray();

                var eduInfoData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uEduAndCar",
                    "userData" : this.formatEducationlInfoData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                //console.log(eduInfoData);
                this.$rootScope.registrStatus = "Processing..."
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(eduInfoData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[3].showLevel = true;                                
                                self.levels[3].isLevel = true;
                                setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);
                                self.currIndex += 1;
                                self.switchToLevel(4);
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }           
                           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        });
        }
        else{
            this.educationErr = (this.uEducation === "AA_DEFAULT")? "*Please select your Educational Info" : "";
            this.jobProfileErr = (this.uJobProfile === "AA_DEFAULT")? "*Please select your Job Profile" : "";            
            this.upperLimErr = (this.incomeUL === "to")? "*Please select your Income Upper Limit" : "";
            this.lowerLimErr = (this.incomeLL === "from")? "*Please select your Income Lower Limit" : "";
        }
    },
    sendLifeStyleInfoDetails : function(){
        var self = this;
            var serializedFormData = $('.lifeStyle-info').serializeArray();

                var lifeStyleData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uLifeStyle",
                    "userData" : this.formatLifeStyleData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                //console.log(lifeStyleData);
                this.$rootScope.registrStatus = "Processing..."
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(lifeStyleData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[4].showLevel = true;                                
                                self.levels[4].isLevel = true;
                                setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);
                                self.currIndex += 1;
                                self.switchToLevel(5);
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        });
    },
    sendPartnerPreferenceData : function(){
        if( this.partnerMaritalStatus !== "AA_DEFAULT" && this.ageLL !== "from" && this.ageUL !== "to" &&
            this.heightLL !== "from" && this.heightUL !== "to" && this.weightLL !== "from" && this.weightUL !== "to" &&
            this.uIncomeLL !== "from" && this.uIncomeUL !== "to" &&
            this.uPProfessional !== "AA_DEFAULT" && this.uPQualification !== "AA_DEFAULT" && 
            this.uPReligion !== "AA_DEFAULT" && this.uPMotherTongue !== "AA_DEFAULT" &&
            this.uPCommunity !== "AA_DEFAULT" && this.uPSubCommunity !== "AA_DEFAULT" && 
            this.uPRaasi !== "AA_DEFAULT" && this.uPNakshatra !== "AA_DEFAULT" && this.uPGothra !== "AA_DEFAULT" &&        
            this.pCurrentCountry !== "AA_DEFAULT" && this.pCurrentState !== "AA_DEFAULT" &&  this.pCurrentCity  !== "AA_DEFAULT"){
                
                this.partnerMaritalStatusErr = "";
                this.partnerAgeLowerLimErr = "";
                this.partnerAgeUpperLimErr = "";
                this.partnerIncomeLowerLimErr = "";
                this.partnerIncomeUpperLimErr = "";
                this.partnerWeightLowerLimErr = "";
                this.partnerWeightUpperLimErr = "";
                this.partnerHeightLowerLimErr = "";
                this.partnerHeightUpperLimErr = "";
                this.partnerProfeesionalErr = "";
                this.partnerQualificationErr = "";
                this.partnerReligionErr = "";
                this.partnerMotherTongueErr = "";
                this.partnerCommunityErr = "";
                this.partnerSubCommunityErr =  "";
                this.partnerRassiErr = "";
                this.partnerStarErr = "";
                this.partnerGothraErr = "";
                this.partnerCountryErr  = "";
                this.partnerStateErr  = "";
                this.partnerCityErr  = "";

                var self = this;
                var serializedFormData = $('.partner-pref-info').serializeArray();
                console.log($('.partner-pref-info'));
                console.log(serializedFormData);
                var partPrefData = [{
                    "memberID" : this.memberID,
                    "currPage" : "uPartnerPref",
                    "userData" : this.formatPartnerPrefData(serializedFormData)
                }];  
                

                var config = {
                    headers : {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                    }
                }
                this.videsh = partPrefData;
                //console.log(partPrefData);
                this.$rootScope.registrStatus = "Processing..."
                $("#reg-succs-modal").modal('show');
                var requestJSON ="requestJSON="+JSON.stringify(partPrefData);
                $.post('UserActionCommand', requestJSON)
                        .success(function (d, status, headers, config) {
                            var data= JSON.parse(d); 
                            //console.log(data);
                            if(data[0].status === "success"){
                                self.levels[5].showLevel = true;                                
                                self.levels[5].isLevel = true;  
                                /*setTimeout(function() {
                                    $("#reg-succs-modal").modal('hide');
                                }, 3000);*/
                                self.switchToLevel(5);
                                var data = [{
                                    "memberID": self.$rootScope.memberID,
                                    "currPage": "searchMatchingProfiles",
                                    "userData": {
                                        "offsetVal": {
                                            "isOthers": "false",
                                            "value":  ""
                                        }
                                    }
                                }];
                              
                              var requestJSON ="requestJSON="+JSON.stringify(data); 

                              $.post('UserActionCommand', requestJSON)
                                .success(function (d, status, headers, config) { 
                                    var data = JSON.parse(d);
                                    if(data[0].status === "success"){
                                        self.$rootScope.searchResult = data[0].navPageData; 
                                        self.$rootScope.homeRedirect = true; 
                                        self.$rootScope.isPrefUpdated = true; 
                                        self.$rootScope.percentageComplete = 100;    
                                        $("#reg-succs-modal").modal('hide');                
                                        self.$location.path('/profile');
                                    }
                                    else{
                                        self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                                    }
                                })
                                .error(function(){
                                    console.log("error");
                                });
                            }
                            else{ 
                                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                            }           
                        })
                        .error(function () {
                             //console.log("error");
                             self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                             $("#reg-succs-modal").modal('show');
                        }); 
        }
        else{
            this.partnerMaritalStatusErr = (this.partnerMaritalStatus === "AA_DEFAULT")? "Please select your partner's Marital Status" : "";
            this.partnerProfeesionalErr = (this.uPProfessional === "AA_DEFAULT")? "Please select your partner's Profession" : "";
            this.partnerQualificationErr = (this.uPQualification === "AA_DEFAULT")? "Please select your partner's Qualification" : "";
            this.partnerReligionErr = (this.uPReligion === "AA_DEFAULT")? "Please select your partner's Religion" : "";
            this.partnerMotherTongueErr = (this.uPMotherTongue === "AA_DEFAULT")? "Please select your partner's Mother Tongue" : "";
            this.partnerCommunityErr = (this.uPCommunity === "AA_DEFAULT")? "Please select your partner's Community" : "";
            this.partnerSubCommunityErr = (this.uPSubCommunity === "AA_DEFAULT")? "Please select your partner's Sub Community" : "";
            this.partnerRassiErr = (this.uPRaasi === "AA_DEFAULT")? "Please select your partner's Raasi" : "";
            this.partnerStarErr = (this.uPNakshatra === "AA_DEFAULT")? "Please select your partner's Star" : "";
            this.partnerGothraErr = (this.uPGothra === "AA_DEFAULT")? "Please select your partner's Gothram" : "";    
            this.partnerCountryErr = (this.pCurrentCountry === "AA_DEFAULT")? "Please select your partner's Country" : "";
            this.partnerStateErr  = (this.pCurrentState === "AA_DEFAULT")? "Please select your partner's State" : "";
            this.partnerCityErr  = (this.pCurrentCity  === "AA_DEFAULT")? "Please select your partner's City" : "";
            this.partnerAgeLowerLimErr = (this.ageLL  === "from")? "Please select your partner's Age lower limit" : "";
            this.partnerAgeUpperLimErr = (this.ageUL  === "to")? "Please select your partner's Age upper limit" : "";
            this.partnerIncomeLowerLimErr = (this.uIncomeLL  === "from")? "Please select your partner's Income lower limit" : "";
            this.partnerIncomeUpperLimErr = (this.uIncomeUL  === "to")? "Please select your partner's Income upper limit" : "";
            this.partnerWeightLowerLimErr = (this.weightLL  === "from")? "Please select your partner's Weight lower limit" : "";
            this.partnerWeightUpperLimErr = (this.weightUL  === "to")? "Please select your partner's Weight upper limit" : "";
            this.partnerHeightLowerLimErr = (this.heightLL  === "from")? "Please select your partner's Height lower limit" : "";
            this.partnerHeightUpperLimErr = (this.heightUL  === "to")? "Please select your partner's Height upper limit" : "";
        }       
    },
    formatBasicInfoData : function(formData){
        var tempList = {}, self = this;

        _.each(formData, function(val){
            if(val.name === "religion"){
              tempList[val.name] = {"isOthers" : (self.uReligion ==="Others")? "true" : "false", 
              "value": (self.uReligion==="Others")? self.religionText : val.value}
            }
            else if(val.name === "language"){              
                tempList[val.name] = {"isOthers" : (self.uMotherTongue ==="Others")? "true" : "false", 
                "value": (self.uMotherTongue==="Others")? self.motherTongueText : val.value}               
            }
            else if(val.name === "caste"){              
                tempList[val.name] = {"isOthers" : (self.uCommunity ==="Others")? "true" : "false", 
                "value": (self.uCommunity==="Others")? self.communityListText : val.value}               
            }
            else if(val.name === "subcaste"){              
                tempList[val.name] = {"isOthers" : (self.uSubCommunity ==="Others")? "true" : "false", 
                "value": (self.uSubCommunity==="Others")? self.subCommunityListText : val.value}               
            }
            else if(val.name === "gothram"){              
                tempList[val.name] = {"isOthers" : (self.uGothra ==="Others")? "true" : "false", 
                "value": (self.uGothra==="Others")? self.gothraText : val.value}               
            }else{          
              tempList[val.name] = {"isOthers" : "false", "value": val.value} 
            }
        });

        return tempList;
    },
    formatFamilyInfoData : function(formData){
        var tempList = {}, self = this;

        _.each(formData, function(val){
           tempList[val.name] = {"isOthers" : "false", "value": val.value};
           });

        return tempList;
    },
    formatAstroInfoData : function(formData){
        var tempList = {}, self = this;

        _.each(formData, function(val){
            if(val.name === "raasi"){
              tempList[val.name] = {"isOthers" : (self.uAstroRaasi ==="Others")? "true" : "false", 
              "value": (self.uAstroRaasi==="Others")? self.raasiText : val.value}
            }
            else if(val.name === "star"){              
                tempList[val.name] = {"isOthers" : (self.uNakshatra ==="Others")? "true" : "false", 
                "value": (self.uNakshatra==="Others")? self.nakshatraText : val.value}               
            }
            else if(val.name === "horoscopeLoc"){ 
                var loc = "";
                _.each(self.HoroPics, function(pics){
                    loc = loc + pics.resized.dataURL + "||";
                });
                tempList[val.name] = {"isOthers" : "false", "value": loc} 
            }else{          
              tempList[val.name] = {"isOthers" : "false", "value": val.value} 
            }
        });

        return tempList;

    },
    formatEducationlInfoData : function(formData){
        var tempList = {}, self = this;

        _.each(formData, function(val){
            if(val.name === "education"){
              tempList[val.name] = {"isOthers" : (self.uEducation ==="Others")? "true" : "false", 
              "value": (self.uEducation==="Others")? self.educationText : val.value}
            }
            else if(val.name === "profession"){              
                tempList[val.name] = {"isOthers" : (self.uJobProfile ==="Others")? "true" : "false", 
                "value": (self.uJobProfile==="Others")? self.jobProfileText : val.value}               
            }
            else if(val.name === "annualIncome_LL"){              
                tempList[val.name] = {"isOthers" : "false", "value":self.incomeLL}               
            }
            else if(val.name === "annualIncome_UL"){              
                tempList[val.name] = {"isOthers" : "false", "value":self.incomeUL}               
            }else{          
              tempList[val.name] = {"isOthers" : "false", "value": val.value} 
            }
        });

        return tempList;

    },
    formatLifeStyleData : function(formData){
        var tempList = {}, self = this;

        _.each(formData, function(val){ 
            tempList[val.name] = {"isOthers" : "false", "value": val.value} ;            
        });

        return tempList;
    },
    formatPartnerPrefData : function(formData){
        var tempList = {"locCount" :[{"isOthers" : "false", "value": "1"}] }, self = this;
        var countryCount = 0, stateCount = 0, cityCount = 0;

        _.each(formData, function(val, key){            
            if(val.name.indexOf("professionID")>-1 || val.name.indexOf("QualificationID")>-1 || val.name.indexOf("ReligionID")>-1 || 
                val.name.indexOf("motherTongueID")>-1 || val.name.indexOf("casteID")>-1 || val.name.indexOf("SubCasteID")>-1 || 
                val.name.indexOf("RaasiID")>-1 ||val.name.indexOf("StarID")>-1 || val.name.indexOf("GothramID")>-1){ 
                    
                    if(val.name.match(/^[a-zA-Z]+$/)){
                        if(!tempList[val.name]){
                            tempList[val.name] = [];
                        }
                        tempList[val.name].push(self.checkForIsOThersData(val, key, formData)); 
                    }
            }            
            else if(val.name.indexOf("countryID")>-1){  
                    if(val.value !== "AA_DEFAULT"){ 
                        countryCount++;
                        tempList["countryID"+countryCount] = [{"isOthers" : "false", "value": val.value}] ;
                    }
            }            
            else if(val.name.indexOf("StateID")>-1){ 
                    if(val.value !== "AA_DEFAULT"){  
                        stateCount++;
                        tempList["StateID"+stateCount] = [{"isOthers" : "false", "value": val.value}] ;
                    }
            }            
            else if(val.name.indexOf("CityID")>-1){  
                    if(val.value !== "AA_DEFAULT"){ 
                        cityCount++;
                        tempList["CityID"+cityCount] = [{"isOthers" : "false", "value": val.value}] ;
                    }
            }
            else{
                tempList[val.name] = [{"isOthers" : "false", "value": val.value}] ;
            }
        });
        tempList.locCount[0].value = "" + countryCount.toString() + "";
        //console.log(tempList);
        return tempList;
    },
    checkForIsOThersData : function(val, idx, formData){          
        var value = (val.value === "Others")? formData[idx+1].value : val.value;
        return {"isOthers" : (val.value === "Others")? "true" : "false", "value": value};
    },
    changeUpperLimit : function(id){ 
        var upperSelect = document.querySelectorAll('select[name='+id.split("_")[0]+'_UL]')[0];
        var lowerValue = document.querySelectorAll('select[name='+id.split("_")[0]+'_LL]')[0];

        $(upperSelect).children('option').removeAttr("selected");

        _.each(upperSelect.children, function(opt){
            opt.disabled = (lowerValue.value === 'from' || opt.value==='to' || parseInt(opt.value) >= parseInt(lowerValue.value))? false : true; 
        });

        //console.log(upperSelect, lowerValue);
    },
    alterSelectList : function(id){
        setTimeout(function() {       
            var allSelect = document.querySelectorAll('select[name='+id+']');
                _.each(allSelect, function(a){
                    _.each(allSelect, function(b){
                        if(a !== b){
                            _.each(b.options, function(opt){
                                if(opt.value === a.value){
                                    opt.disabled = true;
                                }
                            });
                        }
                    });
                });
        }, 100);
    },
    updateUserProfilePic : function(){
        var loc = "";
        var self = this;
        _.each(self.profilePics, function(pics){
            loc = loc + pics.resized.dataURL + "||";
        });
        this.$rootScope.ppIndex = this.profIndex;
        loc = loc + "index=" + this.profIndex;

        var tempList  = [{
                        "memberID": this.$rootScope.memberID,
                        "currPage": "uNameAndPic",
                        "userData": {
                            "name": {
                                "isOthers": "false",
                                "value": this.$rootScope.logedUserName
                            },
                            "picStream": {
                                "isOthers": "false",
                                "value": loc
                            }
                        }
                     }];
        this.$rootScope.registrStatus = "Processing..."
        $("#reg-succs-modal").modal('show');
        var requestJSON ="requestJSON="+JSON.stringify(tempList);
        $.post('UserActionCommand', requestJSON)
            .success(function (d, status, headers, config) {
                var data= JSON.parse(d);  
                if(data[0].status === "success"){ 
                    setTimeout(function() {
                        $("#reg-succs-modal").modal('hide');
                    }, 1000); 
                }
                else{ 
                    self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
                }           
            })
            .error(function () { 
                 self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                 $("#reg-succs-modal").modal('show');
            }); 
     } 

};