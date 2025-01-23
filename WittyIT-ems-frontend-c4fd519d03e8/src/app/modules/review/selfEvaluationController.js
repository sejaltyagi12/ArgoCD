(function () {
    'use strict';
    angular.module('app')
    .controller('selfEvaluationCtrl', ['$state','$stateParams', 'authService', 'EncApiService','globalFactory', '$q',function ($state,$stateParams, authService, EncApiService,globalFactory,$q) {

        var vm = this;
        vm.isEdit = null;
        vm.isView = null;
        vm.feedbackData=[];
        vm.averageRatingEmp=0;
        vm.averageRatingManager=0;
        vm.ratingDescription = [];
        vm.user = localStorage.getItem('user');
        vm.department = localStorage.getItem('department').toLowerCase();
        vm.enable=false;
        vm.hasQues = false;

            // Functions start

            vm.updateAverage = updateAverage;
            vm.initialData = initialData;
            vm.ratingDesFunction = ratingDesFunction;
            vm.reviewSave = reviewSave;
            vm.reviewSubmit = reviewSubmit;
            vm.goToPreviousPage = goToPreviousPage;
            vm.alertModal= alertModal;
            vm.sortingData=sortingData;
            vm.enabledSave=enabledSave;
            
            
            // Functions end

            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);


            function initialData(){
                $("#global-progress-display").show();
                vm.cycleId=$stateParams.cycleId;
                EncApiService('getEvaluationCycleList',null,{'dept':''}).then(function(response) {
                    for(var i=0;i<response.data.length;i++){
                        if(parseInt($stateParams.cycleId)==response.data[i].id){
                            vm.startDate=globalFactory.convertDates(response.data[i].startDate);
                            vm.endDate=globalFactory.convertDates(response.data[i].endDate);
                            vm.departmentName=response.data[i].department.deptName;
                            if(response.data[i].isCompleted==false){

                                if(vm.user!='ADMIN' && (vm.department!='human resource' && vm.department!='hr' && vm.department!='hr dept')){
                                    vm.isEmp = ($stateParams.empId == '') ? true : false;
                                }
                                else{
                                    if($stateParams.empId == '')
                                        vm.isEmp = true ;
                                    else 
                                        vm.specialAuthority=true;
                                }

                            }
                            else
                                vm.cycleCompleted=true;

                        }
                    }
                    if($stateParams.empId!=undefined && $stateParams.empId!=null && $stateParams.empId!=''){
                        vm.cycleIdRoute=$stateParams.cycleId;
                        vm.empId=parseInt($stateParams.empId);
                        EncApiService('getCurrentCycleEmployeeData',null,{'id':vm.empId,'cycleId':vm.cycleId}).then(function(response) {

                            vm.reviewDataContent=vm.sortingData(response.data);


                            vm.reviewDataContent=vm.ratingDesFunction(vm.reviewDataContent);
                            updateAverage();   // Updating average values
                            $("#global-progress-display").hide();
                        },
                        function (err) {
                            vm.formSubmitted=false;
                            vm.message=err.data.message;
                            vm.alertModal();
                            $("#global-progress-display").hide();
                            
                        });
                    }
                    else{
                        EncApiService('userInfo').then(function(response) {
                            
                            EncApiService('getCurrentCycleEmployeeData',null,{'id':response.data.id,'cycleId':vm.cycleId}).then(function(response) {
                                
                                vm.reviewDataContent=vm.sortingData(response.data);


                                
                                vm.reviewDataContent=vm.ratingDesFunction(vm.reviewDataContent);
                                updateAverage();   // Updating average values
                                $("#global-progress-display").hide();
                            },
                            function (err) {
                                vm.formSubmitted=false;
                                vm.message=err.data.message;
                                vm.alertModal();
                                $("#global-progress-display").hide();
                            });
                        },
                        function (err) {});
                    }
                },
                function (err) {
                    vm.formSubmitted=false;
                    vm.message=err.data.message;
                    vm.alertModal();

                });
            }
            
            function sortingData(x){
                if(x.categories.length>0)
                {
                    x.categories=x.categories.sort(function(a,b) {return (a.categoryOrderIndex > b.categoryOrderIndex) ? 1 : ((b.categoryOrderIndex > a.categoryOrderIndex) ? -1 : 0);} );
                    for(var i=0;i<x.categories.length;i++){
                     if(x.categories[i].questions.length>0)
                     {
                       vm.hasQues =  true;
                       x.categories[i].questions=x.categories[i].questions.sort(function(a,b){return (a.orderIndex > b.orderIndex) ? 1 : ((b.orderIndex > a.orderIndex) ? -1 : 0);} )
                   }
               }
           }
           return x;
           

       }

       function ratingDesFunction(employeeArr){
        for(var i=0;i<employeeArr.categories.length;i++){
            for(var j=0;j<employeeArr.categories[i].questions.length;j++){
             employeeArr.categories[i].questions[j].rating.des=JSON.parse(employeeArr.categories[i].questions[j].rating.description);
             if(employeeArr.categories[i].questions[j].target.evaluationHistory!=null){

                var evalHistory = angular.copy(employeeArr.categories[i].questions[j].target.evaluationHistory);
                if($stateParams.empId!='' && $stateParams.empId!=undefined && $stateParams.empId!=null){
                    if(evalHistory.hasOwnProperty('managerSubmitted'))
                        vm.isMangerSubmitted=evalHistory.managerSubmitted;
                }
                else{
                    if(evalHistory.hasOwnProperty('employeeSubmitted'))
                        vm.isEmpSubmitted=evalHistory.employeeSubmitted;
                }
                if(evalHistory.hasOwnProperty('employeeRating')){
                    if(evalHistory.employeeRating==-1 || evalHistory.employeeRating==null)
                        evalHistory.employeeRating=null;
                    else
                        evalHistory.employeeRating=evalHistory.employeeRating.toString();
                }
                if(evalHistory.hasOwnProperty('managerRating')){
                    if(evalHistory.managerRating==-1 || evalHistory.managerRating==null)
                        evalHistory.managerRating=null;
                    else
                        evalHistory.managerRating=evalHistory.managerRating.toString();
                }
                employeeArr.categories[i].questions[j].target.evaluationHistory = angular.copy(evalHistory);
            }
        }                
    }
    return employeeArr;   
} 

function reviewSave(){
    var dataArray=[];
    for(var i=0;i<vm.reviewDataContent.categories.length;i++){
        for(var j=0;j<vm.reviewDataContent.categories[i].questions.length;j++){
            var obj={};
            obj.targetId=vm.reviewDataContent.categories[i].questions[j].target.id;
            if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory!=null){
                var evalHistory = angular.copy(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory);
                if(evalHistory.hasOwnProperty('id'))
                {
                    vm.EvalutionId = evalHistory.id;
                    obj.id=evalHistory.id
                }
                if(evalHistory.employeeRating==undefined || evalHistory.employeeRating==null)
                    obj.employeeRating=-1;
                else
                    obj.employeeRating=parseInt(evalHistory.employeeRating);
                if(evalHistory.employeeReason==undefined )
                    obj.employeeReason='';
                else
                    obj.employeeReason=evalHistory.employeeReason;
            }  
            else{
                obj.employeeRating=-1;
                obj.employeeReason='';
                

            }
            obj.employeeSubmitted=false;
            obj.cycleId=parseInt($stateParams.cycleId);
            dataArray.push(obj);
        }
    }
    EncApiService('employeeSubmitEvaluation', { 'data': dataArray }).then(function(response) {
        vm.formSubmitted = true;
        vm.savedData=true;
        vm.alertModal();
        if( vm.EvalutionId == null ||  vm.EvalutionId == "" || vm.EvalutionId == undefined){
            EncApiService('userInfo').then(function(response) {
                EncApiService('getCurrentCycleEmployeeData',null,{'id':response.data.id,'cycleId':vm.cycleId}).then(function(response) {

                    globalFactory.newCycleInitiated =  false; 
                    vm.reviewDataContent=vm.sortingData(response.data)
                    vm.reviewDataContent=vm.ratingDesFunction(vm.reviewDataContent);
                                    updateAverage();   // Updating average values
                                },
                                function (err) {});
            },
            function (err) {});
        }


    },
    function (err) {
        vm.formSubmitted = false;
        vm.message=err.data.message;
        vm.alertModal();
        
    });
}

function reviewSubmit(){
    if($stateParams.empId!='' && $stateParams.empId!=undefined && $stateParams.empId!=null){
        var dataArray=[];
        for(var i=0;i<vm.reviewDataContent.categories.length;i++){
            for(var j=0;j<vm.reviewDataContent.categories[i].questions.length;j++){
                var obj={};
                obj.targetId=vm.reviewDataContent.categories[i].questions[j].target.id;
                if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory!=null){
                    if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.hasOwnProperty('id'))
                        obj.id=vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.id

                    if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.managerRating==undefined || vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.managerRating==null)
                        obj.managerRating=-1;
                    else
                        obj.managerRating=parseInt(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.managerRating);

                    if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.managerReason==undefined )
                        obj.managerReason='';
                    else
                      obj.managerReason=vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.managerReason;
              }
              obj.managerSubmitted=true;
              obj.cycleId=parseInt($stateParams.cycleId);
              obj.employeeId=vm.reviewDataContent.employeeId;
              dataArray.push(obj);
          }
      }

      EncApiService('managerSubmitEvaluation', { 'data': dataArray }).then(function(response) {
        vm.formSubmitted = true;
        vm.savedData=false;
        vm.isMangerSubmitted=true;
        vm.submittedData=true;
        vm.alertModal();
    },
    function (err) {
        vm.formSubmitted = false;
        vm.message=err.data.message;
        vm.alertModal();
    });
  }
  else{
    var dataArray=[];
    for(var i=0;i<vm.reviewDataContent.categories.length;i++){
        for(var j=0;j<vm.reviewDataContent.categories[i].questions.length;j++){
            var obj={};
            obj.targetId=vm.reviewDataContent.categories[i].questions[j].target.id;
            if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory!=null){
                if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.hasOwnProperty('id'))
                    obj.id=vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.id
                if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeRating==undefined || vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeRating==null)
                    obj.employeeRating=-1;
                else
                    obj.employeeRating=parseInt(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeRating);

                if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeReason==undefined )
                    obj.employeeReason='';
                else
                    obj.employeeReason=vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeReason;
                
            }
            obj.employeeSubmitted=true;
            obj.cycleId=parseInt($stateParams.cycleId);
            dataArray.push(obj);
        }
    }
    EncApiService('employeeSubmitEvaluation', { 'data': dataArray }).then(function(response) {
        vm.formSubmitted = true;
        vm.savedData=false;
        vm.isEmpSubmitted=true;
        vm.submittedData=true;
        globalFactory.newCycleInitiated =  false; 
        vm.alertModal();
    },
    function (err) {
        vm.formSubmitted = false;
        vm.message=err.data.message;
        vm.alertModal();
    });
}
}

