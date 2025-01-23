/* =========================================================
 * Module: leaveDetailsController.js
 * Handles leave Details page behaviour
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app')
        .controller('subordinateResignationCtrl', ['$state', 'authService', 'EncApiService','DTOptionsBuilder', 'DTColumnDefBuilder','globalFactory','$window',function ($state, authService, EncApiService,DTOptionsBuilder, DTColumnDefBuilder,globalFactory,$window) {

            var vm = this;
            vm.user = localStorage.getItem('user').toLowerCase();
            vm.department = localStorage.getItem('department').toLowerCase();
            vm.designationName = localStorage.getItem('designation').toLowerCase();



            var createTable=function(){
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
                    // DTColumnDefBuilder.newColumnDef([]).notSortable(),
                    DTColumnDefBuilder.newColumnDef([3,4]).withOption('type', 'date')
                ];
            }

            vm.getInitialData = function(){
                $("#global-progress-display").show();
                var pendingList=[];
                var actionTakenList=[];
                createTable();
                EncApiService('getEmployeeResignation',null,{'emp': ''}).then(function(response) {

                    for(var i=0;i<response.data.length;i++){
                        if(response.data[i].resignationDate !== null)
                            response.data[i].resignationDate=globalFactory.convertDates(response.data[i].resignationDate);
                        if(response.data[i].lastDay !== null)
                        response.data[i].lastDay=globalFactory.convertDates(response.data[i].lastDay);

                        if(vm.user.toLowerCase() === 'admin' || vm.department.toLowerCase() === 'human resource' || vm.department.toLowerCase() === 'hr'|| vm.department.toLowerCase() === 'hr dept') {
                            if(response.data[i].hrStatus === 'PENDING')
                                pendingList.push(response.data[i]);
                            else
                                actionTakenList.push(response.data[i]);
                        }
                        else{
                            if(response.data[i].managerStatus === 'PENDING')
                                pendingList.push(response.data[i]);
                            else
                                actionTakenList.push(response.data[i]);

                        }

                    }

                    pendingList = pendingList.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                    actionTakenList = actionTakenList.sort(function(a,b) {return (a.id > b.id) ? -1 : ((b.id > a.id) ? 1 : 0);} );
                    vm.teamResignationData = pendingList.concat(actionTakenList);
                    $("#global-progress-display").hide();
                },
                function (err) {
                    vm.message = err.data.message;
                    $("#global-progress-display").hide();

                });

            }

            vm.reHire = function (empCode) {

                vm.alertModal(function (confirmed) {
                    if (confirmed) {
                        console.log(vm.registrationData.joiningDate)
                        EncApiService('reHireEmployee', {}, { 'empCode': empCode, 'date': moment(vm.registrationData.joiningDate, 'DD/MM/YYYY').format("YYYY-MM-DD") }).then(function (response) {
                            console.log(response.data);
                            vm.getInitialData();
                        });
                    }
                });
            };
            vm.alertModal = function (callback) {
                jQuery('#item-modal').modal();
                jQuery('#item-modal button[data-dismiss="modal"]').click(function () {
                    var confirmed = jQuery(this).text().toLowerCase() === 'yes';
                    if (confirmed && vm.registrationData.joiningDate) {
                        if (callback && typeof callback === 'function') {
                            callback(confirmed);
                        }
                    }
                });
            };

            vm.getCurrentDate = function () {
                return new Date();
            };

           

            var HrAdminData =  {
                        "hrPublicNotes": null,
                        "hrPrivateNotes": null,
                        "hrAccepted": null,
                        "lastDay": null,
                        "id": null

                    }

            var ManagerData =  {
                      "managerPublicNotes": null,
                      "managerPrivateNotes":null,
                      "managerAccepted": null,
                      "id": null
                    }

            vm.removeClass = function($index,publicNotes){
                if(publicNotes !== undefined && publicNotes!=='')
                    $('#publicNotes_'+$index).removeClass('highlightErr'); 

            }

            // vm.approve = function(approvalStatus,id,publicNotes,privateNotes,lastDay,$index){


            //     if(vm.user.toLowerCase() === 'admin' || vm.department.toLowerCase() === 'human resource' || vm.department.toLowerCase() === 'hr'|| vm.department.toLowerCase() === 'hr dept' ){
                    
            //         if(publicNotes!== undefined && publicNotes!==''){
            //              // vm['publicNotes_'+id] = false;
            //             $('#publicNotes_'+$index).removeClass('highlightErr');

            //             HrAdminData.hrPublicNotes = publicNotes;
            //             HrAdminData.hrPrivateNotes = privateNotes;
            //             HrAdminData.hrAccepted = approvalStatus;
            //             HrAdminData.lastDay = globalFactory.convertDates(lastDay);
            //             HrAdminData.id = id;

            //             if(privateNotes === undefined || privateNotes === ''){
            //                 delete HrAdminData.hrPrivateNotes; 
            //             }

            //             var data = angular.copy(HrAdminData);
                       
            //             EncApiService('resignationApprovalHrAdmin',{'data':data}).then(function(response) {
            //                vm.getInitialData();
            //             },
            //             function (err) {
            //                 vm.message = err.data.message;
            //             });
            //         }
            //         else{
            //             // vm['publicNotes_'+id] = true;
            //             $('#publicNotes_'+$index).addClass('highlightErr'); 
            //         }
            //     }
            //     else{
            //         if(publicNotes!== undefined && publicNotes!==''){

            //             $('#publicNotes_'+$index).removeClass('highlightErr'); 

            //             ManagerData.managerPublicNotes = publicNotes;
            //             ManagerData.managerPrivateNotes = privateNotes;
            //             ManagerData.managerAccepted = approvalStatus;
            //             ManagerData.id = id;

            //             if(privateNotes === undefined || privateNotes === ''){
            //                 delete HrAdminData.hrPrivateNotes; 
            //             }

            //             var data = angular.copy(ManagerData);
                        
            //             EncApiService('resignationApprovalManager',{'data':data}).then(function(response) {
            //                vm.getInitialData();
            //             },
            //             function (err) {
            //                 vm.message = err.data.message;
            //             });
            //         }
            //         else{
            //            $('#publicNotes_'+$index).addClass('highlightErr');  
            //         }

            //     }


            // }

        }]);
})();