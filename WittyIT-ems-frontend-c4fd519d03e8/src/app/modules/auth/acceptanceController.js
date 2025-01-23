(function () {
    'use strict';
    angular.module('app')
    .controller('acceptanceCtrl', ['authService', '$state', '$stateParams', 'EncApiService','globalFactory','$timeout', function (authService, $state, $stateParams, EncApiService,globalFactory,$timeout) {

        var vm = this;
        vm.tick=false;
        vm.tickMessage='';

        vm.accept=function(){
            if(vm.tick==false){
                vm.tickMessage="Please accept terms and conditions"

            }
            else{
                var data={
                    policyAccepted: true,
                }
                EncApiService('employeeAcceptPolicy', { 'data': data }).then(function(response) {
                    vm.formSubmitted=true;
                    if(localStorage.getItem('policyAccepted')==='false'){
                        localStorage.setItem('policyAccepted',true);
                            // globalFactory.disableMenu=false;
                            vm.alertModal();
                        }

                    },
                    function (err) {
                        vm.formSubmitted=false;
                        if(err.data.message!=undefined)
                            vm.message=err.data.message;

                    });
            }
        }

        vm.tickfunction=function(){
            if(vm.tick)
                vm.tickMessage='';
        }

        vm.alertModal= function() {
            jQuery('#policyAcceptance-modal').modal({backdrop: 'static', keyboard: false});
        };

        vm.dashboardPage=function(){
            if(vm.formSubmitted==true){
                    $timeout(function(){
                        $('#list1').trigger('click');
                        globalFactory.disableMenu=false;
                    }, 100);

                }

            }
            


            

        }]);
})();



