app.controller('searchResultController', ['$scope','$rootScope','$location','loggedUserService', '$cookieStore', SearchResultController]);

function SearchResultController($scope, $rootScope,$location,loggedUserService, $cookieStore){    
    var self = this;
    this.$rootScope = $rootScope;
    this.$location = $location;
    this.$cookieStore = $cookieStore;
    this.$rootScope.$broadcast("profileBroadCast", true);  
    var uname = $cookieStore.get('IB-uName');
    var memberID = $cookieStore.get('IB-ID'); 
    var ppp = $cookieStore.get("IB-uPWD"); 
    var prefUpdated = $cookieStore.get("IB-prefUpdated");
    $cookieStore.put("isFromSearchRes", true); 
    $rootScope.activeNav='Search';
    $rootScope.isFromSearchRes = $cookieStore.get("isFromSearchRes");
    if(!$rootScope.isPrefUpdated) $rootScope.isPrefUpdated = prefUpdated;
    this.resLoaded = false;
    this.$rootScope.showSubBar = true;
    this.currentUsersMembersInfo = [];
    this.loggedUserService = loggedUserService;
    this.sortSelected = "";
    this.sortSelectedReverse= false;   
    self.married = "any";
    self.relgn = "any";
    self.mtnge = "any";
    self.smkng = "any";
    self.drnkng = "any";
    self.enableLoader = false;
    self.enableNoMoreText = false;
    this.activityList = [
                            {
                                name: "Active Members",
                                mapper: "active_members",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Yes"
                                    },
                                    {
                                        name : "No"
                                    }
                                ]
                            }, 
                            {
                                name: "Profile Type",
                                mapper: "profile_type",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Basic"
                                    },
                                    {
                                        name : "Silver"
                                    },
                                    {
                                        name : "Gold"
                                    },
                                    {
                                        name : "Diamond"
                                    }
                                ]
                            }, 
                            {
                                name: "Recently Joined",
                                mapper: "recently_joined",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Yes"
                                    },
                                    {
                                        name : "No"
                                    }
                                ]
                            }, 
                            {
                                name: "Marital Status",
                                mapper: "marital_status",
                                expand : false,
                                display: true,
                                options: [ 
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Single",
                                        value : "Single"
                                    },
                                    {
                                        name : "Married",
                                        value : "Married",
                                    },
                                    {
                                        name : "Divorced",
                                        value : "Divorced"
                                    },
                                    {
                                        name : "Widow",
                                        value : "Widow"
                                    },
                                    {
                                        name : "Separated",
                                        value : "Separated"
                                    }
                                ]
                            },
                            {
                                name:  "Religion",
                                mapper: "religion",
                                expand : false,
                                display: true,
                                options: [
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Hindu",
                                        value : "Hindu"
                                    },
                                    {
                                        name : "Islam",
                                        value : "Islam"
                                    },
                                    {
                                        name : "Christian",
                                        value : "Christian"
                                    } 
                                ]
                            },
                            {
                                name:  "Community",
                                mapper: "community",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Mudhaliyar"
                                    },
                                    {
                                        name : "Brahmin"
                                    },
                                    {
                                        name : "Nadar"
                                    },
                                    {
                                        name : "Others"
                                    }
                                ]
                            },
                            {
                                name:  "Mother Tongue",
                                mapper: "mother_tongue",
                                display: true,
                                expand : false,
                                options: [
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Tamil",
                                        value : "Tamil"
                                    },
                                    {
                                        name : "Telugu",
                                        value : "Telugu"
                                    },
                                    {
                                        name : "Malayalam",
                                        value : "Malayalam"
                                    },
                                    {
                                        name : "Kannadam",
                                        value : "Kannadam"
                                    },
                                    {
                                        name : "Hindi",
                                        value : "Hindi"
                                    }
                                ]
                            },
                            {
                                name:  "Education",
                                mapper: "education",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Masters",
                                        value : "Masters"
                                    },
                                    {
                                        name : "Bachelors",
                                        value : "Bachelors"
                                    },
                                    {
                                        name : "Diploma",
                                        value : "Diploma"
                                    },
                                    {
                                        name : "Under Graduate",
                                        value : "Under Graduate"
                                    }
                                ]
                            },
                            {
                                name:  "Working As",
                                mapper: "working_with",
                                display: false,
                                expand : false,
                                options: [
                                    {
                                        name : "Teacher"
                                    },
                                    {
                                        name : "Software Professional"
                                    },
                                    {
                                        name : "Banking Professional"
                                    },
                                    {
                                        name : "Others"
                                    }
                                ]
                            }, 
                            {
                                name:  "Smoking",
                                mapper: "smoking",
                                display: true,
                                expand : false,
                                options: [
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Yes",
                                        value : "yes"
                                    },
                                    {
                                        name : "No",
                                        value : "no"
                                    }
                                ]
                            }, 
                            {
                                name:  "Drinking",
                                mapper: "smoking",
                                display: true,
                                expand : false,
                                options: [
                                    {
                                        name : "Any",
                                        value : "any"
                                    },
                                    {
                                        name : "Yes",
                                        value : "Yes"
                                    },
                                    {
                                        name : "No",
                                        value : "No"
                                    }
                                ]
                            }
                        ]; 
    //this.$rootScope.memberID = "IBM10000004";
    this.currentMemberProfileInfo = {};    
        
        if(!self.$rootScope.msearchResult){
            this.resLoaded = false;
            if(!self.$rootScope.memberID) self.$rootScope.memberID = memberID;  
            if(self.$rootScope.memberID) {
                this.loggedUserService.getCurrentMemberProfileInfo(this.$rootScope.memberID).then(function(resp){
                   var data= JSON.parse(resp);          
                   
                    var msearchResult = data[0].navPageData;             
                    self.currentMemberProfileInfo["member_count"] = msearchResult.totalProfiles;           
                    _.each(msearchResult.matchedprofData, function(val, key){           
                        self.pushMemberData(msearchResult, val, key);
                    });                         
                    self.resLoaded = true; 
                    self.$rootScope.msearchResult = self.currentMemberProfileInfo;             
                });
            }
            else{
                 $location.path("/home");
            } 
        }
        else{
            self.currentMemberProfileInfo = self.$rootScope.msearchResult;
            this.resLoaded = true; 
        }
}