function goToPreviousPage(cycleId) {
    cycleId=parseInt(cycleId);
    
    if(globalFactory.previousRoute == ''){
        var previousRoute = localStorage.getItem('previousRoute');
        globalFactory.previousRoute = (previousRoute != '' && previousRoute != undefined && previousRoute != null)? previousRoute: 'page.dashboard';
    }
    $state.go(globalFactory.previousRoute, { 'cycleId':cycleId });
    
};

function alertModal() {
    jQuery('#review-modal').modal({backdrop: 'static', keyboard: false});
};

function updateAverage(){
    var totalEmployeeRating = 0;
    var totalManagerRating = 0;
    vm.averageRatingEmp = 0;
    vm.averageRatingManager = 0;
    for(var i=0;i<vm.reviewDataContent.categories.length;i++){
        for(var j=0;j<vm.reviewDataContent.categories[i].questions.length;j++){
            if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory !== null ){
                var evalHistory = angular.copy(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory);
                if(evalHistory.hasOwnProperty('employeeRating')){
                    if (evalHistory.employeeRating !== undefined && evalHistory.employeeRating !== null && evalHistory.employeeRating !== -1) {
                        vm.averageRatingEmp += parseInt(evalHistory.employeeRating); 
                        totalEmployeeRating += 1;
                    }
                }
                if(evalHistory.hasOwnProperty('managerRating')){
                    if (evalHistory.managerRating !== undefined && evalHistory.managerRating !== null && evalHistory.managerRating !== -1) {
                        vm.averageRatingManager += parseInt(evalHistory.managerRating); 
                        totalManagerRating += 1;
                    }
                }
            }
        }
    }
    if (totalEmployeeRating !== 0)
        vm.averageRatingEmp = Math.round( vm.averageRatingEmp/totalEmployeeRating * 10 ) / 10;
    if (totalManagerRating !== 0)
        vm.averageRatingManager = Math.round(vm.averageRatingManager/totalManagerRating * 10 ) / 10;
}

function enabledSave(x){
    for(var i=0;i<vm.reviewDataContent.categories.length;i++){
        for(var j=0;j<vm.reviewDataContent.categories[i].questions.length;j++){
            if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory != null){
                if(vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeRating!=undefined||
                    vm.reviewDataContent.categories[i].questions[j].target.evaluationHistory.employeeReason!=undefined){
                    vm.enable=true;
                return;
            }
            
            else{
                vm.enable=false;
            }
        }
    }
}

}

}]);
})();