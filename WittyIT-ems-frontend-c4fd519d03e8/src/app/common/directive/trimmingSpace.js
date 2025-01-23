/* =========================================================
 * Module: onlydigits.directive.js
 * Accepts only numbers in input fields
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app').directive('trimmingSpace', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, modelCtrl) {
                modelCtrl.$parsers.push(function (inputValue) {
                    if (inputValue == undefined) return '';
                    var transformedInput = inputValue.toString().replace(/[^0-9]/g, '');
                    if (transformedInput !== inputValue) {
                        modelCtrl.$setViewValue(transformedInput);
                        modelCtrl.$render();
                    }
                    return transformedInput;
                });
            }
        };
    });
})();