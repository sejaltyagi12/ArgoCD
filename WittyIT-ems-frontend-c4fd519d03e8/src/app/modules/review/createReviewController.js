(function () {
    'use strict';
    angular.module('app')
        .controller('createReviewCtrl', ['$state','$stateParams', 'authService', 'EncApiService','globalFactory','$scope', function ($state,$stateParams, authService, EncApiService,globalFactory,$scope) {

            var vm = this;

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);
            var deptId=localStorage.getItem('deptId');
           

            vm.formSubmitted = null;
            var CycleInitiated=null;

            vm.createReview= {

                from: null,
                to:null,
                endDateEmployee:null,
                endDateManager:null
            };

            // changes
            vm.departmentErr='';
            vm.departments = [];
            vm.departmentData= [];
            vm.getInitialData=function(){
                EncApiService('fetchDepartmentList').then(function(response) {
                        var out = [];
                        for (var key in response.data) {
                            if (response.data.hasOwnProperty(key)) {
                                out.push({
                                    'id': response.data[key].deptId,
                                    'sId': response.data[key].id,
                                    'deptName': response.data[key].deptName,
                                });
                            }
                        }
                        out.sort(function(a, b){
                            var nameA=a.deptName.toLowerCase(), nameB=b.deptName.toLowerCase()
                            if (nameA < nameB) //sort string ascending
                                return -1 
                            if (nameA > nameB)
                                return 1
                            return 0 //default return value (no sorting)
                        })
                        vm.departmentData = out;

                },
                function (err) {
                });

            }

            vm.checkingDepartment=function(x){
                if(x.length > 0)
                    vm.departmentErr='';

            }
            vm.settings = {
                checkBoxes: true, displayProp: 'deptName'
            };
           
            vm.initiateReview = function () {
                if(vm.departments.length>0){
                    vm.createReview.fromCopy = angular.copy(vm.createReview.from);
                    vm.createReview.toCopy = angular.copy(vm.createReview.to);
                    vm.createReview.from = globalFactory.convertDates(vm.createReview.from);
                    vm.createReview.to= globalFactory.convertDates(vm.createReview.to);
                    vm.createReview.endDateEmployee= globalFactory.convertDates(vm.createReview.endDateEmployee);
                    vm.createReview.endDateManager= globalFactory.convertDates(vm.createReview.endDateManager);
                    var data={};
                    data.startDate=vm.createReview.from;
                    data.endDate=vm.createReview.to;
                    data.employeeEndDate=vm.createReview.endDateEmployee;
                    data.managerEndDate=vm.createReview.endDateManager;

                    data.departments=[];
                    for(var i=0; i<=vm.departments.length-1 ; i++){
                        if(deptId==vm.departments[i].id)
                            CycleInitiated=true;
                        data.departments.push(vm.departments[i].id);
                    } 

                    EncApiService('initiateEvaluation', { 'data': data }).then(function(response) {
                        vm.formSubmitted=true;
                        if(CycleInitiated!=null)
                            globalFactory.newCycleInitiated = CycleInitiated; 
                        vm.alertModal();
                        document.getElementById("createReviewForm").reset();
                        $scope.createReviewForm.$setPristine();
                        vm.departments=null;
                       
                    },
                    function (err) {
                        CycleInitiated=null;
                        vm.formSubmitted=false;
                        vm.message = err.data.message;
                        vm.alertModal();
                        document.getElementById("createReviewForm").reset();
                        $scope.createReviewForm.$setPristine();
                        vm.departments=null;
                    });

               } 
                else{
                    vm.departmentErr="Please select department(s)";
                }
                
            };

            vm.alertModal= function() {
                jQuery('#initiateReview-modal').modal({backdrop: 'static', keyboard: false});
            };


            vm.goToPreviousPage = function () {
                var previousRoute = localStorage.getItem('previousRoute');
                globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
                $state.go(globalFactory.previousRoute);
            };


        }]);
})();