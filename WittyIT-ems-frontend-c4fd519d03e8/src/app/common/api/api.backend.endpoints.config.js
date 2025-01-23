/* =========================================================
 * Module: api.backend.endpoints.config.js
 * Defining all the application API(s)
 * =========================================================
 */

(function () {
    'use strict';

    angular.module('app')
        .value('apiEndpoints', {
            'login': {
                url: 'oauth/token?grant_type=password&username=:email&password=:pass',
                method: 'POST',
                hasQueryParmas: true
            },
            'tfauth': {
                url: 'login/tfauth'
            },
            'resetqr': {
                url: 'user/resetqr?userId=:userId',
                hasQueryParmas: true
            },
            'toggletfa': {
                url: 'user/toggletfa?userId=:userId',
                hasQueryParmas: true
            },
            'userInfo': {
                url: 'employee/details'
            },
            'registerEmployee': {
                url: 'employee/registration',
                method: 'POST'
            },
            'userRole': {
                url: 'role/details',
                method: 'GET'
            },

            'fetchDesignations': {
                url: 'designation/department',
                method: 'GET'
            },
            'fetchDepartmentList': {
                url: 'department/list',
                method: 'GET'
            },
            'fetchCompanyList': {
                url: 'company/list',
                method: 'GET'
            },
            'sendLeaveApplication': {
                url: 'leave/apply',
                method: 'POST'
            },
            'fetchLeaveTypes': {
                url: 'leave/list/types'
            },
            'fetchUserList': {
                url: 'employee/getAll',
                method: 'GET'
            },
            'changePassword': {
                url: 'employee/changePassword',
                method: 'POST',
            },

            'leaveApprove': {
                url: 'leave/approve',
                method: 'POST'
            },
            'leaveReq': {
                url: 'leave/teamAppliedLeave/list'
            },

            'getIndividualEmployee': {
                url: 'employee/get?id=:regId',
                method: 'GET',
                hasQueryParmas: true
            },
            'resetPassEmp': {
                url: 'employee/resetPassword',
                method: 'POST'
            },

            'leaveDetails': {
                url: 'leave/applied?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },

            'employeeLeaveDetail': {
                url: 'leave/leaveAccountDetails?id=:id',
                method: 'GET',
                hasQueryParmas: true

            },
            'chartData': {
                url: 'employee/getChartData',
                method: 'GET'
            },
            'profilePicUpload': {
                url: 'employee/avatar/upload',
                // headers: {'Content-Type': null},
                method: 'POST'
            },
            'holidayList': {
                url: 'holiday/list?admin=:admin',
                method: 'GET',
                hasQueryParmas: true
            },
            'departmentList': {
                url: 'department/list',
                method: 'GET',
                hasQueryParmas: true
            },
            'designationList': {
                url: 'designation/list',
                method: 'GET',
                hasQueryParmas: true
            },
            'addHoliday': {
                url: 'holiday/save',
                method: 'PUT'
            },
            'addDepartment': {
                url: 'department',
                method: 'POST'
            },
            'addDesignation': {
                url: 'designation',
                method: 'POST'
            },
            'updateDepartment': {
                url: 'department/:deptId',
                method: 'PUT',
                hasQueryParmas: true
            },
            'updateDesignation': {
                url: 'designation/:designationId',
                method: 'PUT',
                hasQueryParmas: true
            },
            'deleteDepartment': {
                url: 'department/:deptId',
                method: 'DELETE',
                hasQueryParmas: true
            },
            'deleteDesignation': {
                url: 'designation/:designationId',
                method: 'DELETE',
                hasQueryParmas: true
            },
            'deleteHoliday': {
                url: 'holiday/delete?id=:id',
                method: 'DELETE',
                hasQueryParmas: true
            },
            'getEvents': {
                url: 'employee/upcoming/events',
                method: 'GET'
            },
            'getNotification': {
                url: 'notification/get?admin=:admin',
                method: 'GET',
                hasQueryParmas: true
            },
            'getHoliday': {
                url: 'holiday/get?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },
            'getDepartment': {
                url: 'department/:deptId',
                method: 'GET',
                hasQueryParmas: true
            },
            'getDesignation': {
                url: 'designation/:designationId',
                method: 'GET',
                hasQueryParmas: true
            },
            'uploadRoster': {
                url: 'roaster/upload ',
                method: 'POST'
            },
            'getRoster': {
                url: 'roaster/get?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },
            'postIdeaSuggestion': {
                url: 'idea/post ',
                method: 'PUT'
            },
            'IdeasPostedByEmployee': {
                url: 'idea/list',
                method: 'GET'
            },
            'IdeasPostedByTeam': {
                url: 'idea/team/list',
                method: 'GET'
            },
            'ideaById': {
                url: '/idea/get?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },
            'readStatusIdea': {
                url: 'idea/status?id=:id&read=:read',
                method: 'PUT',
                hasQueryParmas: true
            },
            'sendEventWish': {
                url: 'notification/event?id=:id&eventType=:eventType',
                method: 'PUT',
                hasQueryParmas: true
            },
            'addCategory': {
                url: 'evaluation/group/add',
                method: 'PUT',
            },
            'getCategoryById': {
                url: 'evaluation/group/get?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },
            'editCategory': {
                url: 'evaluation/group/edit',
                method: 'PUT'
            },
            'deleteCategory': {
                url: 'evaluation/group/delete?id=:id',
                method: 'DELETE',
                hasQueryParmas: true
            },
            'addRating': {
                url: 'evaluation/rating/add ',
                method: 'PUT',
            },
            'getCategoryList': {
                url: 'evaluation/group/list',
                method: 'GET',
            },
            'getRatingList': {
                url: 'evaluation/rating/list',
                method: 'GET'
            },
            'getTargetsList': {
                url: 'evaluation/target/list',
                method: 'GET',
            },
            'addTarget': {
                url: 'evaluation/target/add',
                method: 'PUT',
            },
            'editTarget': {
                url: 'evaluation/target/edit',
                method: 'PUT',
            },

            'getRatingById': {
                url: 'evaluation/rating/get?id=:id ',
                method: 'GET',
                hasQueryParmas: true
            },
            'initiateEvaluation': {
                url: 'evaluation/start',
                method: 'POST'
            },
            'getTargetsOfEmp': {
                url: 'evaluation/employee/targets ',
                method: 'GET'
            },
            'getEvaluationCycleList': {
                url: 'evaluation/cycle/list?dept=:dept',
                method: 'GET',
                hasQueryParmas: true
            },

            'employeeSubmitEvaluation': {
                url: 'evaluation/employee/submit ',
                method: 'POST'
            },

            'getListAllQuestions': {
                url: 'evaluation/target/list?cycleId=:cycleId',
                method: 'GET',
                hasQueryParmas: true

            },
            'getCurrentCycleEmployeeData': {
                url: 'evaluation/employee/get?id=:id&cycleId=:cycleId',
                method: 'GET',
                hasQueryParmas: true

            },
            'getManagerList': {
                url: 'evaluation/manager/list?cycleId=:cycleId ',
                method: 'GET',
                hasQueryParmas: true

            },
            'getTargetById': {
                url: 'evaluation/target?id=:id ',
                method: 'GET',
                hasQueryParmas: true

            },
            'editTargetById': {
                url: 'evaluation/target/edit',
                method: 'PUT',
                hasQueryParmas: true
            },

            'deleteTargetById': {
                url: 'evaluation/target/delete?id=:id ',
                method: 'DELETE',
                hasQueryParmas: true
            },
            'managerSubmitEvaluation': {
                url: 'evaluation/manager/submit',
                method: 'POST',

            },
            'endEvaluationCycle': {
                url: 'evaluation/cycle/end?id=:id',
                method: 'POST',
                hasQueryParmas: true

            },
            'actiDeactiveEmployee': {
                url: 'employee/activate?id=:id&active=:active ',
                method: 'PUT',
                hasQueryParmas: true
            },
            'signalReview': {
                url: 'evaluation/notification',
                method: 'GET',
            },
            'notificationSend': {
                url: 'notification/admin ',
                method: 'PUT',
            },
            'deleteNotification': {
                url: 'notification/admin?id=:id',
                method: 'Delete',
                hasQueryParmas: true
            },
            'sendForgetPassMail': {
                url: 'password/recovery/email',
                method: 'POST'
            },
            'forgetPassReset': {
                url: 'password/recovery/reset',
                method: 'POST'
            },
            'evaluationCycleCheck': {
                url: 'evaluation/cycle/active',
                method: 'GET'
            },
            'payrollPaySlipUpload': {
                url: 'payroll/pay/upload?month=:month&year=:year',
                method: 'POST',
                hasQueryParmas: true

            },
            'getEmployeePaySlip': {
                url: 'payroll/pay/slips?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },

            'downloadEmployeePaySlip': {
                url: 'payslip/pdf/?id=:id ',
                method: 'GET',
                hasQueryParmas: true
            },


            'currentBreakDown': {
                url: 'payslip/current',
                method: 'GET',
            },
            'downloadLeaveReport': {
                url: 'reports/leave/download.xls?start=:start&end=:end',
                method: 'GET',
                hasQueryParmas: true
            },
            'downloadJoineeReport': {
                url: 'reports/joinees/download.xls?start=:start&end=:end',
                method: 'GET',
                hasQueryParmas: true
            },
            'resign': {
                url: 'employee/resign',
                method: 'PUT'
            },
            'holidayImageUpload': {
                url: 'holiday/image/upload?id=:id',
                method: 'POST',
                hasQueryParmas: true
            },
            'evaluationReportDownload': {
                url: 'reports/evaluation/download.xls?cycle=:cycleId ',
                method: 'GET',
                hasQueryParmas: true
            },
            'getEvaluationCycleListForTeamEvaluation': {
                url: 'evaluation/cycle/list/team-evaluation',
                method: 'GET',
                // hasQueryParmas: true
            },
            'getEvaluationCycleListForSelfEvaluation': {
                url: 'evaluation/cycle/list/self-evaluation',
                method: 'GET',
            },
            'getCountOfSubmittedEvaluations': {
                url: 'evaluation/cycle/list/team-evaluation/notification ',
                method: 'GET',
            },

            'uploadMedicalCertificate': {
                url: 'leave/upload?id=:id',
                method: 'Post',
                hasQueryParmas: true
            },

            'submitEmployeeResignation': {
                url: 'resignation/employee ',
                method: 'PUT'
            },
            'getEmployeeResignation': {
                url: 'resignation/submitted-list?emp=:emp',
                method: 'GET',
                hasQueryParmas: true
            },
            'getEmployeeResignationById': {
                url: 'resignation/submitted-list?id=:id',
                method: 'GET',
                hasQueryParmas: true
            },

            'leaveAppliedCount': {
                url: 'leave/team/applied/count',
                method: 'GET',
            },
            'leaveWithDraw': {
                url: 'leave/withdraw?id=:id',
                method: 'PUT',
                hasQueryParmas: true
            },
            'employeeAcceptPolicy': {
                url: 'employee/policy/accept',
                method: 'PUT',
            },
            'resignationTypes': {
                url: 'resignation/types/resignation-types ',
                method: 'GET',
            },
            'resignationReason': {
                url: 'resignation/types/resignation-reasons ',
                method: 'GET',
            },
            'resignationApprovalManager': {
                url: 'resignation/approve/manager ',
                method: 'POST',
            },
            'resignationApprovalHrAdmin': {
                url: 'resignation/approve/hr ',
                method: 'POST',
            },
            'getCurrentResignation': {
                url: 'resignation/employee/current ',
                method: 'GET',
            },
            'getPolicyPdf': {
                url: 'policy/pdf ',
                method: 'GET',
            },
            'getPolicyHtml': {
                url: 'policy/html',
                method: 'GET',
            },
            'getEmployeeHandbook': {
                url: 'policy/get?name=:name',
                method: 'GET',
                hasQueryParmas: true
            },
            'reHireEmployee': {
                url: 'resignation/activate/employee/:empCode/:date',
                method: 'POST',
                hasQueryParmas: true
            },
            'activateOrDeactivateEmployee' : {
                url: 'employee/active/inactive/:userId/:status',
                method: 'PUT',
                hasQueryParmas: true
            }

        });
})();