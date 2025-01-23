/* =========================================================
 * Module: profileController.js
 * Handles: profile page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('profileCtrl', ['$state', 'authService','EncApiService','globalFactory','$stateParams','$scope','apiEndpoints','$http','$q', function ($state, authService,EncApiService,globalFactory,$stateParams,$scope,apiEndpoints,$http,$q) {

            var vm = this;
            $("#global-progress-display").show();

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            vm.initialData = function(){

                EncApiService('userInfo').then(function(response) {
                    vm.userInfo = response.data;
                },
                function (err) {
                    $("#global-progress-display").hide();
                   
                });

                
            };

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