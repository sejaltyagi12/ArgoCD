/* =========================================================
 * Module: DashboardController.js
 * Handles Dashboard page behaviour
 * =========================================================
 */
(function () {
	'use strict';

	angular.module('app')
		.controller('dashboardCtrl', ['$state', 'Upload', 'authService', 'EncApiService', '$scope', '$cookies', '$window', '$stateParams', '$http', 'apiEndpoints', '$q', 'globalFactory', '$timeout', '$interval',
			function ($state, Upload, authService, EncApiService, $scope, $cookies, $window, $stateParams, $http, apiEndpoints, $q, globalFactory, $timeout, $interval) {

				var vm = this;
				vm.validExtension = true;
				vm.displayCroppedImg = false;
				vm.picCropped = false;
				vm.imageSelected = false;
				var dateArr = [];
				vm.rosterTypeArr = [];
				var typeMatched = false;
				vm.invalidSize = false;
				vm.timeStamp = new Date().getTime();
				vm.hoverEdit = false;

				var sliderInterval = $interval(function () {
					if (!vm.hoverEdit) {
						var elem = angular.element('#holiday-slider').siblings().children();
						if (elem.length > 0) {
							elem[1].dispatchEvent(new MouseEvent('click'));
						}
					}
				}, 10000);

				$scope.$on('$destroy', function () {
					$interval.cancel(sliderInterval);
				});

				/*
				 *	To show custom date format on the UI
				 */
				var customDateStr = function (bdayDate, type) {
					var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August",
						"September", "October", "November", "December"
					];
					var dayNames = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY']
					var customDateObj = new Date(bdayDate);
					if (type === 'bday')
						return customDateObj.getDate() + " " + monthNames[customDateObj.getMonth()];
					else
						return dayNames[customDateObj.getDay()] + ',' + ' ' + customDateObj.getDate()  + " " + monthNames[customDateObj.getMonth()]+ "," + " " + customDateObj.getFullYear();
				}

				var sortDates = function (listArr, dateVariable) {
					var curYear = (new Date()).getFullYear();
					listArr.sort(function (a, b) {
						// Turn your strings into dates, and then subtract them
						// to get a value that is either negative, positive, or zero.
						var date1 = new Date(a[dateVariable]);
						// Making the year same for both the Dates so then they will be
						// sorted on the basis of DAY and MONTH only.
						var date1New = new Date(curYear, date1.getMonth(), date1.getDate())
						var date2 = new Date(b[dateVariable]);
						var date2New = new Date(curYear, date2.getMonth(), date2.getDate())
						return date1New.getTime() - date2New.getTime();
					});
					return listArr;
				}



				vm.sendWishes = function (empId, empName, eventType) {
					// Update eventType to "Anniversary" if it's "Work Anniversary"
					if (eventType === "Work Anniversary") {
						eventType = "Anniversary";
					}
					EncApiService('sendEventWish', null, { 'id': empId, 'eventType': eventType }).then(function (response) {
						var bd_msg = "Wishes sent to"
						vm.WishAlertModal(empName, bd_msg);

					},
						function (err) {
							vm.WishAlertModal(empName, err.data.message);
						});
				};

				/*
				 * image upload
				 */

				vm.myImage = '';
				vm.myCroppedImage = '';
				var selectedFile = '';
				var file = null;

				vm.upload = function () {
					var fileModel = vm.myFile;
					vm.croppedImg = vm.dataURItoBlob(vm.myCroppedImage);
					var formdata1 = new FormData();
					formdata1.append('avatarContent', vm.croppedImg);
					formdata1.append('name', file.name);
					var headers = {
						'Content-Type': undefined
					};
					EncApiService('profilePicUpload', {
						'data': formdata1,
						'transformRequest': angular.identity,
						'headers': headers
					}).then(function (response) {
						vm.timeStamp = new Date().getTime();
						vm.cancelCrop();
						console.log("res image", response.data);
						vm.getEmployeeData.avatarCompleteUrl = "";
						vm.getEmployeeData.avatarCompleteUrl = response.data;
						//vm.getDashboardEmployeeData();
					},
						function (err) {
							vm.formSubmitted = false;

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
					file = evt.currentTarget.files[0];
					vm.validExtension = (/\.(gif|jpg|jpeg|png)$/i).test(file.name);
					$scope.$apply();
					if (!vm.validExtension) {
						vm.cancelCrop();
						return;
					}

					var actualImg = new Image();
					actualImg.onload = function () {
						var HeightDiff = (this.height / this.width) * 100;
						var widthDiff = (this.width / this.height) * 100;
						if (HeightDiff > 200 || widthDiff > 200) {
							vm.invalidSize = true;
							vm.cancelCrop();
							return;
						} else {
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
						}
					};

					actualImg.src = _URL.createObjectURL(file);

				};
				angular.element(document.querySelector('#fileInput')).on('change', handleFileSelect);


				vm.imageCropped = function () {
					vm.profilePic = vm.myCroppedImage;
					vm.picCropped = true;
					vm.displayCroppedImg = true;
				};

				vm.cancelCrop = function () {
					vm.picCropped = false;
					vm.imageSelected = false;
					vm.displayCroppedImg = false;
					document.getElementById('fileInput').value = "";
				};

				/*
				 * gets the details of logged in user
				 */
				vm.getDashboardEmployeeData = function () {
					$("#global-progress-display").show();

					EncApiService('getNotification', null, { 'admin': false }).then(function (response) {
						vm.notificationList = response.data;

						EncApiService('holidayList', null, { 'admin': false }).then(function (response) {
							var listArr = angular.copy(response.data);
							vm.holidayList = sortDates(listArr, 'holidayDate');;
							vm.holidayList.map(function (data) {
								data.holidayDate = customDateStr(data.holidayDate, 'holiday');
								console.log(data.holidayDate);
							});


							EncApiService('getEvents').then(function (response) {
								var listArr = angular.copy(response.data);
								// vm.birthdayList = sortDates(listArr, 'eventDate');
								vm.birthdayList = listArr;
								vm.birthdayList.map(function (data) {
									data.eventDate = customDateStr(data.eventDate, 'bday');
								})


								EncApiService('userInfo').then(function (res) {
									var data = angular.copy(res.data);
									data.joiningDate = globalFactory.convertDates(data.joiningDate);
									data.dob = globalFactory.convertDates(data.dob);
									var profilePic = null;
									if ((vm.getEmployeeData != undefined) && (vm.getEmployeeData.hasOwnProperty('avatarCompleteUrl')) && (vm.getEmployeeData.avatarCompleteUrl != null)) {
										profilePic = vm.getEmployeeData.avatarCompleteUrl;
									}
									vm.getEmployeeData = data;
									if (profilePic !== null)
										vm.getEmployeeData.avatarCompleteUrl = profilePic;
									vm.message = "";
									vm.mapEmpData = {
										"Designation": vm.getEmployeeData.designation,
										"Employee Code": vm.getEmployeeData.empCode,
										"Department": vm.getEmployeeData.departmentName,
										"Date of Joining": vm.getEmployeeData.joiningDate,
										"Email-Id": vm.getEmployeeData.officialEmail,
										"Primary Phone": vm.getEmployeeData.primaryPhone,
										"Date of Birth": vm.getEmployeeData.dob,
										"Blood Group": vm.getEmployeeData.bloodGroup,
										"Permanent Address": vm.getEmployeeData.permanentAddress
									};

									$("#global-progress-display").hide();
								},
									function (err) {
										console.log("Error : ", err);
										$("#global-progress-display").hide();
									});


							},
								function (err) {
									console.log("Error : ", err);
									$("#global-progress-display").hide();
								});
						},
							function (err) {
								console.log("Error : ", err);
								$("#global-progress-display").hide();
							});


					},
						function (err) {
							console.log("Error : ", err);
							$("#global-progress-display").hide();
						});

				};

				vm.goToPreviousPage = function () {

					if ($stateParams.id != undefined && $stateParams.id != '') {
						if (globalFactory.previousRoute == '') {
							var previousRoute = localStorage.getItem('previousRoute');
							globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null) ? previousRoute : 'page.dashboard';
						}
						$state.go(globalFactory.previousRoute);
					} else {
						$state.go('page.dashboard');
					}
				};

				/*
				 *notification settings
				 */

				var startdelay = 2; // START SCROLLING DELAY IN SECONDS
				vm.scrollspeed = 0.1; // ADJUST SCROLL SPEED
				var scrollwind = 1; // FRAME START ADJUST
				var speedjump = 30; // ADJUST SCROLL JUMPING = RANGE 20 TO 40
				var nextdelay = 0; // SECOND SCROLL DELAY IN SECONDS 0 = QUICKEST
				var topspace = "0px"; // TOP SPACING FIRST TIME SCROLLING
				var frameheight = angular.element(document.querySelector("#notificationDiv"))[0].clientHeight; // IF YOU RESIZE THE CSS HEIGHT, EDIT THIS HEIGHT TO MATCH
				// frameheight = parseInt(frameheight) - 50
				var dataobj = null;
				var AreaHeight = null;


				vm.current = (vm.scrollspeed);


				var HeightData = function () {
					AreaHeight = dataobj.offsetHeight
					if (AreaHeight === 0) {
						$timeout(function () {
							HeightData();
						}, startdelay * 1000);
					} else {
						ScrollNewsDiv()
					}
				};

				vm.NewsScrollStart = function () {
					dataobj = document.all ? document.all.NewsDiv : document.getElementById("NewsDiv")
					dataobj.style.top = topspace;
					$timeout(function () {
						HeightData();
					}, startdelay * 1000);
				};

				var ScrollNewsDiv = function () {
					dataobj.style.top = scrollwind + 'px';
					scrollwind -= vm.scrollspeed;
					if (parseInt(dataobj.style.top) < AreaHeight * (-1)) {
						dataobj.style.top = frameheight + 'px';
						scrollwind = frameheight;
						$timeout(function () {
							ScrollNewsDiv();
						}, nextdelay * 1000);
					} else {
						$timeout(function () {
							ScrollNewsDiv();
						}, nextdelay * speedjump);
					}
				};

				vm.WishAlertModal = function (empName, msg) {
					vm.empName_bd = empName.charAt(0).toUpperCase() + empName.slice(1);
					vm.msg_bd = msg;
					jQuery('#wishAlert').modal();
				};
			}
		]);
})();