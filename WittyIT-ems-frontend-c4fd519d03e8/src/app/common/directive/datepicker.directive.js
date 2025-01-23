/* =========================================================
 * Module: datepicker.directive.js
 * Jquery Datepicker
 * =========================================================
 */

 (function () {
    'use strict';
    angular.module('app').directive('datepicker', ['globalFactory', function (globalFactory) {
        return {
            require: 'ngModel',
            link: function(scope, el, attr, ngModel) {
                function datepic() {
                    var minDate = '',
                    maxDate = '',
                    changeYearMonths = '';
                    if (attr.isReviewAccount === undefined && attr.isHolidayAccount === undefined &&  attr.isReportAccount === undefined) {

                        if(attr.isResignationLastday === 'true'){
                            minDate = '-1y';
                            maxDate = '+100y';
                            changeYearMonths = true;
                        }
                        else if(attr.isDateOfBirth == 'true'){
                            minDate = '-100y';
                           maxDate =  '-16y';
                           changeYearMonths = true;
                        }
                        else{
                           minDate = (attr.minDate) ? new Date(globalFactory.convertDates(attr.minDate)) : ((attr.isLeaveAccount) ? (attr.isSickLeaves === 'true'? '-100y' : '1d'):'-100y');
                           maxDate = (attr.isLeaveAccount) ? '+29d' : '0y';
                           changeYearMonths = (attr.isLeaveAccount) ? false : true;

                       }
                       
                   }
                   else {

                    if (attr.isReviewAccount === 'true') {
                        
                        minDate = (attr.miniDate) ? new Date(new Date(globalFactory.convertDates(attr.miniDate)).setDate(new Date(globalFactory.convertDates(attr.miniDate)).getDate() + 1)) : '-100y';
                        maxDate = '-12m';
                        
                        changeYearMonths = true;
                    }
                    else {
                     
                        if(attr.isHolidayAccount === 'true'){
                            minDate = '-100y';
                            maxDate = '+100y';
                            changeYearMonths=true;
                        }
                        else{

                            if(attr.isReportAccount === 'true'){
                                minDate = (attr.minDate)? new Date(globalFactory.convertDates(attr.minDate)):'-100y';
                                maxDate = '0d';
                                changeYearMonths = true;

                            }
                            else{
                                minDate = (attr.miniDate) ? new Date(new Date(globalFactory.convertDates(attr.miniDate)).setDate(new Date(globalFactory.convertDates(attr.miniDate)).getDate() + 1)) : '0d';
                                if(attr.isEmployeeMinDate)
                                    minDate = '+1d';
                                maxDate = '+100y';
                                changeYearMonths = true;
                            }
                        }

                    }
                }
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
                if (attr.isRegistrationAccount === 'true')
                    datePickerOptions.yearRange = "-100:+1";
                if (attr.isReportAccount === 'true')
                    datePickerOptions.yearRange = "-100:+100";
                if (attr.isReviewAccount === 'false')
                    datePickerOptions.yearRange = "0:+100";
                if (attr.isReviewAccount === 'true')
                    datePickerOptions.yearRange = "-100:+100";
                if (attr.isHolidayAccount === 'true')
                    datePickerOptions.yearRange = "-0:+1";
                if (attr.isHolidayAccount === 'false')
                    datePickerOptions.yearRange = "0:+100";
                if (attr.isResignationLastday === 'true')
                    datePickerOptions.yearRange = "0:+100";
                $(el).datepicker(datePickerOptions);
            }
            datepic();

        }
    };
}]);
})();