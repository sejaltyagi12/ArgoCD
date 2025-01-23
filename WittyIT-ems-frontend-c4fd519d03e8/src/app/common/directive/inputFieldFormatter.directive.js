/* =========================================================
 * Module: inputFieldFormatter.directive.js
 * Accepts regex according to input fields
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app').directive('inputFieldFormatter', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, modelCtrl) {
                modelCtrl.$parsers.push(function (inputValue) {
                    if (inputValue) {
                        var regex = new RegExp(attrs.inputRegex);
                        var transformedInput = inputValue.toString().replace(regex, '').replace(/^\s\s*/, '');
                        // var transformedInput = inputValue.toString().replace(regex, '');
                        if (transformedInput !== inputValue) {
                            modelCtrl.$setViewValue(transformedInput);
                            modelCtrl.$render();
                        }
                        return transformedInput;
                    }
                    return null;
                });
            }
        };
    });
})();


