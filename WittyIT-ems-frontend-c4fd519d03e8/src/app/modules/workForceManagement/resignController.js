/* =========================================================
 * Module: resignController.js
 * Handles: resign page behaviour
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('resignCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'globalFactory','$stateParams','DTOptionsBuilder', 'DTColumnDefBuilder','userId','$uibModalInstance','fullName',
			function($state ,authService, EncApiService, $scope, apiEndpoints, globalFactory, $stateParams,DTOptionsBuilder,DTColumnDefBuilder,userId,$uibModalInstance,fullName) {

				var vm = this;


				
				vm.formSubmitted = null;
				vm.resignForm = {
					"resignationDate": null,
					"resignationReason": null,
					"id":null
				};
				vm.resignFormResetData = angular.copy(vm.resignForm);

				vm.getInitialData=function(){
					vm.fullName=fullName;
					EncApiService('getIndividualEmployee', null,{ 'regId': userId}).then(function(response) {
						if(response.data.resignationReason!= null && response.data.resignationDate != null){
							response.data.resignationDate=globalFactory.convertDates(response.data.resignationDate)
						}
						vm.resignForm.resignationDate=response.data.resignationDate;
						vm.resignForm.resignationReason=response.data.resignationReason;

	                },
	                function (err) {
	                   
	                });

				}

        		vm.resign= function(){
					if (userId != undefined && userId != '') {
						vm.resignForm.id= userId; 
					}
					var data=angular.copy(vm.resignForm);
					data.resignationDate= globalFactory.convertDates(vm.resignForm.resignationDate);
	                EncApiService('resign', { 'data': data}).then(function(response) {

	                    vm.formSubmitted =true;
	                    vm.alertModal();

	                },
	                function (err) {
	                    vm.formSubmitted = false;
	                    vm.message=err.data.message;
	                    vm.alertModal();

	                });
               	};

               	vm.closeModal = function(x) {
               		if(x==undefined){
	               		if(vm.formSubmitted)
	               			$uibModalInstance.close(vm.formSubmitted);
               		}
               		else
               			$uibModalInstance.dismiss('cancel');
            	};

            	vm.alertModal= function() {
                	jQuery('#resign-modal').modal({backdrop: 'static', keyboard: false});
            	};


			}]);
	})();