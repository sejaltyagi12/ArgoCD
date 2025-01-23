/* =========================================================
 * Module: authService.js
 * Authentication Service
 * =========================================================
 */

 (function () {
    'use strict';

    angular.module('app')
    .factory('authService', ['$http', '$q', '$state', 'EncApiService', '$cookies', function ($http, $q, $state, EncApiService, $cookies) {

        var authServiceFactory = {};

        authServiceFactory.authentication = {
            isAuth: false,
            access_token: '',
            userInfo: {}
        };

        authServiceFactory.login = function (loginData) {
            var deferred = $q.defer();

            EncApiService('login', {
                headers: {
                    "Authorization": 'Basic ' + btoa("witty-portal" + ':' + "1234567890"),
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                }
            }, { 'email': loginData.UserName, 'pass': loginData.Password }).then(function (res) {
                var response = res.data;            
                authServiceFactory.authentication.isAuth = true;         
                authServiceFactory.authentication.access_token = response.access_token;            
                    // $cookies.put('authorizationData', JSON.stringify(response), {path: '/'});
                    localStorage.setItem('authorizationData', JSON.stringify(response), {path: '/'});
                    authServiceFactory.getUserInfo();
                    deferred.resolve(response);
                }, function (err) {
                    authServiceFactory.logOut();
                    deferred.reject(err);
                });

            return deferred.promise;
        };

        authServiceFactory.getUserInfo = function () {
            EncApiService('userInfo').then(function (res) {

                var authData = localStorage.getItem('authorizationData');
                if (authData) {
                    authData = JSON.parse(authData);
                    authServiceFactory.authentication.isAuth = true;         
                    authServiceFactory.authentication.access_token = authData.access_token;
                    authServiceFactory.authentication.userInfo = res.data;

                    localStorage.removeItem('authorizationData');
                    localStorage.setItem('authorizationData', JSON.stringify(authServiceFactory.authentication), {path: '/'});
                }
            });
        };

        authServiceFactory.logOut = function () {
         
            localStorage.removeItem('authorizationData', { path: '/' });
            authServiceFactory.authentication.isAuth = false;        
            $state.go('page.login');

        };

        authServiceFactory.fillAuthData = function () {
            
            var authData = localStorage.getItem('authorizationData');
            if (authData) {
                authData = JSON.parse(authData);
                authServiceFactory.authentication.isAuth = true;          
                authServiceFactory.authentication.access_token = authData.access_token;
                authServiceFactory.authentication.userInfo = authData.userInfo;
            }
            else {
                authServiceFactory.logOut();
            }
        };

        authServiceFactory.getAuthData = function () {
            
            var authData = localStorage.getItem('authorizationData');
            if (authData) {
                authData = JSON.parse(authData);
            }
            return authData;
        };

        return authServiceFactory;
    }]);
})();