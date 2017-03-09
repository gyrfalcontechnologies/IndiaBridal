app.controller('userProfileController', ['$rootScope', '$timeout', 'loggedUserService', '$location', '$cookieStore', UserProfileController]);

function UserProfileController($rootScope, $timeout, loggedUserService, $location, $cookieStore){
    
    var self = this;
    this.loggedUserService = loggedUserService;
    this.$rootScope = $rootScope;
    this.$timeout = $timeout;
    this.$location = $location;
    var uname = $cookieStore.get('IB-uName');
    var memberID = $cookieStore.get('IB-ID'); 
    var ppp = $cookieStore.get("IB-uPWD");
    $rootScope.activeNav=''; 
    $cookieStore.put("isFromSearchRes", false); 
    $rootScope.isFromSearchRes = $cookieStore.get("isFromSearchRes");
    if(memberID === "" && uname==="") {
        $location.path("/");
    }
    else{
        this.$rootScope.memberID = memberID;
            this.$rootScope.showSubBar = true;
            this.$rootScope.$broadcast("homeBroadCast", false);
            this.$rootScope.$broadcast("profileBroadCast", true);
            this.slideCount = 0; 
            $( window ).scroll(function(event) {
               // self.$rootScope.$broadcast("minimizeBanner", event);
               self.minTopBanner(event);
            });

            $('#login-modal').modal('hide');

            this.$timeout(function(){        
                $("html,body").animate({'scrollTop':$(".top-banner").position().top});
            });
             this.activityList = [
                                    {
                                        name: "Invitations",
                                        mapper: "invitations",
                                        isActive: true
                                    }, 
                                    {
                                        name: "Request Sent",
                                        mapper: "requestSent",
                                        isActive: false
                                    }, 
                                    {
                                        name: "Request Approved",
                                        mapper: "requestApproved",
                                        isActive: false
                                    }, 
                                    {
                                        name: "Approval Pending",
                                        mapper: "approvalPending",
                                        isActive: false
                                    },
                                    {
                                        name:  "Message Recieved",
                                        mapper: "recievedMessages",
                                        isActive: false
                                    },
                                    {
                                        name:  "Message Sent",
                                        mapper: "sentMessages",
                                        isActive: false
                                    }
                                ];

            this.showLoader = true;
            //this.currentInboxItem = [];
            // getting dummy/sample user profile info
           
            this.loggedUserService.getCurrentUserProfileInfo().then(function(resp){ 
                  //self.currentUserProfileInfo = resp.data; 
                  /*_.each(self.activityList, function(a){
                        _.each(self.currentUserProfileInfo[a.mapper], function(val){
                            self.currentInboxItem.push(val);
                        });
                  }); */
                    if(self.$rootScope.searchResult){
                        if(!self.$rootScope.currentUserProfileInfo) {
                            self.getMatchingProfileInfos(resp.data, self.$rootScope.searchResult);   
                        }
                        else{
                            self.currentUserProfileInfo = self.$rootScope.currentUserProfileInfo;
                            self.setInvitations();
                        }                   
                    } else{
                        self.getInitialData(resp.data, uname, ppp);
                    }
                });
            
            this.currentUserProfPic = (this.$rootScope.profilePics && this.$rootScope.profilePics.length>0)? this.$rootScope.profilePics.slice().reverse()[this.$rootScope.ppIndex].resized.dataURL : ""; 
            if(!this.currentUserProfileInfo) this.currentUserProfileInfo = [];
            this.currentUserProfileInfo["name"] = this.$rootScope.logedUserName;
            this.currentUserProfileInfo["progress"] = this.$rootScope.percentageComplete;

            this.currentMsgItem = [];
            this.showInboxContent = false;

            this.individualQueryReq = {getFeaturedProfiles : "featuredProf",getBestMatchedProfiles : "bestMatches",
                                        getShortlistedProfiles : "shortlistedProf" , getProfilesShortlistedYou:"shortLisetedYou"};

            this.featCount = 0;
            this.bestMatchCount = 0;
            this.shortlistedProfCount = 0;
            this.shortlistedProfYCount = 0;
    }
}

