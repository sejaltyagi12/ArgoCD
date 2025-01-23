/* =========================================================
 * Module: ngTooltip.directive.js
 * display tooltip for password field
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app').directive('ngTooltip', function () {
         return {
        scope:{
            minlen : '=',
            pattern : '='
        },
        link: function (scope, element, attribute) {
            function createError(){
                //Our tooltip element
                var $target = angular.element('#tooltipcontent');
                var innerHtml = '';
                var first = '';
                var second = '';
               
                //Here you can customize what attributes you accept and how you show them on tooltip
               
                first = '<i class="fa fa-check" aria-hidden="true"></i>';
                if (scope.minlen == true) {
                     first = '<i class="fa fa-times" aria-hidden="true"></i>';
                }

                second = '<i class="fa fa-check" aria-hidden="true"></i>' ;
                if (scope.pattern== true) {
                    second = '<i class="fa fa-times" aria-hidden="true"></i>';
                }

                innerHtml = "<div class='rule-condition-tooltip'><p>" 
                + first + 'Passwords must be between 8 and 20 characters</p><p>' 
                + second + "Must contain one lower and one uppercase letter</p></div>";

                element.hover(function (e) {
                      //Show tooltip
                    angular.element($target).show();
                }, function () {
                    //Hide tooltip upon element mouseleaving
                    angular.element($target).hide();
                });

                element.on('keyup', function(e) { 
                //Set inner content
                angular.element($target).html(innerHtml);
                });
           }
           createError();
           
           scope.$watch(function(){ 
                return scope.minlen;
           }, function(){
                createError();
           }, true);
           
           scope.$watch(function(){ 
                return scope.pattern;
           }, function(){
                createError();
           }, true);
       }
    };
    });
})();