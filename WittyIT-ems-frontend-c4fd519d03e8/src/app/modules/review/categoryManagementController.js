/* =========================================================
 * Module: categoryManagementController.js
 * Handles Categories under review
 * =========================================================
 */
(function() {
	'use strict';
	angular.module('app')
		.controller('categoryManagementCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'DTOptionsBuilder', 'DTColumnDefBuilder','$uibModal',
			function($state ,authService, EncApiService, $scope, apiEndpoints, DTOptionsBuilder , DTColumnDefBuilder,$uibModal) {

				var vm = this;
                $("#global-progress-display").show();

				vm.editable =  false;

                vm.initialData = function(){
             

				    EncApiService('getCategoryList').then(function(response) {
                        response.data=response.data.sort(function(a,b) {return (a.orderIndex > b.orderIndex) ? 1 : ((b.orderIndex > a.orderIndex) ? -1 : 0);} );
                        vm.categoryList = response.data;
                          $("#global-progress-display").hide();
                },
                function (err) {
                     $("#global-progress-display").hide();
                });
				}
				

				vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
				vm.dtColumnDefs = [
					DTColumnDefBuilder.newColumnDef([0]),
					DTColumnDefBuilder.newColumnDef([0,4,5]).notSortable()
				];

                vm.deleteCategory = function(catId){
                    var cat_id = {id : catId};
                    EncApiService('deleteCategory',null,{ 'id': catId }).then(function(response) {
                        vm.formSubmitted=true;
                        vm.alertModal();
                        vm.initialData();
                    },
                    function (err) {
                        vm.formSubmitted=false;
                        vm.message=err.data.message;
                        vm.alertModal();
                    });

                };

               vm.targetCategoryUpdate=function(id){

                var modalReturnbck = $uibModal.open({
                    templateUrl: 'app/modules/target/html/targetCategory.html',
                    controller: 'targetCategoryCtrl as vm',
                    backdrop: 'static',
                    resolve: {
                        catId: function() {
                            return id;
                        }
                    }
                });

                modalReturnbck.result.then(function (status) {
                     if(status){
                        vm.initialData();
                     }
                }, function () {

                });

            };

             vm.alertModal= function() {
                jQuery('#cat-modal').modal({backdrop: 'static', keyboard: false});
            };


			


             

			}]);
	})();