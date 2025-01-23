(function() {
    'use strict';
    angular.module('app')
        .controller('notificationCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory','$stateParams','$uibModal',function($state ,authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams,$uibModal) {
            var vm = this;
            
            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            vm.notification={
                from:null,
                text:null,
                expiryDate:null
            }
            var notificationReset=angular.copy(vm.notification);

            vm.notificationSubmit=function(){

                var data={
                    text:null,
                    expiryDate:null
                }
                data.text=vm.notification.text+' '+'-'+ ' '+vm.notification.from;
                data.expiryDate=globalFactory.convertDates(vm.notification.expiryDate);
            
                EncApiService('notificationSend', {'data':data}).then(function(response) {
                    vm.formSubmitted=true;
                    vm.alertModal();
                    vm.notification=notificationReset;
                    $scope.notificationForm.$setPristine();
                  //  vm.formValueReset();
                  
                },
                function(err) {
                    vm.formSubmitted=false;
                    vm.message=err.data.message;
                    vm.alertModal();

                });

            }
            // vm.formValueReset = function(){
            //     vm.notification = null;
            // }

            vm.goToPreviousPage = function () {
                if(globalFactory.previousRoute == ''){
                    var previousRoute = localStorage.getItem('previousRoute');
                    globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
                }
                $state.go(globalFactory.previousRoute);
             
            };

            vm.alertModal= function() {
                jQuery('#notification-modal').modal({backdrop: 'static', keyboard: false});

            };

        }]);
})();


