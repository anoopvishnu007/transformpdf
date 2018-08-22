
var pdfApp = angular.module('pdfApp', ['ngRoute','angular-google-adsense','720kb.socialshare','angulike']);

pdfApp.config(function($routeProvider,$locationProvider) {
	$routeProvider
		// route for the about page
		.when('/about', {
			templateUrl : 'pages/about.html',
			controller  : 'aboutController'
		})

		// route for the contact page
		.when('/contact', {
			templateUrl : 'pages/contact.html',
			controller  : 'contactController'
		})
		.when('/sitemap', {
			templateUrl : '/sitemap.html'
		})
		.when('/robots', {
			templateUrl : '/robots.txt'
		})
		.when('/', {
			templateUrl : 'pages/home.html',
			controller  : 'appController'
		});
	$locationProvider.html5Mode(true);	
});
pdfApp.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);
pdfApp.controller('appController',['$scope','$rootScope','$http','fileUpload', function ($scope, $rootScope, $http,fileUpload) {
    $scope.selectedImgeType = null;
   var downloadBtn = document.getElementById('downloadBtn');
    $scope.fileList = [];

    $scope.pdfUpload = function (ele) {
    	 $scope.pdffileNameList = [];
        $scope.pdffileList = [];
        $scope.imagefileList = [];
       
        var files = ele.files;
        console.log(files);
        for (var i = 0; i < files.length; i++) {
            $scope.pdffileList.push(files[i]);
            $scope.pdffileNameList.push(files[i].name);
        }
        $scope.fileList = $scope.pdffileList;
        console.log($scope.pdffileList);
        $scope.$apply();
    }

    $scope.imageUpload = function (ele) {
        $scope.imagefileList = [];
        $scope.imagefileNameList = [];
        var files = ele.files;
        console.log(files);
        for (var i = 0; i < files.length; i++) {
            $scope.imagefileList.push(files[i]);
            $scope.imagefileNameList.push(files[i].name);
        }
        $scope.fileList = $scope.imagefileList
        console.log($scope.imagefileList);
        $scope.$apply();
    };

    $scope.selectImageType = function(imageType){
        $scope.imgType = imageType;
    };
    
    $scope.submit = function () {
        if ($scope.imgType == null || $scope.imgType == 0 ){
            alert("Please select image type")
            return
        }
        if ($scope.fileList.length <= 0 ){
            alert("Please Select File")
            return
        }
        var file = $scope.fileList[0];
		var format = $scope.imgType;
			console.log('file is ' + JSON.stringify(file));
		var uploadUrl = "/transform/uploadPDFFile";
		$scope.metadata=fileUpload.uploadFileToUrl(uploadUrl, file, format).then(function(data){
	    	 $scope.metadata = data;
	    	 $scope.download = true;
             downloadBtn.style.display = 'block';
	     });
        if ($scope.$root.$$phase != '$apply' && $scope.$root.$$phase != '$digest') {
            $scope.$apply();
        }
    }

    $scope.downloadFile = function(){            	
        window.open("transform/downloadFile/"+$scope.metadata.data.uuid+"/"+$scope.metadata.data.outputFileName,'_self');
     }
}]);

pdfApp.controller('aboutController', function($scope) {
	$scope.message = 'www.transformpdf.com is a public utility service provider to do all kind of operation using pdf. The site offers various pdf transformation such as pdf to doc,pdf to text, pdf to image,jepg,png or PNG etc';
});

pdfApp.controller('contactController', function($scope) {
	$scope.message = 'Contact us! www.transformpdf.com';
});

pdfApp.service('fileUpload', ['$http', function($http) {
  
 		this.uploadFileToUrl=function(uploadUrl, file,format) {
 	
		var fd = new FormData();
		fd.append('file', file);
		console.log('file is ' + JSON.stringify(file));

		fd.append('convertFormat', format);
		fd.append('resolution', "300");

 		return $http.post(uploadUrl, fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}, 
			params: {
	              fd
            }
            
		}).then(function(success) {
			 console.log("success")
			 return success;
		});
 	   
	};
	
} ]);
pdfApp.controller('TestController', ['Socialshare', function testController(Socialshare) {

    Socialshare.share({
      'provider': 'facebook',
      'attrs': {
        'socialshareUrl': 'http://www.transformpdf.com'
      }
    })}]);

pdfApp.run([
    '$rootScope', function ($rootScope) {
        $rootScope.facebookAppId = '[304434373459921]'; // set your facebook app id here
    }
]);

pdfApp.controller('myController', [
    '$scope', function ($scope) {
        $scope.myModel = {
            Url: 'http://www.transformpdf.com',
            Name: "Online Free PDF converter | Convert PDF to word DOC, JPG or JPEG, PNG, text and vice-versa", 
            ImageUrl: ''
        };
    }
]);

    
    