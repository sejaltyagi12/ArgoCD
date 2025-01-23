
(function () {
    'use strict';

    angular.module('app')
        .controller('isLoadingCtrl', ['globalFactory', function (globalFactory) {
            var vm = this;
            vm.isglobalFactory=globalFactory
            console.log("vm.isglobalFactory",vm.isglobalFactory.isLoading)
        }]);
})();