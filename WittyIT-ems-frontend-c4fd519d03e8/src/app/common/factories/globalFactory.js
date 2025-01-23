/* =========================================================
 * Module: globalFactory.js
 * Maintains global methods and data 
 * =========================================================
 */

(function () {
	'use strict';

	angular.module('app')
		.factory('globalFactory', ['$cookies', function ($cookies) {
			var globalFactoryItems = {};

			globalFactoryItems.leaveList = 0;
			globalFactoryItems.newCycleInitiated = false;
			globalFactoryItems.currentRoute = '';
			//globalFactoryItems.serverUrl = 'http://localhost:8080/ems';
			globalFactoryItems.serverUrl = 'http://35.232.248.128:8080/ems';
			globalFactoryItems.isLoading = true;
			globalFactoryItems.isModalOpen = false;
			globalFactoryItems.reviewCount = [];
			globalFactoryItems.disableMenu = false;
			globalFactoryItems.triggerDashboard = false;


			/*
			 * Converts dates to either 'mm/dd/yy' or 'dd/mm/yy'
			 */
			globalFactoryItems.convertDates = function (dateString) {
				var splitDate = dateString.split('/');
				return splitDate[1] + '/' + splitDate[0] + '/' + splitDate[2];
			};
			globalFactoryItems.sortDateFormat = function (dateString) {
				return new Date(dateString);
			};

			globalFactoryItems.convertDateString = function (dateString) {
				var time = new Date(dateString);
				return time.getDate() + '/' + (time.getMonth() + 1) + '/' + time.getFullYear();
			}
			/*
			 * Detects if session is available
			 */
			globalFactoryItems.checkSession = function () {

				var authData = localStorage.getItem('authorizationData');
				return (authData && (authData != undefined) && (authData != null) && (Object.keys(JSON.parse(authData)).length > 0)) ? true : false;
			};

			return globalFactoryItems;
		}]);
})();
