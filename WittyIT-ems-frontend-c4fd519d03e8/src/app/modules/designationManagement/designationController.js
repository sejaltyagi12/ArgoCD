/* =========================================================
 * Module: roasterController.js
 * Handles Roaster page behaviour
 * =========================================================
 */
(function () {
    'use strict';
    angular.module('app')
        .controller('designationCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory', '$stateParams', 'DTOptionsBuilder', 'DTColumnDefBuilder',
            function ($state, authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams, DTOptionsBuilder, DTColumnDefBuilder) {

                var vm = this;
                vm.departments = [];
                vm.invalidSize = false;
                vm.formSubmitted = null;
                vm.message = '';
                vm.designationData = {
                    "designationId": null,
                    "designation": null,
                    "level": null,
                    "deptId": null
                };


                if (globalFactory.previousRoute !== '')
                    localStorage.setItem('previousRoute', globalFactory.previousRoute);


                /*
                * Gets data in case of edit
                */

                if ($stateParams.designationId != undefined && $stateParams.designationId != '') {
                    EncApiService('getDesignation', null, { 'designationId': $stateParams.designationId }).then(function (response) {
                        vm.designationData = response.data;

                    },
                        function (err) {
                        });
                }

                vm.getDesignationList = function () {
                    $("#global-progress-display").show();

                    EncApiService('designationList', null).then(function (response) {
                        vm.designationList = response.data;

                        $("#global-progress-display").hide();

                    },
                        function (err) {
                            $("#global-progress-display").hide();
                        });

                };

                vm.addDesignation = function () {

                    var data = angular.copy(vm.designationData);

                    EncApiService('addDesignation', { 'data': data }).then(function (response) {
                        vm.formSubmitted = true;
                        vm.alertModal();

                    },
                        function (err) {
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();
                        });
                }

                vm.updateDesignation = function () {

                    var data = angular.copy(vm.designationData);

                    EncApiService('updateDesignation', { 'data': data }, { 'designationId': data.designationId }).then(function (response) {
                        vm.formSubmitted = true;
                        vm.alertModal();

                    },
                        function (err) {
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();
                        });
                }

                vm.deleteDesignation = function (designationId, index) {

                    EncApiService('deleteDesignation', null, { 'designationId': designationId }).then(function (response) {

                        vm.designationList.splice(index, 1);
                    },

                        function (err) {
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            vm.alertModal();
                        });
                };

                vm.getInitialData = function () {
                    EncApiService('departmentList').then(function (response) {
                        vm.departmentList = response.data;
                    },
                        function (err) {

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
                    jQuery('#designation-modal').modal({ backdrop: 'static', keyboard: false });
                };

            }]);
})();