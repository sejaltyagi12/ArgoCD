/* =========================================================
 * Module: leaveApplicationController.js
 * Handles Leave Application page behaviour
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .controller('leaveApplicationCtrl', ['$state','$scope', 'authService', 'EncApiService', 'globalFactory', '$timeout','$stateParams','toaster', function ($state,$scope, authService, EncApiService, globalFactory, $timeout,$stateParams,toaster) {

            $("#global-progress-display").show();
            var vm = this;
         
            vm.today = new Date();
            vm.today=vm.today.getDate() + '/' +(vm.today.getMonth() + 1)  + '/' + vm.today.getFullYear();
            vm.minDate = '-29d';
            vm.flagChanged=true;
            localStorage.setItem('medicalCeritificateName', null);
            vm.validExtension=undefined;
            vm.invalidSize=false;
          
            vm.view=false;
            vm.mandtoryFile='';
            vm.myRoster = null;
        
            vm.fileChoosen = 'No file chosen';

            /*
             * keeps track if application for leave was successful or not
             */
            var formSubmitted = null;
            var errMessage = null;
            var pdfUpload=null;

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

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            if($stateParams.id!=null && $stateParams.id!=undefined && $stateParams.id!=''){
                EncApiService('fetchLeaveTypes').then(function(response) {
                    vm.leaveTypes=[];
                    for (var i = 0; i < response.data.length; i++) {
                      vm.leaveTypes.push(response.data[i].type);
                      
                    }
                    vm.view=true;
                    vm.id=$stateParams.id
                    EncApiService('leaveDetails',null,{'id':vm.id}).then(function(response) {
                        vm.leaveApplicationData.fromDate=globalFactory.convertDateString(response.data.fromDate);
                        if(response.data.dayCount == 0.5)
                            vm.halfDay=true;
                        else
                            vm.leaveApplicationData.toDate=globalFactory.convertDateString(response.data.toDate)
                        vm.leaveType=response.data.leaveType.type;
                        vm.leaveApplicationData.employeeComments=response.data.employeeComments;
                        if(response.data.attachmentUrl!=null){
                            var url=response.data.attachmentUrl;
                            var myEl = angular.element( document.querySelector( '#upload-file-info' ) );

                            var fileName = url.substring( url.lastIndexOf('/')+1, url.length);
                            // String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
                            myEl.text(fileName);
                            // myEl.text('attachment.pdf');
                        }


                        $("#global-progress-display").hide();

                    },
                    function (err) {
                        $("#global-progress-display").hide();
                        vm.message = err.data.message;

                    });

                },
                function (err) {
                    $("#global-progress-display").hide();
                    vm.message = err.data.message;
                    
                });

               

            }
            else{
                EncApiService('fetchLeaveTypes').then(function(response) {
                    vm.leaveTypes = [];
                    vm.leaveTypeMap = {};

                    for (var i = 0; i < response.data.length; i++) {
                      vm.leaveTypes.push(response.data[i].type);
                      vm.leaveTypeMap[response.data[i].type] = response.data[i].typeId;
                    }

                    vm.leaveType =  vm.leaveTypes[0];
                    vm.leaveApplicationData.typeId = vm.leaveTypeMap[vm.leaveType];

                    /*
                    * Gets leave Status
                    */

                    EncApiService('employeeLeaveDetail',null,{'id':''}).then(function(response) {

                        vm.remainingLeaves['Casual Leave'] = response.data.remainingCasualLeaves;
                        vm.remainingLeaves['Sick Leave'] = response.data.remainingSickLeaves;
                        vm.remainingLeaves['Civil Duty Leave'] = response.data.remainingCivilDutyLeave;
                        vm.remainingLeaves['Privilege Leave'] = response.data.remainingPrivilegeLeave;
                        vm.remainingLeaves['Paternity Leave'] = response.data.remainingPaternityLeave;
                        vm.remainingLeaves['Maternity Leave'] = response.data.remainingMaternityLeave;
                        vm.remainingLeaves['Marriage Leave'] = response.data.remainingMarriageLeave;
                        vm.remainingLeaves['unit'] = response.data.unit;


                        $("#global-progress-display").hide();

                    },
                    function (err) {
                        $("#global-progress-display").hide();
                        if(err.data.message !== null)
                            vm.message = err.data.message;
                       
                    });

                },
                function (err) {
                    $("#global-progress-display").hide();
                    if(err.data.message !== null)
                        vm.message = err.data.message;
                    
                });
               

              
            }
            /*
             * leave type change handling
             */
            vm.typeChanged = function () {
                vm.leaveApplicationData.typeId =  vm.leaveTypeMap[vm.leaveType];
                vm.leaveApplicationData.fromDate = "";
                vm.leaveApplicationData.toDate = "";
                vm.leaveApplicationData.employeeComments = "";
                vm.halfDay = false;
                $("#upload-file-info").html(document.getElementById("rosterInput").files=null)
                vm.invalidSize = false;
            };

            vm.onReset = function(){
                vm.leaveType = "Casual Leave";
                vm.leaveApplicationData.fromDate = "";
                vm.leaveApplicationData.toDate = "";
                vm.leaveApplicationData.employeeComments = "";
                vm.halfDay = false;
                $("#upload-file-info").html(document.getElementById("rosterInput").files=null)

            };

            var handleFileSelect = function(evt) {
                vm.mandtoryFile='';
                vm.validExtension=undefined;
                
                vm.rosterfile = evt.currentTarget.files[0];

                if(vm.rosterfile !== undefined){
                    vm.invalidSize=false;
                    vm.validExtension = (/\.(pdf|jpg|jpeg|png)$/i).test(vm.rosterfile.name);
                }

                $scope.$apply();
                if (!vm.validExtension) {
                    return;
                }
                if(vm.rosterfile.size > 1000000){
                    vm.invalidSize=true;
                  // change   
                    $("#upload-file-info").html(document.getElementById("rosterInput").files=null);
                    vm.fileChoosen = 'No file chosen';
                  
                    // $("#upload-file-info").html(document.getElementById("rosterInput").files[0].name=null)

                    $scope.$apply();
                    return;
                }

                var reader = new FileReader();
                reader.onload = function(evt) {
                    $scope.$apply(function($scope) {
                        vm.tempMedicalCertificate=angular.copy(evt.target.result);
                        vm.myRoster = angular.copy(evt.target.result);
                    });
                };
                reader.readAsDataURL(vm.rosterfile);
            };
            
            angular.element(document.querySelector('#rosterInput')).on('change', handleFileSelect);


            vm.fileValidation = function(){
             
                var fileExtArr = ["pdf","jpg","jpeg", "png"];

                var fileName = document.getElementById("rosterInput").files[0].name;
                vm.fileChoosen = fileName;
             
                var fileExt = fileName.split('.').pop();
              
                if(fileExtArr.indexOf(fileExt) < 0){
                    $("#upload-file-info").html(document.getElementById("rosterInput").files=null)
                    return;
                }
                $("#upload-file-info").html(document.getElementById("rosterInput").files[0].name);
            };

            vm.upload = function(id) {
                // pdfUploadErrMsg = null;
                errMessage = null;
                if(vm.myRoster === undefined|| vm.myRoster === null ){
                    vm.mandtoryFile="Please choose medical certificate"
                }
                else{
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

                    },{'id':id},true).then(function(response) {
                        pdfUpload= true;
                        toasterShow();       
                        vm.myRoster=null;
                        angular.element('#rosterInput').val(null);
                       
                    },
                    function(err) {
                        vm.myRoster=null;
                        angular.element('#rosterInput').val(null);
                        pdfUpload = false;
                        if(err.data.message !== undefined && err.data.message !== null)
                            // pdfUploadErrMsg = err.data.message;
                            errMessage = err.data.message;
                        toasterShow();
                        document.getElementById("upload-file-info").innerHTML = '';
                    });
                }

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

            vm.applyLeave = function(){
                errMessage = null;
                var data = angular.copy(vm.leaveApplicationData);

                data.fromDate = globalFactory.convertDates(vm.leaveApplicationData.fromDate);
                data.toDate = (vm.halfDay == true)?data.fromDate: globalFactory.convertDates(vm.leaveApplicationData.toDate);
                data.dayCount = (vm.halfDay == true)? 0.5: (Math.ceil(Math.abs(new Date(data.fromDate) - new Date(data.toDate))/(1000 * 3600 * 24))+1);

                if (data.dayCount < 0) {
                    errMessage = "From date must be smaller than to date";
                    return;
                }
                var myformdata = new FormData();

                if(vm.myRoster !== null && data.typeId === 2 ){
                    vm.myRoster = vm.dataURItoBlob(vm.myRoster);
                    myformdata.append('fileContent', vm.myRoster,vm.rosterfile.name);
                }

                myformdata.append('fromDateAsString', data.fromDate);
                myformdata.append('toDateAsString', data.toDate);
                myformdata.append('typeId', data.typeId);
                myformdata.append('employeeComments', data.employeeComments);
                myformdata.append('dayCount', data.dayCount);

                var headers = {
                    'Content-Type': undefined
                };
                EncApiService('sendLeaveApplication', {
                    'data': myformdata,
                    'transformRequest': angular.identity,
                    'headers': headers

                },true).then(function(response) {
                    formSubmitted = true;
                    toasterShow();

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

                    vm.validExtension=undefined;
                    vm.leaveApplicationData.typeId =  vm.leaveTypeMap[vm.leaveType];
                    $scope.leaveForm.$setPristine();
                    vm.halfDay = false;

                    vm.deductionInLeaves = response.data;
                },
                function(err) {
                    vm.validExtension=undefined;
                    formSubmitted = false;
                    vm.myRoster = null;
                    if(err.data.message !== undefined && err.data.message!== null)
                        errMessage = err.data.message ;
                    toasterShow();
                    // $("#upload-file-info").html(document.getElementById("rosterInput").files=null);

                });
            }

        

            var pop = function(type,title){
                toaster.pop({
                    type: type,
                    title: title,
                    timeout: 5000
                });
            };

            var toasterShow = function(){
                if(formSubmitted){
                    pop('success','You have successfully applied for the leaves');
                }
                else if((formSubmitted === false || pdfUpload === false )&& errMessage === null){
                    pop('error','Unable to process your request. Please retry or contact system admin');
                }
                else if(pdfUpload){
                    pop('success','You have successfully uploaded medical certificate');
                }
                else{
                    pop('error',errMessage);
                }
            }

            vm.goToPreviousPage = function () {
                if ($stateParams.id != undefined && $stateParams.id!= null && $stateParams.id != '') {
                    if(localStorage.getItem('previousRoute')!='' && localStorage.getItem('previousRoute')!=null && localStorage.getItem('previousRoute')!=undefined)
                        $state.go(localStorage.getItem('previousRoute'));
                    else 
                        $state.go('page.dashboard');
                }
                else{
                    $state.go('page.dashboard');
                }
            }; 

    }]);
})();