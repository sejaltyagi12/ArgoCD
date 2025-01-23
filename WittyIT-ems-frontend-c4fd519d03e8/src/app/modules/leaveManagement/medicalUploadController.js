(function () {
    'use strict';

    angular.module('app')
        .controller('medicalUploadCtrl', ['$state','$scope', 'authService', 'EncApiService','globalFactory', '$timeout', function ($state,$scope, authService, EncApiService, globalFactory, $timeout) {

            var vm = this;
            vm.message = '';
            vm.today = new Date();
            vm.today=vm.today.getDate() + '/' +(vm.today.getMonth() + 1)  + '/' + vm.today.getFullYear();
            vm.minDate = '-29d';
            vm.flagChanged=true;
            localStorage.setItem('medicalCeritificateName', null);
            vm.validExtension=undefined;
            vm.invalidSize=false;
            vm.pdfUploadErrMsg='';



            /*
             * keeps track if application for leave was successful or not
             */
            vm.formSubmitted = null;
            vm.pdfUpload=null;

            /*
             * keeps track if applied for a half day
             */
            vm.halfDay = false;

            /*
             * data to be sent along leave application call
             */
            vm.leaveApplicationData = {
                fromDate: null,
                toDate: null,
                employeeComments: null,
                typeId: null,
                dayCount: null
            };

            /*
             * Remainig Leaves
             */
            vm.remainingLeaves = {};

            /*
             * Gets leave types
             */

            vm.initialData=function(){

                EncApiService('leaveDetails').then(function(response) {
                    response.data
                },
                function (err) {
                    vm.message = err.data.message;
                });
            }


            /*
             * Gets leave Status
             */

            var handleFileSelect = function(evt) {
                vm.validExtension=undefined;
                vm.invalidSize=false;
                vm.rosterfile = evt.currentTarget.files[0];

                vm.validExtension = (/\.(pdf)$/i).test(vm.rosterfile.name);
                $scope.$apply();
                if (!vm.validExtension) {
                    return;
                }
                if(vm.rosterfile.size > 1000000){
                    vm.invalidSize=true;
                    return;
                }

                var reader = new FileReader();
                reader.onload = function(evt) {
                    $scope.$apply(function($scope) {
                        vm.myRoster = angular.copy(evt.target.result);
                    });
                };
                reader.readAsDataURL(vm.rosterfile);
            };
            
            angular.element(document.querySelector('#rosterInput')).on('change', handleFileSelect);

            vm.upload = function(id) {
                    vm.pdfUploadErrMsg='';
                    vm.pdfUpload=null;

                    console.log("here");
                    var myformdata = new FormData();
                    vm.myRoster = vm.dataURItoBlob(vm.myRoster);
                    myformdata.append('fileContent', vm.myRoster,vm.rosterfile.name);

                    var headers = {
                        'Content-Type': undefined
                    };
                    EncApiService('uploadMedicalCertificate', {
                        'data': myformdata,
                        'transformRequest': angular.identity,
                        'headers': headers

                    },{'id':id})
                    .then(function(response) {
                        vm.pdfUpload= true;
                    },
                    function(err) {
                       vm.pdfUpload = false;
                       vm.pdfUploadErrMsg=err.data.message;

                    });

                    
                };

                vm.dataURItoBlob = function(dataURI) {
                    // convert base64/URLEncoded data component to raw binary data held in a string
                    var byteString;
                    if (dataURI.split(',')[0].indexOf('base64') >= 0)
                        byteString = atob(dataURI.split(',')[1]);
                    else
                        byteString = unescape(dataURI.split(',')[1]);

                    // separate out the mime component
                    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

                    // write the bytes of the string to a typed array
                    var ia = new Uint8Array(byteString.length);
                    for (var i = 0; i < byteString.length; i++) {
                        ia[i] = byteString.charCodeAt(i);
                    }

                    return new Blob([ia], {
                        type: mimeString
                    });
                };

            /*
             * Submit leave application
             */
            vm.applyLeave = function () {
                vm.message = "";
                vm.errMessage = "";

                var data = angular.copy(vm.leaveApplicationData);

                data.fromDate = globalFactory.convertDates(vm.leaveApplicationData.fromDate);
                data.toDate = (vm.halfDay == true)?data.fromDate: globalFactory.convertDates(vm.leaveApplicationData.toDate);

                data.dayCount = (vm.halfDay == true)? 0.5: (Math.ceil(Math.abs(new Date(data.fromDate) - new Date(data.toDate))/(1000 * 3600 * 24))+1);

                if (data.dayCount < 0) {
                    vm.errMessage = "From date must be smaller than to date";
                    return;
                }

                EncApiService('sendLeaveApplication', { 'data': data }).then(function (response) {
                        vm.pdfUploadErrMsg='';
                        vm.pdfUpload=null;                    
                        vm.formSubmitted = true;

                        if(vm.myRoster!= null && data.typeId==2 ){
                            vm.upload(response.data.id);
                        }
                        vm.myRoster=null;
                        angular.element('#rosterInput').val(null);
                        document.getElementById("upload-file-info").innerHTML = '';
                        document.getElementById("leaveForm").reset();

                        vm.leaveApplicationData = {
                            fromDate: null,
                            toDate: null,
                            employeeComments: null,
                            typeId: null,
                            dayCount: null
                        };
                        
                        vm.leaveType= vm.leaveTypes[0];
                        vm.flagChanged=false;
                        $timeout(function(){vm.flagChanged=true;}, 1);
                        vm.validExtension=undefined;
                        vm.leaveApplicationData.typeId =  vm.leaveTypeMap[vm.leaveType];
                        $scope.leaveForm.$setPristine();
                        vm.halfDay = false;

                    }, function (err) {
                        vm.pdfUploadErrMsg='';
                        vm.pdfUpload=null;
                        vm.validExtension=undefined;
                        vm.formSubmitted = false;
                        vm.message = err.data.message;

                    });
            };

    }]);
})();