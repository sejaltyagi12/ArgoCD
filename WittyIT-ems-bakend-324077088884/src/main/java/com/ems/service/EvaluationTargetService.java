package com.ems.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.domain.Department;
import com.ems.domain.Designation;
import com.ems.domain.Employee;
import com.ems.domain.EvaluationCycle;
import com.ems.domain.EvaluationGroup;
import com.ems.domain.EvaluationHistory;
import com.ems.domain.EvaluationRating;
import com.ems.domain.EvaluationTarget;
import com.ems.domain.EvaluationTargetQuestion;
import com.ems.exception.ServiceException;
import com.ems.repository.EvaluationCycleRepository;
import com.ems.repository.EvaluationGroupRepository;
import com.ems.repository.EvaluationHistoryRepository;
import com.ems.repository.EvaluationRatingRepository;
import com.ems.repository.EvaluationTargetQuestionRepository;
import com.ems.repository.EvaluationTargetRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.wrappers.AddEvaluationComponentWrapper;
import com.ems.wrappers.EvaluationHistoryWrapper;
import com.ems.wrappers.EvaluationQuestionWrapper;
import com.ems.wrappers.EvaluationTargetWrapper;
import com.ems.wrappers.SubmitEvaluationWrapper;
import com.ems.wrappers.TargetWrapper;
import com.ems.wrappers.TargetListWrapper;

@Service
@Transactional
public class EvaluationTargetService {

	@Autowired
	private EvaluationGroupRepository groupRepository;

	@Autowired
	private EvaluationRatingRepository ratingRepository;

	@Autowired
	private EvaluationTargetQuestionRepository questionRepository;

	@Autowired
	private EvaluationTargetRepository targetRepository;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EvaluationCycleRepository cycleRepository;

	@Autowired
	private EvaluationHistoryRepository historyRepository;

	/**
	 * Find by Group id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationGroup
	 */
	public EvaluationGroup findGroupById(Long id) {
		return groupRepository.findById(id);
	}

	/**
	 * Find by Rating id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationRating
	 */
	public EvaluationRating findRatingById(Long id) {
		return ratingRepository.findByRatingId(id);
	}

	/**
	 * Find by Target id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationTarget
	 */
	public EvaluationTarget findTargetById(Long id) {
		return targetRepository.findById(id);
	}

	/**
	 * Find by Target question id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationTargetQuestion
	 */
	public EvaluationTargetQuestion findTargetQuestionById(Long id) {
		return questionRepository.findById(id);
	}

	/**
	 * Find by Cycle id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationCycle
	 */
	public EvaluationCycle findCycleById(Long id) {
		return cycleRepository.findById(id);
	}

	/**
	 * Find current Cycle.
	 *
	 * 
	 * @return the EvaluationCycle
	 */
	public EvaluationCycle getCurrentCycle(Department department) {
		return cycleRepository.findByDepartmentAndIsCompleted(department, false);
	}

	/**
	 * Find current Cycle.
	 *
	 * 
	 * @return the EvaluationCycle
	 */
	public List<EvaluationCycle> getCurrentCycleList() {
		return cycleRepository.findByIsCompleted(false);
	}

	/**
	 * Find current Cycle.
	 *
	 * 
	 * @return the EvaluationCycle
	 */
	public Boolean anyCycleInProgress() {
		return getCurrentCycleList().size() > 0;
	}

	/**
	 * Fetch all Evaluation Group.
	 *
	 * 
	 * @return the Evaluation Group List
	 * @throws ServiceException
	 */
	public List<EvaluationGroup> getEvaluationGroups() throws ServiceException {
		return groupRepository.findByIsActive(true);

	}

	/**
	 * Fetch all Evaluation Ratings.
	 *
	 * 
	 * @return the Evaluation Group List
	 * @throws ServiceException
	 */
	public List<EvaluationRating> getEvaluationRatings() throws ServiceException {
		return ratingRepository.findAll();

	}

	/**
	 * Add Target category or group.
	 *
	 * 
	 * @return the Evaluation Group id
	 * @throws ServiceException
	 */
	public Long addEvaluationGroup(AddEvaluationComponentWrapper wrapper) throws ServiceException {
		if (authenticationService.isHR() || authenticationService.isAdmin()) {
			List<EvaluationGroup> groups = groupRepository.findByOrderIndexAndIsActive(wrapper.getOrder(), true);

			if (wrapper.getOrder() < 1 || groups.size() != 0)
				throw new ServiceException("Order is invalid or already exist.");

			EvaluationGroup group = new EvaluationGroup();

			group.setName(wrapper.getDescription());
			group.setOrderIndex(wrapper.getOrder());

			groupRepository.save(group);

			return group.getId();
		}

		throw new ServiceException("You dont have rights.");
	}

