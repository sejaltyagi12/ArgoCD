/* =========================================================
 * Module: datepicker.directive2.js
 * Jquery Datepicker
 * =========================================================
 */

(function () {
    'use strict';
    angular.module('app').directive('datepicker2', ['globalFactory', function (globalFactory) {
        return {
            require: 'ngModel',

            link: function(scope, el, attr, ngModel) {
                function datepic() {
                    var minDate = '',
                    maxDate = '',
                    changeYearMonths = '';
                    minDate = (attr.minDate) ? new Date(globalFactory.convertDates(attr.minDate)) : '-100y';

                    maxDate = '+100y';

                    changeYearMonths=true;
                 
                    var datePickerOptions = {
                        dateFormat: 'dd/mm/yy',
                        showStatus: true,
                        showWeeks: true,
                        highlightWeek: true,
                        minDate: minDate,
                        maxDate: maxDate,
                        changeMonth: changeYearMonths,
                        changeYear: changeYearMonths,
                        onSelect: function (date) {
                            if (attr.initiateId)
                                ngModel.$setViewValue(null);
                            ngModel.$setViewValue(date);
                            scope.$apply();
                        }
                    };
                    if (attr.isLeaveAccount === 'true')
                        datePickerOptions.yearRange = "-100:+100";
                    $(el).datepicker(datePickerOptions);
                }
                datepic();


            }
        };
    }]);
})();