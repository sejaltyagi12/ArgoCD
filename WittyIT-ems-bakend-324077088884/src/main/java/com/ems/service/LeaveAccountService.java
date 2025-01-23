package com.ems.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Employee;
import com.ems.domain.LeaveAccount;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.LeaveAccountRepository;
import com.ems.servicefinder.utils.Constants;
import com.ems.servicefinder.utils.UserUtils;

@Service
@Transactional
public class LeaveAccountService {

	@Autowired
	private LeaveAccountRepository leaveAccountRepository;

	@Autowired
	private LeaveService leaveService;

	/**
	 * Find leave account by id.
	 *
	 * @param id
	 * 
	 * @return the LeaveAccount
	 */
	public LeaveAccount findById(Long id) {
		return leaveAccountRepository.findById(id);
	}

	/**
	 * Find leave account by employee id and year.
	 *
	 * @param employeeId
	 * @param year
	 * 
	 * @return the LeaveAccount
	 */
	public LeaveAccount findByEmpIdAndYear(Long id, Integer year) {
		return leaveAccountRepository.findByEmpIdAndYear(id, year);
	}

	/**
	 * Saves a leave account.
	 *
	 * @param leaveAccount
	 * 
	 */
	public void saveAccount(LeaveAccount leaveAccount) {
		leaveAccountRepository.save(leaveAccount);
	}

	public void deleteByEmpId(Long empId) {
		leaveAccountRepository.deleteByEmpId(empId);
	}

	/**
	 * Add leave Account If Not Exists
	 * 
	 * @throws ServiceException
	 *
	 * 
	 */
	public void addLeaveAccount(Employee employee, Integer year, boolean updatePrivileges) {

		Integer currentYear = year;

		LeaveAccount leaveAccount = null;
		try {
			leaveAccount = leaveAccountRepository.findByEmpIdAndYear(employee.getId(), currentYear);

			if (leaveAccount == null) {
				leaveAccount = new LeaveAccount(employee.getId(), currentYear, 0f, 0f, 0f,
						(float) getEmployeeRemainsCasualLeaves(employee, year),
						(float) getEmployeeRemainsSickLeaves(employee, year),
						getTotalPrivilegeLeave(employee, currentYear));
				leaveAccountRepository.save(leaveAccount);
			} else if (updatePrivileges) {
				float totalPrivilegesThisYear = getTotalPrivilegeLeave(employee, currentYear);
				if (leaveAccount.getTotalPrivilegeLeave() != totalPrivilegesThisYear) {
					leaveAccount.setTotalPrivilegeLeave(totalPrivilegesThisYear);
					leaveAccountRepository.save(leaveAccount);
				}
			}
		} catch (Exception e) {
			System.out.println("2 Leave Account creation problem handled.");
		}
	}

	/**
	 * Get total privilege leave.
	 * 
	 * @param employee
	 * @param currentYear
	 * @return total privilege leave.
	 */
	private float getTotalPrivilegeLeave(Employee employee, int currentYear) {
		Float totalprivilegeLeaveCarryForwarded = (float) employee.getCompanyCategory()
				.getTotalPrivilegeLeaveCarryForwarder();
		LocalDate joiningDateOrDesignationChangeDate = employee.getLevelChangedFromZero() == null
				? new LocalDate(employee.getJoiningDate())
				: employee.getLevelChangedFromZero().toLocalDate();
		DateTime nowDate = new DateTime();
		Integer totalPrivilegeLeavePerYear = employee.getCompanyCategory().getTotalPrivilegeLeave();
		float currentYearTotalPrivalegeLeave = 0f;
		int level = Integer.parseInt(employee.getDesignation().getLevel().split("_")[1]);

		// Considering joining date to calculate PL
		if (level > 0) {
			if (DateUtil.checkIfBothInSameFinancialYear(joiningDateOrDesignationChangeDate, nowDate.toLocalDate())) {
				float months = Months
						.monthsBetween(joiningDateOrDesignationChangeDate,
								DateUtil.getFirstOrLastDateOfFinancialYear(nowDate.toLocalDate(), false))
						.getMonths() + 1;
				currentYearTotalPrivalegeLeave = months * ((float) totalPrivilegeLeavePerYear / 12);
			} else {
				// PL not given for trainee
				currentYearTotalPrivalegeLeave = totalPrivilegeLeavePerYear;
			}
		}

		Float previousYearRemainPrivilegeLeave = 0f;
		if (level > 0
				&& UserUtils.isEmployeeJoiningGreaterThan(employee, new LocalDate(), Constants.CONFORMATION_MONTHS)
				&& DateUtil.getFinancialYear(new LocalDate()) == currentYear) {

			LeaveAccount previousYearLeaveAccount = leaveAccountRepository.findByEmpIdAndYear(employee.getId(),
					currentYear - 1);

			previousYearRemainPrivilegeLeave = previousYearLeaveAccount != null
					? (previousYearLeaveAccount.getTotalPrivilegeLeave()
							- previousYearLeaveAccount.getAvailedPrivilegeLeave())
					: previousYearRemainPrivilegeLeave;
		}

		return currentYearTotalPrivalegeLeave + (previousYearRemainPrivilegeLeave > totalprivilegeLeaveCarryForwarded
				? totalprivilegeLeaveCarryForwarded
				: previousYearRemainPrivilegeLeave);
	}
	// public void addLeaveAccount(Employee employee) {
	//
	// Integer currentYear = new DateTime().getYear();
	//
	// LeaveAccount leaveAccount =
	// leaveAccountRepository.findByEmpIdAndYear(employee.getId(), currentYear);
	//
	// if (leaveAccount == null) {
	// Float sickLeave = (float)
	// employee.getCompanyCategory().getTotalSickLeave();
	//
	// Float totalCasualLeave =
	// EmsMathUtils.round(getEmployeeProRataCasualBalance(employee), 1);
	//
	// Float totalSickLeave = EmsMathUtils.round(sickLeave, 1);
	//
	// leaveAccount = new LeaveAccount(employee.getId(), new
	// DateTime().getYear(), totalCasualLeave,
	// totalSickLeave, totalCasualLeave, totalSickLeave);
	//
	// leaveAccountRepository.save(leaveAccount);
	// }
	// }

