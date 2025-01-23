/* =========================================================
 * Module: roasterController.js
 * Handles Roaster page behaviour
 * =========================================================
 */
(function () {
    'use strict';
    angular.module('app')
        .controller('departmentCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory', '$stateParams', 'DTOptionsBuilder', 'DTColumnDefBuilder', 'toaster',
            function ($state, authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams, DTOptionsBuilder, DTColumnDefBuilder, toaster) {

                var vm = this;
                vm.invalidSize = false;
                vm.formSubmitted = null;
                vm.message = '';
                vm.departmentData = {
                    "deptId": null,
                    "deptName": null
                };


                if (globalFactory.previousRoute !== '')
                    localStorage.setItem('previousRoute', globalFactory.previousRoute);



                /*
                * Gets data in case of edit
                */

                if ($stateParams.deptId != undefined && $stateParams.id != '') {
                    EncApiService('getDepartment', null, { 'deptId': $stateParams.deptId }).then(function (response) {
                        vm.departmentData = response.data;

                    },
                        function (err) {
                        });
                }



                vm.addDepartment = function () {

                    var data = angular.copy(vm.departmentData);

                    EncApiService('addDepartment', { 'data': data }).then(function (response) {
                        vm.formSubmitted = true;
                        vm.alertModal();

                    },
                        function (err) {
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();
                        });
                }

                vm.updateDepartment = function () {

                    var data = angular.copy(vm.departmentData);

                    EncApiService('updateDepartment', { 'data': data }, { 'deptId': data.deptId }).then(function (response) {
                        vm.formSubmitted = true;
                        vm.alertModal();

                    },
                        function (err) {
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();
                        });
                }


                vm.deleteDepartment = function (deptId, index) {

                    EncApiService('deleteDepartment', null, { 'deptId': deptId }).then(function (response) {

                        vm.departmentList.splice(index, 1);
                    },
                        function (err) {

                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();

                        });
                };

                vm.getDepartmentList = function () {
                    $("#global-progress-display").show();

                    EncApiService('departmentList', null).then(function (response) {
                        vm.departmentList = response.data;

                        $("#global-progress-display").hide();

                    },
                        function (err) {
                            $("#global-progress-display").hide();
                        });

                };


                vm.goToPreviousPage = function () {
                    if (globalFactory.previousRoute == '') {
                        var previousRoute = localStorage.getItem('previousRoute');
                        globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null) ? previousRoute : 'page.dashboard';
                    }
                    $state.go(globalFactory.previousRoute);
                }

                vm.alertModal = function () {
                    jQuery('#department-modal').modal({ backdrop: 'static', keyboard: false });
                };

            }]);
})();