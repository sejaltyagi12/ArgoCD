/* =========================================================
 * Module: lightSliser.directive.js
 * load lightSlider when ng-repeat is complete
 * =========================================================
 */

(function () {
    'use strict';
        angular.module('app').directive('lightgallery', function() {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    if (scope.$last) {
                    // ng-repeat is completed
                        element.parent().lightSlider({
                            keyPress:false,
                            item:4,
                            loop:false,
                            onSliderLoad: function() {
                                $('#content-slider').removeClass('cS-hidden');
                            } 
                        });
                    }
                }
            };
        });
        angular.module('app').directive('holidaygallery', ['$timeout', function($timeout) {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    if (scope.$last) {
                        element.parent().lightSlider({
                            item: 1,
                            loop: true,
                            slideMargin: 0, 
                            enableDrag: false,
                            onBeforeSlide: function (el) {

                                $('.holiday-label').css('color', '#000');
                            },
                            onAfterSlide: function (el) {
                                $timeout(function() {
                                    $('.holiday-label').css('color', 'white');
                                }, 100);
                            }
                             
                        });
                    }
                }
            };
        }]);
})();