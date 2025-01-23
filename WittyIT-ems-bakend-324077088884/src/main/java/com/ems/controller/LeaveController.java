package com.ems.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.LeaveType;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.LeaveService;
import com.ems.wrappers.ApplyLeaveWrapper;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.LeaveAccountWrapper;
import com.ems.wrappers.LeaveApproveWrapper;
import com.ems.wrappers.LeaveHistoryDetailsWrapper;
import com.ems.wrappers.LeaveHistoryWrapper;

@RestController
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private AuthenticationService authenticationService;

	public LeaveController() {

	}

	/**
	 * Get list of all leave types Service url: /leave/list/types method: GET
	 *
	 * @return List of LeaveType in the response
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/list/types", method = RequestMethod.GET)
	List<LeaveType> getAllLeaveTypes() throws ServiceException {
		return leaveService.getAllLeaveTypes();
	}

	/**
	 * Apply a leave. Service url: /leave/apply method: POST
	 *
	 * @param ApplyLeaveWrapper
	 *            leave to be apply
	 * @throws ServiceException
	 *             the service exception
	 * @throws SerialException
	 * @see com.ems.wrappers.ApplyLeaveWrapper
	 */

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	List<LeaveHistoryDetailsWrapper> applyLeave(ApplyLeaveWrapper leave, HttpServletRequest request)
			throws ServiceException, SerialException {
		return leaveService.leaveApply(leave, null, true);
	}

	/**
	 * Approve a leave Service url: /leave/approve method: POST
	 *
	 * @param ApplyLeaveWrapper
	 *            Information needed to approve leave
	 * @throws ServiceException
	 *             the service exception
	 * @throws SerialException
	 * @see com.ems.wrappers.LeaveApproveWrapper
	 */

	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	void approveLeave(@RequestBody LeaveApproveWrapper approveWrapper) throws ServiceException, SerialException {
		leaveService.approveLeave(approveWrapper, false);
	}

	/**
	 * Get list of all leave applied by an employee. Service url: /leave/applied
	 * method: GET
	 *
	 * @return LeaveHistoryWrapper List in the response
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.PUT)
	void withdrawLeave(@RequestParam("id") Long id) throws ServiceException {
		leaveService.withdrawLeave(id);
	}

	/**
	 * Get list of all leave of a team for a Manager/Admin Service url:
	 * /leave/teamAppliedLeave/list method: GET
	 * 
	 * @return LeaveHistoryWrapper List with list of all from team in response.
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */
	@RequestMapping(value = "/teamAppliedLeave/list", method = RequestMethod.GET)
	List<LeaveHistoryWrapper> getTeamAppliedLeaveList() throws ServiceException {
		return leaveService.getTeamAppliedLeaveList();
	}

	/**
	 * Get count of all leave of a team for a Manager/Admin with status pending Service url:
	 * /leave/teamAppliedLeave/list method: GET \ *
	 * 
	 * @return LeaveHistoryWrapper count of all from team in response.
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */
	@RequestMapping(value = "/team/applied/count", method = RequestMethod.GET)
	int getTeamAppliedLeaveCount() throws ServiceException {
		return leaveService.getTeamAppliedLeaveCount();
	}

	/**
	 * Get list of all leave applied by an employee. Service url: /leave/applied
	 * method: GET
	 *
	 * @return LeaveHistoryWrapper List in the response
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */
	@RequestMapping(value = "/applied", method = RequestMethod.GET)
	Object getAppliedLeaveList(@RequestParam(name = "id", required = false) Long id) throws ServiceException {
		return leaveService.getLeaveList(id);
	}

	/**
	 * Get details of leave account. Service url: /leave/leaveAccountDetails
	 * method: GET /leave/leaveAccountDetails?id={id} method: GET
	 *
	 * @param id
	 *            Employee id
	 * @return LeaveAccountWrapper with leave account data in it
	 * @throws ServiceException
	 *             the service exception
	 * @throws SerialException
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */

	@RequestMapping(value = "/leaveAccountDetails", method = RequestMethod.GET)
	LeaveAccountWrapper getLeaveAccountDetails(@RequestParam(required = false, value = "id") Long id)
			throws ServiceException, SerialException {

		return leaveService
				.getLeaveAccountDetails(id == null ? authenticationService.getAuthenticatedEmployee().getId() : id);
	}

	/**
	 * Upload leave attachment Service url: /leave/upload method: POST
	 *
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	void upload(@RequestParam("id") Long id, FileUploadWrapper uploadWrapper) throws ServiceException {
		leaveService.uploadAttachment(uploadWrapper, id);
	}
	
	/**
	 * Retrieves all leave records of employees who report to the logged-in manager.
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @author krishna kumar
	 */
	@RequestMapping(value = "/teamLeaves/list", method = RequestMethod.GET)
	List<LeaveHistoryWrapper> getTeamsLeave() throws ServiceException {
		return leaveService.getTeamsLeave();
	}
	
	/**
	 * Get count of all leaves of a team for a Manager/Admin with status pending Service url:
	 * /leave/teams/applied/leave/count method: GET \ *
	 * 
	 * @return LeaveHistoryWrapper count of all from team in response.
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.LeaveHistoryWrapper
	 */
	@RequestMapping(value = "/teams/applied/leave/count", method = RequestMethod.GET)
	int getTeamsLeaveCount() throws ServiceException {
		return leaveService.getTeamsLeaveCount();
	}

}