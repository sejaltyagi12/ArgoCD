/* =========================================================
 * Module: api.auth.interceptor.js
 * Unknown
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .factory('authInterceptorService', ['$q', '$injector', '$location', 'ApiLoading', '$cookies','globalFactory', function ($q, $injector, $location, ApiLoading, $cookies,globalFactory) {

        var authInterceptorServiceFactory = {};

        var _request = function (config) {
            if (!config.noProgress) {
                ApiLoading.start();
            }

            config.headers = config.headers || {};

            // var authData = $cookies.get('authorizationData');
            var authData = localStorage.getItem('authorizationData');
            if (authData && (config.url.indexOf('password/recovery/email') === -1) && (config.url.indexOf('password/recovery/reset') === -1) && (config.url.indexOf('oauth/token') === -1)) {
                authData = JSON.parse(authData);
                config.headers.Authorization = 'Bearer ' + authData.access_token;
            }

            return config;
        }

        var _response = function (res) {
            ApiLoading.stop();
            return res;
        };

        var _responseError = function (rejection) {
            ApiLoading.stop();
            if (rejection.status === 401) {

                if ((rejection.data.code) && (rejection.data.code = 100)) {
                    /*
                     * Case that OTP is not valid but access token is valid
                     */
                    return $q.reject(rejection);
                }

                var authService = $injector.get('authService');
            }
            return $q.reject(rejection);
        }
        authInterceptorServiceFactory.request = _request;
        authInterceptorServiceFactory.response = _response;
        authInterceptorServiceFactory.responseError = _responseError;
        authInterceptorServiceFactory.getAuthtoken = function () {
            return config.headers.AuthToken || '';
        };
         
        return authInterceptorServiceFactory;

    }]);
})();