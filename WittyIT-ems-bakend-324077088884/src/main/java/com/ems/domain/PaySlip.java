package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PaySlip {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long empId;

	@JsonIgnore
	private Float workingDays;

	@JsonIgnore
	private Float basicSalary;

	@JsonIgnore
	private Float hra;

	@JsonIgnore
	private Float medical;

	@JsonIgnore
	private Float specialAllowances;

	@JsonIgnore
	private Float lta;

	@JsonIgnore
	private Float arrear;

	@JsonIgnore
	private Float vehicleRunning;

	@JsonIgnore
	private Float attireAllowances;

	@JsonIgnore
	private Float vehicleReimburse;

	@JsonIgnore
	private Float telephoneReimburse;

	@JsonIgnore
	private Float pf;

	@JsonIgnore
	private Float esi;

	@JsonIgnore
	private Float deductionTds;

	@JsonIgnore
	private Float deductionLeave;

	@JsonIgnore
	private Float absent;

	@JsonIgnore
	private Float otherDeductions;

	@JsonIgnore
	private Float otherAllowances;

	@JsonIgnore
	private Float transportAllowances;

	@JsonIgnore
	private Float driverAllowances;

	@JsonIgnore
	private Float bonus;

	@JsonIgnore
	private Float variableBonus;

	@JsonIgnore
	private Float businessPromotion;

	@JsonIgnore
	private Float loan;

	private Integer month;

	private Integer year;

	@JsonIgnore
	@Transient
	private String empName;

	@JsonIgnore
	@Transient
	private String empCode;

	public PaySlip(Long empId, Integer month, Integer year) {
		this.empId = empId;
		this.month = month;
		this.year = year;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Float getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(Float workingDays) {
		this.workingDays = workingDays;
	}

	public Float getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Float basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Float getHra() {
		return hra;
	}

	public void setHra(Float hra) {
		this.hra = hra;
	}

	public Float getMedical() {
		return medical;
	}

	public void setMedical(Float medical) {
		this.medical = medical;
	}

	public Float getSpecialAllowances() {
		return specialAllowances;
	}

	public void setSpecialAllowances(Float specialAllowances) {
		this.specialAllowances = specialAllowances;
	}

	public Float getLta() {
		return lta;
	}

	public void setLta(Float lta) {
		this.lta = lta;
	}

	public Float getVehicleRunning() {
		return vehicleRunning;
	}

	public void setVehicleRunning(Float vehicleRunning) {
		this.vehicleRunning = vehicleRunning;
	}

	public Float getAttireAllowances() {
		return attireAllowances;
	}

	public void setAttireAllowances(Float attireAllowances) {
		this.attireAllowances = attireAllowances;
	}

	public Float getVehicleReimburse() {
		return vehicleReimburse;
	}

	public void setVehicleReimburse(Float vehicleReimburse) {
		this.vehicleReimburse = vehicleReimburse;
	}

	public Float getTelephoneReimburse() {
		return telephoneReimburse;
	}

	public void setTelephoneReimburse(Float telephoneReimburse) {
		this.telephoneReimburse = telephoneReimburse;
	}

	public Float getPf() {
		return pf;
	}

	public void setPf(Float pf) {
		this.pf = pf;
	}

	public Float getEsi() {
		return esi;
	}

	public void setEsi(Float esi) {
		this.esi = esi;
	}

	public Float getDeductionTds() {
		return deductionTds;
	}

	public void setDeductionTds(Float deductionTds) {
		this.deductionTds = deductionTds;
	}

	public Float getDeductionLeave() {
		return deductionLeave;
	}

	public void setDeductionLeave(Float deductionLeave) {
		this.deductionLeave = deductionLeave;
	}

	public Float getAbsent() {
		return absent;
	}

	public void setAbsent(Float absent) {
		this.absent = absent;
	}

	public Float getOtherDeductions() {
		return otherDeductions;
	}

	public void setOtherDeductions(Float otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Float getArrear() {
		return arrear;
	}

	public void setArrear(Float arrear) {
		this.arrear = arrear;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Float getOtherAllowances() {
		return otherAllowances;
	}

	public void setOtherAllowances(Float otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	public Float getDriverAllowances() {
		return driverAllowances;
	}

	public void setDriverAllowances(Float driverAllowances) {
		this.driverAllowances = driverAllowances;
	}

	public Float getBonus() {
		return bonus;
	}

	public void setBonus(Float bonus) {
		this.bonus = bonus;
	}

	public Float getVariableBonus() {
		return variableBonus;
	}

	public void setVariableBonus(Float variableBonus) {
		this.variableBonus = variableBonus;
	}

	public Float getBusinessPromotion() {
		return businessPromotion;
	}

	public void setBusinessPromotion(Float businessPromotion) {
		this.businessPromotion = businessPromotion;
	}

	/**
	 * @return the transportAllowances
	 */
	public Float getTransportAllowances() {
		return transportAllowances;
	}

	/**
	 * @param transportAllowances
	 *            the transportAllowances to set
	 */
	public void setTransportAllowances(Float transportAllowances) {
		this.transportAllowances = transportAllowances;
	}

	/**
	 * @return the loan
	 */
	public Float getLoan() {
		return loan;
	}

	/**
	 * @param loan
	 *            the loan to set
	 */
	public void setLoan(Float loan) {
		this.loan = loan;
	}

	public PaySlip() {
	}

	public PaySlip(PayrollFixed payrollFixed, PayrollVariable payrollVariable) {
		if (payrollFixed != null) {
			this.basicSalary = payrollFixed.getBasicPay();
			this.hra = payrollFixed.getHra();
			this.medical = payrollFixed.getMedicalReimbursement();
			this.specialAllowances = payrollFixed.getSpecialAllowance();
			this.businessPromotion = payrollFixed.getBusinessPromotion();
			this.lta = payrollFixed.getLta();
			this.vehicleReimburse = payrollFixed.getVehicleReimbursement();
			this.attireAllowances = payrollFixed.getAttireAllowance();
			this.driverAllowances = payrollFixed.getDriverAllowance();
			this.bonus = payrollFixed.getFixedBonus();
			this.deductionTds = payrollFixed.getTaxes();
			this.vehicleRunning = payrollFixed.getVehicleRunning();
			this.telephoneReimburse = payrollFixed.getTelReimbursement();
		}

		if (payrollVariable != null) {
			this.variableBonus = payrollVariable.getBonus();
			this.arrear = payrollVariable.getArrear();
			this.otherDeductions = payrollVariable.getOtherDeductions();
			this.otherAllowances = payrollVariable.getOtherAllowances();

			if (payrollVariable.getTaxes() != null)
				this.deductionTds = payrollVariable.getTaxes();
		}
	}

}
