/* =========================================================
 * Module: RegistrationController.js
 * Handles Registartion page behaviour
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
    .directive('validform', ['$window',function ($window) {
        return {
            restrict: 'A',
            link: function (scope, elem) {

                // set up event handler on the form element
                elem.on('submit', function () {

                    // find the first invalid element
                    var firstInvalid = elem[0].querySelector('.ng-invalid');

                    // if we find one, set focus

                    if (firstInvalid) {
                      
                         $window.scrollTo(0,$('fieldset').find('.ng-invalid').first().offset().top-80);
                    }
                });
            }
        };
    }]) .directive('ngPatternRestrict', ['$log', function ($log) {
        'use strict';
    
        function showDebugInfo() {
          //$log.debug("[ngPatternRestrict] " + Array.prototype.join.call(arguments, ' '));
        }
    
        return {
          restrict: 'A',
          require: "?ngModel",
          compile: function uiPatternRestrictCompile() {
         
    
            return function ngPatternRestrictLinking(scope, iElement, iAttrs, ngModelController) {
              var regex, // validation regex object
                oldValue, // keeping track of the previous value of the element
                caretPosition, // keeping track of where the caret is at to avoid jumpiness
                // housekeeping
                initialized = false, // have we initialized our directive yet?
                eventsBound = false, // have we bound our events yet?
                // functions
                getCaretPosition, // function to get the caret position, set in detectGetCaretPositionMethods
                setCaretPosition; // function to set the caret position, set in detectSetCaretPositionMethods
    
              //-------------------------------------------------------------------
              // caret position
              function getCaretPositionWithInputSelectionStart() {
                return iElement[0].selectionStart; // we need to go under jqlite
              }
    
              function getCaretPositionWithDocumentSelection() {
                // create a selection range from where we are to the beggining
                // and measure how much we moved
                var range = document.selection.createRange();
                range.moveStart('character', -iElement.val().length);
                return range.text.length;
              }
    
              function getCaretPositionWithWindowSelection() {
                var s = window.getSelection(),
                  originalSelectionLength = String(s).length,
                  selectionLength,
                  didReachZero = false,
                  detectedCaretPosition,
                  restorePositionCounter;
    
                do {
                  selectionLength = String(s).length;
                  s.modify('extend', 'backward', 'character');
                  // we're undoing a selection, and starting a new one towards the beggining of the string
                  if (String(s).length === 0) {
                    didReachZero = true;
                  }
                } while (selectionLength !== String(s).length);
    
                detectedCaretPosition = didReachZero ? selectionLength : selectionLength - originalSelectionLength;
                s.collapseToStart();
    
                restorePositionCounter = detectedCaretPosition;
                while (restorePositionCounter-- > 0) {
                  s.modify('move', 'forward', 'character');
                }
                while (originalSelectionLength-- > 0) {
                  s.modify('extend', 'forward', 'character');
                }
    
                return detectedCaretPosition;
              }
    
              function setCaretPositionWithSetSelectionRange(position) {
                iElement[0].setSelectionRange(position, position);
              }
    
              function setCaretPositionWithCreateTextRange(position) {
                var textRange = iElement[0].createTextRange();
                textRange.collapse(true);
                textRange.moveEnd('character', position);
                textRange.moveStart('character', position);
                textRange.select();
              }
    
              function setCaretPositionWithWindowSelection(position) {
                var s = window.getSelection(),
                  selectionLength;
    
                do {
                  selectionLength = String(s).length;
                  s.modify('extend', 'backward', 'line');
                } while (selectionLength !== String(s).length);
                s.collapseToStart();
    
                while (position--) {
                  s.modify('move', 'forward', 'character');
                }
              }
    
           
              function getValueLengthThroughSelection(input) {
                
                if (!/Opera/i.test(navigator.userAgent)) {
                  return 0;
                }
    
                input.focus();
                document.execCommand("selectAll");
                var focusNode = window.getSelection().focusNode;
                return (focusNode || {}).selectionStart || 0;
              }
    
              //-------------------------------------------------------------------
              // event handlers
              function revertToPreviousValue() {
                if (ngModelController) {
                  scope.$apply(function () {
                    ngModelController.$setViewValue(oldValue);
                  });
                }
                iElement.val(oldValue);
    
                if (!angular.isUndefined(caretPosition)) {
                  setCaretPosition(caretPosition);
                }
              }
    
              function updateCurrentValue(newValue) {
                oldValue = newValue;
                caretPosition = getCaretPosition();
              }
    
              function genericEventHandler(evt) {
               
    
               
                var newValue = iElement.val(),
                  inputValidity = iElement.prop("validity");
                if (newValue === "" && iElement.attr("type") !== "text" && inputValidity && inputValidity.badInput) {
                
                  evt.preventDefault();
                  revertToPreviousValue();
                } else if (newValue === "" && getValueLengthThroughSelection(iElement[0]) !== 0) {
                
                  evt.preventDefault();
                  revertToPreviousValue();
                } else if (regex.test(newValue)) {
                
                  updateCurrentValue(newValue);
                } else {
                  
                  evt.preventDefault();
                  revertToPreviousValue();
                }
              }
    
              //-------------------------------------------------------------------
              // setup based on attributes
              function tryParseRegex(regexString) {
                try {
                  regex = new RegExp(regexString);
                } catch (e) {
                  throw "Invalid RegEx string parsed for ngPatternRestrict: " + regexString;
                }
              }
    
              //-------------------------------------------------------------------
              // setup events
              function bindListeners() {
                if (eventsBound) {
                  return;
                }
    
                iElement.bind('input keyup click', genericEventHandler);
    
               
              }
    
              function unbindListeners() {
                if (!eventsBound) {
                  return;
                }
    
                iElement.unbind('input', genericEventHandler);
                //input: HTML5 spec, changes in content
    
                iElement.unbind('keyup', genericEventHandler);
                //keyup: DOM L3 spec, key released (possibly changing content)
    
                iElement.unbind('click', genericEventHandler);
                //click: DOM L3 spec, mouse clicked and released (possibly changing content)
    
               
    
                eventsBound = false;
              }
    
              //-------------------------------------------------------------------
              // initialization
              function readPattern() {
                var entryRegex = !!iAttrs.ngPatternRestrict ? iAttrs.ngPatternRestrict : iAttrs.pattern;
              
                tryParseRegex(entryRegex);
              }
    
              function notThrows(testFn, shouldReturnTruthy) {
                  try {
                      return testFn() || !shouldReturnTruthy;
                  } catch (e) {
                      return false;
                  }
              }
    
              function detectGetCaretPositionMethods() {
                var input = iElement[0];
    
                // Chrome will throw on input.selectionStart of input type=number
                // See http://stackoverflow.com/a/21959157/147507
                if (notThrows(function () { return input.selectionStart; })) {
                  getCaretPosition = getCaretPositionWithInputSelectionStart;
                } else {
                  // IE 9- will use document.selection
                  // TODO support IE 11+ with document.getSelection()
                  if (notThrows(function () { return document.selection; }, true)) {
                    getCaretPosition = getCaretPositionWithDocumentSelection;
                  } else {
                    getCaretPosition = getCaretPositionWithWindowSelection;
                  }
                }
              }
    
              function detectSetCaretPositionMethods() {
                var input = iElement[0];
                if (typeof input.setSelectionRange === 'function') {
                  setCaretPosition = setCaretPositionWithSetSelectionRange;
                } else if (typeof input.createTextRange === 'function') {
                  setCaretPosition = setCaretPositionWithCreateTextRange;
                } else {
                  setCaretPosition = setCaretPositionWithWindowSelection;
                }
              }
    
              function initialize() {
                if (initialized) {
                  return;
                }
                
    
                readPattern();
    
                oldValue = iElement.val();
                if (!oldValue) {
                  oldValue = "";
                }
               
    
                bindListeners();
    
                detectGetCaretPositionMethods();
                detectSetCaretPositionMethods();
    
                initialized = true;
              }
    
              function uninitialize() {
              
                unbindListeners();
              }
    
              iAttrs.$observe("ngPatternRestrict", readPattern);
              iAttrs.$observe("pattern", readPattern);
    
              scope.$on("$destroy", uninitialize);
    
              initialize();
            };
          }
        };
      }])
   
        .controller('registrationCtrl', ['$state', 'authService', 'EncApiService', '$scope', '$cookies', '$window', '$stateParams', '$http', 'apiEndpoints', '$q', 'globalFactory', 'toaster',
            function ($state, authService, EncApiService, $scope, $cookies, $window, $stateParams, $http, apiEndpoints, $q, globalFactory, toaster) {
            // $("#global-progress-display").show();

            var vm = this;
            vm.formSubmitted = "";
            var allDepartments = [];
            var empHavingDept = [];
            var adminHavingDept = [];

            vm.user = localStorage.getItem('user');
            // vm.deptId = localStorage.getItem('deptId');
            vm.departmentName = localStorage.getItem('department').toLowerCase();
            vm.designationName = localStorage.getItem('designation').toLowerCase();
            $window.scrollTo(0, 0);
            if (globalFactory.previousRoute !== '')
                localStorage.setItem('previousRoute', globalFactory.previousRoute);

            vm.nameManagerMap={};
            vm.isEdit = null;
            vm.isView = null;
            vm.isUserEdit = null;
            vm.managerErr = "";
            vm.designations = [];

            vm.registrationData = {
                companyId: null,
                password: "",
                repeatPassword: "",
                firstName: null,
                middleName: null,
                lastName: null,
                fatherName: null,
                gender: 'Male',
                dob: null,
                department: null,
                deptId: null,
                roleId: null,
                designationId: null,
                designation: null,
                joiningDate: null,
                managerId: null,
                location: null,
                officialEmail: null,
                bankName: null,
                bankAccountNumber: null,
                bankIfsc: null,
                personalEmail: null,
                permanentAddress: null,
                presentAddress: null,
                primaryPhone: null,
                secondaryPhone: null,
                panNumber: null,
                uanNumber: null,
                aadhar: null,
                bloodGroup: null,
                isActive: true,
                role: null,
                deleted: false,
                empCode:null,
                hobbies:null,
                nationality:null,
                religion:null,
                maritalStatus:'Single',
                nomineeDetails:{
                    nomineeName : null,
                    nomineeRelation : null,
                    nomineePhone : null,
                    nomineeEmail : null,
                    nomineePermanentAddress : null
                }

            };


            vm. registrationDataReset = angular.copy(vm.registrationData);

            /*
            * Fetching all drop downs data
            */
            var allApiConfig = [
                'userRole',
                'fetchDesignations',
                // 'fetchDepartmentList',
                'fetchCompanyList'
            ];


            if(vm.user ==='ADMIN' || ((vm.departmentName === 'human resource'||vm.departmentName === 'hr'||vm.departmentName ==='hr dept') 
                && (vm.designationName !== 'hr trainee' && vm.designationName !== 'hr recruiter associate' && vm.designationName !== 'hr generalist associate')))
                allApiConfig.push('fetchUserList');

            var allDropDownsData = [];

            for (var count=0; count<allApiConfig.length; count++) {
                var apiConfig = angular.copy(apiEndpoints[allApiConfig[count]]);
                apiConfig.url = globalFactory.serverUrl + '/' + apiConfig.url;
                allDropDownsData.push($http(apiConfig));
            }
           


            $q.all(allDropDownsData).then(function (response) {

                vm.roles = response[0].data;
                allDepartments = response[1].data;
                vm.companyList = response[2].data;

                empHavingDept = empHavingDept(); //for having array of department in case of role 'employee'
                adminHavingDept = adminHavingDept(); //for having array of department in case of role 'admin'

                if(vm.user ==='ADMIN' || ((vm.departmentName === 'human resource'||vm.departmentName === 'hr'||vm.departmentName ==='hr dept') 
                && (vm.designationName !== 'hr trainee' && vm.designationName !== 'hr recruiter associate' && vm.designationName !== 'hr generalist associate')))
                {
                    vm.idManagerMap = {};
                    vm.managersName = [];

                    for (var i = 0; i < response[3].data.length; i++) {
                        if(response[3].data[i].isActive === true){
                            vm.managersName.push(response[3].data[i].firstName + '-' + response[3].data[i].empCode);
                            vm.idManagerMap[response[3].data[i].firstName + '-'+response[3].data[i].empCode] = response[3].data[i].id;
                            vm.nameManagerMap[response[3].data[i].id] = response[3].data[i].firstName + '-'+response[3].data[i].empCode;
                        }
                    }

                    vm.search = null;
                }



                vm.mode = $stateParams.mode;

                if ($stateParams.id !== undefined && $stateParams.id !== '' && $stateParams.id !== null) {

                    if(vm.user ==='ADMIN' || ((vm.departmentName === 'human resource'||vm.departmentName === 'hr'||vm.departmentName ==='hr dept') && 
                    (vm.designationName !== 'hr trainee' && vm.designationName !== 'hr recruiter associate' && vm.designationName !== 'hr generalist associate'))){

                        if($stateParams.mode === 'v'){
                            //view by workforcement page through admin or hr
                            vm.isEdit = false;
                            vm.isUserEdit = false;

                            EncApiService('getIndividualEmployee',null,{'regId':$stateParams.id}).then(function(response) {
                                vm.departmentChanged(response.data.roleId, response.data.deptId, response.data.designationId);
                                vm.departmentIdCheck = response.data.deptId;
                                var keysMap = {
                                departmentName: 'department'
                                // deptId: 'departmentId'
                                };
                                for (var key in response.data) {
                                    if (key === 'bankAccountNumber')
                                        vm.registrationData[key] = parseInt(response.data[key]);
                                    else
                                        vm.registrationData[(keysMap.hasOwnProperty(key))? keysMap[key]: key] = response.data[key];
                                }
                                vm.registrationData.id = parseInt($stateParams.id);
                                // vm.manager = vm.nameManagerMap[vm.registrationData.managerId];
                                 vm.manager = response.data.managerName;
                                vm.registrationData.joiningDate = globalFactory.convertDates(vm.registrationData.joiningDate);
                                vm.registrationData.oldJoiningDate = vm.registrationData.joiningDate;
                                vm.registrationData.dob = globalFactory.convertDates(vm.registrationData.dob);
                                if( vm.registrationData.resignationDate!=null && vm.registrationData.resignationDate != undefined)
                                    vm.registrationData.resignationDate=globalFactory.convertDates(vm.registrationData.resignationDate);


                                $("#global-progress-display").hide();
                            },
                            function (err) {
                                $("#global-progress-display").hide();
                            });

                        }
                        else {
                           //view profile page 
                            vm.isUserEdit = true;


                            EncApiService('userInfo').then(function(response) {
                                vm.departmentChanged(response.data.roleId, response.data.deptId, response.data.designationId);
                                var keysMap = {
                                    departmentName: 'department'
                                };
                                for (var key in response.data) {
                                    if (key === 'bankAccountNumber')
                                        vm.registrationData[key] = parseInt(response.data[key]);
                                    else
                                        vm.registrationData[(keysMap.hasOwnProperty(key))? keysMap[key]: key] = response.data[key];
                                }
                                vm.registrationData.id = parseInt($stateParams.id);
                                if(response.data.managerName && response.data.managerCode){
                                vm.manager = response.data.managerName + '-' + response.data.managerCode;
                                }
                                vm.registrationData.joiningDate = globalFactory.convertDates(vm.registrationData.joiningDate);
                                
                                vm.registrationData.dob = globalFactory.convertDates(vm.registrationData.dob);
                                if( vm.registrationData.resignationDate!=null && vm.registrationData.resignationDate != undefined)
                                    vm.registrationData.resignationDate=globalFactory.convertDates(vm.registrationData.resignationDate);

                                $("#global-progress-display").hide();
                                },
                            function (err) {
                                $("#global-progress-display").hide();
                            });
                            
                        }

                    }
                    else{
                        vm.isUserEdit = true;

                        EncApiService('userInfo').then(function(response) {
                            vm.departmentChanged(response.data.roleId, response.data.deptId, response.data.designationId);
                            var keysMap = {
                                departmentName: 'department'
                            };
                            for (var key in response.data) {
                                if (key === 'bankAccountNumber')
                                    vm.registrationData[key] = parseInt(response.data[key]);
                                else
                                    vm.registrationData[(keysMap.hasOwnProperty(key))? keysMap[key]: key] = response.data[key];
                            }
                            vm.registrationData.id = parseInt($stateParams.id);
                            vm.manager = response.data.managerName;
                            vm.registrationData.joiningDate = globalFactory.convertDates(vm.registrationData.joiningDate);
                            // vm.oldJoiningDate = JSON.parese(JSON.stringify(globalFactory.convertDates(vm.registrationData.joiningDate)));

                            vm.registrationData.dob = globalFactory.convertDates(vm.registrationData.dob);
                            if( vm.registrationData.resignationDate!=null && vm.registrationData.resignationDate != undefined)
                                vm.registrationData.resignationDate=globalFactory.convertDates(vm.registrationData.resignationDate);

                            $("#global-progress-display").hide();
                        },
                        function (err) {
                            $("#global-progress-display").hide();
                        });
                    }
                         
                }
                else{
                    vm.registrationData.roleId = vm.roles[0].roleId;
                    vm.registrationData.role = vm.roles[0].roleName;

                    vm.registrationData.companyId = vm.companyList[0].companyId;
                    vm.registrationData.company = vm.companyList[0].companyName;

                    vm.departmentChanged(vm.registrationData.roleId , null , null);

                }

                $("#global-progress-display").hide();
            }, function (error) {
                $("#global-progress-display").hide();
                vm.message = error.message;
            });


            vm.dropDownChanged = function (regDataAttr, dataAttr, vmDataAttr) {
                var map = {
                    role: 'roleName',
                    designation: 'designationName',
                    department: 'departmentName',
                    departmentId: 'deptId',
                    company: 'companyName'
                };
                for (var count=0; count<vm[vmDataAttr].length; count++){
                    if(dataAttr === 'departmentId'){
                        if(vm[vmDataAttr][count][dataAttr] === vm.registrationData[map[dataAttr]]){
                            vm.registrationData[regDataAttr] = vm[vmDataAttr][count][map[regDataAttr]];
                            return;
                        }

                    }
                    else{
                        if(vm[vmDataAttr][count][dataAttr] === vm.registrationData[dataAttr]){
                            vm.registrationData[regDataAttr] = vm[vmDataAttr][count][map[regDataAttr]];
                            return;
                        }
                    }
                }

            };

            var empHavingDept = function(){
                var dept = [];
                for(var i = 0; i < allDepartments.length; i++){
                    if(allDepartments[i].departmentName === 'Management')
                        continue;
                    dept.push(allDepartments[i]);
                }
                return dept;
            }

            var adminHavingDept = function(){
                var dept = [];
                for(var i = 0; i < allDepartments.length; i++){
                    if(allDepartments[i].departmentName === 'Human Resource' || allDepartments[i].departmentName === 'Management'){
                        if(allDepartments[i].departmentName === 'Human Resource'){
                            var obj = {} , arr = [];
                            obj.departmentName = allDepartments[i].departmentName;
                            obj.departmentId  =  allDepartments[i].departmentId;
                            for(var j= 0 ; j < allDepartments[i].designationWrappers.length ; j++){
                                if(allDepartments[i].designationWrappers[j].designationName === 'HR Trainee' || 
                                allDepartments[i].designationWrappers[j].designationName === 'HR Recruiter Associate' || allDepartments[i].designationWrappers[j].designationName ==='HR Generalist Associate')
                                continue;
                                arr.push(allDepartments[i].designationWrappers[j]);
                            }
                            obj.designationWrappers = arr;
                            dept.push(obj); 
                        }
                        else 
                            dept.push(allDepartments[i]);
                    }
                       
                }
                return dept;
            } 

            vm.departmentChanged = function(roleId, deptId, designationId){
                vm.designations = [];
                var designationArr = [] , deptArr = [] ;
                if(vm.registrationData.deptId===9){
                    vm.manager = null;
                }

                if(roleId !== 2 && deptId === null && designationId === null){
                    vm.departments = empHavingDept;
                    vm.registrationData.deptId = vm.departments[0].departmentId;
                   
                    vm.registrationData.department = vm.departments[0].departmentName;
                    vm.registrationData.oldDeptId = vm.registrationData.deptId;
                    vm.registrationData.oldDepartment = vm.registrationData.department;
                    vm.departmentChanged(roleId, vm.registrationData.deptId, null);

                }else if(roleId !== 2 && deptId !== null){
                    vm.departments = empHavingDept;
                    for(var i = 0; i < vm.departments.length; i++){
                        if(vm.departments[i].departmentId === deptId){
                            vm.designations = vm.departments[i].designationWrappers;

                            if(designationId === null){
                                vm.registrationData.designation = vm.designations[0].designationName;
                                vm.registrationData.designationId = vm.designations[0].designationId;
                            }
                            else
                                vm.registrationData.designationId = designationId;
                            return;
                        }

                    }

                }
                else{ 
                    vm.departments = adminHavingDept;
                    if(deptId === null){
                        vm.registrationData.deptId = vm.departments[0].departmentId;
                        vm.registrationData.department = vm.departments[0].departmentName;

                        vm.designations = vm.departments[0].designationWrappers;
                        vm.registrationData.designation = vm.designations[0].designationName;
                        vm.registrationData.designationId = vm.designations[0].designationId;
                    }
                    else{
                        vm.registrationData.deptId = deptId;
                        for( var i = 0; i < vm.departments.length; i++){
                            if(vm.departments[i].departmentId === deptId){
                                vm.designations = vm.departments[i].designationWrappers;
                                if(designationId === null){
                                    vm.registrationData.designation = vm.departments[i].designationWrappers[0].designationName;
                                    vm.registrationData.designationId = vm.departments[i].designationWrappers[0].designationId;
                                }
                                else
                                    vm.registrationData.designationId = designationId;
                                return;
                            }

                        }
                    }

                }
            }

            vm.mangerChanged =function(x){
                vm.managerChoosed = x;
                vm.managerErr = "";
                vm.registrationData.managerId = vm.idManagerMap[x];

                // if(vm.managersName.indexOf(vm.managerChoosed) == -1){
                //     vm.managerErr = "Please enter a valid manager";
                // }
            };




            vm.registerEmployee = function () {
                vm.message = '';
                vm.message = "";
                var data = angular.copy(vm.registrationData);
                data.joiningDate = globalFactory.convertDates(data.joiningDate);
                data.dob = globalFactory.convertDates(data.dob);

                EncApiService('registerEmployee', { 'data': data }).then(function(response) {
                   
                    vm.formSubmitted = true;
                    toasterShow();      
                    vm.registrationData = angular.copy(vm.registrationDataReset);

                    vm.registrationData.roleId = vm.roles[0].roleId;
                    vm.registrationData.role = vm.roles[0].roleName;

                    vm.registrationData.companyId = vm.companyList[0].companyId;
                    vm.registrationData.company = vm.companyList[0].companyName;

                    vm.departmentChanged(vm.registrationData.roleId, null, null);

                    $scope.registrationForm.$setPristine();
                    vm.manager = "";
                    $window.scrollTo(0, angular.element(document.getElementById('topElm')).offsetTop);
                },
                function (err) {
                    vm.formSubmitted = false;
                    $window.scrollTo(0, angular.element(document.getElementById('topElm')).offsetTop);
                    if(err !== undefined && err !== null && err !== '')
                        vm.message = err.data.message;
                    toasterShow(); 
                    $window.scrollTo(0, 0);
                });


            };


            vm.editEmployee = function () {
                vm.message = "";
                var data = angular.copy(vm.registrationData);
                data.joiningDate = globalFactory.convertDates(data.joiningDate);
                data.dob = globalFactory.convertDates(data.dob);
                if(data.resignationDate!=null)
                data.resignationDate=globalFactory.convertDates(data.resignationDate);

                EncApiService('registerEmployee', { 'data': data }).then(function(response) {
                    vm.formSubmitted = true;
                    toasterShow(); 
                    $window.scrollTo(0, angular.element(document.getElementById('topElm')).offsetTop);
                    

                },
                function (err) {
                    vm.formSubmitted = false;
                    vm.message = err.data.message;
                    if(vm.message == "Can't change joining date after applying leave. Please contact support team"){
                        vm.registrationData.joiningDate =  vm.registrationData.oldJoiningDate;
                        
                    }
                    toasterShow(); 
                    $window.scrollTo(0, angular.element(document.getElementById('topElm')).offsetTop);
                });
            };

            vm.func = function(){
                vm.isUserEdit = false;
                vm.isUserNotEditableField = true;
            }

            vm.goToPreviousPage = function () {

                if ($stateParams.id !== undefined && $stateParams.id !== '' && $stateParams.id !== null) {
                    if(localStorage.getItem('previousRoute') !== '' && localStorage.getItem('previousRoute') !== null && localStorage.getItem('previousRoute') !== undefined)
                        $state.go(localStorage.getItem('previousRoute'));
                    else 
                        $state.go('page.dashboard');
                }
                else{
                      $state.go('page.dashboard');
                }
            };


            var pop = function(type,title){
                toaster.pop({
                    type: type,
                    title: title,
                    timeout: 5000
                });
            };

            var toasterShow = function(){
                if(vm.formSubmitted && vm.mode === 'empV'){
                    pop('success',' Updated successful');
                }
                else if(vm.formSubmitted && vm.mode !== 'empV'){
                    pop('success','Registration successful');
                }
                else if(vm.formSubmitted === false && vm.message ===''){
                    pop('error',' Unable to process your request. Please retry or contact system admin');
                }
                else{
                    pop('error',vm.message);
                }
            }

        }]);
})();