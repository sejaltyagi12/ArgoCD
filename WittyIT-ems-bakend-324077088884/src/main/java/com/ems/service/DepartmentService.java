package com.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Department;
import com.ems.exception.ServiceException;
import com.ems.repository.DepartmentRepository;

@Service
@Transactional
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * Find by id.
	 *
	 * @param id
	 * 
	 * @return the Department
	 */
	public Department findById(Integer id) {
		return departmentRepository.findByDeptId(id);
	}

	/**
	 * Fetch all Department.
	 *
	 * 
	 * @return the Department
	 */
	public List<Department> getDepartments() {
		return departmentRepository.findAllOrderByDeptId();

	}
	
	/**
	 * Gets a single department based on Id.
	 * 
	 * @param DeptId
	 * @return Department
	 * @throws ServiceException
	 */
	public Department getDepartmentById(Integer DeptId) throws ServiceException {

		Department deptById = departmentRepository.findByDeptId(DeptId);
		if (deptById == null) {
			throw new ServiceException("Department does't exist.");
		}
		return deptById;
	}

	/**
	 * Add a Department
	 * 
	 * @param department
	 * @return Department
	 * @throws ServiceException
	 */
	public Department saveDepartment(Department department) throws ServiceException {

		Department deptById = departmentRepository.findByDeptId(department.getDeptId());
		if (deptById != null) {
			throw new ServiceException("Department already exist.");
		}

		Department deptByName = departmentRepository.findByDeptName(department.getDeptName());
		if (deptByName != null) {
			throw new ServiceException("Department already exist.");
		}

		return departmentRepository.save(department);
	}

	/**
	 * Edit a Department
	 * 
	 * @param DeptId
	 * @param department
	 * @return Department
	 * @throws ServiceException
	 */
	public Department upadteDepartment(Integer DeptId, Department department) throws ServiceException {

		Department deptById = departmentRepository.findByDeptId(DeptId);
		if (deptById == null) {
			throw new ServiceException("Department does't exist.");
		}
		deptById.setDeptName(department.getDeptName());
		return departmentRepository.save(deptById);
	}

	/**
	 * Delete a Department
	 * 
	 * @param DeptId
	 * @throws ServiceException
	 */
	public void deleteDepartment(Integer DeptId) throws ServiceException {

		Department deptById = departmentRepository.findByDeptId(DeptId);
		if (deptById == null) {
			throw new ServiceException("Department does't exist.");
		}
		departmentRepository.delete(deptById);
	}

}
