/* =========================================================
 * Module: leaveApplicationController.js
 * Handles Leave Application page behaviour
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .controller('reportCtrl', ['$state','$scope', 'authService', 'EncApiService', 'globalFactory', '$timeout', '$stateParams','FileSaver','Blob',function ($state,$scope, authService, EncApiService, globalFactory, $timeout,$stateParams,FileSaver,Blob) {

            var vm = this;
            // vm.message = '';
            /*
             * keeps track if application for leave was successful or not
             */
            // vm.formSubmitted = null;

            vm.reportForm= {
                fromDate: null,
                toDate: null,
            };

            vm.initial=function(){
                vm.reportType=$stateParams.type;
            }


            if ($stateParams.type!= undefined && $stateParams.type != '') {
                
                if($stateParams.type=='leave'){
                    vm.downloadReport=function(startDate,endDate){
                        vm.showLoadingButton=true
                        var startDate = globalFactory.convertDates(startDate).toString().replace(/\//g,"-");
                        var endDate = globalFactory.convertDates(endDate).toString().replace(/\//g,"-");
                       
                        EncApiService('downloadLeaveReport', { responseType: 'arraybuffer' },{'start':startDate,'end':endDate}).then(function(response) {
                            var blob = new Blob([response.data],{type: 'application/pdf;charset=utf-8'});
                            var fileName ="Leave Report" + ".xls";
                            FileSaver.saveAs(blob, fileName);
                            vm.showLoadingButton=false;
                          
                            vm.formSubmitted = true;
                          
                            document.getElementById("reportForm").reset();
                           
                            $scope.reportForm.$setPristine();

                        },
                        function(err) {
                            vm.showLoadingButton=false;
                            vm.formSubmitted = false;
                            vm.message = err.data.message;
                            
                        });


                    }
                    

                }
                else{
                    vm.downloadReport=function(startDate,endDate){
                        vm.showLoadingButton=true
                        var startDate = globalFactory.convertDates(startDate).toString().replace(/\//g,"-");
                        var endDate = globalFactory.convertDates(endDate).toString().replace(/\//g,"-");
                       
                        EncApiService('downloadJoineeReport', { responseType: 'arraybuffer' },{'start':startDate,'end':endDate}).then(function(response) {
                            var blob = new Blob([response.data],{type: 'application/pdf;charset=utf-8'});
                            var fileName ="Joinee Report" + ".xls";
                            FileSaver.saveAs(blob, fileName);
                            vm.showLoadingButton=false;
                           
                            vm.formSubmitted = true;
                            document.getElementById("reportForm").reset();
                            vm.reportForm.fromDate = null;
                            vm.reportForm.toDate = null;
                            
                            $scope.reportForm.$setPristine();

                        },
                        function(err) {
                            vm.showLoadingButton=false;
                            vm.formSubmitted = false;
                            vm.message = err.data.message;

                        });


                    }
                    

                }

            }
                


    }]);
})();


