package com.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Employee;
import com.ems.domain.Idea;
import com.ems.exception.ServiceException;
import com.ems.repository.IdeaRepository;

@Service
@Transactional
public class IdeaService {

	@Autowired
	private IdeaRepository ideaRepository;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Find by id.
	 *
	 * @param id
	 * 
	 * @return the Idea
	 * @throws ServiceException
	 */
	public Idea findById(Long id) throws ServiceException {
		Idea idea = ideaRepository.findById(id);

		if (idea != null) {
			setEmpDataInIdea(idea);
			return idea;
		}

		else
			throw new ServiceException("Idea not found.");
	}

	/**
	 * Add Idea.
	 *
	 * @param Idea
	 * @throws ServiceException
	 */
	public void addIdea(Idea idea) throws ServiceException {
		Employee employee = authenticationService.getAuthenticatedEmployee();

		idea.setEmpId(employee.getId());
		idea.setManagerId(employee.getManagerId());
		idea.setId(null);

		ideaRepository.save(idea);
	}

	/**
	 * Get Posted Ideas.
	 *
	 * @param Idea
	 * @throws ServiceException
	 */
	public List<Idea> getIdeas() throws ServiceException {

		Employee employee = authenticationService.getAuthenticatedEmployee();

		return ideaRepository.findByEmpId(employee.getId());
	}

	/**
	 * Get Ideas where manager is included.
	 *
	 * @param Idea
	 * @throws ServiceException
	 */
	public List<Idea> getManagerIncludedIdeas() throws ServiceException {

		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		List<Idea> ideas = authenticationService.isAdmin() || authenticationService.isHR() ? ideaRepository.findAll()
				: ideaRepository.findByManagerIdAndManagerShared(authenticatedEmployee.getId(), true);

		for (Idea idea : ideas)
			setEmpDataInIdea(idea);

		return ideas;
	}

	/**
	 * Set values in an idea
	 *
	 * @param Idea
	 * @throws ServiceException
	 */
	public Idea setEmpDataInIdea(Idea idea) throws ServiceException {
		Employee employee = null;
		try {
			employee = employeeService.findById(idea.getEmpId());
		} catch (Exception e) {
		}
		if (employee != null) {
			idea.setEmpCode(employee.getEmpCode());

			idea.setEmpName(employee.getFullName());
		}

		return idea;
	}

	/**
	 * Get Ideas where manager is included.
	 *
	 * @param Idea
	 * @throws ServiceException
	 */
	public void setIdeaReadStatus(Long id, Boolean isRead) throws ServiceException {
		Idea idea = findById(id);

		idea.setIsRead(isRead);
		ideaRepository.save(idea);
	}

}
