app.controller('memberInfoController', ['$rootScope', '$location', '$timeout', 'loggedUserService', '$cookieStore', MemberInfoController]);

function MemberInfoController($rootScope, $location, $timeout, loggedUserService, $cookieStore){
    
    var self = this;
    this.loggedUserService = loggedUserService;
    this.$rootScope = $rootScope;
    this.$timeout = $timeout;
    this.currentThumbnailIndex = 0;
    this.currentThumbnailModelIndex = 0;
    this.$rootScope.$broadcast("homeBroadCast", false);
    this.$rootScope.$broadcast("profileBroadCast", true);  
    $rootScope.isFromSearchRes = $cookieStore.get("isFromSearchRes");
    this.epandView = true;

    this.$timeout(function(){        
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});
    });

    this.activeCount = 0;
    this.rowCount = 0;
    if(!this.$rootScope.memberID) {
     if($rootScope.isFromSearchRes) $location.path("/search");
     if(!$rootScope.isFromSearchRes) $location.path("/profile");
    }
    else{
        //this.$rootScope.memberID = "IBM10000001";
        // getting dummy/sample member profile info     
        
        /*if(!self.loggedUserService.usersMembersList) this.loggedUserService.getCurrentMemberProfileInfo(this.$rootScope.memberID).then(function(resp){
           self.currentMemberProfileInfo = resp.data;
           self.currentMemberProfileInfoModel = angular.copy(resp.data);
           self.loggedUserService.usersMembersList =  resp.data;
        }); */

        var currMem = self.loggedUserService.usersMembersList;
        this.currentMemberProfileInfo = currMem;
        this.currentThumbnailIndex = this.currentThumbnailModelIndex = self.currentMemberProfileInfo[self.$rootScope.searchMemberID].imgIdx;
        this.currentMemberProfileInfoModel = currMem; //angular.copy(self.currentMemberProfileInfo);
        //console.log(" From MIC " + this.$rootScope.memberID);
        if(this.loggedUserService.usersMembersList[this.$rootScope.searchMemberID].matchedValues.length===0){
            this.loadMembersMatchedData();
        }
    }
}

