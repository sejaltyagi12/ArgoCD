(function() {
    'use strict';
    angular.module('app')
        .controller('ratingManagementCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints', 'DTOptionsBuilder', 'DTColumnDefBuilder','$uibModal','globalFactory','$rootScope',
            function($state ,authService, EncApiService, $scope, apiEndpoints, DTOptionsBuilder , DTColumnDefBuilder,$uibModal,globalFactory,$rootScope) {

                var vm = this;
                  $("#global-progress-display").show();
                vm.initialData = function(){
                  
                    EncApiService('getRatingList').then(function(response) {
                        vm.formSubmitted=true;
                        vm.ratingList = response.data;

                        for(var i=0;i<vm.ratingList.length;i++){
                            var des_data=[];
                            var des=JSON.parse(vm.ratingList[i].description)

                            for(var j=0;j<des.length;j++){
                                des_data.push(' '+des[j].order+'-'+des[j].description+' ');
                            }
                            vm.ratingList[i].des=des_data.toString();
                        }

                        $("#global-progress-display").hide();

                    },
                    function (err) {
                        $("#global-progress-display").hide();
                        vm.formSubmitted=false;
                    });
                }

                

                vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
                vm.dtColumnDefs = [
                    DTColumnDefBuilder.newColumnDef(0),
                    DTColumnDefBuilder.newColumnDef([0,3]).notSortable()
                ];

                vm.targetRatingUpdate=function(id){
                    var modalReturnbck=$uibModal.open({
                        templateUrl: 'app/modules/target/html/targetRating.html',
                        controller: 'targetRatingCtrl as vm',
                        backdrop: 'static',
                        resolve: {
                            ratId: function() {
                                return id;
                            }

                        }
                    })

                    modalReturnbck.result.then(function (status) {
                     if(status){
                        vm.initialData();
                     }
                    }, function () {
 
                    });

                }
            }]);
})();