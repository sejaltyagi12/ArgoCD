(function () {
    angular.module('app').service('UserProfileService', ['EncApiService', '$q', '$state',
        function (EncApiService, $q, $state) {
            var  _userProfileFetched = false;
            return {
                // this is used to get user's info using authentication token            
                fetchUserProfile: function () {
                    var _userProfile = $q.defer()
                    EncApiService('userInfo').then(function (res) {
                        _userProfile.resolve(res);
                    }, function () {
                        _userProfileFetched = false;
                    });
                    return _userProfile.promise;
                }
            }
        }]);
})();
