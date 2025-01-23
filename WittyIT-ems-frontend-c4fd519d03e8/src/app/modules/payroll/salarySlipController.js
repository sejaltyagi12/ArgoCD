/* =========================================================
 * Module: roasterController.js
 * Handles salary slip download
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('salarySlipCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'DTOptionsBuilder', 'DTColumnDefBuilder','$timeout', 'FileSaver','Blob', function($state ,authService, EncApiService, $scope, apiEndpoints, DTOptionsBuilder , DTColumnDefBuilder, $timeout,FileSaver,Blob ) {
			var vm = this;
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

	        ];
	        vm.monthList = [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];


  				
			vm.getInitialData = function(){
				EncApiService('getEmployeePaySlip',null,{'id':''}).then(function(response) {
                    for(var i=0;i<response.data.length;i++)
                    	response.data[i].month=vm.monthList[response.data[i].month-1];
                    vm.paySlip=response.data;

                    console.log("employeePaySlip",response.data);
                  
                },
	            function (err) {

	            });
	            EncApiService('userInfo').then(function(response) {
                    vm.name=response.data.firstName+' '+'('+response.data.empCode+')'
                  
                },
	            function (err) {

	            });
			};
			vm.showLoadingButton=[];
			vm.downloadPaySlip=function(index,paySlipId,month,year){
				vm.showLoadingButton[index]=true;
				EncApiService('downloadEmployeePaySlip',null,{'id':paySlipId}).then(function(response) {
					var byteCharacters = atob(response.data);
					var byteNumbers = new Array(byteCharacters.length); 
				    for (var i = 0; i < byteCharacters.length; i++) {  
			            byteNumbers[i] = byteCharacters.charCodeAt(i);    
	               	}      
	               	var byteArray = new Uint8Array(byteNumbers);
			     	var blob = new Blob([byteArray],{type: 'application/pdf;charset=utf-8'});
			     	var fileName=vm.name+'_'+month+'_'+year + ".pdf";
			     	FileSaver.saveAs(blob, fileName);
				    vm.showLoadingButton[index]=false;
				    
				},
				function(err) {
					console.log("error",err);
					vm.showLoadingButton[index]=false;
				
				});

			}

			
			
		}]);
})();



