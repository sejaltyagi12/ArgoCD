/* =========================================================
 * Module: reviewAllController.js
 * Handles reviewAll page behaviour
 * =========================================================
 */
 (function() {
    'use strict';
    angular.module('app')
    .controller('reviewAllCtrl', ['$state', 'authService', 'EncApiService', 'DTOptionsBuilder', 'DTColumnDefBuilder','$stateParams','globalFactory',function($state, authService, EncApiService, DTOptionsBuilder, DTColumnDefBuilder,$stateParams,globalFactory) {

        var vm = this;
        var managerListData=null;
        vm.itemPerPage = 10;

        if(globalFactory.previousRoute!='page.selfEvaluation' && globalFactory.previousRoute !== '' )
            localStorage.setItem('previousRoute', globalFactory.previousRoute);
        else 
            localStorage.setItem('previousRoute','page.teamEvaluationHistory');
        


        var sortingData=function(x){
            x=x.sort(function(a,b) {return (a.categoryOrderIndex > b.categoryOrderIndex) ? 1 : ((b.categoryOrderIndex > a.categoryOrderIndex) ? -1 : 0);} );
            for(var i=0;i< x.length;i++){
                for(var j=0;j<x[i].questions.length;j++){
                    x[i].questions=x[i].questions.sort(function(a,b){return (a.orderIndex > b.orderIndex) ? 1 : ((b.orderIndex > a.orderIndex) ? -1 : 0);} )
                }
            }
            return x;
        }
        
        vm.initialData = function() {
            EncApiService('getCountOfSubmittedEvaluations').then(function(response) {
                globalFactory.reviewCount = response.data;
            },
            function (err){});
            $("#global-progress-display").show();
            vm.cycleId=$stateParams.cycleId;
            EncApiService('getEvaluationCycleList',null,{'dept':''}).then(function(response) {
                for(var i=0;i<response.data.length;i++){
                    if(parseInt($stateParams.cycleId)==response.data[i].id){
                        vm.startDate=globalFactory.convertDates(response.data[i].startDate);
                        vm.endDate=globalFactory.convertDates(response.data[i].endDate);
                        vm.departmentName=response.data[i].department.deptName;
                        if(response.data[i].isCompleted==false)

                            console.log("isCompleted is false")

                    }
                }
                EncApiService('getListAllQuestions',null,{'cycleId':vm.cycleId}).then(function(response) {
                    var reviewTargets = sortingData(response.data);

                    vm.targets = [];
                    vm.category = [];
                    vm.category.push({"category": "Name"});
                    vm.targets.push({"targetName": ""});
                    
                    for (var i = 0; i < reviewTargets.length; i++) {
                        var obj = {};
                        for (var j = 0; j < reviewTargets[i].questions.length; j++) {
                            var targetObj = {}
                            targetObj.questionId = reviewTargets[i].questions[j].id;
                            targetObj.targetName = reviewTargets[i].questions[j].name;
                            vm.targets.push(targetObj);
                        }
                        if (reviewTargets[i].questions.length > 0) {
                            obj.colspan = reviewTargets[i].questions.length;
                            obj.categoryId = reviewTargets[i].categoryId;
                            obj.category = reviewTargets[i].category;
                            vm.category.push(obj)
                        }
                    }

                    vm.category.push({"category": "Action"});
                    vm.targets.push({"targetName": ""});

                    vm.employeeRatingArray = [];

                    EncApiService('getManagerList',null,{'cycleId':vm.cycleId}).then(function(response) {
                        vm.lengthManagerList=response.data.length;

                        if(response.data.length >0){
                            vm.managerList = response.data;
                            vm.arr = [];
                            for (var empIndex = 0; empIndex < vm.managerList.length; empIndex++) {
                                var obj = {};
                                obj.empname = vm.managerList[empIndex].employeeName;
                                obj.empId = vm.managerList[empIndex].employeeId;
                                obj.employeeRating = [];
                                obj.employeeRating.push(vm.managerList[empIndex].employeeName);


                                
                                for (var idx = 1; idx < vm.targets.length - 1; idx++) {

                                    var idRatingFound = false;
                                    for (var eval1 = 0; eval1 < vm.managerList[empIndex].categories.length; eval1++) {
                                                // Extracting questions.
                                                var questionsArr = angular.copy(vm.managerList[empIndex].categories[eval1].questions);
                                                if(questionsArr.length > 0){
                                                    obj.cycleId = questionsArr[0].target.evaluationHistory.cycle.id;
                                                    obj.isCompleted=questionsArr[0].target.evaluationHistory.cycle.isCompleted;
                                                    obj.managerSubmitted=questionsArr[0].target.evaluationHistory.managerSubmitted;
                                                    for (var pdx=0; pdx< questionsArr.length; pdx++){
                                                        if (vm.targets[idx].questionId == questionsArr[pdx].id){
                                                            idRatingFound = true;
                                                            var rating = questionsArr[pdx].target.evaluationHistory.employeeRating;
                                                            if (rating === -1)
                                                                obj.employeeRating.push(0);
                                                            else    
                                                                obj.employeeRating.push(rating);
                                                        }
                                                    } 
                                                }
                                                
                                            }
                                            if(!idRatingFound)
                                                obj.employeeRating.push(null);
                                        }
                                        vm.arr.push(obj);
                                    }
                                    managerListData = angular.copy(vm.arr);

                                    // changes

                                    vm.paginate();


                                }
                                $("#global-progress-display").hide();
                            },
                            function(err) {
                             $("#global-progress-display").hide();
                             vm.formSubmitted=false;
                             vm.message=err.data.message;
                             vm.alertModal();
                         });

                },
                function(err) {
                    vm.formSubmitted=false;
                    vm.message=err.data.message;
                    vm.alertModal();
                });
},
function (err) {
    vm.formSubmitted=false;
    vm.message=err.data.message;
    vm.alertModal();

});

}



vm.applyFilter = function (searchString) {
    var result = [];
    if(searchString !== undefined && searchString !== ''){
        loop1:for(var i=0;i<vm.arr.length;i++){
            loop2:for(var j=0; j<vm.arr[i]['employeeRating'].length; j++){
                if(String(vm.arr[i]['employeeRating'][j]).toLowerCase().indexOf(searchString.toLowerCase()) !== -1){
                    result.push(vm.arr[i]);
                    continue loop1;
                }
            }
            if(vm.arr[i]['isCompleted'] || vm.arr[i]['managerSubmitted']) {
                if("view".indexOf(searchString.toLowerCase()) !== -1)
                    result.push(vm.arr[i]);
            }
            else if(!vm.arr[i]['isCompleted'] || !vm.arr[i]['managerSubmitted']){
                if("pending".indexOf(searchString.toLowerCase()) !== -1)
                    result.push(vm.arr[i]);
            }
        }
        vm.arr=angular.copy(result)
    }
    else 
        vm.arr=managerListData;
    vm.paginate();
};




vm.paginate = function(){
    if(vm.managerList.length > 0){
        vm.paginatedArr = [];
        for(var idx=0; idx<vm.arr.length; idx+=vm.itemPerPage){
            var singlePageDataArr = [];
            for (var pdx=idx ; pdx<(idx+vm.itemPerPage); pdx++) {
                if(pdx >= vm.arr.length)
                    break;
                singlePageDataArr.push(vm.arr[pdx]);
            }
            vm.paginatedArr[idx/vm.itemPerPage] = angular.copy(singlePageDataArr);
        }
        if (vm.paginatedArr.length > 0){
            vm.employeeRatingArray = angular.copy(vm.paginatedArr[0]);
            vm.totalColumns = vm.employeeRatingArray[0].employeeRating.length + 2;
        }
        else{
            vm.employeeRatingArray = [];
            vm.totalColumns = 0;
        }
    }
}

vm.changePaginatedTableData = function (idx){
    vm.employeeRatingArray = angular.copy(vm.paginatedArr[idx]); 
    console.log("PAGINATED DATA : ",vm.employeeRatingArray);
}

vm.alertModal= function alertModal() {
    jQuery('#reviewAll-modal').modal({backdrop: 'static', keyboard: false});
};
vm.goToPreviousPage=function(){
    var previousRoute = localStorage.getItem('previousRoute');
    globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
    $state.go(globalFactory.previousRoute);
}

}]);
})();

