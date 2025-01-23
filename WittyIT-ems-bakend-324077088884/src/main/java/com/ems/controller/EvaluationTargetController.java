package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.EvaluationCycle;
import com.ems.domain.EvaluationGroup;
import com.ems.domain.EvaluationRating;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.EvaluationTargetService;
import com.ems.servicefinder.utils.Constants;
import com.ems.wrappers.AddEvaluationComponentWrapper;
import com.ems.wrappers.EvaluationHistoryWrapper;
import com.ems.wrappers.SubmitEvaluationWrapper;
import com.ems.wrappers.TargetWrapper;
import com.ems.wrappers.TargetListWrapper;

@RestController
@RequestMapping("/evaluation")
public class EvaluationTargetController {

	@Autowired
	private EvaluationTargetService targetService;

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Add Target category or group. Service url: /evaluation/group/add method:
	 * PUT
	 *
	 * @return Group Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/group/add", method = RequestMethod.PUT)
	Long addEvaluationGroup(@RequestBody AddEvaluationComponentWrapper wrapper) throws ServiceException {
		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		return targetService.addEvaluationGroup(wrapper);
	}

	/**
	 * Edit Target category or group. Service url: /evaluation/group/edit
	 * method: PUT
	 *
	 * @return Group Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/group/edit", method = RequestMethod.PUT)
	Long editEvaluationGroup(@RequestBody AddEvaluationComponentWrapper wrapper) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		return targetService.editEvaluationGroup(wrapper);
	}

	/**
	 * Delete a Target category or group. Service url: /evaluation/group/delete
	 * method: DELETE
	 *
	 * @return Group Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/group/delete", method = RequestMethod.DELETE)
	void deleteEvaluationGroup(@RequestParam("id") Long id) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		targetService.deleteEvaluationGroup(id);
	}

	/**
	 * Add Target Rating. Service url: /evaluation/rating/add method: PUT
	 *
	 * @return Rating Id
	 * @throws ServiceException
	 *             the service exception
	 * @see com.ems.wrappers.AddEvaluationComponentWrapper
	 */

