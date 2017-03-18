app.service('loggedUserService', [ '$http', LoggedUserService ]);

function LoggedUserService($http) {
	var self = this;
	this.$http = $http;
	this.usersMembersList = [];
	this.offsetVal = 0;
}

LoggedUserService.prototype = {
	getCurrentUserProfileInfo : function() {
		return this.$http.get("data/loggedUserProfileInfo.json");
	},
	getCurrentMemberProfileInfo : function(memberID) {
		var data = [ {
			"memberID" : "IBM10000004",
			"currPage" : "getAllProfiles",
			"userData" : {
				"offsetVal" : {
					"isOthers" : "false",
					"value" : this.offsetVal.toString()
				}
			}
		} ];
		this.offsetVal = this.offsetVal + 5;
		var requestJSON = "requestJSON=" + JSON.stringify(data);

		return $
				.post('UserActionCommand', requestJSON)
				.success(
						function(d, status, headers, config) {
							var data = JSON.parse(d);
							// console.log(data);
							if (data[0].status === "success") {
								var data = JSON.stringify(d);
								return data;
							} else {
								alert("Ooops!! Something went wrong. Please try again later");
							}
						})
				.error(
						function() {
							alert("Ooops!! Something went wrong. Please try again later");
						});
	}
};