	/**
	 * Edit Target category or group. Set the current group as inactive and then
	 * create a new group with new data
	 *
	 * 
	 * @return the Evaluation Group id
	 * @throws ServiceException
	 */
	public Long editEvaluationGroup(AddEvaluationComponentWrapper wrapper) throws ServiceException {
		EvaluationGroup group = groupRepository.findById(wrapper.getId());

		List<TargetWrapper> targetwrappers = new ArrayList<>();

		if (group != null) {
			List<EvaluationTargetQuestion> targetQuestions = questionRepository.findByEvaluationGroupAndIsActive(group,
					true);

			for (EvaluationTargetQuestion targetQuestion : targetQuestions) {
				List<EvaluationTarget> targets = targetRepository.findByTargetQuestionAndIsActive(targetQuestion, true);

				targetwrappers.add(new TargetWrapper(targetQuestion.getName(), targetQuestion.getDescription(),
						targetQuestion.getEvaluationGroup().getId(), targetQuestion.getRating().getRatingId(),
						targets.get(0).getOrderIndex(), getDesignationsList(targets)));
			}
		} else
			throw new ServiceException("Target group not found.");

		deleteEvaluationGroup(wrapper.getId());

		Long groupId = addEvaluationGroup(wrapper);

		for (TargetWrapper targetWrapper : targetwrappers) {
			targetWrapper.setGroupId(groupId);

			addEvaluationTarget(targetWrapper);
		}

		return groupId;
	}

	/**
	 * Find by Target id.
	 *
	 * @param id
	 * 
	 * @return the EvaluationTarget
	 * @throws ServiceException
	 */
	public TargetWrapper getEvaluationTargetQuestion(Long id) throws ServiceException {
		EvaluationTargetQuestion targetQuestion = questionRepository.findById(id);

		List<EvaluationTarget> targets = targetRepository.findByTargetQuestionAndIsActive(targetQuestion, true);

		if (targetQuestion != null && !targets.isEmpty()) {
			TargetWrapper targetWrapper = new TargetWrapper(targetQuestion.getName(), targetQuestion.getDescription(),
					targetQuestion.getEvaluationGroup().getId(), targetQuestion.getRating().getRatingId(),
					targets.get(0).getOrderIndex(), getDesignationsList(targets));

			targetWrapper.setGroup(targetQuestion.getEvaluationGroup());
			targetWrapper.setRating(targetQuestion.getRating());
			targetWrapper.setId(targetQuestion.getId());

			return targetWrapper;
		}

		throw new ServiceException("Target not found.");
	}

	/**
	 * Edit Target category or group. Set the current group as inactive and then
	 * create a new group with new data
	 *
	 * 
	 * @return the Evaluation Group id
	 * @throws ServiceException
	 */
	public void editEvaluationTarget(TargetWrapper wrapper) throws ServiceException {
		EvaluationTargetQuestion targetQuestion = findTargetQuestionById(wrapper.getId());

		wrapper.setId(null);

		deleteTarget(targetQuestion);

		addEvaluationTarget(wrapper);
	}

	/**
	 * Delete Target category or group.
	 *
	 * 
	 * @param the
	 *            Evaluation Group id
	 * @throws ServiceException
	 */
	public void deleteEvaluationGroup(Long id) throws ServiceException {
		EvaluationGroup group = groupRepository.findById(id);

		if (group != null) {
			List<EvaluationTargetQuestion> targetQuestions = questionRepository.findByEvaluationGroupAndIsActive(group,
					true);

			for (EvaluationTargetQuestion evaluationTargetQuestion : targetQuestions) {
				deleteTarget(evaluationTargetQuestion);
			}

			group.setIsActive(false);

			groupRepository.save(group);
		} else
			throw new ServiceException("Target group not found.");
	}

	/**
	 * Delete Targets
	 *
	 * 
	 * @param the
	 *            evaluationTargetQuestion
	 * @throws ServiceException
	 */
	public void deleteTarget(EvaluationTargetQuestion evaluationTargetQuestion) throws ServiceException {
		List<EvaluationTarget> targets = targetRepository.findByTargetQuestionAndIsActive(evaluationTargetQuestion,
				true);

		for (EvaluationTarget target : targets) {
			target.setIsActive(false);

			targetRepository.save(target);
		}

		evaluationTargetQuestion.setIsActive(false);

		questionRepository.save(evaluationTargetQuestion);
	}

