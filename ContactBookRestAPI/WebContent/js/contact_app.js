var app = angular.module('contactBookApp', []);

app.controller('contactBookCtrl', function($scope, $http) {
	
	$scope.createContactUrl = "rest/ContactBookService/create";
	$scope.updateContactUrl = "rest/ContactBookService/update";
	$scope.getContactUrl = "rest/ContactBookService/get/";
	$scope.deleteContactUrl = "rest/ContactBookService/delete";
	$scope.searchContactByNameUrl ="rest/ContactBookService/searchname";
	$scope.searchContactByEmailUrl ="rest/ContactBookService/searchemail";

	var config = {
		headers : {
			'Authorization' : 'Basic ' + btoa('prabhakar:plivo'),
			'Accept' : 'application/json'
		}
	};

	$scope.contactData = [];
	$scope.showNextPage = false;
	$scope.searchByNameFlag = true;
	$scope.totalSearchCount = 0;
	$scope.nextPageStartIndex = 0;
	
	$scope.clearSearchResults = function() { 
		$scope.contactData.length = 0;
	}
	
	$scope.createContact = function() {

		var contactBookWebRequest = {
			email : $scope.createEmail,
			name : $scope.createName
		};
	
		$http.put($scope.createContactUrl, contactBookWebRequest, config)
			.then(function(response) {
				
				alert("REST API response : " + response.status);
				alert("REST API message : " + response.data.msg);
		});
	};
		
		
		
	$scope.updateContact = function() {
	
		var contactBookWebRequest = {
				email : $scope.updateEmail,
				name : $scope.updateName
		};
		
		$http.post($scope.updateContactUrl, contactBookWebRequest, config)
			.then(function(response) {
				
				alert("REST API response : " + response.status);
				alert("REST API message : " + $scope.response.data.msg);
		});
	};

	$scope.getContact = function() {
		
		var getConfig = {
				params : { 
					email : $scope.getEmail
			    },
				headers : {
					'Authorization' : 'Basic ' + btoa('prabhakar:plivo'),
					'Accept' : 'application/json'
				}
			};
		
		$http.get($scope.getContactUrl, getConfig)
			.then(function(response) {
				
				alert("REST API response : " + response.status);
				alert("REST API message : " + response.data.msg);
				
				if(response.data.sr) {					
					$scope.contactData = response.data.sr.cont;
					
					if($scope.contactData[0]) {
						alert("Contact : " + $scope.contactData[0].email
								+ " " + $scope.contactData[0].name);
					}
				}
		});
	};


	$scope.deleteContact = function(contact) {
		
		var deleteConfig = {
				params: {
			        email : contact.email
			    },
				headers : {
					'Authorization' : 'Basic ' + btoa('prabhakar:plivo'),
					'Accept' : 'application/json'
				}
			};

		$http.delete($scope.deleteContactUrl, deleteConfig)
			.then(function(response) {
					
					alert("REST API response : " + response.status);
					alert("REST API message : " + response.data.msg);
					
					if($scope.statuscode == '200' && response.data.rsp == 'SUCCESS') {
						$scope.contactData[0].remove(contact) 
					}
				});
	};
	
	$scope.searchByName = function() {
		$scope.nextPageStartIndex = 0;
		$scope.searchByNameFlag = true;		
		$scope.search(0);
	}
	
	$scope.searchByEmail = function() {
		$scope.nextPageStartIndex = 0;
		$scope.searchByNameFlag = false;
		$scope.search(0);
	}
	
	$scope.searchNextPage = function(startIndex) {
		$scope.showNextPage = $scope.search(startIndex + 5);
		$scope.nextPageStartIndex = startIndex + 5;
	}
	
	$scope.search = function(startIndex) {
		
		var searchUrl;
		var searchString;
		
		if($scope.searchByNameFlag == true) {
			searchUrl = $scope.searchContactByNameUrl;
			searchString = $scope.searchName
		} else {
			searchUrl = $scope.searchContactByEmailUrl;
			searchString = $scope.searchEmail
		}
		
		var webSearchRequest = {
			sin : startIndex,
			sstr : searchString
		};
	
		$http.post(searchUrl, webSearchRequest, config)
			.then(function(response) {

			alert("REST API response : " + response.status);
			alert("REST API message : " + response.data.rsp);
			
			if(response.data.sr) {
				$scope.contactData = response.data.sr.cont;
				$scope.totalSearchCount = response.data.sr.tc;
			
				if($scope.totalSearchCount >  (startIndex + 5)) {
					$scope.showNextPage = true;						
				}
				else {
					$scope.showNextPage = false;
				}
			}
		});
	};
	
});