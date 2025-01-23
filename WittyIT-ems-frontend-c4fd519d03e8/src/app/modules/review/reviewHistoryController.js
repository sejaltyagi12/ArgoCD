(function () {
    'use strict';
    angular.module('app')
        .controller('reviewHistoryCtrl', ['$state','$stateParams', 'authService', 'EncApiService','globalFactory','DTOptionsBuilder', 'DTColumnDefBuilder','$uibModal','FileSaver','Blob',function ($state,$stateParams, authService, EncApiService,globalFactory,DTOptionsBuilder, DTColumnDefBuilder,$uibModal,FileSaver,Blob) {

            var vm = this;
            var selfCycleId=null;
            $("#global-progress-display").show();
           
            
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
            vm.initialData=function(){
                EncApiService('getEvaluationCycleList',null,{'dept':''}).then(function(response) {
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
                   vm.message = err.message;
                });
                EncApiService('evaluationCycleCheck').then(function(response){
                    selfCycleId=response.data.id;           
                },
                function(err){

                });
            }

            vm.endEvaluation=function(x){
                vm.formSubmitted=null;
                vm.message == undefined;
                vm.confirmationDelete=true;
                vm.alertModal();
                vm.confirmation=function(y){
                    if(y){
                        EncApiService('endEvaluationCycle',null,{'id':x,}).then(function(response) {
                            if(selfCycleId==x)
                                globalFactory.newCycleInitiated = false; 
                            
                            vm.confirmationDelete=false;
                            vm.formSubmitted=true;
                            vm.alertModal();
                            vm.initialData();
                        },
                        function (err) {
                            vm.confirmationDelete=false;
                            vm.formSubmitted=false;  
                            vm.message=err.data.message 
                            vm.alertModal();         
                        });

                    }
                }
            }
            vm.showLoadingButton=[];
            vm.download=function(index,cycleId){
                vm.showLoadingButton[index]=true;   
                EncApiService('evaluationReportDownload', { responseType: 'arraybuffer' },{'cycleId':cycleId}).then(function(response) {
                    var blob = new Blob([response.data],{type: 'application/pdf;charset=utf-8'});
                    var fileName ="Evaluation Report" + ".xls";
                    FileSaver.saveAs(blob, fileName);
                    vm.showLoadingButton[index]=false;
                },
                function(err) {
                    vm.showLoadingButton[index]=false;
                });

            }


            vm.alertModal= function() {
                jQuery('#reviewHistory-modal').modal({backdrop: 'static', keyboard: false});
            };


        }]);
})();