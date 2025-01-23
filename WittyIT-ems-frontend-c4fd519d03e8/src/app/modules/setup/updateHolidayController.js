/* =========================================================
 * Module: roasterController.js
 * Handles Roaster page behaviour
 * =========================================================
 */
(function () {
	'use strict';
	angular.module('app')
		.controller('updateHolidayCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory', '$stateParams', 'DTOptionsBuilder', 'DTColumnDefBuilder',
			function ($state, authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams, DTOptionsBuilder, DTColumnDefBuilder) {

				var vm = this;
				vm.invalidSize = false;
				vm.formSubmitted = null;
				vm.imageUpload = null;
				vm.imageUploadMsg = '';
				vm.message = '';
				vm.holidayData = {
					"holidayDate": null,
					"name": null,
					"companyId": null,
					"id": null
				};
				vm.fileChoosen = 'No file chosen';
				// vm.holidayResetData = angular.copy(vm.holidayData);

				jQuery.extend(jQuery.fn.dataTableExt.oSort, {
					"date-pre": function (date) {
						return moment(date, 'DD/MM/YYYY');
					}
				});

				jQuery.extend(jQuery.fn.dataTableExt.oSort, {
					"nullable-asc": function (a, b) {
						return (a == b ? 0 : (!a ? 1 : (!b ? -1 : (a > b ? 1 : -1))));
					},
					"nullable-desc": function (a, b) {
						return (a == b ? 0 : (!a ? -1 : (!b ? 1 : (a > b ? -1 : 1))));
					}
				});

				vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
				vm.dtColumnDefs = [
					DTColumnDefBuilder.newColumnDef(0),
					DTColumnDefBuilder.newColumnDef([0, 5, 6]).notSortable(),
					DTColumnDefBuilder.newColumnDef([2]).withOption('type', 'date')
				];

				if (globalFactory.previousRoute !== '')
					localStorage.setItem('previousRoute', globalFactory.previousRoute);

				/*changes
				*/
				vm.validExtension = true;
				vm.displayCroppedImg = false;
				vm.picCropped = false;
				vm.imageSelected = false;
				var dateArr = [];

				vm.timeStamp = new Date().getTime();
				vm.errImgMsg = '';


				vm.myImage = '';
				var selectedFile = '';
				var file = null;

				vm.upload = function () {
					vm.croppedImg = vm.dataURItoBlob(vm.myImage);

					var formdata1 = new FormData();
					formdata1.append('fileContent', vm.croppedImg);
					formdata1.append('name', file.name);

					var headers = {
						'Content-Type': undefined
					};
					EncApiService('holidayImageUpload', {
						'data': formdata1,
						'transformRequest': angular.identity,
						'headers': headers
					}, { 'id': vm.holidayId }).then(function (response) {
						vm.imageUpload = true;
					},
						function (err) {
							vm.imageUpload = false;
							vm.imageUploadMsg = err.data.message;


						});
				};




				/*
				 * converts base_64 image to blob
				 */
				vm.dataURItoBlob = function (dataURI) {
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
				var _URL = window.URL || window.webkitURL;
				var handleFileSelect = function (evt) {
					vm.invalidSize = false;
					vm.errImgMsg = '';
					file = evt.currentTarget.files[0];
					vm.validExtension = (/\.(gif|jpg|jpeg|png)$/i).test(file.name);
					$scope.$apply();
					if (!vm.validExtension) {
						$("#upload-file-info").html(document.getElementById("fileInput").files = null);
						vm.fileChoosen = 'No file chosen';
					}
					if (!vm.validExtension) {

						return;
					}

					if (file.size > 512000) {
						vm.invalidSize = true;
						return;
					} else {
						var actualImg = new Image();
						actualImg.onload = function () {

							vm.invalidSize = false;
							var reader = new FileReader();
							reader.onload = function (evt) {
								$scope.$apply(function ($scope) {
									vm.myImage = evt.target.result;
									var imgData = JSON.stringify(vm.myImage);
									vm.imageSelected = true;
								});
							};
							reader.readAsDataURL(file);

						};

						actualImg.src = _URL.createObjectURL(file);
					}

				};
				angular.element(document.querySelector('#fileInput')).on('change', handleFileSelect);



				vm.cancel = function () {
					vm.myImage = '';
					vm.imageSelected = false;
					document.getElementById('fileInput').value = "";
				};

				/*changes */



				/*
				 * Gets employee data in case of edit
				 */

				if ($stateParams.id != undefined && $stateParams.id != '') {
					EncApiService('getHoliday', null, { 'id': $stateParams.id }).then(function (response) {
						response.data.holidayDate = globalFactory.convertDates(response.data.holidayDate);
						vm.holidayData = response.data;
						if (response.data.imageUrl != null)
							vm.myImage = response.data.imageUrl + '?x=' + vm.timeStamp;
						else
							vm.myImage = '';

					},
						function (err) {
						});
				}



				vm.addHoliday = function () {
					if (vm.myImage != '' && vm.myImage != undefined && vm.myImage != null) {

						if ($stateParams.id != undefined && $stateParams.id != '') {
							vm.holidayData.id = $stateParams.id;
						}
						var data = angular.copy(vm.holidayData);
						data.holidayDate = globalFactory.convertDates(vm.holidayData.holidayDate);

						EncApiService('addHoliday', { 'data': data }).then(function (response) {
							vm.formSubmitted = true;
							vm.holidayId = response.data.id
							if (file != null)
								vm.upload();

							vm.alertModal();
							if ($stateParams.id == '') {
								vm.holidayData = {
									"holidayDate": null,
									"name": null,
									"companyId": null,
									"id": null
								};

								$scope.UpdateHoliday.$setPristine();
								vm.myImage = '';

							}

						},
							function (err) {
								vm.formSubmitted = false;
								vm.message = err.data.message;
								vm.alertModal();
							});
					}
					else
						vm.errImgMsg = "Please Choose Image"
				};

				vm.deleteHoliday = function (id, index) {

					EncApiService('deleteHoliday', null, { 'id': id }).then(function (response) {

						vm.holidayList.splice(index, 1);
					},
						function (err) {

						});
				};

				vm.getHolidayList = function () {
					$("#global-progress-display").show();

					EncApiService('holidayList', null, { 'admin': true }).then(function (response) {
						vm.holidayList = response.data;
						vm.holidayList.forEach(function (value, index) {
							value.holidayDate = globalFactory.sortDateFormat(value.holidayDate);
						});

						$("#global-progress-display").hide();

					},
						function (err) {
							$("#global-progress-display").hide();
						});

				};

				vm.getInitialData = function () {
					EncApiService('fetchCompanyList').then(function (response) {
						console.log("holiday list", response);
						vm.companyList = response.data;
					},
						function (err) {

						});

				};


				vm.goToPreviousPage = function () {
					if (globalFactory.previousRoute == '') {
						var previousRoute = localStorage.getItem('previousRoute');
						globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null) ? previousRoute : 'page.dashboard';
					}
					$state.go(globalFactory.previousRoute);
				}

				vm.alertModal = function () {
					jQuery('#holiday-modal').modal({ backdrop: 'static', keyboard: false });
				};

			}]);
})();