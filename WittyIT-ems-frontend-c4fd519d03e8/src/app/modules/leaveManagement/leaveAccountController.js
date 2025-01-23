/* =========================================================
 * Module: leaveAccountController.js
 * Handles leave account page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('leaveAccountCtrl', ['$timeout','$state','$stateParams', 'authService', 'EncApiService','globalFactory','$window', function ($timeout,$state,$stateParams, authService, EncApiService,globalFactory,$window) {
            $("#global-progress-display").show();
            var vm = this;

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            vm.isIndividual = true;
            if ($stateParams.empId != undefined && $stateParams.empId != '' && $stateParams.empId !=null) 
                vm.isIndividual= false;

            vm.goToPreviousPage = function () {
                if ($stateParams.empId != undefined && $stateParams.empId != null && $stateParams.empId != '') {
                    if(localStorage.getItem('previousRoute')!='' && localStorage.getItem('previousRoute')!=null && localStorage.getItem('previousRoute')!=undefined)
                        $state.go(localStorage.getItem('previousRoute'));
                    else 
                        $state.go('page.dashboard');
                
                }
                else{
                    $state.go('page.dashboard');
                }
            };
            vm.alertModal= function() {
                jQuery('#resign-modal2').modal();
            };
            vm.closeModal = function(x) {
                $timeout(function(){
                    vm.goToPreviousPage();
                },500);
              
         };

            

            /*
             * Gets leave Status
             */
             if ($stateParams.empId != undefined && $stateParams.empId != '') {
                EncApiService('employeeLeaveDetail',null,{'id':$stateParams.empId}).then(function(response) {
                    vm.cancelButton=true;
                    vm.leaves = response.data;
                    EncApiService('getIndividualEmployee',null,{'regId':$stateParams.empId}).then(function(response) {
                        vm.personalData=response.data;
                        $("#global-progress-display").hide();
                    },
                    function (err) {
                        $("#global-progress-display").hide();
                    });
                },
                function (err)
                {   $("#global-progress-display").hide();
                vm.message=err.data.message;
                vm.alertModal();
                    
                });

               
             }
             else{
                EncApiService('employeeLeaveDetail',null,{'id':''}).then(function(response) {
                    $("#global-progress-display").hide();
                    vm.leaves = response.data;
                },
                function (err) {
                    $("#global-progress-display").hide();
                    if(err.data.message !== undefined && err.data.message !== null && err.data.message !== '')
                        vm.message = err.message;
                });
            }
           

           

        }]);
})();