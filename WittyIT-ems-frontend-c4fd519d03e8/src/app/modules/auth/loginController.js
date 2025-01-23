/* =========================================================
 * Module: loginController.js
 * Handles login page behaviour
 * =========================================================
 */

 (function () {
    'use strict';
    angular.module('app')
    .controller('loginCtrl', ['authService', '$state', '$stateParams', 'EncApiService','globalFactory','constantValues',function (authService, $state, $stateParams, EncApiService,globalFactory,constantValues) {

        var vm = this;
        vm.constantValues = constantValues;
        vm.message = "";
        vm.isChecked = true;
        vm.formSubmitted = false;
        vm.loginData = {
            UserName : "",
            Password : ""
        };
        vm.changePassData = {
            oldPassword : "",
            newPassword : ""
        };


        vm.togglePassword= function (){
           

            // Checked State
            var checked = vm.isChecked;
            vm.isChecked = !vm.isChecked;

            if(checked){
            // Changing type attribute
            document.getElementById("password").type = 'text';


        }else{
            // Changing type attribute
            document.getElementById("password").type = 'password';

        }

    }

    vm.login = function () {
        if((!vm.loginData.UserName || !vm.loginData.Password) ||vm.loginData.UserName===""||vm.loginData.Password===""){
            vm.info = "Either username or password is invalid";
            vm.flag = false;
            return;

        }

        vm.message = "";
        if( vm.loginData.UserName!=""||vm.loginData.Password!="") {
            authService.login(vm.loginData).then(function (response) {

                            // changes
                            localStorage.setItem('policyAccepted',null);
                            EncApiService('userInfo').then(function(response){
                                var policyAccepted=response.data.policyAccepted;
                                if(policyAccepted==false){
                                    $state.go('page.termsAndConditions');
                                    localStorage.setItem('policyAccepted',false);
                                    
                                }
                                else{
                                    globalFactory.menuTabDisable = false;
                                    $state.go('page.dashboard');
                                }
                            },function(err){

                            });



                            // changes

            }, function (err) {
                vm.message = err;
                if (vm.message.status == 400) {
                    vm.info = "Wrong Credentials";
                } else if (vm.message.status == 401) {
                    if (vm.message.data && vm.message.data.error === "unauthorized") {
                        vm.info = vm.message.data.error_description;
                    } else {
                        vm.info = "Unauthorized";
                    }
                } else {
                    vm.info = "Error on SignIn";
                }
            });
            
        }
    };

    vm.changePassEmpData = {
        empId: "",
        newPassword: ""
    };

    vm.isEmpPassChange = false;
    if ($stateParams.id != undefined && $stateParams.id != '' && $stateParams.id != null) {
        vm.isEmpPassChange = true;

        EncApiService('getIndividualEmployee',null,{'regId':$stateParams.id}).then(function(response) {
            vm.personalData=response.data;
            
        },
        function (err) {});
    }
    vm.changePass = function() {
     if ($stateParams.id != undefined && $stateParams.id != '') {

        vm.changePassEmpData.empId = $stateParams.id;
        vm.changePassEmpData.newPassword = vm.changePassData.newPassword;

        EncApiService('resetPassEmp',{'data': vm.changePassEmpData })
        .then(function(response) {
            vm.formSubmitted = true;
        },
        function(err) {
            vm.formSubmitted = false;
            vm.message = err.data.message;
            console.log(err.data.message);
        });

        
    }
    else {
        EncApiService('changePassword', { 'data': vm.changePassData })
        .then(function(response) {
         vm.message = "";
         vm.formSubmitted = true;
     },
     function(err) {
         vm.formSubmitted = false;
         vm.message = err.data.message;
         console.log(err.data.message);
     });
        
    }
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
}]);
})();



