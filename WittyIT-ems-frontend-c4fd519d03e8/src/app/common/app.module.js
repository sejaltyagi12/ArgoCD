/* =========================================================
 * Module: app.module.js
 * App start configurations
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app',
        ['ui.router',
        'ngCookies',
        // '720kb.datepicker',
        'datatables',
        'ngFileUpload',
        'ngImgCrop',
        'ui.bootstrap',
        'angularjs-dropdown-multiselect',
        'ngFileSaver',
        'toaster', 
        'ngAnimate',
        ]);

    angular.module('app').config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('authInterceptorService');

    }]);

    angular.module('app').run(['authService', '$rootScope','globalFactory', '$state','$templateCache', function (authService, $rootScope, globalFactory, $state,$templateCache) {
        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            $rootScope.onlyHeader = (toState.name == 'page.login')? true: false;
            globalFactory.currentRoute = toState.name;
            globalFactory.previousRoute = fromState.name;

            if (toState.name !== 'page.login' && toState.name !== 'forgetPass'  && toState.name !== 'resetForgetPass'  && !globalFactory.checkSession()) {
                $state.go('page.login');
                return;
            }

            var routeIgnore = ['term', 'who','forgetPass','resetForgetPass'];
            var indexRoute = routeIgnore.indexOf(toState.name);
           
            if (indexRoute == -1 ) {
                authService.fillAuthData();
            }
        });
        /********to handle template cache********
        $rootScope.$on('$routeChangeStart', function(event, next, current) {
            if (typeof(current) !== 'undefined'){
                $templateCache.remove(current.templateUrl);
            }
        });
         /********to handle template cache ends********/

    }]);

    function configureTemplateFactory($provide) {
        // Set a suffix outside the decorator function 
        var cacheBuster = Date.now().toString();

        function templateFactoryDecorator($delegate) {
            var fromUrl = angular.bind($delegate, $delegate.fromUrl);
            $delegate.fromUrl = function (url, params) {
                if (url !== null && angular.isDefined(url) && angular.isString(url)) {
                    url += (url.indexOf("?") === -1 ? "?" : "&");
                    url += "v=" + cacheBuster;
                }

                return fromUrl(url, params);
            };

            return $delegate;
        }

        $provide.decorator('$templateFactory', ['$delegate', templateFactoryDecorator]);
    }

    angular.module('app').config(['$provide', configureTemplateFactory]);
})();