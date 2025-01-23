/* =========================================================
 * Module: myModal.directive.js
 * modal popup
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app').directive('modal', function () {
    return {
        restrict: 'EA',
        scope: {
            title: '=modalTitle',
            header: '=modalHeader',
            body: '=modalBody',
            footer: '=modalFooter',
            callbackbuttonleft: '&ngClickLeftButton',
            callbackbuttonright: '&ngClickRightButton',
            handler: '=lolo'
        },
        templateUrl: 'app/common/myModal.html',
        transclude: true,
        controller: function ($scope) {
            $scope.handler = 'pop'; 
        },
    };
});
})();