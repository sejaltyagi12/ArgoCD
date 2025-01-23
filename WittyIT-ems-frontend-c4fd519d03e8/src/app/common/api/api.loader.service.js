/* =========================================================
 * Module: api.loader.service.js
 * Unknown
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .service('ApiLoading', function () {
            var nProgress = window.NProgress,
                _callsInProgress = 0, _hasStarted = false;

            nProgress.configure({ trickleRate: 0.01, trickleSpeed: 400, showSpinner: false});

            return {
                start: function () {
                    ++_callsInProgress;
                    if (!_hasStarted) {
                        nProgress.start();
                        $("#global-progress-display").show();
                    } else {

                    }
                },
                stop: function () {
                    --_callsInProgress;
                    if (_callsInProgress <= 0) {
                        nProgress.done();
                        $("#global-progress-display").hide();
                    }
                }
            };
        });
})();
