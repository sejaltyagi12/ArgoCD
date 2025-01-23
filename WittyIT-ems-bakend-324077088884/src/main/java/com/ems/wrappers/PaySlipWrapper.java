package com.ems.wrappers;

import java.text.NumberFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import com.ems.domain.CompanyCategory;
import com.ems.domain.Employee;
import com.ems.domain.PaySlip;
import com.ems.servicefinder.utils.NumberToWords;
import com.ems.servicefinder.utils.UserUtils;

public class PaySlipWrapper {
	private String campanyName;
	private String campanyAddress1;
	private String campanyAddress2;
	private String campanyCINNum;
	private String campanyContactNumber;

	private String paySlipMonth;

	private String employeeCode;
	private String employeeName;
	private String joiningDate;
	private String bankName;
	private String bankAccountNumber;
	private String location;
	private String panNumber;
	private String uanNumber;
	private String designation;

	private int monthDays;
	private int paidDays;

	private long basicSalary;
	private long hra;
	private long specialAllowances;
	private long transportAllowances;
	private long pf;
	private long esi;
	private long tds;
	private long leave;
	private long otherDeductions;
	private long loan;

	private long totalEarning;
	private long totalDeduction;

	private String netPay;

	public PaySlipWrapper() {
	}

	public PaySlipWrapper(Employee employee, PaySlip paySlip) {
		CompanyCategory companyCategory = employee.getCompanyCategory();
		this.campanyName = companyCategory.getCompanyName();
		this.campanyAddress1 = "H.O- #205, Dwarkashish Building, Shakarpur, Delhi-110092";
		this.campanyAddress2 = "B.O- A-12, Second Floor, Sector-9, Noida-201301";
		this.campanyCINNum = "U72200DL2012PTC234788";
		this.campanyContactNumber = "0120-6402908";
		this.paySlipMonth = new YearMonth(paySlip.getYear(), paySlip.getMonth()).monthOfYear()
				.getAsText(new Locale("en")) + " " + paySlip.getYear();
		this.employeeCode = employee.getEmpCode();
		this.employeeName = employee.getFullName() != null ? employee.getFullName().toUpperCase()
				: employee.getFullName();
		this.joiningDate = UserUtils.dateTimeFormat(new DateTime(employee.getJoiningDate()), "dd/MM/YYYY");
		this.bankName = employee.getBankName()!=null?employee.getBankName().toUpperCase():employee.getBankName();
		this.bankAccountNumber = employee.getBankAccountNumber();
		this.location = employee.getLocation() != null ? employee.getLocation().toUpperCase() : employee.getLocation();
		this.panNumber = employee.getPanNumber()!=null?employee.getPanNumber().toUpperCase():employee.getPanNumber();
		this.uanNumber = employee.getUanNumber()!=null?employee.getUanNumber().toUpperCase():employee.getUanNumber();
		this.designation = employee.getDesignation().getDesignation() != null
				? employee.getDesignation().getDesignation().toUpperCase() : employee.getDesignation().getDesignation();
		this.monthDays = new LocalDate(paySlip.getYear(), paySlip.getMonth(), 1).dayOfMonth().getMaximumValue();
		this.paidDays = paySlip.getWorkingDays() == null ? 0 : paySlip.getWorkingDays().intValue();
		this.basicSalary = paySlip.getBasicSalary() == null ? 0 : paySlip.getBasicSalary().longValue();
		this.hra = paySlip.getHra() == null ? 0 : paySlip.getHra().longValue();
		this.specialAllowances = paySlip.getSpecialAllowances() == null ? 0
				: paySlip.getSpecialAllowances().longValue();
		this.transportAllowances = paySlip.getTransportAllowances() == null ? 0
				: paySlip.getTransportAllowances().longValue();
		this.pf = paySlip.getPf() == null ? 0 : paySlip.getPf().longValue();
		this.esi = paySlip.getEsi() == null ? 0 : paySlip.getEsi().longValue();
		this.tds = paySlip.getDeductionTds() == null ? 0 : paySlip.getDeductionTds().longValue();
		this.leave = paySlip.getDeductionLeave() == null ? 0 : paySlip.getDeductionLeave().longValue();
		this.otherDeductions = paySlip.getOtherDeductions() == null ? 0 : paySlip.getOtherDeductions().longValue();
		this.loan = paySlip.getLoan() == null ? 0 : paySlip.getLoan().longValue();

		this.totalEarning = calculateTotalEarning();
		this.totalDeduction = calculateTotalDeduction();
		this.netPay = calculateNetPay();
	}

	private long calculateTotalEarning() {
		return (basicSalary + hra + specialAllowances + transportAllowances);
	}

	private long calculateTotalDeduction() {
		return (pf + esi + tds + leave + otherDeductions + loan);
	}

	private String calculateNetPay() {
		long amount = totalEarning - totalDeduction;
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		return formatter.format(amount) + " (RUPEES " + NumberToWords.convert(amount).toUpperCase() + " ONLY).";
	}

	/**
	 * @return the campanyName
	 */
	public String getCampanyName() {
		return campanyName;
	}

	/**
	 * @param campanyName
	 *            the campanyName to set
	 */
	public void setCampanyName(String campanyName) {
		this.campanyName = campanyName;
	}

	/**
	 * @return the campanyAddress1
	 */
	public String getCampanyAddress1() {
		return campanyAddress1;
	}

	/**
	 * @param campanyAddress1
	 *            the campanyAddress1 to set
	 */
	public void setCampanyAddress1(String campanyAddress1) {
		this.campanyAddress1 = campanyAddress1;
	}

	/**
	 * @return the campanyAddress2
	 */
	public String getCampanyAddress2() {
		return campanyAddress2;
	}

