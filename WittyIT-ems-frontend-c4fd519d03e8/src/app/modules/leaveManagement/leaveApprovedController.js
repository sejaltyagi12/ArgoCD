(function () {
    'use strict';
    angular.module('app')
        .controller('leaveApprovedCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder', '$rootScope','globalFactory','$window',function ($state, authService, EncApiService,DTOptionsBuilder, DTColumnDefBuilder,$rootScope,globalFactory,$window) {
            var vm = this;
            // var ab = document.getElementsByClassName('comment-hover');
            // console.log(ab);
            // console.log(ab[0]);
            
        //    vm.callFun = function(){
        //     var eles = document.getElementsByClassName('comment-hover');

        //     console.log(eles[0]);
        //    }
         vm.user=localStorage.getItem('user');
         vm.innerWidth = window.innerWidth>1551 ? true: false;
       vm.fun= function(e){
        var eles = document.getElementsByClassName('comment-hover');
        //console.log(e)
        // console.log(eles[0].ch)
        // var p= eles[0].children[0]
        //  var width = p.getElementsByClassName('p-tag');
        //  console.log(width);
       }
            
            var createTable=function(){
                  jQuery.extend( jQuery.fn.dataTableExt.oSort, {
                        "date-pre": function (date) {
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
                          DTColumnDefBuilder.newColumnDef([0,7,12]).notSortable(),
                          DTColumnDefBuilder.newColumnDef([4,5]).withOption('type', 'date')
                    ];

            }

            vm.getInitialData=function(){
                $("#global-progress-display").show();
                var actionNotTakenLeave=[];
                var actionTakenLeave=[];
                createTable();
                EncApiService('leaveAppliedCount').then(function (response) {
                    globalFactory.leaveList=response.data;
                        EncApiService('leaveReq').then(function(response) {
                        for(var i=0;i<response.data.length;i++){
                            if(response.data[i].status=='PENDING')
                                actionNotTakenLeave.push(response.data[i]);
                            else
                               actionTakenLeave.push(response.data[i]);
                        }
                        actionNotTakenLeave=actionNotTakenLeave.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                        actionTakenLeave=actionTakenLeave.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                        vm.leaveApplicationList = actionNotTakenLeave.concat(actionTakenLeave);
                        $("#global-progress-display").hide();

                        },
                        function (err) {
                        $("#global-progress-display").hide();
                        vm.message = err.message;
                        });

                }, function (err) {
                    $("#global-progress-display").hide();
                    vm.message = err.message;

                });
                

            }


            vm.download = function (index,url,name,empCode) {
                $window.open(url, '_blank');
            }

            vm.approve = function(approved, id, comments, index) {
                vm.leaveApproveData = {
                    leaveId: id,
                    isApproved: approved,
                    managerComments:comments
                };
                var data=angular.copy(vm.leaveApproveData);
                EncApiService('leaveApprove', { 'data':data },null,true).then(function (response) {
                    vm.getInitialData();
                    EncApiService('leaveAppliedCount').then(function (response) {
                        globalFactory.leaveList=response.data;
                    }, function (err) {
                        vm.message = err.message;
                    });
                }, function (err) {
                    vm.message = err.message;
                });
            }


         vm.dtOptions = DTOptionsBuilder.newOptions().withOption('initComplete', function() {
        angular.element('.dataTables_filter input').attr('placeholder', 'Search ...');
        })

        }]);
})();