/* =========================================================
 * Module: api.data.service.js
 * Protocol to communicate with server
 * =========================================================
 */
(function () {
    'use strict';

    function injectQueryfullParams(_url_, paramObj) {
        var url = _url_;
        angular.forEach(paramObj, function (value, key) {
            url = url.replace(':' + key, value);
        });

        return url;
    }

    angular.module('app')
        .provider('EncApiService', function () {
            var provider = this,

                prevErrCode = null;

            provider.$get = ['$rootScope', '$state', '$http', '$q', '$location', 'apiEndpoints', 'authInterceptorService', 'globalFactory', '$cookies', function ($rootScope, $state, $http, $q, $location, apiEndpoints, authInterceptorService, globalFactory, $cookies) {
                var host = globalFactory.serverUrl;

                function apiService(endpointName, _config_, restfullParams, flagLoadingScreen) {

                    if (flagLoadingScreen === undefined)
                        var flagLoadingScreen = false;

                    var config = apiEndpoints[endpointName];
                    var defer = $q.defer();
                    if (config === undefined) {
                        throw new Error('API config not found in endpointConfig. Please check api.backend.endpoints.config.js');
                        return;
                    } else {
                        config = angular.copy(config);
                    }

                    /*
                     * Inject Restful params here.
                     */
                    if (config.hasQueryParmas) {
                        config.url = injectQueryfullParams(config.url, restfullParams);
                    } else {
                        config.url = config.url;
                    }

                    config.url = host + '/' + config.url;

                    angular.extend(config, _config_);
                    config.method = config.method || 'GET';

                    function success(response) {
                        if (flagLoadingScreen)
                            $("#global-progress-display").hide();
                        defer.resolve(response);
                    }

                    function error(response) {
                        if (flagLoadingScreen)
                            $("#global-progress-display").hide();
                        switch (response.status) {
                            /*
                             * user is attempting to access forbidden data
                             */
                            case 401:

                                $state.go('page.login');
                                defer.reject(response);

                                break;

                            default:

                                defer.reject(response);
                                break;
                        };

                        prevErrCode = response.status;
                    }



                    if (globalFactory.currentRoute !== 'page.login' && !globalFactory.checkSession()) {



                        if (globalFactory.currentRoute === 'forgetPass' || (globalFactory.currentRoute === 'resetForgetPass' && $location.search().email !== undefined && $location.search().token !== undefined)) {

                        }
                        else {

                            $state.go('page.login');
                            $("#global-progress-display").hide();
                            return;
                        }
                    }

                    /*
                     * Create defer object and supply
                     */

                    if (flagLoadingScreen)
                        $("#global-progress-display").show();

                    $http(config).then(success, error);

                    return defer.promise;


                }

                apiService.host = host;
                apiService.authToken = authInterceptorService.getAuthtoken;

                return apiService;
            }];

        });
})();