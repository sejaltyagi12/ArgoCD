/* =========================================================
 * Module: roasterController.js
 * Handles Roaster page behaviour
 * =========================================================
 */
(function() {
    'use strict';
    angular.module('app')
        .controller('notificationHistoryCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory','$stateParams','DTOptionsBuilder', 'DTColumnDefBuilder',
            function($state ,authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams,DTOptionsBuilder,DTColumnDefBuilder) {

                var vm = this;
                vm.holidayResetData = angular.copy(vm.holidayData);

                jQuery.extend( jQuery.fn.dataTableExt.oSort, {
                    "date-pre": function ( date ) {
                        return moment(date, 'DD/MM/YYYY');
                    }
                });

                jQuery.extend( jQuery.fn.dataTableExt.oSort, {
                    "nullable-asc": function ( a, b ) {
                        return ( a == b ? 0 : ( !a ? 1 : ( !b ? -1 : ( a > b ? 1 : -1 ))));
                    },
                    "nullable-desc": function ( a, b ) {
                        return ( a == b ? 0 : ( !a ? -1 : ( !b ? 1 : ( a > b ? -1 : 1 ))));
                    }
                });
        

                vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
                vm.dtColumnDefs = [
                    DTColumnDefBuilder.newColumnDef(0),
                    DTColumnDefBuilder.newColumnDef([0,6,7]).notSortable(),
                    DTColumnDefBuilder.newColumnDef([2,3,5]).withOption('type', 'date')
                ];

                
                vm.getInitialData = function(){
                     $("#global-progress-display").show();
                    EncApiService('getNotification',null,{'admin':true}).then(function(response) {

                        response.data=response.data.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                        for(var i=0;i<response.data.length;i++){
                            if(response.data[i].actualExpiryDate!=null)
                                response.data[i].actualExpiryDate=globalFactory.convertDates(response.data[i].actualExpiryDate);
                            response.data[i].expiryDate = globalFactory.convertDates(response.data[i].expiryDate);
                        }
                        vm.notificationList=response.data;
                         $("#global-progress-display").hide();
                          
                    },
                    function (err) {

                         $("#global-progress-display").hide();
                     
                    });

                };

                vm.deleteNotification = function(id,index){
                    vm.formSubmitted = null;
                    vm.notificationNum = index + 1;
                    vm.confirmationDelete=true;
                    vm.alertModal();
                    vm.confirmation=function(y){
                        if(y){
                            EncApiService('deleteNotification',null,{'id':id}).then(function(response) {
                                vm.confirmationDelete=false;
                                vm.formSubmitted=true;
                                vm.alertModal();
                                vm.getInitialData();
                            },
                            function (err) {
                                vm.confirmationDelete=false;
                                vm.formSubmitted=false;  
                                vm.message=err.data.message 
                                vm.alertModal();         
                            });

                        }
                    }    
                };

                vm.alertModal= function() {
                    jQuery('#notificationDelete-modal').modal({backdrop: 'static', keyboard: false});
                };
               
            }]);
    })();