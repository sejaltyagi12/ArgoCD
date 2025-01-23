/* =========================================================
 * Module: viewCurrentBreakdownController.js
 * Handles: viewCurrentBreakdown page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('viewCurrentBreakdownCtrl', ['$state', 'authService','EncApiService','$window', 'globalFactory',function ($state, authService, EncApiService, $window, globalFactory ) {

            var vm = this;
            $("#global-progress-display").show();
            vm.getInitialData = function(){

                EncApiService('currentBreakDown').then(function(response) {
                    vm.formSubmitted = true;
                    vm.currentBreakDown = response.data;
                    $("#global-progress-display").hide();
                },
                function (err) {
                    $("#global-progress-display").hide();
                    vm.formSubmitted = false;
                    if(err.data.message !== undefined)
                        vm.message = err.data.message;
                    toasterShow();
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
                    return;
                }
                if(formSubmitted === false && (vm.message === null || vm.message === undefined)){
                    pop('error','Unable to process your request. Please retry or contact system admin');
                }
                else{
                    pop('error',vm.message);
                }
            }
         

        }]);
})();