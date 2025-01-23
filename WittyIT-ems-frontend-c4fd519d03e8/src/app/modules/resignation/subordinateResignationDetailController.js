/* =========================================================
 * Module: subordinateResignationDetailController.js
 * Handles: subprdinateResignationDetailData
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('subordinateResignationDetailCtrl', ['$state', 'authService', 'EncApiService','globalFactory','$stateParams',function ($state, authService, EncApiService,globalFactory,$stateParams) {

            var vm = this;
            $("#global-progress-display").show();

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            vm.user = localStorage.getItem('user').toLowerCase();
            vm.department = localStorage.getItem('department').toLowerCase();
            vm.designationName = localStorage.getItem('designation').toLowerCase();
            vm.logedInUserId  =  JSON.parse(localStorage.getItem('authorizationData')).userInfo.id;


          

            vm.getInitialData = function(){
                var resignationId = $stateParams.id;
                console.log($stateParams.view);
                if($stateParams.view === 'self')
                    vm.self=true;
                else 
                    vm.self=false;
                

                EncApiService('getEmployeeResignationById',null,{'id': resignationId}).then(function(response) {
                    if(response.data.lastDay !== null)
                        response.data.lastDay=globalFactory.convertDates(response.data.lastDay);
                    response.data.resignationDate=globalFactory.convertDates(response.data.resignationDate);
                    vm.managerId = response.data.managerId;
                    console.log(vm.managerId);
                    vm.resignationDetail = response.data;
                    $("#global-progress-display").hide();
                },
                function (err) {
                    vm.message = err.data.message;
                    $("#global-progress-display").hide();
                });
                
            }

            vm.convertDate = function(lastDay){
                
                HrAdminData.lastDay = globalFactory.convertDates(lastDay);
            }

            var HrAdminData =  {
                        "hrPublicNotes": null,
                        "hrPrivateNotes": null,
                        "hrAccepted": null,
                        "lastDay": null,
                        "id": null

                    }

            var ManagerData =  {
                      "managerPublicNotes": null,
                      "managerPrivateNotes":null,
                      "managerAccepted": null,
                      "id": null
                    }



            vm.approve = function(approvalStatus,id,publicNotes,privateNotes){

                if(vm.user === 'admin' || ((vm.department  === 'human resource' || vm.department === 'hr'|| vm.department === 'hr dept' ) 
                && (vm.designationName !== 'hr trainee' && vm.designationName !== 'hr recruiter associate' && vm.designationName !== 'hr generalist associate'))){
                    
                    HrAdminData.hrPublicNotes = publicNotes;
                    HrAdminData.hrPrivateNotes = privateNotes;
                    HrAdminData.hrAccepted = approvalStatus;

                    if(HrAdminData.lastDay === null)
                        HrAdminData.lastDay = globalFactory.convertDates(vm.resignationDetail.lastDay);
                    HrAdminData.id = id;

                    if(privateNotes === undefined || privateNotes === ''){
                            delete HrAdminData.hrPrivateNotes; 
                    }

                    var data = angular.copy(HrAdminData);
                   
                    EncApiService('resignationApprovalHrAdmin',{'data':data}).then(function(response) {
                        $("#global-progress-display").show();
                        vm.getInitialData();
                    },
                    function (err) {
                        vm.message = err.data.message;
                    });
                }
                else{

                    ManagerData.managerPublicNotes = publicNotes;
                    ManagerData.managerPrivateNotes = privateNotes;
                    ManagerData.managerAccepted = approvalStatus;
                    ManagerData.id = id;

                    if(privateNotes === undefined || privateNotes === ''){
                        delete ManagerData.managerPrivateNotes; 
                    }

                    var data = angular.copy(ManagerData);
                    
                    EncApiService('resignationApprovalManager',{'data':data}).then(function(response) {
                        $("#global-progress-display").show();
                        vm.getInitialData();
                    },
                    function (err) {
                        vm.message = err.data.message;
                    });

                }
            }



            vm.goToPreviousPage = function () {
                if(localStorage.getItem('previousRoute')!='' && localStorage.getItem('previousRoute')!=null && localStorage.getItem('previousRoute')!=undefined)
                    $state.go(localStorage.getItem('previousRoute'));
                else 
                    $state.go('page.dashboard');
               
            };


        }]);
})();