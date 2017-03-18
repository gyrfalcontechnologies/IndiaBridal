app.controller('memberShipController', ['$http','$rootScope', '$timeout', 'loggedUserService', 'md5', MemberShipController]);

function MemberShipController($http, $rootScope, $timeout, loggedUserService, md5){
    
    var self = this;
    this.loggedUserService = loggedUserService;
    this.$http = $http;
    this.$rootScope = $rootScope;
    this.$timeout = $timeout;
    this.md5 = md5;
    this.currentThumbnailIndex = 0;
    this.currentThumbnailModelIndex = 0;
    this.$rootScope.$broadcast("homeBroadCast", false);
    this.$rootScope.$broadcast("profileBroadCast", true);  

    this.$timeout(function(){        
        $("html,body").animate({'scrollTop':$(".top-banner").position().top});
    });  
}

MemberShipController.prototype = { 
    
    sendData : function () {
            
            
                /*https://test.payu.in/_payment 
                JBZaLc 
                GQs7yium */
            
           /* var dummy = {
                    "key" : "MlTAMgPz",
                    "txnid" : "INDBZ100000001",
                    "amount" : 1.00,
                    "productinfo" : "Basic",
                    "firstname" : "Videsh",
                    "email" : "vis.vas24@gmail.com",
                    "phone" : "9952024278",
                    "surl": "/profile",
                    "furl" : "/profile" 
                };

            var hashText = dummy.key+ "|" +dummy.txnid+ "|" +dummy.amount+ "|" +dummy.productinfo+ "|" +
                           dummy.firstname+ "|" +dummy.email + "|e4mJi7T1yf";
           
            var chkSum = this.md5.createHash(hashText);
            dummy["hash"] = chkSum; 

            var data = $.param(dummy);
        
            var config = {
                headers : {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            }

            this.$http.post('https://secure.payu.in/_payment', data)
            .success(function (data, status, headers, config) { 
                $(".resp")[0].innerHTML = data;
            })
            .error(function () {
                 //console.log("error");
            });*/
            var data = [{
                            "memberID": "IBM10000001",
                            "currPage": "payment",
                            "payment": {
                                "key": "MlTAMgPz",
                                "txnid": "INDBZ100000001",
                                "amount": 1.00,
                                "productinfo": {
                                    "paymentParts": [{
                                        "name": "abc",
                                        "description": "abcd",
                                        " value ": "500",
                                        " isRequired ": "true",
                                        " settlementEvent ": "EmailConfirmation "
                                    }, {
                                        " name ": " xyz ",
                                        " description ": " wxyz ",
                                        " value ": " 1500 ",
                                        " isRequired ": " false ",
                                        " settlementEvent ": " EmailConfirmation "
                                    }]
                                },
                                "firstname": "karthick",
                                "email": "skarthi32@gmail.com",
                                "phone": "9159420731",
                                "surl": "/profile",
                                "furl": "/profile",
                                "service_provider": "payu_paisa"
                            }
                        }];
            request({
            	  url:"https://secure.payu.in/_payment",
            	  method:"POST",
            	  json:true,
            	  body:params
            	
            	});
                        
            var requestJSON =JSON.stringify(data);
            var config = {
                     params: {requestJSON:requestJSON},
                     headers : {'Accept' : 'application/json'}
                    };
            return this.$http.post('UserActionCommand',config)
            .success(function (d, status, headers, config) {            
                var data= JSON.stringify(d);
                console.log("Data:"+data);
                return data; 
            })
            .error(function () {
                console.log("error"); 
           });
        }

};