/* =========================================================
 * Module: roasterController.js
 * Handles Roaster page behaviour
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('roasterCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints','DTOptionsBuilder', 'DTColumnDefBuilder','globalFactory',
			function($state ,authService, EncApiService, $scope, apiEndpoints, DTOptionsBuilder, DTColumnDefBuilder, globalFactory) {

				var vm = this;
				vm.rosterTypeArr = [];
				var typeMatched = false;
				vm.rosterUploaded = null;
				vm.informed=null;

				if (globalFactory.previousRoute !== '')
                	localStorage.setItem('previousRoute', globalFactory.previousRoute);

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
					DTColumnDefBuilder.newColumnDef([0,3]).notSortable(),
					DTColumnDefBuilder.newColumnDef([1]).withOption('type', 'date')
				];

            
	             var commonOptions  = {
						editable: true,
						firstDay: 1, //  1(Monday) this can be changed to 0(Sunday) for the USA system
						selectable: true,
						defaultView: 'month'
					};


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
						var myformdata = new FormData();
						vm.myRoster = vm.dataURItoBlob(vm.myRoster);
						myformdata.append('fileContent', vm.myRoster,vm.rosterfile.name);

						var headers = {
							'Content-Type': undefined
						};
						EncApiService('uploadRoster', {
							'data': myformdata,
							'transformRequest': angular.identity,
							'headers': headers

						})
						.then(function(response) {
							vm.rosterUploaded = true;
							vm.myRoster=null;
	                        angular.element('#rosterInput').val(null);
	                        document.getElementById("upload-file-info").innerHTML = '';

						},
						function(err) {
	                       vm.rosterUploaded = false;
	                       vm.myRoster=null;
	                       angular.element('#rosterInput').val(null);
	                       document.getElementById("upload-file-info").innerHTML = '';

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
					var empId = $('datalist').find('option[value="'+employee+'"]').attr('id');
					if(empId != undefined){
						EncApiService('getRoster',null,{'id': empId}).then(function(response) {
							vm.rosterData = response.data;

							var colors =  ['#800000', '#008000', '#008080', '#0000FF', '#000080', 
									'#800080', '#00FFFF', '#FF0000', '#FF00FF', '#808080'];
							for (var i = 0; i <= vm.rosterData.length - 1; i++) {
								if(vm.rosterTypeArr.length > 0){
									for(var j= 0 ; j<= vm.rosterTypeArr.length - 1; j++){
										if(vm.rosterData[i].roasterType.type==vm.rosterTypeArr[j].type){
											vm.rosterTypeArr[j].dateList.push(JSON.stringify(new Date(vm.rosterData[i].roasterDate))); 
											typeMatched = true;
										}
									}
									if(!typeMatched){
										var newTypeObj={"dateList" : []};
										newTypeObj["type"] = vm.rosterData[i].roasterType.type;
										newTypeObj["dateList"].push(JSON.stringify(new Date(vm.rosterData[i].roasterDate)));
										newTypeObj["color"] = colors[vm.rosterTypeArr.length];
										vm.rosterTypeArr.push(newTypeObj);
									}
								}
								else{
									var newTypeObj={"dateList" : []};
								    newTypeObj["type"] = vm.rosterData[i].roasterType.type;
									newTypeObj["dateList"].push(JSON.stringify(new Date(vm.rosterData[i].roasterDate)));
									    newTypeObj["color"] = colors[vm.rosterTypeArr.length];
									vm.rosterTypeArr.push(newTypeObj);
								}
								typeMatched = false;
							}

							commonOptions.dayRender = function(date, cell) {
							for (var i = 0; i <= vm.rosterTypeArr.length - 1; i++) {
								if(vm.rosterTypeArr[i].dateList.indexOf(JSON.stringify(date)) != -1){
									 $(cell).find('div.fc-day-number').css("background-color", vm.rosterTypeArr[i].color);
									cell.addClass('highLightCell');

								}
							}

					}
					$( "#roster_calendar" ).empty();
					
					$('#roster_calendar').fullCalendar(commonOptions);
					vm.legendArr = angular.copy(vm.rosterTypeArr);
					vm.rosterTypeArr = [];

							},
							function(err) {


							});
					  }
				};

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

				$('#roster_calendar').fullCalendar(commonOptions);

				vm.goToPreviousPage=function(){
					if(globalFactory.previousRoute == ''){
						var previousRoute = localStorage.getItem('previousRoute');
						globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
					}
					$state.go(globalFactory.previousRoute);
        		}

             

			}]);
	})();