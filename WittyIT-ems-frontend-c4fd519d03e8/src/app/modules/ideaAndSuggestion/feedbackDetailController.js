/* =========================================================
 * Module: feedbackDetailController.js
 * Handles: My Feedbacks page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('feedbackDetailCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder','toaster', function ($state, authService, EncApiService,DTOptionsBuilder, DTColumnDefBuilder,toaster) {
            $("#global-progress-display").show();
            var vm = this;
            var isfeedbackDetailDataAvaliable = null;
            var errMessage = null;

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
                DTColumnDefBuilder.newColumnDef([0,4,5]).notSortable(),
                DTColumnDefBuilder.newColumnDef([1]).withOption('type', 'date')
            ];

            vm.getInitialData = function(){
                EncApiService('IdeasPostedByEmployee').then(function(response) {
                    vm.feedbackDetail = response.data;
                    $("#global-progress-display").hide();

                },
                function (err) {
                    isfeedbackDetailDataAvaliable = false;
                    if(err.data.message !== undefined && err.data.message !== null && err.data.message !== '')
                        errMessage = err.data.message;
                    $("#global-progress-display").hide();
                    toasterShow();
                    
                });

            }


            var pop = function(type,title){
                toaster.pop({
                    type: type,
                    title: title,
                    timeout: 5000
                });
            };

            var toasterShow = function(){
                if(isfeedbackDetailDataAvaliable === false  &&  errMessage === null){
                    pop('error','Unable to process your request. Please retry or contact system admin');
                }
                else{
                    pop('error',errMessage);
                }
            }
            

        }]);
})();