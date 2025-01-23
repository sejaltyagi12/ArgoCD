/* =========================================================
 * Module: feedbackController.js
 * Handles feedback page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('feedbackCtrl', ['$state', 'authService', 'EncApiService', 'DTOptionsBuilder', 'DTColumnDefBuilder', 'globalFactory', '$stateParams', '$scope', function ($state, authService, EncApiService, DTOptionsBuilder, DTColumnDefBuilder, globalFactory, $stateParams, $scope) {

            var vm = this;
            vm.departments = [];



            vm.isIndividual = true;
            if ($stateParams.id != undefined && $stateParams.id != '' && $stateParams.id != null)
                vm.isIndividual = false;


            vm.feedback = {
                subject: null,
                departmentName: null,
                managerShared: false,
                description: null,
            };
            vm.feedbackDataReset = angular.copy(vm.feedback);

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);



            EncApiService('fetchDepartmentList').then(function (response) {
                for (var i = 0; i < response.data.length; i++) {
                    vm.departments.push(response.data[i].deptName);
                }
                vm.departments.push("General");
            },
                function (err) {
                    vm.message = err.message;
                });



            vm.feedbackSubmit = function () {
                var data = vm.feedback;

                EncApiService('postIdeaSuggestion', { 'data': data }).then(function (response) {
                    vm.formSubmitted = true;
                    vm.alertModal();
                    vm.feedback = angular.copy(vm.feedbackDataReset);
                    $scope.feedbackForm.$setPristine();

                },
                    function (err) {
                        vm.formSubmitted = false;
                        vm.message = err.data.message;
                        vm.alertModal();
                    });
            }

            if ($stateParams.id != undefined && $stateParams.id != '' && $stateParams.id != null) {
                vm.view = true

                EncApiService('ideaById', null, { 'id': $stateParams.id }).then(function (response) {

                    vm.feedback = response.data;
                },
                    function (err) {

                    });

            }

            vm.goToPreviousPage = function () {

                if ($stateParams.id != undefined && $stateParams.id != '' && $stateParams.id != null || $stateParams.id == '') {
                    if (globalFactory.previousRoute == '') {
                        var previousRoute = localStorage.getItem('previousRoute');
                        globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null) ? previousRoute : 'page.dashboard';
                    }
                    $state.go(globalFactory.previousRoute);
                }
                else {
                    $state.go('page.dashboard');
                }
            };


            vm.alertModal = function () {
                jQuery('#item-modal').modal();
            };



        }]);
})();