SearchResultController.prototype = {  

    pushMemberData : function(msearchResult, val, key){
        var imgList=[], imgPos=0, self = this;
            if(msearchResult.matchedprofData[key].userPics!==""){
                imgList = self.getProfilePics(msearchResult.matchedprofData[key].userPics.split('index=')[0]);
                imgPos = msearchResult.matchedprofData[key].userPics.split('index=')[1];
            }

            var obj = {
                "name" : val.fullName,
                "img" :  imgList,
                "imgIdx" : parseInt(imgPos),
                "createdBY" : "Self",
                "age" : msearchResult.matchedPrefData[key].age.currMatchVal,
                "height" : msearchResult.matchedPrefData[key].height.currMatchVal,
                "weight" : msearchResult.matchedPrefData[key].weight.currMatchVal,
                "married" : msearchResult.matchedPrefData[key].relationShip.currMatchVal,
                "religion" : msearchResult.matchedPrefData[key].religion.currMatchVal,
                "motherTongue" : msearchResult.matchedPrefData[key].language.currMatchVal,
                "community" : msearchResult.matchedPrefData[key].caste.currMatchVal,
                "food" : msearchResult.matchedPrefData[key].foodType.currMatchVal,
                "education" : msearchResult.matchedPrefData[key].qualification.currMatchVal,
                "work" : msearchResult.matchedPrefData[key].profession.currMatchVal,
                "location" : msearchResult.matchedPrefData[key].country.currMatchVal + " / " + msearchResult.matchedPrefData[key].state.currMatchVal + " / " + msearchResult.matchedPrefData[key].city.currMatchVal ,
                "about" : msearchResult.matchedprofData[key].description,
                "lifestle" :{"drinker" : msearchResult.matchedPrefData[key].drinker.currMatchVal, "smoker" : msearchResult.matchedPrefData[key].smoker.currMatchVal},
                "family" : {"familyValue" : msearchResult.matchedprofData[key].FamilyValue, "familyStatus" : msearchResult.matchedprofData[key].FamilyStatus, "familyType" : msearchResult.matchedprofData[key].FamilyTypeID},
                "Religion" : {
                        "religion" : msearchResult.matchedPrefData[key].religion.currMatchVal, 
                        "caste" : msearchResult.matchedPrefData[key].caste.currMatchVal
                },
                "education&career" : {
                                    "job" : msearchResult.matchedPrefData[key].profession.currMatchVal, 
                                    "degree" : msearchResult.matchedPrefData[key].qualification.currMatchVal, 
                                    "salary" : "Earns INR " + msearchResult.matchedPrefData[key].income.currMatchVal.split(' - ')[0] + " lakh to " + msearchResult.matchedPrefData[key].income.currMatchVal.split(' - ')[1] + " lakh annually"
                                },
                "horoscope" :  {
                        "raasi" : msearchResult.matchedPrefData[key].raasi.currMatchVal, 
                        "star" : msearchResult.matchedPrefData[key].star.currMatchVal, 
                        "dosham"  : msearchResult.matchedPrefData[key].dosham.currMatchVal, 
                        "pics" : self.getHoroScopePics(msearchResult.matchedprofData[key].horoPics)
                },
                "memberID" : val.memberID,
                "matchedValues" : msearchResult.matchedPrefData[key]
            }; 
            self.currentMemberProfileInfo[val.memberID] = obj; 
            self.loggedUserService.usersMembersList[val.memberID] = obj; 
    },
    getMoreMembers : function(){
        var self = this;    
        self.enableLoader = true;   
        self.enableNoMoreText = false;
        this.loggedUserService.getCurrentMemberProfileInfo(this.$rootScope.memberID).then(function(resp){
            var data = JSON.parse(resp);          
            self.enableLoader = false;
            self.enableNoMoreText = (data[0].navPageData.totalProfiles === "0");
            if(data[0].navPageData.totalProfiles !== "0"){                
                var msearchResult = data[0].navPageData;             
                self.currentMemberProfileInfo["member_count"] = msearchResult.totalProfiles;           
                _.each(msearchResult.matchedprofData, function(val, key){           
                    self.pushMemberData(msearchResult, val, key);
                });   
                self.$rootScope.msearchResult = self.currentMemberProfileInfo;
            }else{
                self.enableLoader = false; 
            }         
        });
            
    },
    getHoroScopePics : function(path){
        var templist = [];    
        var images = path.split('||');
        _.each(images, function(img){
            templist.push(img.split(" ").join("+"));
        });

        return templist;
    },

    setCurrentMember : function(memberID){
        this.$rootScope.searchMemberID = memberID;
        this.$location.path("/member-info");
        //console.log(" From src " +this.$rootScope.memberID);
    },
    getProfilePics : function(picData){
        var templist = [];
        var images = picData.split("||");
        _.each(images, function(img){
            if(img!=="") templist.push({url : img.split(" ").join("+"), resized : {dataURL : img.split(" ").join("+")}});
        });

        return templist;
    },
        closePrevious : function(member){
        _.each(this.currentMemberProfileInfo, function(members){
            members.showConnect = (members === member)? member.showConnect : false;
            members.sendSMS = false;
            members.showNumber = false;
            members.showHoro = false;
        });
    },
    closeSMSPrevious : function(member){
        _.each(this.currentMemberProfileInfo, function(members){
            members.sendSMS = (members === member)? member.sendSMS : false;
            members.showConnect = false;
            members.showHoro = false;
            members.showNumber = false;
        });
    },
    closePreviousNumber : function(member){
        _.each(this.currentMemberProfileInfo, function(members){
            members.showNumber = (members === member)? member.showNumber : false;
            members.showConnect = false;
            members.sendSMS = false;
            members.showHoro = false;
        });
    },
    closePreviousHoro : function(member){
        _.each(this.currentMemberProfileInfo, function(members){
            members.showHoro = (members === member)? member.showHoro : false;
            members.showConnect = false;
            members.sendSMS = false;
            members.showNumber = false;
        });
    },
    removeMemberFromList : function(member){
        return !confirm('Are you sure you want to remove the member from the list forever?');
    },
    sortArray : function(cond){        
        this.sortSelectedReverse = (cond === "");  
    },
    scrollToCurrentButton : function(event,showConnect){
      /* if(showConnect){            
           $('html,body').animate({
            scrollTop: ($("#"+event.currentTarget.id).offset().top-150)},
            'slow');

       }    */
   },
   updateCount : function(resp){
    var count = 0;
    _.each(resp, function(){
        count++;
    })
    this.currentMemberProfileInfo.member_count = count;
   },
   pointMe : function(event, expand){
        if(expand)setTimeout(function() {
            $("html,body").animate({'scrollTop': event.layerY},1000);
        });
   },
   scrollTop :function(){
        setTimeout(function() {
            $("html,body").animate({'scrollTop':$(".result-view").position().top},1000);
        });
   },
   showPremiumFeature : function(){  
    this.$rootScope.registrStatus = "Membership Feature on its way.."
    $("#reg-succs-modal").modal('show');
   }
};

app.filter('myFilter', function () {
return function (input, filterKey, filterVal) {
    var filteredInput ={};
    var count = 0;
     angular.forEach(input, function(value, key){
       if(key !== "member_count")if((value[filterKey] && value[filterKey].trim() === filterVal) || filterVal==="any" ){
          filteredInput[key]= value;
          count++;
        }
     });
     //input["member_count"] = count;
     return filteredInput; 
}});

app.filter('orderObjectBy', function(){
 return function(input, attribute, isReverse) {
    if (!angular.isObject(input)) return input;

    
    var array = [];
    for(var objectKey in input) {
      if (typeof(input[objectKey])  === "object" && objectKey.charAt(0) !== "$"){
        input.memberID = objectKey;
        array.push(input[objectKey]);
      }
    }

    array.sort(function(a, b){
        a = parseInt(a[attribute]);
        b = parseInt(b[attribute]);
        return a - b;
    });

    return (isReverse)? array.reverse() : array;
 }
});