	/**
	 * Get Designations associated with a Target or question.
	 *
	 * 
	 * @return the List of designation ids
	 * @throws ServiceException
	 */
	public List<Long> getDesignationsList(List<EvaluationTarget> targets) throws ServiceException {
		List<Long> designationIds = new ArrayList<>();

		for (EvaluationTarget evaluationTarget : targets) {
			designationIds.add(evaluationTarget.getDesignation().getDesignationId());
		}

		return designationIds;
	}

	/**
	 * Add Target Rating.
	 *
	 * 
	 * @return the Evaluation Rating id
	 * @throws ServiceException
	 */
	public Long addEvaluationRating(AddEvaluationComponentWrapper[] ratings) throws ServiceException {
		if (authenticationService.isHR() || authenticationService.isAdmin()) {
			EvaluationRating rating = new EvaluationRating();

			StringBuilder description = new StringBuilder("[");

			for (int i = 0; i < ratings.length; i++) {
				description.append("{" + '"' + "order" + '"' + ":" + ratings[i].getOrder());
				description
						.append("," + '"' + "description" + '"' + ":" + '"' + ratings[i].getDescription() + '"' + "}");

				if (i != ratings.length - 1)
					description.append(",");
			}

			description.append("]");

			if (ratingRepository.findByDescription(description.toString()) != null)
				throw new ServiceException("This rating set already exist");

			rating.setDescription(description.toString());

			ratingRepository.save(rating);

			return rating.getRatingId();
		}

		throw new ServiceException("You dont have rights.");
	}

	/**
	 * Add Evaluation Target.
	 *
	 * 
	 * @return the Evaluation Rating id
	 * @throws ServiceException
	 */
	public void addEvaluationTarget(TargetWrapper targetWrapper) throws ServiceException {
		EvaluationGroup evaluationGroup = groupRepository.findById(targetWrapper.getGroupId());

		EvaluationRating rating = ratingRepository.findByRatingId(targetWrapper.getRatingId());

		if (evaluationGroup == null || rating == null)
			throw new ServiceException("Group or rating doesn't exists.");

		if (targetWrapper.getDescription() == "" || targetWrapper.getName() == "" || targetWrapper.getOrder() < 1)
			throw new ServiceException("Missing Order, Description or Name.");

		List<EvaluationTargetQuestion> targetQuestions = questionRepository
				.findByEvaluationGroupAndIsActive(evaluationGroup, true);

		for (EvaluationTargetQuestion question : targetQuestions) {
			if (question.getOrderIndex().equals(targetWrapper.getOrder()))
				throw new ServiceException("Order already exist. Please enter a valid order.");
		}

		EvaluationTargetQuestion targetQuestion = questionRepository
				.findByNameAndEvaluationGroupAndIsActive(targetWrapper.getName(), evaluationGroup, true);

		if (targetQuestion == null)
			targetQuestion = new EvaluationTargetQuestion(targetWrapper.getName(), targetWrapper.getDescription(),
					rating, evaluationGroup);
		else
			throw new ServiceException("Target already exist.");

		questionRepository.save(targetQuestion);

		for (Long designationId : targetWrapper.getDesignation()) {
			Designation designation = designationService.findById(designationId);

			if (designation != null) {
				EvaluationTarget target = targetRepository.findByDesignationAndTargetQuestionAndOrderIndexAndIsActive(
						designation, targetQuestion, targetWrapper.getOrder(), true);

				if (target == null)
					target = new EvaluationTarget(targetQuestion, designation, targetWrapper.getOrder());
				else
					throw new ServiceException("Target already exist for " + targetWrapper.getOrder() + "order, "
							+ evaluationGroup.getName() + "category and" + designation.getDesignation()
							+ "designation");

				targetRepository.save(target);
			}
		}
	}

	/**
	 * Get all Evaluation Targets.
	 *
	 * 
	 * @return List of targets with categories or groups
	 * @throws ServiceException
	 */
	public List<TargetListWrapper> getEvaluationTargets() throws ServiceException {

		List<TargetListWrapper> targetList = new ArrayList<>();

		List<EvaluationGroup> groups = groupRepository.findByIsActive(true);

		for (EvaluationGroup evaluationGroup : groups) {
			List<EvaluationTargetQuestion> targetQuestions = questionRepository
					.findByEvaluationGroupAndIsActive(evaluationGroup, true);

			List<EvaluationQuestionWrapper> evaluationQuestionWrappers = new ArrayList<>();

			for (EvaluationTargetQuestion evaluationTargetQuestion : targetQuestions)
				evaluationQuestionWrappers.add(new EvaluationQuestionWrapper(evaluationTargetQuestion.getId(),
						evaluationTargetQuestion.getName(), evaluationTargetQuestion.getDescription(),
						evaluationTargetQuestion.getRating(), null, evaluationTargetQuestion.getOrderIndex()));

			targetList.add(new TargetListWrapper(evaluationGroup.getId(), evaluationGroup.getName(),
					evaluationQuestionWrappers, evaluationGroup.getOrderIndex()));
		}
		return targetList;
	}

