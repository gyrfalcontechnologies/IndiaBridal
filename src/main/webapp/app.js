var app = angular.module('IndiaBridal', ['ngRoute','ui.bootstrap','angular-md5', 'ngCookies']);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'views/home.html',
        controller: 'homeController as hc'
      }).
      when('/profile', {
        templateUrl: 'views/userProfile.html',
        controller: 'userProfileController as upc'
      }).
      when('/fetch', {
        templateUrl: 'views/loader.html', 
      }).
      when('/inbox', {
        templateUrl: 'views/inbox.html',
        controller: 'userProfileController as upc'
      }).
      when('/about/IndiaBridal', {
        templateUrl: 'views/aboutIndiaBridal.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/t&c', {
        templateUrl: 'views/terms&cond.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/policy', {
        templateUrl: 'views/policy.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/blog', {
        templateUrl: 'views/blog.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/career', {
        templateUrl: 'views/career.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/site-map', {
        templateUrl: 'views/site-map.html',
        controller: 'mainAppCtrl as mc'
      }).      
      when('/security-tips', {
        templateUrl: 'views/security-tips.html',
        controller: 'mainAppCtrl as mc'
      }).
      when('/contact-us', {
        templateUrl: 'views/contact-us.html',
        controller: 'mainAppCtrl as mc'
      }).
      when('/faq', {
        templateUrl: 'views/faq.html',
        controller: 'mainAppCtrl as mc'
      }).
      when('/member-info', {
        templateUrl: 'views/member-info.html',
        controller: 'memberInfoController as mic'
      }).
      when('/search', {
        templateUrl: 'views/search-result.html',
        controller: 'searchResultController as src'
      }).
      when('/profile/edit', {
        templateUrl: 'views/edit-profile.html',
        controller: 'profileEditController as pec'
      }).
      when('/memberShip', {
        templateUrl: 'views/memberShip.html',
        controller: 'memberShipController as msc'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);

app.controller('mainAppCtrl', ['$http','$scope','$rootScope', 'loggedUserService', '$location', '$timeout', '$cookieStore', MainAppCtrl]);

function MainAppCtrl($http, $scope, $rootScope, loggedUserService, $location, $timeout, $cookieStore){  
    var self = this;
    this.isProfile = false;
    this.$scope = $scope;
    this.$http = $http;
    this.isPanelExpand =  true;
    this.isChatOpened = false;
    this.$rootScope = $rootScope;
    this.loggedUserService = loggedUserService;
    this.$location = $location;
    this.$timeout = $timeout;
    this.$cookies = $cookieStore;
    this.uName = '';
    this.pwd = '';  
    this.isLogged = false;
    this.$rootScope.isLogged = true;
    this.isFooter = false;
    this.$rootScope.activeNav = ""; 
    this.isFooter = false;
    this.$rootScope.$on("profileBroadCast", function(event, flag){
        self.isProfile = flag;
        self.isFooter = false;
        self.isPanelExpand = true;
        self.toggleSocial();
    }); 
    this.$rootScope.isHome = true;
    this.$rootScope.$on("homeBroadCast", function(event, flag){
        self.$rootScope.isHome = flag;
        self.isPanelExpand = true;
        self.toggleSocial();
    });
    this.loggedUserService.getCurrentUserProfileInfo().then(function(resp){
      self.currentUserProfileInfo = resp.data;
    });
 
    this.regCreatedBy = this.regCommunity = this.regMtongue = "AA_DEFAULT";
    
    this.showForward = false;

    this.activityList =[
                        {name:"FAQ's", isActive:true},
                        {name:"Registration / Login", isActive:false},
                        {name:"Profile Management", isActive:false},
                        {name:"Search", isActive:false},
                        {name:"Membership", isActive:false},
                        {name:"Block & Revert", isActive:false},
                        {name:"Chat", isActive:false},
                        {name:"Payment Option", isActive:false},
                        {name:"Technical Issues", isActive:false},
                      ];
    setTimeout(function() {      
      $('li.faq-answer').hide();
    });
    
    $('[data-toggle="tooltip"]').tooltip(); 
    this.$timeout(function(){        
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});
    },500);

    this.showCommErr = "";
    this.showMothTngeErr = "";
    this.showCreateByErr = "";
    this.showEmailErr = "";
    this.showEmailExist = "";
    this.showPhNumExist = "";
    this.isAuthProcessed = true;
    this.defaultProfCreatedList =  {
                        "AA_DEFAULT" : "Profile Created By",
                        "S001" : "Self",
                        "S002" : "Mother",
                        "S003" : "Father",
                        "S004" : "Brother",
                        "S005" : "Sister",
                        "S006" : "Uncle"
                      };

    this.defaultcastList =   
                        {"AA_DEFAULT" : "Your Community",
                        "C001" : "Mudhaliyar",
                        "C002" : "Nadar",
                        "C003" : "Iyer",
                        "C004" : "Iyengar",
                        "C005" : "Catholic",
                        "Others" : "Others"};

    this.defaultmotherTongueList =  {
                        "AA_DEFAULT" : "Your Mother Tongue",
                        "M001" : "Tamil",
                        "M002" : "Telugu",
                        "M003" : "Malayalam",
                        "M004" : "Hindi",
                        "Others" : "Others"
                      };
 
    
    this.profileCreatedList = this.defaultProfCreatedList;
    this.castList = this.defaultcastList; 
    this.motherTongue = this.defaultmotherTongueList;

    this.regMtongueText = "";
    this.regCastText = "";    
    this.myCrslImg = [{"src" : "images/ban-2.jpg","isVisible":true},{"src" : "images/ban-3.jpg","isVisible":false}];
    var imgDisplayCount = 0;
    this.crslInteVal = setInterval(function() {
       if(imgDisplayCount <self.myCrslImg.length-1){
          imgDisplayCount++;          
       }
       else{
          imgDisplayCount = 0;
       }
       _.each(self.myCrslImg, function(img){
          img.isVisible = false;
      });
       self.myCrslImg[imgDisplayCount].isVisible = true;
       self.$scope.$apply();
    }, 8000); 
    this.isFromEditProf = false;
    //this.resetRegForm();
    this.loadInitialFormData();
    this.$rootScope.$on("re-enter", function(event, u ,p, flag){
      self.uName = u;
      self.pwd = p;
      self.isFromEditProf = flag;
      if(u){
        self.logUser()
      }
      else{
        $location.path("/");}
    });
    this.gyrShowMenu= false;
}

MainAppCtrl.prototype = {
  clearCache : function(){    
    this.$cookies.put("IB-uName", "");
    this.$cookies.put("IB-ID", "");
    this.$cookies.put("IB-latestUpdated", "");                         
    this.$cookies.put("IB-logedUserName", "");
    self.$cookies.put("IB-uPWD", "");
    self.$cookies.put("IB-prefUpdated", ""); 
  },
  resetRegForm : function(){
    setTimeout(function() {      
      var form = document.getElementsByClassName("form-body");
      form[0].reset();
    },500);  
  },
  loadInitialFormData : function(){
	  var self = this;
    
    var date = new Date;
    this.maxDateLimit = (date.getFullYear() - 18) + "-12-31";

	  var data =[{"memberID": "","currPage": "regForm",  "userData":{"": {"isOthers": "false","value": ""}}}];
      
      var requestJSON ="requestJSON="+JSON.stringify(data);
      var config = {
          headers : {
              'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
          }
      }

      $.post('UserActionCommand', requestJSON)
      .success(function (d, status, headers, config) { 
    	  //console.log("data from db = "+data);
    	  var data= JSON.parse(d);
	    if(data[0].status === "success"){
	    	var defaultProfCreatedList =  {"AA_DEFAULT" : "Profile Created By"};
	    	var defaultcastList ={"AA_DEFAULT" : "Your Community"};
	    	var defaultmotherTongueList =  {"AA_DEFAULT" : "Your Mother Tongue"};
	    	 
	    	  _.each(data[0].createdBy, function(val, key){
	    		  defaultProfCreatedList[key] = val;
	    	  });
	    	  
	    	  _.each(data[0].caste, function(val, key){
	    		  key = (val==="Others")? "Others" : key;
	    		  defaultcastList[key] = val;
	    		  
	    	  });
	    	  
	    	  _.each(data[0].motherTongue, function(val, key){
	    		  key = (val==="Others")? "Others" : key;
	    		  defaultmotherTongueList[key] = val;
	    	  }); 
	    	  
	    	  self.profileCreatedList = defaultProfCreatedList;
	    	  self.castList = defaultcastList;
	    	  self.motherTongue = defaultmotherTongueList;
	    } 
      })
      .error(function () {
          //console.log("error");
          self.profileCreatedList = self.defaultProfCreatedList;
    	  self.castList = self.defaultcastList;
    	  self.motherTongue = self.defaultmotherTongueList;
          self.$scope.$apply();
      });
  
  },
  sendRegisterdData : function(){ 
    var split = this.mailID.split("."), self = this;
    var isEmailID = (split[split.length-1].length>=2 && split[split.length-1].length<=3)? true : false;
    var isCommAlExist  = false;
    var isMTALredyExist = false;

    _.each(this.castList, function(cl){
      if(cl.toUpperCase() === self.regCastText.toUpperCase()){
         isCommAlExist = true;
      }
    });
    _.each(this.motherTongue, function(mt){
      if(mt.toUpperCase() === self.regMtongueText.toUpperCase()){
         isMTALredyExist = true;
      }
    });

    if(this.regCommunity !== "AA_DEFAULT" 
        && this.regMtongue !== "AA_DEFAULT" && 
        this.regCreatedBy !== "AA_DEFAULT" && 
        isEmailID && !isMTALredyExist && !isCommAlExist){

        this.showCommErr = "";
        this.showMothTngeErr = "";
        this.showCreateByErr = "";
        this.showEmailErr = "";
        
        var serializedFormData = $('.form-body').serializeArray();

        this.regData = [{
            "memberID" : "",
            "currPage" : "register",
            "userData" : this.formatRegData(serializedFormData)
        }];  
        

        var config = {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        }
        //console.log( this.regData);
        this.$rootScope.registrStatus = "Registration in progress..."
        $("#reg-succs-modal").modal('show');
        var requestJSON ="requestJSON="+JSON.stringify(this.regData);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    //console.log(d);
                    var data= JSON.parse(d);
                   if(data[0].isProfileCreated==="true"){
                	   self.$rootScope.registrStatus = "Profile registered successfully!!.... Please check your registered email id for further info" ;
                     var form = document.getElementsByClassName("form-body");
                     setTimeout(function() {form[0].reset();}, 1000);   
                   }
                   else{
                	   self.showEmailExist = "";
                	   self.showPhNumExist = "";
                	   
                	   self.showEmailExist = (data[0].isEmailExists==="true")? "Sorry!.. Email ID already registered ": "";
                	   self.showPhNumExist = (data[0].isPhNumExists==="true")? "Sorry!.. Phone number already registered " : "";
                	   
                	   if(data[0].status==="failure" && data[0].isEmailExists==="false" && data[0].isPhNumExists==="false"){
                		   self.$rootScope.registrStatus = "Ooops!!! ...Please try again after sometime." ;
                           $("#reg-succs-modal").modal('show');
                	   }

                   }
                })
                .error(function () {
                     //console.log("error");
                     self.$rootScope.registrStatus = "Ooops!!! ... Something went wrong. Please try again after sometime." ;
                     $("#reg-succs-modal").modal('show');
                });
    }
    else{
      this.showCommErr = (this.regCommunity === "AA_DEFAULT")? "* Please select your Community":
                          ((isCommAlExist)? "* Entered value already Available in the list" : "");
      this.showMothTngeErr = (this.regMtongue === "AA_DEFAULT")? "* Please select your Mother Tounge": 
                              ((isMTALredyExist)? "* Entered value already Available in the list" : "");
      this.showCreateByErr = (this.regCreatedBy === "AA_DEFAULT")? "* Please select Created by": "";
      this.showEmailErr = (isEmailID)? "" :"* Please enter a valid id";
    }
  },
  formatRegData : function(dat){
    var tempList = {}, self = this;

    _.each(dat, function(val){
        if(val.name === "casteID"){
          tempList[val.name] = {"isOthers" : (self.regCommunity ==="Others")? "true" : "false", 
          "value": (self.regCommunity==="Others")? self.regCastText : val.value}
        }
        else if(val.name === "motherTongueID"){
          
            tempList[val.name] = {"isOthers" : (self.regMtongue ==="Others")? "true" : "false", 
            "value": (self.regMtongue==="Others")? self.regMtongueText : val.value}
           
        }else{          
          tempList[val.name] = {"isOthers" : "false", "value": val.value} 
        }
    });

    return tempList;
  },
   toggleFAQ : function(event){  
      $(event.currentTarget).next().slideToggle(); 
   },
   toggleSocial : function(){  
      this.isPanelExpand = !this.isPanelExpand;
      if(this.isPanelExpand){
        $(".social-panel").animate({'left':'-50px'},500);
        $(".social-panel button").html('&raquo;'); 
      }
      else{
        $(".social-panel").animate({'left':'0px'},500);
        $(".social-panel button").html('&laquo;'); 
      }
   },
   toggleChat : function(){
      this.isChatOpened = !this.isChatOpened;
      if(this.isChatOpened){
        $(".chat").animate({'bottom':'400px'},500);
        //$('.chat').css({ 'height': 'calc(100% - 238px)' });
        if(this.isPanelExpand){
         this.toggleSocial();
        }
      }else{
        $(".chat").animate({'bottom':'0px'},500);
      }
   },
   toggleAdminChat : function(){
      this.isChatOpened = !this.isChatOpened;
      if(this.isChatOpened){
        $(".admin-chat").animate({'bottom':'240px'},500);
        //$('.chat').css({ 'height': 'calc(100% - 238px)' });
        if(this.isPanelExpand){
         this.toggleSocial();
        }
      }else{
        $(".admin-chat").animate({'bottom':'0px'},500);
      }
   },
   logUser : function(){ 
    var self = this;
    var loginreq = [{
        "memberID": "",
        "currPage": "authenticate",
        "userData": {
          "userName": {
            "isOthers": "false",
            "value": this.uName
          },
          "password": {
            "isOthers": "false",
            "value": this.pwd
          },
          "userNameType": {
            "isOthers": "false",
            "value":  this.getUserNameType(this.uName)
          }
        }
      }];
      this.isAuthProcessed = false;
       var requestJSON ="requestJSON="+JSON.stringify(loginreq);
        $.post('UserActionCommand', requestJSON)
                .success(function (d, status, headers, config) { 
                    var data= JSON.parse(d);
                    //console.log(data);
                    if(data[0].isnavPageDataAvailable==="true" && data[0].isAuthenticated==="true"){
                      var percentages = {"partnerProfiles" : 100, "basicInfo" : 10, "uBasicInfo":10, "uFamilyInfo":20, "uAstroDet":30, "uEduAndCar":45, "uLifeStyle":75, "uPartnerPref":100}
                      self.$cookies.put("IB-uName", self.uName); 
                      self.$cookies.put("IB-uPWD", self.pwd); 
                      self.$rootScope.isLogged = true;
                      self.$rootScope.memberID = data[0].memberID;
                      self.$cookies.put("IB-ID", data[0].memberID);
                      self.$rootScope.userRegData = data;   
                      self.$cookies.put("IB-latestUpdated", data[0].navPage);
                      self.$rootScope.isPrefUpdated = (data[0].isPrefUpdated === "true");
                      self.$cookies.put("IB-prefUpdated", self.$rootScope.isPrefUpdated);
                      self.isLogged = true; 
                      self.$rootScope.logedUserName = data[0].navPageData.profPicAndName.fullName;                      
                      self.$cookies.put("IB-logedUserName", self.$rootScope.logedUserName);
                      if(data[0].navPageData.profPicAndName.profilePic!==""){
                          self.$rootScope.profilePics = self.getProfilePics(data[0].navPageData.profPicAndName.profilePic.split('index=')[0]);
                          self.$rootScope.ppIndex = parseInt(data[0].navPageData.profPicAndName.profilePic.split('index=')[1]);
                      }
                      else{
                        self.$rootScope.profilePics = [];
                        self.$rootScope.ppIndex = 0;
                      } 
                      self.$rootScope.percentageComplete = percentages[data[0].navPage];
                      //$('#login-modal').modal('hide');
                      if(data[0].navPage === "partnerProfiles" && !self.isFromEditProf){ 
                        self.$rootScope.searchResult = data[0].navPageData; 
                        self.$rootScope.homeRedirect = true;                       
                        self.$location.path('/profile'); 
                      }else{
                        self.$rootScope.homeRedirect = false;
                        self.$location.path('/profile/edit');
                      }
                   }
                   else{
                      self.$rootScope.isLogged = false;
                      self.isLogged = false;                      
                      self.uName = '';
                      self.pwd = '';
                      self.$cookies.put("IB-uName", self.uName);
                      self.$cookies.put("IB-uPWD", self.pwd);                        
                      self.$cookies.put("IB-ID", "");              
                      self.$cookies.put("IB-latestData", "");                     
                      self.$cookies.put("IB-latestUpdated", "");
                      self.$cookies.put("IB-prefUpdated", "");
                   }
                   self.isAuthProcessed = true;
                  })
                .error(function () {
                     //console.log("error"); 
                }); 
   },
   getProfilePics : function(picData){
    var templist = [];
    var images = picData.split("||");
    _.each(images, function(img){
        if(img!=="") templist.push({url : img.split(" ").join("+"), resized : {dataURL : img.split(" ").join("+")}});
    });

    return templist;
  },
   getUserNameType : function(uName){
    return uName.match(/[789][0-9]{9}/)? "phoneNum" : "email";
   },
   scrollToTop : function(fromFooter){
    this.isProfile = this.isProfile;
    this.$timeout(function(){        
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});
    },500);
    //this.isFooter = (fromFooter)? fromFooter : false;
   },
   enableDisableList : function(actionItem){
    this.currentTreeView = actionItem;
      _.each(this.activityList, function(item){
          item.isActive = (item.name === actionItem)? true : false;
      });
      setTimeout(function() {      
        $('li.faq-answer').hide();
      });
   },
   navBack : function(){
      window.history.back(); 
      this.showForward = true;
   },
   navFront : function(){
      window.history.forward();
   },
   initializeMap : function(){  
      var position = new google.maps.LatLng(13.1387, 80.1337);
      var myOptions = {
        zoom: 14,
        center: position,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      var map = new google.maps.Map(
          document.getElementById("googleMap"),
          myOptions);
   
      var marker = new google.maps.Marker({
          position: position,
          map: map,
          title:"This is the place."
      });  
   
      var contentString = '<img src="http://www.indiazbridal.com/images/logo-login.png"><br><strong>IndiazBridal</strong><br> Head Quarters';
      var infowindow = new google.maps.InfoWindow({
          content: contentString
      });
   
      google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map,marker);
      });
    }
};