MemberInfoController.prototype = { 
    shortlistAProfile : function(){
        var self = this, data = [{
                    "memberID": this.$rootScope.memberID,
                    "currPage": "shortlistAProfile",
                    "userData": {
                        "partMemID": {
                            "isOthers": "false",
                            "value": this.$rootScope.searchMemberID
                        }
                    }}];

        var requestJSON ="requestJSON="+JSON.stringify(data); 
        self.$rootScope.registrStatus = "Processing..." ;
        $("#reg-succs-modal").modal('show');

        $.post('UserActionCommand', requestJSON)
        .success(function (d, status, headers, config) { 
            var data = JSON.parse(d);
            if(data[0].status === "success"){
                self.$rootScope.registrStatus = "Profile ShortListed Successfully..." ; 
                self.$rootScope.currentUserProfileInfo.shortLisetedByYou 

                var obj = {
                           "name" :  self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].name.split(" ")[0],
                           "img" :  self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].img,
                           "imgIdx" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].imgIdx,
                           "age" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].age,
                           "height" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].height,
                           "caste": self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].community,
                           "memberId" : self.$rootScope.searchMemberID,
                           "married" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].married,
                            "horoscope" :  {
                                    "raasi" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.raasi, 
                                    "star" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.star, 
                                    "dosham"  : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.dosham,
                                    "pics" : self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.pics
                            }
                        };
                        self.$rootScope.currentUserProfileInfo.shortLisetedByYou.profiles.push(obj);
            }
            else{
                self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime.";                
            }
        })
        .error(function(){
            console.log("error"); 
        });
    },
    sendInterestAProfile : function(){
        var self = this, data = [{
                    "memberID": this.$rootScope.memberID,
                    "currPage": "sendARequest",
                    "userData": {
                        "partMemID": {
                            "isOthers": "false",
                            "value": this.$rootScope.searchMemberID
                        }
                    }}];

        var requestJSON ="requestJSON="+JSON.stringify(data); 
        self.$rootScope.registrStatus = "Processing..." ;
        $("#reg-succs-modal").modal('show');

        $.post('UserActionCommand', requestJSON)
        .success(function (d, status, headers, config) { 
            var data = JSON.parse(d);
            if(data[0].status === "success"){
                self.$rootScope.registrStatus = "Profile Interest sent Successfully..." ; 
            }
            else{
                self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime.";                
            }
        })
        .error(function(){
            console.log("error"); 
        });
    },
    loadMembersMatchedData : function(){
        this.showLoader = true;      
        var self = this, data = [{
                    "memberID": this.$rootScope.memberID,
                    "currPage": "analyseCurrProfile",
                    "userData": {
                        "partMemID": {
                            "isOthers": "false",
                            "value": this.$rootScope.searchMemberID
                        }
                    }}];

        var requestJSON ="requestJSON="+JSON.stringify(data);
        var config = {
               headers : {
                  'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
              }
        };
        $.post('UserActionCommand', requestJSON)
        .success(function (d, status, headers, config) { 
            var data= JSON.parse(d)[0].navPageData;
            var key = "match0";
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].motherTongue = data.matchedPrefData[key].language.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].community = data.matchedPrefData[key].caste.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].food = data.matchedPrefData[key].foodType.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].education = data.matchedPrefData[key].qualification.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].location = data.matchedPrefData[key].country.currMatchVal + " / " 
                                                                + data.matchedPrefData[key].state.currMatchVal + " / " 
                                                                + data.matchedPrefData[key].city.currMatchVal;
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].about = data.matchedprofData[key].description; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].lifestle.drinker = data.matchedPrefData[key].drinker.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].lifestle.smoker = data.matchedPrefData[key].smoker.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].family.familyValue = data.matchedprofData[key].FamilyValue; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].family.familyStatus = data.matchedprofData[key].FamilyStatus; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].family.fatherStatus = data.matchedprofData[key].FatherStatus; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].Religion.caste = data.matchedPrefData[key].caste.currMatchVal; 
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID]["education&career"].degree = data.matchedPrefData[key].qualification.currMatchVal;            
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.dosham = data.matchedPrefData[key].dosham.currMatchVal;
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].horoscope.pics = self.getHoroScopePics(data.matchedprofData[key].horoPics.split('||'));
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].matchedValues = data.matchedPrefData[key];
            self.loggedUserService.usersMembersList[self.$rootScope.searchMemberID].matchedProfileExpecs = data.matchedprofData[key].matchedProfileExpecs;
            self.showLoader = false;
        })
        .error(function () {
          console.log("error"); 
        });
    },
    getHoroScopePics : function(images){
        var templist = [];     
        _.each(images, function(img){
            templist.push(img.split(" ").join("+"));
        });

        return templist;
    },
    changeCurrentProfImage : function(index){
        this.currentThumbnailIndex = index;
        this.currentThumbnailModelIndex = index;

        _.each(this.currentMemberProfileInfo[this.$rootScope.searchMemberID]['img'], function(v,k){
            v.isActive = (k===index)? true : false;
        });
         _.each(this.currentMemberProfileInfoModel[this.$rootScope.searchMemberID]['img'], function(v,k){
            v.isActive = (k===index)? true : false;
        });
    },
    changeCurrentModelProfImage : function(index){
        this.currentThumbnailModelIndex = index;

        _.each(this.currentMemberProfileInfoModel[this.$rootScope.searchMemberID]['img'], function(v,k){
            v.isActive = (k===index)? true : false;
        });
    },
    movePrevious : function(position){  

        if(position==="left"){
            if(this.currentThumbnailModelIndex>0){
                this.currentThumbnailModelIndex--;
            }
        }
        if(position==="right"){
            if(this.currentThumbnailModelIndex<this.currentMemberProfileInfoModel[this.$rootScope.searchMemberID]['img'].length){
                this.currentThumbnailModelIndex++;
            }
        }

        this.changeCurrentModelProfImage(this.currentThumbnailModelIndex);
    },
    getActiveCount : function(isMactched){
        this.rowCount = this.rowCount+1;
        this.activeCount = (isMactched==="true")? (this.activeCount+1) : this.activeCount;
    }
};