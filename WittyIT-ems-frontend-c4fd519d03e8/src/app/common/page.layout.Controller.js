/* =========================================================
 * Module: page.layout.controller.js
 * handles: menutab
 * =========================================================
 */

(function () {
  'use strict';
  angular.module('app')
    .controller('pagelayoutCtrl', ['authService', '$state', 'EncApiService', '$cookies', 'globalFactory', '$timeout', 'constantValues',
      function (authService, $state, EncApiService, $cookies, globalFactory, $timeout, constantValues) {
        var vm = this;
        vm.constantValues = constantValues;
        vm.disableMenu = false;
        vm.globalFactoryRelay = globalFactory;
        vm.policy = vm.constantValues.policy

        if (localStorage.getItem('policyAccepted') === 'false') {
          vm.globalFactoryRelay.disableMenu = true;

          $timeout(function () {
            $('#sub3').trigger('click');
            vm.openColorText('', 'list5');
          }, 100);
        }

        if (vm.globalFactoryRelay.triggerDashboard == true) {
          console.log("welcome to dashboard page");
        }


        EncApiService('leaveAppliedCount').then(function (response) {
          globalFactory.leaveList = response.data;

        }, function (err) {
          vm.message = err.message;
        });

        EncApiService('userInfo').then(function (res) {
          var info = res.data;
          vm.userOfficialEmail = info.officialEmail;
          vm.UserName = info.firstName;
          vm.middleName = info.middleName;
          vm.lastName = info.lastName;
          vm.userId = info.id;
          vm.isOnlyEmployee = info.isOnlyEmployee;
          localStorage.setItem('user', info.role);
          localStorage.setItem('userRoleId', info.roleId);
          localStorage.setItem('department', info.departmentName);
          localStorage.setItem('deptId', info.deptId);
          localStorage.setItem('designation', info.designation);
          localStorage.setItem('designationId', info.designationId);
          vm.user = localStorage.getItem('user');
          vm.deptId = localStorage.getItem('deptId');
          vm.departmentName = localStorage.getItem('department');
          vm.designationName = localStorage.getItem('designation');
        }, function (err) {
          vm.message = err.message;
        });

        EncApiService('signalReview').then(function (res) {
          globalFactory.newCycleInitiated = res.data;
        }, function (err) {
          vm.message = err.message;
        });

        EncApiService('getCountOfSubmittedEvaluations').then(function (response) {
          globalFactory.reviewCount = response.data;

        }, function (err) {
          vm.message = err.message;
        });

        vm.logOut = function () {
          localStorage.removeItem('user');
          authService.logOut();
        };

        vm.openColorText = function (event, id) {

          var openActiveElements = angular.element(document.getElementsByClassName("clicked-tab"));
          for (var i = 0; i <= openActiveElements.length - 1; i++) {
            openActiveElements[i].classList.remove("clicked-tab");
            if ($('#' + openActiveElements[i].id).parent('ul').parent('li').children('a') != undefined) {
              // $( '#'+openActiveElements[i].id ).parent('ul').parent('li').children('a').css( "color", "#6d89b0" );
              // $( '#'+openActiveElements[i].id ).parent('ul').parent('li').children('a').children('i').css( "color", "#6d89b0" );
            }
          }
          $('#' + id).addClass('clicked-tab');
          // if ($('#' + id).parent('ul').parent('li').children('a') != undefined) {
          //   $('#' + id).parent('ul').parent('li').children('a').css("color", "white");
          //   $('#' + id).parent('ul').parent('li').children('a').children('i').css("color", "#ff4200");
          // }
          if (event != '')
            event.stopPropagation();
          //location.reload();
        }

        /*
        /* closes other open menus in left-panel
        */
        vm.openThis = function (id) {
          var openElements = angular.element(document.getElementsByClassName("in")); //open elements
          var elem = document.getElementById(id); //element clicked

          // loops through open elements and closes them(other then menu clicked)
          for (var i = 0; i < openElements.length; i++) {
            if (openElements[i] != elem) {
              openElements[i].classList.remove("in");
            }
          }

        };

      }]);
})();