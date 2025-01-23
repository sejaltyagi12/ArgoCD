/* =========================================================
 * Module: feedbackController.js
 * Handles feedback page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('selfResignationCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder','globalFactory','$stateParams','$scope', function ($state, authService, EncApiService, DTOptionsBuilder, DTColumnDefBuilder,globalFactory,$stateParams,$scope) {

            var vm = this;
            $("#global-progress-display").show();

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);
            

            vm.resignData={
                resignationText:null,
            };

            vm.resignDataReset = angular.copy(vm.resignData);


            vm.initialData=function(){
                EncApiService('userInfo').then(function(response) {
                    var id=response.data.id;
                    EncApiService('getEmployeeResignation',null,{'emp': id}).then(function(response) {
                        response.data=response.data.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                        for(var i=0;i<response.data.length;i++){
                            response.data[i].resignationDate=globalFactory.convertDates(response.data[i].resignationDate);
                        }
                        vm.selfResignationDetail=response.data
                        $("#global-progress-display").hide();
                      
                    },
                    function (err) {
                        $("#global-progress-display").hide();
                        vm.message = err.data.message;
                    });
                },
                function (err) {
                    vm.message = err.data.message;
                    $("#global-progress-display").hide();
                });

              
            }



           vm.resignSubmit = function () {
                EncApiService('submitEmployeeResignation', { 'data': vm.resignData }).then(function(response) {
                    vm.formSubmitted=true;
                    vm.alertModal();
                    vm.initialData();
                    vm.resignData = angular.copy(vm.resignDataReset);
                    $scope.resignForm.$setPristine();

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