	/**
	 * Add leave Account If Not Exists
	 * 
	 * @throws ServiceException
	 *
	 * 
	 */
	public void resetLeaveAccount(Employee employee) throws ServiceException {

		Integer currentYear = DateUtil.getFinancialYear(new LocalDate());

		LeaveAccount leaveAccount = leaveAccountRepository.findByEmpIdAndYear(employee.getId(), currentYear);

		if (leaveAccount == null)
			throw new ServiceException("Can't able to fetch accounts. Please contact support team");

		leaveAccount.setTotalCasualLeave(getEmployeeRemainsCasualLeaves(employee, currentYear));
		leaveAccount.setTotalSickLeave(getEmployeeRemainsSickLeaves(employee, currentYear));
		leaveAccount.setTotalPrivilegeLeave(getTotalPrivilegeLeave(employee, currentYear));

		leaveAccount.setAvailedPrivilegeLeave(0f);
		leaveAccount.setAvailedSickLeave(0f);
		leaveAccount.setAvailedCasualLeave(0f);

		leaveService.deleteHistoryByEmpId(employee.getId());

		leaveAccountRepository.save(leaveAccount);
	}

	/**
	 * Will update the PL for an employee in case of designation change.
	 * 
	 * @param employee
	 * @throws ServiceException
	 */
	public void updateLeaveAccountWithPrivilegeLeave(Employee employee) throws ServiceException {
		Integer currentYear = DateUtil.getFinancialYear(new LocalDate());

		LeaveAccount leaveAccount = leaveAccountRepository.findByEmpIdAndYear(employee.getId(), currentYear);

		if (leaveAccount == null)
			throw new ServiceException("Not able to fetch leave account. Please contact support team");

		leaveAccount.setTotalPrivilegeLeave(getTotalPrivilegeLeave(employee, currentYear));
		leaveAccountRepository.save(leaveAccount);
	}

	// /**
	// * Calculates Balance as per joining date
	// *
	// *
	// */
	// public Float getEmployeeProRataCasualBalance(Employee employee) {
	//
	// Float calculatedCasualLeave =
	// employee.getCompanyCategory().getTotalCasualLeave()
	// - getEmployeeProRataCasualLeaves(employee);
	//
	// return calculatedCasualLeave;
	//
	// }
	//
	// /**
	// * Calculates leaves to be deducted as per joining date
	// *
	// *
	// */
	// public Float getEmployeeProRataCasualLeaves(Employee employee) {
	//
	// DateTime employeeJoiningDate = new DateTime(employee.getJoiningDate());
	//
	// if (employeeJoiningDate.getYear() != new DateTime().getYear())
	// return 0f;
	//
	// Float leavesPerMonth = ((float)
	// employee.getCompanyCategory().getTotalCasualLeave() / 12);
	//
	// Float leaveCount = (employeeJoiningDate.getMonthOfYear() - 1) *
	// leavesPerMonth;
	//
	// return leaveCount;
	//
	// }

	/**
	 * Calculates leaves to be deducted as per joining date(Casual Leave)
	 *
	 * 
	 */
	public float getEmployeeRemainsCasualLeaves(Employee employee, int year) {
		LocalDate joiningDate = new LocalDate(employee.getJoiningDate());
		LocalDate localYear = DateUtil.getFirstOrLastDateOfFinancialYear(year, true);
		float totalLeave = employee.getCompanyCategory().getTotalCasualLeave();
		if (DateUtil.checkIfBothInSameFinancialYear(joiningDate, localYear)) {
			LocalDate lastDateOfFinancialYear = DateUtil.getFirstOrLastDateOfFinancialYear(joiningDate, false);
			int months = Months.monthsBetween(joiningDate, lastDateOfFinancialYear).getMonths() + 1;
			totalLeave *= (float) months / 12;
			return totalLeave;
		}
		return totalLeave;
	}

	/**
	 * Calculates leaves to be deducted as per joining date(Sick Leave)
	 *
	 * @author Sarit Mukherjee.
	 */
	public float getEmployeeRemainsSickLeaves(Employee employee, int year) {
		LocalDate joiningDate = new LocalDate(employee.getJoiningDate());
		LocalDate localYear = DateUtil.getFirstOrLastDateOfFinancialYear(year, true);
		float totalLeave = employee.getCompanyCategory().getTotalSickLeave();
		if (DateUtil.checkIfBothInSameFinancialYear(joiningDate, localYear)) {
			LocalDate lastDateOfFinancialYear = DateUtil.getFirstOrLastDateOfFinancialYear(joiningDate, false);
			int months = Months.monthsBetween(joiningDate, lastDateOfFinancialYear).getMonths() + 1;
			totalLeave *= (float) months / 12;
			return totalLeave;
		}
		return totalLeave;
	}

}