	/**
	 * Get all Evaluation Targets of a cycle.
	 *
	 * 
	 * @return List of targets with categories or groups
	 * @throws ServiceException
	 */
	public List<TargetListWrapper> getEvaluationTargetsByCycle(Long cycleId) throws ServiceException {

		EvaluationCycle cycle = findCycleById(cycleId);

		if (cycle == null)
			throw new ServiceException("Cycle not found.");

		List<EvaluationHistory> employeeHistories = historyRepository.findCycleByGroupByTargets(cycle);

		List<TargetListWrapper> targetList = new ArrayList<>();

		List<EvaluationQuestionWrapper> targetQuestionList = new ArrayList<>();

		for (EvaluationHistory history : employeeHistories)
			targetQuestionList.add(new EvaluationQuestionWrapper(history.getTarget().getTargetQuestion().getId(),
					history.getTarget().getTargetQuestion().getName(),
					history.getTarget().getTargetQuestion().getDescription(),
					history.getTarget().getTargetQuestion().getRating(),
					new EvaluationTargetWrapper(history.getTarget().getId(), history,
							history.getTarget().getClosureDate()),
					history.getTarget().getOrderIndex()));

		List<EvaluationGroup> groups = groupRepository.findAll();

		for (EvaluationGroup evaluationGroup : groups) {
			List<EvaluationQuestionWrapper> questions = new ArrayList<>();

			for (EvaluationQuestionWrapper targetQuestion : targetQuestionList) {
				if (targetQuestion.getTarget().getEvaluationHistory().getTarget().getTargetQuestion()
						.getEvaluationGroup().getId().equals(evaluationGroup.getId()))
					questions.add(targetQuestion);
			}

			if (!questions.isEmpty())
				targetList.add(new TargetListWrapper(evaluationGroup.getId(), evaluationGroup.getName(), questions,
						evaluationGroup.getOrderIndex()));
		}

		return targetList;
	}

	/**
	 * Get Employee Evaluation Targets.
	 *
	 *
	 * @param Employee
	 * 
	 * @return List of targets of an employee
	 * @throws ServiceException
	 */
	public List<TargetListWrapper> getEmployeeEvaluationTargets(Employee employee) throws ServiceException {

		List<TargetListWrapper> targetList = new ArrayList<>();

		List<EvaluationGroup> groups = groupRepository.findByIsActive(true);

		for (EvaluationGroup evaluationGroup : groups) {
			List<EvaluationQuestionWrapper> targetQuestions = new ArrayList<>();

			List<EvaluationTarget> evaluationTargets = targetRepository
					.findByDesignationAndGroupAndIsActive(employee.getDesignation(), evaluationGroup, true);

			for (EvaluationTarget evaluationTarget : evaluationTargets)
				targetQuestions.add(new EvaluationQuestionWrapper(evaluationTarget.getTargetQuestion().getId(),
						evaluationTarget.getTargetQuestion().getName(),
						evaluationTarget.getTargetQuestion().getDescription(),
						evaluationTarget.getTargetQuestion().getRating(),
						new EvaluationTargetWrapper(evaluationTarget.getId(), null, evaluationTarget.getClosureDate()),
						evaluationTarget.getOrderIndex()));

			targetList.add(new TargetListWrapper(evaluationGroup.getId(), evaluationGroup.getName(), targetQuestions,
					evaluationGroup.getOrderIndex()));
		}

		return targetList;
	}

