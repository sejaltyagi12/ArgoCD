package com.ems.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ems.configuration.PropConfig;
import com.ems.domain.Avatar;
import com.ems.domain.Designation;
import com.ems.domain.Employee;
import com.ems.domain.LeaveAccount;
import com.ems.domain.LeaveHistory;
import com.ems.domain.Resignation;
import com.ems.domain.Role;
import com.ems.enums.MaritalStatus;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.AvatarRepository;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.NomineeRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.servicefinder.utils.UserUtils;
import com.ems.wrappers.ChangePasswordWrapper;
import com.ems.wrappers.EmployeeChartWrapper;
import com.ems.wrappers.EmployeeRegistrationWrapper;
import com.ems.wrappers.EmployeeWrapper;
import com.ems.wrappers.EventWrapper;
import com.ems.wrappers.ExcelReportWrapper;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.NomineeWrapper;
import com.ems.wrappers.UpdateProfileWrapper;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private EvaluationTargetService evaluationTargetService;

	@Autowired
	private CompanyCategoryService companyCategoryService;

	@Autowired
	private AvatarRepository avatarRepository;

	@Autowired
	private LeaveAccountService leaveAccountService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PropConfig propConfig;

	@Autowired
	private NomineeRepository nomineeRepository;
	
	@Autowired
	private ResignationService resignationService;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
	
	private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the employee
	 */
	public Employee findByEmail(String email) {
		return employeeRepository.findByOfficialEmailAndDeleted(email, false);
	}

	/**
	 * Find by Employee Code.
	 *
	 * @param code the Employee Code
	 * @return the employee
	 */
	public Employee findByEmpCode(String empCode) {
		return employeeRepository.findByEmpCode(empCode);
	}

	/**
	 * Save the recovery password (temporary password) to send to the employee
	 * later.
	 *
	 * @param user             the employee
	 * @param recoveryPassword the recovery password
	 */
	@Transactional(readOnly = false)
	public void saveRecoveryPassword(Employee employee, String recoveryPassword) {
		employee.setRecoveryPassword(recoveryPassword);
		saveOnly(employee);
	}

	/**
	 * Save the user.
	 *
	 * @param user the user
	 */
	@Transactional(readOnly = false)
	public void saveOnly(Employee employee) {
		employeeRepository.saveAndFlush(employee);
	}

	/**
	 * Find by id.
	 *
	 * @param email the id
	 * @return the employee
	 * @throws ServiceException
	 */
	public Employee findById(Long id) throws ServiceException {
		Employee employee = employeeRepository.findByIdAndDeleted(id, false);

		if (employee == null)
			throw new ServiceException("Employee not found. May have resigned.");

		return employee;
	}

	/**
	 * find Employee by his id
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public Employee findByEmployeeId(Long id) throws ServiceException {
		Employee employee = employeeRepository.findOne(id);
		if (employee == null)
			throw new ServiceException("Employee not found.");

		return employee;
	}

	/**
	 * Find by id.
	 *
	 * @param email the id
	 * @return the employee
	 * @throws ServiceException
	 */
	public List<Employee> findByActive() throws ServiceException {
		return employeeRepository.findByIsActive(true);
	}

	/**
	 * Creates new Employee Wrapper.
	 *
	 * @param code the Employee
	 * @return the employeeWrapper
	 * @throws ServiceException
	 */
	public EmployeeWrapper createEmployeeWrapper(Employee employee) throws ServiceException {
		EmployeeWrapper employeeWrapper = new EmployeeWrapper(employee);

		if (employee.getManagerId() != null) {
			Employee manager = null;
			try {
				manager = findById(employee.getManagerId());
				if (employee.getId().longValue() != employee.getManagerId().longValue()) {
					employeeWrapper.setManagerName(manager.getFullName());
					employeeWrapper.setManagerCode(manager.getEmpCode());
				} else {
					employeeWrapper.setManagerId(null);
				}
			} catch (Exception e) {
			}
		}

		String avatar = "";

		if (employee.getAvatar() != null)
			avatar = getAvatarCompleteUrl(employee.getAvatar());

		employeeWrapper.setAvatarCompleteUrl(avatar);

		employeeWrapper.setIsOnlyEmployee(isOnlyEmployee());

		return employeeWrapper;
	}

	/**
	 * Get Employee Details.
	 *
	 * @return the EmployeeWrapper
	 * @throws ServiceException
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	public EmployeeWrapper getEmployeeDetails() throws ServiceException {
		Employee employee = authenticationService.getAuthenticatedEmployee();

		createEmployeeLeaveAccountIfNotExist(employee);

		EmployeeWrapper employeeWrapper = createEmployeeWrapper(employee);

		// Hiding default user data.
		if (employee.getId().longValue() == 1l) {
			employeeWrapper.setDesignation("");
			employeeWrapper.setDepartmentName("");
			employeeWrapper.setDeptId(0);
			employeeWrapper.setDesignationId(0l);
			// employeeWrapper.setRole(null);
			// employeeWrapper.setJoiningDate(null);
			// employeeWrapper.setDob(null);
		}

		return employeeWrapper;
	}

	/**
	 * Saves and Update Employee Details.
	 *
	 * @param employeeWrapper
	 * @throws ServiceException
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	public void saveEmployee(EmployeeRegistrationWrapper employeeWrapper) throws ServiceException {
		Employee employee = null;
		Context ctx = new Context();
		boolean isUpdate = true;
		Boolean flagToUpdate = false;
		Designation designation = designationService.findById(employeeWrapper.getDesignationId());
		if (employeeWrapper.getRole().equalsIgnoreCase("Admin")) {
			String department = designation.getDepartment().getDeptName();
			if (department.equalsIgnoreCase("Human Resource") || department.equalsIgnoreCase("hr")
					|| department.equalsIgnoreCase("hr dept")) {
				int level = Integer.parseInt(designation.getLevel().split("_")[1]);
				if (level < Constants.HR_ACCESS_LEVEL) {
					throw new ServiceException("please select correct designation.");
				}
			} else if (!department.equalsIgnoreCase("Management")) {
				throw new ServiceException("please select Human Resource department.");
			}
		}

		if (!authenticationService.isHR() && authenticationService.isEmployee()) {
			employee = authenticationService.getAuthenticatedEmployee();
			if (employee != null) {
				ArrayList<UpdateProfileWrapper> updateProfileWrappers = getUpdateProfileWrapper(employeeWrapper,
						employee);
				if (updateProfileWrappers.isEmpty()) {
					ctx = null;
				} else {
					ctx.setVariable("record", updateProfileWrappers);
				}
				employee.setPrimaryPhone(employeeWrapper.getPrimaryPhone());
				employee.setPersonalEmail(employeeWrapper.getPersonalEmail());
				employee.setPresentAddress(employeeWrapper.getPresentAddress());
				employee.setMaritalStatus(employeeWrapper.getMaritalStatus());
				if (ctx != null) {
					ctx.setVariable("empName", UserUtils.toCamelCase(employee.getFullName()));
					ctx.setVariable("empCode", employee.getEmpCode());
				}
			} else {
				throw new ServiceException("Employee doesnot exist");
			}
		} else if (authenticationService.isHrOrAdmin()) {
			if (employeeWrapper.getId() != null) {
				employee = employeeRepository.findByIdAndDeleted(employeeWrapper.getId(), false);
				if (employee != null) {
					processJoiningDateChange(employeeWrapper, employee);
					if (evaluationTargetService.getCurrentCycle(employee.getDesignation().getDepartment()) != null) {
						if (!employeeWrapper.getManagerId().equals(employee.getManagerId()))
							throw new ServiceException("Cannot change manager during evaluation progress.");
					}
					if (employee.getEmpCode() == null || !employee.getEmpCode().equals(employeeWrapper.getEmpCode())) {
						checkIfEmployeeCodeExists(employeeWrapper.getEmpCode());
					}
					if (employee.getOfficialEmail() == null
							|| !employee.getOfficialEmail().equals(employeeWrapper.getOfficialEmail())) {
						checkIfEmailExists(employeeWrapper.getOfficialEmail());
					}

					ArrayList<UpdateProfileWrapper> updateProfileWrappers = getUpdateProfileWrapper(employeeWrapper,
							employee);
					if (updateProfileWrappers.isEmpty()) {
						ctx = null;
					} else {
						ctx.setVariable("record", updateProfileWrappers);
					}
					employee.setEmployeeDetail(employeeWrapper);
					if (ctx != null) {
						ctx.setVariable("empName", UserUtils.toCamelCase(employee.getFullName()));
						ctx.setVariable("empCode", employee.getEmpCode());
					}
				} else {
					throw new ServiceException("Employee doesnot exist");
				}
			} else {
				validateEmployee(employeeWrapper);
				employee = new Employee(employeeWrapper);
				ctx.setVariable("empName", UserUtils.toCamelCase(employee.getFirstName()));
				ctx.setVariable("password", employeeWrapper.getPassword());
				ctx.setVariable("officialEmail", employee.getOfficialEmail());
				isUpdate = false;
			}

			if (employeeWrapper.getDeptId().longValue() == 9l && employee.getManagerId() != null)
				throw new ServiceException("Manager is not necessary for the management department.");

			// Setting the default CEO manager for employees without the manager
			// field.As now it's not mandatory anymore.
			if (employee.getManagerId() == null)
				employee.setManagerId(2l);

			// // Only for management department setting employee's manager to
			// // employee itself.
			// if (employeeWrapper.getDeptId().longValue() == 9l) {
			// employee.setManagerId(employee.getId());
			// }

			int empLevel = Integer.parseInt(designation.getLevel().split("_")[1]);
			Employee newManager = employeeRepository.findByIdAndDeleted(employee.getManagerId(), false);
			int managerLevel = Integer.parseInt(newManager.getDesignation().getLevel().split("_")[1]);

			// Leaving management department
			if (empLevel > managerLevel && employeeWrapper.getDeptId().longValue() != 9l)
				throw new ServiceException("Manager level is not smaller then from employee level");
//				throw new ServiceException("Manager can only be from senior level");

			// if (employeeWrapper.getDeptId().longValue() == 9l &&
			// employee.getManagerId() != employee.getId())
			// throw new ServiceException(
			// "Manager of a management department employee should be the
			// employee himself.");

			// Setting Level date changed from zero.In case creation joining
			// date
			// will be set if designation is higher then level 0.
			int level = Integer.parseInt(designation.getLevel().split("_")[1]);
			if (employee.getId() != null) {
				if (level > 0 && employee.getLevelChangedFromZero() == null) {
					employee.setLevelChangedFromZero(new DateTime());
					flagToUpdate = true;
				} else if (level == 0 && employee.getLevelChangedFromZero() != null) {
					LeaveAccount leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
							DateUtil.getFinancialYear(new LocalDate()));

					List<LeaveHistory> leaveHistories = leaveService.findHistoryByEmpId(employee.getId());

					if (leaveAccount != null) {
						if (!leaveHistories.isEmpty()) {
							throw new ServiceException(
									"Can't change the designation to trainee as already taken leaves. Please contact support team");
						} else {
							employee.setLevelChangedFromZero(null);
							// Leave account will generate while login first
							// time. Not now as new designation is not persisted
							// till now. It will persist after this method ends.
							leaveAccountService.deleteByEmpId(employee.getId());
						}
					} else
						throw new ServiceException("Can't able to fetch accounts. Please contact support team");
				}
			} else {
				// In case new employee registration
				if (level > 0 && employee.getLevelChangedFromZero() == null)
					employee.setLevelChangedFromZero(new DateTime(employee.getJoiningDate()));
			}

			employee.setDesignation(designation);
			employee.setRole(roleService.findById(employeeWrapper.getRoleId()));

			employee.setCompanyCategory(companyCategoryService.findById(employeeWrapper.getCompanyId()));
		}
		if (employee.getNominee() != null && employee.isNomineeWrapperEmpty(employeeWrapper.getNomineeDetails())) {
			long id = employee.getNominee().getNomineeId();
			nomineeRepository.delete(id);
		}
		employee.setNomineeDetails(employeeWrapper.getNomineeDetails());
		Employee savedEmp = employeeRepository.saveAndFlush(employee);

		// Only for management department setting employee's manager to
		// employee itself.
		if (savedEmp.getDesignation().getDepartment().getDeptId().longValue() == 9l) {
			savedEmp.setManagerId(savedEmp.getId());
			employeeRepository.saveAndFlush(savedEmp);
		}

		if (flagToUpdate)
			leaveAccountService.updateLeaveAccountWithPrivilegeLeave(employee);

		createEmployeeLeaveAccountIfNotExist(employee);
		// Adding wittyEMSLink in the email notification of registration.
		ctx.setVariable("wittyEMSLink", propConfig.getApplicationUrl());
		if (propConfig.isIncludeLeaveMail())
			sendMail(isUpdate, employee, ctx);

	}

	/**
	 * Get list of UpdateProfileWrapper.
	 * 
	 * @param employeeWrapper
	 * @param employee
	 * @return List of UpdateProfileWrapper.
	 * @throws ServiceException 
	 */
	private ArrayList<UpdateProfileWrapper> getUpdateProfileWrapper(EmployeeRegistrationWrapper employeeWrapper,
			Employee employee) throws ServiceException {
		ArrayList<UpdateProfileWrapper> updateProfileWrappers = new ArrayList<UpdateProfileWrapper>();
		addUpdatedField(updateProfileWrappers, "Emp Code", employee.getEmpCode(), employeeWrapper.getEmpCode());

		addUpdatedField(updateProfileWrappers, "First Name", employee.getFirstName(), employeeWrapper.getFirstName());

		addUpdatedField(updateProfileWrappers, "Middle Name", employee.getMiddleName(),
				employeeWrapper.getMiddleName());

		addUpdatedField(updateProfileWrappers, "Last Name", employee.getLastName(), employeeWrapper.getLastName());

		addUpdatedField(updateProfileWrappers, "father's Name", employee.getFatherName(),
				employeeWrapper.getFatherName());

		addUpdatedField(updateProfileWrappers, "DOB", DateUtil.removeTime(employee.getDob()),
				DateUtil.removeTime(employeeWrapper.getDob().toDate()));

		addUpdatedField(updateProfileWrappers, "Designation", employee.getDesignation().getDesignation(),
				employeeWrapper.getDesignation());

		addUpdatedField(updateProfileWrappers, "Joining Date", DateUtil.removeTime(employee.getJoiningDate()),
				DateUtil.removeTime(employeeWrapper.getJoiningDate().toDate()));

		addUpdatedField(updateProfileWrappers, "Location", employee.getLocation(), employeeWrapper.getLocation());

		addUpdatedField(updateProfileWrappers, "Official Email", employee.getOfficialEmail(),
				employeeWrapper.getOfficialEmail());

		addUpdatedField(updateProfileWrappers, "Bank Name", employee.getBankName(), employeeWrapper.getBankName());
		
		// Check if bank account number is being updated or not
		if (!employee.getBankAccountNumber().equals(employeeWrapper.getBankAccountNumber())) {
			// check if bank account number is already exist in database then throw error
			if (employeeRepository.isBankAccountNumberAlreadyExistOrNot(employeeWrapper.getBankAccountNumber()))
				throw new ServiceException("Bank account number already exists.");
			else {
				addUpdatedField(updateProfileWrappers, "Bank Account Number", employee.getBankAccountNumber(),
						employeeWrapper.getBankAccountNumber());
			}
		}
		
		addUpdatedField(updateProfileWrappers, "Bank IfSC", employee.getBankIfsc(), employeeWrapper.getBankIfsc());

		addUpdatedField(updateProfileWrappers, "Personal Email", employee.getPersonalEmail(),
				employeeWrapper.getPersonalEmail());

		addUpdatedField(updateProfileWrappers, "Present Address", employee.getPresentAddress(),
				employeeWrapper.getPresentAddress());

		addUpdatedField(updateProfileWrappers, "Permanent Address", employee.getPermanentAddress(),
				employeeWrapper.getPermanentAddress());

		addUpdatedField(updateProfileWrappers, "Primary Phone", employee.getPrimaryPhone(),
				employeeWrapper.getPrimaryPhone());

		addUpdatedField(updateProfileWrappers, "Secondary Phone", employee.getSecondaryPhone(),
				employeeWrapper.getSecondaryPhone());

		addUpdatedField(updateProfileWrappers, "Pan Number", employee.getPanNumber(), employeeWrapper.getPanNumber());

		addUpdatedField(updateProfileWrappers, "Uan Number", employee.getUanNumber(), employeeWrapper.getUanNumber());

		addUpdatedField(updateProfileWrappers, "Aadhar Number", employee.getAadhar(), employeeWrapper.getAadhar());

		addUpdatedField(updateProfileWrappers, "Blood Group", employee.getBloodGroup(),
				employeeWrapper.getBloodGroup());

		addUpdatedField(updateProfileWrappers, "Role", employee.getRole().getRoleName(), employeeWrapper.getRole());

		addUpdatedField(updateProfileWrappers, "Hobbies", employee.getHobbies(), employeeWrapper.getHobbies());

		addUpdatedField(updateProfileWrappers, "Nationality", employee.getNationality(),
				employeeWrapper.getNationality());

		addUpdatedField(updateProfileWrappers, "Religion", employee.getReligion(), employeeWrapper.getReligion());

		addUpdatedField(updateProfileWrappers, "Marital Status", employee.getMaritalStatus().name(),
				employeeWrapper.getMaritalStatus().name());

		addUpdatedField(updateProfileWrappers, "Religion", employee.getReligion(), employeeWrapper.getReligion());

		Employee emp1 = employee.getManagerId() != null ? employeeRepository.findOne(employee.getManagerId()) : null;
		Employee emp2 = employeeWrapper.getManagerId() != null
				? employeeRepository.findOne(employeeWrapper.getManagerId())
				: null;

		addUpdatedField(updateProfileWrappers, "Manager Name", emp1 != null ? emp1.getFullName() : "",
				emp2 != null ? emp2.getFullName() : "");

		addUpdatedField(updateProfileWrappers, "Nominee Name",
				employee.getNominee() != null ? employee.getNominee().getNomineeName() : "",
				employeeWrapper.getNomineeDetails().getNomineeName());

		addUpdatedField(updateProfileWrappers, "Nominee Relation",
				employee.getNominee() != null ? employee.getNominee().getNomineeRelation() : "",
				employeeWrapper.getNomineeDetails().getNomineeRelation());

		addUpdatedField(updateProfileWrappers, "Nominee Phone",
				employee.getNominee() != null ? employee.getNominee().getNomineePhone() : "",
				employeeWrapper.getNomineeDetails().getNomineePhone());

		addUpdatedField(updateProfileWrappers, "Nominee Email",
				employee.getNominee() != null ? employee.getNominee().getNomineeEmail() : "",
				employeeWrapper.getNomineeDetails().getNomineeEmail());

		addUpdatedField(updateProfileWrappers, "Nominee Permanent Address",
				employee.getNominee() != null ? employee.getNominee().getNomineePermanentAddress() : "",
				employeeWrapper.getNomineeDetails().getNomineePermanentAddress());

		return updateProfileWrappers;
	}

	/**
	 * check data is added to UpdateProfileWrapper list if true than add otherwise
	 * not.
	 * 
	 * @param updateProfileWrappers
	 * @param fieldName
	 * @param preValue
	 * @param newValue
	 */
	private void addUpdatedField(ArrayList<UpdateProfileWrapper> updateProfileWrappers, String fieldName,
			String preValue, String newValue) {
		if (!StringUtils.isBlank(preValue)) {
			if (!preValue.equals(newValue)) {
				updateProfileWrappers.add(new UpdateProfileWrapper(fieldName, preValue, newValue));
			}
		} else if (!StringUtils.isBlank(newValue)) {
			if (!newValue.equals(preValue)) {
				updateProfileWrappers.add(new UpdateProfileWrapper(fieldName, preValue, newValue));
			}
		}
	}

	/**
	 * Send mail at the time of new Registeration and profile update.
	 * 
	 * @param isUpdate
	 * @param toEmp
	 * @param ctx
	 */
	private void sendMail(boolean isUpdate, Employee toEmp, Context ctx) {
		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getMailUser());
		if (isUpdate) {
			if (ctx != null) {
				// List<Employee> hrEmployees =
				// employeeRepository.findAllHRAndNotDeleted("Human Resource",
				// false);
				List<String> hrEmployees = propConfig.getLeaveMailCcArrayList();
				if (!hrEmployees.isEmpty()) {
					for (String emp : hrEmployees) {

						sendMail(from, "HR Department", emp, "Profile Update",
								this.templateEngine.process("profile_update.html", ctx));
					}
				}
			}
		} else {
			sendMail(from, toEmp.getFullName(), toEmp.getOfficialEmail(), "Account Created",
					this.templateEngine.process("registeration.html", ctx));
		}
	}

	/**
	 * Checks If Email Exist.
	 *
	 * @param email
	 * @throws ServiceException
	 */
	public void checkIfEmailExists(String email) throws ServiceException {
		if (StringUtils.isEmpty(email)) {
			throw new ServiceException("Email an not empty.");
		}
		Employee employee = employeeRepository.findByOfficialEmail(email);
		if (employee != null) {
			if (employee.isDeleted())
				throw new ServiceException("Email already exist and the account is inactive.");
			else
				throw new ServiceException("Email already exist");
		}
	}

	/**
	 * Checks If Email Exist.
	 *
	 * @param email
	 * @throws ServiceException
	 */
	public void checkIfEmployeeCodeExists(String empCode) throws ServiceException {
		if (StringUtils.isEmpty(empCode)) {
			throw new ServiceException("Employee Code can not empty.");
		}
		Employee employee = findByEmpCode(empCode);
		if (employee != null)
			throw new ServiceException("Employee Code already exist");
	}

	/**
	 * Edits an Employee Details.
	 *
	 * @param employeeWrapper
	 * @throws ServiceException
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	public void editEmployee(EmployeeRegistrationWrapper employeeWrapper) throws ServiceException {
		if (employeeWrapper.getEmpCode() != null && employeeWrapper.getId() != null) {
			Employee employee = employeeRepository.findByIdAndDeleted(employeeWrapper.getId(), false);

			if (employee != null) {
				// employeeWrapper.setEncodedPassword(employee.getPassword());

				processJoiningDateChange(employeeWrapper, employee);

				if (employee.getDesignation() != null
						&& evaluationTargetService.getCurrentCycle(employee.getDesignation().getDepartment()) != null) {
					if (!employeeWrapper.getManagerId().equals(employee.getManagerId()))
						throw new ServiceException("Cannot change manager during evaluation progress.");
				}

				if (employee.getEmpCode() != null) {
					if (!employee.getEmpCode().equals(employeeWrapper.getEmpCode()))
						checkIfEmployeeCodeExists(employeeWrapper.getEmpCode());
				} else
					checkIfEmployeeCodeExists(employeeWrapper.getEmpCode());

				saveEmployee(employeeWrapper);
			} else {
				throw new ServiceException("Employee doesnot exist");
			}
		} else {
			throw new ServiceException("Employee code or id doesnot exist");
		}
	}

	/*
	 * Checks if joining date is changed. If yes, then will check if an employee
	 * applied for any leaves. Changing Joining Date after applying leave is not
	 * allowed
	 */
	public void processJoiningDateChange(EmployeeRegistrationWrapper employeeWrapper, Employee employee)
			throws ServiceException {
		if ((DateUtil.resetTime(employeeWrapper.getJoiningDate().toDate())
				.equals(DateUtil.resetTime(employee.getJoiningDate()))))
			return;

		LeaveAccount leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
				DateUtil.getFinancialYear(new LocalDate()));

		List<LeaveHistory> leaveHistories = leaveService.findHistoryByEmpId(employee.getId());

		if (leaveAccount != null) {
			if (!leaveHistories.isEmpty()) {
				throw new ServiceException(
						"Can't change joining date after applying leave. Please contact support team");
			} else {

				// Setting the date of change of level if joining date and date
				// of change of level were previously same.
				if (employee.getLevelChangedFromZero() != null && (employee.getLevelChangedFromZero()
						.withTimeAtStartOfDay().isEqual(new DateTime(employee.getJoiningDate()).withTimeAtStartOfDay())
						|| employee.getLevelChangedFromZero().withTimeAtStartOfDay()
								.isBefore(new DateTime(employeeWrapper.getJoiningDate())))) {
					employee.setLevelChangedFromZero(employeeWrapper.getJoiningDate());
				}

				employee.setJoiningDate(employeeWrapper.getJoiningDate());
				leaveAccountService.resetLeaveAccount(employee);
			}
		} else
			throw new ServiceException("Can't able to fetch accounts. Please contact support team");
	}

	// Create leave account for an employee.
	public void createEmployeeLeaveAccountIfNotExist(Employee employee) throws ServiceException {
		leaveAccountService.addLeaveAccount(employee, DateUtil.getFinancialYear(new LocalDate()), false);
	}

	/**
	 * Activate or deactivates an Employee.
	 *
	 * @param id the Employee id
	 * @throws ServiceException
	 */
	public void editEmployeeStatus(Long id, Boolean active) throws ServiceException {
		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		Employee employee = findById(id);

		if (employee != null) {
			if (authenticatedEmployee.getId() == employee.getId())
				throw new ServiceException("You can't change your status.");

			employee.setIsActive(active);
			employeeRepository.save(employee);

			return;
		}

		throw new ServiceException("Employee not found.");
	}

	/**
	 * Resets the password of an Employee (Admin Only).
	 *
	 * @param passwordWrapper
	 * @throws ServiceException
	 * @see com.ems.wrappers.ChangePasswordWrapper
	 */
	public void resetPassword(ChangePasswordWrapper passwordWrapper) throws ServiceException {

		if (!authenticationService.isAdmin() && !authenticationService.isHR()) {
			throw new ServiceException("You are not Admin");
		}

		Employee employee = findById(passwordWrapper.getEmpId());

		if (employee == null) {
			employee = findByEmail(passwordWrapper.getEmpEmail());
		}

		if (employee != null) {
			employee.setPassword(passwordEncoder.encode(passwordWrapper.getNewPassword()));
			employeeRepository.save(employee);
		} else {
			throw new ServiceException("Employee does not exist.");
		}
	}

	/**
	 * Changes the password of an Employee.
	 *
	 * @param newPassword
	 * @param newPassword
	 * @throws ServiceException
	 */
	public Employee changePassword(String oldPassword, String newPassword) throws ServiceException {

		Employee employee = authenticationService.getAuthenticatedEmployee();

		if (passwordEncoder.matches(oldPassword, employee.getPassword())) {
			employee.setPassword(passwordEncoder.encode(newPassword));
			employeeRepository.save(employee);
			return employee;
		}

		throw new ServiceException("Old password does not match");
	}

	/**
	 * Changes the password of an Employee.
	 *
	 * @param newPassword
	 * @param newPassword
	 * @throws ServiceException
	 */
	public void changePassword(String password, Employee employee) throws ServiceException {
		employee.setPassword(passwordEncoder.encode(password));
		employeeRepository.save(employee);
	}

	/**
	 * Fetches list of all Employees.
	 *
	 * @throws ServiceException
	 * @see com.ems.wrappers.EmployeeWrapper
	 */
	public List<EmployeeWrapper> getAllEmployees() throws ServiceException {
		List<Employee> employeeList = employeeRepository.findAll();

		List<EmployeeWrapper> employeeWrapperList = new ArrayList<>();

		if (!employeeList.isEmpty()) {
			for (Employee employee : employeeList) {
				// Skipping default employees. Super admin and CEO
				if (employee.getId().longValue() == 1l || employee.getId().longValue() == 2l)
					continue;
				employeeWrapperList.add(createEmployeeWrapper(employee));
			}
		}

		return employeeWrapperList;
	}

	/**
	 * Gets Employee chart data.
	 *
	 * @return EmployeeChartWrapper with chart relates data
	 * @throws ServiceException
	 * @see com.ems.wrappers.EmployeeChartWrapper
	 * @see com.ems.wrappers.ChartData
	 */
	public EmployeeChartWrapper getEmployeeChartData() throws ServiceException {
		EmployeeChartWrapper chartWrapper = new EmployeeChartWrapper(
				employeeRepository.getDepartmentEmployeesGraphData(true),
				employeeRepository.getCompanyEmployeesGraphData(true),
				employeeRepository.getDesignationEmployeesGraphData(true));

		return chartWrapper;
	}

	/**
	 * To change Avatar Url associated: /employee/avatar/upload
	 * 
	 * @param avatar
	 * @throws ServiceException
	 */
	public String changeAvatar(Avatar avatar) throws ServiceException {
		try {
			Employee employee = authenticationService.getAuthenticatedEmployee();

			Avatar storedAvatar = avatarRepository.findByEmployee(employee);

			try {
				UserUtils.validateFileSize(512, avatar.getAvatarContent(), "avatar", false, true, false);
			} catch (ServiceException e) {
				throw e;
			}

			if (storedAvatar != null)
				avatar.setId(storedAvatar.getId());

			avatar.setEmployee(employee);
			avatar.setName(avatar.getAvatarContent().getOriginalFilename());
			String fileName = UserUtils.getAssetStandardName(avatar.getName(), "avatar");

			String avatarUrl = storeAsset(employee, avatar.getAvatarContent().getBytes(), fileName);
			avatarUrl = avatarUrl.replaceAll("\\\\", "/");
			avatar.setAvatarUrl(avatarUrl);

			avatarRepository.save(avatar);

			return getAvatarCompleteUrl(avatar);
		} catch (Exception e) {
			// return RestResponse.error("Error while changing avatar.");
		}

		throw new ServiceException("Error while changing avatar.");
	}

	/**
	 * Gets the Avatar complete url.
	 * 
	 */
	public String getAvatarCompleteUrl(Avatar avatar) {
		if (avatar.getAvatarUrl() != null)
			return avatar.getAvatarUrl() != null
					? propConfig.getApplicationUrl() + propConfig.getBaseContext() + avatar.getAvatarUrl()
					: null;

		return null;
	}

	/**
	 * Store the assets in local file system for DEV and QA server Store the assets
	 * in Amazon S3 for Production and STAGE server
	 * 
	 */
	public String storeAsset(Employee employee, byte[] fileContent, String fileName) throws Exception {

		fileName = UserUtils.validateSpecialCharacters(fileName);
		String assetBasePathPath = propConfig.getAssetBasePath() + File.separator;
		String contextPath = UserUtils.getPath(employee);
		String path = assetBasePathPath + contextPath;

		String absolutePath = path + fileName;
		try {
			File dirFile = new java.io.File(path);
			if (!dirFile.exists()) {
				if (!dirFile.mkdirs()) {
					throw new Exception(
							"The File: " + fileName + ", could not be uploaded, because no upload dir exists.");
				}
			}

			FileUtils.writeByteArrayToFile(new java.io.File(absolutePath), fileContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contextPath + fileName;
	}

	/**
	 * Gets Upcomming events.
	 *
	 * @return BithdayWrapper list
	 * @throws ServiceException
	 * @see com.ems.wrappers.EventWrapper
	 */
	public List<EventWrapper> getUpcommingEvents() throws ServiceException {
		final DateTime nowdate = new DateTime().withTimeAtStartOfDay();

		List<EventWrapper> eventWrappers = new ArrayList<EventWrapper>();
		// Excluding 'Super Admin and Default CEO accuont'
		List<Employee> allEmployees = employeeRepository.findByIsActiveAndIdNotIn(true, Arrays.asList(1l, 2l));
		
		List<Employee> upcommingBirthday = new ArrayList<>();
		List<Employee> upcommingAnniversary = new ArrayList<>();
		List<Employee> newJoinee = new ArrayList<>();
		for (int i = 0; i < allEmployees.size(); i++) {
			Employee emp = allEmployees.get(i);
			DateTime dobWithCurrentYear = new DateTime(emp.getDob())
					.withYear(nowdate.getYear() + determineYear(emp, nowdate));
			if (dobWithCurrentYear.isAfter(nowdate) && dobWithCurrentYear.isBefore(nowdate.plusDays(30))) {
				upcommingBirthday.add(emp);
			}
			DateTime joiningWithCurrentYear = new DateTime(emp.getJoiningDate())
					.withYear(nowdate.getYear() + determineJoiningYear(emp, nowdate));
			
			boolean isSameMonthAndYear = new DateTime(emp.getJoiningDate()).getDayOfMonth() == nowdate.getDayOfMonth()
				    && new DateTime(emp.getJoiningDate()).getMonthOfYear() == nowdate.getMonthOfYear()
				    && new DateTime(emp.getJoiningDate()).getYear() == nowdate.getYear();
			
			//New joinee arrayList
			if(isSameMonthAndYear) {
				newJoinee.add(emp);
			}
			
			if (!isSameMonthAndYear && joiningWithCurrentYear.isAfter(nowdate) && joiningWithCurrentYear.isBefore(nowdate.plusDays(30))) {
				upcommingAnniversary.add(emp);
				
			}
		}
		
//		for (int i = 0; i < allEmployees.size(); i++) {
//			Employee emp = allEmployees.get(i);
//			DateTime joiningWithCurrentYear = new DateTime(emp.getJoiningDate())
//					.withYear(nowdate.getYear() + determineJoiningYear(emp, nowdate));
//			if (joiningWithCurrentYear.isAfter(nowdate) && joiningWithCurrentYear.isBefore(nowdate.plusDays(30))) {
//				upcommingAnniversary.add(emp);
//			}
			
//		}

		addEvent(upcommingBirthday, eventWrappers, "Birthday");
		addEvent(upcommingAnniversary, eventWrappers, "Work Anniversary");
		addEvent(newJoinee, eventWrappers, "New Joinee");

		Collections.sort(eventWrappers, new Comparator<EventWrapper>() {
			@Override
			public int compare(EventWrapper e1, EventWrapper e2) {
				int y1 = e1.getEventDate().getMonthOfYear()<nowdate.getMonthOfYear()?nowdate.getYear()+1:nowdate.getYear();
				int y2 = e2.getEventDate().getMonthOfYear()<nowdate.getMonthOfYear()?nowdate.getYear()+1:nowdate.getYear();
				
				int result = e1.getEventDate().withYear(y1).withTime(0, 0, 0, 0)
						.compareTo(e2.getEventDate().withYear(y2).withTime(0, 0, 0, 0));
				if (result == 0) {
					return e1.getFullName().compareTo(e2.getFullName());
				}
				return result;
			}
		});
		return eventWrappers;
	}

	/**
	 * @param emp
	 * @param nowdate
	 * @return
	 */
	private int determineYear(Employee emp, DateTime nowdate) {
		return emp.getDob().getMonth() == 0 && nowdate.getYear() < nowdate.plusDays(30).getYear() ? 1 : 0;
	}
	
	/**
	 * @param emp
	 * @param nowdate
	 * @return
	 */
	private int determineJoiningYear(Employee emp, DateTime nowdate) {
		return emp.getJoiningDate().getMonth() == 0 && nowdate.getYear() < nowdate.plusDays(30).getYear() ? 1 : 0;
	}

	/**
	 * Add upcoming event to EventWrapper.
	 * 
	 * @param employees
	 * @param eventWrappers
	 * @param eventType
	 */
	private void addEvent(List<Employee> employees, List<EventWrapper> eventWrappers, String eventType) {
		String anniversaryYear = null;
		final DateTime nowdate = new DateTime().withTimeAtStartOfDay().plusDays(30);
		
		for (Employee employee : employees) {
			String avatar = "";

			if (employee.getAvatar() != null)
				avatar = getAvatarCompleteUrl(employee.getAvatar());
			
			// It will calculate anniversary years of employee like 4th anniversary
			if(eventType.equalsIgnoreCase("Work Anniversary")) {
				DateTime joiningDate = new DateTime(employee.getJoiningDate());
				int year = nowdate.getYear() -  joiningDate.getYear();
				String yearSuffix;
			    if (year % 100 >= 11 && year % 100 <= 13) {
			        yearSuffix = "th";
			    } else {
			        switch (year % 10) {
			            case 1:  yearSuffix = "st"; break;
			            case 2:  yearSuffix = "nd"; break;
			            case 3:  yearSuffix = "rd"; break;
			            default: yearSuffix = "th"; break;
			        }
			    }
			    anniversaryYear = year + yearSuffix;
			}

			eventWrappers.add(new EventWrapper(employee.getId(), employee.getFirstName(), employee.getMiddleName(),
					employee.getLastName(), employee.getEmpCode(),
					employee.getDesignation().getDepartment().getDeptName(), avatar,
					new DateTime(
							eventType.equalsIgnoreCase("birthday") ? employee.getDob() : employee.getJoiningDate()),
					eventType, anniversaryYear));
		}

	}

	
	/**
	 * Get super admin 
	 * @return id
	 */
	public Long getSuperAdmin() {
		Long id = null;
		List<Employee> findEmpByRole = employeeRepository.findByRole("admin");
		if (!CollectionUtils.isEmpty(findEmpByRole)) {
			id = findEmpByRole.get(0).getId();
		}
		return id;
	}

	/**
	 * Send a password recover message. The user will receive via email instructions
	 * to reset the password
	 * 
	 * @param user    user to send the instructions
	 * @param request HttpServletRequest to get the url and generate the rest
	 *                password link
	 * @return RestResponse with a success or error message
	 * @throws ServiceException
	 */
	public void sendPasswordRecovery(Employee employee, HttpServletRequest request) throws ServiceException {
		Employee employeeDb = findByEmail(employee.getOfficialEmail());
		if (employeeDb == null)
			throw new ServiceException("Sorry, there is no user with this email address. Please check and try again.");
		if (!employeeDb.isActive())
			throw new ServiceException("The User is inactive.");
		String recoveryPassword = UserUtils.randomPassword();

		// String link = propConfig.getClientApplicationUrl() +
		// propConfig.getClientPath() + "/#/password/reset?token="
		// + recoveryPassword + "&email=" + employee.getOfficialEmail();

		String link = propConfig.getClientApplicationUrl() + "/#!/password/reset?token=" + recoveryPassword + "&email="
				+ employee.getOfficialEmail();
		try {
			sendPasswordRecovery(employee, link, recoveryPassword);
			return;
		} catch (Exception e) {
			throw new ServiceException("Error while sending the password recovery email.", e);
		}
	}

	/**
	 * Send password recovery.
	 *
	 * @param user             the user
	 * @param link             the link
	 * @param firmName         the firm name
	 * @param recoveryPassword the recovery password
	 * @throws ServiceException the service exception
	 */
	public void sendPasswordRecovery(Employee employee, String link, String recoveryPassword) throws ServiceException {

		employee = findByEmail(employee.getOfficialEmail());
		if (employee == null)
			throw new ServiceException("Email not found!");

		saveRecoveryPassword(employee, recoveryPassword);

		String loginUrl = propConfig.getApplicationUrl() + "/#/signin";

		Context ctx = new Context(new Locale("US"));
		ctx.setVariable("user", employee);
		ctx.setVariable("link", link);
		ctx.setVariable("loginUrl", loginUrl);
		String htmlContent = this.templateEngine.process("password-recovery-email.html", ctx);

		Employee from = new Employee();
		from.setOfficialEmail(propConfig.getMailUser());

		sendMail(from, employee.getFullName(), employee.getOfficialEmail(), "Password reset", htmlContent);
	}

	/**
	 * MVP and RESKIN Send mail.
	 *
	 * @param from    the from
	 * @param to      the to
	 * @param subject the subject
	 * @param message the message
	 * @param file    the file
	 */
	private void sendMail(final Employee from, final String toName, final String toEmail, final String subject,
			final String message) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {

				String fromName = "Admin";
				String fromEmail = from.getOfficialEmail();

				mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmail, toName));

				mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
				mimeMessage.setSubject(subject);
				InternetHeaders headers = new InternetHeaders();
				headers.addHeader("Content-type", "text/html; charset=UTF-8");

				MimeBodyPart body = new MimeBodyPart(headers, message.getBytes("UTF-8"));
				MimeMultipart multipart = new MimeMultipart();
				multipart.addBodyPart(body);
				mimeMessage.setContent(multipart);
			}
		};

		EmployeeService.this.mailSender.send(preparator);
	}

	/**
	 * Reset user's password based on a previous request. The token will be verified
	 * on this service
	 * 
	 * @param email    user's email
	 * @param token    token received via email in the rest process. This will be
	 *                 verified against what was sent previously
	 * @param password the password
	 * @param firmName the firm name
	 * @param request  the request
	 * @return RestResponse with a success or error message
	 * @throws ServiceException
	 */
	public void updatePassword(EmployeeRegistrationWrapper wrapper, HttpServletRequest request)
			throws ServiceException {

		Employee employee = findByEmail(wrapper.getOfficialEmail());
		if (employee == null)
			throw new ServiceException("Email not found");

		if (wrapper.getToken() != null && !wrapper.getToken().equals(employee.getRecoveryPassword()))
			throw new ServiceException("This link has expired.");

		if (wrapper.getPassword() == null)
			throw new ServiceException("Password not present or Invalid.");

		employee.setRecoveryPassword(UserUtils.randomPassword());
		employee.setPassword(wrapper.getEncodedPassword());
		employee.setLoginAttempts(0);
		employee.setAccountUnlockTime(null);
		try {
			employeeRepository.save(employee);
			return;
		} catch (Exception e) {
			throw new ServiceException("Error while updating the password.");
		}
	}

	/**
	 * Fetches the number of leaves taken by an employee
	 *
	 * @param Employee id
	 * @param Start    date
	 * @param End      date
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public List<ExcelReportWrapper> getJoineeBetween(String startDateString, String endDateString)
			throws ServiceException, ParseException {
		Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(startDateString);
		Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(endDateString);

		if (startDate == null || endDate == null)
			throw new ServiceException("Cannot parse date. Please send in MM-dd-yyyy format.");

		List<ExcelReportWrapper> reportWrappers = new ArrayList<>();

		List<Employee> employees = employeeRepository.findByJoiningDateBetween(DateUtil.resetTime(startDate),
				DateUtil.maxTime(endDate));

		for (Employee employee : employees) {
			ExcelReportWrapper wrapper = new ExcelReportWrapper();
			Employee manager = null;
			try {
				manager = findById(employee.getManagerId());
			} catch (Exception e) {
			}

			if (manager != null)
				wrapper.setManager(manager);

			wrapper.setEmployee(employee);

			reportWrappers.add(wrapper);
		}

		return reportWrappers;
	}

	/**
	 * Resign, deactivates an Employee and If employee resign through mail then also
	 * create entry in Team's Resignation page by Admin (only for Admin).
	 * 
	 * @param EmployeeRegistrationWrapper
	 * @throws ServiceException the service exception
	 */
	public void resignEmployee(EmployeeRegistrationWrapper wrapper) throws ServiceException {
		
		logger.info("Inside resignEmployee ");

		Employee employee = findById(wrapper.getId());
		
		// convert value BigInteger into boolean because this function return
		// BigInterger isEmployeeInResignationTable
		BigInteger countResult = employeeRepository.isEmployeeInResignationTable(wrapper.getId());
		boolean response = countResult.intValue() > 0;

		employee.setResignationDate(wrapper.getResignationDate());
		employee.setResignationReason(wrapper.getResignationReason());
		employee.setIsActive(false);
		employee.setDeleted(true);

		if (response == false) {
			Resignation resignation = new Resignation();
			resignation.setEmployee(employee);
			resignation.setTypeId(1L);
			resignation.setReasonId(5L);
			resignation.setResignationText("Resigned by Mail");

			resignationService.addResignation(resignation);
		}

		// List<Employee> employees =
		// employeeRepositorSy.findAllByManagerId(employee.getId());
		// for (Employee emp : employees) {
		// emp.setManagerId(null);
		// employeeRepository.save(emp);
		// }
		logger.info("Employee Resigned Successfully by Admin employeeEmpCode: {}" + employee.getEmpCode());
		
		employeeRepository.save(employee);
	}

	/**
	 * Set employee whether S/he read policy
	 *
	 * @param policyAccepted flag
	 * @throws ServiceException the service exception
	 */
	public void setEmployeePolicy(Employee employee, boolean policyAccepted) throws ServiceException {
		employee.setPolicyAccepted(policyAccepted);
		employeeRepository.save(employee);
	}

	public EmployeeService() {
		// TODO Auto-generated constructor stub
	}

	private void validateEmployee(EmployeeRegistrationWrapper employeeWrapper) throws ServiceException {
		checkIfEmailExists(employeeWrapper.getOfficialEmail());
		checkIfEmployeeCodeExists(employeeWrapper.getEmpCode());
		//check bank acoount number already exist or not
		checkIsBankAccountAlreadyExists(employeeWrapper.getBankAccountNumber());
	}

	/**
	 * Will return true only if employee is only an employee. In case employee is
	 * manager,hr,admin return will be false.
	 * 
	 * @param empId
	 * @return
	 * @throws ServiceException
	 */
	public Boolean isOnlyEmployee() throws ServiceException {
		Boolean decision = false;
		Employee employee = authenticationService.getAuthenticatedEmployee();

		if (employeeRepository.findAllByManagerId(employee.getId()).isEmpty() && !authenticationService.isHrOrAdmin())
			decision = true;

		return decision;
	}

	public String registerAll(FileUploadWrapper fileUploadWrapper) throws ServiceException {
		// System.out.println(fileUploadWrapper.getFileContent());
		if (!authenticationService.isHrOrAdmin())
			throw new ServiceException("Only a HR or an admin can upload employee data.");
		if (fileUploadWrapper.getFileContent() == null)
			throw new ServiceException("File content should not be empty.");
		final Integer TAGS_ROW = 0;
		Workbook workbook;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(fileUploadWrapper.getFileContent().getBytes());

			if (fileUploadWrapper.getFileContent().getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);
			} else if (fileUploadWrapper.getFileContent().getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new ServiceException("Received file does not have a standard excel extension.");
			}

			List<String> tags = new ArrayList<>();

			LinkedHashMap<String, String> empEmailManagerMap = new LinkedHashMap<>();

			for (Row row : workbook.getSheetAt(0)) {
				Iterator<Cell> cellIterator = row.cellIterator();

				if (row.getRowNum() == TAGS_ROW) {
					while (cellIterator.hasNext())
						tags.add(cellIterator.next().getStringCellValue());
					// for(String x : tags)
					// System.out.println(x);
				} else if (row.getRowNum() > TAGS_ROW) {
					EmployeeRegistrationWrapper employeeRegistrationWrapper = new EmployeeRegistrationWrapper();
					LeaveAccount tempLeaveAccount = new LeaveAccount();
					Float carryForwarded = 0f;
					NomineeWrapper nomineeDetails = new NomineeWrapper();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();

						try {
							employeeRegistrationWrapper = addEmployeeEntry(employeeRegistrationWrapper,
									tempLeaveAccount, nomineeDetails, tags.get(cell.getColumnIndex()), cell,
									empEmailManagerMap);
							if (tags.get(cell.getColumnIndex()).toUpperCase()
									.equalsIgnoreCase("PREVIOUS YEAR CARRY FORWARDED*")) {
								carryForwarded = (float) cell.getNumericCellValue();
							}
						} catch (IllegalStateException | NumberFormatException e) {
						}
					}
					employeeRegistrationWrapper.setNomineeDetails(nomineeDetails);
					try {
						saveEmployee(employeeRegistrationWrapper);
						Employee employee = findByEmail(employeeRegistrationWrapper.getOfficialEmail());
						createEmployeeLeaveAccountIfNotExist(employee);
						Integer currentYear = DateUtil.getFinancialYear(new LocalDate());
						LeaveAccount leaveAccount = leaveAccountService.findByEmpIdAndYear(employee.getId(),
								currentYear);
						leaveAccount.setAvailedCasualLeave(tempLeaveAccount.getAvailedCasualLeave());
						leaveAccount.setAvailedSickLeave(tempLeaveAccount.getAvailedSickLeave());
						if (employee.getLevelChangedFromZero() != null) {
							leaveAccount.setAvailedPrivilegeLeave(tempLeaveAccount.getAvailedPrivilegeLeave());
							leaveAccount.setTotalPrivilegeLeave(leaveAccount.getTotalPrivilegeLeave() + carryForwarded);
						}
						leaveAccountService.saveAccount(leaveAccount);
					} catch (ServiceException e) {
						throw new ServiceException("ROW: " + row.getRowNum() + ". " + e.getMessage());
					}
				}
			}

			List<String> errorEntries = new ArrayList<>();
			// Now Processing the managers.
			for (Map.Entry<String, String> e : empEmailManagerMap.entrySet()) {
				Employee employee = employeeRepository.findByEmpCodeAndDeleted(e.getKey(), false);
				Employee manager = employeeRepository.findByEmpCodeAndDeleted(e.getValue(), false);
				if (employee == null || manager == null) {
					errorEntries.add(e.getKey());
					continue;
				}

				int empLevel = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);
				int managerLevel = Integer.parseInt(manager.getDesignation().getLevel().split("_")[1]);

				// Leaving management department
				if (empLevel >= managerLevel
						&& employee.getDesignation().getDepartment().getDeptId().longValue() != 9l) {
					errorEntries.add(e.getKey());
					continue;
				}

				employee.setManagerId(manager.getId());
				employeeRepository.saveAndFlush(employee);

			}

			if (errorEntries.size() != 0) {
				String allError = "";
				for (String error : errorEntries) {
					allError = allError + error + ", ";
				}

				throw new ServiceException("Can't process assigning manager for " + allError + " emp code.");
			}

		} catch (Exception e) {
			throw new ServiceException("Unable to upload: " + e.getMessage());
		}

		return "Done";
	}

	private EmployeeRegistrationWrapper addEmployeeEntry(EmployeeRegistrationWrapper employeeRegistrationWrapper,
			LeaveAccount tempLeaveAccount, NomineeWrapper nomineeWrapper, String tag, Cell cell,
			LinkedHashMap<String, String> empEmailManagerMap) throws ServiceException, SerialException {
		employeeRegistrationWrapper.setCompanyId(1);
		employeeRegistrationWrapper.setPassword("Witty$1234");
		employeeRegistrationWrapper.setIsActive(true);
		employeeRegistrationWrapper.setDeleted(false);
		switch (tag.toUpperCase()) {
		case "EMPLOYEE CODE*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "EMPLOYEE CODE* is empty");

			employeeRegistrationWrapper.setEmpCode(cell.getStringCellValue());
			break;

		case "FIRST NAME*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "FIRST NAME* is empty");

			employeeRegistrationWrapper.setFirstName(cell.getStringCellValue());
			break;

		case "MIDDLE NAME":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setMiddleName(cell.getStringCellValue());
			break;

		case "LAST NAME*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "LAST NAME* is empty");

			employeeRegistrationWrapper.setLastName(cell.getStringCellValue());
			break;

		case "FATHER'S NAME":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setFatherName(cell.getStringCellValue());
			break;

		case "DATE OF BIRTH*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "DATE OF BIRTH* is empty");

			employeeRegistrationWrapper
					.setDob(DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(cell.getStringCellValue()));
			;
			break;

		case "GENDER*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "GENDER* is empty");

			employeeRegistrationWrapper.setGender(cell.getStringCellValue());
			;
			break;

		case "MARITAL STATUS*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "MARITAL STATUS* is empty");

			employeeRegistrationWrapper.setMaritalStatus(MaritalStatus.valueOf(cell.getStringCellValue()));
			break;

		case "PRIMARY PHONE*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "PRIMARY PHONE* is empty");

			if (cell.getStringCellValue().length() != 10)
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "PRIMARY PHONE* length error.");

			employeeRegistrationWrapper.setPrimaryPhone(cell.getStringCellValue());
			break;

		case "SECONDARY PHONE*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "SECONDARY PHONE* is empty");

			if (cell.getStringCellValue().length() != 10)
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "SECONDARY PHONE* length error.");

			employeeRegistrationWrapper.setSecondaryPhone(cell.getStringCellValue());
			break;

		case "RELIGION*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "RELIGION* is empty");

			employeeRegistrationWrapper.setReligion(cell.getStringCellValue());
			break;

		case "BLOOD GROUP":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setBloodGroup(cell.getStringCellValue().toUpperCase());
			break;

		case "PRESENT ADDRESS*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "PRESENT ADDRESS* is empty");

			employeeRegistrationWrapper.setPresentAddress(cell.getStringCellValue());
			break;

		case "PERMANENT ADDRESS*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "PERMANENT ADDRESS* is empty");

			employeeRegistrationWrapper.setPermanentAddress(cell.getStringCellValue());
			break;

		case "HOBBIES":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setHobbies(cell.getStringCellValue());
			break;

		case "DATE OF JOINING*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "DATE OF JOINING* is empty");

			DateTime joiningDate = DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(cell.getStringCellValue());

			if (joiningDate.isAfterNow())
				throw new ServiceException(
						"ROW: " + cell.getRowIndex() + ". " + "Joining date should be before current date and time.");

			employeeRegistrationWrapper.setJoiningDate(joiningDate);
			break;

		case "ROLE TYPE*":

			if (cell.getNumericCellValue() == 0)
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "ROLE TYPE* is empty");

			employeeRegistrationWrapper.setRoleId((int) cell.getNumericCellValue());
			Role role = roleService.findById(employeeRegistrationWrapper.getRoleId());
			employeeRegistrationWrapper.setRole(role.getRoleName());
			break;
		//
		// case "DEPARTMENT*":
		//
		// if(cell.getNumericCellValue() == 0)
		// throw new ServiceException("ROW: " + cell.getRowIndex() + ". " +
		// "DEPARTMENT* is empty");
		//
		// if(employeeRegistrationWrapper.getRoleId() == 2) {
		// if((int) cell.getNumericCellValue() != 5 && (int)
		// cell.getNumericCellValue() != 9)
		// throw new ServiceException("ROW: " + cell.getRowIndex() + ". " +
		// "Employee having role admin should be of department Human Resource or
		// Management");
		// }else {
		// if((int) cell.getNumericCellValue() == 9)
		// throw new ServiceException("ROW: " + cell.getRowIndex() + ". " +
		// "Role type employee can't have departmant Management");
		// }
		// employeeRegistrationWrapper.setDeptId((int)
		// cell.getNumericCellValue());
		// break;

		case "DESIGNATION*":

			if (cell.getNumericCellValue() == 0)
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "DESIGNATION* is empty");
			List<Long> desigForAdminList = new ArrayList<>();
			desigForAdminList.add(33l);
			desigForAdminList.add(34l);
			desigForAdminList.add(35l);
			desigForAdminList.add(36l);
			desigForAdminList.add(48l);
			desigForAdminList.add(49l);
			if (employeeRegistrationWrapper.getRoleId() == 2) {
				if (!desigForAdminList.contains((long) cell.getNumericCellValue()))
					throw new ServiceException("ROW: " + cell.getRowIndex() + ". "
							+ "Designations available for admin are HR Manager, Senior HR Manager, HR Head, VP, CTO, CEO");
			} else {
				if ((int) cell.getNumericCellValue() == 48 || (int) cell.getNumericCellValue() == 49)
					throw new ServiceException(
							"ROW: " + cell.getRowIndex() + ". " + "Role type employee can't have designation CTO,CEO");
			}
			Designation designation = designationService.findById((long) cell.getNumericCellValue());
			// if(designation.getDepartment().getDeptId() !=
			// employeeRegistrationWrapper.getDeptId())
			// throw new ServiceException("ROW: " + cell.getRowIndex() + ". " +
			// "Designation: " + (long) cell.getNumericCellValue() + " does not
			// belong to department: " +
			// employeeRegistrationWrapper.getDeptId());
			employeeRegistrationWrapper.setDeptId(designation.getDepartment().getDeptId());
			employeeRegistrationWrapper.setDesignationId((long) cell.getNumericCellValue());
			employeeRegistrationWrapper.setDepartmentName(designation.getDepartment().getDeptName());
			employeeRegistrationWrapper.setDesignation(designation.getDesignation());
			break;

		case "MANAGER EMPCODE*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			// if (cell.getStringCellValue() == null ||
			// cell.getStringCellValue().isEmpty())
			// throw new ServiceException("ROW: " + cell.getRowIndex() + ". " +
			// "MANAGER EMPCODE* is empty");

			// As manager is no more mandatory.
			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				break;

			// Employee manager =
			// employeeRepository.findByEmpCodeAndDeleted(cell.getStringCellValue(),
			// false);
			// if (manager == null)
			// throw new ServiceException("ROW: " + cell.getRowIndex() + ". "
			// + "Manager emp code is invalid or you have to first register
			// manager then its team. Please put manager details before his
			// teams row.");

			empEmailManagerMap.put(employeeRegistrationWrapper.getEmpCode(), cell.getStringCellValue());
			// employeeRegistrationWrapper.setManagerId(manager.getId());
			break;

		case "LOCATION*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "LOCATION* is empty");

			employeeRegistrationWrapper.setLocation(cell.getStringCellValue());
			break;

		case "NATIONALITY*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "NATIONALITY* is empty");

			employeeRegistrationWrapper.setNationality(cell.getStringCellValue());
			break;

		case "PERSONAL EMAIL ID":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setPersonalEmail(cell.getStringCellValue());
			break;

		case "OFFICIAL EMAIL ID*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "OFFICIAL EMAIL ID* is empty");

			employeeRegistrationWrapper.setOfficialEmail(cell.getStringCellValue());
			break;

		case "BANK NAME*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "BANK NAME* is empty");

			employeeRegistrationWrapper.setBankName(cell.getStringCellValue());
			break;

		case "BANK ACCOUNT NO*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "BANK ACCOUNT NO* is empty");

			employeeRegistrationWrapper.setBankAccountNumber(cell.getStringCellValue());
			break;

		case "IFSC*":
			cell.setCellType(Cell.CELL_TYPE_STRING);

			if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
				throw new ServiceException("ROW: " + cell.getRowIndex() + ". " + "IFSC* is empty");

			employeeRegistrationWrapper.setBankIfsc(cell.getStringCellValue());
			break;

		case "AADHAR CARD NUMBER":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setAadhar(cell.getStringCellValue());
			break;

		case "UAN NUMBER":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setUanNumber(cell.getStringCellValue());
			break;

		case "PAN CARD NUMBER":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			employeeRegistrationWrapper.setPanNumber(cell.getStringCellValue());
			break;

		case "NOMINEE NAME":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			nomineeWrapper.setNomineeName(cell.getStringCellValue());
			break;

		case "RELATION WITH NOMINEE":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			nomineeWrapper.setNomineeRelation(cell.getStringCellValue());
			break;

		case "PHONE NO OF NOMINEE":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			nomineeWrapper.setNomineePhone(cell.getStringCellValue());
			break;

		case "EMAIL ID OF NOMINEE":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			nomineeWrapper.setNomineeEmail(cell.getStringCellValue());
			break;

		case "PERMANENT ADDRESS OF NOMINEE":
			cell.setCellType(Cell.CELL_TYPE_STRING);
			nomineeWrapper.setNomineePermanentAddress(cell.getStringCellValue());
			break;

		case "AVAILED CASUAL LEAVE*":
			tempLeaveAccount.setAvailedCasualLeave((float) cell.getNumericCellValue());
			break;

		case "AVAILED SICK LEAVE*":
			tempLeaveAccount.setAvailedSickLeave((float) cell.getNumericCellValue());
			break;

		case "AVAILED PRIVILEGE LEAVE*":
			tempLeaveAccount.setAvailedPrivilegeLeave((float) cell.getNumericCellValue());
			break;

		default:
			break;
		}
		return employeeRegistrationWrapper;
	}
	/**
	 * Activate or Deactivate Employees
	 * @param userId
	 * @param status
	 * @throws ServiceException
	 */
	public void activateOrDeactivateEmployee(Long userId, boolean status) throws ServiceException {
		
		logger.info("Activating employee : {} " + userId);
		
		Employee employee = findByEmployeeId(userId);
		
//		boolean isActive = "activate".equalsIgnoreCase(status);
		employee.setIsActive(!status);
		employee.setDeleted(status);
		
		logger.info("Employee Id {}" +userId+ " Activated Successfully ");
		
		employeeRepository.save(employee);
	}

	/**
	 * Check bank account number already exist or not
	 * 
	 * @param bankAccountNumber
	 * @throws ServiceException
	 */
	public void checkIsBankAccountAlreadyExists(String bankAccountNumber) throws ServiceException {
		logger.info("inside checkIsBankAccountAlreadyExists");
		if (employeeRepository.isBankAccountNumberAlreadyExistOrNot(bankAccountNumber))
			throw new ServiceException("Bank account number already exists.");
	}
	
}
