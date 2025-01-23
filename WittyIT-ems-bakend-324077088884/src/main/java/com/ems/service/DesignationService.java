package com.ems.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Department;
import com.ems.domain.Designation;
import com.ems.exception.ServiceException;
import com.ems.repository.DesignationRepository;
import com.ems.wrappers.DepartmentWithDesignationListWrapper;
import com.ems.wrappers.DesignationWrapper;

@Service
@Transactional
public class DesignationService {

	@Autowired
	private DesignationRepository designationRepository;
	
	@Autowired
	private DepartmentService departmentService;

	/**
	 * Find by id.
	 *
	 * @param id
	 * 
	 * @return the Designation
	 */
	public Designation findById(Long id) {
		return designationRepository.findByDesignationId(id);
	}

	/**
	 * Fetch all Designation.
	 *
	 * 
	 * @return the Designation
	 */
	public List<DesignationWrapper> getDesignations() {

		List<DesignationWrapper> designationWrappers = new ArrayList<DesignationWrapper>();
		List<Designation> designations = designationRepository.findAll();
		for (Designation designation : designations) {
			designationWrappers.add(new DesignationWrapper(designation.getDesignationId(), designation.getDesignation(),
					designation.getLevel(), designation.getDepartment().getDeptId(),
					designation.getDepartment().getDeptName()));
		}
		return designationWrappers;

	}

	/**
	 * Gets a single designation based on Id.
	 * 
	 * @param designationId
	 * @return Designation
	 * @throws ServiceException
	 */
	public Designation getDesignationById(Long designationId) throws ServiceException {

		Designation designationById = designationRepository.findByDesignationId(designationId);
		if (designationById == null) {
			throw new ServiceException("Designation does't exist.");
		}
		return designationById;
	}

	/**
	 * Add a Designation
	 * 
	 * @param designationWrapper
	 * @return Designation
	 * @throws ServiceException
	 */
	public Designation saveDesignation(DesignationWrapper designationWrapper) throws ServiceException {
		
		Designation designation;

		Designation designationById = designationRepository.findByDesignationId(designationWrapper.getDesignationId());
		if (designationById != null) {
			throw new ServiceException("Designation already exist.");
		}

		Designation designationByName = designationRepository.findByDesignation(designationWrapper.getDesignationName());
		if (designationByName != null) {
			throw new ServiceException("Designation already exist.");
		}
		
		designation = new Designation(designationWrapper.getDesignationId(), designationWrapper.getDesignationName(),
													designationWrapper.getLevel());
		
		Department department = departmentService.findById(designationWrapper.getDeptId());
		if(department!=null) {
			designation.setDepartment(department);
		}

		return designationRepository.save(designation);
	}

	/**
	 * Edit a Designation
	 * 
	 * @param designationId
	 * @param designationWrapper
	 * @return Designation
	 * @throws ServiceException
	 */
	public Designation upadteDesignation(Long designationId, DesignationWrapper designationWrapper) throws ServiceException {

		Designation designationById = designationRepository.findByDesignationId(designationId);
		if (designationById == null) {
			throw new ServiceException("Designation does't exist.");
		}
		Designation designationByName = designationRepository.findByDesignation(designationWrapper.getDesignationName());
		if (designationByName != null) {
			throw new ServiceException("Designation already exist.");
		}
		designationById.setDesignation(designationWrapper.getDesignationName());
		designationById.setLevel(designationWrapper.getLevel());
		Department department = departmentService.findById(designationWrapper.getDeptId());
		designationById.setDepartment(department);
		return designationRepository.save(designationById);
	}

	/**
	 * Delete a Designation
	 * 
	 * @param designationId
	 * @throws ServiceException
	 */
	public void deleteDesignation(Long designationId) throws ServiceException {

		Designation designationById = designationRepository.findByDesignationId(designationId);
		if (designationById == null) {
			throw new ServiceException("Designation does't exist.");
		}
		designationRepository.delete(designationById);
	}

	public List<DepartmentWithDesignationListWrapper> getDesignationsWithDepartment() {
//		List<Designation> designations = getDesignations();
		List<Designation> designations = designationRepository.findAll();
		List<DepartmentWithDesignationListWrapper> departmentWithDesignationListWrappers = new ArrayList<DepartmentWithDesignationListWrapper>();
		Map<Department, List<Designation>> groupedDesignation = new HashMap<Department, List<Designation>>();
		for (Designation designation : designations) {
			Department key = designation.getDepartment();
			if (groupedDesignation.get(key) == null) {
				groupedDesignation.put(key, new ArrayList<Designation>());
			}
			groupedDesignation.get(key).add(designation);
		}
		for (Department department : groupedDesignation.keySet()) {
			departmentWithDesignationListWrappers
					.add(new DepartmentWithDesignationListWrapper(department, groupedDesignation.get(department)));
		}
		Collections.sort(departmentWithDesignationListWrappers, new Comparator<DepartmentWithDesignationListWrapper>() {
			@Override
			public int compare(DepartmentWithDesignationListWrapper e1, DepartmentWithDesignationListWrapper e2) {
				long departmentId1 = e1.getDepartmentId();
				long departmentId2 = e2.getDepartmentId();
				if (departmentId1 > departmentId2) {
					return 1;
				} else if (departmentId1 < departmentId2) {
					return -1;
				}
				return 0;
			}
		});
		return departmentWithDesignationListWrappers;
	}

}
