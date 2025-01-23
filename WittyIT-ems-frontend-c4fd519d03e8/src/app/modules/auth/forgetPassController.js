/* =========================================================
 * Module: forgetPassController.js
 * Handles forget password page behaviour
 * =========================================================
 */

 (function () {
    'use strict';
    angular.module('app')
    .controller('forgetPassCtrl', ['authService', '$state', '$stateParams','EncApiService','$location', '$scope','constantValues',function (authService, $state, $stateParams,EncApiService,$location,$scope,constantValues) {

        var vm = this;
        vm.constantValues = constantValues;
        var data = {};
        var resetPassData = {};
        vm.changePassData = {};
        
        vm.initialiseChangePass = function(){
            resetPassData.officialEmail = $location.search().email;
            resetPassData.token =  $location.search().token;
        };

        vm.initialiseChangePass();
        vm.changePass = function(){
           $("#global-progress-display").show();

           resetPassData.password = vm.changePassData.newPassword;
           EncApiService('forgetPassReset', { 'data': resetPassData }).then(function (response) {
            $("#global-progress-display").hide();
            vm.formSubmitted=true;
            vm.alertModal();
        },
        function (err) {
            $("#global-progress-display").hide();
            vm.formSubmitted=false;
            vm.message=err.data.message;
            vm.alertModal();
        });
       };
       vm.sendMail = function(){
        $("#global-progress-display").show();
        data.officialEmail = vm.officialMail;

        EncApiService('sendForgetPassMail', { 'data': data }).then(function (response) {
            $("#global-progress-display").hide();
            vm.formSubmitted=true;
            vm.alertModal();
            vm.officialMail=null;
            $scope.forgetPassForm.$setPristine();

        },
        function (err) {
            $("#global-progress-display").hide();
            vm.formSubmitted=false;
            vm.message=err.data.message;
            vm.alertModal();
        });
    };

    vm.goToPreviousPage = function () {

        if ($stateParams.id != undefined && $stateParams.id != '') {
            if (globalFactory.previousRoute == '') {
                var previousRoute = localStorage.getItem('previousRoute');
                globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
            }
            $state.go(globalFactory.previousRoute);
        }
        else{
          $state.go('page.dashboard');
      }

  };

  vm.alertModal= function() {
    jQuery('#forgetPass-modal').modal({backdrop: 'static', keyboard: false});
};

}]);
})();



