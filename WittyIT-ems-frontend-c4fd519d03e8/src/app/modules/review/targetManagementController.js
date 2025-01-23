(function() {
    'use strict';
    angular.module('app')
        .controller('targetManagementCtrl', ['$state', 'authService', 'EncApiService', '$scope', 'apiEndpoints' ,'globalFactory','$stateParams','DTOptionsBuilder', 'DTColumnDefBuilder','$uibModal',function($state ,authService, EncApiService, $scope, apiEndpoints ,globalFactory, $stateParams,DTOptionsBuilder,DTColumnDefBuilder,$uibModal) {
            var vm = this;
            $("#global-progress-display").show();

            vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
            vm.dtColumnDefs = [
                DTColumnDefBuilder.newColumnDef(0),
                DTColumnDefBuilder.newColumnDef(0,7,8).notSortable()
            ];

            var sortingData=function(x){
                x=x.sort(function(a,b) {return (a.categoryOrderIndex > b.categoryOrderIndex) ? 1 : ((b.categoryOrderIndex > a.categoryOrderIndex) ? -1 : 0);} );
                for(var i=0;i< x.length;i++){
                    for(var j=0;j<x[i].questions.length;j++){
                        x[i].questions=x[i].questions.sort(function(a,b){return (a.orderIndex > b.orderIndex) ? 1 : ((b.orderIndex > a.orderIndex) ? -1 : 0);} )
                    }
                }
                return x;
            }

            vm.initialData=function(){
                EncApiService('getTargetsList').then(function(response) {
                    response.data=sortingData(response.data);
                    console.log("getTargetList",response.data);

                    vm.array=[];
                    for(var i=0;i<response.data.length;i++){
                        for(var j=0;j<response.data[i].questions.length;j++){
                            var obj={};
                            var obj_question={};
                            obj.category=response.data[i].category;
                            obj_question.id=response.data[i].questions[j].id;
                            obj_question.name=response.data[i].questions[j].name;
                            obj_question.order=response.data[i].questions[j].orderIndex;
                            obj_question.description=response.data[i].questions[j].description;
                            obj.question=obj_question;
                            obj.rating=JSON.parse(response.data[i].questions[j].rating.description);
                            vm.array.push(obj);
                        }
                    }

                    vm.formSubmitted = true;
                      $("#global-progress-display").hide();

                },
                function (err) {
                    $("#global-progress-display").hide();
                    vm.formSubmitted = false;

                });
            }

            vm.targetUpdate=function(id){
                var modalReturnbck=$uibModal.open({
                    templateUrl: 'app/modules/target/html/targetGoal.html',
                    controller: 'targetGoalCtrl as vm',
                    backdrop: 'static',
                    resolve: {
                        targetId: function() {
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
            }


            vm.targetDelete=function(x){

                EncApiService('deleteTargetById',null,{id : x}).then(function(response) {
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

            vm.alertModal= function() {
                jQuery('#target-modal').modal({backdrop: 'static', keyboard: false});
            };


        }]);
})();