	@RequestMapping(value = "/rating/add", method = RequestMethod.PUT)
	Long addEvaluationRating(@RequestBody AddEvaluationComponentWrapper[] ratings) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		return targetService.addEvaluationRating(ratings);
	}

	/**
	 * Get Target category or group. Service url: /evaluation/group/list method:
	 * GET
	 *
	 * @return Group List
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/group/list", method = RequestMethod.GET)
	List<EvaluationGroup> getEvaluationGroups() throws ServiceException {
		return targetService.getEvaluationGroups();
	}

	/**
	 * Get Target category or group. Service url: /evaluation/group/get method:
	 * GET
	 *
	 * @return Group
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/group/get", method = RequestMethod.GET)
	EvaluationGroup getEvaluationGroup(@RequestParam("id") Long id) throws ServiceException {
		return targetService.findGroupById(id);
	}

	/**
	 * Get Target Ratings. Service url: /evaluation/rating/get method: GET
	 *
	 * @return rating List Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/rating/list", method = RequestMethod.GET)
	List<EvaluationRating> getEvaluationRatings() throws ServiceException {
		return targetService.getEvaluationRatings();
	}

	/**
	 * Get Target Rating. Service url: /evaluation/rating/get method: GET
	 *
	 * @return rating List Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/rating/get", method = RequestMethod.GET)
	EvaluationRating getEvaluationRatings(@RequestParam("id") Long id) throws ServiceException {
		return targetService.findRatingById(id);
	}

	/**
	 * Adds a Target. Service url: /evaluation/target/add method: PUT
	 *
	 * @return Group Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/target/add", method = RequestMethod.PUT)
	void addEvaluationTarget(@RequestBody TargetWrapper targetWrapper) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		targetService.addEvaluationTarget(targetWrapper);
	}

	/**
	 * Get all Targets. Service url: /evaluation/target/list method: GET
	 *
	 * @return List of targets according to groups
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/target/list", method = RequestMethod.GET)
	List<TargetListWrapper> getEvaluationTargetsList(@RequestParam(value = "cycleId", required = false) Long cycleId)
			throws ServiceException {
		return cycleId == null ? targetService.getEvaluationTargets()
				: targetService.getEvaluationTargetsByCycle(cycleId);
	}

	/**
	 * Get Targets of an employee. Service url: /evaluation/target/get method:
	 * GET
	 *
	 * @return List of targets according to groups
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/employee/targets", method = RequestMethod.GET)
	List<TargetListWrapper> getEvaluationTargets() throws ServiceException {
		return targetService.getEmployeeEvaluationTargets(authenticationService.getAuthenticatedEmployee());
	}

	/**
	 * Get Target by Id. Service url: /evaluation/target/get method: GET
	 *
	 * @return List of targets according to groups
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/target", method = RequestMethod.GET)
	TargetWrapper getEvaluationTarget(@RequestParam("id") Long id) throws ServiceException {
		return targetService.getEvaluationTargetQuestion(id);
	}

	/**
	 * Edit a target. Service url: /evaluation/target/edit method: PUT
	 *
	 * @return Target Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/target/edit", method = RequestMethod.PUT)
	void editEvaluationTarget(@RequestBody TargetWrapper wrapper) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		targetService.editEvaluationTarget(wrapper);
	}

	/**
	 * Delete a Target category or group. Service url: /evaluation/group/delete
	 * method: DELETE
	 *
	 * @return Target Id
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/target/delete", method = RequestMethod.DELETE)
	void deleteEvaluationTarget(@RequestParam("id") Long id) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		if (targetService.anyCycleInProgress())
			throw new ServiceException(Constants.CYCLE_IN_PROGRESS);

		targetService.deleteTarget(targetService.findTargetQuestionById(id));
	}

	/**
	 * Get Targets of an employee. Service url: /evaluation/start method: POST
	 *
	 * @return List of targets according to groups
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	void startEvaluation(@RequestBody EvaluationCycle cycle) throws ServiceException {
		if (!(authenticationService.isAdmin() || authenticationService.isHR()))
			throw new ServiceException("Admin only access. You dont have rights.");

		targetService.startEvaluation(cycle);
	}

	/**
	 * Submits or save an evaluation filled by an employee. Service url:
	 * /evaluation/employee/submit method: POST
	 *
	 * @param SubmitEvaluationWrapper
	 *            list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/employee/submit", method = RequestMethod.POST)
	EvaluationHistoryWrapper submitEmployeeEvaluation(@RequestBody SubmitEvaluationWrapper[] historyWrappers)
			throws ServiceException {
		return targetService.submitEmployeeEvaluation(historyWrappers);
	}

	/**
	 * Submits or save an employee evaluation filled by an manager. Service url:
	 * /evaluation/manager/submit method: POST
	 *
	 * @param SubmitEvaluationWrapper
	 *            list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/manager/submit", method = RequestMethod.POST)
	EvaluationHistoryWrapper submitManagerEvaluation(@RequestBody SubmitEvaluationWrapper[] historyWrappers)
			throws ServiceException {
		return targetService.submitManagerEvaluation(historyWrappers);
	}

	/**
	 * Fetches list of evaluation of employees for manager rating. Service url:
	 * /evaluation/manager/list method: GET
	 *
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/manager/list", method = RequestMethod.GET)
	List<EvaluationHistoryWrapper> getManagerPendingList(@RequestParam(value = "cycleId", required = true) Long cycleId)
			throws ServiceException {
		return targetService.getManagerPendingEvaluations(cycleId);
	}

	/**
	 * Fetches a evaluation of an employee by Id. Service url:
	 * /evaluation/employee/get method: GET
	 *
	 * @param id
	 *            get manager evaluations
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/employee/get", method = RequestMethod.GET)
	EvaluationHistoryWrapper getEmployeeEvaluation(@RequestParam("id") Long id,
			@RequestParam(value = "cycleId", required = true) Long cycleId) throws ServiceException {
		return targetService.getEmployeeEvaluation(id, cycleId);
	}

	/**
	 * Get List of evaluation cycles. Service url: /evaluation/cycle/list
	 * method: GET
	 *
	 * @return EvaluationCycle list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/list", method = RequestMethod.GET)
	List<EvaluationCycle> getEvaluationCycles() throws ServiceException {
		return targetService.getEvaluationCycles();
	}

	/**
	 * Get List of evaluation cycles. Service url:
	 * /evaluation/cycle/list/self-evaluation method: GET
	 *
	 * @return EvaluationCycle list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/list/self-evaluation", method = RequestMethod.GET)
	List<EvaluationCycle> getSelfEvaluationCycles() throws ServiceException {
		return targetService.getSelfEvaluationCycles();
	}

	/**
	 * Get List of evaluation cycles. Service url:
	 * /evaluation/cycle/list/team-evaluation method: GET
	 *
	 * @return EvaluationCycle list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/list/team-evaluation", method = RequestMethod.GET)
	List<EvaluationCycle> getTeamEvaluationCycles() throws ServiceException {
		return authenticationService.isAdmin() || authenticationService.isHR() ? targetService.getEvaluationCycles()
				: targetService.getTeamEvaluationCycles();
	}

	/**
	 * Get List of evaluation cycles. Service url:
	 * /evaluation/cycle/list//notification method: GET
	 *
	 * @return EvaluationCycle list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/list/team-evaluation/notification", method = RequestMethod.GET)
	Integer getEmployeeSubmittedEvaluationCount() throws ServiceException {
		return targetService.getEmployeeSubmittedEvaluationCount();
	}

	/**
	 * Get active or current evaluation cycle. Service url:
	 * /evaluation/cycle/active method: GET
	 *
	 * @return EvaluationCycle list
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/active", method = RequestMethod.GET)
	EvaluationCycle getCurrentEvaluationCycle() throws ServiceException {
		return targetService
				.getCurrentCycle(authenticationService.getAuthenticatedEmployee().getDesignation().getDepartment());
	}

	/**
	 * Ends an cycles. Service url: /evaluation/cycle/end method: POST
	 *
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/cycle/end", method = RequestMethod.POST)
	void endEvaluationCycles(@RequestParam("id") Long id) throws ServiceException {
		targetService.completeEvaluationCycle(id);
	}

	/**
	 * Ends an cycles. Service url: /evaluation/cycle/end method: GET
	 *
	 * @throws ServiceException
	 *             the service exception
	 */

	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	Boolean showNotification() throws ServiceException {
		return targetService.showNotification();
	}
}