	/**
	 * Get Employee Evaluation Targets.
	 *
	 *
	 * @param Employee
	 * 
	 * @return List of targets of an employee
	 * @throws ServiceException
	 */
	public List<TargetListWrapper> getCurrentCycleEmployeeEvaluations(Employee employee, Boolean isEmployeeSubmitted)
			throws ServiceException {

		List<TargetListWrapper> targetList = new ArrayList<>();

		List<EvaluationGroup> groups = groupRepository.findByIsActive(true);

		for (EvaluationGroup evaluationGroup : groups) {
			List<EvaluationQuestionWrapper> targetQuestions = new ArrayList<>();

			List<EvaluationTarget> evaluationTargets = targetRepository
					.findByDesignationAndGroupAndIsActive(employee.getDesignation(), evaluationGroup, true);

			for (EvaluationTarget evaluationTarget : evaluationTargets) {
				EvaluationHistory history = isEmployeeSubmitted
						? historyRepository.findByEmployeeAndTargetAndCycleAndEmployeeSubmitted(employee,
								evaluationTarget, getCurrentCycle(employee.getDesignation().getDepartment()),
								isEmployeeSubmitted)
						: historyRepository.findByEmployeeAndTargetAndCycle(employee, evaluationTarget,
								getCurrentCycle(employee.getDesignation().getDepartment()));

				EvaluationTargetWrapper targetWrapper = new EvaluationTargetWrapper(evaluationTarget.getId(), history,
						evaluationTarget.getClosureDate());

				targetQuestions.add(new EvaluationQuestionWrapper(evaluationTarget.getTargetQuestion().getId(),
						evaluationTarget.getTargetQuestion().getName(),
						evaluationTarget.getTargetQuestion().getDescription(),
						evaluationTarget.getTargetQuestion().getRating(), targetWrapper,
						evaluationTarget.getOrderIndex()));
			}

			targetList.add(new TargetListWrapper(evaluationGroup.getId(), evaluationGroup.getName(), targetQuestions,
					evaluationGroup.getOrderIndex()));
		}

		return targetList;
	}

	/**
	 * Get Employee Evaluation Targets.
	 *
	 *
	 * @param Employee
	 * 
	 * @return List of targets of an employee
	 * @throws ServiceException
	 */
	public List<TargetListWrapper> getCycleEmployeeEvaluations(Employee employee, EvaluationCycle cycle)
			throws ServiceException {

		List<EvaluationHistory> employeeHistories = historyRepository.findByEmployeeAndCycle(employee, cycle);

		List<TargetListWrapper> targetList = new ArrayList<>();

		List<EvaluationQuestionWrapper> targetQuestionList = new ArrayList<>();

		for (EvaluationHistory history : employeeHistories) {
			EvaluationTargetWrapper targetWrapper = new EvaluationTargetWrapper(history.getTarget().getId(), history,
					history.getTarget().getClosureDate());

			targetQuestionList.add(new EvaluationQuestionWrapper(history.getTarget().getTargetQuestion().getId(),
					history.getTarget().getTargetQuestion().getName(),
					history.getTarget().getTargetQuestion().getDescription(),
					history.getTarget().getTargetQuestion().getRating(), targetWrapper,
					history.getTarget().getOrderIndex()));
		}

		List<EvaluationGroup> groups = groupRepository.findAll();

		for (EvaluationGroup evaluationGroup : groups) {
			List<EvaluationQuestionWrapper> questions = new ArrayList<>();

			for (EvaluationQuestionWrapper targetQuestion : targetQuestionList) {
				if (targetQuestion.getTarget().getEvaluationHistory().getTarget().getTargetQuestion()
						.getEvaluationGroup().getId().equals(evaluationGroup.getId()))
					questions.add(targetQuestion);
			}

			if (!questions.isEmpty())
				targetList.add(new TargetListWrapper(evaluationGroup.getId(), evaluationGroup.getName(), questions,
						evaluationGroup.getOrderIndex()));
		}

		return targetList;
	}

	/**
	 * Starts an Employee Evaluation.
	 *
	 * @param EvaluationCycle
	 *
	 * @throws ServiceException
	 */
	public void startEvaluation(EvaluationCycle cycle) throws ServiceException {

		if (cycle.getDepartments().size() <= 0)
			throw new ServiceException("Please select a department to initiate cycle.");

		for (int deptId : cycle.getDepartments()) {
			if (cycle.getStartDate() != null && cycle.getEndDate() != null && cycle.getEmployeeEndDate() != null
					&& cycle.getManagerEndDate() != null && cycle.getStartDate().isBefore(cycle.getEndDate())
					&& cycle.getEndDate().isBefore(cycle.getEmployeeEndDate().plusHours(1))
					&& cycle.getEmployeeEndDate().isBefore(cycle.getManagerEndDate().plusHours(1))) {
				Department department = departmentService.findById(deptId);

				if (department != null) {
					if (getCurrentCycle(department) != null)
						throw new ServiceException(
								"Cycle with " + department.getDeptName() + " department is in progress.");

					EvaluationCycle evaluationCycle = new EvaluationCycle(cycle.getStartDate(), cycle.getEndDate(),
							cycle.getEmployeeEndDate(), cycle.getManagerEndDate(), department);
					cycleRepository.save(evaluationCycle);
				} else {
					throw new ServiceException("Department with id " + deptId + " doesnot exist.");
				}
			}

			else {
				throw new ServiceException("Improper dates.");
			}
		}
	}

