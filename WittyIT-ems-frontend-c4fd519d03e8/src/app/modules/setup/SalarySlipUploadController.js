/* =========================================================
 * Module: salarySlipUploadController.js
 * Handles salarySlipUpload page behaviour
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('salarySlipUploadCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'DTOptionsBuilder', 'DTColumnDefBuilder','globalFactory','$window','$sce','FileSaver','Blob',function($state ,authService, EncApiService, $scope, apiEndpoints, DTOptionsBuilder , DTColumnDefBuilder,globalFactory,$window,$sce,FileSaver,Blob) {

			var vm = this;
			vm.rosterTypeArr = [];
			var typeMatched = false;
			vm.informed=null;


			
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
	            DTColumnDefBuilder.newColumnDef([0,4,5]).notSortable(),
	        ];


                 /*
                  *handles file selection
                  */
         	var handleFileSelect = function(evt) {
         		vm.informed=null;
				vm.rosterfile = evt.currentTarget.files[0];
				vm.validExtension = (/\.(xlsx|xls)$/i).test(vm.rosterfile.name);
				$scope.$apply();
				if (!vm.validExtension) {
					return;
				}

				var reader = new FileReader();
				reader.onload = function(evt) {
					$scope.$apply(function($scope) {
						vm.myRoster = evt.target.result;
						
					});
				};
				reader.readAsDataURL(vm.rosterfile);
			};
			angular.element(document.querySelector('#rosterInput')).on('change', handleFileSelect);

			vm.upload = function() {
				if(document.getElementById("upload-file-info").innerHTML!='' && vm.validExtension){
					vm.message=undefined;
					var monthIndex=vm.monthList.indexOf(vm.month)+1;

					var myformdata = new FormData();
					vm.myRoster = vm.dataURItoBlob(vm.myRoster);
					myformdata.append('fileContent', vm.myRoster,vm.rosterfile.name); 
					var headers = {
						'Content-Type': undefined
					};
					EncApiService('payrollPaySlipUpload', {
						'data': myformdata,
						'transformRequest': angular.identity,
						'headers': headers

					},{'month':monthIndex,'year':vm.year}).then(function(response) {
						vm.paySlipUploaded = true;
						
						vm.myRoster=null;
	                    angular.element('#rosterInput').val(null);
	                    document.getElementById("upload-file-info").innerHTML = '';

						
						
					},
					function(err) {
						vm.paySlipUploaded = false;
						vm.myRoster=null;
						angular.element('#rosterInput').val(null);
						document.getElementById("upload-file-info").innerHTML = '';
						vm.message=err.data.message;

					});
				}

				else{
					if(document.getElementById("upload-file-info").innerHTML=='')
						vm.informed="Please upload file";
				}

			};

			vm.dataURItoBlob = function(dataURI) {
				// convert base64/URLEncoded data component to raw binary data held in a string
				var byteString;
				if (dataURI.split(',')[0].indexOf('base64') >= 0)
					byteString = atob(dataURI.split(',')[1]);
				else
					byteString = unescape(dataURI.split(',')[1]);

				// separate out the mime component
				var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

				// write the bytes of the string to a typed array
				var ia = new Uint8Array(byteString.length);
				for (var i = 0; i < byteString.length; i++) {
					ia[i] = byteString.charCodeAt(i);
				}

				return new Blob([ia], {
					type: mimeString
				});
			};

			vm.employeeChanged = function(employee){
				var monthList = [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
				var empId = $('datalist').find('option[value="'+employee+'"]').attr('id');
				if(empId!=undefined){
					EncApiService('getEmployeePaySlip',null,{'id':empId}).then(function(response) {
	                    for(var i=0;i<response.data.length;i++)
                    		response.data[i].month=monthList[response.data[i].month-1];
                    	vm.paySlip=response.data
                    	vm.name=employee;
                    	
	                },
		            function (err) {

		            });
				}

			};

			

			vm.getInitialData1=function(){
				vm.yearList=[];
				vm.currentDateTime = new Date();
	            vm.monthList = [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
	            vm.month=vm.monthList[vm.currentDateTime.getMonth()];
	            for(var i=0;i<3;i++)
	            	vm.yearList.push(vm.currentDateTime.getFullYear()-i)
	            vm.year=vm.currentDateTime.getFullYear();

			}

			vm.getInitialData = function(){

				EncApiService('fetchUserList').then(function(response) {
					vm.employeeList = [];
                    for (var i = 0; i < response.data.length; i++) {
                        if(response.data[i].isActive === true)
                            vm.employeeList.push(response.data[i]);
                   	     
                    }

				},
				function(err) {
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

			vm.goToPreviousPage=function(){
                if(globalFactory.previousRoute == ''){
                    var previousRoute = localStorage.getItem('previousRoute');
                    globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
                }
                $state.go(globalFactory.previousRoute);
                
            };

            vm.alertModal=function() {
                jQuery('#paySlipUpload-modal').modal({backdrop: 'static', keyboard: false});
            };

				
		}]);
})();