app.directive('image', function($q) {
        'use strict'

        var URL = window.URL || window.webkitURL;

        var getResizeArea = function () {
            var resizeAreaId = 'fileupload-resize-area';

            var resizeArea = document.getElementById(resizeAreaId);

            if (!resizeArea) {
                resizeArea = document.createElement('canvas');
                resizeArea.id = resizeAreaId;
                resizeArea.style.visibility = 'hidden';
                document.body.appendChild(resizeArea);
            }

            return resizeArea;
        }

        var resizeImage = function (origImage, options) {
            var maxHeight = options.resizeMaxHeight || 300;
            var maxWidth = options.resizeMaxWidth || 250;
            var quality = options.resizeQuality || 0.7;
            var type = options.resizeType || 'image/jpg';

            var canvas = getResizeArea();

            var height = origImage.height;
            var width = origImage.width;

            // calculate the width and height, constraining the proportions
            if (width > height) {
                if (width > maxWidth) {
                    height = Math.round(height *= maxWidth / width);
                    width = maxWidth;
                }
            } else {
                if (height > maxHeight) {
                    width = Math.round(width *= maxHeight / height);
                    height = maxHeight;
                }
            }

            canvas.width = width;
            canvas.height = height;

            //draw image on canvas
            var ctx = canvas.getContext("2d");
            ctx.drawImage(origImage, 0, 0, width, height);

            // get the data from canvas as 70% jpg (or specified type).
            return canvas.toDataURL(type, quality);
        };

        var createImage = function(url, callback) {
            var image = new Image();
            image.onload = function() {
                callback(image);
            };
            image.src = url;
        };

        var fileToDataURL = function (file) {
            var deferred = $q.defer();
            var reader = new FileReader();
            reader.onload = function (e) {
                deferred.resolve(e.target.result);
            };
            reader.readAsDataURL(file);
            return deferred.promise;
        };


        return {
            restrict: 'A',
            scope: {
                image: '=',
                resizeMaxHeight: '@?',
                resizeMaxWidth: '@?',
                resizeQuality: '@?',
                resizeType: '@?',
            },
            link: function postLink(scope, element, attrs, ctrl) {

                var doResizing = function(imageResult, callback) {
                    createImage(imageResult.url, function(image) {
                        var dataURL = resizeImage(image, scope);
                        imageResult.resized = {
                            dataURL: dataURL,
                            type: dataURL.match(/:(.+\/.+);/)[1],
                        };
                        callback(imageResult);
                    });
                };

                var applyScope = function(imageResult) {
                    scope.$apply(function() {
                        ////console.log(imageResult);
                        if(attrs.multiple)
                            scope.image.push(imageResult);
                        else
                            scope.image = imageResult; 
                    });
                };


                element.bind('change', function (evt) {
                    //when multiple always return an array of images
                    
                    //console.log("Started .....");
                    var count = 0;
                    if(attrs.multiple)
                        scope.image = [];

                    var files = evt.target.files;
                    for(var i = 0; i < files.length; i++) {
                        //create a result object for each file in files                        
                        var imageResult = {
                            file: files[i],
                            url: URL.createObjectURL(files[i])
                        };

                        fileToDataURL(files[i]).then(function (dataURL) {
                            imageResult.dataURL = dataURL;
                        });

                        if(scope.resizeMaxHeight || scope.resizeMaxWidth) { //resize image
                            doResizing(imageResult, function(imageResult) {                              
                                count++;
                                //console.log((count/files.length*100)+"%")
                                applyScope(imageResult);
                                if(count===files.length){
                                  //console.log("done.....");
                                }
                            });
                        }
                        else { //no resizing
                            applyScope(imageResult);
                        }
                    }
                });
            }
        };
    });