	/**
	 * Starts an Employee Evaluation.
	 *
	 * @param EvaluationCycle
	 *
	 * @throws ServiceException
	 */
	public void endEvaluation(Long id) throws ServiceException {

		EvaluationCycle cycle = cycleRepository.findById(id);

		if (cycle == null)
			throw new ServiceException("Cycle id not found.");

		cycle.setIsCompleted(true);

		cycleRepository.save(cycle);
	}

	/**
	 * Submits an evaluation filled by an employee.
	 * 
	 * @param SubmitEvaluationWrapper
	 *            array
	 * @throws ServiceException
	 *             the service exception
	 */
	public EvaluationHistoryWrapper submitEmployeeEvaluation(SubmitEvaluationWrapper[] historyWrappers)
			throws ServiceException {

		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		EvaluationCycle cycle = new EvaluationCycle();

		for (SubmitEvaluationWrapper historyWrapper : historyWrappers) {
			EvaluationTarget target = findTargetById(historyWrapper.getTargetId());

			cycle = findCycleById(historyWrapper.getCycleId());

			EvaluationHistory history = historyRepository.findByEmployeeAndTargetAndCycle(authenticatedEmployee, target,
					getCurrentCycle(authenticatedEmployee.getDesignation().getDepartment()));

			if (target == null || cycle == null)
				throw new ServiceException("Provide a valid Cycle ID and Target ID.");

			if (cycle.getEmployeeEndDate().hourOfDay().withMaximumValue().isBeforeNow())
				throw new ServiceException("Employee submission date passed.");

			if (cycle.getIsCompleted() || !target.getIsActive() || !target.getDesignation().getDesignationId()
					.equals(authenticatedEmployee.getDesignation().getDesignationId()))
				throw new ServiceException("Provide a valid Cycle ID and Target ID.");

			if (cycle.getDepartment().getDeptId() != authenticatedEmployee.getDesignation().getDepartment().getDeptId())
				throw new ServiceException("You are not associated with this cycle.");

			try {
				employeeService.findById(authenticatedEmployee.getManagerId());
			} catch (Exception e) {
				throw new ServiceException("You doesn't have any manager associated");
			}

			if (history != null && historyWrapper.getId() == null)
				throw new ServiceException("Missing evaluation ID.");

			history = historyWrapper.getId() != null ? historyRepository.findById(historyWrapper.getId())
					: new EvaluationHistory(authenticatedEmployee, target, cycle, authenticatedEmployee.getManagerId());

			if (history != null) {
				if (history.getEmployeeSubmitted())
					throw new ServiceException("Already Submitted.");

				history.setEmployeeRating(historyWrapper.getEmployeeRating());
				history.setEmployeeReason(historyWrapper.getEmployeeReason());

				if (historyWrapper.getEmployeeSubmitted()) {
					history.setEmployeeSubmitted(historyWrapper.getEmployeeSubmitted());
					history.setEmployeeSubmittedDate(new DateTime());
				}

				historyRepository.save(history);
			} else
				throw new ServiceException("Can't find target history with Id: " + historyWrapper.getId());
		}

		return getEmployeeEvaluation(authenticatedEmployee.getId(), cycle.getId());
	}