	/**
	 * @param campanyAddress2
	 *            the campanyAddress2 to set
	 */
	public void setCampanyAddress2(String campanyAddress2) {
		this.campanyAddress2 = campanyAddress2;
	}

	/**
	 * @return the campanyCINNum
	 */
	public String getCampanyCINNum() {
		return campanyCINNum;
	}

	/**
	 * @param campanyCINNum
	 *            the campanyCINNum to set
	 */
	public void setCampanyCINNum(String campanyCINNum) {
		this.campanyCINNum = campanyCINNum;
	}

	/**
	 * @return the campanyContactNumber
	 */
	public String getCampanyContactNumber() {
		return campanyContactNumber;
	}

	/**
	 * @param campanyContactNumber
	 *            the campanyContactNumber to set
	 */
	public void setCampanyContactNumber(String campanyContactNumber) {
		this.campanyContactNumber = campanyContactNumber;
	}

	/**
	 * @return the paySlipMonth
	 */
	public String getPaySlipMonth() {
		return paySlipMonth;
	}

	/**
	 * @param paySlipMonth
	 *            the paySlipMonth to set
	 */
	public void setPaySlipMonth(String paySlipMonth) {
		this.paySlipMonth = paySlipMonth;
	}

	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode
	 *            the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the joiningDate
	 */
	public String getJoiningDate() {
		return joiningDate;
	}

	/**
	 * @param joiningDate
	 *            the joiningDate to set
	 */
	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	/**
	 * @param bankAccountNumber
	 *            the bankAccountNumber to set
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the panNumber
	 */
	public String getPanNumber() {
		return panNumber;
	}

	/**
	 * @param panNumber
	 *            the panNumber to set
	 */
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	/**
	 * @return the uanNumber
	 */
	public String getUanNumber() {
		return uanNumber;
	}

	/**
	 * @param uanNumber
	 *            the uanNumber to set
	 */
	public void setUanNumber(String uanNumber) {
		this.uanNumber = uanNumber;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the monthDays
	 */
	public int getMonthDays() {
		return monthDays;
	}

	/**
	 * @param monthDays
	 *            the monthDays to set
	 */
	public void setMonthDays(int monthDays) {
		this.monthDays = monthDays;
	}

	/**
	 * @return the paidDays
	 */
	public int getPaidDays() {
		return paidDays;
	}

	/**
	 * @param paidDays
	 *            the paidDays to set
	 */
	public void setPaidDays(int paidDays) {
		this.paidDays = paidDays;
	}

	/**
	 * @return the basicSalary
	 */
	public long getBasicSalary() {
		return basicSalary;
	}

	/**
	 * @param basicSalary
	 *            the basicSalary to set
	 */
	public void setBasicSalary(long basicSalary) {
		this.basicSalary = basicSalary;
	}

	/**
	 * @return the hra
	 */
	public long getHra() {
		return hra;
	}

	/**
	 * @param hra
	 *            the hra to set
	 */
	public void setHra(long hra) {
		this.hra = hra;
	}

	/**
	 * @return the specialAllowances
	 */
	public long getSpecialAllowances() {
		return specialAllowances;
	}

	/**
	 * @param specialAllowances
	 *            the specialAllowances to set
	 */
	public void setSpecialAllowances(long specialAllowances) {
		this.specialAllowances = specialAllowances;
	}

	/**
	 * @return the transportAllowances
	 */
	public long getTransportAllowances() {
		return transportAllowances;
	}

	/**
	 * @param transportAllowances
	 *            the transportAllowances to set
	 */
	public void setTransportAllowances(long transportAllowances) {
		this.transportAllowances = transportAllowances;
	}

	/**
	 * @return the pf
	 */
	public long getPf() {
		return pf;
	}

	/**
	 * @param pf
	 *            the pf to set
	 */
	public void setPf(long pf) {
		this.pf = pf;
	}

	/**
	 * @return the esi
	 */
	public long getEsi() {
		return esi;
	}

	/**
	 * @param esi
	 *            the esi to set
	 */
	public void setEsi(long esi) {
		this.esi = esi;
	}

	/**
	 * @return the tds
	 */
	public long getTds() {
		return tds;
	}

	/**
	 * @param tds
	 *            the tds to set
	 */
	public void setTds(long tds) {
		this.tds = tds;
	}

	/**
	 * @return the leave
	 */
	public long getLeave() {
		return leave;
	}

	/**
	 * @param leave
	 *            the leave to set
	 */
	public void setLeave(long leave) {
		this.leave = leave;
	}

	/**
	 * @return the otherDeductions
	 */
	public long getOtherDeductions() {
		return otherDeductions;
	}

	/**
	 * @param otherDeductions
	 *            the otherDeductions to set
	 */
	public void setOtherDeductions(long otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

	/**
	 * @return the loan
	 */
	public long getLoan() {
		return loan;
	}

	/**
	 * @param loan
	 *            the loan to set
	 */
	public void setLoan(long loan) {
		this.loan = loan;
	}

	/**
	 * @return the totalEarning
	 */
	public long getTotalEarning() {
		return totalEarning;
	}

	/**
	 * @param totalEarning
	 *            the totalEarning to set
	 */
	public void setTotalEarning(long totalEarning) {
		this.totalEarning = totalEarning;
	}

	/**
	 * @return the totalDeduction
	 */
	public long getTotalDeduction() {
		return totalDeduction;
	}

	/**
	 * @param totalDeduction
	 *            the totalDeduction to set
	 */
	public void setTotalDeduction(long totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	/**
	 * @return the netPay
	 */
	public String getNetPay() {
		return netPay;
	}

	/**
	 * @param netPay
	 *            the netPay to set
	 */
	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}

}
