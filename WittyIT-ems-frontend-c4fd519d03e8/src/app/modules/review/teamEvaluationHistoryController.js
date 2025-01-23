(function () {
    'use strict';
    angular.module('app')
        .controller('teamEvaluationHistoryCtrl', ['$state','$stateParams', 'authService', 'EncApiService','globalFactory','DTOptionsBuilder', 'DTColumnDefBuilder', function ($state,$stateParams, authService, EncApiService,globalFactory,DTOptionsBuilder, DTColumnDefBuilder) {

            var vm = this;
            $("#global-progress-display").show();
            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);
            
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
                  DTColumnDefBuilder.newColumnDef([0,8,9]).notSortable(),
                  DTColumnDefBuilder.newColumnDef([3,4,5,6]).withOption('type', 'date')
            ];

            EncApiService('getCountOfSubmittedEvaluations').then(function(response) {
              globalFactory.reviewCount = response.data;
               
            },
            function (err){

            });

            EncApiService('getEvaluationCycleListForTeamEvaluation').then(function(response) {
                response.data=response.data.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                for(var i=0;i<response.data.length;i++){
                    response.data[i].startDate=globalFactory.sortDateFormat(response.data[i].startDate)
                    response.data[i].endDate=globalFactory.sortDateFormat(response.data[i].endDate)
                    response.data[i].employeeEndDate=globalFactory.sortDateFormat(response.data[i].employeeEndDate)
                    response.data[i].managerEndDate=globalFactory.sortDateFormat(response.data[i].managerEndDate)
                }

                vm.reviewDetailsResponse=response.data;
                $("#global-progress-display").hide();

            },
            function (err) {
                $("#global-progress-display").hide();
                vm.message = err.data.message;
            });

        }]);
})();