	/**
	 * Submits an employee evaluation filled by manager .
	 * 
	 * @param SubmitEvaluationWrapper
	 * @throws ServiceException
	 *             the service exception
	 */
	public EvaluationHistoryWrapper submitManagerEvaluation(SubmitEvaluationWrapper[] historyWrappers)
			throws ServiceException {

		Employee targetEmployee = null;

		EvaluationCycle cycle = new EvaluationCycle();

		for (SubmitEvaluationWrapper historyWrapper : historyWrappers) {
			Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

			targetEmployee = employeeService.findByEmployeeId(historyWrapper.getEmployeeId());

			EvaluationHistory history = historyRepository.findById(historyWrapper.getId());

			EvaluationTarget target = findTargetById(historyWrapper.getTargetId());

			cycle = findCycleById(historyWrapper.getCycleId());

			if (target == null || cycle == null || history == null || targetEmployee == null)
				throw new ServiceException("Incorrect data for Cycle ID, Target ID, Evaluation ID or Employee ID.");

			if (cycle.getManagerEndDate().hourOfDay().withMaximumValue().isBeforeNow()
					&& !(authenticationService.isAdmin() || authenticationService.isHR()))
				throw new ServiceException("Manager submission date passed.");

			if (cycle.getIsCompleted() || !target.getIsActive())
				throw new ServiceException("Provide a valid Cycle ID and Target ID.");

			if (!targetEmployee.getManagerId().equals(authenticatedEmployee.getId())
					&& !(authenticationService.isAdmin() || authenticationService.isHR()))
				throw new ServiceException("You are not manager of this employee");

			if (history.getManagerSubmitted() && !(authenticationService.isAdmin() || authenticationService.isHR()))
				throw new ServiceException("Already Submitted.");

			if (!history.getEmployeeSubmitted())
				throw new ServiceException("Let employee submit first.");

			history.setManagerRating(historyWrapper.getManagerRating());
			history.setManagerReason(historyWrapper.getManagerReason());

			if (historyWrapper.getManagerSubmitted()) {
				history.setManagerSubmitted(historyWrapper.getManagerSubmitted());
				history.setManagerSubmittedDate(new DateTime());
			}

			historyRepository.save(history);
		}

		if (targetEmployee == null)
			throw new ServiceException("Employee not found");

		return getEmployeeEvaluation(targetEmployee.getId(), cycle.getId());
	}

	/**
	 * Fetches list of evaluation of employees for manager rating..
	 * 
	 * @return EvaluationHistory List
	 * @throws ServiceException
	 *             the service exception
	 */
	public List<EvaluationHistoryWrapper> getManagerPendingEvaluations(Long cycleId) throws ServiceException {
		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		List<EvaluationHistoryWrapper> wrappers = new ArrayList<>();

		List<EvaluationHistory> histories = new ArrayList<>();

		EvaluationCycle cycle = findCycleById(cycleId);

		if (cycle == null)
			throw new ServiceException("Invalid cycle.");

		if (authenticationService.isAdmin() || authenticationService.isHR())
			histories = historyRepository.findByCycleAndEmployeeSubmitted(cycle, true);
		else
			histories = historyRepository.findByManagerIdAndCycleAndEmployeeSubmitted(authenticatedEmployee.getId(),
					cycle, true);

		if (!cycle.getIsCompleted())
			cleanTargetHistory(histories);

		for (EvaluationHistory evaluationHistory : histories) {
			EvaluationHistoryWrapper wrapper = new EvaluationHistoryWrapper(evaluationHistory.getEmployee().getId(),
					evaluationHistory.getEmployee().getFullName());

			Integer index = wrappers.indexOf(wrapper);

			List<TargetListWrapper> categories = !cycle.getIsCompleted()
					? getCurrentCycleEmployeeEvaluations(evaluationHistory.getEmployee(), true)
					: getCycleEmployeeEvaluations(evaluationHistory.getEmployee(), cycle);

			if (index >= 0) {
				wrappers.get(index).setCategories(categories);
			} else {
				wrapper.setCategories(categories);

				wrappers.add(wrapper);
			}
		}

		return wrappers;
	}

	/**
	 * Cleans Evaluation history by discarding Inactive Questions or Targets.
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	public void cleanTargetHistory(List<EvaluationHistory> histories) throws ServiceException {
		Iterator<EvaluationHistory> iterator = histories.iterator();

		while (iterator.hasNext()) {
			EvaluationHistory evaluationHistory = (EvaluationHistory) iterator.next();

			if (!evaluationHistory.getTarget().getIsActive())
				iterator.remove();
		}
	}

	/**
	 * Fetches a evaluation of employees for manager rating..
	 * 
	 * @return EvaluationHistory
	 * @throws ServiceException
	 *             the service exception
	 */
	public EvaluationHistoryWrapper getEmployeeEvaluation(Long id, Long cycleId) throws ServiceException {
		Employee employee = employeeService.findById(id);
		if (employee == null)
			throw new ServiceException("Employee not found.");

		EvaluationCycle cycle = findCycleById(cycleId);

		if (cycle == null)
			throw new ServiceException("Invalid cycle.");

		EvaluationHistoryWrapper wrapper = new EvaluationHistoryWrapper(employee.getId(), employee.getFullName());

		List<TargetListWrapper> categories = !cycle.getIsCompleted()
				? getCurrentCycleEmployeeEvaluations(employee, false) : getCycleEmployeeEvaluations(employee, cycle);

		wrapper.setCategories(categories);

		return wrapper;
	}