UserProfileController.prototype = { 
    getInitialData : function(resp, uname, pwd){
        /*var self = this;
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
                self.getMatchingProfileInfos(resp, self.$rootScope.searchResult); 
            }
            else{
                self.$rootScope.registrStatus = "Ooops!! Something went wrong. Please try again later";
            }
        })
        .error(function(){
            console.log("error");
        });*/
        this.$rootScope.$broadcast("re-enter", uname, pwd, false);
        this.$location.path('/fetch');
         
    },
    setInvitations : function(){
        var self = this;       
        self.showLoader = false;
           self.currentInboxItem = (!self.currentInboxItem)? 
                        this.$rootScope.currentUserProfileInfo["invitations"] : self.currentInboxItem;
           self.$rootScope.activeNav = (self.$rootScope.activeNav.indexOf('Index')>-1)? 'Inbox > Invitations' : self.$rootScope.activeNav;
           self.currentInboxItemLength = self.currentInboxItem.length;
    },
    minTopBanner : function(event){
      var scrollDiff = event.originalEvent.currentTarget.scrollY; 
      $(".top-banner-view").removeClass("animate-height");
      $(".top-banner").removeClass("animate-height");
      if(scrollDiff>0){
        $(".top-banner").addClass("min-banner-top-banner");
        $(".logo img").addClass("min-banner-logo");
        $(".app-download").addClass("min-banner-app-download");
        $(".top-banner-menu").addClass("min-top-banner-menu");
        $(".profile-nav").css("margin-top","56px");        
        $(".profile-nav").css("height","25px");      
        $(".profile-options span").css("padding","3px");
        $(".profile-options span img").css("width", "28px");
        $(".search-member").css("margin-top","12px");
      }else{
        $(".top-banner").removeClass("min-banner-top-banner");
        $(".logo img").removeClass("min-banner-logo");
        $(".app-download").removeClass("min-banner-app-download");
        $(".top-banner-menu").removeClass("min-top-banner-menu"); 
        $(".profile-nav").css("margin-top","80px");        
        $(".profile-nav").css("height","40px");          
        $(".profile-options span").css("padding","10px");
        $(".profile-options span img").css("width", "45px");
        $(".search-member").css("margin-top","28px");
      } 
    },
    scrollProfileList : function(flag, currentClass){
        var element =  $("."+currentClass);
        var isLastElement = false;

       
        if(flag){ 
            
            if(this.prevClass === currentClass || !this.prevClass){
                this.slideCount = this.slideCount + element[0].offsetWidth;
                 if((element[0].scrollWidth - this.slideCount) < 250){
                    isLastElement = true;
                }
            }else{
                this.slideCount = element[0].offsetWidth;
            }
            if (this.slideCount < element[0].scrollWidth) {
                if(isLastElement){
                    $("."+currentClass).animate({'scrollLeft':(element[0].scrollWidth - this.slideCount)+this.slideCount},2000);
                }
                else{                    
                    $("."+currentClass).animate({'scrollLeft':element[0].offsetWidth},2000);  
                }
            }else{
                this.slideCount = this.slideCount - element[0].offsetWidth;
            } 
        }
        else{          
            
            if(this.prevClass === currentClass || !this.prevClass){
                this.slideCount = this.slideCount - element[0].offsetWidth;
            }else{
                this.slideCount = 0;
            }
            if (this.slideCount  >=0) {
                $("."+currentClass).animate({'scrollLeft':-element[0].offsetWidth},2000);
            }else{
                this.slideCount = this.slideCount + element[0].offsetWidth;
            } 
        }
        this.prevClass = currentClass;
    },
    setCurrentMember : function(memberID){
        this.$rootScope.searchMemberID = memberID;
        //console.log(" From UPC " +this.$rootScope.memberID);
    },
    setInboxMessageItem : function(item){
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});
        this.$rootScope.activeNav = "Inbox > " + item.name;
        _.each(this.activityList, function(list){
            list.isActive = (list.name===item.name)? true : false;
        });

        this.currentInboxItem = this.currentUserProfileInfo[item.mapper]; 
        this.currentInboxItemLength = this.currentInboxItem.length;        
        this.showHideList([], true, false);

    },
    enableDisable : function(isAllChecked){
        var elem = document.querySelectorAll('#inner-check');

        _.each(elem, function(chkbx){
            chkbx.checked = isAllChecked;
        });
    },
    changeReadStatus : function(isRead){
        var self=this,
            elem = document.querySelectorAll('#inner-check');

        _.each(elem, function(chkbx){
            if(chkbx.checked){
                _.each(self.currentInboxItem, function(item){
                    item.isRead = (chkbx.className===item.memberId)?  isRead :item.isRead;
                });
            }
        });
    },
    checkLength : function(){
        var count = 0,
            elem = document.querySelectorAll('#inner-check');
        _.each(elem, function(chkbx){
            count = (chkbx.checked)? (count+1) : count;
        });

        this.isAllChecked = (count === this.currentInboxItem.length)? true : false;
    },
    deleteMessage : function(){
        var self=this,
            elem = document.querySelectorAll('#inner-check'); 
        _.each(elem, function(chkbx){
            if(chkbx.checked){
                _.each(self.currentInboxItem, function(item, index){
                     if(chkbx.className===item.memberId){
                        item.showMail = false;
                        self.currentInboxItemLength--
                     }
                });
            }
        }); 
    },
    showHideList : function(currentItem, showList, showCont){
        _.each(this.currentInboxItem, function(item){
            item.showMail = showList;
            item.isRead = (currentItem === item)? true : item.isRead;
        });
        this.isAllChecked = false;
        this.currentMsgItem = currentItem;
        this.showInboxContent = showCont;
    },
    deactivateActiveList : function(){
        _.each(this.activityList, function(list, index){
            list.isActive = (index===0)? true : false;
        });
    },
    getMatchingProfileInfos : function(jsonData, respData){
        var self = this;
        /*var data =[{"memberID": this.$rootScope.memberID,"currPage": "getFeaturedProfiles",  "userData":{"": {"isOthers": "false","value": ""}}}];
          
        var requestJSON ="requestJSON="+JSON.stringify(data);
        var config = {
               headers : {
                  'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
              }
        }*/
        var resultSet = [
                            {"act":"featured", "resp" : "featuredProf"},
                            {"act":"bestMatches", "resp" : "bestMatches"},
                            {"act":"newMatches", "resp" : "newMatches"},
                            {"act":"recentVisitors", "resp" : "recentVisitors"},
                            {"act":"shortLisetedYou", "resp" : "profShortlistedYou"},
                            {"act":"shortLisetedByYou", "resp" : "shortlistedProf"},
                        ];
       /* $.post('UserActionCommand', requestJSON)
        .success(function (d, status, headers, config) { 
            var data= JSON.parse(d);*/
            self.currentUserProfileInfo = jsonData;
            self.currentUserProfileInfo.name = self.$rootScope.logedUserName;
            self.currentUserProfileInfo.progress = self.$rootScope.percentageComplete;
            //if(data[0].status === "success"){ 
                self.$rootScope.searchResult = respData;            
                _.each(resultSet, function(sresp){
                    self.currentUserProfileInfo[sresp.act].profiles = [];
                    if(self.$rootScope.searchResult && self.$rootScope.searchResult[sresp.resp])_.each(self.$rootScope.searchResult[sresp.resp].matchedBasicProfData, function(val, key){
                        var imgList=[], imgPos=0;
                        if(self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].userPics!==""){
                            imgList = self.getProfilePics(self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].userPics.split('index=')[0]);
                            imgPos = self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].userPics.split('index=')[1];
                        }
                        var obj = {
                           "name" : val.fullName.split(" ")[0],
                           "img" :  imgList,
                           "imgIdx" : parseInt(imgPos),
                           "age" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].age,
                           "height" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].height,
                           "caste": self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].caste,
                           "memberId" : val.memberID,
                           "married" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].relationShip,
                            "horoscope" :  {
                                    "raasi" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].raasi, 
                                    "star" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].star, 
                                    "dosham"  : "",
                                    "pics" : ""
                            }
                        };
                        self.currentUserProfileInfo[sresp.act].profiles.push(obj);
                        var obj = {
                            "name" : val.fullName,
                            "img" :  imgList,
                            "imgIdx" : parseInt(imgPos),
                            "createdBY" : "Self",
                            "age" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].age,
                            "height" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].height,
                            "weight" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].weight,
                            "married" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].relationShip,
                            "religion" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].religion,
                            "motherTongue" : "",
                            "community" : "",
                            "food" : "",
                            "education" : "",
                            "matchedProfileExpecs" : "",
                            "work" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].profession,
                            "location" : "" ,
                            "about" : "",
                            "lifestle" :{"drinker" : "", "smoker" : ""},
                            "family" : {"familyValue" : "", "familyStatus" : "", "fatherStatus" : ""},
                            "Religion" : {
                                    "religion" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].religion, 
                                    "caste" : ""
                            },
                            "education&career" : {
                                                "job" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].profession, 
                                                "degree" : "",
                                                "salary" : "earns INR " + self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].income.split(' - ')[0] + " lakh to " + self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].income.split(' - ')[1] + " lakh annually"
                                            },
                            "horoscope" :  {
                                    "raasi" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].raasi, 
                                    "star" : self.$rootScope.searchResult[sresp.resp].matchedBasicProfData[key].star, 
                                    "dosham"  : "",
                                    "pics" : ""
                            },
                            "matchedValues" : []
                        };
                        self.loggedUserService.usersMembersList[val.memberID] = obj;
                        /*var obj = {
                           "name" : val.fullName,
                           "age" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].age.currMatchVal,
                           "height" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].height.currMatchVal,
                           "caste": self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].caste.currMatchVal,
                           "memberId" : val.memberID
                        };
                        self.currentUserProfileInfo[sresp.act].profiles.push(obj);
                        var imgList=[], imgPos=0;
                        if(self.$rootScope.searchResult[sresp.resp].matchedprofData[key].userPics!==""){
                            imgList = self.getProfilePics(self.$rootScope.searchResult[sresp.resp].matchedprofData[key].userPics.split('index=')[0]);
                            imgPos = self.$rootScope.searchResult[sresp.resp].matchedprofData[key].userPics.split('index=')[1];
                        }
                        var obj = {
                            "name" : val.fullName,
                            "img" :  imgList,
                            "imgIdx" : parseInt(imgPos),
                            "createdBY" : "Self",
                            "age" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].age.currMatchVal,
                            "height" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].height.currMatchVal,
                            "weight" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].weight.currMatchVal,
                            "married" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].relationShip.currMatchVal,
                            "religion" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].religion.currMatchVal,
                            "motherTongue" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].language.currMatchVal,
                            "community" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].caste.currMatchVal,
                            "food" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].foodType.currMatchVal,
                            "education" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].qualification.currMatchVal,
                            "work" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].profession.currMatchVal,
                            "location" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].country.currMatchVal + " / " + self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].state.currMatchVal + " / " + self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].city.currMatchVal ,
                            "about" : self.$rootScope.searchResult[sresp.resp].matchedprofData[key].description,
                            "lifestle" :{"drinker" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].drinker.currMatchVal, "smoker" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].smoker.currMatchVal},
                            "family" : {"familyValue" : self.$rootScope.searchResult[sresp.resp].matchedprofData[key].FamilyValue, "familyStatus" : self.$rootScope.searchResult[sresp.resp].matchedprofData[key].FamilyStatus, "familyType" : self.$rootScope.searchResult[sresp.resp].matchedprofData[key].FamilyTypeID},
                            "Religion" : {
                                    "religion" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].religion.currMatchVal, 
                                    "caste" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].caste.currMatchVal
                            },
                            "education&career" : {
                                                "job" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].profession.currMatchVal, 
                                                "degree" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].qualification.currMatchVal, 
                                                "salary" : "Earns INR " + self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].income.currMatchVal.split(' - ')[0] + " lakh to " + self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].income.currMatchVal.split(' - ')[1] + " lakh annually"
                                            },
                            "horoscope" :  {
                                    "raasi" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].raasi.currMatchVal, 
                                    "star" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].star.currMatchVal, 
                                    "dosham"  : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key].dosham.currMatchVal, 
                                    "pics" : self.getHoroScopePics(sresp.resp, key)
                            },
                            "matchedValues" : self.$rootScope.searchResult[sresp.resp].matchedPrefData[key]
                        };
                        self.loggedUserService.usersMembersList[val.memberID] = obj;*/
                    });
                });
                self.showLoader = false;
                self.$rootScope.currentUserProfileInfo = self.currentUserProfileInfo;
                self.setInvitations();
            /*}
            else{
                alert("Something went wrong.. Please Try again after sometime");
            }*/
      /*})
      .error(function () {
          console.log("error"); 
      });*/
  
  },
    getProfilePics : function(picData){
        var templist = [];
        var images = picData.split("||");
        _.each(images, function(img){
            if(img!=="") templist.push({url : img.split(" ").join("+"), resized : {dataURL : img.split(" ").join("+")}});
        });

        return templist;
    },
  getHoroScopePics : function(sresp, key){
    var templist = [];    
    var images = this.$rootScope.searchResult[sresp].matchedprofData[key].horoPics.split('||');
    _.each(images, function(img){
        templist.push(img.split(" ").join("+"));
    });

    return templist;
  },
  setPositionOfHover : function(event, memberID){
        var divPos = document.getElementById(event.currentTarget.parentNode.id).getBoundingClientRect();

        console.log(divPos);
        $('.member-popover').css("left", divPos.left-115);
        //$('.member-popover').css("top", divPos.top-180);

  },
  getMoreFeaturedProf : function(){
    var self = this;
    this.featCount += 5;
    this.showFeaturedLoader = true;    
    this.featuredEndText = false;
    var data = [{
            "memberID": this.$rootScope.memberID,
            "currPage": "getFeaturedProfiles",
            "userData": {
                "offsetVal": {
                    "isOthers": "false",
                    "value": this.featCount.toString()
                }
            }
        }];
      
      var requestJSON ="requestJSON="+JSON.stringify(data); 

      $.post('UserActionCommand', requestJSON)
      .success(function (d, status, headers, config) { 
        var data = JSON.parse(d);
        if(data[0].status === "success"){
            if(data[0].navPageData.featuredProf.totalProfiles !== "0"){ 
                setTimeout(function() { 
                    self.featuredEndText = false;
                    self.showFeaturedLoader = false;                    
                    self.scrollProfileList(false, 'featured-user-list');            
                    self.scrollProfileList(true, 'featured-user-list');
                }, 1000);
                self.getMatchingProfileInfos(self.$rootScope.currentUserProfileInfo, data);                
            }
            else{                         
                setTimeout(function() {
                    self.featuredEndText = true;  
                    self.showFeaturedLoader = false;                    
                    self.scrollProfileList(false, 'featured-user-list');                 
                    self.scrollProfileList(true, 'featured-user-list');
                }, 1000);
            }
        }
        else{
            self.featCount = 0;
            self.featuredEndText = false;
            self.showFeaturedLoader = false;
            alert("Oops!.. Something Went Wrong.. Please Try again Later");
        }
        console.log(" " + data);
      }).error(function(){

      });
  },
  getMoreBMProf : function(){
    var self = this;
    this.bestMatchCount += 5;
    this.shownBestMatchLoader = true;    
    this.bestMatchEndText = false;
    var data = [{
            "memberID": this.$rootScope.memberID,
            "currPage": "getBestMatchedProfiles",
            "userData": {
                "offsetVal": {
                    "isOthers": "false",
                    "value": this.bestMatchCount.toString()
                }
            }
        }];
      
      var requestJSON ="requestJSON="+JSON.stringify(data); 

      $.post('UserActionCommand', requestJSON)
      .success(function (d, status, headers, config) { 
        var data = JSON.parse(d);
        if(data[0].status === "success"){
            if(data[0].navPageData.bestMatches.totalProfiles !== "0"){                          
                setTimeout(function() { 
                    self.bestMatchEndText = false;
                    self.shownBestMatchLoader = false;                  
                    self.scrollProfileList(false, 'bm-user-list');            
                    self.scrollProfileList(true, 'bm-user-list');
                }, 1000);
                self.getMatchingProfileInfos(self.$rootScope.currentUserProfileInfo, data);                
            }
            else{                         
                setTimeout(function() {
                    self.bestMatchEndText = true; 
                    self.shownBestMatchLoader = false;                  
                    self.scrollProfileList(false, 'bm-user-list');            
                    self.scrollProfileList(true, 'bm-user-list');
                }, 1000);
            }
        }
        else{
            self.bestMatchCount = 0;
            self.bestMatchEndText = false;
            self.shownBestMatchLoader = false; 
            alert("Oops!.. Something Went Wrong.. Please Try again Later");
        }
        console.log(" " + data);
      }).error(function(){

      });
  },
  getMoreShortListedProf : function(){
    var self = this;
    this.shortlistedProfCount += 5;
    this.shownShortListLoader = true;    
    this.shortListedEndText = false;
    var data = [{
            "memberID": this.$rootScope.memberID,
            "currPage": "getBestMatchedProfiles",
            "userData": {
                "offsetVal": {
                    "isOthers": "false",
                    "value": this.shortlistedProfCount.toString()
                }
            }
        }];
      
      var requestJSON ="requestJSON="+JSON.stringify(data); 

      $.post('UserActionCommand', requestJSON)
      .success(function (d, status, headers, config) { 
        var data = JSON.parse(d);
        if(data[0].status === "success"){
            if(data[0].navPageData.shortLisetedByYou.totalProfiles !== "0"){                            
                setTimeout(function() {
                    self.shortListedEndText = false; 
                    self.shownShortListLoader = false;                  
                    self.scrollProfileList(false, 'ms-user-list');            
                    self.scrollProfileList(true, 'ms-user-list');
                 }, 1000);
                self.getMatchingProfileInfos(self.$rootScope.currentUserProfileInfo, data);                
            }
            else{          
                setTimeout(function() { 
                    self.shortListedEndText = true; 
                    self.shownShortListLoader = false;                  
                    self.scrollProfileList(false, 'ms-user-list');            
                    self.scrollProfileList(true, 'ms-user-list');
                }, 1000);
            }
        }
        else{
            self.shortlistedProfCount = 0;
            self.shortListedEndText = false; 
            self.shownShortListLoader = false;  
            alert("Oops!.. Something Went Wrong.. Please Try again Later");
        }
        console.log(" " + data);
      }).error(function(){

      });
  },
  getMoreShortListedYProf : function(){
    var self = this;
    this.shortlistedProfYCount += 5;
    this.shownShortListYLoader = true;    
    this.shortListedYEndText = false;
    var data = [{
            "memberID": this.$rootScope.memberID,
            "currPage": "getProfilesShortlistedYou",
            "userData": {
                "offsetVal": {
                    "isOthers": "false",
                    "value": this.shortlistedProfYCount.toString()
                }
            }
        }];
      
      var requestJSON ="requestJSON="+JSON.stringify(data); 

      $.post('UserActionCommand', requestJSON)
      .success(function (d, status, headers, config) { 
        var data = JSON.parse(d);
        if(data[0].status === "success"){
            if(data[0].navPageData.shortLisetedYou.totalProfiles !== "0"){                                
                setTimeout(function() { 
                    self.shortListedYEndText = false; 
                    self.shownShortListYLoader = false;                   
                    self.scrollProfileList(false, 'sm-user-list');            
                    self.scrollProfileList(true, 'sm-user-list');
                }, 1000);
                self.getMatchingProfileInfos(self.$rootScope.currentUserProfileInfo, data);                
            }
            else{             
                setTimeout(function() { 
                    self.shortListedYEndText = true;  
                    self.shownShortListYLoader = false;                  
                    self.scrollProfileList(false, 'sm-user-list');            
                    self.scrollProfileList(true, 'sm-user-list'); 
                }, 1000);
            }
        }
        else{
            self.shortlistedProfYCount = 0;
            self.shortListedYEndText = false; 
            self.shownShortListYLoader = false;     
            alert("Oops!.. Something Went Wrong.. Please Try again Later");
        }
        console.log(" " + data);
      }).error(function(){

      });
  }
};