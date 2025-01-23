/* =========================================================
 * Module: config.route.js
 * Defining all the application routes
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/page/login');


            $stateProvider


                .state('page', {
                    abstract: true,
                    templateUrl: 'app/common/page.layout.html',
                    url: '/page',
                    cache: false,
                    reload: true,

                })

                .state('page.login', {
                    url: '/login',
                    templateUrl: 'app/modules/auth/html/login.html',
                    controller: 'loginCtrl as vm',
                    cache: false,
                    reload: true,

                })

                .state('page.changePass', {
                    url: '/changePass/:id',
                    templateUrl: 'app/modules/auth/html/changePass.html',
                    controller: 'loginCtrl as vm',
                    cache: false,
                    reload: true,
                })

                .state('page.registration', {
                    url: '/registration/:mode/:id',
                    templateUrl: 'app/modules/registration/html/registration.html',
                    controller: 'registrationCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.leaveApplication', {
                    url: '/leaveApplication/:id',
                    templateUrl: 'app/modules/leaveManagement/html/leaveApplication.html',
                    controller: 'leaveApplicationCtrl as vm',
                    cache: false,
                    reload: true,
                })

                .state('page.leaveAccount', {
                    url: '/leaveAccount/:empId',
                    // url: '/leaveAccount/',
                    templateUrl: 'app/modules/leaveManagement/html/leaveAccount.html',
                    controller: 'leaveAccountCtrl as vm',
                    cache: false,
                    reload: true,
                })

                .state('page.policy', {
                    url: '/policy/:policyType',
                    templateUrl: 'app/modules/PoliciesAndProcedure/html/policy.html',
                    controller: 'policyCtrl',
                    cache: false,
                    reload: true
                })

                .state('page.workForceManagement', {
                    url: '/workForceManagement',
                    templateUrl: 'app/modules/workForceManagement/html/workForceManagement.html',
                    controller: 'workForceCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.approveLeave', {
                    url: '/approveLeave',
                    templateUrl: 'app/modules/leaveManagement/html/approvedLeave.html',
                    controller: 'leaveApprovedCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.dashboard', {
                    url: '/dashboard',
                    templateUrl: 'app/modules/registration/html/dashboard.html',
                    controller: 'dashboardCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.charts', {
                    url: '/charts',
                    templateUrl: 'app/modules/workForceManagement/html/pieCharts.html',
                    controller: 'pieChartsCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.leaveDetails', {
                    url: '/leaveDetails',
                    templateUrl: 'app/modules/leaveManagement/html/leaveDetails.html',
                    controller: 'leaveDetailsCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.underConstruction', {
                    url: '/underConstruction',
                    templateUrl: 'app/modules/underConstruction.html',
                    cache: false,
                    reload: true
                })
                .state('page.createReview', {
                    url: '/createReview',
                    templateUrl: 'app/modules/review/html/createReview.html',
                    controller: 'createReviewCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.reviewDetails', {
                    url: '/reviewDetails',
                    templateUrl: 'app/modules/review/html/reviewDetails.html',
                    controller: 'reviewDetailCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.review', {
                    url: '/review/:cycleId/:empId',
                    templateUrl: 'app/modules/review/html/review.html',
                    controller: 'reviewCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.selfEvaluation', {
                    url: '/selfEvaluation/:cycleId/:empId',
                    templateUrl: 'app/modules/review/html/selfEvaluation.html',
                    controller: 'selfEvaluationCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.reviewOfAll', {
                    url: '/reviewOfAll/:cycleId',
                    templateUrl: 'app/modules/review/html/reviewAll.html',
                    controller: 'reviewAllCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.roaster', {
                    url: '/roaster',
                    templateUrl: 'app/modules/setup/html/roaster.html',
                    controller: 'roasterCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.UpdateHoliday', {
                    url: '/UpdateHoliday/:id',
                    templateUrl: 'app/modules/setup/html/updateHoliday.html',
                    controller: 'updateHolidayCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.AddDepartment', {
                    url: '/AddDepartment',
                    templateUrl: 'app/modules/designationManagement/html/addDepartment.html',
                    controller: 'departmentCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.AddDesignation', {
                    url: '/AddDesignation',
                    templateUrl: 'app/modules/designationManagement/html/addDesignation.html',
                    controller: 'designationCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.UpdateDepartment', {
                    url: '/UpdateDepartment/:deptId',
                    templateUrl: 'app/modules/designationManagement/html/updateDepartment.html',
                    controller: 'departmentCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.UpdateDesignation', {
                    url: '/UpdateDesignation/:designationId',
                    templateUrl: 'app/modules/designationManagement/html/updateDesignation.html',
                    controller: 'designationCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.feedback', {
                    url: '/feedback/:id',
                    templateUrl: 'app/modules/ideaAndSuggestion/html/feedback.html',
                    controller: 'feedbackCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.feedbackDetail', {
                    url: '/feedbackDetail',
                    templateUrl: 'app/modules/ideaAndSuggestion/html/feedbackDetail.html',
                    controller: 'feedbackDetailCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.teamFeedback', {
                    url: '/teamFeedback',
                    templateUrl: 'app/modules/ideaAndSuggestion/html/teamFeedback.html',
                    controller: 'teamFeedbackCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.HolidayList', {
                    url: '/HolidayList',
                    templateUrl: 'app/modules/setup/html/holidayListing.html',
                    controller: 'updateHolidayCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.DepartmentList', {
                    url: '/DepartmentList',
                    templateUrl: 'app/modules/designationManagement/html/departmentListing.html',
                    controller: 'departmentCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.DesignationList', {
                    url: '/DesignationList',
                    templateUrl: 'app/modules/designationManagement/html/designationListing.html',
                    controller: 'designationCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.roasterUpdate', {
                    url: '/rosterUpload',
                    templateUrl: 'app/modules/setup/html/roasterUpload.html',
                    controller: 'roasterCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.target', {
                    url: '/target',
                    templateUrl: 'app/modules/review/html/target.html',
                    controller: 'targetCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.Category', {
                    url: '/category',
                    templateUrl: 'app/modules/review/html/categoryManagement.html',
                    controller: 'categoryManagementCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.targetManagement', {
                    url: '/targetManagement',
                    templateUrl: 'app/modules/review/html/targetManagement.html',
                    controller: 'targetManagementCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.ratingManagement', {
                    url: '/ratingManagement',
                    templateUrl: 'app/modules/review/html/ratingManagement.html',
                    controller: 'ratingManagementCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.reviewHistory', {
                    url: '/reviewHistory',
                    templateUrl: 'app/modules/review/html/reviewHistory.html',
                    controller: 'reviewHistoryCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.teamEvaluationHistory', {
                    url: '/teamEvaluationHistory',
                    templateUrl: 'app/modules/review/html/teamEvaluationHistory.html',
                    controller: 'teamEvaluationHistoryCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.notification', {
                    url: '/notification',
                    templateUrl: 'app/modules/setup/html/notification.html',
                    controller: 'notificationCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.notificationHistory', {
                    url: '/notificationHistory',
                    templateUrl: 'app/modules/setup/html/notificationHistory.html',
                    controller: 'notificationHistoryCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('forgetPass', {
                    url: '/password/forget',
                    templateUrl: 'app/modules/auth/html/forgetPassMail.html',
                    controller: 'forgetPassCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('resetForgetPass', {
                    url: '/password/reset',
                    templateUrl: 'app/modules/auth/html/resetForgetPass.html',
                    controller: 'forgetPassCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.salarySlip', {
                    url: '/salary/slip',
                    templateUrl: 'app/modules/payroll/html/salarySlip.html',
                    controller: 'salarySlipCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.salarySlipUpload', {
                    url: '/salarySlip/Upload',
                    templateUrl: 'app/modules/setup/html/salarySlipUpload.html',
                    controller: 'salarySlipUploadCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.salarySlipAdmin', {
                    url: '/salarySlip',
                    templateUrl: 'app/modules/setup/html/salarySlipAdmin.html',
                    controller: 'salarySlipUploadCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.viewCurrentBreakdown', {
                    url: '/viewCurrentBreakdown',
                    templateUrl: 'app/modules/payroll/html/viewCurrentBreakdown.html',
                    controller: 'viewCurrentBreakdownCtrl as vm',
                    cache: false,
                    reload: true
                })

                .state('page.report', {
                    url: '/report/:type',
                    templateUrl: 'app/modules/report/html/report.html',
                    controller: 'reportCtrl as vm',
                    cache: false,
                    reload: true
                })
                .state('page.selfResignation', {
                    url: '/self/resignation',
                    templateUrl: 'app/modules/resignation/html/resignNew.html',
                    controller: 'resignNewCtrl as vm',
                    cache: false,
                    reload: true


                })
                .state('page.subordinateResignation', {
                    url: '/subordinate/resignation',
                    templateUrl: 'app/modules/resignation/html/subordinateResignation.html',
                    controller: 'subordinateResignationCtrl as vm',
                    cache: false,
                    reload: true

                })
                .state('page.medicalUpload', {
                    url: '/medical/upload/:id',
                    templateUrl: 'app/modules/leaveManagement/html/medicalUpload.html',
                    controller: 'medicalUploadCtrl as vm',
                    cache: false,
                    reload: true,

                })
                .state('page.termsAndConditions', {
                    url: '/terms/conditions',
                    templateUrl: 'app/modules/auth/html/termsAndConditions.html',

                })

                .state('page.acceptance', {
                    url: '/acceptance',
                    templateUrl: 'app/modules/auth/html/acceptance.html',
                    controller: 'acceptanceCtrl as vm'

                })

                .state('page.subordinateResignationDetail', {
                    url: '/subordinate/resignation/detail/:id/:view',
                    templateUrl: 'app/modules/resignation/html/subordinateResignationDetail.html',
                    controller: 'subordinateResignationDetailCtrl as vm',
                    cache: false,
                    reload: true

                })


                .state('page.resignHistory', {
                    url: '/resign/history',
                    templateUrl: 'app/modules/resignation/html/resignHistory.html',
                    controller: 'resignHistoryCtrl as vm'

                });




        }]);

})();

