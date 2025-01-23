/* =========================================================
 * Module: policyCtrl.js
 * Handles policy.html
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('policyCtrl', ['$state', 'authService', 'EncApiService', '$scope','$stateParams','$sce',function($state ,authService, EncApiService, $scope,$stateParams,$sce) {

			var vm = this;
			var urlPdf;
			var name="EmployeeHandbook";
			$scope.urlPdff='';
			EncApiService('getEmployeeHandbook', null, {'name':name}).then(function(response) {
				
				vm.pdfData = response.data;
				urlPdf = vm.pdfData;
				$scope.trustSrc = function(src) {
				    return $sce.trustAsResourceUrl(src);
				}
				$scope.urlPdf = {src:urlPdf};


			});
			
		}]);
})();



