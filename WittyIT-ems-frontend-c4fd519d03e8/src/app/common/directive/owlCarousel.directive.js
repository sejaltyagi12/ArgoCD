(function () {
    'use strict';

    angular.module('app').directive('owlCarousel', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                if (scope.$last) {
                // ng-repeat is completed
                    element.parent().owlCarousel({
                        items : 4,
                        loop  : true,
                        margin : 30,
                        nav    : true,
                        smartSpeed :900,
                    });
                }
            }
        };
    });
        
})();