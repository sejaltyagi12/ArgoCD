/* =========================================================
 * Module: workForceManagementController.js
 * Handles work force of employees
 * =========================================================
 */

(function () {
  'use strict';

  angular.module('app')
    .controller('workForceCtrl', ['$window','$state', 'authService', 'EncApiService', '$http', 'apiEndpoints', '$q', 'globalFactory', 'DTOptionsBuilder', 'DTColumnDefBuilder', '$uibModal', function ($window,$state, authService, EncApiService, $http, apiEndpoints, $q, globalFactory, DTOptionsBuilder, DTColumnDefBuilder, $uibModal) {
      var vm = this;


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

      $("#global-progress-display").show();

      /*
       * Fetching all filters and employee data
       */
      var allApiConfig = [
        'fetchDesignations',
        'fetchCompanyList',
        'fetchUserList'
      ];

      var allFiltersData = [];

      vm.searchAll = function () {
        // createTable();

        for (var count = 0; count < allApiConfig.length; count++) {
          var apiConfig = angular.copy(apiEndpoints[allApiConfig[count]]);
          apiConfig.url = globalFactory.serverUrl + '/' + apiConfig.url;
          allFiltersData.push($http(apiConfig));
        }

        $q.all(allFiltersData).then(function (response) {
          vm.allDesignations = response[0].data;

          vm.departments = response[0].data;
          vm.departments.unshift({ departmentName: 'All', departmentId: -1 });

          vm.companyList = response[1].data;
          vm.companyList.unshift({ companyName: 'All', companyId: -1 });

          vm.userList = response[2].data;
          vm.userList.forEach(function (value, index) {
            value.joiningDate = globalFactory.sortDateFormat(value.joiningDate);
          });

          vm.completeUserList = angular.copy(vm.userList);


          //  $("#global-progress-display").hide();
        },
          function (error) {
            $("#global-progress-display").hide();
          });

      };


      vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withDisplayLength(10);
      vm.dtColumnDefs = [
        DTColumnDefBuilder.newColumnDef(0),
        DTColumnDefBuilder.newColumnDef([0, 7, 8]).notSortable(),
        DTColumnDefBuilder.newColumnDef([5]).withOption('type', 'date')
      ];

      vm.filters = {};

      /*
       * Applying filters based on drop downs
       */
      vm.applyFilter = function () {
        var newUserList = [];

        for (var i = 0; i < vm.completeUserList.length; i++) {
          var toBeAdded = true;

          for (var key in vm.filters) {
            if (vm.filters[key] == -1 || vm.filters[key] == '')
              continue;
            toBeAdded = ((vm.completeUserList[i][key] == vm.filters[key]) && toBeAdded) ? true : false;
          }

          if (toBeAdded)
            newUserList.push(vm.completeUserList[i]);
        }
        // createTable();
        vm.userList = angular.copy(newUserList);

      };

      vm.departmentChanged = function (departmentId) {
        vm.designations = [];
        for (var i = 0; i < vm.departments.length; i++) {
          if (vm.departments[i].departmentId === departmentId) {
            if (vm.departments[i].designationWrappers !== undefined && vm.departments[i].departmentId !== -1) {
              vm.designations = angular.copy(vm.departments[i].designationWrappers);
              vm.designations.unshift({ designation: 'All', designationId: -1 });
              vm.filters.designationId = -1;
            }
            else {
              vm.designations.push({ designation: 'All', designationId: -1 });
              vm.filters.designationId = -1;
            }

          }
        }

      }





      vm.resign = function (id, firstName, lastName) {
        var isActive = null;
        var reqId = id + "_isActive";
        var changeStatusId = id + "_changeStatus";
        var statusChangedTo = document.getElementById(changeStatusId).innerHTML;


        // changes


        var modalReturnbck = $uibModal.open({
          templateUrl: 'app/modules/workForceManagement/html/resign.html',
          controller: 'resignCtrl as vm',
          backdrop: 'static',
          resolve: {
            userId: function () {
              return id;
            },
            fullName: function () {
              return firstName + '' + lastName + ' ' + lastName;
            }

          }
        });

        modalReturnbck.result.then(function (status) {
          if (status) {
            document.getElementById(reqId).innerHTML = "Inactive";
            document.getElementById(changeStatusId).innerHTML = "Edit Resignation";
          }
        }, function () {

        });
      }

      vm.activateOrDeactivateEmployee = function (userId, isActive) {
        // var url = 'employee/active/inactive/' + userId + '/' + status;
        EncApiService('activateOrDeactivateEmployee', {}, { 'userId': userId, 'status': isActive }).then(function (response) {
          console.log(response.data);
          $window.location.reload();
        });
      };      


      vm.alertModal = function alertModal() {
        jQuery('#workForceManagement-modal').modal();
      };

    }]);
})();