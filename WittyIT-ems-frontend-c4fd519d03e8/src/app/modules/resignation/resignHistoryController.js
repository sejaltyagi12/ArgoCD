/* =========================================================
 * Module: leaveDetailsController.js
 * Handles leave Details page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('resignHistoryCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder','globalFactory',function ($state, authService, EncApiService,DTOptionsBuilder, DTColumnDefBuilder,globalFactory) {

            var vm = this;
            $("#global-progress-display").show();

            // if (globalFactory.previousRoute !== '')
            //     localStorage.setItem('previousRoute', globalFactory.previousRoute);

            if(globalFactory.previousRoute!='page.subordinateResignationDetail' && globalFactory.previousRoute !== '' )
                localStorage.setItem('previousRoute', globalFactory.previousRoute);
            else 
                localStorage.setItem('previousRoute','page.selfResignation');

            vm.user = localStorage.getItem('user');
            vm.department = localStorage.getItem('department');

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
                DTColumnDefBuilder.newColumnDef([0,10]).notSortable(),
                DTColumnDefBuilder.newColumnDef([4,5]).withOption('type', 'date')
            ];

            vm.getInitialData = function(){

                EncApiService('userInfo').then(function(response) {
                    var empId=response.data.id;
                    EncApiService('getEmployeeResignation',null,{'emp': empId}).then(function(response) {
                        response.data=response.data.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);});
                        for(var i=0;i<response.data.length;i++){
                            if(response.data[i].resignationDate !== null)
                                response.data[i].resignationDate=globalFactory.convertDates(response.data[i].resignationDate);
                            if(response.data[i].lastDay !== null)
                            response.data[i].lastDay=globalFactory.convertDates(response.data[i].lastDay);
                        }

                        vm.selfResignationData = response.data;
                        $("#global-progress-display").hide();
                    },
                    function (err) {
                        vm.message = err.data.message;
                        $("#global-progress-display").hide();
                    });
                   
                },
                function (err) {
                    if(err !== undefined)
                        vm.message = err.data.message;
                    $("#global-progress-display").hide();
                });

            }

          
            vm.goToPreviousPage=function(){
                var previousRoute = localStorage.getItem('previousRoute');
                globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
                $state.go(globalFactory.previousRoute);
            }
           

        }]);
})();