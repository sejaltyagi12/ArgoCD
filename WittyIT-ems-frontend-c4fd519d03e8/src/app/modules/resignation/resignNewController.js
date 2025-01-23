/* =========================================================
 * Module: resignNewController.js
 * Handles: resignNew page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('resignNewCtrl', ['$state', 'authService','EncApiService','globalFactory','$stateParams','$scope','apiEndpoints','$http','$q', function ($state, authService,EncApiService,globalFactory,$stateParams,$scope,apiEndpoints,$http,$q) {

            var vm = this;
            $("#global-progress-display").show();

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);
            

            vm.resignData = {
                // resignationType: null,
                typeId:null,
                // resignationReason:null,
                reasonId:null,
                resignationText:null
            };

            vm.resignDataReset = angular.copy(vm.resignData);

            var allApiConfig = [
              'userInfo',
              'resignationTypes',
              'resignationReason',
            ];

            var allFiltersData = [];

            vm.initialData = function(){

                EncApiService('getCurrentResignation').then(function(response) {
                    // vm.disabled = response.data.length;
                    if (response.data && response.data.length > 0) {
                        // Access properties only if data is available
                        vm.disabled = new Date(response.data[0].joiningDate) < new Date(response.data[0].resignationDate);
                    }
                    

                    for (var count=0; count<allApiConfig.length; count++) {
                        var apiConfig = angular.copy(apiEndpoints[allApiConfig[count]]);
                        apiConfig.url = globalFactory.serverUrl + '/' + apiConfig.url;
                        allFiltersData.push($http(apiConfig));
                    }


                    if(response.data.length > 0){
                        $('#you').removeClass('step-you');
                        $('#you').addClass('approved');


                        // FOR HR STATUS
                        if(response.data[0].hrStatus !== "PENDING" ){
                            if(response.data[0].hrStatus === 'APPROVED'){
                                $('#hr').removeClass('step-hr');
                                $('#hr').addClass('approved'); 

                            }
                            else{
                                $('#hr').removeClass('step-hr');
                                $('#hr').addClass('rejected');
                            }
                        }

                        // FOR MANAGER STATUS
                        if(response.data[0].managerStatus !== 'PENDING'){
                            if(response.data[0].managerStatus === 'APPROVED'){
                                $('#manager').removeClass('step-manager');
                                $('#manager').addClass('approved');
                            }
                            else{
                                $('#manager').removeClass('step-manager');
                                $('#manager').addClass('rejected');
                            }
                        }


                        $q.all(allFiltersData).then(function (response) {
                            var userInfo = response[0].data;
                            vm.resignationTypes = response[1].data;
                            vm.resignationReason = response[2].data;
                            $("#global-progress-display").hide();
                        }, 
                        function (error) {
                            $("#global-progress-display").hide();
                        });
                        vm.currentResignationData = response.data[0];

                        vm.EmployeeName = response.data[0].employeeName + ' ' +'(' + response.data[0].employeeCode + ')';
                        vm.resignData.typeId = response.data[0].type.id;
                        vm.resignData.reasonId = response.data[0].reason.id;
                        vm.resignData.resignationText = response.data[0].resignationText;
                        vm.resignationDate = globalFactory.convertDates(response.data[0].resignationDate);
                        vm.lastDay = globalFactory.convertDates(response.data[0].lastDay);

                        // vm.managerPublicNotes = response.data[0].managerPublicNotes;
                        // vm.hrPublicNotes = response.data[0].hrPublicNotes;


                    }
                    else{

                        $q.all(allFiltersData).then(function (response) {
                            var userInfo = response[0].data;


                            // # for getting Resignation Date & Last day in Company
                            var noticePeriodDays=userInfo.noticePeriod;
                            var today = new Date();
                            vm.resignationDate = globalFactory.convertDateString(today);
                            var lastDay = today.setDate(today.getDate() + noticePeriodDays); 
                            var lastDay = new Date(lastDay);
                            vm.lastDay = globalFactory.convertDateString(lastDay);

                            // # for getting Employee's Name
                            if(userInfo.middleName!==null && typeof userInfo.middleName != 'undefined')
                                vm.EmployeeName=userInfo.firstName + ' ' + userInfo.middleName + ' ' + userInfo.lastName + ' ' + '(' + userInfo.empCode + ')';
                            else
                                vm.EmployeeName=userInfo.firstName + ' ' + userInfo.lastName + ' ' + '(' + userInfo.empCode + ')';


                            vm.resignationTypes = response[1].data;
                            vm.resignData.typeId = vm.resignationTypes[0].id;
                            // vm.resignData.resignationType = vm.resignationTypes[0].type;

                            vm.resignationReason = response[2].data;
                            vm.resignData.reasonId = vm.resignationReason[0].id;
                            // vm.resignData.resignationReason = vm.resignationReason[0].type;


                            $("#global-progress-display").hide();
                        }, 
                        function (error) {
                            $("#global-progress-display").hide();
                        });


                    }
                  
                },
                function (err) {
                    $("#global-progress-display").hide();
                   
                });

                
            };

            // vm.dropDownChanged = function (regDataAttr, dataAttr, vmDataAttr) {
            //     for (var count=0; count<vm[vmDataAttr].length; count++)
            //         if (vm[vmDataAttr][count]['id'] == vm.resignData[dataAttr])
            //             vm.resignData[vmDataAttr] = vm[vmDataAttr][count][regDataAttr];
            // };

           vm.resignSubmit = function () {
                EncApiService('submitEmployeeResignation', {'data': vm.resignData }).then(function(response) {
                    $('#you').removeClass('step-you');
                    $('#you').addClass('approved');
                    vm.formSubmitted=true;
                    vm.alertModal();
                    vm.currentResignationData=response.data;
                    vm.disabled = 1;
                },
                function (err) {
                    vm.formSubmitted=false;
                    vm.message = err.data.message;
                    vm.alertModal();
                });
            }


            vm.alertModal= function() {
                jQuery('#selfResignation-modal').modal();
            };



        }]);
})();