/* =========================================================
 * Module: leaveDetailsController.js
 * Handles leave Details page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('leaveDetailsCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder', '$sce', '$window','$compile',function ($state, authService,EncApiService,DTOptionsBuilder, DTColumnDefBuilder, $sce,$window,$compile) {

            var vm = this;
           
            vm.sce = $sce;
            vm.innerWidth = window.innerWidth>1551 ? true: false;
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
                DTColumnDefBuilder.newColumnDef([9]).notSortable(),
                DTColumnDefBuilder.newColumnDef([2,3]).withOption('type', 'date')
            ];


            vm.initialData=function(){
                EncApiService('leaveDetails',null,{'id':''}).then(function(response) {
                    response.data=response.data.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                    vm.appliedLeaves = response.data;
                    $("#global-progress-display").hide();
                 

                },
                function (err) {
                    $("#global-progress-display").hide();
                    vm.message = err.message;
                });

            }

            

            vm.showLoadingButton=[];
            vm.download=function(index,url){
                $window.open(url, '_blank');
            }
            vm.calFun = function(){
                var myvalue = document.getElementsByTagName('th')[7].offsetWidth; 
                var half = Math.ceil(myvalue/2);
                console.log(myvalue,half);
                console.log(document.getElementsByClassName('commentText')); 

            }
            

            vm.withDraw=function(id){
                EncApiService('leaveWithDraw',null,{'id':id}).then(function(response) {
                    vm.initialData();
                
                },
                function (err) {
                $("#global-progress-display").hide();
                vm.message = err.message;
                });

            }
            
        }]);
})();