	/**
	 * Get List of evaluation cycles. Serve : evaluation/cycle/list method: GET
	 * 
	 * 
	 * @return EvaluationCycle List
	 * @throws ServiceException
	 *             the service exception
	 */
	public List<EvaluationCycle> getEvaluationCycles() throws ServiceException {
		List<EvaluationCycle> cycles = cycleRepository.findAll();

		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		for (EvaluationCycle cycle : cycles) {
			List<EvaluationHistory> histories = historyRepository.findByEmployeeAndCycle(authenticatedEmployee, cycle);

			if (!histories.isEmpty())
				cycle.setUserStatus(histories.get(0).getEmployeeSubmitted() ? Constants.STATUS_COMPLETED
						: Constants.STATUS_IN_PROGRESS);
		}

		return cycles;
	}

	/**
	 * Get List of evaluation cycles. Serve :
	 * evaluation/cycle/list/self-eveluation method: GET
	 * 
	 * @return EvaluationCycle List
	 * @throws ServiceException
	 *             the service exception
	 */
	public List<EvaluationCycle> getSelfEvaluationCycles() throws ServiceException {
		List<EvaluationCycle> cycles = new ArrayList<EvaluationCycle>();

		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		EvaluationCycle currentCycle = getCurrentCycle(authenticatedEmployee.getDesignation().getDepartment());

		if (currentCycle != null) {
			EvaluationHistory history = historyRepository.findByEmployeeAndCycleGroupByCycle(authenticatedEmployee,
					currentCycle);

			if (history != null)
				currentCycle.setUserStatus(history.getEmployeeSubmitted() || history.getCycle().getIsCompleted()
						? Constants.STATUS_COMPLETED : Constants.STATUS_IN_PROGRESS);
			cycles.add(currentCycle);
		}

		List<EvaluationHistory> histories = historyRepository.findByEmployeeAndGroupByCycle(authenticatedEmployee);

		for (EvaluationHistory history : histories) {
			history.getCycle().setUserStatus(Constants.STATUS_COMPLETED);
			cycles.add(history.getCycle());
		}

		return cycles;
	}

	/**
	 * Get List of evaluation cycles. Serve :
	 * evaluation/cycle/list/self-eveluation method: GET
	 * 
	 * @return EvaluationCycle List
	 * @throws ServiceException
	 *             the service exception
	 */
	public List<EvaluationCycle> getTeamEvaluationCycles() throws ServiceException {
		List<EvaluationCycle> cycles = new ArrayList<EvaluationCycle>();

		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		List<EvaluationHistory> histories = historyRepository
				.findByManagerIdAndGroupByCycle(authenticatedEmployee.getId());

		for (EvaluationHistory history : histories) {
			history.getCycle().setUserStatus(
					history.getEmployeeSubmitted() ? Constants.STATUS_COMPLETED : Constants.STATUS_IN_PROGRESS);
			cycles.add(history.getCycle());
		}

		return cycles;
	}

	/**
	 * Get count of employee submitted evaluations.
	 * 
	 * @return Count
	 * @throws ServiceException
	 *             the service exception
	 */
	public int getEmployeeSubmittedEvaluationCount() throws ServiceException {
		return (authenticationService.isAdmin() || authenticationService.isHR())
				? historyRepository.findEmployeeCountByEmployeeSubmittedAndCycleCompleted(true, false)
				: historyRepository.findEmployeeCountByManagerIdAndEmployeeSubmittedAndCycleCompleted(
						authenticationService.getAuthenticatedEmployee().getId(), true, false);
	}

	/**
	 * Completes an evaluation cycle.
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	public void completeEvaluationCycle(long evaluationCycleID) throws ServiceException {
		if (!authenticationService.isHR() && !authenticationService.isAdmin())
			throw new ServiceException("You dont have rights.");

		EvaluationCycle evaluationCycle = findCycleById(evaluationCycleID);
		evaluationCycle.setIsCompleted(true);
		cycleRepository.save(evaluationCycle);
	}

	/**
	 * Whether to show notification on Evaluation Start or nor.
	 * 
	 * @return boolean
	 * @throws ServiceException
	 *             the service exception
	 */
	public Boolean showNotification() throws ServiceException {
		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		EvaluationCycle cycle = getCurrentCycle(authenticatedEmployee.getDesignation().getDepartment());

		if (cycle == null)
			return false;

		List<EvaluationHistory> histories = historyRepository.findByEmployeeAndCycle(authenticatedEmployee, cycle);

		if (!histories.isEmpty())
			return false;

		return true;
	}

}
